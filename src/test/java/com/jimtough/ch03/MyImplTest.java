package com.jimtough.ch03;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MyImplTest {

	private MyImpl impl;
	
	@Before
	public void setUp() {
		impl = new MyImpl();
	}
	
	@Test
	public void testX() {
		assertEquals(InterfaceA.FOO, impl.getFoo());
		assertEquals(InterfaceB2.BAR, impl.getBar());
		assertEquals(MyImpl.IMPL_BAZ, impl.getBaz());
	}
	
}
