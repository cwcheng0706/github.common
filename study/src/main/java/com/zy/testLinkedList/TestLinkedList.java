package com.zy.testLinkedList;

import java.util.LinkedList;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class TestLinkedList {

	public static void main(String[] args) {
		testLinkedList();
	
	}
	
	
	
	public static void testLinkedList() {
		
		
		LinkedList<String> list = new LinkedList<String>();
		for(int i = 0 ; i < 1000000; i++) {
			list.add(String.valueOf(i));
		}
		
		long t1 = System.currentTimeMillis();
		int temp = 0 ;
		for(int i = 0 ; i < 1000; i++) {
			temp = new Random().nextInt(list.size()-1);
			list.remove(temp);
		}
		
		long t2 = System.currentTimeMillis();
		
		System.out.println(t2-t1);
	}
}
