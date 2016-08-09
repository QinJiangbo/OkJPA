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
package com.qinjiangbo.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
/**
 * This is a helper class for extracting information of keys and values from a map
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class MapUtil {
	
	@SuppressWarnings("rawtypes")
	public static Object getMapKey(Map map) {
		Object object = null;
		Iterator iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			object = entry.getKey();
			break;
		}
		return object;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object getMapValue(Map map) {
		Object object = null;
		Iterator iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			object = entry.getValue();
			break;
		}
		return object;
	}
	
	
}
