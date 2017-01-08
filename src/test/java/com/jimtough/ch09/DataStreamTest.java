package com.jimtough.ch09;

import static com.jimtough.ch09.Ch09Utils.*;
import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class DataStreamTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataStreamTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------

	private boolean startsWithJavaClassFileMagicNumber(Path filePath) throws IOException {
		// 4 bytes that appear at the very start of every Java .class file
		int javaClassFileMagicNumber = 0xCAFEBABE;
		try (
				FileInputStream fis = new FileInputStream(filePath.toFile());
				BufferedInputStream bis = new BufferedInputStream(fis);
				DataInputStream dis = new DataInputStream(bis)) {
			int firstIntOfDataStream = dis.readInt();
			if (firstIntOfDataStream == javaClassFileMagicNumber) {
				return true;
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
		Path classFilePath = classFileDirPath.resolve(DataStreamTest.class.getSimpleName() + ".class");
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
	
	@Test
	public void testWritePrimitiveValuesToDataStream() throws Exception {
		final int NUMBER_OF_TIMES_IN_LOOP = 10;
		Path dataFilePath = TARGET_DIR.resolve("ch09-DataStreamTest.dat");
		
		try (FileOutputStream fos = new FileOutputStream(dataFilePath.toFile());
				DataOutputStream dos = new DataOutputStream(fos);) {
			// Write data out to the stream, which in turn will write to a binary file
			for (int i=0; i<NUMBER_OF_TIMES_IN_LOOP; i++) {
				dos.writeByte(i);
				dos.writeShort(i);
				dos.writeInt(i);
				dos.writeLong(i);
				dos.writeFloat(i);
				dos.writeDouble(i);
			}
		}
		
		try (FileInputStream fis = new FileInputStream(dataFilePath.toFile());
				DataInputStream dis = new DataInputStream(fis);) {
			// Read data in from the stream, which in turn reads from the binary file
			for (int i=0; i<NUMBER_OF_TIMES_IN_LOOP; i++) {
				// Read values in the same order that we wrote them out above
				String s = String.format("%d %d %d %d %g %g", 
						dis.readByte(),
						dis.readShort(),
						dis.readInt(),
						dis.readLong(),
						dis.readFloat(),
						dis.readDouble());
				LOGGER.debug(s);
			}
		}
	}

}


