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

/**
 * This class is operation class for the LRU cache
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class MapperCache {
	/**
	 * cache to store the Mapper information
	 */
	private static BaseCache<String, CacheElement> elementCache = new BaseCache<String, CacheElement>(200);
	private static BaseCache<String, CacheResultMap> mapCache = new BaseCache<String, CacheResultMap>(200);
	
	/**
	 * put the elements inside the cache
	 * @param mapperClassPath full path, for example: com.qinjiangbo.cache.BaseCache 
	 * @param cacheElement cache elements
	 */
	public static void putElement(String mapperClassPath, CacheElement cacheElement) {
		elementCache.put(mapperClassPath, cacheElement);
	}
	
	/**
	 * find the elements by the full class path
	 * @param mapperClassPath full class path
	 * @return the cache elements
	 */
	public static CacheElement getElement(String mapperClassPath) {
		return elementCache.get(mapperClassPath);
	}
	
	/**
	 * put the elements inside the cache
	 * @param mapperClassPath full path, for example: com.qinjiangbo.cache.BaseCache 
	 * @param cacheResultMap cache result maps
	 */
	public static void putResultMap(String mapperClassPath, CacheResultMap cacheResultMap) {
		mapCache.put(mapperClassPath, cacheResultMap);
	}
	
	/**
	 * find the elements by the full class path
	 * @param mapperClassPath full class path
	 * @return the cache result maps
	 */
	public static CacheResultMap getResultMap(String mapperClassPath) {
		return mapCache.get(mapperClassPath);
	}
	
}
