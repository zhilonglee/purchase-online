package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.entity.ItemCategory;
import com.zhilong.springcloud.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
public class ItemCategoryController {

    @Autowired
    ItemCategoryService itemCategoryService;

    @GetMapping
    public ResponseEntity<List<ItemCategory>> categorylist(){
        List<ItemCategory> itemCategories = itemCategoryService.findAllCategory();
        return ResponseEntity.ok(itemCategories);
    }

    @PostMapping
    public ResponseEntity<ItemCategory> saveCategory(ItemCategory itemCategory){
        ItemCategory itemCategoryIndb = itemCategoryService.save(itemCategory);
        return ResponseEntity.ok(itemCategoryIndb);
    }
}
