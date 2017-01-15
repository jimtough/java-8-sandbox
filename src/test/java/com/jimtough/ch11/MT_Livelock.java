package com.jimtough.ch11;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This test is manual because it does not pass or fail consistently.
// Thread livelocks are unpredictable.
public class MT_Livelock {

	private static final Logger LOGGER = LoggerFactory.getLogger(MT_Livelock.class);
	
	@Rule
	public TestName testName = new TestName();

	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	private static class IntegerChangerRunnable implements Runnable {
		private final AtomicInteger atomicInteger;
		private final int delta;
		public IntegerChangerRunnable(AtomicInteger atomicInteger, int delta) {
			this.atomicInteger = atomicInteger;
			this.delta = delta;
		}
		@Override
		public void run() {
			while (true) {
				int i = atomicInteger.get();
				if (i % 10_000_000 == 0) {
					LOGGER.debug("i: {}", i);
				}
				if (i != Integer.MIN_VALUE && i != Integer.MAX_VALUE) {
					atomicInteger.addAndGet(delta);
				} else {
					break;
				}
			}
			LOGGER.debug("exiting");
		}
	}
	
	@Test(timeout=120000)
	public void testLivelock() throws Exception {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		// This thread will keep trying to increment the AtomicInteger
		Thread t1 = new Thread(new IntegerChangerRunnable(atomicInteger, +1), "incrementor");
		// This thread will keep trying to decrement the AtomicInteger
		Thread t2 = new Thread(new IntegerChangerRunnable(atomicInteger, -1), "decrementor");
		t1.start();
		t2.start();

		while (true) {
			t1.join(2500);
			t2.join(2500);
			// I expect to see a tug-of-war between the two threads
			LOGGER.debug("current value: {}", atomicInteger.get());
			if (!t1.isAlive() && !t2.isAlive()) {
				break;
			}
		}
		LOGGER.debug("final value: {}", atomicInteger.get());
	}

}
