package com.zy.jmx.helloworld;

public class HelloWorld implements HelloWorldMBean {
	
	private String name;

	public void setName(String name) {
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public void printHello(String whoName) {
		System.out.println("Hello , " + whoName);

	}

	
	public void printHello() {
		System.out.println("Hello World, " + name);
	}
}
