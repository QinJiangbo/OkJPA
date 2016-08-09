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
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.qinjiangbo.annotations.Param;
import com.qinjiangbo.cache.CacheElement;
import com.qinjiangbo.cache.MapperCache;
import com.qinjiangbo.exceptions.ParameterDismatchException;
import com.qinjiangbo.jdbc.SQL;

/**
 * Reflector class by using the JDK reflect technologies
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class MethodReflector<T> {
	
	private Class<T> mapperInterface;
	private Method method;
	private Object[] args;
	
	public MethodReflector(Class<T> mapperInterface, Method method, Object[] args) {
		this.mapperInterface = mapperInterface;
		this.method = method;
		this.args = args;
	}
	
	/**
	 * generate the SQL instance
	 * @return SQL instance
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SQL generateSQL() {
		CacheElement cacheElement = MapperCache.getElement(mapperInterface.getName());
		String methodID = ParamReflector.getMethodID(method);
		String rawSQL = cacheElement.get(methodID);
		Parameter[] params = method.getParameters();
		int count = method.getParameterCount();
		SQL sql;
		if(count == 0) {
			sql = new SQL(rawSQL, null);
		}
		else if(isPrimitive(params)) {
			Map map = new ConcurrentHashMap();
			if(args.length != params.length) {
				throw new ParameterDismatchException("parameters: " + params.length + " , " + "arguments: " + args.length);
			}
			for(int i=0; i< params.length; i++) {
				if(params[i].isAnnotationPresent(Param.class)) {
					Param annotation = params[i].getAnnotation(Param.class);
					map.put(annotation.value(), args[i]);
				}
				else {
					map.put(params[i].getName(), args[i]);
				}
			}
			sql = new SQL(rawSQL, map);
		}
		else {
			/**
			 * get the compound type of the parameter
			 */
			Class parameterType = method.getParameterTypes()[0];
			Map map = new ConcurrentHashMap();
			map.put(parameterType.getName(), args[0]);
			sql = new SQL(rawSQL, map);
		}
		return sql;
	}
	
	/**
	 * judge whether the parameters are primitive
	 * @param params
	 * @return
	 */
	private boolean isPrimitive(Parameter[] params) {
		boolean isPrimitive = false;
		for(Parameter parameter: params) {
			if(ParamReflector.isPrimitive(parameter.getType())) {
				isPrimitive = true;
				break;
			}
		}
		return isPrimitive;
	}
	
	/**
	 * get the result type of the query methods based on the return types
	 * @param method query method
	 * @return result type
	 */
	public static String findResultTypes(Method method) {
		return method.getGenericReturnType().getTypeName();
	}
}
