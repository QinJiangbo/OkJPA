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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.qinjiangbo.cache.CacheResultMap;
import com.qinjiangbo.enums.ResultTypes;
import com.qinjiangbo.exceptions.ResultTypeException;
import com.qinjiangbo.reflection.MethodReflector;
import com.qinjiangbo.reflection.ParamReflector;
/**
 * Result class for processing the type of the return values,
 * and cast them into appropriate type
 * @author QinJiangbo
 * @date 2016-03-16
 */
@SuppressWarnings("rawtypes")
public class Result {
	
	private Method method;
	private ResultSet resultSet;
	private CacheResultMap cacheResultMap;
	
	public Result(Method method, ResultSet resultSet, CacheResultMap cacheResultMap) {
		this.method = method;
		this.resultSet = resultSet;
		this.cacheResultMap = cacheResultMap;
	}
	
	@SuppressWarnings("unchecked")
	public Object genResult() {
		ResultTypes resultTypes = findResultTypes();
		String resultType = MethodReflector.findResultTypes(method);
		int index = 0, endIndex = 0;
		switch (resultTypes) {
		case GenericPrimitive:
			index = resultType.indexOf("<");
			endIndex = resultType.indexOf(">");
			String param1 = resultType.substring(index+1, endIndex);
			try {
				int colNum = resultSet.getMetaData().getColumnCount();
				if(colNum > 1) {
					throw new ResultTypeException("cloumn numbers dismatch!");
				}
				Class paramC = Class.forName(param1);
				return getPrimitiveListResult(resultSet, paramC);
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case GenericCompound:
			index = resultType.indexOf("<");
			endIndex = resultType.indexOf(">");
			String param2 = resultType.substring(index+1, endIndex);
			try {
				int colNum = resultSet.getMetaData().getColumnCount();
				if(colNum == 1) {
					throw new ResultTypeException("column numbers dismatch!");
				}
				Class paramC = Class.forName(param2);
				return getCompoundListResult(resultSet, paramC);
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case NormalMap:
			Map map = getMapResult(resultSet);
			return map;
		case NormalPrimitive:
			Class paramC = ParamReflector.getClass(resultType);
			return getPrimitiveResult(resultSet, paramC);
		case MapArray:
			Map[] maps = getMapArrayResult(resultSet);
			return maps;
		case NormalPoJo:
			Class param2C = ParamReflector.getClass(resultType);
			return getPojoResult(resultSet, param2C);
			
		}
		return null;
	}
	
	/**
	 * get the result types of the method
	 * @return result types
	 */
	public ResultTypes findResultTypes() {
		String resultType = MethodReflector.findResultTypes(method);
		int index = resultType.indexOf("<");
		if(index > 0) {
			String genericStr = resultType.substring(0, index);
			if(genericStr.contains("Map")) {
				throw new ResultTypeException("return type is illegal!");
			}
			int endIndex = resultType.indexOf(">");
			String genericParam = resultType.substring(index+1, endIndex);
			try {
				Class c = Class.forName(genericParam);
				boolean isPrimitive = ParamReflector.isPrimitive(c);
				if(isPrimitive) {
					return ResultTypes.GenericPrimitive;
				}
				return ResultTypes.GenericCompound;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			if(resultType.contains("List")) {
				throw new ResultTypeException("return type is illegal!");
			}
			else if(resultType.contains("Map[]")){
				return ResultTypes.MapArray;
			}
			else if (resultType.contains("Map")) {
				return ResultTypes.NormalMap;
			} 
			else if(ParamReflector.isPrimitive(ParamReflector.getClass(resultType))) {
				return ResultTypes.NormalPrimitive;
			}
			else {
				return ResultTypes.NormalPoJo;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> getPrimitiveListResult(ResultSet resultSet, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		try {
			while(resultSet.next()) {
				list.add((T) ParamReflector.castObject(clazz, resultSet.getObject(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> getCompoundListResult(ResultSet resultSet, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		String methodID = ParamReflector.getMethodID(method);
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		if(cacheResultMap != null) {
			map = cacheResultMap.get(methodID);
		}
		try {
			Object object = clazz.newInstance();
			int colNum = resultSet.getMetaData().getColumnCount();
			ResultSetMetaData rsm = resultSet.getMetaData();
			while(resultSet.next()) {
				for(int i=1; i<=colNum; i++) {
					String colName = rsm.getColumnName(i);
					Field field;
					if(map != null) {
						String fieldName = map.get(colName);
						field = clazz.getDeclaredField(fieldName);
					}else {
						field = clazz.getDeclaredField(colName);
					}
					field.setAccessible(true);
					field.set(object, resultSet.getObject(i));
				}
				list.add((T) ParamReflector.castObject(clazz, object));
			}
		} catch (InstantiationException | IllegalAccessException | SQLException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Map getMapResult(ResultSet resultSet) {
		Map map = new HashMap();
		if (getRowCount(resultSet) > 1) {
			throw new ResultTypeException("return type is illegal!");
		}
		try {
			ResultSetMetaData rsm = resultSet.getMetaData();
			int colNum = rsm.getColumnCount();
			for(int i = 1; i <= colNum; i++) {
				map.put(rsm.getColumnName(i), resultSet.getObject(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public <T> Object getPrimitiveResult(ResultSet resultSet, Class<T> clazz) {
		Object object = null;
		if (getRowCount(resultSet) > 1) {
			throw new ResultTypeException("return type is illegal!");
		}
		try {
			ResultSetMetaData rsm = resultSet.getMetaData();
			int colNum = rsm.getColumnCount();
			if(colNum > 1) {
				throw new ResultTypeException("return type is illegal!");
			}
			for(int i = 1; i <= colNum; i++) {
				object = resultSet.getObject(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ParamReflector.castObject(clazz, object);
	}
	
	@SuppressWarnings("unchecked")
	public Map[] getMapArrayResult(ResultSet resultSet) {
		int rowCount = getRowCount(resultSet);
		Map[] maps = new HashMap[rowCount];
		try {
			ResultSetMetaData rsm = resultSet.getMetaData();
			int colNum = rsm.getColumnCount();
			int count = 0;
			while(resultSet.next()) {
				Map map = new HashMap();
				for(int i = 1; i <= colNum; i++) {
					map.put(rsm.getColumnName(i), resultSet.getObject(i));
				}
				maps[count] = map;
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	public <T> T getPojoResult(ResultSet resultSet, Class<T> clazz) {
		String methodID = ParamReflector.getMethodID(method);
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		if(cacheResultMap != null) {
			map = cacheResultMap.get(methodID);
		}
		try {
			Object object = clazz.newInstance();
			int colNum = resultSet.getMetaData().getColumnCount();
			ResultSetMetaData rsm = resultSet.getMetaData();
			while(resultSet.next()) {
				for(int i=1; i<=colNum; i++) {
					String colName = rsm.getColumnName(i);
					Field field;
					if(map.size() != 0) {
						String fieldName = map.get(colName);
						field = clazz.getDeclaredField(fieldName);
					}else {
						field = clazz.getDeclaredField(colName);
					}
					field.setAccessible(true);
					field.set(object, resultSet.getObject(i));
				}
			}
			return clazz.cast(object);
		} catch (InstantiationException | IllegalAccessException | SQLException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * find the row number of the result set
	 * @param resultSet result set to be dealt with
	 * @return the row number of the result set
	 */
	private int getRowCount(ResultSet resultSet) {
		int rowCount = 0;
		try {
			resultSet.last();
			rowCount = resultSet.getRow();
			resultSet.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCount;
	}
	
}
