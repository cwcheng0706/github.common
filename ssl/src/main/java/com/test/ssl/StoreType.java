package com.test.ssl;

public enum StoreType {

	PKCS12("PKCS12"), JKS("JKS"), JCEKS("JCEKS");

	private String name;

	private StoreType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
