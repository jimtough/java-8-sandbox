package com.jimtough.ch04;

import static com.jimtough.ch04.Ch04Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamTerminalOperationsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamTerminalOperationsTest.class);
    
	private List<String> stringList;
	
	@Before
	public void setUp() {
		stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testForEach_trivial() {
		stringList.forEach(s -> LOGGER.debug("s: [{}]", s));
	}

	@Test
	public void testForEach_WRONG() {
		final AtomicInteger counter = new AtomicInteger(0);

		// INCORRECT USE OF A STREAM!
		// Read Effective Java 3rd Edition for an explanation of why this is
		// considered a 'side-effect' and is not a correct use of streams.
		stringList.forEach(s -> counter.incrementAndGet());
		
		assertEquals(stringList.size(), counter.get());
	}

	@Test
	public void testForEach_RIGHT() {
		// Use the built-in count() terminal operation
		long count = stringList.stream().count();
		
		assertEquals((long)stringList.size(), count);
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
		final ThreadLocalRandom rnd = ThreadLocalRandom.current();

		final int minIntFromStreamOfRandomValues;
		// Create an IntStream of random int values between 0 (inclusive) and 1,000,001 (exclusive)
		try (IntStream intStream = rnd.ints(10000, 0, 1_000_001)) {
			// The min() operation returns an OptionalInt. Since we know it will
			// contain a value, just chain the getAsInt() to the method calls.
			minIntFromStreamOfRandomValues = intStream.min().getAsInt();
		}

		LOGGER.debug("minIntFromStreamOfRandomValues: {}", minIntFromStreamOfRandomValues);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCount() {
		try (IntStream intStream = ThreadLocalRandom.current().ints(999, 0, 10)) {
			assertEquals(999, intStream.limit(999).count());
		}
	}

	//--------------------------------------------------------------------
	
	@Test(expected=IllegalStateException.class)
	public void testStreamCannotBeReusedAfterTerminalOperationIsInvoked() {
		Stream<String> stream = stringList.stream();
		
		// 'count()' is a terminal operation. After this invocation, 'stream' cannot be used again.
		stream.count();
		// This will cause an exception
		stream.forEach(s -> LOGGER.debug("s: [{}]", s));
	}
	
}



