package com.jimtough.ch08;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Experimenting with the Java 8 'Instant' class.
 * 
 * @author JTOUGH
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstantTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(InstantTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testInstantNow() {
		Instant instantNow = Instant.now();
		LOGGER.debug("instantNow: {} | {}", instantNow, testName.getMethodName());
	}
	
	@Test
	public void testInstantVersusLocalDateTime() {
		Instant instantNow = Instant.now();
		// Create a LocalDateTime from an Instant via the static factory method
		// 'ofInstant()' of the LocalDateTime class
		LocalDateTime ldtNow = LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault());
		
		// Notice that Instant is always with respect to GMT/'Zulu' time,
		// but LocalDateTime is with respect to a JVM timezone (I chose the system default above)
		LOGGER.debug("instantNow: {} | ldtNow: {} | {}",
				instantNow, ldtNow, testName.getMethodName());
	}
	
	@Test
	public void testInstantVersusLegacyDate() {
		Instant instantNow = Instant.now();
		// The java.util.Date() class now has a static factory method named 'from()'
		// that accepts a Java 8 Instant as input
		java.util.Date legacyDateFromInstantNow = java.util.Date.from(instantNow);

		// Notice that Instant is always with respect to GMT/'Zulu' time,
		// but the java.util.Date is with respect to the local JVM timezone
		LOGGER.debug("instantNow: {} | legacyDateFromInstantNow: {} | {}", 
				instantNow, legacyDateFromInstantNow, testName.getMethodName());
	}
	
	@Test
	public void testInstantMinusAndPlus() {
		Instant now = Instant.now();
		Instant oneMsAgo = now.minusMillis(1);
		Instant oneMsFromNow = now.plusMillis(1);
		
		LOGGER.debug("oneMsAgo: {} | now: {} | oneMsFromNow: {} | {}",
				oneMsAgo, now, oneMsFromNow, testName.getMethodName());
		assertTrue(oneMsAgo.isBefore(now));
		assertTrue(oneMsFromNow.isAfter(now));
	}
	
	@Test
	public void testInstantGetEpochSecond() {
		Instant instant = Instant.now();
		java.util.Date legacyDate = java.util.Date.from(instant);
		// Same as java.util.Date.getTime(), but only has accuracy in Seconds, not Milliseconds
		long instantEpochSecond = instant.getEpochSecond();
		// Use the getNano() method to get the fractional seconds, in Nanoseconds
		int instantNanos = instant.getNano();
		// Exactly the same as java.util.Date.getTime()
		long instantEpochMilli = instant.toEpochMilli();
		long legacyDateEpochMilli = legacyDate.getTime();
		
		LOGGER.debug("instant sec/nanos: {}.{} | instantEpochMilli: {} | legacyDateEpochMilli: {} | {}",
				instantEpochSecond, instantNanos, instantEpochMilli, legacyDateEpochMilli, testName.getMethodName());
		assertEquals(instantEpochMilli, legacyDateEpochMilli);
	}
	
}


