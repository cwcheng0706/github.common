package com.zy.jmx.helloworld;

public interface HelloWorldMBean {

	public void setName(String name);

	public String getName();

	public void printHello(String whoName);
}
