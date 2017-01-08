package com.jimtough.ch09;

import static com.jimtough.ch09.Ch09Utils.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileReaderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileReaderTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testFileReaderReadsTextFileOneCharAtATime() throws Exception {
		StringBuilder sb = new StringBuilder();
		try (FileReader fr = new FileReader(BIG_SAMPLE_TEXT_FILE_PATH_A.toString())) {
			int ch = 0;
			while ((ch = fr.read()) != -1) {
				//System.out.print(((char)ch));
				sb.append((char)ch);
			}
		}
		assertNotEquals(0, sb.length());
		// The try-with-resources will auto-close the FileReader object.
		// Now all the contents of the file are in the StringBuilder object.
		// Write everything out to the log.
		LOGGER.trace(sb.toString());
	}
	
	@Test
	public void testFileReaderReadsTextFileAndWritesToFileWriter() throws Exception {
		assertTrue(Files.exists(TARGET_DIR));
		assertTrue(Files.isDirectory(TARGET_DIR));
		Path fileCopyPath = TARGET_DIR.resolve("copy-of-" + BIG_SAMPLE_TEXT_FILE_PATH_A.getFileName().toString());
		Files.deleteIfExists(fileCopyPath);
		
		try (
				FileReader fr = new FileReader(BIG_SAMPLE_TEXT_FILE_PATH_A.toString());
				BufferedReader br = new BufferedReader(fr);
				FileWriter fw = new FileWriter(fileCopyPath.toString());
				BufferedWriter bw = new BufferedWriter(fw);) {
			int ch = 0;
			while ((ch = br.read()) != -1) {
				bw.write(ch);
			}
		}
		
		assertNotEquals(0, Files.size(fileCopyPath));
		assertEquals(Files.size(BIG_SAMPLE_TEXT_FILE_PATH_A), Files.size(fileCopyPath));
	}

}
