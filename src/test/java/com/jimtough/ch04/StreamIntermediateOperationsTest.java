package com.jimtough.ch04;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamIntermediateOperationsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamIntermediateOperationsTest.class);
	
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
	
	private void verifyFilter(Object[] output) {
		LOGGER.debug(Arrays.toString(output));
		assertEquals(2, output.length);
		assertTrue(Arrays.asList(output).containsAll(Arrays.asList(E,G)));
	}
	
	@Test
	public void testFilterA() {
		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean test(String s) {
				if (s.length() == 4) {
					return true;
				}
				return false;
			}
		};
		
		// Use the 'filter()' intermediate operation with a Predicate instance
		Object[] output = stringList.stream().filter(pred).toArray();

		verifyFilter(output);
	}

	@Test
	public void testFilterB() {
		// Use the 'filter()' intermediate operation with a Lambda expression that evaluates to a boolean result
		Object[] output = stringList.stream().filter(s -> s.length() == 4).toArray();
		
		verifyFilter(output);
	}

	//--------------------------------------------------------------------

	@Test
	public void testFilterIntegersUsingLambda() {
		// Use the 'filter()' intermediate operation with a Lambda expression that evaluates to a boolean result.
		// This expression should keep any even numbers and filter out any odd numbers.
		int[] output = IntStream.rangeClosed(0,10).filter(i -> (i % 2) == 0).toArray();
		
		LOGGER.debug(Arrays.toString(output));
		assertEquals(6, output.length);
	}

	private static boolean isEven(int i) {
		return (i % 2) == 0;
	}
	
	@Test
	public void testFilterIntegersUsingStaticMethodReference() {
		// Use the 'filter()' intermediate operation with an appropriate static method reference.
		// This expression should keep any even numbers and filter out any odd numbers.
		int[] output = IntStream.rangeClosed(0,10).filter(StreamIntermediateOperationsTest::isEven).toArray();
		
		LOGGER.debug(Arrays.toString(output));
		assertEquals(6, output.length);
	}

	//--------------------------------------------------------------------

	private void verifyMap(Object[] output) {
		LOGGER.debug(Arrays.toString(output));
		assertEquals(7, output.length);
		assertTrue(Arrays.asList(output).containsAll(Arrays.asList(
				A.toUpperCase(),
				B.toUpperCase(),
				C.toUpperCase(),
				D.toUpperCase(),
				E.toUpperCase(),
				F.toUpperCase(),
				G.toUpperCase()
			)));
	}
	
	@Test
	public void testMapA() {
		Function<String,String> mapper = new Function<String,String>() {
			@Override
			public String apply(String s) {
				return s.toUpperCase();
			}
		};
		
		// Use the 'map()' intermediate operation with a Function instance
		Object[] output = stringList.stream().map(mapper).toArray();

		verifyMap(output);
	}
	
	@Test
	public void testMapB() {
		// Use the 'map()' intermediate operation with a Lambda expression that returns a transformed string
		Object[] output = stringList.stream().map(s->s.toUpperCase()).toArray();

		verifyMap(output);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testDistinct() {
		List<String> stringListWithDuplicates = new ArrayList<>();
		stringListWithDuplicates.addAll(Arrays.asList(A,B,A,B,C,D,C,D,E,F,E,F,G,G,G,G,G,G,G,G,G,G,G));
		
		// Use the 'distinct()' intermediate operation to remove duplicates from the stream
		Object[] output = stringListWithDuplicates.stream().distinct().toArray();

		LOGGER.debug(Arrays.toString(output));
		assertEquals(7, output.length);
		assertTrue(Arrays.asList(output).containsAll(stringList));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testSorted() {
		List<String> unsortedStringList = new ArrayList<>();
		unsortedStringList.addAll(Arrays.asList(D,E,A,G,B,C,F));
		
		// Use the 'sorted()' intermediate operation to sort the stream contents in natural order
		Object[] output = unsortedStringList.stream().sorted().toArray();

		LOGGER.debug(Arrays.toString(output));
		assertEquals(7, output.length);
		assertEquals(A, output[0]);
		assertEquals(G, output[6]);
		assertTrue(Arrays.asList(output).containsAll(stringList));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testPeekMapPeek() {
		// The 'peek()' intermediate operation seems suited to be a debugging tool
		Object[] output = stringList.stream()
				.peek(s->LOGGER.debug("before: [{}]",s))
				.map(s->s.toUpperCase())
				.peek(s->LOGGER.debug("after : [{}]",s))
				.toArray();

		verifyMap(output);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testLimit() {
		// The 'limit()' intermediate operation will discard any elements after element x
		final int x = 3;
		Object[] output = stringList.stream()
				.peek(s->LOGGER.debug("before: [{}]",s))
				.limit(x)
				.peek(s->LOGGER.debug("after : [{}]",s))
				.toArray();

		assertEquals(3, output.length);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testMultipleIntermediateOperations() {
		List<String> unsortedStringListWithDups = new ArrayList<>();
		unsortedStringListWithDups.addAll(Arrays.asList(G,E,A,B,C,C,D,E,G,F,A,C,D,B,D,G,F,A,C));
		
		Object[] output = unsortedStringListWithDups.stream()
				// remove duplicates
				.distinct()
				// get rid of the D's
				.filter(s -> !s.equals(D))
				// sort the stream
				.sorted()
				// convert all to uppercase
				.map(s -> s.toUpperCase())
				.toArray();

		LOGGER.debug(Arrays.toString(output));
		assertEquals(6, output.length);
	}
	
}
