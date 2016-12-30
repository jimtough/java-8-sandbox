package com.jimtough.ch06;

import static com.jimtough.ch06.Ch06Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamCollectorsTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StreamCollectorsTest.class);
    
	private List<String> stringList;
	private List<String> stringListWithDuplicates;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		stringList = newAlphabeticalStringList();
		stringListWithDuplicates = new ArrayList<>();
		stringListWithDuplicates.addAll(Arrays.asList(F,C,E,D,A,F,G,C,B,D,G,B,C,E));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCollectorsToList() {
		List<String> output = stringList.stream().collect(Collectors.toList());
		
		LOGGER.debug("{} | {}", output, testName.getMethodName());
		assertEquals(A, output.get(0));
		assertEquals(G, output.get(6));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCollectorsToSet() {
		Set<String> output = stringListWithDuplicates.stream().collect(Collectors.toSet());
		
		LOGGER.debug("{} | {}", output, testName.getMethodName());
		assertEquals(7, output.size());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCollectorsToCollection() {
		// Supplier must supply a type that implements Collection
		Supplier<TreeSet<String>> collectionFactory = TreeSet::new;
		
		TreeSet<String> output = stringListWithDuplicates.stream()
				// The 'collect()' operation will now get the Collection object from your Supplier and populate it
				.collect(Collectors.toCollection(collectionFactory));
		
		LOGGER.debug("{} | {}", output, testName.getMethodName());
		assertEquals(7, output.size());
		assertEquals(A, output.iterator().next());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCollectorsToMap() {
		// keyMapper is a simple identity function (key IS the input string)
		Function<String,String> keyMapper = Function.identity();
		// valueMapper function maps the length of the input string to an integer value
		Function<String,Integer> valueMapper = s -> s.length();
		
		Map<String,Integer> output = stringList.stream()
				// The 'toMap()' collector needs a keyMapper function and a valueMapper function
				.collect(Collectors.toMap(keyMapper, valueMapper));
		
		LOGGER.debug("{} | {}", output, testName.getMethodName());
		assertEquals(7, output.size());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCollectorsGroupingBy() {
		// Classify objects of type String into a group that is identified by an object of type Integer
		Function<String,Integer> groupClassifier = s -> s.length();
		
		Map<Integer,List<String>> output = stringList.stream()
				// Group a stream of String objects by string length
				.collect(Collectors.groupingBy(groupClassifier));
		
		LOGGER.debug("{} | {}", output, testName.getMethodName());
		assertEquals(3, output.size());
		assertEquals(2, output.get(4).size()); // echo, golf
		assertEquals(3, output.get(5).size()); // alpha, bravo, delta
		assertEquals(2, output.get(7).size()); // charlie, foxtrot
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCollectorsPartitioningBy() {
		Predicate<Integer> isEvenPredicate = i -> i % 2 == 0;

		Map<Boolean,List<Integer>> output = IntStream.rangeClosed(1, 20)
				// Change IntStream to Stream<Integer>
				.boxed()
				// Creates a Map with a TRUE and FALSE key, each with a List<Integer> as the value
				.collect(Collectors.partitioningBy(isEvenPredicate));
		
		LOGGER.debug("{} | {}", output, testName.getMethodName());
		assertEquals(2, output.size());
		assertEquals(10, output.get(Boolean.TRUE).size());
		assertEquals(10, output.get(Boolean.FALSE).size());
	}
	
}

