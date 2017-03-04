package com.jimtough.ch11;

import static com.jimtough.ch11.Ch11Utils.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AtomicReferenceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AtomicReferenceTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	
	//--------------------------------------------------------------------
	// AtomicReference tests (not as simple as I first thought...)
	//--------------------------------------------------------------------
	
	private static class LongAndLocalDateTime {
		final long l;
		@SuppressWarnings("unused")
		final LocalDateTime ldt;
		final String threadName;
		public LongAndLocalDateTime(long l, LocalDateTime ldt, Thread t) {
			this.l = l;
			this.ldt = ldt;
			this.threadName = t.getName();
		}
		@Override
		public String toString() {
			return Long.toString(l) + " | " + threadName;
		}
	}

	private static void updateIfAppropriateUsingCompareAndSet(
			AtomicReference<LongAndLocalDateTime> atomicReference, 
			LongAndLocalDateTime candidateNewVal) {
		// This has to be done in a while loop in case the 'compareAndSet()' fails.
		// A failure will happen if another thread updates the AtomicReference value
		// between the time when this thread called 'get()' and 'compareAndSet()'.
		boolean retry;
		do {
			retry = false;
			LongAndLocalDateTime curVal = atomicReference.get();
			if (curVal == null || curVal.l < candidateNewVal.l) {
				boolean didUpdate = atomicReference.compareAndSet(curVal, candidateNewVal);
				if (didUpdate) {
					LOGGER.debug("New high score! {} | MAX_VALUE = {} | DIFF FROM MAX: {}", 
							candidateNewVal.l, Long.MAX_VALUE, Long.MAX_VALUE - candidateNewVal.l);
					// This is a new high score and it was successfully set on the AtomicReference.
				} else {
					LOGGER.debug("compareAndSet() failed! Another thread updated reference? Will try again."
							+ " | candidateNewVal: [{}]", candidateNewVal);
					retry = true;
				}
			} else {
				// Not a new high score
			}
		} while (retry);
	}

	// This method satisfies the function specification for the AtomicReference.accumulateAndGet() method's
	// second parameter, which is a BinaryOperator function.
	//
	// NOTE: This method may be invoked more than once by the JVM. If another thread updates the AtomicReference
	//       before this method returns a new value and the update is applied, then the JVM will retry, and this
	//       method will be run again with a different value of 'curVal'.
	private static LongAndLocalDateTime apply(LongAndLocalDateTime curVal, LongAndLocalDateTime candidateVal) {
		//LOGGER.trace("curVal: [{}] | newVal: [{}]", curVal, newVal);
		if (curVal == null) {
			LOGGER.debug("AtomicReference currently has a null value | candidateVal: [{}]", candidateVal.l);
			return candidateVal;
		} else if (curVal.l < candidateVal.l) {
			LOGGER.debug("My value is bigger! | cur: [{}] | mine: [{}]", curVal.l, candidateVal.l);
			return candidateVal;
		}
		return curVal;
	}
	
	private static class RetryingCompareAndSetRunnable implements Runnable {
		private final Random r;
		private final AtomicReference<LongAndLocalDateTime> atomicReference;
		public RetryingCompareAndSetRunnable(
				final Random r,
				final AtomicReference<LongAndLocalDateTime> atomicReference) {
			this.r = r;
			this.atomicReference = atomicReference;
		}
		@Override
		public void run() {
			LOGGER.trace("AtomicReference hash code: [{}]", atomicReference.hashCode());
			while (!Thread.interrupted()) {
				LongAndLocalDateTime candidateNewVal = 
						new LongAndLocalDateTime(r.nextLong(), LocalDateTime.now(), Thread.currentThread());
				updateIfAppropriateUsingCompareAndSet(this.atomicReference, candidateNewVal);
			}
			LOGGER.debug("exiting");
		}
	}
	
	@Test
	public void testAtomicReferenceUpdatedViaCompareAndSet() throws Exception {
		final int NUM_THREADS = ForkJoinPool.getCommonPoolParallelism();
		LOGGER.debug("Will use {} threads", NUM_THREADS);
		final Random r = new Random();
		final AtomicReference<LongAndLocalDateTime> atomicReference = new AtomicReference<>();
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS, new SimpleThreadFactory());
		for (int i=0; i<NUM_THREADS; i++) {
			executorService.submit(new RetryingCompareAndSetRunnable(r, atomicReference));
		}
		executorService.shutdown();
		executorService.awaitTermination(4, TimeUnit.SECONDS);
		executorService.shutdownNow();
		executorService.awaitTermination(1, TimeUnit.SECONDS);
		LongAndLocalDateTime laldt = atomicReference.get();
		if (laldt != null) {
			LOGGER.debug("final value: [{}] -  Long.MAX_VALUE = {}", laldt, Long.MAX_VALUE);
		}
		assertNotNull(atomicReference.get());
	}
	
	private static class GetAndAccumulateRunnable implements Runnable {
		private final Random r;
		private final AtomicReference<LongAndLocalDateTime> atomicReference;
		public GetAndAccumulateRunnable(
				final Random r,
				final AtomicReference<LongAndLocalDateTime> atomicReference) {
			this.r = r;
			this.atomicReference = atomicReference;
		}
		@Override
		public void run() {
			LOGGER.trace("AtomicReference hash code: [{}]", atomicReference.hashCode());
			while (!Thread.interrupted()) {
				LongAndLocalDateTime candidateNewVal = 
						new LongAndLocalDateTime(r.nextLong(), LocalDateTime.now(), Thread.currentThread());
				//atomicReference.accumulateAndGet(candidateNewVal, AtomicReferenceTest::apply);
				LongAndLocalDateTime oldVal = atomicReference.getAndAccumulate(candidateNewVal, AtomicReferenceTest::apply);
				LongAndLocalDateTime curVal = atomicReference.get();
				if (oldVal == null || oldVal.l != curVal.l) {
					LOGGER.debug("AtomicReference value changed: [{}]", curVal);
				}
			}
			LOGGER.debug("exiting");
		}
	}
	
	@Test
	public void testAtomicReferenceUpdatedViaGetAndAccumulate() throws Exception {
		final int NUM_THREADS = ForkJoinPool.getCommonPoolParallelism();
		LOGGER.debug("Will use {} threads", NUM_THREADS);
		final Random r = new Random();
		final AtomicReference<LongAndLocalDateTime> atomicReference = new AtomicReference<>();
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS, new SimpleThreadFactory());
		for (int i=0; i<NUM_THREADS; i++) {
			executorService.submit(new GetAndAccumulateRunnable(r, atomicReference));
		}
		executorService.shutdown();
		executorService.awaitTermination(4, TimeUnit.SECONDS);
		executorService.shutdownNow();
		executorService.awaitTermination(1, TimeUnit.SECONDS);
		LongAndLocalDateTime laldt = atomicReference.get();
		if (laldt != null) {
			LOGGER.debug("final value: [{}] -  Long.MAX_VALUE = {}", laldt, Long.MAX_VALUE);
		}
		assertNotNull(atomicReference.get());
	}

}
