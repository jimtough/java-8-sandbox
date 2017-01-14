package com.jimtough.ch10;

import static com.jimtough.ch10.Ch10Utils.*;
import static org.junit.Assert.*;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
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
public class StreamApiWithFilesAndDirsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamApiWithFilesAndDirsTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	// Files.list() will list contents of single directory and will NOT
	// consider contents of subdirectories
	
	@Test
	public void testFilesList_A() throws Exception {
		// NOTE: The Stream<Path> returned by the Files.list() method is an auto-closable resource
		//       that must be closed, so it is created with a try-with-resources statement
		try (Stream<Path> pathStream = Files.list(TEST_RESOURCES_DIR)) {
			pathStream.forEach(p -> LOGGER.debug("p: [{}]", p));
		}
	}
	
	@Test
	public void testFilesList_B() throws Exception {
		// NOTE: The Stream<Path> returned by the Files.list() method is an auto-closable resource
		//       that must be closed, so it is created with a try-with-resources statement
		try (Stream<Path> pathStream = Files.list(TARGET_DIR)) {
			// Apply some stream operations
			pathStream
				.map(p->p.toAbsolutePath())
				.sorted()
				.forEach(p -> LOGGER.debug("p: [{}->{}]", Files.isDirectory(p)?" DIR":"FILE", p));
		}
	}

	//--------------------------------------------------------------------
	// Files.walk() will recursively walk subdirectories
	
	@Test
	public void testFilesWalk_A() throws Exception {
		try (Stream<Path> pathStream = Files.walk(TARGET_DIR)) {
			// Apply some stream operations
			pathStream
				.map(p->p.toAbsolutePath())
				.sorted()
				.forEach(p -> LOGGER.debug("p: [{}->{}]", Files.isDirectory(p)?" DIR":"FILE", p));
		}
	}
	
	@Test
	public void testFilesWalk_B() throws Exception {
		try (Stream<Path> pathStream = Files.walk(TARGET_DIR)) {
			long count = pathStream.count();
			LOGGER.debug("There are {} files/dirs in the directory tree that starts at [{}]", count, TARGET_DIR.toAbsolutePath());
			assertNotEquals(0L, count);
		}
	}

	//--------------------------------------------------------------------

	@Test
	public void testFilesFind() throws Exception {
		// BiPredicate that is true only for paths to XML files
		BiPredicate<Path, BasicFileAttributes> biPred = 
				(p,bfa) -> bfa.isRegularFile() && p.getFileName().toString().endsWith(".xml");
		try (Stream<Path> pathStream = Files.find(TARGET_DIR, Integer.MAX_VALUE, biPred, FileVisitOption.FOLLOW_LINKS)){
			// Apply some stream operations
			pathStream
				.sorted()
				.forEach(p -> LOGGER.debug("p: [{}]", p));
		}
	}

	//--------------------------------------------------------------------

	@Test
	public void testFilesLines() throws Exception {
		LOGGER.debug("Contents of file [{}]", BIG_SAMPLE_TEXT_FILE_PATH.toAbsolutePath());
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(4);
		nf.setGroupingUsed(false);
		final AtomicInteger lineNumber = new AtomicInteger(0);
		try (Stream<String> textFileLinesStream = Files.lines(BIG_SAMPLE_TEXT_FILE_PATH)) {
			// Apply some stream operations
			textFileLinesStream
				.map(s -> nf.format(lineNumber.incrementAndGet()) + " => [" + s + "]")
				.filter(s -> lineNumber.get() <= 250)
				.forEach(s -> LOGGER.debug(s));
		}
	}
	
}
