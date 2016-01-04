/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年1月4日 下午9:18:23
 */
package com.zy.observer;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年1月4日 下午9:18:23
 */
public class MultTest {
	public static void main(String[] args) {
		MultA multa = new MultA();
		MultB multb = new MultB();

		multb.addObserver(multa);
		multa.addObserver(multb);

		multb.setData(1);
	}
}
