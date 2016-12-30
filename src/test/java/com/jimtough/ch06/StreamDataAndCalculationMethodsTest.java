package com.jimtough.ch06;

import static com.jimtough.ch06.Ch06Utils.*;
import static org.junit.Assert.*;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamDataAndCalculationMethodsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamDataAndCalculationMethodsTest.class);

	private List<String> unsortedStringList;
	private int[] unsortedIntArray;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		unsortedStringList = newDistinctButUnsortedStringList();
		unsortedIntArray = newUnsortedIntArray();
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testStreamCount() {
		long count = unsortedStringList.stream().count();
		
		LOGGER.debug("count: {} | {}", count, testName.getMethodName());
		assertEquals(7, count);
	}
	
	@Test
	public void testStreamCountOnEmptyStream() {
		long count = Stream.of(new String[]{}).count();
		
		LOGGER.debug("count: {} | {}", count, testName.getMethodName());
		assertEquals(0, count);
	}
	
	@Test
	public void testStreamMin() {
		Optional<String> min = unsortedStringList.stream().min(String.CASE_INSENSITIVE_ORDER);

		LOGGER.debug("min: {} | {}", min, testName.getMethodName());
		assertTrue(min.isPresent());
		assertEquals(A, min.get());
	}
	
	@Test
	public void testStreamMinOnEmptyStream() {
		Optional<String> min = Stream.of(new String[]{}).min(String.CASE_INSENSITIVE_ORDER);

		LOGGER.debug("min: {} | {}", min, testName.getMethodName());
		assertFalse(min.isPresent());
	}
	
	@Test
	public void testStreamMax() {
		Optional<String> max = unsortedStringList.stream().max(String.CASE_INSENSITIVE_ORDER);

		LOGGER.debug("max: {} | {}", max, testName.getMethodName());
		assertTrue(max.isPresent());
		assertEquals(G, max.get());
	}
	
	@Test
	public void testStreamMaxOnEmptyStream() {
		Optional<String> max = Stream.of(new String[]{}).max(String.CASE_INSENSITIVE_ORDER);

		LOGGER.debug("max: {} | {}", max, testName.getMethodName());
		assertFalse(max.isPresent());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testIntStreamCount() {
		long count = IntStream.of(unsortedIntArray).count();
		
		LOGGER.debug("count: {} | {}", count, testName.getMethodName());
		assertEquals(10, count);
	}
	
	@Test
	public void testIntStreamMin() {
		OptionalInt min = IntStream.of(unsortedIntArray).min();
		
		LOGGER.debug("min: {} | {}", min, testName.getMethodName());
		assertTrue(min.isPresent());
		assertEquals(1, min.getAsInt());
	}
	
	@Test
	public void testIntStreamMax() {
		OptionalInt max = IntStream.of(unsortedIntArray).max();
		
		LOGGER.debug("max: {} | {}", max, testName.getMethodName());
		assertTrue(max.isPresent());
		assertEquals(10, max.getAsInt());
	}
	
	@Test
	public void testIntStreamSum() {
		// NOTE: 'sum()' returns int and not OptionalInt
		int sum = IntStream.of(unsortedIntArray).sum();
		
		LOGGER.debug("sum: {} | {}", sum, testName.getMethodName());
		assertEquals(55, sum);
	}
	
	@Test
	public void testIntStreamAverage() {
		OptionalDouble avg = IntStream.of(unsortedIntArray).average();
		
		LOGGER.debug("avg: {} | {}", avg, testName.getMethodName());
		assertTrue(avg.isPresent());
		assertEquals(5.5D, avg.getAsDouble(), 0.0D);
	}
	
	@Test
	public void testIntStreamSummaryStatistics() {
		IntSummaryStatistics stats = IntStream.of(unsortedIntArray).summaryStatistics();
		
		LOGGER.debug("count: {} | sum: {} | avg: {} | min: {} | max: {} | {}",
				stats.getCount(),
				stats.getSum(), 
				stats.getAverage(),
				stats.getMin(),
				stats.getMax(),
				testName.getMethodName());
	}

	// NOTE: DoubleStream and LongStream have similar operations to IntStream.
	//       I am not creating example code, as it would be nearly the same as above.

	
	//--------------------------------------------------------------------
	// reduce() TESTS
	//--------------------------------------------------------------------

	@Test
	public void testStreamReducer_ExplicitWithoutLambda_concatenate() {
		BinaryOperator<String> biOperatorForConcat = new BinaryOperator<String>() {
			@Override
			public String apply(String concatString, String s) {
				return concatString == null ? s : concatString + "," + s;
			}
		};
		
		Optional<String> concatenatedStreamValues = 
				unsortedStringList.stream().sorted().reduce(biOperatorForConcat);
		
		LOGGER.debug("concatenatedStreamValues: {} | {}", concatenatedStreamValues, testName.getMethodName());
		assertTrue(concatenatedStreamValues.isPresent());
	}

	@Test
	public void testStreamReducer_ExplicitUsingLambda_concatenate() {
		BinaryOperator<String> biOperatorForConcat = 
			(concatString, s) -> concatString == null ? s : concatString + "," + s;
		
		Optional<String> concatenatedStreamValues = 
				unsortedStringList.stream().sorted().reduce(biOperatorForConcat);
		
		LOGGER.debug("concatenatedStreamValues: {} | {}", concatenatedStreamValues, testName.getMethodName());
		assertTrue(concatenatedStreamValues.isPresent());
	}
	
	//--------------------------------------------------------------------
	
	@Test
	public void testIntStreamReducer_ExplicitWithoutLambda_sum() {
		IntBinaryOperator intBinaryOperatorForSum = new IntBinaryOperator() {
			@Override
			public int applyAsInt(int left, int right) {
				return left + right;
			}
		};
		
		OptionalInt sum = IntStream.of(unsortedIntArray).reduce(intBinaryOperatorForSum);
		
		LOGGER.debug("sum: {} | {}", sum, testName.getMethodName());
		assertTrue(sum.isPresent());
		assertEquals(55, sum.getAsInt());
	}
	
	@Test
	public void testIntStreamReducer_ExplicitUsingLambda_sum() {
		IntBinaryOperator intBinaryOperatorForSum = (currentSum, i) -> currentSum + i;
		
		OptionalInt sum = IntStream.of(unsortedIntArray).reduce(intBinaryOperatorForSum);
		
		LOGGER.debug("sum: {} | {}", sum, testName.getMethodName());
		assertTrue(sum.isPresent());
		assertEquals(55, sum.getAsInt());
	}
	
}


