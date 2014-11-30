/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午9:31:19
 */
package com.zy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午9:31:19
 */
@Entity(name = "t_product")
public class Product {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
