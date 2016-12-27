package com.jimtough.ch04;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamTerminalOperationsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamTerminalOperationsTest.class);
	
	private static final String A = "alpha";
	private static final String B = "bravo";
	private static final String C = "charlie";
	private static final String D = "delta";
	private static final String E = "echo";
	private static final String F = "foxtrot";
	private static final String G = "golf";
    
	private List<String> stringList;
	
	@Before
	public void setUp() {
		stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testForEachA() {
		stringList.forEach(s -> LOGGER.debug("s: [{}]", s));
	}

	@Test
	public void testForEachB() {
		final AtomicInteger counter = new AtomicInteger(0);
		
		stringList.forEach(s -> counter.incrementAndGet());
		
		assertEquals(stringList.size(), counter.get());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testToArray() {
		Object[] output = stringList.stream().toArray();
		
		LOGGER.debug(Arrays.toString(output));
		assertEquals(7, output.length);
		assertTrue(Arrays.asList(output).containsAll(stringList));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testMinA() {
		Optional<String> minValue = stringList.stream().min(String.CASE_INSENSITIVE_ORDER);
		
		assertEquals(A, minValue.get());
	}
	
	@Test
	public void testMinB() {
		// Get the min value from a stream of 10000 random ints between zero and one million
		OptionalInt minValue = (new Random()).ints(0,1000000).limit(10000).min();

		LOGGER.debug("minValue: {}", minValue);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCount() {
		long count = (new Random()).ints(0,10).limit(999).count();

		assertEquals(999, count);
	}

	//--------------------------------------------------------------------
	
	@Test(expected=IllegalStateException.class)
	public void testStreamCannotBeReused() {
		Stream<String> stream = stringList.stream();
		
		// 'count()' is a terminal operation. After this invocation, 'stream' cannot be used again.
		stream.count();
		// This will cause an exception
		stream.forEach(s -> LOGGER.debug("s: [{}]", s));
	}
	
}



