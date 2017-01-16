package com.jimtough.ch11;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
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
public class CyclicBarrierTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CyclicBarrierTest.class);
	
	@Rule
	public TestName testName = new TestName();
	@Rule
	public Timeout globalTimeout = new Timeout(20, TimeUnit.SECONDS);
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	private static class LionRunnable implements Runnable {
		private final CyclicBarrier cb;
		public LionRunnable(CyclicBarrier cb) {
			this.cb = cb;
		}
		@Override
		public void run() {
			Random r = new Random();
			LOGGER.debug("Getting my lion ready...");
			try {
				TimeUnit.MILLISECONDS.sleep(r.nextInt(3000));
				LOGGER.debug("Ready to join the other lions!");
				cb.await();
				LOGGER.debug("COMBINE TO FORM...");
			} catch (Exception e) {
				LOGGER.error("An exception happened!", e);
				return;
			}
		}
	}
	
	@Test
	public void testJoinLionsToFormVoltron() throws Exception {
		CyclicBarrier cb = new CyclicBarrier(5);
		Thread t1 = new Thread(new LionRunnable(cb), "Black Lion");
		Thread t2 = new Thread(new LionRunnable(cb), "Red Lion");
		Thread t3 = new Thread(new LionRunnable(cb), "Blue Lion");
		Thread t4 = new Thread(new LionRunnable(cb), "Green Lion");
		Thread t5 = new Thread(new LionRunnable(cb), "Yellow Lion");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		LOGGER.debug("VOLTRON!");
		assertFalse(cb.isBroken());
	}

}
