package com.jimtough.ch05;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Play with the 'supplier' family of built-in functional interfaces in Java 8
 * 
 * @author JTOUGH
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplierTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierTest.class);

	@Rule
	public TestName testName = new TestName();

	//--------------------------------------------------------------------
	
	@Test
	public void testSupplierExplicitWithoutLambda() {
		final AtomicInteger ai = new AtomicInteger(0);
		// Define a Supplier implementation the old pre-lambda way, as an anonymous class
		Supplier<String> supplier = new Supplier<String>() {
			@Override
			public String get() {
				return Integer.toString(ai.incrementAndGet());
			}
		};

		// NOTE
		// This Supplier will create an infinite stream.
		// The 'limit()' intermediate operation makes it stop getting values from the Supplier after 10 values.
		Object[] output = Stream.generate(supplier).limit(10).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}
	
	@Test
	public void testSupplierExplicitUsingLambda() {
		final AtomicInteger ai = new AtomicInteger(0);
		// Define a Supplier implementation using a Lambda expression
		Supplier<String> supplier = () -> Integer.toString(ai.incrementAndGet());

		Object[] output = Stream.generate(supplier).limit(10).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}
	
	@Test
	public void testSupplierInlineUsingLambda() {
		final AtomicInteger ai = new AtomicInteger(0);

		Object[] output = Stream.generate(() -> Integer.toString(ai.incrementAndGet())).limit(10).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testIntSupplierExplicitUsingLambda() {
		final AtomicInteger ai = new AtomicInteger(0);
		// Define an IntSupplier implementation using a Lambda expression
		IntSupplier supplier = () -> ai.incrementAndGet();
		
		int[] output = IntStream.generate(supplier).limit(10).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testLongSupplierExplicitUsingLambda() {
		final AtomicLong al = new AtomicLong(0L);
		// Define a LongSupplier implementation using a Lambda expression
		LongSupplier supplier = () -> al.incrementAndGet();
		
		long[] output = LongStream.generate(supplier).limit(10).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testDoubleSupplierExplicitUsingLambda() {
		final Random rando = new Random();
		// Define a DoubleSupplier implementation using a Lambda expression
		DoubleSupplier supplier = () -> rando.nextDouble();
		
		double[] output = DoubleStream.generate(supplier).limit(3).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(3, output.length);
	}

	//--------------------------------------------------------------------

	// NOTE: There is also a BooleanSupplier class, but I don't know where one would use it
	
}
