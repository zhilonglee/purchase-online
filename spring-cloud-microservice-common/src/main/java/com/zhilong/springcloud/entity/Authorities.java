package com.zhilong.springcloud.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {

    @EmbeddedId
    EmbeddedUsers embeddedUsers;

    @Column(columnDefinition = "varchar(50) not null")
    private String authority;
}
