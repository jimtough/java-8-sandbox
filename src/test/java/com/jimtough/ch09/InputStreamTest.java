package com.jimtough.ch09;

import static com.jimtough.ch09.Ch09Utils.*;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InputStreamTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(InputStreamTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------

	private boolean startsWithJavaClassFileMagicNumber(Path filePath) throws IOException {
		// 4 bytes that appear at the very start of every Java .class file
		byte[] javaClassFileMagicNumber = {(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE};
		try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
			// Use a byte array as a buffer to read the first 4 bytes
			byte[] buffer = new byte[4];
			if (fis.read(buffer) != -1) {
				if (Arrays.equals(javaClassFileMagicNumber, buffer)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Test
	public void testReadJavaClassFile() throws Exception {
		assertTrue(Files.exists(TARGET_DIR));
		assertTrue(Files.isDirectory(TARGET_DIR));
		Path classFileDirPath = TARGET_DIR.resolve("test-classes/com/jimtough/ch09");
		// Path to compiled version of this class
		Path classFilePath = classFileDirPath.resolve("ByteStreamTest.class");
		assertTrue(Files.exists(classFilePath));

		if (startsWithJavaClassFileMagicNumber(classFilePath)) {
			LOGGER.debug("File [{}] starts with Java class file magic number", classFilePath);
		} else {
			LOGGER.debug("File [{}] does not appear to be a Java class file", classFilePath);
		}
		
		if (startsWithJavaClassFileMagicNumber(BIG_SAMPLE_TEXT_FILE_PATH_A)) {
			LOGGER.debug("File [{}] starts with Java class file magic number", BIG_SAMPLE_TEXT_FILE_PATH_A);
		} else {
			LOGGER.debug("File [{}] does not appear to be a Java class file", BIG_SAMPLE_TEXT_FILE_PATH_A);
		}
	}

}
