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

import java.lang.reflect.Method;

/**
 * Reflector class by using the JDK reflect technologies,
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class ParamReflector {
	
	/**
	 * judge whether the class is primitive
	 * @param c class type
	 * @return true primitive, false non-primitive
	 */
	public static boolean isPrimitive(Class<?> c) {
		return c.isPrimitive()
				|| c == Integer.class
				|| c == Long.class
				|| c == Boolean.class
				|| c == Character.class
				|| c == Float.class
				|| c == Byte.class
				|| c == Double.class
				|| c == Short.class
				|| c == String.class;
	}
	
	/**
	 * get the methodID by annotation and method which annotated by it
	 * @param method
	 * @return methodID the method identifier, for example, getAll_String_int
	 */
	@SuppressWarnings("rawtypes")
	public static String getMethodID(Method method) {
		Class[] parameTypes = method.getParameterTypes();
		String methodID = method.getName();
		for(Class param: parameTypes) {
			methodID = methodID + "_" + param.getSimpleName();
		}
		return methodID;
	}
	
	/**
	 * convert the object to the corresponding result type
	 * @param c result type
	 * @param str result string
	 * @return valid object
	 */
	public static <T> Object castObject(Class<T> c, Object object) {
		if(c == Integer.class || c == int.class) {
			object = Integer.parseInt(object.toString());
		}else if(c == Long.class || c == long.class) {
			object = Long.parseLong(object.toString());
		}else if(c == Boolean.class || c == boolean.class) {
			object = Boolean.parseBoolean(object.toString());
		}else if(c == Float.class || c == float.class) {
			object = Float.parseFloat(object.toString());
		}else if(c == Double.class || c == double.class) {
			object = Double.parseDouble(object.toString());
		}else {
			object = object.toString();
		}
		return object;
	}
	
	@SuppressWarnings("rawtypes")
	/**
	 * get real class type of the object
	 * @param className class name
	 * @return corresponding class type
	 */
	public static Class getClass(String className) {
		if(className.equals("int")) {
			return int.class;
		}
		else if(className.equals("long")) {
			return long.class;
		}
		else if(className.equals("float")) {
			return float.class;
		}
		else if(className.equals("boolean")) {
			return boolean.class;
		}
		else if(className.equals("double")) {
			return double.class;
		}
		else {
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
