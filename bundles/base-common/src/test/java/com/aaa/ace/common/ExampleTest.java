package com.aaa.ace.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExampleTest {

	@Test
	public void testingHelloWorld() {
		assertEquals("Here is test for Hello World String: ", "Hello + World", helloWorld());
	}

	public String helloWorld() {
		String helloWorld = "Hello +" + " World";
		return helloWorld;
	}
}
