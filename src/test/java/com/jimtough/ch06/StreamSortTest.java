package com.jimtough.ch06;

import static com.jimtough.ch06.Ch06Utils.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
public class StreamSortTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StreamSortTest.class);

	private List<String> unsortedStringList;
	private int[] unsortedIntArray;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		unsortedStringList = newDistinctButUnsortedStringList();
		unsortedIntArray = newUnsortedIntArray();
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testStreamSort_natural() {
		Object[] output = unsortedStringList.stream().sorted().toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(A, output[0]);
		assertEquals(G, output[6]);
	}
	
	@Test
	public void testStreamSort_stringLength() {
		Comparator<String> lengthComp = (s1,s2) -> s1.length() - s2.length();
		
		Object[] output = unsortedStringList.stream().sorted(lengthComp).toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertNotEquals(A, output[0]); // 'alpha' will not be first
		assertNotEquals(G, output[6]); // 'golf' will not be last
	}
	
	@Test
	public void testStreamSort_comparatorChaining() {
		Comparator<String> lengthComp = (s1,s2) -> s1.length() - s2.length();
		Comparator<String> naturalComp = String::compareTo;

		
		Object[] output = unsortedStringList.stream()
				// First sort by 'lengthComp' comparator, then use 'naturalComp' to break any ties in length
				.sorted(lengthComp.thenComparing(naturalComp))
				.toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(E, output[0]); // 'echo' will be first
		assertEquals(F, output[6]); // 'foxtrot' will be last
	}
	
	@Test
	public void testStreamSort_comparatorReversal() {
		Comparator<String> lengthComp = (s1,s2) -> s1.length() - s2.length();
		Comparator<String> naturalComp = String::compareTo;

		
		Object[] output = unsortedStringList.stream()
				// First sort by 'lengthComp' comparator in descending order, 
				// then use 'naturalComp' to break any ties in length
				.sorted(lengthComp.reversed().thenComparing(naturalComp))
				.toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(C, output[0]); // 'charlie' will be first
		assertEquals(G, output[6]); // 'golf' will be last
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testIntStreamSort() {
		int[] output = IntStream.of(unsortedIntArray).sorted().toArray();

		LOGGER.debug("{} | {}", Arrays.toString(output), testName.getMethodName());
		assertEquals(1, output[0]);
		assertEquals(10, output[9]);
	}
	
}


