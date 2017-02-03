package com.jimtough.ch11;

import static com.jimtough.ch11.Ch11Utils.*;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParallelStreamsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParallelStreamsTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	private ThreadLocal<NumberFormat> createThreadLocalNumberFormat() {
		return ThreadLocal.withInitial(new Supplier<NumberFormat> () {
			@Override
			public NumberFormat get() {
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setMinimumIntegerDigits(4);
				nf.setGroupingUsed(false);
				return nf;
			}
		});
	}

	private static String doBunchOfStringReplacements(String s) {
		// I am very immature  :)
		return s.replaceAll(" s[a-z]{3,} ", " shiz ")
				.replaceAll("Buck", "Butt")
				.replaceAll(" the ", " teh ")
				.replaceAll(" he ", " she ")
				.replaceAll(" his ", " her ")
				.replaceAll(" him ", " her ")
				.replaceAll(" himself ", " herself ")
				.replaceAll(" man ", " woman ")
				.replaceAll(" men ", " women ");
	}
	
	Function<String,String> mapperFunction = new Function<String,String>() {
		@Override
		public String apply(String s) {
			return doBunchOfStringReplacements(s);
		}
	};

	private static String concatStringLinefeedString(String s1, String s2) {
		return s1 + System.lineSeparator() + s2;
	}
	
	BinaryOperator<String> stringReducer = new BinaryOperator<String>() {
		@Override
		public String apply(String s1, String s2) {
			final String lineNumberS1 = s1.trim().length() >= 5 ? s1.trim().substring(0, 5) : "???";
			final String lineNumberS2 = s2.trim().length() >= 5 ? s2.trim().substring(0, 5) : "???";
			LOGGER.debug("COMBINE | {} and {}", lineNumberS1, lineNumberS2);
			return concatStringLinefeedString(s1, s2);
		}
	};

	//--------------------------------------------------------------------

	private static boolean isPrimeNumber(long n) {
		for (long x=2; x <= n/2; x++) {
			if (n % x == 0) {
				return false;
			}
		}
		return true;
	}
	
	@Test
	public void testCountPrimesSequential() throws Exception {
		long numberOfPrimes = LongStream.rangeClosed(2, 100_000)
				.filter(ParallelStreamsTest::isPrimeNumber)
				.count();
		LOGGER.debug("Count of prime numbers below {}: {}", 100_000, numberOfPrimes);
	}
	
	@Test
	public void testCountPrimesParallel() throws Exception {
		long numberOfPrimes = LongStream.rangeClosed(2, 100_000)
				.parallel()
				.filter(ParallelStreamsTest::isPrimeNumber)
				.count();
		LOGGER.debug("Count of prime numbers below {}: {}", 100_000, numberOfPrimes);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testFilesLinesSequential() throws Exception {
		LOGGER.debug("Contents of file [{}]", BIG_SAMPLE_TEXT_FILE_PATH.toAbsolutePath());
		ThreadLocal<NumberFormat> tlnf = createThreadLocalNumberFormat();
		final AtomicInteger lineNumber = new AtomicInteger(0);
		try (Stream<String> textFileLinesStream = Files.lines(BIG_SAMPLE_TEXT_FILE_PATH)) {
			// Apply some stream operations
			textFileLinesStream
				.map(s -> tlnf.get().format(lineNumber.incrementAndGet()) + " => [" + s + "]")
				.limit(250)
				.map(ParallelStreamsTest::doBunchOfStringReplacements)
				.forEach(s -> LOGGER.debug(s));
		}
		LOGGER.debug("lineNumber final value: {}", lineNumber.get());
		assertEquals(250, lineNumber.get());
	}

	@Test
	public void testFilesLinesMixed() throws Exception {
		LOGGER.debug("Contents of file [{}]", BIG_SAMPLE_TEXT_FILE_PATH.toAbsolutePath());
		ThreadLocal<NumberFormat> tlnf = createThreadLocalNumberFormat();
		final AtomicInteger lineNumber = new AtomicInteger(0);
		try (Stream<String> textFileLinesStream = Files.lines(BIG_SAMPLE_TEXT_FILE_PATH)) {
			// Apply some stream operations
			textFileLinesStream
				.map(s -> tlnf.get().format(lineNumber.incrementAndGet()) + " => [" + s + "]")
				.limit(250)
				// This is fun, but using parallel() or not makes no difference to the
				// time for this test to complete. I can't be bothered coming up with a
				// time-consuming task to exercise the parallelism thing.
				.parallel()
				.map(ParallelStreamsTest::doBunchOfStringReplacements)
				.sequential()
				// NOTE: If you remove the 'sequential()' that comes before the 'sorted()'
				//       call, then you'll end up with a mess at the end. I suppose each
				//       parallel stream is sorting its own subset of the elements from
				//       the original stream and sending them to the 'forEach()' in an
				//       unpredictable order from the various parallel threads.
				.sorted()
				.forEach(s -> LOGGER.debug(s));
		}
		LOGGER.debug("lineNumber final value: {}", lineNumber.get());
		assertEquals(250, lineNumber.get());
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testBadReduceThatDependsOnGlobalState() throws Exception {
		
		// IMPORTANT! This is the WRONG WAY to implement a reduce() operation!
		
		// I don't understand why yet, but using the 'lineNumber' AtomicInteger in the
		// intermediate stream operations leaves you with strange results at the end.
		// The lines appear to be in a sane order at the end, but the lines numbers at
		// the start of each line are in a nonsense order.
		
		LOGGER.debug("Contents of file [{}]", BIG_SAMPLE_TEXT_FILE_PATH.toAbsolutePath());
		ThreadLocal<NumberFormat> tlnf = createThreadLocalNumberFormat();
		final AtomicInteger lineNumber = new AtomicInteger(0);
		Optional<String> concatenatedString;
		try (Stream<String> textFileLinesStream = Files.lines(BIG_SAMPLE_TEXT_FILE_PATH)) {
			LOGGER.debug("isParallel?: {}", textFileLinesStream.isParallel());
			// Apply some stream operations
			concatenatedString = textFileLinesStream
				.sequential()
				.map(s -> tlnf.get().format(lineNumber.incrementAndGet()) + " => [" + s + "]")
				.limit(500)
				.parallel()
				.map(ParallelStreamsTest::doBunchOfStringReplacements)
				.reduce((s1,s2) -> s1 + System.lineSeparator() + s2);
		}
		LOGGER.debug(System.lineSeparator() + concatenatedString.get());
		LOGGER.debug("lineNumber final value: {}", lineNumber.get());
	}
	
	@Test
	public void testReduce_A() throws Exception {
		LOGGER.debug("Contents of file [{}]", BIG_SAMPLE_TEXT_FILE_PATH.toAbsolutePath());
		ThreadLocal<NumberFormat> tlnf = createThreadLocalNumberFormat();
		final AtomicInteger lineNumber = new AtomicInteger(0);
		List<String> numberedLinesList = new ArrayList<>();
		
		// Prefix each line with its line number first in a sequential stream.
		// Save the results in a list that we will process in parallel below.
		
		try (Stream<String> textFileLinesStream = Files.lines(BIG_SAMPLE_TEXT_FILE_PATH)) {
			assertFalse(textFileLinesStream.isParallel());
			textFileLinesStream
					.map(s -> tlnf.get().format(lineNumber.incrementAndGet()) + " => [" + s + "]")
					.limit(500)
					.forEach(s -> numberedLinesList.add(s));
		}

		// Process the list as a parallel stream, with no external global state
		// or dependencies. Seems to work fine.
		
		Optional<String> concatenatedString;
		try (Stream<String> numberedLinesStream = numberedLinesList.parallelStream()) {
			assertTrue(numberedLinesStream.isParallel());
			concatenatedString = numberedLinesStream
				.map(ParallelStreamsTest::doBunchOfStringReplacements)
				.reduce((s1,s2) -> s1 + System.lineSeparator() + s2);
				//.reduce(stringReducer);
				//.reduce(ParallelStreamsTest::concatStringLinefeedString);
		}
		LOGGER.debug(System.lineSeparator() + concatenatedString.get());
	}
	
	@Test
	public void testReduce_B() throws Exception {
		Optional<String> concatenatedString;
		try (Stream<String> ss = Stream.of(A,B,C,D,E,F,G).parallel()) {
			concatenatedString = ss.reduce((s1,s2) -> s1 + " " + s2);
		}
		LOGGER.debug(concatenatedString.get());
		assertTrue(concatenatedString.get().startsWith(A));
		assertTrue(concatenatedString.get().endsWith(G));
	}

	//--------------------------------------------------------------------

	Supplier<List<String>> supplier = new Supplier<List<String>>() {
		@Override public List<String> get() {
			return new ArrayList<String>();
		}
	};
    BiConsumer<List<String>, String> accumulator = new BiConsumer<List<String>, String>() {
		@Override public synchronized void accept(List<String> t, String u) {
			t.add(u);
		}
    };
    BiConsumer<List<String>,List<String>> combiner = new BiConsumer<List<String>,List<String>>() {
		@Override public synchronized void accept(List<String> t, List<String> u) {
			t.addAll(u);
		}
    };
    
	@Test
	public void testCollect_A() throws Exception {
		List<String> stringList;
		try (Stream<String> ss = Stream.of(A,B,C,D,E,F,G).parallel()) {
			stringList = ss.collect(supplier, accumulator, combiner);
		}
		LOGGER.debug(stringList.toString());
		assertEquals(7, stringList.size());
		assertEquals(A, stringList.get(0));
		assertEquals(G, stringList.get(6));
	}
    
	@Test
	public void testCollect_B() throws Exception {
		List<String> stringList;
		try (Stream<String> ss = Stream.of(A,B,C,D,E,F,G).parallel()) {
			stringList = ss.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		}
		LOGGER.debug(stringList.toString());
		assertEquals(7, stringList.size());
		assertEquals(A, stringList.get(0));
		assertEquals(G, stringList.get(6));
	}
    
	@Test
	public void testCollect_C() throws Exception {
		List<String> stringList;
		try (Stream<String> ss = Files.lines(BIG_SAMPLE_TEXT_FILE_PATH).parallel()) {
			stringList = ss.limit(250).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		}
		LOGGER.debug(stringList.get(0));
		LOGGER.debug(stringList.get(249));
		assertEquals(250, stringList.size());
		assertTrue(stringList.get(0).contains("Call"));
		assertTrue(stringList.get(249).contains("ferocity"));
	}
	
}
