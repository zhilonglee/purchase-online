package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.Seckill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SecKillRepository extends JpaRepository<Seckill, Long>, JpaSpecificationExecutor<Seckill> {
    List<Seckill> findSeckillsByStartTimeEqualsAndEndTimeEquals(Date startTime, Date endTime);
    Seckill findSeckillByItemIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(Long itemId,Date startTime, Date endTime);
}
