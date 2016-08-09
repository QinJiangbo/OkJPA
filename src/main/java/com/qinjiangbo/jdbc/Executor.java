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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Executor class for executing the SQL statements
 * @author QinJiangbo
 * @date 2016-03-16
 */
public class Executor {
	
	/**
	 * statement to deal with
	 */
	private Statement statement;
	
	public Executor(Statement statement) {
		this.statement = statement;
	}
	
	/**
	 * do select for getting data from the database
	 * @param sql the query SQL to be executed
	 * @return ResultSet type result
	 */
	public ResultSet doSelect(SQL sql) {
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * do update for updating the data in database
	 * @param sql the update SQL to be executed
	 * @return the influenced rows
	 */
	public int doUpdate(SQL sql) {
		int rows = 0;
		try {
			rows = statement.executeUpdate(sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
}
