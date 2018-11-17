package com.zhilong.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilong.springcloud.entity.enu.TokenMoudle;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Table(name = "tbl_person_token",uniqueConstraints = {@UniqueConstraint(columnNames = "token")})
@Entity
@DynamicUpdate
@DynamicInsert
public class PersonToken {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint comment 'person primary key'")
    private Long id;

    @ManyToOne
    private Person person;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'create date'")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'expired date'")
    private Date expiredDate;

    @Column(columnDefinition = "varchar(100) comment 'one time token'")
    private String token;

    private Boolean isUse;

    private TokenMoudle tokenMoudle;

    public PersonToken() {
    }

    public PersonToken(Person person, String token, TokenMoudle tokenMoudle) {
        this.isUse = false;
        this.person = person;
        this.token = token;
        this.tokenMoudle = tokenMoudle;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.createDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,3);
        this.expiredDate = calendar.getTime();
    }
}
