package com.jimtough.ch04;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class ForEachStreamIterationTest {

	private static final String A = "alpha";
	private static final String B = "bravo";
	private static final String C = "charlie";
	private static final String D = "delta";
	private static final String E = "echo";
	private static final String F = "foxtrot";
	private static final String G = "golf";
	
	private List<String> stringList;
	
	@Before
	public void setUp() {
		stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
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

	@Test
	public void testJava8ForEachUsingConsumerInstance() {
		final List<String> newList = new ArrayList<>();
		Consumer<String> myConsumer = new Consumer<String>() {
			@Override 
			public void accept(String s) {
				newList.add(s);
			}
		};
		stringList.forEach(myConsumer);
		validateNewList(newList);
	}

	@Test
	public void testJava8ForEachUsingLambda() {
		final List<String> newList = new ArrayList<>();
		stringList.forEach(s -> newList.add(s));
		validateNewList(newList);
	}
	
}
