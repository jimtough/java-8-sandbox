package com.jimtough.ch09;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// These tests need to be run manually (MT = manual test) since they require user input to console
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MT_StandardStreams {

	private static final Logger LOGGER = LoggerFactory.getLogger(MT_StandardStreams.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testReadSingleCharacterFromSystemIn() throws Exception {
		// System.out is a PrintStream
		System.out.print("Type a character: ");
		int val = -1;
		try {
			// reads one character from System.in as an int value
			val = System.in.read();
		} catch (IOException ioe) {
			LOGGER.error("Cannot read input", ioe);
			throw ioe;
		}
		LOGGER.debug("You typed: [{}] | int value: {}", (char)val, val);
		assertNotEquals(-1, val);
	}
	
	@Test
	public void testReassignSystemOutToTextFile() throws Exception {
		assertTrue("This test can only be run if the 'target' subdir exists", Files.exists(Paths.get("target")));
		final PrintStream originalSystemOut = System.out;
		try (PrintStream ps = new PrintStream("target/ch09-log.txt")) {
			System.setOut(ps);
			System.out.println("Test output to System.out");
			System.out.println("Current time is: " + LocalDateTime.now().toString());
		} finally {
			System.setOut(originalSystemOut);
		}
		System.out.println("Back to console?");
	}

}
