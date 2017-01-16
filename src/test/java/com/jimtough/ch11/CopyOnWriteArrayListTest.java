package com.jimtough.ch11;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class CopyOnWriteArrayListTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CopyOnWriteArrayListTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	private static class MyRunnable implements Runnable {
		private final Random r;
		private final CopyOnWriteArrayList<String> cowal;
		public MyRunnable(final Random r, final CopyOnWriteArrayList<String> cowal) {
			this.r = r;
			this.cowal = cowal;
		}
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				boolean addString = r.nextBoolean();
				if (addString) {
					cowal.add(Thread.currentThread().getName());
				} else {
					try {
						cowal.remove(0);
					} catch (RuntimeException re) {
						LOGGER.trace("OOPS! Failed to remove first item from list.");
					}
				}
				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e) {
					break;
				}
			}
			LOGGER.debug("exiting");
		}
	}
	
	@Test
	public void testConcurrentModificationOfContainer() throws Exception {
		final int NUM_THREADS = 3;
		LOGGER.debug("Will use {} threads", NUM_THREADS);
		final Random r = new Random();
		CopyOnWriteArrayList<String> cowal = new CopyOnWriteArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS, new SimpleThreadFactory());
		for (int i=0; i<NUM_THREADS; i++) {
			executorService.submit(new MyRunnable(r, cowal));
		}
		executorService.shutdown();
		int maxLoops = 30;
		do {
			// These iterations will not be affected by the container modifications in the running threads.
			// Each iteration of the container will cause a copy to be made of the underlying array, and
			// it is the copy that is actually being iterated over.
			executorService.awaitTermination(250, TimeUnit.MILLISECONDS);
			StringBuilder sb = new StringBuilder("");
			for (String s : cowal) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(s);
			}
			LOGGER.debug("size: {} | contents: [{}]", cowal.size(), sb.toString());
			sb = null;
			maxLoops--;
		} while (!executorService.isTerminated() && maxLoops > 0);
		executorService.shutdownNow();
		LOGGER.debug("done");
	}

}
