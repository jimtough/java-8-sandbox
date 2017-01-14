package com.jimtough.ch10;

import static com.jimtough.ch10.Ch10Utils.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileAttributesAndPropertiesTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileAttributesAndPropertiesTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testGetAttributeByName() throws Exception {
		Path filePath = BIG_SAMPLE_TEXT_FILE_PATH;
		Path dirPath = TEST_RESOURCES_DIR;
		Path[] pathArray = {filePath, dirPath};
		for (Path p : pathArray) {
			LOGGER.debug("Path: [{}] | creationTime: {} | lastModifiedTime: {} | size: {} | isDirectory: {}", 
					p, 
					Files.getAttribute(p, "creationTime", LinkOption.NOFOLLOW_LINKS),
					Files.getAttribute(p, "lastModifiedTime"),
					Files.getAttribute(p, "size"),
					Files.getAttribute(p, "isDirectory"));
		}
	}
	
	@Test
	public void testGetAttributeWindowsSpecific() throws Exception {
		assumeTrue("Skipping test that will only run on Windows-based O/S", isRunningOnWindowsOS());
		Path filePath = BIG_SAMPLE_TEXT_FILE_PATH;
		Path dirPath = TEST_RESOURCES_DIR;
		Path[] pathArray = {filePath, dirPath};
		for (Path p : pathArray) {
			LOGGER.debug("Path: [{}] | dos:hidden: {}", p, Files.getAttribute(p, "dos:hidden"));
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetAttributeWithInvalidAttributeNameString() throws Exception {
		Files.getAttribute(BIG_SAMPLE_TEXT_FILE_PATH, "foobar");
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testReadAttributesForBasicFileAttributes() throws Exception {
		Path filePath = BIG_SAMPLE_TEXT_FILE_PATH;
		Path dirPath = TEST_RESOURCES_DIR;
		Path[] pathArray = {filePath, dirPath};
		for (Path p : pathArray) {
			BasicFileAttributes bfa = Files.readAttributes(p, BasicFileAttributes.class);
			LOGGER.debug("Path: [{}] | creationTime: {} | lastModifiedTime: {} | size: {} | isDirectory: {}", 
					p, 
					bfa.creationTime(),
					bfa.lastModifiedTime(),
					bfa.size(),
					bfa.isDirectory());
		}
	}
	
	@Test
	public void testReadAttributesForDosFileAttributes() throws Exception {
		
		assumeTrue("Skipping test that will only run on Windows-based O/S", isRunningOnWindowsOS());
		
		Path filePath = BIG_SAMPLE_TEXT_FILE_PATH;
		Path dirPath = TEST_RESOURCES_DIR;
		Path[] pathArray = {filePath, dirPath};
		for (Path p : pathArray) {
			// Extends BasicFileAttributes, so you get all the basics plus a few more
			DosFileAttributes dfa = Files.readAttributes(p, DosFileAttributes.class);
			LOGGER.debug("Path: [{}] | creationTime: {} | size: {} | isHidden: {}", 
					p, 
					dfa.creationTime(),
					dfa.size(),
					dfa.isHidden());
		}
	}
	
	@Test
	public void testReadAttributesAsMap() throws Exception {
		Path filePath = BIG_SAMPLE_TEXT_FILE_PATH;
		Path dirPath = TEST_RESOURCES_DIR;
		Path[] pathArray = {filePath, dirPath};
		for (Path p : pathArray) {
			LOGGER.debug("--------------------------------------------------");
			LOGGER.debug("attributes for [{}]", p);
			Map<String,Object> attrMap = Files.readAttributes(p, "*");
			assertFalse(attrMap.isEmpty());
			attrMap.forEach((k,v)->LOGGER.debug("attr key: [{}] | attr value: [{}]", k, v));
		}
		LOGGER.debug("--------------------------------------------------");
	}

	//--------------------------------------------------------------------
	
}
