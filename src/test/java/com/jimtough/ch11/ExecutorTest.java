package com.jimtough.ch11;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jimtough.ch11.Ch11Utils.SimpleThreadFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExecutorTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	private class CallableFactorialCalculator implements Callable<CallableFactorialCalculator> {
		private final int n;
		private BigDecimal factorial;
		public CallableFactorialCalculator(int n) {
			this.n = n;
		}
		@Override
		public CallableFactorialCalculator call() throws Exception {
			if (n <= 0) {
				throw new IllegalArgumentException("'n' must be greater than zero");
			}
			BigDecimal f = BigDecimal.ONE;
			for (int i=1; i<=n; i++) {
				f = f.multiply(BigDecimal.valueOf(i));
			}
			this.factorial = f;
			return this;
		}
	}
	
	@Test
	public void testCalculateBunchOfFactorialsInThreads() throws Exception {
		final int NUM_THREADS = ForkJoinPool.getCommonPoolParallelism();
		final int MAX_FACTORIAL = 50;
		LOGGER.debug("Will use {} threads", NUM_THREADS);
		List<Future<CallableFactorialCalculator>> cfcFutureList = new LinkedList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS, new SimpleThreadFactory());
		for (int i=1; i<=MAX_FACTORIAL; i++) {
			cfcFutureList.add(executorService.submit(new CallableFactorialCalculator(i)));
		}
		executorService.shutdown();
		TimeUnit.MILLISECONDS.sleep(1);
		while (!cfcFutureList.isEmpty()) {
			Iterator<Future<CallableFactorialCalculator>> iterator = cfcFutureList.iterator();
			while (iterator.hasNext()) {
				Future<CallableFactorialCalculator> cfcFuture = iterator.next();
				if (cfcFuture.isDone()) {
					iterator.remove();
					CallableFactorialCalculator cfc = cfcFuture.get();
					LOGGER.debug("{}! = {}", cfc.n, cfc.factorial);
				}
			}
		}
		assertTrue(executorService.isTerminated());
	}

}
