package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.ItemCategory;

import java.util.List;

public interface ItemCategoryService {
    List<ItemCategory> findAllCategory();

    ItemCategory save(ItemCategory itemCategory);
}
