/**
 * Copyright 2016 QinJiangbo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qinjiangbo.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is mainly used for parsing the information of the POJO
 * which stands for <em>P</em>lain <em>O</em>rdinary <em>J</em>ava <em>O</em>bject
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class PojoReflector {
	
	//prevent this class from instanced
	private PojoReflector() {
		
	}
	
	/**
	 * parse the POJO object, get the information
	 * @param pojo
	 * @return the information map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map parsePojo(Object pojo) {
		Map map = new ConcurrentHashMap();
		//get the class type of object pojo
		Class c = pojo.getClass();
		Field[] fields = c.getDeclaredFields();
		for(Field field : fields) {
			String fieldName = field.getName();
			String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			try {
				Method method = c.getMethod(getterName);
				Object fieldValue = method.invoke(pojo);
				if(fieldValue != null) {
					map.put(fieldName, fieldValue);
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
