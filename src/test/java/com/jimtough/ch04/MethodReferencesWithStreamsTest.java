package com.jimtough.ch04;

import static com.jimtough.ch04.Ch04Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodReferencesWithStreamsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodReferencesWithStreamsTest.class);

	private static final void writeToLog(String s) {
		LOGGER.debug("writeToLog() | s: [{}]", s);
	}
    
	private List<String> stringList;
	
	@Before
	public void setUp() {
		stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
	}

	@Test
	public void testPassingStaticMethodAsFunctionReference() {
		stringList.forEach(MethodReferencesWithStreamsTest::writeToLog);
	}

	//-------------------------------------------------------------------------
	
	private List<Double> findSquareRoots_BAD_Autoboxing(List<String> listOfIntegerStrings, Function<String,Integer> f) {
		//--------------------------------------------------------------------------
		// The 'f' parameter is a reference to the Integer.valueOf() method.
		// The Lambda function is parsing a string to create an integer
		// each time the internal iteration is done on the list (String) Stream.
		//
		// This is a silly example, but it does illustrate how to pass a reference
		// to a function as a method parameter.
		//
		// NOTE: This approach is not ideal because each intermediate stream
		//       operation is using autoboxing to convert from Integer to int,
		//       or Double to double. See better version below!
		//
		//--------------------------------------------------------------------------
		//
		// First, create a Stream<String> from the input parameter 'listOfIntegerStrings'
		try (Stream<String> stream = listOfIntegerStrings.stream()) {
			List<Double> result = stream
				// Next, use the 'f' parameter as a mapping function to convert each
				// string-encoded integer to an Integer object
				.map(f)
				// Next use a lambda function to convert each Integer to a Double,
				// resulting in a new stream of type Stream<Double>
				.map(i -> Double.valueOf(i.doubleValue()))
				// Next use the static method Math.sqrt() to transform each value in
				// the stream (a Double) to its square root
				.map(Math::sqrt)
				// Now use the (terminal) collect() operation to put each stream element in a list
				.collect(Collectors.toList());
			return result;
		}
	}	

	@Test
	public void testPassingFunctionReferences_BAD_Autoboxing() {
		List<String> numbers = Arrays.asList("4","9","16","25","36");
 		
 		// We pass the list of values and a Function reference to the static Integer.valueOf() method
 		List<Double> squaredRootedNumbers = findSquareRoots_BAD_Autoboxing(numbers, Integer::valueOf);
 		assertEquals(2.0D, squaredRootedNumbers.get(0), 0.0D);
 		assertEquals(3.0D, squaredRootedNumbers.get(1), 0.0D);
 		assertEquals(4.0D, squaredRootedNumbers.get(2), 0.0D);
 		assertEquals(5.0D, squaredRootedNumbers.get(3), 0.0D);
 		assertEquals(6.0D, squaredRootedNumbers.get(4), 0.0D);
 		LOGGER.debug(squaredRootedNumbers.toString());
 	}
	
	private double[] findSquareRoots(List<String> listOfIntegerStrings, ToIntFunction<String> f) {
		//--------------------------------------------------------------------------
		// The 'f' parameter is a reference to the Integer.valueOf() method.
		// The Lambda function is parsing a string to create an int each time
		// the internal iteration is done on the list (String) Stream.
		//
		// This is a silly example, but it does illustrate how to pass a reference
		// to a function as a method parameter.
		//--------------------------------------------------------------------------
		//
		// First, create a Stream<String> from the input parameter 'listOfIntegerStrings'
		try (Stream<String> stream = listOfIntegerStrings.stream()) {
			double[] result = stream
				// Next, use the 'f' parameter as a mapping function to convert each
				// string-encoded integer to an int primitive, resulting in an
				// intermediate IntStream
				.mapToInt(f)
				// Next use the static method Math.sqrt() to transform each value in
				// the stream (an int) to its square root, resulting in an
				// intermediate DoubleStream
				.mapToDouble(Math::sqrt)
				// Now use the (terminal) toArray() operation to put each 
				// DoubleStream element into an array
				.toArray();
			return result;
		}
	}	

	@Test
	public void testPassingFunctionReferences() {
		List<String> numbers = Arrays.asList("4","9","16","25","36");
 		
 		// We pass the list of values and a Function reference to the static Integer.parseInt() method
		double[] squaredRootedNumbers = findSquareRoots(numbers,Integer::parseInt);
		assertEquals(5, squaredRootedNumbers.length);
 		assertEquals(2.0D, squaredRootedNumbers[0], 0.0D);
 		assertEquals(3.0D, squaredRootedNumbers[1], 0.0D);
 		assertEquals(4.0D, squaredRootedNumbers[2], 0.0D);
 		assertEquals(5.0D, squaredRootedNumbers[3], 0.0D);
 		assertEquals(6.0D, squaredRootedNumbers[4], 0.0D);
 		LOGGER.debug(Arrays.toString(squaredRootedNumbers));
 	}
	
	//-------------------------------------------------------------------------
	
	private Map<String,Integer> getStringLengths(List<String> stringList, Function<String,Integer> f) {
		try (Stream<String> stream = stringList.stream()) {
			// The 'f' parameter is a reference to the 'length()' method of the String class.
			// The Lambda function is getting the length of each string via the Function
			// each time the internal iteration is done on the list Stream.
			return stream.collect(Collectors.toMap(Function.identity(), f));
		}
	}

	@Test
	public void testClassMethodReference() {
 		// We pass the list of values and a Function reference to the 'length()' method of the String class.
		Map<String,Integer> map = getStringLengths(stringList, String::length);

		assertEquals(stringList.size(), map.size());
		for (String s : stringList) {
	 		assertEquals(s.length(), map.get(s).intValue());
		}
		// The order of iteration likely won't be the same as the order of the
		// strings in the original list. This could be fixed, but it would make
		// the example more complicated.
 		LOGGER.debug("String lengths");
 		LOGGER.debug("--------------");
		for (Entry<String,Integer> entry : map.entrySet()) {
 			LOGGER.debug("s: {} | length: {}", entry.getKey(), entry.getValue());
		}
	}
	
	//-------------------------------------------------------------------------

	// This class is only used in the following test method
	private static class FirstCharHolder {
		private final List<Character> firstCharList = new ArrayList<>();
		void storeFirstCharOfString(String s) {
			char c = s.charAt(0);
			firstCharList.add(Character.valueOf(c));
		}
	}
	
	private void consumeStringList(List<String> list, Consumer<String> c) {
		// The Consumer reference passed to this method should be associated
		// with a FirstCharHolder object. This method doesn't need to know
		// about that. We just feed the Consumer reference.
		try (Stream<String> stream = list.stream()) {
			stream.forEachOrdered(s -> c.accept(s));
		}
	}
	
	@Test
	public void testObjectMethodReference() {
		final FirstCharHolder holder = new FirstCharHolder();
		
		consumeStringList(stringList, holder::storeFirstCharOfString);
		
		// Demonstrate that the Consumer reference passed to the method
		// sent the values to my instance of FirstCharHolder
 		LOGGER.debug("String first chars");
 		LOGGER.debug("------------------");
 		for (int i=0; i<stringList.size(); i++) {
 			LOGGER.debug("{} | {}", holder.firstCharList.get(i), stringList.get(i));
 		}
	}
	
}
