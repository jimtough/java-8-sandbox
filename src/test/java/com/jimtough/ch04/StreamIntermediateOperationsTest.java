package com.jimtough.ch04;

import static com.jimtough.ch04.Ch04Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamIntermediateOperationsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamIntermediateOperationsTest.class);
    
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
	public void testFilterWithExplicitPredicateInstance() {
		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean test(String s) {
				if (s.length() == 4) {
					return true;
				}
				return false;
			}
		};
		
		final Object[] output;
		try (Stream<String> stream = stringList.stream()) {
			// Use the 'filter()' intermediate operation with a Predicate instance
			output = stream.filter(pred).toArray();
		}

		verifyFilter(output);
	}

	@Test
	public void testFilterWithLambdaPredicate() {
		final Object[] output;
		try (Stream<String> stream = stringList.stream()) {
			// Use the 'filter()' intermediate operation with a lambda Predicate
			output = stream.filter(s -> s.length() == 4).toArray();
		}
		
		verifyFilter(output);
	}

	//--------------------------------------------------------------------

	@Test
	public void testFilterIntStreamUsingLambda() {
		final int[] output;
		try (IntStream intStream = IntStream.rangeClosed(0,10)) {
			// Use the 'filter()' intermediate operation with a Lambda expression that evaluates to a boolean result.
			// This expression should keep any even numbers and filter out any odd numbers.
			output = intStream.filter(i -> (i % 2) == 0).toArray();
		}
		
		LOGGER.debug(Arrays.toString(output));
		assertEquals(6, output.length);
	}

	private static boolean isEven(int i) {
		return (i % 2) == 0;
	}
	
	@Test
	public void testFilterIntStreamUsingStaticMethodReference() {
		final int[] output;
		try (IntStream intStream = IntStream.rangeClosed(0,10)) {
			// Use the 'filter()' intermediate operation with an appropriate static method reference.
			// This expression should keep any even numbers and filter out any odd numbers.
			output = intStream.filter(StreamIntermediateOperationsTest::isEven).toArray();
		}
		
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
	public void testMapWithExplicitFunctionReference() {
		// So verbose. So ugly.
		Function<String,String> mapper = new Function<String,String>() {
			@Override
			public String apply(String s) {
				return s.toUpperCase();
			}
		};

		final Object[] output;
		try (Stream<String> stream = stringList.stream()) {
			// Use the 'map()' intermediate operation with a Function instance
			output = stream.map(mapper).toArray();
		}

		verifyMap(output);
	}
	
	@Test
	public void testMapWithLamdbaMappingFunction() {
		final Object[] output;
		try (Stream<String> stream = stringList.stream()) {
			// Use the 'map()' intermediate operation with a Lambda expression that returns a transformed string
			output = stream.map(s->s.toUpperCase()).toArray();
		}

		verifyMap(output);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testDistinct() {
		List<String> stringListWithDuplicates = new ArrayList<>();
		stringListWithDuplicates.addAll(Arrays.asList(A,B,A,B,C,D,C,D,E,F,E,F,G,G,G,G,G,G,G,G,G,G,G));
		
		// Use the 'distinct()' intermediate operation to remove duplicates from the stream
		final Object[] output;
		try (Stream<String> stream = stringListWithDuplicates.stream()) {
			// Use the 'distinct()' intermediate operation to remove duplicates from the stream.
			// No function required for this one.
			output = stream.distinct().toArray();
		}
		
		LOGGER.debug(Arrays.toString(output));
		assertEquals(7, output.length);
		assertTrue(Arrays.asList(output).containsAll(stringList));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testSorted() {
		List<String> unsortedStringList = new ArrayList<>();
		unsortedStringList.addAll(Arrays.asList(D,E,A,G,B,C,F));

		final Object[] output;
		try (Stream<String> stream = unsortedStringList.stream()) {
			// Use the 'sorted()' intermediate operation to sort the stream contents in natural order
			output = stream.sorted().toArray();
		}

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
		final int MAX_NUMBER_OF_STREAM_ELEMENTS_TO_KEEP = 3;
	
		final Object[] output;
		try (Stream<String> stream = stringList.stream()) {
			// The 'limit()' intermediate operation will discard any elements after element x
			output = stream
				.peek(s->LOGGER.debug("before: [{}]",s))
				.limit(MAX_NUMBER_OF_STREAM_ELEMENTS_TO_KEEP)
				.peek(s->LOGGER.debug("after : [{}]",s))
				.toArray();
		}

		// Notice in the 'peek()' output that the stream elements after the limit() amount
		// do not appear at all. This was counter-intuitive to me. I had expected to see
		// the entire list of values output from the peek() that comes before the limit(),
		// but it does not work that way.
		assertEquals(3, output.length);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testMultipleIntermediateOperations() {
		List<String> unsortedStringListWithDups = new ArrayList<>();
		unsortedStringListWithDups.addAll(Arrays.asList(G,E,A,B,C,C,D,E,G,F,A,C,D,B,D,G,F,A,C));

		final Object[] output;
		try (Stream<String> stream = stringList.stream()) {
			output = stream
				// remove duplicates
				.distinct()
				// get rid of the D's
				.filter(s -> !s.equals(D))
				// sort the stream
				.sorted()
				// convert all to uppercase
				.map(s -> s.toUpperCase())
				.toArray();
		}
		

		LOGGER.debug(Arrays.toString(output));
		assertEquals(6, output.length);
	}
	
}
