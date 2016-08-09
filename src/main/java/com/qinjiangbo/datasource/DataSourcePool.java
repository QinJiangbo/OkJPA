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
package com.qinjiangbo.datasource;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.qinjiangbo.enums.PropertyNames;
import com.qinjiangbo.util.Configuration;
/**
 * This class is data source pool, which stores the SQL connections of the database;
 * the number initial connections of the data source pool is 1000, 
 * you can set the number in the properties file
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class DataSourcePool implements DataSource{
	
	private static String url;
	private static String username;
	private static String password;
	
	private static LinkedList<Connection> connections = new LinkedList<Connection>();
	/**
	 * default capacity of the pool size
	 */
	private static int capacity = 50;
	
	static {
		try {
			Class.forName(Configuration.newInstance().getValue(PropertyNames.DRIVER.getValue()));
			url = Configuration.newInstance().getValue(PropertyNames.URL.getValue());
			username = Configuration.newInstance().getValue(PropertyNames.USERNAME.getValue());
			password = Configuration.newInstance().getValue(PropertyNames.PASSWORD.getValue());
			capacity = Configuration.newInstance().getValue(PropertyNames.CAPACITY.getValue()) == null ? 1000 :
				Integer.parseInt(Configuration.newInstance().getValue(PropertyNames.CAPACITY.getValue()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static DataSourcePool dataSourcePool = null;
	
	/**
	 * initialize the connection pool when the constructor invoked
	 */
	private DataSourcePool() {
		try {
			for(int i=0; i<capacity; i++) {
				Connection connection = DriverManager.getConnection(url, username, password);
				connections.push(connection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Singleton Pattern
	 * fetch the dataSourcePool instance
	 * @return dataSourcePool
	 */
	public static DataSourcePool newInstance() {
		if(dataSourcePool == null) {
			synchronized (DataSourcePool.class) {
				if(dataSourcePool == null) {
					dataSourcePool = new DataSourcePool();
				}
			}
		}
		return dataSourcePool;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if(connections.size() > 0) {
			final Connection connection = connections.pop();
			return (Connection) Proxy.newProxyInstance(DataSourcePool.class.getClassLoader(), 
					connection.getClass().getInterfaces(), 
					new InvocationHandler() {
						
						@Override
						public Object invoke(Object proxy, Method method, Object[] args)
								throws Throwable {
							if(method.getName().equals("close")) {
								connections.push(connection);
								return null;
							}
							return method.invoke(connection, args);
						}
					});
		}
		throw new RuntimeException("connections run out!");
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		DriverManager.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		DriverManager.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException(getClass().getName() + " is not a wrapper.");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return null;
	}
	
}
