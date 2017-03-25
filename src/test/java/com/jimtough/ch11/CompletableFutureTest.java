package com.jimtough.ch11;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The book doesn't mention CompletableFuture at all. I found out about this little gem from a blog post.
 * It looked so useful that I just had to experiment with it!
 * 
 * @author JTOUGH
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompletableFutureTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompletableFutureTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	private ExecutorService executor;
	private AtomicBoolean successFlag;
	private Random r;
	
	@Before
	public void setUp() {
		executor = Executors.newCachedThreadPool();
		successFlag = new AtomicBoolean(false);
		r = new Random();
	}
    
	@After
	public void tearDown() {
		executor.shutdown();
	}
	
	//--------------------------------------------------------------------

	private boolean logMessage() {
		LOGGER.debug("I have been invoked");
		return true;
	}

	private void myAcceptMethod(boolean returnValue) {
		LOGGER.debug("Return value is {}", returnValue);
		successFlag.set(returnValue);
	}
	
	@Test
	public void testCompletableFuture_A1_Simple() throws Exception {
		// This version executes the "Supplier" (the logMessage() method) in the common ForkJoinPool
		CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(this::logMessage);
		assertTrue(cf.join());
	}
	
	@Test
	public void testCompletableFuture_A2_Simple() throws Exception {
		// This version executes the "Supplier" (the logMessage() method) in the specified Executor
		CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(this::logMessage, executor);
		assertTrue(cf.join());
	}
	
	@Test
	public void testCompletableFuture_B1_WithConsumerCallback() throws Exception {
		// Runs the task in the common ForkJoinPool and then allows a callback method to verify the returned value
		CompletableFuture.supplyAsync(this::logMessage).thenAccept(this::myAcceptMethod);
		while (!successFlag.get()) {
			TimeUnit.MILLISECONDS.sleep(5);
		}
	}
	
	@Test
	public void testCompletableFuture_B2_WithConsumerCallback() throws Exception {
		// Runs the task in a Executor and then allows a callback method to verify the returned value
		CompletableFuture.supplyAsync(this::logMessage, executor).thenAccept(this::myAcceptMethod);
		while (!successFlag.get()) {
			TimeUnit.MILLISECONDS.sleep(5);
		}
	}

	//--------------------------------------------------------------------

	private static class MyContainer {
		private final LocalDateTime startTime;
		private int sleepTimeInMilliseconds;
		private LocalDateTime finishTime;
		private MyContainer(LocalDateTime startTime) {
			this.startTime = startTime;
		}
	}
	
	private MyContainer createNewContainer() {
		LOGGER.debug("createNewContainer() | INVOKED");
		return new MyContainer(LocalDateTime.now());
	}

	private MyContainer callbackOne(MyContainer mc) {
		try {
			int sleepTime = r.nextInt(4001);
			if (sleepTime % 2 == 0) {
				throw new RuntimeException("Even sleep time numbers are rejected by callbackOne(): " + sleepTime);
			}
			mc.sleepTimeInMilliseconds = sleepTime;
			LOGGER.debug("callbackOne() | Sleeping for {} ms", sleepTime);
			TimeUnit.MILLISECONDS.sleep(sleepTime);
			return mc;
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted during sleep");
		}
	}

	private MyContainer callbackTwo(MyContainer mc) {
		mc.finishTime = LocalDateTime.now();
		LOGGER.debug("callbackTwo() | Setting finish time: [{}]", mc.finishTime);
		return mc;
	}
	
	private void myAcceptMethodC(MyContainer mc) {
		LOGGER.debug("myAcceptMethodC() | start: {} | sleep: {} | finish: {}", 
				mc.startTime, mc.sleepTimeInMilliseconds, mc.finishTime);
		successFlag.set(true);
	}

	private Void exceptionHandler(Throwable t) {
		LOGGER.debug("exceptionHandler() | An exception occurred | {}", t.getMessage());
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testCompletableFuture_C_WithMultipleCallbacksAndExceptionally() throws Exception {
		final int numberOfInstances = 10;
		CompletableFuture[] cfArray = new CompletableFuture[numberOfInstances];
		for (int i=0; i<numberOfInstances; i++) {
			cfArray[i] = CompletableFuture.supplyAsync(this::createNewContainer, executor)
				// Invoke 'callbackOne()' first
				.thenApply(this::callbackOne)
				// Invoke 'callbackTwo()' second
				.thenApply(this::callbackTwo)
				// Invoke 'myAcceptMethodC()' last
				.thenAccept(this::myAcceptMethodC)
				// Invoke 'exceptionHandler()' if any of the three callbacks above throw an exception
				.exceptionally(this::exceptionHandler);
		}
		for (CompletableFuture<?> cf : cfArray) {
			cf.get();
			assertTrue(cf.isDone());
		}
	}
	
	//--------------------------------------------------------------------
	
}
