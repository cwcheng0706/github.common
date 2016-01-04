/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年1月4日 下午9:17:16
 */
package com.zy.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年1月4日 下午9:17:16
 */
public class MultA extends Observable implements Observer {

	@Override
	public void update(Observable o, Object arg) { // 有被观察者发生变化，自动调用相应观察者的update方法
		MultB multb = (MultB) o; // 获取被观察者对象
		System.out.println("MultA监听到MultB数据变化：" + multb.data);

		setChanged();
		notifyObservers(); // 自己观察到数据变化，通知自己的观察者
	}

}
