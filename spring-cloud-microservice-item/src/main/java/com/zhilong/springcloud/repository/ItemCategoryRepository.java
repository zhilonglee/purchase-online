package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long>, JpaSpecificationExecutor<ItemCategory> {
}
