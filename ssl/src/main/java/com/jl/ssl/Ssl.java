package com.jl.ssl;

import java.io.InputStream;

public class Ssl {

	private String url;
	private InputStream clientIs;
	private InputStream serverIs;
	private String clientPasswd;
	private String serverPasswd;

	public String getUrl() {
		return url;
	}

	public InputStream getClientIs() {
		return clientIs;
	}

	public InputStream getServerIs() {
		return serverIs;
	}

	public String getClientPasswd() {
		return clientPasswd;
	}

	public String getServerPasswd() {
		return serverPasswd;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setClientIs(InputStream clientIs) {
		this.clientIs = clientIs;
	}

	public void setServerIs(InputStream serverIs) {
		this.serverIs = serverIs;
	}

	public void setClientPasswd(String clientPasswd) {
		this.clientPasswd = clientPasswd;
	}

	public void setServerPasswd(String serverPasswd) {
		this.serverPasswd = serverPasswd;
	}
}
