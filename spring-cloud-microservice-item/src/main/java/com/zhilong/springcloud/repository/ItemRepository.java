package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.entity.dto.ItemSimpleTo;
import com.zhilong.springcloud.entity.enu.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> , JpaSpecificationExecutor<Item> {

    Page<Item> findAllByOrderByCreateDateDesc(Pageable pageable);

    @Query(value = "SELECT new com.zhilong.springcloud.entity.dto.ItemSimpleTo(a.id,a.price*a.rate,a.image,a.item_des,a.title,a.catId, a.stockNum) from Item as a" +
            " where a.status = :status and a.stockNum > :stockNum ORDER BY a.createDate DESC")
    Page<ItemSimpleTo> findItemBirefByStatusAndStockNumGreaterThan(@Param("status") ItemStatus status, @Param("stockNum")Integer stockNum, Pageable pageable);

    @Query(value = "SELECT new com.zhilong.springcloud.entity.dto.ItemSimpleTo(a.id,a.price*a.rate,a.image,a.item_des,a.title,a.catId, a.stockNum) from Item as a" +
            " where a.status = :status and a.catId = :catId and a.stockNum > :stockNum ORDER BY a.createDate DESC")
    Page<ItemSimpleTo> findItemBirefByStatusAndStockNumCategoryGreaterThan(@Param("status") ItemStatus status,@Param("catId")Long catId, @Param("stockNum")Integer stockNum, Pageable pageable);
}
