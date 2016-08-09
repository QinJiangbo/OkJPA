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

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Session interface, it's implemented by DefaultSession class
 * which produced database service via JDBC connection
 * @author QinJiangbo
 * @date 2016-03-16
 */
public interface Session extends Closeable{
	
	public Connection getConnection();
	
	public <T> T getMapper(Class<T> clazz);
	
	public void commit() throws SQLException;
	
	public void rollback() throws SQLException;
}
