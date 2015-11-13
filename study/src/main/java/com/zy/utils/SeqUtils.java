package com.zy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SeqUtils {
	public static void main(String[] args) {
		String no = getMaxId();
		System.out.println("流水号" + '\n' + no);

	}

	public static String getMaxId() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		String firstNo = "000017";
		String lastNo = firstNo + date + "001";
		/**
		 * 此处可以冲数据库中查询然后进行比较，如果为空默认为lastno如果部位空 讲取出的流水号作为temp，然后截取，累加返回。
		 */
		StringBuffer sb = new StringBuffer();
		String temp = lastNo.substring(lastNo.length() - 3, lastNo.length());

		if (Integer.parseInt(temp) >= 1 && Integer.parseInt(temp) < 999) {
			temp = String.valueOf(Integer.parseInt(temp) + 1);
		}
		switch (temp.length()) {
		case 1:
			temp = "00" + temp;
			break;
		case 2:
			temp = "0" + temp;
			break;
		default:
			break;
		}
		lastNo = firstNo + date + temp;
		return lastNo;

	}
}
