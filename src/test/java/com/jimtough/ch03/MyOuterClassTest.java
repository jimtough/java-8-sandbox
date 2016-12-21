package com.jimtough.ch03;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jimtough.ch03.MyOuterClass.FooGetter.FooBarGetter;
import com.jimtough.ch03.MyOuterClass.FooGetter.FooBarGetter.FooBarBazGetter;
import com.jimtough.ch03.MyOuterClass.MyInnerClass;
import com.jimtough.ch03.MyOuterClass.MyStaticInnerClass;

public class MyOuterClassTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyOuterClassTest.class);

	private MyOuterClass outer;
	
	@Before
	public void setUp() {
		outer = new MyOuterClass();
	}
	
	@Test
	public void testMyInnerClass() {
		MyInnerClass inner = outer.new MyInnerClass();
		assertNotEquals(outer.foo, inner.getFoo());
		assertEquals(outer.bar, inner.getBar());
		LOGGER.debug("inner.getFoo(): {}", inner.getFoo());
		LOGGER.debug("inner.getBar(): {}", inner.getBar());
	}
	
	@Test
	public void testMyStaticInnerClass() {
		MyStaticInnerClass staticInner = new MyStaticInnerClass();
		assertEquals(MyOuterClass.baz, staticInner.getBaz());
		LOGGER.debug("staticInner.getBaz(): {}", staticInner.getBaz());
	}
	
	@Test
	public void testDoSomethingLocal() {
		FooBarGetter localInner = outer.doSomethingLocal();
		assertNotEquals(outer.foo, localInner.getFoo());
		assertEquals(outer.bar, localInner.getBar());
		LOGGER.debug("localInner.getFoo(): {}", localInner.getFoo());
		LOGGER.debug("localInner.getBar(): {}", localInner.getBar());
	}
	
	@Test
	public void testDoSomethingAnonymous() {
		FooBarBazGetter anonymousInner = outer.doSomethingAnonymous();
		assertNotEquals(outer.foo, anonymousInner.getFoo());
		assertNotEquals(outer.bar, anonymousInner.getBar());
		assertEquals(MyOuterClass.baz, anonymousInner.getBaz());
		LOGGER.debug("anonymousInner.getFoo(): {}", anonymousInner.getFoo());
		LOGGER.debug("anonymousInner.getBar(): {}", anonymousInner.getBar());
		LOGGER.debug("anonymousInner.getBaz(): {}", anonymousInner.getBaz());
	}
	
}
