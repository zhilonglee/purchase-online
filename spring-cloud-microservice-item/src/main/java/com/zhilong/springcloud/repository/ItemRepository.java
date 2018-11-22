package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> , JpaSpecificationExecutor<Item> {

    Page<Item> findAllByOrderByCreateDateDesc(Pageable pageable);
}
