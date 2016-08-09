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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is high-performance cache based on <code>LinkedHashMap</code>
 * The cache is a typical LRU (Least recently used) algorithm structure
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class BaseCache<K, V>{

	/**
	 * default load factor, for expanding the capacity of the map
	 */
	private final float DEFAULT_LOAD_FACTOR = 0.75f;
	private final int MAX_CACHE_SIZE;
	private LinkedHashMap<K, V> map;
	
	@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
	public BaseCache(int cacheSize) {
		MAX_CACHE_SIZE = cacheSize;
		int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTOR) + 1;
		map = new LinkedHashMap(capacity, DEFAULT_LOAD_FACTOR, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry eldest) {
				return size() > MAX_CACHE_SIZE;
			}
		};
	}
	
	public synchronized void put(K key, V value) {
		map.put(key, value);
	}
	
	public synchronized V get(K key) {
		return map.get(key);
	}
	
	public synchronized void remove(K key) {
		map.remove(key);
	}
	
	public synchronized Set<Map.Entry<K, V>> getAll() {
		return map.entrySet();
	}
	
	public synchronized int size() {
		return map.size();
	}
	
	public synchronized void clear() {
		map.clear();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry entry: map.entrySet()) {
			sb.append(String.format("%s:%s", entry.getKey(), entry.getValue()));
		}
		return sb.toString();
	}
}
