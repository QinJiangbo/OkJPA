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
package com.qinjiangbo.enums;

import com.qinjiangbo.annotations.Delete;
import com.qinjiangbo.annotations.Insert;
import com.qinjiangbo.annotations.Select;
import com.qinjiangbo.annotations.Update;

@SuppressWarnings("rawtypes")
public enum JdbcOperations {
	SELECT("Select", Select.class),
	INSERT("Insert", Insert.class),
	DELETE("Delete", Delete.class),
	UPDATE("Update", Update.class);
	
	private String name;
	private Class clazz;
	
	private JdbcOperations(String name, Class clazz) {
		this.name = name;
		this.clazz = clazz;
	}
	
	public String getName() {
		return name;
	}
	
	public Class getClazz() {
		return clazz;
	}
}
