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
 * This class is used for store the mapped return results of each method;
 * the key is the methodID connected by underscores (eg. findUser_String),
 * the value is the result mapped columns
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class CacheResultMap {
	
	private Map<String, Map<String, String>> map = new ConcurrentHashMap<String, Map<String,String>>();
	
	public CacheResultMap(){}
	
	/**
	 * get raw SQL of the method
	 * @param methodID
	 * @return result map
	 */
	public Map<String, String> get(String methodID) {
		return map.get(methodID);
	}
	
	/**
	 * @param methodID
	 * full name of the method, including parameters
	 * for example: a).getAll_String_String
	 * 				b).deleteUser_int
	 *
	 * @param result
	 * the raw SQL string of the method
	 */
	public void put(String methodID, Map<String, String> result) {
		map.put(methodID, result);
	}
}
