package com.jimtough.ch02;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Implements a singleton using the "initialization on demand holder" idiom, per the example
 * on Page 39 of the book. It exploits the fact that the JVM does not load inner classes until
 * they are referenced.</p>
 * 
 * <p>This singleton contains a counter, but doesn't do much else.</p>
 * 
 * @author JTOUGH
 */
public class MySingleton {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySingleton.class);
	
	/**
	 * The static inner class that holds the singleton reference
	 */
	public static class SingletonHolder {
		private static final Logger HOLDER_LOGGER = LoggerFactory.getLogger(SingletonHolder.class);
		static {
			HOLDER_LOGGER.debug("Static inner 'holder' class has been loaded");
		}
		public static final MySingleton THE_SINGLETON = new MySingleton();
	}
	
	private final AtomicInteger counter = new AtomicInteger(0);
	
	private MySingleton() {} // prevent instantiation from outside this class

	/**
	 * Gets a reference to the one and only instance of this class
	 * @return Non-null reference to the singleton
	 */
	public static MySingleton getInstance() {
		LOGGER.debug("getInstance() | INVOKED");
		return SingletonHolder.THE_SINGLETON;
	}

	/**
	 * This method isn't part of the singleton pattern. My singleton class needs to do something! This is it. :)
	 * @return New counter value after incrementing the current value by 1
	 */
	public int incrementAndGet() {
		LOGGER.debug("incrementAndGet() | INVOKED");
		return counter.incrementAndGet();
	}
	
}
