package com.zy.jmx.helloworld;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

public class HelloAgent {
	public static void main(String[] args) throws Exception {
//        MBeanServer server = MBeanServerFactory.createMBeanServer();

//        ObjectName helloName = new ObjectName("chengang:name=HelloWorld");
//        server.registerMBean(new HelloWorld(), helloName);

//        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");
//        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
//        server.registerMBean(adapter, adapterName);
//        adapter.start();
        
		
		
		
		
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.example.mbeans:type=Hello");  
        
        // Create the Hello World MBean  
        HelloWorld mbean = new HelloWorld();  
      
        // Register the Hello World MBean  
        server.registerMBean(mbean, name);  
      
        
        System.out.println("start.....");
        Thread.sleep(Long.MAX_VALUE);
        

    }
} 
