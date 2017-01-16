package com.jimtough.ch11;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

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
public class AtomicIntegerArrayTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AtomicIntegerArrayTest.class);
	
	@Rule
	public TestName testName = new TestName();
	@Rule
	public Timeout globalTimeout = new Timeout(20, TimeUnit.SECONDS);
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------

	private static class SimpleThreadFactory implements ThreadFactory {
		private final AtomicInteger count = new AtomicInteger(0);
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "T-" + count.incrementAndGet());
			t.setDaemon(true);
			return t;
		}
	}

	private static void updateIfAppropriateUsingCompareAndSet(
			final AtomicIntegerArray aia,
			final int index,
			final int newVal) {
		// This has to be done in a while loop in case the 'compareAndSet()' fails.
		// A failure will happen if another thread updates the AtomicReference value
		// between the time when this thread called 'get()' and 'compareAndSet()'.
		boolean retry;
		do {
			boolean attemptToUpdate = false;
			retry = false;
			int curVal = aia.get(index);
			switch (index) {
				case 0:
					// Want the smallest value
					if (newVal < curVal) {
						attemptToUpdate = true;
					}
					break;
				case 1:
					// Want the value closest to zero
					if (Math.abs(curVal - 0) > Math.abs(newVal - 0)) {
						attemptToUpdate = true;
					}
					break;
				default:
					// Want the largest value
					if (newVal > curVal) {
						attemptToUpdate = true;
					}
			}
			if (attemptToUpdate) {
				boolean didUpdate = aia.compareAndSet(index, curVal, newVal);
				if (didUpdate) {
					LOGGER.debug("Value updated | index: {} | newVal: {}", index, newVal);
				} else {
					LOGGER.debug("compareAndSet() failed! Another thread updated reference? Will try again."
							+ " | index: {} | newVal: {}", index, newVal);
					retry = true;
				}
			} else {
				// Not a new best value for this index
			}
		} while (retry);
	}
	
	private static void updateIfAppropriateUsingCompareAndSet(
			final AtomicIntegerArray aia,
			final int newVal) {
		for (int i=0; i<aia.length(); i++) {
			updateIfAppropriateUsingCompareAndSet(aia, i, newVal);
		}
	}
	
	private static class RetryingCompareAndSetRunnable implements Runnable {
		private final Random r;
		private final AtomicIntegerArray aia;
		public RetryingCompareAndSetRunnable(
				final Random r,
				final AtomicIntegerArray aia) {
			this.r = r;
			this.aia = aia;
		}
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				int newVal = r.nextInt();
				updateIfAppropriateUsingCompareAndSet(aia, newVal);
			}
			LOGGER.debug("exiting");
		}
	}
	
	@Test
	public void testMultithreadedUpdateOfAtomicIntegerArray() throws Exception {
		// X threads will continuously generate random integer values and update an
		// AtomicIntegerArray with the lowest, closest-to-zero, and highest values
		// in array positions 0, 1 and 2 respectively.
		// The AtomicIntegerArray is updated via the 'compareAndSet()' method, so
		// sometimes it will be necessary to retry the 'compareAndSet()' call if
		// another thread has already changed the underlying value.
		AtomicIntegerArray aia = new AtomicIntegerArray(3);
		aia.set(0, Integer.MAX_VALUE);
		aia.set(1, Integer.MAX_VALUE);
		aia.set(2, Integer.MIN_VALUE);
		final int NUM_THREADS = ForkJoinPool.getCommonPoolParallelism();
		LOGGER.debug("Will use {} threads", NUM_THREADS);
		final Random r = new Random();
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS, new SimpleThreadFactory());
		for (int i=0; i<NUM_THREADS; i++) {
			executorService.submit(new RetryingCompareAndSetRunnable(r, aia));
		}
		executorService.shutdown();
		executorService.awaitTermination(4, TimeUnit.SECONDS);
		executorService.shutdownNow();
		executorService.awaitTermination(1, TimeUnit.SECONDS);
		LOGGER.debug("Final values | {} | {} | {}", aia.get(0), aia.get(1), aia.get(2));
		assertNotEquals(Integer.MAX_VALUE, aia.get(0));
		assertNotEquals(Integer.MAX_VALUE, aia.get(1));
		assertNotEquals(Integer.MIN_VALUE, aia.get(2));
	}

}


