package com.qinjiangbo.integrated;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qinjiangbo.mapper.UserMapper;
import com.qinjiangbo.pojo.User;
import com.qinjiangbo.session.Session;
import com.qinjiangbo.session.SessionFactory;
import com.qinjiangbo.session.SessionFactoryBuilder;

public class IntegratedTest {
	
	private Session session = null;
	
	@Before
	public void beforeTest() {
		SessionFactory sessionFactory = new SessionFactoryBuilder().build("datasource.properties");
		session = sessionFactory.getSession(true);
	}
	
	@Test
	public void testNormalPrimitive() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		User user = new User();
		user.setName("Cici");
		user.setAge(20);
		user.setLocation("Changsha, Hunan");
		int row = userMapper.addUser(user);
		Assert.assertEquals(1, row);
	}
	
	@Test
	public void testNormalPoJo() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		Long userId = 648732678412321L;
		User user = userMapper.findUser(userId);
		Assert.assertEquals(22, user.getAge().intValue());
	}
	
	@Test
	public void testGenericCompound() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		Integer age = 18;
		List<User> list = userMapper.findUsers(age);
		Assert.assertEquals(2, list.size());
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testNormalMap() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		Long userID = 648732678412321L;
		Map map = userMapper.getUserInfo(userID);
		Assert.assertEquals(22, Integer.parseInt(map.get("age").toString()));
	}
	
	@Test
	public void testGenericPrimitive() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		List<Long> list = userMapper.getUserIds();
		Assert.assertEquals(7, list.size());
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testMapArray() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		Map[] maps = userMapper.getInfosByName("Cici");
		Assert.assertEquals(3, maps.length);
	}
	
	@Test
	public void testUpdate() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		int row = userMapper.updateUserInfo(20);
		Assert.assertEquals(3, row);
	}
	
	@Test
	public void testDelete() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		int row = userMapper.deleteUsers("Lily");
		Assert.assertEquals(3, row);
	}
	
	@Test
	public void testNormalPrimitive2() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		int row = userMapper.getUserCount();
		Assert.assertEquals(4, row);
	}
	
	@Test
	public void testNormalPoJo2() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		User user = userMapper.findUser();
		Assert.assertEquals(11, user.getAge().intValue());
	}
	
	@Test
	public void testGenericCompound2() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		List<User> list = userMapper.findUsers();
		Assert.assertEquals(2, list.size());
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testNormalMap2() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		Map map = userMapper.getUserInfo();
		Assert.assertEquals(11, Integer.parseInt(map.get("age").toString()));
	}
	
	@Test
	public void testGenericPrimitive2() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		List<Long> list = userMapper.getUserIds(18);
		Assert.assertEquals(2, list.size());
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testMapArray2() {
		UserMapper userMapper = session.getMapper(UserMapper.class);
		Map[] maps = userMapper.getInfosByName();
		Assert.assertEquals(4, maps.length);
	}
	
	@After
	public void afterTest() {
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
