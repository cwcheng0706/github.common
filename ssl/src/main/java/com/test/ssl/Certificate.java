package com.test.ssl;

/**
 * @Project: 
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年8月14日 上午9:23:10
 */
public class Certificate {

	private String alias;
	private String storeType;
	private String keyStore;
	private String validity;
	private String storePass;
	private String keyPass;
	
	private CertificateBody body;

	public CertificateBody getBody() {
		return body;
	}

	public void setBody(CertificateBody body) {
		this.body = body;
	}

	public String getAlias() {
		return alias;
	}

	public String getStoreType() {
		return storeType;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public String getValidity() {
		return validity;
	}

	public String getStorePass() {
		return storePass;
	}

	public String getKeyPass() {
		return keyPass;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public void setStorePass(String storePass) {
		this.storePass = storePass;
	}

	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}
}
