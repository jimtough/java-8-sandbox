package com.jimtough.ch04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Experiment with several different ways of creating a Stream
 * 
 * @author JTOUGH
 */
public class StreamSourcesTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamSourcesTest.class);
	
	private static final String A = "alpha";
	private static final String B = "bravo";
	private static final String C = "charlie";
	private static final String D = "delta";
	private static final String E = "echo";
	private static final String F = "foxtrot";
	private static final String G = "golf";
    
	private List<String> stringList;
	
	@Before
	public void setUp() {
		stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
	}
	
	@Test
	public void testRandomIntStream() {
		// Initially creates an infinite Stream of random integers
		// between 0 and 9, but uses the 'limit()' intermediate operation
		// to stop the stream output after 15 values
		(new Random()).ints(0,10).limit(15).forEach(i -> LOGGER.debug("rando: {}", i));
	}
	
	@Test
	public void testRangeIntStream() {
		// Create a stream of sequential int values from 1 to 10
		IntStream.rangeClosed(1, 10).forEach(i -> LOGGER.debug("seq: {}", i));
	}
	
	@Test
	public void testIterateLongStream() {
		// Create an infinite stream of int values, starting from 1, and multiplying
		// by 2 on each iteration.
		// Uses the 'limit()' intermediate operation to stop the stream output after 15 values.
		LongStream.iterate(1, i -> i*2).limit(15).forEach(i -> LOGGER.debug("seq: {}", i));
	}
	
	@Test
	public void testRangeLongStream() {
		// Create a stream of sequential long values from 1 to 10
		LongStream.rangeClosed(1, 10).forEach(i -> LOGGER.debug("seq: {}", i));
	}
	
	@Test
	public void testExplicitValuesDoubleStream() {
		// Create a stream of double values that contains exactly the values
		// that are supplied to the 'of()' method
		DoubleStream.of(11.11, 22.22, 33.33).forEach(i -> LOGGER.debug("value: {}", i));
	}
	
	@Test
	public void testExplicitStringValuesStreamWithArrays() {
		// Create a stream of string values that contains exactly the values
		// that are supplied to the 'of()' method
		String[] stringArray = {A,B,C,D,E,F,G};
		Arrays.stream(stringArray).forEach(s -> LOGGER.debug("value: {}", s));
	}
	
	@Test
	public void testExplicitStringValuesStreamWithOfFactoryMethod() {
		// Create a stream of string values that contains exactly the values
		// that are supplied to the 'of()' method
		Stream.of(A,B,C,D,E,F,G).forEach(s -> LOGGER.debug("value: {}", s));
	}
	
	@Test
	public void testExplicitStringValuesStreamWithBuilder() {
		// Create a stream of string values that contains exactly the values
		// that are supplied to the 'of()' method
		Stream.Builder<String> builder = Stream.builder();
		Stream<String> stream = builder
			.add(A)
			.add(B)
			.add(C)
			.add(D)
			.add(E)
			.add(F)
			.add(G)
			.build();
		stream.forEach(s -> LOGGER.debug("value: {}", s));
	}
	
	@Test
	public void testExplicitObjectValuesStreamWithOf() {
		// Create a stream of Object instances that contains exactly the values
		// that are supplied to the 'of()' method
		Stream.of(new Date(), new StringBuilder("sb 4 me"), new Object()).forEach(i -> LOGGER.debug("value: {}", i));
	}
	
	@Test
	public void testStreamGenerate() {
		Supplier<UUID> uuidSupplier = new Supplier<UUID>() {
			@Override
			public UUID get() {
				return UUID.randomUUID();
			}
		};
		// Creates an infinite stream of random UUID objects via a Supplier instance,
		// but uses the 'limit()' intermediate operation to stop the stream output after 10 values
		Stream.generate(uuidSupplier).limit(10).forEach(i -> LOGGER.debug("value: {}", i));
	}
	
	@Test
	public void testFilesLines() throws IOException {
		// Create a stream of strings, where each string represents one line from
		// the specified text file
		Files.lines(Paths.get("./src/test/resources/The Call of the Wild.txt"))
				.limit(15)
				.forEach(line -> LOGGER.debug(line));
	}
	
	@Test
	public void testPatternSplitAsStream() {
		Pattern.compile(" ").splitAsStream("java 8 is fun").forEach(token -> LOGGER.debug(token));
	}
	
	@Test
	public void testStringAsCharsStream() {
		"This is a string.".chars().forEach(chr -> LOGGER.debug(Character.valueOf((char)chr).toString()));
	}
	
}
