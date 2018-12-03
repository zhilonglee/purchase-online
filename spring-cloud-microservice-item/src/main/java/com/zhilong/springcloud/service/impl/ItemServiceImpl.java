package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.constant.ItemConstant;
import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import com.zhilong.springcloud.entity.enu.ItemStatus;
import com.zhilong.springcloud.repository.ItemRepository;
import com.zhilong.springcloud.service.ItemService;
import com.zhilong.springcloud.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository repository;

    @Autowired
    RedisUtils redisUtils;

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
    public List<ItemSimpleTo> findAllSimpleItemByCategory(Integer catId,Pageable pageable) {
        return repository.findItemBirefByStatusAndStockNumCategoryGreaterThan(ItemStatus.NORMAL,catId,0,pageable).getContent();
    }

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }
}
