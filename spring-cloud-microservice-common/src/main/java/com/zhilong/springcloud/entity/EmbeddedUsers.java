package com.zhilong.springcloud.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@Embeddable
public class EmbeddedUsers implements Serializable {
    @OneToOne
    @JoinColumn(name = "username")
    private Users users;
}
