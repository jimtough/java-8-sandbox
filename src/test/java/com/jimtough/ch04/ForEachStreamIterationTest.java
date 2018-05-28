package com.jimtough.ch04;

import static com.jimtough.ch04.Ch04Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class ForEachStreamIterationTest {
	
	private List<String> stringList;
	
	@Before
	public void setUp() {
		List<String> stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
		this.stringList = Collections.unmodifiableList(stringList);
	}

	private void validateNewList(List<String> newList) {
		assertEquals(7, newList.size());
		assertEquals(A, newList.get(0));
		assertEquals(G, newList.get(newList.size()-1));
		assertTrue(newList.containsAll(Arrays.asList(A,B,C,D,E,F,G)));
	}
	
	// This test method does NOT use the new Streams API
	@Test
	public void testJava5ForLoop() {
		List<String> newList = new ArrayList<>();
		for (String s : stringList) {
			newList.add(s);
		}
		validateNewList(newList);
	}

	// DO NOT USE CODE LIKE THIS!
	@Test
	public void testJava8ForEachUsingExplicitConsumerInstance_WRONG() {
		final List<String> newList = new ArrayList<>();
		// This is the wrong way to use a stream!
		// Read Item 46 in Effective Java 3rd Edition for a detailed
		// explanation of why this is an incorrect of streams.
		Consumer<String> myConsumer = new Consumer<String>() {
			@Override 
			public void accept(String s) {
				newList.add(s);
			}
		};
		stringList.forEach(myConsumer);
		validateNewList(newList);
	}

	// DO NOT USE CODE LIKE THIS!
	@Test
	public void testJava8ForEachUsingLambdaConsumer_WRONG() {
		final List<String> newList = new ArrayList<>();
		// This is the wrong way to use a stream!
		// Read Item 46 in Effective Java 3rd Edition for a detailed
		// explanation of why this is an incorrect of streams.
		stringList.forEach(s -> newList.add(s));
		validateNewList(newList);
	}

	@Test
	public void testJava8ForEachUsingLambdaConsumer_RIGHT_verbose() {
		List<String> newList;
		// Added this line to illustrate what this built-in collector signature looks like
		Collector<? super String, ?, List<String>> builtInCollector = Collectors.toList();
		try (Stream<String> myStream = stringList.stream()) {
			newList = myStream.collect(builtInCollector);
		}
		assertFalse(newList instanceof LinkedList);
		validateNewList(newList);
	}

	@Test
	public void testJava8ForEachUsingLambdaConsumer_RIGHT_concise() {
		List<String> newList;
		try (Stream<String> myStream = stringList.stream()) {
			newList = myStream.collect(Collectors.toList());
		}
		validateNewList(newList);
	}

	@Test
	public void testJava8ForEachUsingLambdaConsumer_RIGHT_WithExplicitListImplType() {
		List<String> newList;
		// Added this line to illustrate what this built-in collector signature looks like
		Collector<? super String, ?, List<String>> builtInCollector = Collectors.toCollection(LinkedList::new);
		try (Stream<String> myStream = stringList.stream()) {
			newList = myStream.collect(builtInCollector);
		}
		assertTrue(newList instanceof LinkedList);
		validateNewList(newList);
	}
	
}
