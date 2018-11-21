package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> , JpaSpecificationExecutor<Item> {


}
