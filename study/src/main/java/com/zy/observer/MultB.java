/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年1月4日 下午9:17:42
 */
package com.zy.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年1月4日 下午9:17:42
 */
public class MultB extends Observable implements Observer {
	int data = 0;

	public void setData(int i) {
		data = i;
		setChanged(); // 标记此 Observable对象为已改变的对象
		notifyObservers(); // 通知所有观察者
	}

	public void update(Observable arg0, Object arg1) {
		System.out.println("MultB观察到几乎同时MultA也有数据变化，貌似自己的变化被监听。。。。");
	}
}
