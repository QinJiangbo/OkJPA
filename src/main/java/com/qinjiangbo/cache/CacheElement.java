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
package com.qinjiangbo.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * This class is used for store the mapped SQL of each method;
 * the key is the methodID connected by underscores (eg. findUser_String),
 * the value is the SQL statement for the method
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class CacheElement {
	
	private Map<String, String> map = new ConcurrentHashMap<String, String>();
	
	public CacheElement() {}
	
	/**
	 * get raw SQL of the method
	 * @param methodID
	 * @return raw SQL
	 */
	public String get(String methodID) {
		return map.get(methodID);
	}
	
	/**
	 * @param methodID
	 * full name of the method, including parameters
	 * for example: a).getAll_String_String
	 * 				b).deleteUser_int
	 *
	 * @param sql
	 * the raw SQL string of the method
	 */
	public void put(String methodID, String sql) {
		map.put(methodID, sql);
	}
}
