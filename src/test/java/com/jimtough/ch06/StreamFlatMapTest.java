package com.jimtough.ch06;

import static com.jimtough.ch06.Ch06Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamFlatMapTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StreamFlatMapTest.class);
    
	private List<List<String>> stringListList;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		stringListList = new ArrayList<>();
		stringListList.add(Arrays.asList(new String[]{A,B}));
		stringListList.add(Arrays.asList(new String[]{C,D,E}));
		stringListList.add(Arrays.asList(new String[]{F,G}));
	}

	//--------------------------------------------------------------------

	@Test
	public void testMapVersusFlatMap() {
		// The 'map()' operation creates a new Stream<Stream<String>> - not what I wanted
		Object[] mapOutput = stringListList.stream().map(s -> s.stream()).distinct().toArray();
		// The 'flatMap()' operation creates a new Stream<String> from each element of the original Stream<List<String>>
		// and results in a single output stream of Stream<String>
		Object[] flatMapOutput = stringListList.stream().flatMap(s -> s.stream()).distinct().toArray();
		
		LOGGER.debug("{} | {}", mapOutput, testName.getMethodName());
		LOGGER.debug("{} | {}", flatMapOutput, testName.getMethodName());
		assertEquals(7, flatMapOutput.length);
		assertTrue(flatMapOutput[0] instanceof String);
		assertEquals(A, flatMapOutput[0]);
		assertEquals(G, flatMapOutput[6]);
		assertNotEquals(7, mapOutput.length);
		assertFalse(mapOutput[0] instanceof String);
	}
	
}

