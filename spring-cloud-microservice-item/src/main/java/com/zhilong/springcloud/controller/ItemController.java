package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import com.zhilong.springcloud.fegin.UploadFeginClient;
import com.zhilong.springcloud.service.ItemService;
import com.zhilong.springcloud.service.SeckillService;
import com.zhilong.springcloud.utils.CalendarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class ItemController {

    private static Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    ItemService itemService;

    @Autowired
    SeckillService seckillService;

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

    @GetMapping("/{id}")
    public ResponseEntity item( @PathVariable("id") Long id){
        Item item = itemService.findById(id);
        return ResponseEntity.ok(item);
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

    @PutMapping("/deduct/{id}")
    public ResponseEntity itemDeductStockNum( @PathVariable("id") Long id, Integer num){
        Integer deducted = itemService.deductItemStockNum(id, num);
        return ResponseEntity.ok(deducted);
    }

    @GetMapping("/limitedspike")
    public ResponseEntity findRandItem() {
        return ResponseEntity.ok(itemService.findRandItem());
    }

    @GetMapping("/limitedspike/{id}")
    public ResponseEntity findSeckillItem(@PathVariable("id") Long id) {
        Date startTime = CalendarUtils.getSharpClock();
        Date endTime = CalendarUtils.addHourDate(startTime,1);
        return ResponseEntity.ok(seckillService.findSeckillByItemIdAndStartTimeAndEndTime(id,startTime,endTime));
    }

    @PostMapping("/limitedspike")
    public ResponseEntity purchaseSecKill(Long itemId, String username) {
        return itemService.purchaseSecKill(itemId,username);
    }

}
