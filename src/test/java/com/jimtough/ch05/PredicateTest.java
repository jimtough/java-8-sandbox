package com.jimtough.ch05;

import static com.jimtough.ch05.Ch05Utils.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Play with the 'predicate' family of built-in functional interfaces in Java 8
 * 
 * @author JTOUGH
 */
public class PredicateTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PredicateTest.class);
    
	private List<String> stringList;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		stringList = newAlphabeticalStringList();
	}

	//--------------------------------------------------------------------
	
	private void verifyFilter(Object[] output) {
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(4, output.length);
		assertTrue(Arrays.asList(output).containsAll(Arrays.asList(B,E,F,G)));
	}
	
	@Test
	public void testPredicateExplicitWithoutLambda() {
		// Define a Predicate implementation the old pre-lambda way, as an anonymous class
		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean test(String s) {
				if (s.contains("o")) {
					return true;
				}
				return false;
			}
		};
		
		Object[] output = stringList.stream().filter(pred).toArray();

		verifyFilter(output);
	}

	@Test
	public void testPredicateExplicitUsingLambda() {
		// Define a Predicate implementation with a lambda expression, then pass the Predicate reference to filter()
		Predicate<String> pred = s -> s.contains("o");
		
		Object[] output = stringList.stream().filter(pred).toArray();
		
		verifyFilter(output);
	}

	@Test
	public void testPredicateInlineUsingLambda() {
		// Define a Predicate implementation inline as a lambda expression when calling filter()
		Object[] output = stringList.stream().filter(s -> s.contains("o")).toArray();
		
		verifyFilter(output);
	}

	//--------------------------------------------------------------------
	
	private void verifyIntFilter(int[] output) {
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}
	
	@Test
	public void testIntPredicateExplicitWithoutLambda() {
		// Define a IntPredicate implementation the old pre-lambda way, as an anonymous class
		IntPredicate pred = new IntPredicate() {
			@Override
			public boolean test(int i) {
				return i % 200000 == 0;
			}
		};
		
		int[] output = IntStream.rangeClosed(1, 1000000).filter(pred).toArray();

		verifyIntFilter(output);
	}
	
	@Test
	public void testIntPredicateExplicitUsingLambda() {
		// Define an IntPredicate implementation with a lambda expression, then pass the reference to filter()
		IntPredicate pred = i -> i % 200000 == 0;
		
		int[] output = IntStream.rangeClosed(1, 1000000).filter(pred).toArray();

		verifyIntFilter(output);
	}
	
	@Test
	public void testIntPredicateInlineUsingLambda() {
		// Define an IntPredicate implementation inline as a lambda expression when calling filter()
		int[] output = IntStream.rangeClosed(1, 1000000).filter(i -> i % 200000 == 0).toArray();

		verifyIntFilter(output);
	}
	
	// NOTE: This is considered the WRONG WAY to work with a stream of primitive int values.
	//       Integer objects and int primitives will be constantly unboxed/boxed in this method.
	//       The IntStream and IntPredicate classes were created to avoid this use case.
	//       Despite the extra processing overhead, you still get the same output.
	@Test
	public void testPredicateWithAutoBoxing() {
		// Define a Predicate implementation the old pre-lambda way, as an anonymous class
		Predicate<Integer> pred = new Predicate<Integer>() {
			@Override
			public boolean test(Integer i) {
				return i % 200000 == 0;
			}
		};
		AtomicInteger atomicInt = new AtomicInteger(0);
		
		Object[] output = Stream.generate(atomicInt::incrementAndGet).limit(1000000).filter(pred).toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testLongPredicateInlineUsingLambda() {
		// Define a LongPredicate implementation inline as a lambda expression when calling filter()
		long[] output = LongStream.iterate(0L, l -> l + 1L).limit(1000000).filter(l -> l % 200000 == 0).toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}
	
	//--------------------------------------------------------------------
	
	@Test
	public void testDoublePredicateInlineUsingLambda() {
		// Define a DoublePredicate implementation inline as a lambda expression when calling filter()
		double[] output = DoubleStream.iterate(0.0, d -> d + 1.0).limit(1000000).filter(d -> d % 200000 == 0).toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}
	
	//--------------------------------------------------------------------
	
	@Test
	public void testBiPredicateExplicitWithoutLambda() {
		// Define a BiPredicate implementation the old pre-lambda way, as an anonymous class
		BiPredicate<String,String> equalsIgnoreCaseWhenTrimmedPredicate = new BiPredicate<String,String>() {
			@Override
			public boolean test(String t, String u) {
				return t.trim().equalsIgnoreCase(u.trim());
			}
		};

		assertTrue(equalsIgnoreCaseWhenTrimmedPredicate.test("  \t", "\t \n\n"));
		assertTrue(equalsIgnoreCaseWhenTrimmedPredicate.test("foo", " Foo "));
		assertFalse(equalsIgnoreCaseWhenTrimmedPredicate.test("foo", "bar"));
	}
	
}
