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

import java.io.IOException;
import java.util.Properties;

/**
 * This is a helper class for looking up informations in the resource files
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class Configuration {
	
	/**
	 * make a properties which is a subclass of HashTable
	 */
	private static Properties properties = new Properties();
	private static Configuration configuration = null;
	
	/**
	 * no arguments constructor
	 */
	private Configuration() {
		//prevent from multiple instances
	}
	
	/**
	 * singleton pattern for global use
	 * get the only instance of the Configuration
	 * @param resource resource name
	 * @return configuration instance
	 */
	public static Configuration newInstance() {
		if(configuration == null) {
			synchronized (Configuration.class) {
				configuration = new Configuration();
			}
		}
		return configuration;
	}
	
	/**
	 * get the value of the specified key
	 * @param key the name of the value
	 * @return the String value with the specified key
	 */
	public String getValue(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * update the properties with the specified key and value
	 * @param key
	 * @param value
	 */
	public void updateProperties(String key, String value) {
		properties.setProperty(key, value);
    }
	
	/**
	 * load the resource with the specified resource name
	 * @param resource the resource with the corresponding name
	 */
	public static void loadProperties(String resource) {
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
