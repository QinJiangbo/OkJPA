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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.qinjiangbo.cache.CacheResultMap;
import com.qinjiangbo.cache.MapperCache;
import com.qinjiangbo.enums.JdbcOperations;
import com.qinjiangbo.jdbc.Executor;
import com.qinjiangbo.jdbc.Result;
import com.qinjiangbo.jdbc.SQL;
import com.qinjiangbo.reflection.MapperReflector;
import com.qinjiangbo.session.Session;
/**
 * Mapper proxy class for representing the function the mapper, 
 * implements the <code>InvocationHandler</code> interface;
 * Type T for specified class type
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class MapperProxy<T> implements InvocationHandler{
	
	private Session session;
	private Class<T> mapperInterface;
	
	public MapperProxy(Session session, Class<T> mapperInterface) {
		this.session = session;
		this.mapperInterface = mapperInterface;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		SQL sql = new MapperReflector<T>(mapperInterface).genSQL(method, args);
		String sqlStr = sql.parseSQL();
		Connection connection = session.getConnection();
		PreparedStatement statement = connection.prepareStatement(sqlStr, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		Executor executor = new Executor(statement);
		Object object = null;
		if(JdbcOperations.SELECT.equals(MapperReflector.queryType(method))) {
			CacheResultMap cacheResultMap = MapperCache.getResultMap(mapperInterface.getName());
			ResultSet resultSet = executor.doSelect(sql);
			object = new Result(method, resultSet, cacheResultMap).genResult();
			statement.close();
			return object;
		}
		else {
			int rows = executor.doUpdate(sql);
			statement.close();
			return rows;
		}
	}

}
