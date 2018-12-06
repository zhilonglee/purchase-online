package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.entity.ItemCategory;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    List<Item> findAll();

    List<Item> findAll(Pageable pageable);

    List<ItemSimpleTo> findAllSimpleItem(Pageable pageable);

    List<ItemSimpleTo> findAllSimpleItemByCategory(Long catId,Pageable pageable);

    Item save(Item item);

}
