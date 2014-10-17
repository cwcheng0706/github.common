/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午9:31:19
 */
package com.zy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.GroupSequence;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zy.group.First;
import com.zy.group.Second;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午9:31:19
 */
@Entity(name = "t_product")
@Audited
@GroupSequence({First.class, Second.class, Product.class})  
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2805197417135535681L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	@NotEmpty(message = "name不能为空", groups={First.class})
	@Length(min = 5, max = 20, message = "name长度应该在5-20位之间", groups = {Second.class})
	private String name;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column
	@NotEmpty(message="price不能为空")
	private BigDecimal price;

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
