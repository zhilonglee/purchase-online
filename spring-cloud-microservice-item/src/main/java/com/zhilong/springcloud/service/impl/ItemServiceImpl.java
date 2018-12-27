package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.constant.ItemConstant;
import com.zhilong.springcloud.contonst.PurchaseOnlieGlobalConstant;
import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.entity.JsonResult;
import com.zhilong.springcloud.entity.RabbitMessage;
import com.zhilong.springcloud.entity.Seckill;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import com.zhilong.springcloud.entity.enu.ItemStatus;
import com.zhilong.springcloud.entity.to.CartItem;
import com.zhilong.springcloud.entity.to.CartItemTo;
import com.zhilong.springcloud.fegin.OrderFeginClient;
import com.zhilong.springcloud.repository.ItemRepository;
import com.zhilong.springcloud.service.ItemService;
import com.zhilong.springcloud.service.RabbitMqService;
import com.zhilong.springcloud.service.SeckillService;
import com.zhilong.springcloud.utils.CalendarUtils;
import com.zhilong.springcloud.utils.EntityUtils;
import com.zhilong.springcloud.utils.RedisUtils;
import com.zhilong.springcloud.utils.ZKDistributeLockUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    ItemRepository repository;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RabbitMqService rabbitMqService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    ZKDistributeLockUtils zkDistributeLockUtils;

    @Autowired
    OrderFeginClient orderFeginClient;

    @Override
    public List<Item> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Item> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String key = String.format(ItemConstant.ITEMSKEYFORMAT, pageNumber, pageSize);
        List<Item> items = (List<Item>)redisUtils.getObj(key);
        if (items != null) {
            return items;
        } else {
            items = repository.findAllByOrderByCreateDateDesc(pageable).getContent();
            redisUtils.setObj(key,items);
            redisUtils.expire(key,30, RedisUtils.TIME_TO_MINUTES);
        }
        return items;
    }

    @Override
    public List<ItemSimpleTo> findAllSimpleItem(Pageable pageable) {
        return repository.findItemBirefByStatusAndStockNumGreaterThan(ItemStatus.NORMAL,0,pageable).getContent();
    }

    @Override
    public List<ItemSimpleTo> findAllSimpleItemByCategory(Long catId,Pageable pageable) {
        return repository.findItemBirefByStatusAndStockNumCategoryGreaterThan(ItemStatus.NORMAL,catId,0,pageable).getContent();
    }

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    public Item findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Integer  deductItemStockNum(Long id, Integer num) {
        return repository.deductedItemStockNumById(id,num);
    }

    @Override
    public List<Seckill> findRandItem() {
        String key = String.format(ItemConstant.ITEMSLIMITEDSPIKEKEYFORMAT, CalendarUtils.getCurrentDayOfMonth(), CalendarUtils.getCurrentHourOfDay());
        List<Seckill> seckills = (List<Seckill>)redisUtils.getObj(key);
        if (seckills == null) {
            Date startTime = CalendarUtils.getSharpClock();
            Date endTime = CalendarUtils.addHourDate(startTime,1);
            seckills = seckillService.getSeckillItems(startTime,endTime);
            if (seckills == null || seckills.size() <= 0) {
                List<Map<String, Object>> randItem = repository.findRandItem();
                List<ItemSimpleTo> items = EntityUtils.mapCast2Entity(ItemSimpleTo.class, randItem);
                seckills = seckillService.generateSeckillItems(items);
            }
            if (seckills != null) {
                redisUtils.setObj(key, seckills);
                redisUtils.expire(key, 2, RedisUtils.TIME_TO_HOURS);
            }
        }
        return seckills;
    }

    @Override
    public ResponseEntity purchaseSecKill(Long itemId, String username) {
        ResponseEntity responseEntity = null;
        // 1. check stock num. if it is more than 0, then do purchase operation
        Date startTime = CalendarUtils.getSharpClock();
        Date endTime = CalendarUtils.addHourDate(startTime,1);
        Seckill seckill = seckillService.findSeckillByItemIdAndStartTimeAndEndTime(itemId, startTime, endTime);
        if (seckill != null && seckill.getStockNum() > 0 ) {
            zkDistributeLockUtils.getLock();

            // 2. deduct stock
            Seckill seckillInDB = seckillService.deductSeckillStockNumAndSave(seckill, 1);
            if (seckillInDB != null) {
                // 3. generate order
                CartItemTo cartItemTo = new CartItemTo();
                CartItem cartItem = new CartItem();
                cartItem.setId(itemId);
                cartItem.setNum(1);
                cartItemTo.getCartItems().add(cartItem);
                cartItemTo.setUsername(username);
                ResponseEntity orderResponse = orderFeginClient.generateSecKillOrder(cartItemTo);
                if (orderResponse.getStatusCode().equals(HttpStatus.OK)) {
                    responseEntity = ResponseEntity.ok(JsonResult.ok("Rush to purchase successfully!"));
                } else {
                    responseEntity = ResponseEntity.ok(JsonResult.ok("Too Busy, can not consume service."));
                    seckillService.deductSeckillStockNumAndSave(seckill, -1);
                }
            }

            zkDistributeLockUtils.releaseLock();
        } else {
            String key = String.format(ItemConstant.ITEMSLIMITEDSPIKEKEYFORMAT, CalendarUtils.getCurrentDayOfMonth(), CalendarUtils.getCurrentHourOfDay());
            responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("The product has been sold out!");
            List<Seckill> seckills = seckillService.getSeckillItems(startTime, endTime);
            redisUtils.setObj(key, seckills);
            redisUtils.expire(key, 1, RedisUtils.TIME_TO_HOURS);
        }
        return  responseEntity;
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    private void scheduledAddLimitedSpikeItem(){
        logger.info("Scheduled Method --- scheduledAddLimitedSpikeItem()  access");
        findRandItem();

    }
}
