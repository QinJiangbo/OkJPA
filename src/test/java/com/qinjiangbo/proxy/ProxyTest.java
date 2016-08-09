package com.qinjiangbo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ProxyTest {
	
	@Test
	public void testProxy() {
		
		People obj = getPeople();
		List<String> namesList = obj.getNames();
		for (int i = 0; i < namesList.size(); i++) {
			System.out.println(namesList.get(i));
		}
	}
	
	public People getPeople() {
		People people = new Student();
		Object object = Proxy.newProxyInstance(people.getClass().getClassLoader(), 
				people.getClass().getInterfaces(), new InvocationHandler() {
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						if(method.getName().equals("getNames")) {
							List<String> nameStrings = new ArrayList<String>();
							nameStrings.add("richard");
							nameStrings.add("amy");
							nameStrings.add("lily");
							return nameStrings;
						}
						return method.invoke(people, args);
					}
				});
		return (People) object;
	}
}

interface People{
	List<String> getNames();
}

class Student implements People{
	
	@Override
	public List<String> getNames() {
		List<String> namesList = new ArrayList<String>();
		namesList.add("Richard");
		namesList.add("Amy");
		return namesList;
	}
}
