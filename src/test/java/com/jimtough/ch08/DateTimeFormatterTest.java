package com.jimtough.ch08;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateTimeFormatterTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeFormatterTest.class);
	private static final String EOL = System.lineSeparator();
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testDateTimeFormatterFormat() {
		// This one seems to be the most 'standard'
		DateTimeFormatter dtfA = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		// This one adds the Java zone name to the end. Why put 'ISO' in the name if it IS NOT STANDARD???
		DateTimeFormatter dtfB = DateTimeFormatter.ISO_DATE_TIME;
		ZonedDateTime zdtNow = ZonedDateTime.now();
		
		LOGGER.debug(EOL + "dtfA/zdtNow: {} " + EOL + "dtfB/zdtNow: {}" + EOL + "{}",
				dtfA.format(zdtNow), dtfB.format(zdtNow), testName.getMethodName());
	}
	
	@Test
	public void testDateTimeFormatterWithInstant() {
		LOGGER.debug("DateTimeFormatter.ISO_INSTANT.format(Instant.now()): {} | {}",
				DateTimeFormatter.ISO_INSTANT.format(Instant.now()), testName.getMethodName());
	}
	
	@Test
	public void testDateTimeFormatterParse() {
		final String VALID_ZDT_STRING = "2016-12-25T23:59:59.999-04:00[America/Halifax]";
		// NOTES: The single parameter 'parse()' method returns a TemporalAccessor, which isn't
		//        particularly useful in application code. The overloaded 'parse()' method below
		//        takes a 'TemporalQuery<T>' instance as a second parameter, which is used to
		//        convert the 'TemporalAccessor' into one of the temporal classes. I will use
		//        a lambda expression to define the TemporalQuery such that it returns a
		//        ZonedDateTime.
		TemporalQuery<ZonedDateTime> tq = ZonedDateTime::from;
		
		ZonedDateTime zdtA = DateTimeFormatter.ISO_DATE_TIME.parse(VALID_ZDT_STRING, tq);
		ZonedDateTime zdtB = DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(VALID_ZDT_STRING, tq);
		
		LOGGER.debug("zdtA: {} | zdtB: {} | {}", zdtA, zdtB, testName.getMethodName());
	}
	
	@Test
	public void testDateTimeFormatterOfPattern() {
		// NOTE Literal test in the pattern (the word 'on') must be enclosed in single quotes,
		//      otherwise the characters are interpreted as part of the pattern. If a literal
		//      string contains a single-quote, then use two consecutive single-quotes to
		//      escape it.
		DateTimeFormatter dtfOfPattern = DateTimeFormatter.ofPattern("'It''s' HH:mm 'on' EEEE MMMM dd, YYYY G");
		ZonedDateTime zdtNow = ZonedDateTime.now(ZoneId.of("America/Halifax"));
		String s = dtfOfPattern.format(zdtNow);
		
		LOGGER.debug("zdtNow (from pattern): {} | {}", s, testName.getMethodName());
		assertTrue(s.startsWith("It's"));
		assertTrue(s.contains("AD"));
	}
	
}
