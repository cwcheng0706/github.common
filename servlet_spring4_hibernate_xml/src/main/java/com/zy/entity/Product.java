/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午9:31:19
 */
package com.zy.entity;

import java.util.Date;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午9:31:19
 */
public class Product {
	
	private Long id;
	
	private String name;
	
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
