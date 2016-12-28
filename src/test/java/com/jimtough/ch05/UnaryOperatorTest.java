package com.jimtough.ch05;

import static com.jimtough.ch05.Ch05Utils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnaryOperatorTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTest.class);
    
	private List<String> stringList;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		stringList = newAlphabeticalStringList();
	}

	//--------------------------------------------------------------------
	
	private void verifyUnaryOperator(Object[] output) {
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(stringList.size(), output.length);
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
	public void testUnaryOperatorExplicitWithoutLambda() {
		// Define a UnaryOperator implementation the old pre-lambda way, as an anonymous class
		UnaryOperator<String> uOp = new UnaryOperator<String>() {
			@Override
			public String apply(String s) {
				return s.toUpperCase();
			}
		};
		
		stringList.replaceAll(uOp);
		
		Object[] output = stringList.toArray();
		verifyUnaryOperator(output);
	}
	
	@Test
	public void testUnaryOperatorExplicitUsingLambda() {
		// Define a UnaryOperator implementation using a lambda expression
		UnaryOperator<String> uOp = s -> s.toUpperCase();
		
		stringList.replaceAll(uOp);
		
		Object[] output = stringList.toArray();
		verifyUnaryOperator(output);
	}
	
	@Test
	public void testUnaryOperatorInlineUsingLambda() {
		// Define a UnaryOperator implementation using an inline lambda expression
		stringList.replaceAll(s -> s.toUpperCase());
		
		Object[] output = stringList.toArray();
		verifyUnaryOperator(output);
	}

	//--------------------------------------------------------------------

	// Primitive flavours also exist...
	//   IntUnaryOperator
	//   LongUnaryOperator
	//   DoubleUnaryOperator
	//
	//
	// A binary version also exists
	//   BinaryOperator

	// I am not creating example code for any of these.
	
	
}
