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

import org.hibernate.annotations.GenericGenerator;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午9:31:19
 */
@Entity(name = "t_user")
public class User {

//	@Id
//	@GeneratedValue
	@Id 
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	@Column
	private String name;
	
	@Column
	private String sex;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}


}
