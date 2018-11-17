package com.zhilong.springcloud.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @Column(columnDefinition = "varchar(50) not null")
    private String username;
    @Column(columnDefinition = "varchar(255) not null")
    private String password;
    @Column(columnDefinition = "tinyint not null")
    private Boolean enabled;
}
