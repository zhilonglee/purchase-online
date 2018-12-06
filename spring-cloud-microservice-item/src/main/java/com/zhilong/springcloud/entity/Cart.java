package com.zhilong.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_cart")
public class Cart extends BaseEntity implements Serializable {

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<CartItem> cartItems = new ArrayList<CartItem>();

    @Column(columnDefinition = "varchar(50) comment 'person name'")
    private String username;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'create date'")
    private Date createDate;

    @Column
    private BigDecimal total;

    @Column
    private BigDecimal disacountTotal;

}
