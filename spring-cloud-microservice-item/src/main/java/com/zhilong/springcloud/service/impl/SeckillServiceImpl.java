package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.entity.Seckill;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import com.zhilong.springcloud.repository.SecKillRepository;
import com.zhilong.springcloud.service.SeckillService;
import com.zhilong.springcloud.utils.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    SecKillRepository secKillRepository;

    @Override
    public List<Seckill> generateSeckillItems(List<ItemSimpleTo> items) {
        List<Seckill> seckills = new ArrayList<>();
        items.forEach(item -> {
            Seckill seckill = new Seckill();
            seckill.setCreateTime(new Date());
            Date sharpClock = CalendarUtils.getSharpClock();
            seckill.setStartTime(sharpClock);
            seckill.setEndTime(CalendarUtils.addHourDate(sharpClock,1));
            seckill.setImage(item.getImage());
            seckill.setPrice(item.getPrice());
            seckill.setTitle(item.getTitle());
            seckill.setItem_des(item.getItem_des());
            seckill.setStockNum(10);
            seckill.setItemId(item.getId());
            seckill.setCurrentprice(item.getPrice().multiply(new BigDecimal(0.1)));
            seckills.add(seckill);
        });
        return secKillRepository.saveAll(seckills);
    }

    @Override
    public List<Seckill> getSeckillItems(Date startTime, Date endTime) {
        return secKillRepository.findSeckillsByStartTimeEqualsAndEndTimeEquals(startTime,endTime);
    }

    @Override
    public Seckill findSeckillByItemIdAndStartTimeAndEndTime(Long itemId, Date startTime, Date endTime) {
        return secKillRepository.findSeckillByItemIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(itemId,startTime,endTime);
    }

    @Override
    public Seckill deductSeckillStockNumAndSave(Seckill seckill,Integer num) {
        seckill.setStockNum(seckill.getStockNum() - num);
        return secKillRepository.save(seckill);
    }

}
