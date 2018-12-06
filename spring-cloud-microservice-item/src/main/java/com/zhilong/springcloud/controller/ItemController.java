package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.entity.JsonResult;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import com.zhilong.springcloud.fegin.UploadFeginClient;
import com.zhilong.springcloud.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class ItemController {

    private static Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    ItemService itemService;

    @Autowired
    UploadFeginClient uploadFeginClient;

    @GetMapping
    public ResponseEntity<List<Item>> itemlist(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", required = false) Integer size){
        List<Item> all = new ArrayList<>();
        if (size == null || size == 0) {
            all = itemService.findAll();
            logger.info("Finding all items with size : " + all.size());
        } else {
            all = itemService.findAll(PageRequest.of(page, size, Sort.by(Sort.DEFAULT_DIRECTION, "createDate")));
            logger.info("Finding a part of items with size : " + all.size() + ". Page : " + page);
        }
        return ResponseEntity.ok(all);
    }

    @GetMapping("/brief")
    public ResponseEntity<List<ItemSimpleTo>> itemSimplelist(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size",defaultValue = "16") Integer size){
        List<ItemSimpleTo> all = new ArrayList<>();
        all = itemService.findAllSimpleItem(PageRequest.of(page, size));
        logger.info("Finding a part of items with size : " + all.size() + ". Page : " + page);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/brief/{id}")
    public ResponseEntity<List<ItemSimpleTo>> itemSimple(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size",defaultValue = "4") Integer size, @PathVariable("id") Long id){
        List<ItemSimpleTo> all = new ArrayList<>();
        all = itemService.findAllSimpleItemByCategory(id,PageRequest.of(page, size));
        logger.info("Finding a part of items with size : " + all.size() + ". Page : " + page);
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<Item> saveItem(Item item, @RequestParam(name = "file", required = false) MultipartFile file) {
        try {
            logger.info("Access save Item method .");
            if (file != null) {
                ResponseEntity<String> uploadHttpEntity = uploadFeginClient.fileUpload(file);
                if (!uploadHttpEntity.getStatusCode().equals(HttpStatus.OK)) {
                    return ResponseEntity.badRequest().body(new Item("Image Upload Failed!"));
                } else {
                    item.setImage(uploadHttpEntity.getBody());
                }
            }
            Item saveItem = itemService.save(item);
            return ResponseEntity.ok(saveItem);
        } catch (Exception e) {
            logger.error("",e);
            return ResponseEntity.badRequest().body(new Item());
        }
    }

}
