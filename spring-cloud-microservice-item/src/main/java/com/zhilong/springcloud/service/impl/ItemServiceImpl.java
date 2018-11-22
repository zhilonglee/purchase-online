package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.repository.ItemRepository;
import com.zhilong.springcloud.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository repository;

    @Override
    public List<Item> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return repository.findAllByOrderByCreateDateDesc(pageable);
    }

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }
}
