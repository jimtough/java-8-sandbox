package com.jimtough.ch05;

import static com.jimtough.ch05.Ch05Utils.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.LongFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Play with the 'function' family of built-in functional interfaces in Java 8
 * 
 * @author JTOUGH
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionTest.class);
    
	private List<String> stringList;
	private List<String> integerStringList;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		stringList = newAlphabeticalStringList();
		integerStringList = newIntegerStringsList();
	}

	//--------------------------------------------------------------------
	
	private void verifyFunction(Object[] output) {
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
	public void testFunctionExplicitWithoutLambda() {
		// Define a Function implementation the old pre-lambda way, as an anonymous class
		Function<String,String> function = new Function<String,String>() {
			@Override
			public String apply(String s) {
				return s.toUpperCase();
			}
		};
		
		Object[] output = stringList.stream().map(function).toArray();
		
		verifyFunction(output);
	}

	@Test
	public void testFunctionExplicitUsingLambda() {
		// Define a Function implementation with a lambda expression, then pass the reference to map()
		Function<String,String> function = s -> s.toUpperCase();
		
		Object[] output = stringList.stream().map(function).toArray();
		
		verifyFunction(output);
	}

	@Test
	public void testFunctionExplicitUsingMethodReference() {
		// Define a Function implementation with a lambda expression, then pass the reference to map()
		Function<String,String> function = String::toUpperCase;
		
		Object[] output = stringList.stream().map(function).toArray();
		
		verifyFunction(output);
	}

	@Test
	public void testFunctionInlineUsingLambda() {
		Object[] output = stringList.stream().map(s -> s.toUpperCase()).toArray();
		
		verifyFunction(output);
	}

	@Test
	public void testFunctionInlineUsingMethodReference() {
		Object[] output = stringList.stream().map(String::toUpperCase).toArray();
		
		verifyFunction(output);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCombiningFunctionsUsingAndThen() {
		Function<String,String> functionToUpperCase = new Function<String,String>() {
			@Override
			public String apply(String s) {
				return s.toUpperCase();
			}
		};
		Function<String,String> functionAddSExclamation = new Function<String,String>() {
			@Override
			public String apply(String s) {
				return s + "s!";
			}
		};
		// 'andThen()' will make passed in function execute AFTER
		Function<String,String> functionToUpperCaseAndAddExclamation = 
				functionToUpperCase.andThen(functionAddSExclamation);
		
		Object[] output = stringList.stream().map(functionToUpperCaseAndAddExclamation).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(stringList.size(), output.length);
	}
	
	@Test
	public void testCombiningFunctionsUsingCompose() {
		Function<String,String> functionToUpperCase = new Function<String,String>() {
			@Override
			public String apply(String s) {
				return s.toUpperCase();
			}
		};
		Function<String,String> functionAddSExclamation = new Function<String,String>() {
			@Override
			public String apply(String s) {
				return s + "s!";
			}
		};
		// 'compose()' will make passed in function execute BEFORE
		Function<String,String> functionAddExclamationThenToUpperCase = 
				functionToUpperCase.compose(functionAddSExclamation);
		
		Object[] output = stringList.stream().map(functionAddExclamationThenToUpperCase).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(stringList.size(), output.length);
	}

	//--------------------------------------------------------------------

	@Test
	public void testIdentityFunction() {
		// The built-in 'identity()' Function just returns the original value. Useful as a test stub?
		Object[] output = stringList.stream().map(Function.identity()).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(stringList.size(), output.length);
	}

	
	
	//--------------------------------------------------------------------
	// FUNCTION INTERFACES FOR OBJECT to/from PRIMITIVES
	//--------------------------------------------------------------------

	@Test
	public void testIntFunctionExplicitWithoutLambda() {
		IntFunction<String> function = new IntFunction<String>() {
			@Override
			public String apply(int i) {
				return Integer.toString(i);
			}
		};
		
		Object[] output = IntStream.rangeClosed(1, 10).mapToObj(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}

	@Test
	public void testIntFunctionExplicitUsingLambda() {
		IntFunction<String> function = i -> Integer.toString(i);
		
		Object[] output = IntStream.rangeClosed(1, 10).mapToObj(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}

	@Test
	public void testIntFunctionInlineUsingLambda() {
		Object[] output = IntStream.rangeClosed(1, 10).mapToObj(i -> Integer.toString(i)).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}

	@Test
	public void testIntFunctionInlineUsingMethodReference() {
		Object[] output = IntStream.rangeClosed(1, 10).mapToObj(Integer::toString).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}
	
	//--------------------------------------------------------------------

	@Test
	public void testLongFunctionExplicitUsingLambda() {
		LongFunction<String> function = l -> Long.toString(l);
		
		Object[] output = LongStream.rangeClosed(1L, 10L).mapToObj(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}
	
	//--------------------------------------------------------------------

	@Test
	public void testDoubleFunctionExplicitUsingLambda() {
		DoubleFunction<String> function = d -> Double.toString(d);
		
		Object[] output = DoubleStream.iterate(1.0D, d -> d + 1.0D).limit(10).mapToObj(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(10, output.length);
	}
	
	//--------------------------------------------------------------------

	@Test
	public void testToIntFunctionExplicitWithoutLambda() {
		ToIntFunction<String> function = new ToIntFunction<String>() {
			@Override
			public int applyAsInt(String s) {
				return Integer.parseInt(s);
			}
		};
		
		// NOTES
		// Object stream uses 'mapToInt()' intermediate operation, creating an IntStream from the Stream.
		// The resulting IntStream returns 'int[]' from its 'toArray()' method.
		int[] output = integerStringList.stream().mapToInt(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}

	@Test
	public void testToIntFunctionExplicitUsingLambda() {
		ToIntFunction<String> function = s -> Integer.parseInt(s);

		int[] output = integerStringList.stream().mapToInt(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}

	@Test
	public void testToIntFunctionInlineUsingLambda() {
		int[] output = integerStringList.stream().mapToInt(s -> Integer.parseInt(s)).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}

	@Test
	public void testToIntFunctionInlineUsingMethodReference() {
		int[] output = integerStringList.stream().mapToInt(Integer::parseInt).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}
	
	//--------------------------------------------------------------------

	@Test
	public void testToLongFunctionExplicitUsingLambda() {
		ToLongFunction<String> function = s -> Long.parseLong(s);

		long[] output = integerStringList.stream().mapToLong(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}
	
	//--------------------------------------------------------------------

	@Test
	public void testToDoubleFunctionExplicitUsingLambda() {
		ToDoubleFunction<String> function = s -> Double.parseDouble(s);

		double[] output = integerStringList.stream().mapToDouble(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}

	
	
	//--------------------------------------------------------------------
	// FUNCTION INTERFACES FOR PRIMITIVE to PRIMITIVE
	//--------------------------------------------------------------------

	@Test
	public void testIntToLongFunctionExplicitWithoutLambda() {
		IntToLongFunction function = new IntToLongFunction() {
			@Override
			public long applyAsLong(int i) {
				long l = (long)i;
				return l * 2;
			}
		};
		
		// NOTES
		// IntStream uses 'mapToLong()' intermediate operation, creating a LongStream from the IntStream.
		// The resulting LongStream returns 'long[]' from its 'toArray()' method.
		long[] output = IntStream.rangeClosed(Integer.MAX_VALUE - 4, Integer.MAX_VALUE).mapToLong(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}
	
	//--------------------------------------------------------------------

	@Test
	public void testIntToDoubleFunctionExplicitWithoutLambda() {
		IntToDoubleFunction function = new IntToDoubleFunction() {
			@Override
			public double applyAsDouble(int i) {
				double d = (double)i;
				return Math.sqrt(d);
			}
		};
		
		// NOTES
		// IntStream uses 'mapToDouble()' intermediate operation, creating a DoubleStream from the IntStream.
		// The resulting DoubleStream returns 'double[]' from its 'toArray()' method.
		double[] output = IntStream.rangeClosed(1,5).mapToDouble(function).toArray();
		
		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(5, output.length);
	}
	
	//--------------------------------------------------------------------

	// Other flavours exist that are similar to those above. I'm not creating example code for these:
	// - LongToIntFunction
	// - LongToDoubleFunction
	// - DoubleToIntFunction
	// - DoubleToLongFunction

	
	
	//--------------------------------------------------------------------
	// BINARY FUNCTION INTERFACE
	//--------------------------------------------------------------------
	
	@Test
	public void testBiFunctionExplicitWithoutLambda() {
		BiFunction<String,String,Integer> biFunction = new BiFunction<String,String,Integer>() {
			@Override
			public Integer apply(String s1, String s2) {
				int int1 = Integer.parseInt(s1);
				int int2 = Integer.parseInt(s2);
				return int1 + int2;
			}
		};

		assertEquals(2, biFunction.apply("1", "1").intValue());
		assertEquals(9, biFunction.apply("2", "7").intValue());
		assertEquals(-33, biFunction.apply("-99", "66").intValue());
	}
	
}
