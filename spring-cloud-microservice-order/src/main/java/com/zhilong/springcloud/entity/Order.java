package com.zhilong.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.zhilong.springcloud.entity.enu.OrderStatus;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "tbl_order")
public class Order implements Serializable {

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

    @Column(columnDefinition = "varchar(100) comment 'Transaction Code '")
    private String transactionCode;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @Column
    private BigDecimal total;

    @Column
    private BigDecimal disacountTotal;

    @Column
    private OrderStatus orderStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'completed date'")
    private Date completedDate;
}
