package com.zhilong.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_item")
@DynamicUpdate
@DynamicInsert
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint comment 'item primary key'")
    private String id;

    @Column
    private String title;

    @Column
    private String sell_point;

    @Column
    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String image;

    @Column
    private String category_name;

    @Column(columnDefinition = "varchar(1024) comment 'item description'")
    private String item_des;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'create date'")
    private Date createDate;

    public Item() {
    }

    public Item(String image) {
        this.image = image;
    }
}
