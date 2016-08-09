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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.qinjiangbo.annotations.Delete;
import com.qinjiangbo.annotations.Insert;
import com.qinjiangbo.annotations.Result;
import com.qinjiangbo.annotations.Results;
import com.qinjiangbo.annotations.Select;
import com.qinjiangbo.annotations.Update;
import com.qinjiangbo.cache.CacheElement;
import com.qinjiangbo.cache.CacheResultMap;
import com.qinjiangbo.cache.MapperCache;
import com.qinjiangbo.enums.JdbcOperations;
import com.qinjiangbo.jdbc.SQL;

/**
 * Reflector class by using the JDK reflect technologies
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class MapperReflector<T> {
	
	public final Class<T> mapperInterface;
	
	public MapperReflector(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}
	
	@SuppressWarnings("unchecked")
	private void parseClass() {
		if(MapperCache.getElement(mapperInterface.getName()) == null) {
			Method[] methods = mapperInterface.getDeclaredMethods();
			CacheElement cacheElement = new CacheElement();
			CacheResultMap cacheResultMap = new CacheResultMap();
			for(Method method: methods) {
				String methodID = ParamReflector.getMethodID(method);
				for (JdbcOperations opName : JdbcOperations.values()) {
					boolean isPresent = method.isAnnotationPresent(opName.getClazz());
					if(isPresent) {
						String rawSql = "";
						if(opName.getName().equals("Select")) {
							Select annotation = method.getAnnotation(Select.class);
							rawSql = annotation.value();
							if(method.isAnnotationPresent(Results.class)) {
								Results results = method.getAnnotation(Results.class);
								cacheResultMap.put(methodID, parseResultsAnnotation(results));
							}
						}else if(opName.getName().equals("Insert")) {
							Insert annotation = method.getAnnotation(Insert.class);
							rawSql = annotation.value();
						}else if(opName.getName().equals("Update")) {
							Update annotation = method.getAnnotation(Update.class);
							rawSql = annotation.value();
						}else if(opName.getName().equals("Delete")) {
							Delete annotation = method.getAnnotation(Delete.class);
							rawSql = annotation.value();
						}
						cacheElement.put(methodID, rawSql);
						/* Here, break out of the for loop in case there is multiple annotations */
						break;
					}
				}
			}
			/**
			 * put the elements to cache
			 * parsing class / interface done
			 */
			MapperCache.putElement(mapperInterface.getName(), cacheElement);
			if(cacheResultMap != null) {
				MapperCache.putResultMap(mapperInterface.getName(), cacheResultMap);
			}
		}
	}
	
	/**
	 * generate the SQL object
	 * @param mapperInterface
	 * @param method
	 * @return
	 */
	public SQL genSQL(Method method, Object[] args) {
		// parse the mapper class / interface first
		parseClass();
		return new MethodReflector<T>(mapperInterface, method, args).generateSQL();
	}
	
	/**
	 * find query type of the statement
	 * @param method the query method
	 * @return the query type of the method, <i>SELECT</i>, <i>INSERT</i>, <i>UPDATE</i>, <i>DELETE</i>
	 */
	@SuppressWarnings("unchecked")
	public static JdbcOperations queryType(Method method) {
		for (JdbcOperations opName : JdbcOperations.values()) {
			boolean isPresent = method.isAnnotationPresent(opName.getClazz());
			if(isPresent)
				return opName;
		}
		return null;
	}
	
	/**
	 * parse the results annotation of a method
	 * @param results <code>@Result</code> annotation
	 * @return result map
	 */
	private Map<String, String> parseResultsAnnotation(Results results) {
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		Result[] resultArr = results.value();
		int size = resultArr.length;
		for(int i=0; i<size; i++) {
			resultMap.put(resultArr[i].column(), resultArr[i].field());
		}
		return resultMap;
	}
}
