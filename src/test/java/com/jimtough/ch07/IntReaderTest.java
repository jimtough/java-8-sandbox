package com.jimtough.ch07;

import static org.junit.Assert.*;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntReaderTest {

	private static final String STRING_CONTAINING_ONLY_INTEGERS = "10   999  888     777";
	private static final String STRING_CONTAINING_NON_NUMERIC_TEXT = "These are not the values you are looking for...";
	
	@Rule
	public TestName testName = new TestName();

	private IntReader intReader;
	
	@Before
	public void setUp() {
		intReader = new IntReader();
	}
	
	//--------------------------------------------------------------------
	
	@Test
	public void testReadFirstInt() {
		int i = intReader.readFirstInt(STRING_CONTAINING_ONLY_INTEGERS);
		
		assertEquals(10, i);
	}

	@Test
	public void testReadFirstInteger() {
		Integer i = intReader.readFirstInteger(STRING_CONTAINING_ONLY_INTEGERS);
		
		assertEquals(10, i.intValue());
	}

	@Test
	public void testReadFirstIntegerWithMulticatch() {
		Integer i = intReader.readFirstIntegerWithMulticatch(STRING_CONTAINING_ONLY_INTEGERS);
		
		assertEquals(10, i.intValue());
	}
	
	//--------------------------------------------------------------------
	
	@Test(expected=InputMismatchException.class)
	public void testReadFirstIntWithNonNumericString() {
		intReader.readFirstInt(STRING_CONTAINING_NON_NUMERIC_TEXT);
	}

	@Test
	public void testReadFirstIntegerWithNonNumericString() {
		assertNull(intReader.readFirstInteger(STRING_CONTAINING_NON_NUMERIC_TEXT));
	}

	@Test
	public void testReadFirstIntegerWithMulticatchWithNonNumericString() {
		assertNull(intReader.readFirstIntegerWithMulticatch(STRING_CONTAINING_NON_NUMERIC_TEXT));
	}
	
	//--------------------------------------------------------------------
	
	@Test(expected=NoSuchElementException.class)
	public void testReadFirstIntWithEmptyString() {
		intReader.readFirstInt("");
	}

	@Test
	public void testReadFirstIntegerWithEmptyString() {
		assertNull(intReader.readFirstInteger(""));
	}

	@Test
	public void testReadFirstIntegerWithMulticatchWithEmptyString() {
		assertNull(intReader.readFirstIntegerWithMulticatch(""));
	}
	
	//--------------------------------------------------------------------
	
	@Test(expected=NullPointerException.class)
	public void testReadFirstIntWithNullString() {
		intReader.readFirstInt(null);
	}

	@Test
	public void testReadFirstIntegerWithNullString() {
		assertNull(intReader.readFirstInteger(null));
	}

	@Test
	public void testReadFirstIntegerWithMulticatchWithNullString() {
		assertNull(intReader.readFirstIntegerWithMulticatch(null));
	}

	//--------------------------------------------------------------------
	
}
