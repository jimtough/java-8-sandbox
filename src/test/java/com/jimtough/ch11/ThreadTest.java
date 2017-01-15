package com.jimtough.ch11;

import static com.jimtough.ch11.Ch11Utils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
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
public class ThreadTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadTest.class);
	
	@Rule
	public TestName testName = new TestName();
	@Rule
	public Timeout globalTimeout = new Timeout(20, TimeUnit.SECONDS);
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testExtendThreadThenStartAndJoin() throws Exception {
		class CustomThread extends Thread {
			@Override
			public void run() {
				LOGGER.debug("I am {}", Thread.currentThread().getName());
			}
		}
		Thread t = new CustomThread();
		t.setName("Groot");
		
		LOGGER.debug("I am {}", Thread.currentThread().getName());
		t.start();
		t.join();
	}
	
	@Test
	public void testCreateThreadWithRunnable() throws Exception {
		class MyRunnable implements Runnable {
			@Override
			public void run() {
				LOGGER.debug("I am {}", Thread.currentThread().getName());
			}
		}
		Thread t = new Thread(new MyRunnable(), "BabyGroot");
		
		LOGGER.debug("I am {}", Thread.currentThread().getName());
		t.start();
		t.join();
	}
	
	@Test
	public void testCreateMultipleThreads() throws Exception {
		class MyRunnable implements Runnable {
			@Override
			public void run() {
				for (int i=0; i<10; i++) {
					LOGGER.debug("I am {}", Thread.currentThread().getName());
				}
			}
		}
		Thread t1 = new Thread(new MyRunnable(), "Alpha");
		Thread t2 = new Thread(new MyRunnable(), "Baker");
		
		LOGGER.debug("I am {}", Thread.currentThread().getName());
		t1.start();
		t2.start();
		t1.join();
		t2.join();
	}

	@Test
	public void testCreateMultipleThreadsWithDifferentPriorities() throws Exception {
		class MyRunnable implements Runnable {
			@Override
			public void run() {
				doCpuIntensiveOperation(1_000_000);
				LOGGER.debug("I am {}, and I have finished", Thread.currentThread().getName());
			}
		}
		final int NUMBER_OF_THREADS = 32;
		List<Thread> threadList = new ArrayList<Thread>(NUMBER_OF_THREADS);
		for (int i=1; i<=NUMBER_OF_THREADS; i++) {
			Thread t = new Thread(new MyRunnable());
			if (i == NUMBER_OF_THREADS) {
				// Last thread gets MAX_PRIORITY
				t.setPriority(Thread.MAX_PRIORITY);
				t.setName("T-" + i + "-MAX_PRIORITY");
			} else {
				// All other threads get MIN_PRIORITY
				t.setPriority(Thread.MIN_PRIORITY);
				t.setName("T-" + i);
			}
			threadList.add(t);
		}
		
		threadList.forEach(t->t.start());
		// Can't use 'forEach()' here because join() throws a checked exception
		for (Thread t : threadList) {
			t.join();
			assertFalse(t.isAlive());
		}
	}
	
}


