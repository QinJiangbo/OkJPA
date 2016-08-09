package com.qinjiangbo.mapper;

import java.util.List;
import java.util.Map;

import com.qinjiangbo.annotations.Delete;
import com.qinjiangbo.annotations.Insert;
import com.qinjiangbo.annotations.Param;
import com.qinjiangbo.annotations.Result;
import com.qinjiangbo.annotations.Results;
import com.qinjiangbo.annotations.Select;
import com.qinjiangbo.annotations.Update;
import com.qinjiangbo.pojo.User;

public interface UserMapper {
	
	@Insert("insert into User(name, age, location) values (${name}, ${age}, ${location})")
	public int addUser(User user);
	
	@Select("select * from User where user_id = ${userID}")
	@Results
	({
		@Result(column="user_id", field="userID"),
		@Result(column="name", field="name"),
		@Result(column="age", field="age"),
		@Result(column="location", field="location")
	})
	public User findUser(@Param("userID") Long userID);
	
	@Select("select * from User where age = ${ age }")
	@Results
	({
		@Result(column="user_id", field="userID"),
		@Result(column="name", field="name"),
		@Result(column="age", field="age"),
		@Result(column="location", field="location")
	})
	public List<User> findUsers(@Param("age") Integer age);
	
	@SuppressWarnings("rawtypes")
	@Select("select name, age, location from User where user_id = ${ userID }")
	public Map getUserInfo(@Param("userID") Long userID);
	
	@Select("select user_id from User")
	public List<Long> getUserIds();
	
	@SuppressWarnings("rawtypes")
	@Select("select user_id, age from User where name = ${name}")
	public Map[] getInfosByName(@Param("name") String name);
	
	@Update("update User set name = 'Lily' where age = ${age}")
	public int updateUserInfo(@Param("age") Integer age);
	
	@Delete("delete from User where name = ${ name}")
	public int deleteUsers(@Param("name") String name);
	
	@Select("select count(*) n from User")
	public int getUserCount();
	
	@Select("select * from User where user_id = 648732678412322")
	@Results
	({
		@Result(column="user_id", field="userID"),
		@Result(column="name", field="name"),
		@Result(column="age", field="age"),
		@Result(column="location", field="location")
	})
	public User findUser();
	
	@Select("select * from User where age = 18")
	@Results
	({
		@Result(column="user_id", field="userID"),
		@Result(column="name", field="name"),
		@Result(column="age", field="age"),
		@Result(column="location", field="location")
	})
	public List<User> findUsers();
	
	@SuppressWarnings("rawtypes")
	@Select("select name, age, location from User where user_id = 648732678412322")
	public Map getUserInfo();
	
	@Select("select user_id from User where age = ${age}")
	public List<Long> getUserIds(@Param("age") int age);
	
	@SuppressWarnings("rawtypes")
	@Select("select user_id, age from User")
	public Map[] getInfosByName();
	
}
