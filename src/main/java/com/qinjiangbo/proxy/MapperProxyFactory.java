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
package com.qinjiangbo.proxy;

import java.lang.reflect.Proxy;

import com.qinjiangbo.session.Session;
/**
 * Mapper proxy factory class, invoking the methods of the Class <code>Proxy</code> from 
 * JDK; Proxy.newProxyInstance() creates a proxy for the mapper object
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class MapperProxyFactory<T> {
	
	/**
	 * The Mapper Class / Interface which needs proxy
	 */
	private final Class<T> interfaces;
	
	public MapperProxyFactory(Class<T> mapperInterface) {
	    this.interfaces = mapperInterface;
	}
	
	public Class<T> getMapperInterface() {
		return this.interfaces;
	}
	
	/**
	 * Use the proxy pattern to replace the raw methods
	 * @param mapperProxy
	 * @return the type (Class or Interface)
	 */
	@SuppressWarnings("unchecked")
	private T newInstance(MapperProxy<T> mapperProxy) {
		return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces}, mapperProxy);
	}
	
	public T newInstance(Session session) {
		MapperProxy<T> mapperProxy = new MapperProxy<T>(session, interfaces);
		return newInstance(mapperProxy);
	}
}
