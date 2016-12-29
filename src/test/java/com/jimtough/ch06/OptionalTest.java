package com.jimtough.ch06;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playing around with the {@code Optional} class.
 * Looks like the class of the same name from the Google Guava library for Java 6+.
 * 
 * @author JTOUGH
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OptionalTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OptionalTest.class);

	@Rule
	public TestName testName = new TestName();

	//--------------------------------------------------------------------
	
	@Test
	public void testCreateEmptyOptional() {
		Optional<String> emptyA = Optional.empty();
		Optional<String> emptyB = Optional.ofNullable(null);
		
		LOGGER.debug("emptyA: {} | {}", emptyA, testName.getMethodName());
		LOGGER.debug("emptyB: {} | {}", emptyB, testName.getMethodName());
		assertEquals(emptyA, emptyB);
		assertFalse(emptyA.isPresent());
		assertFalse(emptyB.isPresent());
	}

	@Test
	public void testCreateNonEmptyOptional() {
		Optional<String> nonEmpty = Optional.of("foobar");
		
		LOGGER.debug("nonEmpty: {} | {}", nonEmpty, testName.getMethodName());
		assertTrue(nonEmpty.isPresent());
		assertEquals("foobar", nonEmpty.get());
	}

	@Test(expected=NullPointerException.class)
	public void testOptionalOfWithNullParameter() {
		// Not permitted - must use Optional.ofNullable()
		Optional.of(null);
	}

	//--------------------------------------------------------------------

	@Test
	public void testStreamFromOptionalUsingMap() {
		Optional<String> empty = Optional.empty();
		Optional<String> nonEmpty = Optional.of("foobar");

		// The 'map()' operations below map the type from Optional<String> to Optional<Integer> 
		Optional<Integer> emptyResult = empty.map(String::length);
		Optional<Integer> nonEmptyResult = nonEmpty.map(String::length);

		// The 'orElse()' operation will return the value of the Optional, OR ELSE an alternate specified value
		Integer emptyOrElseResult = empty.map(String::length).orElse(-1);
		Integer nonEmptyOrElseResult = nonEmpty.map(String::length).orElse(-1);

		LOGGER.debug("emptyResult: {} | {}", emptyResult, testName.getMethodName());
		LOGGER.debug("nonEmptyResult: {} | {}", nonEmptyResult, testName.getMethodName());
		LOGGER.debug("emptyOrElseResult: {} | {}", emptyOrElseResult, testName.getMethodName());
		LOGGER.debug("nonEmptyOrElseResult: {} | {}", nonEmptyOrElseResult, testName.getMethodName());
		assertFalse(emptyResult.isPresent());
		assertTrue(nonEmptyResult.isPresent());
		assertEquals(-1, emptyOrElseResult.intValue());
		assertEquals(6, nonEmptyOrElseResult.intValue());
	}

	@Test
	public void testStreamFromOptionalUsingFilter() {
		Optional<String> empty = Optional.empty();
		Optional<String> nonEmpty = Optional.of("foobar");

		Optional<String> emptyFilterResult = empty.filter(s->s.equals("foobar"));
		Optional<String> nonEmptyFilterMatchedResult = nonEmpty.filter(s->s.equals("foobar"));
		Optional<String> nonEmptyFilterUnmatchedResult = nonEmpty.filter(s->s.equals("will not match"));

		LOGGER.debug("emptyFilterResult: {} | {}", emptyFilterResult, testName.getMethodName());
		LOGGER.debug("nonEmptyFilterMatchedResult: {} | {}", nonEmptyFilterMatchedResult, testName.getMethodName());
		LOGGER.debug("nonEmptyFilterUnmatchedResult: {} | {}", nonEmptyFilterUnmatchedResult, testName.getMethodName());
		assertFalse(emptyFilterResult.isPresent());
		assertTrue(nonEmptyFilterMatchedResult.isPresent());
		assertFalse(nonEmptyFilterUnmatchedResult.isPresent());
	}


	
	//--------------------------------------------------------------------
	// Optional variants for primitives
	//--------------------------------------------------------------------
	
	@Test
	public void testOptionalInt() {
		OptionalInt empty = OptionalInt.empty();
		OptionalInt nonEmpty = OptionalInt.of(123);
		
		LOGGER.debug("empty: {} | {}", empty, testName.getMethodName());
		LOGGER.debug("nonEmpty: {} | {}", nonEmpty, testName.getMethodName());
		assertFalse(empty.isPresent());
		assertTrue(nonEmpty.isPresent());
		assertEquals(-666, empty.orElse(-666));
		assertEquals(123, nonEmpty.getAsInt());
	}
	
	@Test
	public void testOptionalLong() {
		OptionalLong empty = OptionalLong.empty();
		OptionalLong nonEmpty = OptionalLong.of(123L);
		
		LOGGER.debug("empty: {} | {}", empty, testName.getMethodName());
		LOGGER.debug("nonEmpty: {} | {}", nonEmpty, testName.getMethodName());
		assertFalse(empty.isPresent());
		assertTrue(nonEmpty.isPresent());
		assertEquals(-666L, empty.orElse(-666L));
		assertEquals(123L, nonEmpty.getAsLong());
	}
	
	@Test
	public void testOptionalDouble() {
		OptionalDouble empty = OptionalDouble.empty();
		OptionalDouble nonEmpty = OptionalDouble.of(123.456D);
		
		LOGGER.debug("empty: {} | {}", empty, testName.getMethodName());
		LOGGER.debug("nonEmpty: {} | {}", nonEmpty, testName.getMethodName());
		assertFalse(empty.isPresent());
		assertTrue(nonEmpty.isPresent());
		assertEquals(-66.6D, empty.orElse(-66.6D), 0.0D);
		assertEquals(123.456D, nonEmpty.getAsDouble(), 0.0D);
	}
	
	//--------------------------------------------------------------------
	
}


