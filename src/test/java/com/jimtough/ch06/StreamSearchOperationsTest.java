package com.jimtough.ch06;

import static com.jimtough.ch06.Ch06Utils.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamSearchOperationsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamSearchOperationsTest.class);
    
	private List<String> stringList;

	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {
		stringList = newAlphabeticalStringList();
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testMatchOperations_A() {
		Predicate<String> isEmptyStringMatchPredicate = s -> s.isEmpty();
		
		boolean allStreamElementsAreEmptyString = stringList.stream().allMatch(isEmptyStringMatchPredicate);
		boolean anyStreamElementsAreEmptyString = stringList.stream().anyMatch(isEmptyStringMatchPredicate);
		boolean noStreamElementsAreEmptyString = stringList.stream().noneMatch(isEmptyStringMatchPredicate);

		LOGGER.debug("allStreamElementsAreEmptyString: {} | {}", allStreamElementsAreEmptyString, testName.getMethodName());
		LOGGER.debug("anyStreamElementsAreEmptyString: {} | {}", anyStreamElementsAreEmptyString, testName.getMethodName());
		LOGGER.debug("noStreamElementsAreEmptyString : {} | {}", noStreamElementsAreEmptyString, testName.getMethodName());
		assertFalse(allStreamElementsAreEmptyString);
		assertFalse(anyStreamElementsAreEmptyString);
		assertTrue(noStreamElementsAreEmptyString);
	}

	@Test
	public void testMatchOperations_B() {
		Predicate<String> stringContainsOMatchPredicate = s -> s.contains("o");
		
		boolean allStreamElementsContainsO = stringList.stream().allMatch(stringContainsOMatchPredicate);
		boolean anyStreamElementContainsO = stringList.stream().anyMatch(stringContainsOMatchPredicate);
		boolean noStreamElementContainsO = stringList.stream().noneMatch(stringContainsOMatchPredicate);

		LOGGER.debug("allStreamElementsContainsO: {} | {}", allStreamElementsContainsO, testName.getMethodName());
		LOGGER.debug("anyStreamElementContainsO : {} | {}", anyStreamElementContainsO, testName.getMethodName());
		LOGGER.debug("noStreamElementContainsO  : {} | {}", noStreamElementContainsO, testName.getMethodName());
		assertFalse(allStreamElementsContainsO);
		assertTrue(anyStreamElementContainsO);
		assertFalse(noStreamElementContainsO);
	}

	//--------------------------------------------------------------------
	
	// NOTE: Book says that the 'findFirst()' and 'findAny()' operations have important differences
	//       when using parallel streams, which is covered in Chapter 11.
	
	@Test
	public void testFindFirst() {
		Predicate<String> stringContainsOPredicate = s -> s.contains("o");
		
		Optional<String> result = stringList.stream().filter(stringContainsOPredicate).findFirst();

		LOGGER.debug("First string that contains 'o': {} | {}", result, testName.getMethodName());
		assertTrue(result.isPresent());
		assertEquals(B, result.get());
	}
	
	@Test
	public void testFindAny() {
		Predicate<String> stringContainsOPredicate = s -> s.contains("o");
		
		Optional<String> result = stringList.stream().filter(stringContainsOPredicate).findAny();

		LOGGER.debug("Any (one) string that contains 'o': {} | {}", result, testName.getMethodName());
		assertTrue(result.isPresent());
		assertEquals(B, result.get());
	}
	
	@Test
	public void testFindAnyWhenNothingShouldMatch() {
		Predicate<String> stringContainsZPredicate = s -> s.contains("Z");
		
		Optional<String> result = stringList.stream().filter(stringContainsZPredicate).findAny();

		LOGGER.debug("Any (one) string that contains 'Z': {} | {}", result, testName.getMethodName());
		assertFalse(result.isPresent());
	}

	//--------------------------------------------------------------------
	
}
