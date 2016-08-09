package com.qinjiangbo.result;

import junit.framework.Assert;

import org.junit.Test;

public class ResultTypeTest {
	
	@Test
	public void testResultType() {
		String resultType = "java.lang.List<java.lang.String>";
		int index = resultType.indexOf("<");
		String actualStr = resultType.substring(0, index);
		String expectStr = "java.lang.List";
		Assert.assertEquals(expectStr, actualStr);
	}
	
	@Test
	public void testParamType() {
		String resultType = "java.lang.List<java.lang.String>";
		int index = resultType.indexOf("<");
		int endIndex = resultType.indexOf(">");
		String actualStr = resultType.substring(index+1, endIndex);
		System.out.println(actualStr);
		String expectStr = "java.lang.String";
		Assert.assertEquals(expectStr, actualStr);
	}
}
