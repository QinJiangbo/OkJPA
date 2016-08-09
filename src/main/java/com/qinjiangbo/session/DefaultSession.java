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
package com.qinjiangbo.session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.qinjiangbo.datasource.DataSourcePool;
import com.qinjiangbo.proxy.MapperProxyFactory;

public class DefaultSession implements Session{
	
	/**
	 * each session has a JDBC connection
	 */
	private Connection connection = null;
	
	public DefaultSession() {
		try {
			connection = DataSourcePool.newInstance().getConnection();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DefaultSession(boolean autoCommit) {
		try {
			connection = DataSourcePool.newInstance().getConnection();
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Proxy instance for wrapping class/interface
	 */
	@Override
	public <T> T getMapper(Class<T> mapperInterface) {
		return new MapperProxyFactory<T>(mapperInterface).newInstance(this);
	}

	@Override
	public void close() throws IOException {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void commit() throws SQLException{
		if(connection.getAutoCommit() == false) {
			connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
	}
	
}
