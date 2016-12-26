package com.jimtough.ch04;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodReferencesWithStreamsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodReferencesWithStreamsTest.class);
	
	private static final String A = "alpha";
	private static final String B = "bravo";
	private static final String C = "charlie";
	private static final String D = "delta";
	private static final String E = "echo";
	private static final String F = "foxtrot";
	private static final String G = "golf";

	private static final void writeToLog(String s) {
		LOGGER.debug("writeToLog() | s: [{}]", s);
	}

	private List<Double> findSquareRoot(List<Integer> list, Function<Integer,Integer> f) {
		// This example was adapted from here:
		//   https://blog.idrsolutions.com/2015/02/java-8-method-references-explained-5-minutes/
		List<Double> result = new ArrayList<>();
		// The 'f' parameter is a reference to the constructor for the Integer class.
		// The Lambda function is creating a new Integer object via the constructor
		// each time the internal iteration is done on the list Stream.
		// This is a silly example, but it does illustrate how to pass a reference
		// to a constructor if I ever need to do so.
		list.forEach(x -> result.add(Math.sqrt(f.apply((Integer) x))));
		return result;
	}	
	
	private List<Integer> getStringLengths(List<String> stringList, Function<String,Integer> f) {
		// This example was adapted from here:
		//   https://blog.idrsolutions.com/2015/02/java-8-method-references-explained-5-minutes/
		List<Integer> result = new ArrayList<>();
		// The 'f' parameter is a reference to the 'length()' method of the String class.
		// The Lambda function is getting the length of each string via the Function
		// each time the internal iteration is done on the list Stream.
		stringList.forEach(s -> result.add(f.apply(s)));
		return result;
	}
	
	private void consumeStringList(List<String> list, Consumer<String> c) {
		list.forEach(s -> c.accept(s));
	}
    
	private List<String> stringList;
	
	@Before
	public void setUp() {
		stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
	}

	@Test
	public void testStaticMethodReference() {
		stringList.forEach(MethodReferencesWithStreamsTest::writeToLog);
	}

	@Test
	public void testConstructorReference() {
		// This example was adapted from here:
		//   https://blog.idrsolutions.com/2015/02/java-8-method-references-explained-5-minutes/
		List<Integer> numbers = Arrays.asList(4,9,16,25,36);
		
 		stringList.forEach(MethodReferencesWithStreamsTest::writeToLog);
 		
 		// We pass the list of values and a Function reference to the constructor for Integer class.
 		List<Double> squaredRootedNumbers = findSquareRoot(numbers,Integer::new);
 		assertEquals(2.0D, squaredRootedNumbers.get(0), 0.0D);
 		assertEquals(3.0D, squaredRootedNumbers.get(1), 0.0D);
 		assertEquals(4.0D, squaredRootedNumbers.get(2), 0.0D);
 		assertEquals(5.0D, squaredRootedNumbers.get(3), 0.0D);
 		assertEquals(6.0D, squaredRootedNumbers.get(4), 0.0D);
 		LOGGER.debug(squaredRootedNumbers.toString());
 	}

	@Test
	public void testClassMethodReference() {
 		// We pass the list of values and a Function reference to the 'length()' method of the String class.
		List<Integer> strLenList = getStringLengths(stringList, String::length);
		
 		assertEquals(5, strLenList.get(0).intValue());
 		assertEquals(5, strLenList.get(1).intValue());
 		assertEquals(7, strLenList.get(2).intValue());
 		assertEquals(5, strLenList.get(3).intValue());
 		assertEquals(4, strLenList.get(4).intValue());
 		assertEquals(7, strLenList.get(5).intValue());
 		assertEquals(4, strLenList.get(6).intValue());
 		LOGGER.debug("String lengths");
 		LOGGER.debug("--------------");
 		for (int i=0; i<stringList.size(); i++) {
 			LOGGER.debug("s: {} | length: {}", stringList.get(i), strLenList.get(i));
 		}
	}

	// This class is only used in the following test method
	private static class FirstCharHolder {
		private final List<Character> firstCharList = new ArrayList<>();
		void storeFirstCharOfString(String s) {
			char c = s.charAt(0);
			firstCharList.add(Character.valueOf(c));
		}
	}
	
	@Test
	public void testObjectMethodReference() {
		final FirstCharHolder holder = new FirstCharHolder();
		
		consumeStringList(stringList, holder::storeFirstCharOfString);
		
 		LOGGER.debug("String first chars");
 		LOGGER.debug("------------------");
 		for (int i=0; i<stringList.size(); i++) {
 			LOGGER.debug("{} | {}", holder.firstCharList.get(i), stringList.get(i));
 		}
	}
	
}
