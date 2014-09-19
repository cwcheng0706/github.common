package com.zy.ssl.keytool.entity;

/**
 * @Project: 
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年8月14日 上午9:03:19
 */
public class CertificateBody {

	/** Common Name名字与姓氏 **/
	private String cn;

	/** Organization Unit组织单位名称 **/
	private String ou;

	/** Organization组织名称 **/
	private String o;

	/** Locality城市或区域名称 **/
	private String l;

	/** State州或省份名称 **/
	private String st;

	/** 国家 **/
	private String c;

	/** Common Name名字与姓氏 **/
	public String getCn() {
		return cn;
	}

	/** Organization Unit组织单位名称 **/
	public String getOu() {
		return ou;
	}

	/** Organization组织名称 **/
	public String getO() {
		return o;
	}

	/** Locality城市或区域名称 **/
	public String getL() {
		return l;
	}

	/** State州或省份名称 **/
	public String getSt() {
		return st;
	}

	/** 国家 **/
	public String getC() {
		return c;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public void setO(String o) {
		this.o = o;
	}

	public void setL(String l) {
		this.l = l;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public void setC(String c) {
		this.c = c;
	}

}
