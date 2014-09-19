/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月16日 下午2:20:41
 */
package com.zy.entity;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company:
 * @Create Time: 2014年1月16日 下午2:20:41
 */
public class ExcelBean {
	
	private String line;

	private String number; 
	
	private String payProp;
	private String province;
	private String city;
	private String mainNumber;
	private String charge;
	private String business;
	private String marketingDate;
	private String marktingSource;
	private String orderUser;
	private String state;
	
	public String print(){
		String temp = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("第【" + this.line + "】行").append("\t")
		  .append(this.number).append("\t")
		  .append(this.payProp).append("\t")
		  .append(this.province).append("\t")
		  .append(this.city).append("\t")
		  .append(this.mainNumber).append("\t")
		  .append(this.charge).append("\t")
		  .append(this.business).append("\t")
		  .append(this.marketingDate).append("\t")
		  .append(this.marktingSource).append("\t")
		  .append(this.orderUser).append("\t")
		  .append(this.state);
		temp = sb.toString();
		return temp;
	}

	public String getNumber() {
		return number;
	}

	public String getPayProp() {
		return payProp;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getMainNumber() {
		return mainNumber;
	}

	public String getBusiness() {
		return business;
	}

	public String getMarketingDate() {
		return marketingDate;
	}

	public String getMarktingSource() {
		return marktingSource;
	}

	public String getOrderUser() {
		return orderUser;
	}

	public String getState() {
		return state;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setPayProp(String payProp) {
		this.payProp = payProp;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setMainNumber(String mainNumber) {
		this.mainNumber = mainNumber;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public void setMarketingDate(String marketingDate) {
		this.marketingDate = marketingDate;
	}

	public void setMarktingSource(String marktingSource) {
		this.marktingSource = marktingSource;
	}

	public void setOrderUser(String orderUser) {
		this.orderUser = orderUser;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

}
