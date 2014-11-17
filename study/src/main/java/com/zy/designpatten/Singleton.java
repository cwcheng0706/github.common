package com.zy.designpatten;

/**
 * 采用静态内部类 实现单例模式
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年11月17日 上午9:29:53
 */
public class Singleton {

	private Singleton() {
	}

	public Singleton getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		private static Singleton instance = new Singleton();
	}
}
