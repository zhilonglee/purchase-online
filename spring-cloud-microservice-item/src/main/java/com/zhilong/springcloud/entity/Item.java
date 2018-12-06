package com.zhilong.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhilong.springcloud.entity.enu.ItemStatus;
import lombok.Data;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_item", indexes = {@Index(name = "item_cat_id",columnList = "catId",unique = false),
        @Index(name = "item_status",columnList = "status",unique = false)})
@DynamicUpdate
@DynamicInsert
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint comment 'item primary key'")
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "varchar(1024) comment 'item sell point'")
    private String sell_point;

    @Column
    private BigDecimal price;

    @Column(columnDefinition = "DECIMAL(3,2) default '1.00'  comment 'item rate'")
    private BigDecimal rate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String image;

    @Column(columnDefinition = "bigint comment 'item category Id'")
    private Long catId;

    @Column(columnDefinition = "varchar(1024) comment 'item description'")
    private String item_des;

    @Column(columnDefinition = "bigint comment 'item Stock Number'")
    private Integer stockNum;

    @Column
    private ItemStatus status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date", columnDefinition = "datetime comment 'create date'")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'update date'")
    private Date updateDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    private BigDecimal currentprice;

    public Item() {
    }

    public Item(String image) {
        this.image = image;
    }

    public BigDecimal getCurrentprice() {
        return (this.price.doubleValue() > 0.0d ? this.price.multiply(this.rate) : BigDecimal.valueOf(0.0d));
    }
}
