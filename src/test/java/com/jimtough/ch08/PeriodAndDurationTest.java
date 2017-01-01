package com.jimtough.ch08;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Experimenting with the Java 8 'Period' and 'Duration' classes.
 * The {@code Period} class is used to store a length of time with a precision of 'days' or larger.
 * The {@code Duration} class is used to store a length of time with a precision of 'hours' or smaller.
 * 
 * @author JTOUGH
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PeriodAndDurationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PeriodAndDurationTest.class);

	private static final LocalDate RELEASE_DATE_OF_ORIGINAL_STAR_WARS_MOVIE = LocalDate.of(1977, 5, 25);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testPeriodOf() {
		// Create a Period via one of the various static factory methods
		Period twoYears = Period.ofYears(2);
		Period twoMonths = Period.ofMonths(2);
		Period twoWeeks = Period.ofWeeks(2);
		Period twoDays = Period.ofDays(2);

		LOGGER.debug("twoYears: {} | twoMonths: {} | twoWeeks: {} | twoDays: {} | {}",
				twoYears, twoMonths, twoWeeks, twoDays, testName.getMethodName());
		
		assertEquals(2, twoYears.getYears());
		assertEquals(0, twoYears.getMonths());
		assertEquals(0, twoYears.getDays());
		
		assertEquals(0, twoMonths.getYears());
		assertEquals(2, twoMonths.getMonths());
		assertEquals(0, twoMonths.getDays());
		
		assertEquals(0, twoWeeks.getYears());
		assertEquals(0, twoWeeks.getMonths());
		assertEquals(14, twoWeeks.getDays());
		
		assertEquals(0, twoDays.getYears());
		assertEquals(0, twoDays.getMonths());
		assertEquals(2, twoDays.getDays());
	}
	
	@Test
	public void testPeriodPlus() {
		Period twoYears = Period.ofYears(2);
		Period twoMonths = Period.ofMonths(2);
		Period twoWeeks = Period.ofWeeks(2);
		Period twoDays = Period.ofDays(2);
		// You can add periods together. There is also a 'minus()' method to subtract them.
		Period sum = twoYears.plus(twoMonths).plus(twoWeeks).plus(twoDays);

		LOGGER.debug("sum: {} | {}", sum, testName.getMethodName());
		assertEquals(2, sum.getYears());
		assertEquals(2, sum.getMonths());
		assertEquals(16, sum.getDays());
	}
	
	@Test
	public void testPeriodBetween() {
		// Create a Period using a start LocalDate to end LocalDate
		Period ageOfStarWars = Period.between(RELEASE_DATE_OF_ORIGINAL_STAR_WARS_MOVIE, LocalDate.now());
		int ageYears = ageOfStarWars.getYears();
		int ageMonths = ageOfStarWars.getMonths();
		int ageDays = ageOfStarWars.getDays();

		LOGGER.debug("RELEASE_DATE_OF_ORIGINAL_STAR_WARS_MOVIE: {} | ageOfStarWars: {} | {}",
				RELEASE_DATE_OF_ORIGINAL_STAR_WARS_MOVIE, ageOfStarWars, testName.getMethodName());
		LOGGER.debug("age of Star Wars | ageYears: {} | ageMonths: {} | ageDays: {} | {}",
				ageYears, ageMonths, ageDays, testName.getMethodName());
	}
	
	@Test
	public void testPeriodNegated() {
		Period ageOfStarWars = Period.between(RELEASE_DATE_OF_ORIGINAL_STAR_WARS_MOVIE, LocalDate.now());
		Period ageOfStarWarsNegated = ageOfStarWars.negated();
		
		int ageYears = ageOfStarWarsNegated.getYears();
		int ageMonths = ageOfStarWarsNegated.getMonths();
		int ageDays = ageOfStarWarsNegated.getDays();

		LOGGER.debug("age of Star Wars (negated) | ageYears: {} | ageMonths: {} | ageDays: {} | p: {} | {}",
				ageYears, ageMonths, ageDays, ageOfStarWarsNegated, testName.getMethodName());
	}
	
	@Test
	public void testPeriodParse() {
		// Create a Period by parsing a string
		Period parsedPeriod = Period.parse("P5Y11M22D");
		int ppYears = parsedPeriod.getYears();
		int ppMonths = parsedPeriod.getMonths();
		int ppDays = parsedPeriod.getDays();

		LOGGER.debug("pp: {} | ppYears: {} | ppMonths: {} | ppDays: {} | {}",
				parsedPeriod, ppYears, ppMonths, ppDays, testName.getMethodName());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testDurationOf() {
		// Create a Duration via one of the various static factory methods
		Duration twoHours = Duration.ofHours(2);
		Duration twoMinutes = Duration.ofMinutes(2);
		Duration twoSeconds = Duration.ofSeconds(2);
		Duration twoMillis = Duration.ofMillis(2);
		Duration twoNanos = Duration.ofNanos(2);

		LOGGER.debug("twoHours: {} | twoMinutes: {} | twoSeconds: {} | twoMillis: {} | twoNanos: {} | {}",
				twoHours, twoMinutes, twoSeconds, twoMillis, twoNanos, testName.getMethodName());

		// NOTE: The Duration class only provides 'getSeconds()' and 'getNano()' getters.
		//       Snippet from the JDK Javadoc for this class:
		//       "This class models a quantity or amount of time in terms of seconds and nanoseconds."
		
		assertEquals(60*60*2, twoHours.getSeconds());
		assertEquals(0, twoHours.getNano());
		
		assertEquals(60*2, twoMinutes.getSeconds());
		assertEquals(0, twoMinutes.getNano());
		
		assertEquals(2, twoSeconds.getSeconds());
		assertEquals(0, twoSeconds.getNano());
		
		assertEquals(0, twoMillis.getSeconds());
		assertEquals(2000000, twoMillis.getNano());
		
		assertEquals(0, twoNanos.getSeconds());
		assertEquals(2, twoNanos.getNano());
	}
	
	@Test
	public void testDurationPlus() {
		// Create a Duration via one of the various static factory methods
		Duration twoHours = Duration.ofHours(2);
		Duration twoMinutes = Duration.ofMinutes(2);
		Duration twoSeconds = Duration.ofSeconds(2);
		Duration twoMillis = Duration.ofMillis(2);
		Duration twoNanos = Duration.ofNanos(2);
		// You can add durations together. There is also a 'minus()' method to subtract them.
		Duration sum = twoHours.plus(twoMinutes).plus(twoSeconds).plus(twoMillis).plus(twoNanos);

		LOGGER.debug("sum: {} | {}", sum, testName.getMethodName());
		
		assertEquals((60*60*2)+(60*2)+2, sum.getSeconds());
		assertEquals(2000000+2, sum.getNano());
	}
	
	@Test
	public void testDurationBetweenUsingLocalTimes() {
		// Create a Duration using a start LocalTime to end LocalTime
		Duration d = Duration.between(LocalTime.of(9, 0), LocalTime.of(17, 30));
		
		LOGGER.debug("d: {} | {}", d, testName.getMethodName());
		// Should be 8 hours, 30 minutes, no partial seconds
		assertEquals((8*60*60)+(30*60), d.getSeconds());
		assertEquals(0, d.getNano());
	}
	
	@Test
	public void testDurationBetweenUsingLocalDateTimes() {
		// Create a Duration using a start LocalDateTime to end LocalDateTime
		LocalDateTime yesterdayAtNoon = LocalDateTime.of(LocalDate.now(), LocalTime.NOON).minusDays(1);
		LocalDateTime tomorrowAtMidnight = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
		
		Duration d = Duration.between(yesterdayAtNoon, tomorrowAtMidnight);
		
		LOGGER.debug("d: {} | {}", d, testName.getMethodName());
		// Should be 36 hours, no partial seconds
		assertEquals(36*60*60, d.getSeconds());
		assertEquals(0, d.getNano());
	}
	
	@Test
	public void testDurationParse() {
		// Create a Duration by parsing a string
		Duration parsedDuration = Duration.parse("PT2H2M2.002000002S");

		LOGGER.debug("parsedDuration: {} | {}", parsedDuration, testName.getMethodName());
		
		assertEquals((60*60*2)+(60*2)+2, parsedDuration.getSeconds());
		assertEquals(2000000+2, parsedDuration.getNano());
	}

}
