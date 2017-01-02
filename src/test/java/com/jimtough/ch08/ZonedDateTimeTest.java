package com.jimtough.ch08;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZonedDateTimeTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZonedDateTimeTest.class);
	private static final String EOL = System.lineSeparator();
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Ignore("Ignoring this test because it outputs a lot of values")
	@Test
	public void testOutputAllValidZoneIds() {
		ZoneId.getAvailableZoneIds().forEach(z -> LOGGER.debug("ZoneId: {}", z));
	}
	
	@Test
	public void testOutputAllZoneIdsForAmerica() {
		ZoneId.getAvailableZoneIds().stream()
				.filter(z -> z.startsWith("America"))
				.sorted()
				.forEach(z -> LOGGER.debug("ZoneId: {}", z));
	}
	
	@Test
	public void testOutputAllZoneIdsForEurope() {
		ZoneId.getAvailableZoneIds().stream()
				.filter(z -> z.startsWith("Europe"))
				.sorted()
				.forEach(z -> LOGGER.debug("ZoneId: {}", z));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testZoneRules() {
		ZoneId zoneId = ZoneId.of("America/Halifax");
		ZoneRules zoneRules = zoneId.getRules();
		LOGGER.debug("ZoneRules for 'America/Halifax'" + EOL
					+ " | current DST offset: {}" + EOL
					+ " | current Standard offset : {}" + EOL
					+ " | current offset : {}" + EOL
					+ " | is DST in effect? : {}" + EOL
					+ " | is 'fixed' offset? : {}" + EOL
					+ " | prev transition : {}" + EOL
					+ " | next transition : {}" + EOL
					+ " | {}",
				zoneRules.getDaylightSavings(Instant.now()), 
				zoneRules.getStandardOffset(Instant.now()),
				zoneRules.getOffset(Instant.now()),
				zoneRules.isDaylightSavings(Instant.now()),
				zoneRules.isFixedOffset(),
				zoneRules.previousTransition(Instant.now()),
				zoneRules.nextTransition(Instant.now()), 
				testName.getMethodName());
	}

	//--------------------------------------------------------------------

	@Test
	public void testZonedDateTimeOf() {
		LocalDate ld = LocalDate.now();
		LocalTime lt = LocalTime.now();
		LocalDateTime ldt = LocalDateTime.of(ld, lt);
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime zdtA = ZonedDateTime.of(ld, lt, ZoneId.systemDefault());
		ZonedDateTime zdtB = ZonedDateTime.of(ldt, ZoneId.systemDefault());
		ZonedDateTime zdtC = ldt.atZone(ZoneId.systemDefault());
		ZonedDateTime zdtD = ZonedDateTime.of(2016, 12, 24, 23, 59, 59, 999999999, ZoneId.systemDefault());

		LOGGER.debug("now: {} | zdtA: {} | zdtB: {} | zdtC: {} | zdtD: {} | {}", 
				now, zdtA, zdtB, zdtC, zdtD, testName.getMethodName());
		assertEquals(zdtA, zdtB);
		assertEquals(zdtB, zdtC);
	}

	@Test
	public void testZonedDateTimeGetOffset() {
		ZonedDateTime now = ZonedDateTime.now();
		ZoneId zi = now.getZone();
		ZoneOffset zo = now.getOffset();

		LOGGER.debug("now: {} | zi: {} | zo: {} | {}", now, zi, zo, testName.getMethodName());
	}

	@Test
	public void testDifferenceBetweenSameTimeInDifferentZones() {
		LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		ZonedDateTime zdtRome = ZonedDateTime.of(ldt, ZoneId.of("Europe/Rome"));
		ZonedDateTime zdtHalifax = ZonedDateTime.of(ldt, ZoneId.of("America/Halifax"));

		Duration duration = Duration.between(zdtRome, zdtHalifax);

		LOGGER.debug("zdtRome: {} | zdtHalifax: {} | {}", zdtRome, zdtHalifax, testName.getMethodName());
		LOGGER.debug("Time difference between Halifax and Rome (as Duration): {} | {}", duration, testName.getMethodName());
	}

	@Test
	public void testZonedDateTimeParse() {
		final String VALID_ZDT_STRING = "2016-12-25T23:59:59.999-04:00[America/Halifax]";
		ZonedDateTime parsedZdt = ZonedDateTime.parse(VALID_ZDT_STRING);

		LOGGER.debug("parsedZdt: {} | {}", parsedZdt, testName.getMethodName());
	}
	
}



