package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.Seckill;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface SeckillService {
    List<Seckill> generateSeckillItems(List<ItemSimpleTo> items);
    List<Seckill> getSeckillItems(Date startTime, Date endTime);
    Seckill findSeckillByItemIdAndStartTimeAndEndTime(Long id, Date startTime, Date endTime);
    Seckill deductSeckillStockNumAndSave(Seckill seckill,Integer num);
}
