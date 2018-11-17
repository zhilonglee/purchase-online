package com.zhilong.springcloud.entity;

import java.io.Serializable;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhilong.springcloud.entity.enu.Status;
import com.zhilong.springcloud.utils.EncryptAndDecryptUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "tbl_person",uniqueConstraints = {@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "username")})
@Entity
@DynamicUpdate
@DynamicInsert
public class Person implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint comment 'person primary key'")
	private Long id;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
	@Column(columnDefinition = "datetime comment 'create date'")

	private Date createDate;

	@Column(columnDefinition = "varchar(50) comment 'person name'")
	private String name;

	@Column(columnDefinition = "int comment 'person age'")
	private Integer age;

	@Column(columnDefinition = "varchar(255) comment 'person address'")
	private String address;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(columnDefinition = "datetime comment 'person birth day'")
	private Date birthDay;

	@Column(columnDefinition = "varchar(50) comment 'person email'")
	private String email;

	//@JsonIgnore
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(columnDefinition = "varchar(100) comment 'person password'")
	private String password;

	@Column(columnDefinition = "varchar(50) comment 'person username'")
	private String username;

	private Status status;

	public Person() {
		this.createDate = new Date();
	}

	public Person(final Long id, final String name, final Integer age, final String address) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public Person(Long id) {
		this.id = id;
	}

	public Person addBirthDay(final Date birthDay) {
		this.setBirthDay(birthDay);
		return this;
	}

	public void encryptPassword() {
		this.password = EncryptAndDecryptUtils.md5DigestEncryptAsHex(this.password);
	}

	public String formatDataReport() {
		return this.id + "," + this.createDate + "," + this.name + "," + this.age + "," + this.address + ","
				+ this.birthDay;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", createDate=" + createDate +
				", name='" + name + '\'' +
				", age=" + age +
				", address='" + address + '\'' +
				", birthDay=" + birthDay +
				", email='" + email + '\'' +
				", username='" + username + '\'' +
				", status=" + status +
				'}';
	}
}
