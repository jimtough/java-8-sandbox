package com.jimtough.ch11;

import static org.junit.Assert.*;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// NOTE TO SELF: Never use ForkJoinPool outside of a certification test
public class ForkJoinTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForkJoinTest.class);
    
	private static final long N = 500_000_000;
	private static final int NUM_THREADS = ForkJoinPool.getCommonPoolParallelism();
	
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------

	private long calculateSumOfNUsingFormula(final long n) {
		return (n * (n+1)) / 2;
	}
	
	@Test
	public void testSumOfNUsingFormula() throws Exception {
		long sum = calculateSumOfNUsingFormula(N);
		LOGGER.debug("Sum of first {} integers is: {}", N, sum);
	}
	
	@Test
	public void testSumOfNSequentially() throws Exception {
		long sum = 0;
		for (long x=1; x<=N; x++) {
			sum += x;
		}
		LOGGER.debug("Sum of first {} integers is: {}", N, sum);
		assertEquals(calculateSumOfNUsingFormula(N), sum);
	}

	//--------------------------------------------------------------------
	
	private static class RecursiveSumOfN extends RecursiveTask<Long> {
		
		private static final long serialVersionUID = 1L;
		
		private final long from, to;
		public RecursiveSumOfN(long from, long to) {
			this.from = from;
			this.to = to;
		}
		@Override
		protected Long compute() {
			if ((to - from) <= N/NUM_THREADS) {
				long localSum = 0;
				for (long x=from; x<=to; x++) {
					localSum += x;
				}
				return localSum;
			} else {
				// Range is too large. Need to fork into two smaller tasks.
				long mid = (from + to) / 2;
				RecursiveSumOfN firstHalf = new RecursiveSumOfN(from, mid);
				firstHalf.fork();
				RecursiveSumOfN secondHalf = new RecursiveSumOfN(mid+1, to);
				long resultOfSecondHalf = secondHalf.compute();
				
				return firstHalf.join() + resultOfSecondHalf;
			}
		}
	}
	
	@Test
	public void testSumOfNWithForkJoin() throws Exception {
		LOGGER.debug("Will use {} threads", NUM_THREADS);
		ForkJoinPool fjPool = new ForkJoinPool(NUM_THREADS);
		
		long sum = fjPool.invoke(new RecursiveSumOfN(0, N));
		LOGGER.debug("Sum of first {} integers is: {}", N, sum);
		assertEquals(calculateSumOfNUsingFormula(N), sum);
	}
	
}
