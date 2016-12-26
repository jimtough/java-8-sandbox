package com.jimtough.ch04;

import static org.junit.Assert.*;

import org.junit.Test;

public class GenericsTest {
	
	@Test
	public void testSum() {
		GenericInterface<String, Float> genInterface = new GenericClass();
		
		// The last parameter to assertEquals() is the margin for (rounding) error
		assertEquals(70.2D, genInterface.sum(12.3f, 23.4f, 34.5f), 0.000001D);
	}
	
	@Test
	public void testLogMe() {
		GenericInterface<String, Float> genInterface = new GenericClass();
		
		genInterface.logMe("foobar");
	}

	@Test
	public void testMaxA() {
		GenericInterface<String, Float> genInterface = new GenericClass();

		Byte b = genInterface.max(Byte.valueOf((byte)3), Byte.valueOf((byte)6));
		
		assertEquals(Byte.valueOf((byte)6), b);
	}

	@Test
	public void testMaxB() {
		GenericInterface<String, Float> genInterface = new GenericClass();

		Double d = genInterface.max(Double.valueOf(99.99999), Double.valueOf(66.66666));
		
		assertEquals(Double.valueOf(99.99999), d);
	}
	
}
