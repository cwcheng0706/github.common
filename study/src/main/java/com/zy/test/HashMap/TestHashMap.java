package com.zy.test.HashMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class TestHashMap {
	public static void main(String[] args) {
		
		
		//下面是hashcode相同，equals也不同,  key 不会被覆盖，会追加新的Entry
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
		
		
		
		System.out.println("==========================");
		
		
		//下面hashcode不同，equals也不同,  key 不会被覆盖，会新增加Entry
		String a = "1";
		
		String b = new String();
		b.valueOf("1");
		
		System.out.println("hashCode  = " + (a.hashCode()==b.hashCode()));
		System.out.println("equals  = " + a.equals(b));
		
		System.out.println("b: "+ b);
		
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
		
		
		
		//hashcode 相等  ,equals也相等，直接覆盖
		System.out.println("================================");
		Map<Integer,String> m = new HashMap<Integer,String>();
		m.put(1, "1");
		m.put(1, "2");
		m.put(1, "3");
		
		
		
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
