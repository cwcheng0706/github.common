/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年1月4日 下午9:13:49
 */
package com.zy.observer;

import java.util.Observable;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年1月4日 下午9:13:49
 */
public class NumObservable extends Observable {
	int data = 0;

	public void setData(int i) {
		data = i;
		setChanged(); // 标记此 Observable对象为已改变的对象
		notifyObservers(); // 通知所有观察者
	}
}
