package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    List<Item> findAll();

    Page<Item> findAll(Pageable pageable);

    Item save(Item item);
}
