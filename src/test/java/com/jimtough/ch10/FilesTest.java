package com.jimtough.ch10;

import static com.jimtough.ch10.Ch10Utils.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FilesTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilesTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testCreateDirectoryWhenParentDirectoryExists() throws Exception {
		assertTrue(Files.exists(TARGET_DIR));
		Path newDir = TARGET_DIR.resolve(Paths.get("newdir"));
		assertFalse(Files.exists(newDir));
		try {
			Files.createDirectory(newDir);
			assertTrue(Files.exists(newDir));
		} finally {
			Files.deleteIfExists(newDir);
		}
	}
	
	@Test(expected=IOException.class)
	public void testCreateDirectoryWhenParentAndIntermediateDirectoriesDoNotExist() throws Exception {
		assertTrue(Files.exists(TARGET_DIR));
		Path newDir = TARGET_DIR.resolve(Paths.get("probably","does","not","exist","yet","I","would","imagine"));
		Files.createDirectory(newDir);
	}
	
	@Test
	public void testCreateDirectoriesWhenParentAndIntermediateDirectoriesDoNotExist() throws Exception {
		assertTrue(Files.exists(TARGET_DIR));
		Path newDir = TARGET_DIR.resolve(Paths.get("probably","does","not","exist","yet","I","would","imagine"));
		assertFalse(Files.exists(newDir));
		try {
			Files.createDirectories(newDir);
		} finally {
			Path p = newDir;
			while (!p.toRealPath().equals(TARGET_DIR.toRealPath())) {
				Files.deleteIfExists(p);
				p = p.getParent();
			}
		}
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCreateTempDirectoryAndCreateTempFile() throws Exception {
		assertTrue(Files.exists(TARGET_DIR));
		Path tempDirPath = Files.createTempDirectory(TARGET_DIR, "temp-ch10-");
		assertTrue(Files.exists(tempDirPath));
		Path tempFilePath = Files.createTempFile(tempDirPath, "temp-file-ch10-", ".foo");
		assertTrue(Files.exists(tempFilePath));
		LOGGER.debug("tempDirPath: {} | tempFilePath: {}", tempDirPath, tempFilePath);
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testCopyAndMove() throws Exception {
		Path copiedFilePath = TARGET_DIR.resolve("ch10-copied-file.txt");
		Path movedFilePath = TARGET_DIR.resolve("ch10-moved-file.txt");
		Files.copy(BIG_SAMPLE_TEXT_FILE_PATH, copiedFilePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		assertTrue(Files.exists(copiedFilePath));
		Files.move(copiedFilePath, movedFilePath, StandardCopyOption.REPLACE_EXISTING);
		assertFalse(Files.exists(copiedFilePath));
		assertTrue(Files.exists(movedFilePath));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testIsSameFile() throws Exception {
		Path abnormalPath = Paths.get(".", ".", "src", ".", "test", ".", ".", "resources", ".");
		assertTrue(Files.isSameFile(TEST_RESOURCES_DIR, abnormalPath));
	}
	
	@Test
	public void testIsRegularFile() throws Exception {
		assertTrue(Files.isRegularFile(BIG_SAMPLE_TEXT_FILE_PATH));
		assertFalse(Files.isRegularFile(TEST_RESOURCES_DIR));
	}
	
	@Test
	public void testIsDirectory() throws Exception {
		assertFalse(Files.isDirectory(BIG_SAMPLE_TEXT_FILE_PATH));
		assertTrue(Files.isDirectory(TEST_RESOURCES_DIR));
	}
	
	@Test
	public void testIsSymbolicLink() throws Exception {
		assertFalse(Files.isSymbolicLink(BIG_SAMPLE_TEXT_FILE_PATH));
		assertFalse(Files.isSymbolicLink(TEST_RESOURCES_DIR));
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testSize() throws Exception {
		LOGGER.debug("Size of [{}]: {} bytes", BIG_SAMPLE_TEXT_FILE_PATH, Files.size(BIG_SAMPLE_TEXT_FILE_PATH));
	}
	
	@Test
	public void testGetOwner() throws Exception {
		LOGGER.debug("Owner of [{}]: {}", BIG_SAMPLE_TEXT_FILE_PATH, Files.getOwner(BIG_SAMPLE_TEXT_FILE_PATH));
	}
	
	@Test
	public void testIsReadableIsWritableIsExecutable() throws Exception {
		Path filePath = BIG_SAMPLE_TEXT_FILE_PATH;
		Path dirPath = TEST_RESOURCES_DIR;
		LOGGER.debug("File: [{}] | readable? {} | writable? {} | executable? {}", 
				filePath, Files.isReadable(filePath), Files.isWritable(filePath), Files.isExecutable(filePath));
		LOGGER.debug("Dir: [{}] | readable? {} | writable? {} | executable? {}", 
				dirPath, Files.isReadable(dirPath), Files.isWritable(dirPath), Files.isExecutable(dirPath));
	}
	
}
