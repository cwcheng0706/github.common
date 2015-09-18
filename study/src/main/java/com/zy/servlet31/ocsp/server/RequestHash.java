package com.zy.servlet31.ocsp.server;

import java.util.Arrays;

public class RequestHash {
	private byte [] request;
	private int h=0;
	private static final int modulus=Integer.MAX_VALUE;
	
	public RequestHash(byte [] request) {
		this.request=request;
		long a=31415;
		long b=27183;
		int i=0;
		for (;i<request.length;++i,a=a*b%modulus)
			h=(int)((a*h+request[i])%modulus);
	}
	
	public int hashCode() {
		return h;
	}
	
	public boolean equals(Object obj) {
		return Arrays.equals(request,((RequestHash)obj).request);
	}

}
