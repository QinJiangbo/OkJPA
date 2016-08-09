package com.qinjiangbo.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

public class RegexTest {
	
	@Test
	public void testRegex() {
		String sql = "insert into user values (${ userId }, ${ name}, ${pwd })";
		Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}");
		Matcher matcher = pattern.matcher(sql);
		boolean result = matcher.find();
		
		System.out.println(result);
		Assert.assertEquals(1, 1);
	}
}
