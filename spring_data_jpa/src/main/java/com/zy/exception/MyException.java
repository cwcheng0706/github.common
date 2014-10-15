package com.zy.exception;

public class MyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 448452295028583837L;

	public MyException(String e) {
		super(e);
	}

	public MyException(Exception e) {
		super(e);
	}
	
}
