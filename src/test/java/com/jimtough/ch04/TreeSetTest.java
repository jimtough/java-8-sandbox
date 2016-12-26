package com.jimtough.ch04;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

public class TreeSetTest {

	private static final String A = "alpha";
	private static final String B = "bravo";
	private static final String C = "charlie";
	private static final String D = "delta";
	private static final String E = "echo";
	private static final String F = "foxtrot";
	private static final String G = "golf";
	
	private TreeSet<String> treeSet;
	
	@Before
	public void setUp() {
		treeSet = new TreeSet<String>();
		treeSet.addAll(Arrays.asList(C, E, F, A, G, B, D));
	}

	@Test
	public void testSortedSet() {
		SortedSet<String> ss = treeSet;
		
		assertTrue(ss.contains(D));
		assertFalse(ss.contains("foobar"));

		assertEquals(A, ss.first());
		assertEquals(G, ss.last());
		
		// 'headSet()' returns a subset containing elements that are
		// less than (excludes equal-to elements) the value provided
		assertEquals(3, ss.headSet(D).size());
		assertTrue(ss.headSet(D).containsAll(Arrays.asList(A,B,C)));
		// 'tailSet()' returns a subset containing elements that are
		// greater than OR equal-to the value provided
		assertEquals(4, ss.tailSet(D).size());
		assertTrue(ss.tailSet(D).containsAll(Arrays.asList(D,E,F,G)));

		SortedSet<String> nsSubset = ss.subSet(B, F);
		assertEquals(4, nsSubset.size());
		assertTrue(nsSubset.containsAll(Arrays.asList(B,C,D,E)));
	}

	@Test
	public void testNavigableSet() {
		NavigableSet<String> ns = treeSet;
		
		// 'ceiling()' gets the smallest value that is greater than or
		// equal to the value provided, or null if none exists
		assertEquals(B, ns.ceiling("baby"));
		assertEquals(G, ns.ceiling(G));
		assertNull(ns.ceiling("hello, null!"));
		// 'floor()' gets the largest value that is less than or
		// equal to the value provided, or null if none exists
		assertEquals(E, ns.floor("exist"));
		assertEquals(A, ns.floor(A));
		assertNull(ns.floor("aaaaa null!"));
		
		assertTrue(ns.contains(D));
		assertFalse(ns.contains("foobar"));

		assertEquals(A, ns.first());
		assertEquals(G, ns.last());
		assertEquals(G, ns.descendingSet().first());
		assertEquals(A, ns.descendingSet().last());
		
		// 'headSet()' returns a subset containing elements that are
		// less than (excludes equal-to elements) the value provided
		assertEquals(3, ns.headSet(D).size());
		assertTrue(ns.headSet(D).containsAll(Arrays.asList(A,B,C)));
		// Overloaded variant allows caller to include/exclude equal-to elements
		assertEquals(4, ns.headSet(D,true).size());
		assertTrue(ns.headSet(D,true).containsAll(Arrays.asList(A,B,C,D)));
		// 'tailSet()' returns a subset containing elements that are
		// greater than OR equal-to the value provided
		assertEquals(4, ns.tailSet(D).size());
		assertTrue(ns.tailSet(D).containsAll(Arrays.asList(D,E,F,G)));
		// Overloaded variant allows caller to include/exclude equal-to elements
		assertEquals(3, ns.tailSet(D,false).size());
		assertTrue(ns.tailSet(D,false).containsAll(Arrays.asList(E,F,G)));

		// 'lower()' returns the greatest value that is less than the
		// value provided, or null if none exists
		assertEquals(B, ns.lower(C));
		assertEquals(B, ns.lower("bzzzzz"));
		assertNull(ns.lower("a"));
		// 'higher()' returns the smallest value that is greater than the
		// value provided, or null if none exists
		assertEquals(D, ns.higher(C));
		assertEquals(D, ns.higher("czzzzzz"));
		assertNull(ns.higher(G));

		NavigableSet<String> nsSubset = ns.subSet(B, true, F, true);
		assertEquals(5, nsSubset.size());
		assertTrue(nsSubset.containsAll(Arrays.asList(B,C,D,E,F)));
	}

	@Test
	public void testNavigableSetPollFirstAndPollLast() {
		NavigableSet<String> ns = treeSet;
		
		// The 'poll' methods remove elements from the set
		assertEquals(7, ns.size());
		assertEquals(A, ns.pollFirst());
		assertEquals(B, ns.pollFirst());
		assertEquals(G, ns.pollLast());
		assertEquals(F, ns.pollLast());
		assertEquals(3, ns.size());
	}

	@Test
	public void testNavigableSetRemoveIf() {
		NavigableSet<String> ns = treeSet;
		// Remove all elements that are greater than or equal to
		// constant string D
		Predicate<String> pred = s -> D.compareTo(s) <= 0;
		assertEquals(7, ns.size());
		
		ns.removeIf(pred);
		
		assertEquals(3, ns.size());
		assertTrue(ns.containsAll(Arrays.asList(A,B,C)));
	}
	
}
