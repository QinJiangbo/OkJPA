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
package com.qinjiangbo.jdbc;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qinjiangbo.reflection.ParamReflector;
import com.qinjiangbo.reflection.PojoReflector;
import com.qinjiangbo.util.MapUtil;
/**
 * Used for SQL statements generation and procession
 * @author QinJiangbo
 * @date 2016-03-16
 */
@SuppressWarnings("rawtypes")
public class SQL{
	
	private String sql = null;
	private Map map = null;
	
	public SQL(String rawSql, Map map) {
		this.sql = rawSql;
		this.map = map;
	}
	
	/**
	 * get the executable SQL statement
	 */
	public String parseSQL() {
		if(map == null) {
			return sql;
		}
		else if(map.size() > 1) {
			sql = replace(sql, map);
		}
		else {
			if(ParamReflector.isPrimitive(MapUtil.getMapValue(map).getClass())) {
				sql = replace(sql, map);
			}
			else {
				sql = replaceObj(sql, map);
			}
		}
		return sql;
	}
	
	/**
	 * replace the expression ${*} in the raw SQL statement
	 * @param sql SQL statement
	 * @param params parameter map array
	 */
	private String replace(String sql, Map params) {
		/* special characters need \\ to escape */
		Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}");
		Matcher matcher = pattern.matcher(sql);
		String matchStr = null;
		while(matcher.find()) {
			matchStr = matcher.group();
			String originStr = matchStr;
			matchStr = matchStr.substring(2, matchStr.length() - 1).replaceAll("^\\s+|\\s+$", "");
			Object paramValue = params.get(matchStr);
			//String object needs quotas
			if(paramValue instanceof String
					|| paramValue instanceof Character) {
				paramValue = "'"+paramValue+"'";
			}
			sql = sql.replace(originStr, paramValue.toString());
			matcher = pattern.matcher(sql);
		}
		return sql;
	}
	
	/**
	 * replace the object's properties in the SQL statement
	 * @param sql
	 * @param map
	 */
	private String replaceObj(String sql, Map map) {
		//object which needs to be persisted
		Object pojo = MapUtil.getMapValue(map);
		Map params = PojoReflector.parsePojo(pojo);
		return replace(sql, params);
	}
	
	@Override
	public String toString() {
		return sql;
	}
	
}
