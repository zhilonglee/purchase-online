package com.zhilong.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "tbl_order")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'create date'")
    private Date createDate;

    @Column(columnDefinition = "varchar(50) comment 'person name'")
    private String username;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
}
