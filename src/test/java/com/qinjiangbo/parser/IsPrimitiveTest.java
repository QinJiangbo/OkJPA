package com.qinjiangbo.parser;

import org.junit.Assert;
import org.junit.Test;

import com.qinjiangbo.reflection.ParamReflector;

public class IsPrimitiveTest {
	
	@Test
	public void testPrimitive() {
		boolean bool = ParamReflector.isPrimitive(Integer.class);
		Assert.assertEquals(true, bool);
	}
}
