/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年1月4日 下午9:15:21
 */
package com.zy.observer;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年1月4日 下午9:15:21
 */
public class NumTest {
	public static void main(String[] args) {
		NumObservable number = new NumObservable(); // 被观察者对象
		number.addObserver(new NumObserver()); // 给number这个被观察者添加观察者(当然可以有多个观察者)
		number.setData(1);
		number.setData(2);
		number.setData(3);
	}
}
