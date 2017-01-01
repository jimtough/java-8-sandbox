package com.jimtough.ch08;

import static org.junit.Assert.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalTypesTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalTypesTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testLocalDateNowAndOf() {
		LocalDate now = LocalDate.now();
		LocalDate xmas2016 = LocalDate.of(2016, 12, 25);
		LocalDate alsoXmas2016 = LocalDate.of(2016, Month.DECEMBER, 25);
		
		LOGGER.debug("now: {}", now);
		assertEquals(xmas2016, alsoXmas2016);
		assertTrue(xmas2016.isBefore(now));
	}
	
	@Test
	public void testLocalDateOfAndOfEpochDay() {
		LocalDate firstDayOfUnixEpoch = LocalDate.of(1970, 1, 1);
		LocalDate alsoFirstDayOfUnixEpoch = LocalDate.ofEpochDay(0);
		
		LOGGER.debug("firstDayOfUnixEpoch: {}", firstDayOfUnixEpoch);
		assertEquals(firstDayOfUnixEpoch, alsoFirstDayOfUnixEpoch);
	}
	
	@Test
	public void testLocalDateOfYearDay() {
		LocalDate firstOfFeb2016 = LocalDate.of(2016, 2, 1);
		LocalDate alsoFirstOfFeb2016 = LocalDate.ofYearDay(2016, 32);
		
		LOGGER.debug("alsoFirstOfFeb2016: {}", alsoFirstOfFeb2016);
		assertEquals(firstOfFeb2016, alsoFirstOfFeb2016);
	}
	
	@Test
	public void testLocalDateParse() {
		LocalDate xmas2016 = LocalDate.of(2016, 12, 25);
		// Parse local date string using ISO standard format
		LocalDate parsedXmas2016 = LocalDate.parse("2016-12-25");
		
		LOGGER.debug("parsedXmas2016: {}", parsedXmas2016);
		assertEquals(xmas2016, parsedXmas2016);
	}
	
	@Test
	public void testLocalDateParseUsingBuiltInDateTimeFormatter() {
		LocalDate xmas2016 = LocalDate.of(2016, 12, 25);
		// Parse local date string using ISO standard format
		LocalDate parsedXmas2016 = LocalDate.parse("2016-12-25", DateTimeFormatter.ISO_DATE);
		
		LOGGER.debug("parsedXmas2016: {}", parsedXmas2016);
		assertEquals(xmas2016, parsedXmas2016);
	}
	
	@Test
	public void testLocalDateParseUsingCustomDateTimeFormatter() {
		DateTimeFormatter dtfAmericanStyle = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		
		LocalDate xmas2016 = LocalDate.of(2016, 12, 25);
		// Parse local date string using custom format
		LocalDate customParsedXmas2016 = LocalDate.parse("12/25/2016", dtfAmericanStyle);
		
		LOGGER.debug("customParsedXmas2016: {}", customParsedXmas2016);
		assertEquals(xmas2016, customParsedXmas2016);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testLocalTimeNowAndOf() {
		LocalTime now = LocalTime.now();
		LocalTime startOfDay = LocalTime.of(0, 0, 0, 0);
		LocalTime arbitraryTime = LocalTime.of(13, 59, 33);
		
		LOGGER.debug("LocalTime | now: {} | startOfDay: {} | arbitraryTime: {} | MIDNIGHT: {} | MIN: {} | MAX: {}",
				now, startOfDay, arbitraryTime, LocalTime.MIDNIGHT, LocalTime.MIN, LocalTime.MAX);
		assertEquals(startOfDay, LocalTime.MIDNIGHT);
		assertEquals(LocalTime.MIDNIGHT, LocalTime.MIN);
	}
	
	@Test
	public void testLocalTimeOfSecondOfDay() {
		LocalTime arbitraryTime = LocalTime.ofSecondOfDay(3661);
		
		LOGGER.debug("LocalTime | arbitraryTime: {}", arbitraryTime);
		assertEquals("01:01:01", arbitraryTime.toString());
	}
	
	@Test
	public void testLocalTimeParse() {
		LocalTime lt = LocalTime.parse("01:01:01");
		
		LOGGER.debug("LocalTime | lt: {}", lt);
		assertEquals(3661, lt.toSecondOfDay());
	}
	
	@Test
	public void testLocalTimePlusMethods() {
		LocalTime arbitraryTime = LocalTime.of(13, 59, 33);
		LocalTime sameArbitraryTime = LocalTime.MIDNIGHT.plusHours(13).plusMinutes(59).plusSeconds(33);
		
		assertEquals(arbitraryTime, sameArbitraryTime);
	}
	
	@Test
	public void testLocalTimeNowWithClockParameter() {
		// This Clock would be useful for unit testing. It will always return 01:01 for the LocalTime.
		Clock mockClock = Clock.fixed(Instant.ofEpochSecond(3660), ZoneId.of("GMT"));
		
		LocalTime lt = LocalTime.now();
		LocalTime ltUtcClock = LocalTime.now(Clock.systemUTC());
		LocalTime ltSystemDefaultTimezomeClock = LocalTime.now(Clock.systemDefaultZone());
		LocalTime ltUsingMockClock = LocalTime.now(mockClock);

		LOGGER.debug("LocalTime | lt: {} | ltUtcClock: {} | ltSystemDefaultTimezomeClock: {} | ltUsingMockClock: {}",
				lt, ltUtcClock, ltSystemDefaultTimezomeClock, ltUsingMockClock);
		// It appears that LocalTime.now() uses Clock.systemDefaultZone()
	}
	
	@Test
	public void testLocalTimeNowWithZoneIdParameter() {
		LocalTime lt = LocalTime.now();
		LocalTime ltUtc = LocalTime.now(ZoneId.of("GMT"));
		LocalTime ltSystemDefault = LocalTime.now(ZoneId.systemDefault());

		LOGGER.debug("LocalTime | lt: {} | ltUtc: {} | ltSystemDefault: {}", lt, ltUtc, ltSystemDefault);
		// It appears that LocalTime.now() uses ZoneId.systemDefault()
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testLocalDateTimeNowAndOf() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime xmas2016 = LocalDateTime.of(2016, 12, 25, 0, 0);
		LocalDateTime alsoXmas2016 = LocalDateTime.of(2016, Month.DECEMBER, 25, 0, 0);
		
		LOGGER.debug("now: {}", now);
		assertEquals(xmas2016, alsoXmas2016);
		assertTrue(xmas2016.isBefore(now));

		// LocalDateTime can also be decomposed
		LocalDate ldXmas2016 = LocalDate.of(2016, 12, 25);
		LocalTime startOfDay = LocalTime.of(0, 0, 0, 0);
		assertEquals(ldXmas2016, xmas2016.toLocalDate());
		assertEquals(startOfDay, xmas2016.toLocalTime());
	}
	
	@Test
	public void testLocalDateTimeParse() {
		LocalDate ldArbitrary = LocalDate.of(2016, 11, 8);
		// NOTE: Last parameter of LocalTime.of() is NANO-seconds, not MILLI-seconds
		LocalTime ltArbitrary = LocalTime.of(21, 59, 33, 987650000);
		// LocalDateTime can be composed of a LocalDate and LocalTime
		LocalDateTime ldtArbitrary = LocalDateTime.of(ldArbitrary, ltArbitrary);
		// Parse local date/time string using ISO standard format
		LocalDateTime ldtParsed = LocalDateTime.parse("2016-11-08T21:59:33.98765");
		
		LOGGER.debug("ldtParsed: {}", ldtParsed);
		assertEquals(ldtArbitrary, ldtParsed);
	}

	// NOTE
	// The LocalDateTime class has similar methods to LocalDate and LocalTime.
	// I'm not going to create examples of each.
	
	//--------------------------------------------------------------------

}
