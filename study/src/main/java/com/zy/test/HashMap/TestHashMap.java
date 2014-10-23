package com.zy.test.HashMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class TestHashMap {
	public static void main(String[] args) {
		
		
		//下面是hashcode相同，equals不同,  key 不会被覆盖，会追加新的Entry
		System.out.println("==========hashcode相同，equals不同============");
		Student s1 = new Student(Long.valueOf("1"),"11");
		Student s2 = new Student(Long.valueOf("1"),"22");
		
		System.out.println("hashcode  = " + (s1.hashCode() == s2.hashCode()));
		System.out.println("equals  = " + (s1.equals(s2)));
		
		Map<Student,String> map = new HashMap<Student,String>();
		String st1 = map.put(s1,"1");
		String st2 = map.put(s2,"2");
		System.out.println("st1=" + st1 +"  st2=" + st2);
		
		Set<Student> keySet = map.keySet();
		Iterator<Student> iter = keySet.iterator();
		while(iter.hasNext()) {
			Student key = iter.next();
			String value = map.get(key);
			System.out.println("key【" + key + "】 value【" + value + "】");
		}
		System.out.println(map.get(s1));
		System.out.println(map.get(s2));
		
		
		//下面是hashcode不同，equals相同
		System.out.println("=============hashcode不同，equals相同=============");
		Student s11 = new Student(Long.valueOf("1"),"11");
		Student s22 = new Student(Long.valueOf("2"),"11");
		
		Map<Student,String> mapp = new HashMap<Student,String>();
		mapp.put(s11, "1");
		mapp.put(s22, "2");
		Set<Student> keySett = mapp.keySet();
		Iterator<Student> iterr = keySett.iterator();
		while(iterr.hasNext()) {
			Student key = iterr.next();
			String value = mapp.get(key);
			System.out.println("key【" + key + "】 value【" + value + "】");
		}
		System.out.println(mapp.get(s11));
		System.out.println(mapp.get(s22));
		
		
		
		
		//下面hashcode相同，equals也相同,  key 不会被覆盖，会新增加Entry
		System.out.println("=============hashcode不相同，equals也不相同=============");
		String a = "1";
		String b = "2";
//		String b = new String("");
//		b.valueOf("1");
		
		System.out.println("hashCode  = " + (a.hashCode()==b.hashCode()));
		System.out.println("equals  = " + a.equals(b));
		
		
		HashMap<String,String> map1 = new HashMap<String,String>();
		String str1 = map1.put(a, "1");
		String str2 = map1.put(b, "2");
		System.out.println("str1=" + str1 + "  str2=" + str2);
		
		Set<String> keySet1 = map1.keySet();
		Iterator<String> iter1 = keySet1.iterator();
		while(iter1.hasNext()) {
			String key = iter1.next();
			String value = map1.get(key);
			System.out.println("key【" + key + "】 value【" + value + "】");
		}
		System.out.println(map1.get(a));
		System.out.println(map1.get(b));
		
//		System.out.println(sun.misc.Hashing.stringHash32((String) a));
//		System.out.println(sun.misc.Hashing.stringHash32((String) b));
		
		
		System.out.println("===============================");
		
		/**
		 * 有趣的HashMap.indexFor() 方法
		 */
		System.out.println("下面是1-20 与15 进行 & 运算");
		for(int i = 1 ; i < 20; i++) {
			System.out.println(i + " & 15=" + (i & 15));
		}
		
		System.out.println("下面是1-20 与17 进行 & 运算");
		for(int i = 1 ; i < 20; i++) {
			System.out.println(i + " & 17=" + (i & 15));
		}
		
		
		
		
		
		System.out.println("");
	}
}

class Student {
	
	public Student(Long id,String name) {
		this.id = id;
		this.name = name;
	}

	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Student s = (Student)obj;
		if(s.getName().equals(this.name)) {
			return true;
		}else {
			return false;
		}
		
//		return super.equals(obj);
	}

	
}
