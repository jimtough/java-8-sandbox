package com.jimtough.ch02;

import static org.junit.Assert.*;

import org.junit.Test;

public class MySingletonTest {

	@Test
	public void testGetInstanceSingleThreaded() {
		MySingleton ms1 = MySingleton.getInstance();
		MySingleton ms2 = MySingleton.getInstance();
		assertNotNull(ms1);
		assertSame(ms1, ms2);
		MySingleton.getInstance().incrementAndGet();
		ms1.incrementAndGet();
		ms2.incrementAndGet();
		assertEquals(4, MySingleton.getInstance().incrementAndGet());
	}
	
}
