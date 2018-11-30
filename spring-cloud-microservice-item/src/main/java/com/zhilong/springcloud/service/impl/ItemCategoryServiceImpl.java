package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.entity.ItemCategory;
import com.zhilong.springcloud.repository.ItemCategoryRepository;
import com.zhilong.springcloud.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    ItemCategoryRepository itemCategoryRepository;

    @Override
    public List<ItemCategory> findAllCategory() {
        return itemCategoryRepository.findAll();
    }

    @Override
    public ItemCategory save(ItemCategory itemCategory) {
        return itemCategoryRepository.save(itemCategory);
    }
}
