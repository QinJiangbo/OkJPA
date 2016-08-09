package com.qinjiangbo.log4j;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {
	//记录日志文件
	private static Logger logger = LoggerFactory.getLogger(Log4jTest.class);
	
	@Before
	public void beforTest() {
		System.out.println("Before Test!");
	}
	
	@After
	public void afterTest() {
		System.out.println("After Test!");
	}
	
	@Test
	public void testLog4j() {
		
		logger.debug("This line can not be shown!");
		
		logger.info("This is info line!");
		
		logger.info("Today is {}", new Date());
		
		logger.warn("This is warn line!");
		
		logger.warn("This is warn of {}", "Richard");
		
		logger.error("This is error line!");
	}
}
