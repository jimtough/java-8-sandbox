package com.jimtough.ch11;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This test is manual because it does not pass or fail consistently.
// Thread deadlocks are unpredictable.
public class MT_Deadlock {

	private static final Logger LOGGER = LoggerFactory.getLogger(MT_Deadlock.class);
	
	@Rule
	public TestName testName = new TestName();

	@Before
	public void setUp() {}

	//--------------------------------------------------------------------

	private static class Food {
		public static long foodCount = 0;
	}

	private static class Beverage {
		public static long beverageCount = 0;
	}
	
	private static class MealBuilderRunnable implements Runnable {
		public void FoodFirstThenBeverage() {
			synchronized (Food.class) {
				synchronized (Beverage.class) {
					Food.foodCount++;
					Beverage.beverageCount++;
				}
			}
		}
		public void BeverageFirstThenFood() {
			synchronized (Beverage.class) {
				synchronized (Food.class) {
					Beverage.beverageCount++;
					Food.foodCount++;
				}
			}
		}
		@Override
		public void run() {
			final int MAX_MEALS = 10;
			for (int i=0; i<MAX_MEALS; i+=2) {
				FoodFirstThenBeverage();
				BeverageFirstThenFood();
				LOGGER.debug("Just made two meals");
			}
		}
	}
	
	@Test(timeout=3000)
	public void testDeadlock() throws Exception {
		Thread t1 = new Thread(new MealBuilderRunnable(), "t1");
		Thread t2 = new Thread(new MealBuilderRunnable(), "t2");
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		LOGGER.debug("food: {} | beverages: {}", Food.foodCount, Beverage.beverageCount);
		assertEquals(20, Food.foodCount);
		assertEquals(20, Beverage.beverageCount);
	}

}
