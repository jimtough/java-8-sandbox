package com.jimtough.ch11;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SynchronizedTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedTest.class);
	
	@Rule
	public TestName testName = new TestName();
	@Rule
	public Timeout globalTimeout = new Timeout(20, TimeUnit.SECONDS);

	@Before
	public void setUp() {}

	//--------------------------------------------------------------------

	private static class RunnableWithNonSynchronizedStaticCounter implements Runnable {
		static int staticCounter = 0;
		@Override
		public void run() {
			staticCounter++;
			LOGGER.debug("count A: " + staticCounter);
			staticCounter++;
			LOGGER.debug("count B: " + staticCounter);
			staticCounter++;
			LOGGER.debug("count C: " + staticCounter);
		}
	}
	
	@Test
	public void testNonSynchronizedInteger() throws Exception {
		Thread t1 = new Thread(new RunnableWithNonSynchronizedStaticCounter(), "t1");
		Thread t2 = new Thread(new RunnableWithNonSynchronizedStaticCounter(), "t2");
		Thread t3 = new Thread(new RunnableWithNonSynchronizedStaticCounter(), "t3");
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
	}

	//--------------------------------------------------------------------

	private static class RunnableWithSynchronizedStaticCounter implements Runnable {
		private static int _staticCounter = 0;
		private static synchronized void incrementCounter() {
			_staticCounter++;
			LOGGER.debug("count: " + _staticCounter);
		};
		@Override
		public void run() {
			incrementCounter();
			incrementCounter();
			incrementCounter();
		}
	}
	
	@Test
	public void testSynchronizedInteger() throws Exception {
		Thread t1 = new Thread(new RunnableWithSynchronizedStaticCounter(), "t1");
		Thread t2 = new Thread(new RunnableWithSynchronizedStaticCounter(), "t2");
		Thread t3 = new Thread(new RunnableWithSynchronizedStaticCounter(), "t3");
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
	}

	//--------------------------------------------------------------------

}
