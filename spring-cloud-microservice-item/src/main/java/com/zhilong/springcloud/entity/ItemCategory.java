package com.zhilong.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilong.springcloud.entity.enu.ItemCategoryStatus;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_cat", indexes = {@Index(name = "parent_id",columnList = "parentId",unique = false)})
@DynamicUpdate
@DynamicInsert
public class ItemCategory implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint comment 'item category primary key'")
    private Integer id;

    @Column(columnDefinition = "bigint default '0' comment 'When the ID of the parent category =0, it represents the first-level class'")
    private Integer parentId;

    @Column(columnDefinition = "varchar(100) comment 'item category name'")
    private String name;

    @Column
    private ItemCategoryStatus status;

    @Column(columnDefinition = "tinyint(1) default '1' comment 'Whether the category is a parent, 1 is true and 0 is false'")
    private Boolean isParent;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'create date'")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'update date'")
    private Date updateDate;
}
