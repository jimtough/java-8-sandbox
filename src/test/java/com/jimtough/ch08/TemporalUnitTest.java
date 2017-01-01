package com.jimtough.ch08;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemporalUnitTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemporalUnitTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testOutputChronoUnitValues() {
		LOGGER.debug("");
		for (ChronoUnit cUnit : ChronoUnit.values()) {
			LOGGER.debug("{} | date-based? {} | time-based? {} | estimated? {} | {}",
					cUnit, cUnit.isDateBased(), cUnit.isTimeBased(), cUnit.isDurationEstimated(), cUnit.getDuration());
		}
		LOGGER.debug("");
	}
	
	@Test
	public void testDurationOfXChronoUnits() {
		Duration oneMinute = Duration.of(1, ChronoUnit.MINUTES);
		Duration oneHour = Duration.of(1, ChronoUnit.HOURS);
		Duration oneDay = Duration.of(1, ChronoUnit.DAYS);
		
		LOGGER.debug("oneMinute: {} ({}) | oneHour {} ({}) | oneDay {} ({}) | {}",
				oneMinute, oneMinute.getSeconds(), oneHour, oneHour.getSeconds(), oneDay, oneDay.getSeconds(), testName.getMethodName());
	}
	
}
