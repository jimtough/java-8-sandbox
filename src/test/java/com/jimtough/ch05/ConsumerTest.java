package com.jimtough.ch05;

import static com.jimtough.ch05.Ch05Utils.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Play with the 'consumer' family of built-in functional interfaces in Java 8
 * 
 * @author JTOUGH
 */
public class ConsumerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTest.class);
    
	private List<String> stringList;
    
	@Before
	public void setUp() {
		stringList = newAlphabeticalStringList();
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testConsumerExplicitWithoutLambda() {
		// Define a Consumer implementation the old pre-lambda way, as an anonymous class
		Consumer<String> consumer = new Consumer<String>() {
			@Override
			public void accept(String s) {
				LOGGER.debug(s);
			}
		};
		
		stringList.stream().forEach(consumer);
	}

	@Test
	public void testConsumerExplicitUsingLambda() {
		// Define a Consumer implementation with a lambda expression, then pass the reference to forEach()
		Consumer<String> consumer = s -> LOGGER.debug(s);
		
		stringList.stream().forEach(consumer);
	}

	@Test
	public void testConsumerInlineUsingLambda() {
		// Define a Consumer implementation inline as a lambda expression when calling forEach()
		stringList.stream().forEach(s -> LOGGER.debug(s));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testIntConsumerExplicitWithoutLambda() {
		// Define a IntConsumer implementation the old pre-lambda way, as an anonymous class
		final AtomicInteger atomicInteger = new AtomicInteger(0);
		IntConsumer consumer = new IntConsumer() {
			@Override
			public void accept(int i) {
				atomicInteger.addAndGet(i);
			}
		};
		
		IntStream.rangeClosed(1, 4).forEach(consumer);

		assertEquals(10, atomicInteger.get());
	}
	
	@Test
	public void testIntConsumerExplicitUsingLambda() {
		// Define an IntConsumer implementation with a lambda expression, then pass the reference to forEach()
		final AtomicInteger atomicInteger = new AtomicInteger(0);
		IntConsumer consumer = i -> atomicInteger.addAndGet(i);
		
		IntStream.rangeClosed(1, 4).forEach(consumer);

		assertEquals(10, atomicInteger.get());
	}
	
	@Test
	public void testIntConsumerInlineUsingLambda() {
		// Define an IntConsumer implementation inline as a lambda expression when calling forEach()
		final AtomicInteger atomicInteger = new AtomicInteger(0);
		
		IntStream.rangeClosed(1, 4).forEach(i -> atomicInteger.addAndGet(i));

		assertEquals(10, atomicInteger.get());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testLongConsumerExplicitUsingLambda() {
		// Define an LongConsumer implementation with a lambda expression, then pass the reference to forEach()
		final AtomicLong atomicLong = new AtomicLong(0);
		LongConsumer consumer = l -> atomicLong.addAndGet(l);
		
		LongStream.rangeClosed(1, 4).forEach(consumer);

		assertEquals(10L, atomicLong.get());
	}

	//--------------------------------------------------------------------

	// There is also a DoubleConsumer interface, but I won't bother creating yet another example for it...

	//--------------------------------------------------------------------
	
	@Test
	public void testObjIntConsumerExplicitWithoutLambda() {
		// Define a ObjIntConsumer implementation the old pre-lambda way, as an anonymous class
		ObjIntConsumer<String> objIntConsumer = new ObjIntConsumer<String>() {
			@Override
			public void accept(String s, int i) {
				LOGGER.debug("{}|{}",i,s);
			}
		};

		for (int i=0; i<stringList.size(); i++) {
			objIntConsumer.accept(stringList.get(i), i);
		}
	}
	
	@Test
	public void testObjIntConsumerExplicitUsingLambda() {
		// Define a ObjIntConsumer implementation with a lambda expression
		ObjIntConsumer<String> objIntConsumer = (s,i) -> LOGGER.debug("{}|{}",i,s);

		for (int i=0; i<stringList.size(); i++) {
			objIntConsumer.accept(stringList.get(i), i);
		}
	}

	//--------------------------------------------------------------------

	// There is also a ObjLongConsumer interface, but I won't bother creating yet another example for it...
	// There is also a ObjDoubleConsumer interface, but I won't bother creating yet another example for it...

	//--------------------------------------------------------------------
	
	@Test
	public void testBiConsumerExplicitWithoutLambda() {
		// Define a BiConsumer implementation the old pre-lambda way, as an anonymous class
		BiConsumer<Integer,String> biConsumer = new BiConsumer<Integer,String>() {
			@Override
			public void accept(Integer i, String s) {
				LOGGER.debug("{}|{}",i,s);
			}
		};
		
		for (int i=0; i<stringList.size(); i++) {
			biConsumer.accept(i, stringList.get(i));
		}
	}
	
	@Test
	public void testBiConsumerExplicitUsingLambda() {
		// Define a BiConsumer implementation with a lambda expression
		BiConsumer<Integer,String> biConsumer = (i,s) -> LOGGER.debug("{}|{}",i,s);
		
		for (int i=0; i<stringList.size(); i++) {
			biConsumer.accept(i, stringList.get(i));
		}
	}
	
}
