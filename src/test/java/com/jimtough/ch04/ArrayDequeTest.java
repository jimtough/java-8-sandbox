package com.jimtough.ch04;

import static com.jimtough.ch04.Ch04Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.junit.Before;
import org.junit.Test;

public class ArrayDequeTest {
	
	private ArrayDeque<String> arrayDeque;
	
	@Before
	public void setUp() {
		arrayDeque = new ArrayDeque<String>();
		arrayDeque.addAll(Arrays.asList(A,B,C,D,E,F,G));
	}

	@Test
	public void testRemoveFirstAndRemoveLast() {
		Deque<String> deque = arrayDeque;

		assertEquals(7, deque.size());
		assertEquals(A, deque.removeFirst());
		assertEquals(G, deque.removeLast());
		assertEquals(5, deque.size());
		assertTrue(deque.containsAll(Arrays.asList(B,C,D,E,F)));
	}

	@Test
	public void testPop() {
		Deque<String> deque = arrayDeque;

		assertEquals(7, deque.size());
		// 'pop()' is equivalent to newer 'removeFirst()' method
		assertEquals(A, deque.pop());
		assertEquals(B, deque.pop());
		assertEquals(5, deque.size());
		assertTrue(deque.containsAll(Arrays.asList(C,D,E,F,G)));
	}

	@Test
	public void testAddFirstAndAddLastAndPeekAndGetMethods() {
		Deque<String> deque = new ArrayDeque<>();
		deque.addFirst(A);
		deque.addLast(G);
		deque.addFirst(B);
		deque.addLast(F);
		
		assertEquals(4, deque.size());
		// 'peek' methods do not remove the element from the Deque
		assertEquals(B, deque.peekFirst());
		assertEquals(F, deque.peekLast());
		// 'get' methods do not remove the element from the Deque
		assertEquals(B, deque.getFirst());
		assertEquals(F, deque.getLast());
		// 'element()' is equivalent to 'getFirst()' (defined in Queue interface)
		assertEquals(B, deque.element());
		
		// 'remove' methods DO remove the element from the Deque
		assertEquals(B, deque.removeFirst());
		assertEquals(A, deque.removeFirst());
		assertEquals(G, deque.removeFirst());
		assertEquals(F, deque.removeFirst());
		
		assertTrue(deque.isEmpty());
		// The 'peek' methods return null if invoked on an empty Deque
		assertNull(deque.peekFirst());
		assertNull(deque.peekLast());
		//  All of these methods will throw NoSuchElementException
		//  if invoked on an empty Deque
		//deque.removeFirst();
		//deque.element();
		//deque.getFirst();
		//deque.getLast();
	}

	@Test
	public void testOfferMethods() {
		Deque<String> deque = new ArrayDeque<>();
		// 'offer()' and 'offerLast()' are equivalent
		deque.offerFirst(A);
		deque.offer(G);
		deque.offerFirst(B);
		deque.offerLast(F);
		
		// 'remove' methods DO remove the element from the Deque
		assertEquals(B, deque.removeFirst());
		assertEquals(A, deque.removeFirst());
		assertEquals(G, deque.removeFirst());
		assertEquals(F, deque.removeFirst());
		assertTrue(deque.isEmpty());
	}

	@Test
	public void testPollMethods() {
		Deque<String> deque = new ArrayDeque<>();
		deque.offer(A);
		deque.offer(B);
		deque.offer(C);
		deque.offer(D);
		
		// 'poll' methods DO remove the element from the Deque.
		// 'poll()' is equivalent to 'pollFirst()'
		assertEquals(A, deque.poll());
		assertEquals(D, deque.pollLast());
		assertEquals(B, deque.pollFirst());
		assertEquals(C, deque.poll());
		assertTrue(deque.isEmpty());
		// 'poll' methods return null when invoked on an empty Deque
		assertNull(deque.poll());
		assertNull(deque.pollFirst());
		assertNull(deque.pollLast());
	}


	@Test
	public void testRemoveFirstOccurrence() {
		Deque<String> deque = new ArrayDeque<>();

		deque.offer(A);
		deque.offer(B);
		deque.offer(A);
		deque.offer(C);
		deque.offer(A);
		deque.offer(D);
		deque.offer(A);

		assertEquals(7, deque.size());
		assertTrue(deque.removeFirstOccurrence(A));
		assertTrue(deque.removeFirstOccurrence(A));
		assertTrue(deque.removeFirstOccurrence(A));
		assertTrue(deque.removeFirstOccurrence(A));
		assertFalse(deque.removeFirstOccurrence(A));
		assertEquals(3, deque.size());
		assertTrue(deque.containsAll(Arrays.asList(B,C,D)));
		assertEquals(B, deque.pollFirst());
		assertEquals(D, deque.pollLast());
	}
	
}
