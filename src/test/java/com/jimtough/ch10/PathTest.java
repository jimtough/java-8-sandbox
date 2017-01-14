package com.jimtough.ch10;

import static com.jimtough.ch10.Ch10Utils.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PathTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PathTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testGetRoot() throws Exception {
		// Seems that my relative path does NOT have a root
		LOGGER.debug("TEST_RESOURCES_DIR.getRoot(): {}", TEST_RESOURCES_DIR.getRoot());
		assertNull(TEST_RESOURCES_DIR.getRoot());
		
		// When I convert the relative path to an absolute path, it does have a root (as expected)
		LOGGER.debug("TEST_RESOURCES_DIR.toAbsolutePath().getRoot(): {}",TEST_RESOURCES_DIR.toAbsolutePath().getRoot());
		assertNotNull(TEST_RESOURCES_DIR.toAbsolutePath().getRoot());
	}
	
	@Test
	public void testGetFileName() throws Exception {
		LOGGER.debug("TEST_RESOURCES_DIR.getFileName(): {}", TEST_RESOURCES_DIR.getFileName());
		assertEquals("resources", TEST_RESOURCES_DIR.getFileName().toString());
		
		LOGGER.debug("BIG_SAMPLE_TEXT_FILE_PATH: {}", BIG_SAMPLE_TEXT_FILE_PATH);
		LOGGER.debug("BIG_SAMPLE_TEXT_FILE_PATH.getFileName(): {}", BIG_SAMPLE_TEXT_FILE_PATH.getFileName());
		assertEquals(BIG_SAMPLE_TEXT_FILE_NAME, BIG_SAMPLE_TEXT_FILE_PATH.getFileName().toString());
	}
	
	@Test
	public void testGetParent() throws Exception {
		LOGGER.debug("TEST_RESOURCES_DIR: {}", TEST_RESOURCES_DIR);
		LOGGER.debug("TEST_RESOURCES_DIR.getParent(): {}", TEST_RESOURCES_DIR.getParent());
		Path parent = TEST_RESOURCES_DIR.getParent();
		
		assertEquals("test", parent.getFileName().toString());
		assertTrue(Files.isDirectory(parent));
	}
	
	@Test
	public void testGetNameCount() throws Exception {
		LOGGER.debug("TEST_RESOURCES_DIR: {}", TEST_RESOURCES_DIR);
		LOGGER.debug("TEST_RESOURCES_DIR.getNameCount(): {}", TEST_RESOURCES_DIR.getNameCount());
		assertEquals(3, TEST_RESOURCES_DIR.getNameCount());
		LOGGER.debug("TEST_RESOURCES_DIR.toAbsolutePath().getNameCount(): {}", TEST_RESOURCES_DIR.toAbsolutePath().getNameCount());
		assertTrue(TEST_RESOURCES_DIR.toAbsolutePath().getNameCount() > TEST_RESOURCES_DIR.getNameCount());
	}
	
	@Test
	public void testGetName() throws Exception {
		for (int i=0; i<TEST_RESOURCES_DIR.getNameCount(); i++) {
			LOGGER.debug("i: {} | TEST_RESOURCES_DIR.getName(i): {}", i, TEST_RESOURCES_DIR.getName(i));
		}
		assertEquals("src", TEST_RESOURCES_DIR.getName(0).toString());
		assertEquals("test", TEST_RESOURCES_DIR.getName(1).toString());
		assertEquals("resources", TEST_RESOURCES_DIR.getName(2).toString());
	}
	
	@Test
	public void testSubpath() throws Exception {
		LOGGER.debug("TEST_RESOURCES_DIR.subpath(1, 3): {}", TEST_RESOURCES_DIR.subpath(1, 3));
	}
	
	@Test
	public void testNormalize() throws Exception {
		Path abnormal = Paths.get("silly", ".", "path", ".", ".", ".", "to", ".", "normalize", ".");
		Path normalized = abnormal.normalize();
		LOGGER.debug("abnormal: {}", abnormal);
		LOGGER.debug("normalized: {}", normalized);
		assertEquals(4, normalized.getNameCount());
	}
	
	@Test
	public void testResolve() throws Exception {
		Path path1 = Paths.get("alpha", "baker", "charlie");
		Path path2 = Paths.get("delta", "echo", "foxtrot");
		LOGGER.debug("path1: {}", path1);
		LOGGER.debug("path2: {}", path2);
		Path pathResolvedWithPath = path1.resolve(path2);
		Path pathResolvedWithString = path1.resolve(path2.toString());
		LOGGER.debug("pathResolvedWithPath: {}", pathResolvedWithPath);
		LOGGER.debug("pathResolvedWithString: {}", pathResolvedWithString);
		assertEquals("alpha", pathResolvedWithPath.getName(0).toString());
		assertEquals("foxtrot", pathResolvedWithPath.getFileName().toString());
	}
	
	@Test
	public void testIsAbsoluteAndToAbsolute() throws Exception {
		assertFalse(TEST_RESOURCES_DIR.isAbsolute());
		assertTrue(TEST_RESOURCES_DIR.toAbsolutePath().isAbsolute());
	}
	
	@Test
	public void testStartsWithAndEndsWith() throws Exception {
		Path path1 = Paths.get("alpha", "baker", "charlie");
		Path path2 = Paths.get("delta", "echo", "foxtrot");
		LOGGER.debug("path1: {}", path1);
		LOGGER.debug("path2: {}", path2);
		Path resolved = path1.resolve(path2);
		LOGGER.debug("resolved: {}", resolved);
		assertTrue(resolved.startsWith(path1));
		assertTrue(resolved.endsWith(path2));
	}
	
	@Test
	public void testToUri() throws Exception {
		LOGGER.debug("TEST_RESOURCES_DIR.toAbsolutePath().toUri(): {}", TEST_RESOURCES_DIR.toAbsolutePath().toUri());
	}
	
	@Test
	public void testToRealPathWhenTargetExists() throws Exception {
		Path abnormalPath = Paths.get(".", ".", "src", ".", "test", ".", ".", "resources", ".");
		// It's like a combination of 'normalize()' and 'toAbsolutePath()', BUT the target file/dir MUST EXIST!
		Path realPath = abnormalPath.toRealPath();
		LOGGER.debug("abnormalPath: {} | realPath: {}", abnormalPath, realPath);
	}
	
	@Test(expected=IOException.class)
	public void testToRealPathWhenTargetDoesNotExist() throws Exception {
		Path abnormalPath = Paths.get(".", ".", "src", ".", "does", "not", "exist", ".");
		abnormalPath.toRealPath();
	}

	//--------------------------------------------------------------------
	
	@Test
	public void testEquals() throws Exception {
		Path path1 = Paths.get("alpha", "baker", ".", "charlie");
		Path path2 = Paths.get("delta", "echo", "foxtrot");
		Path path3 = Paths.get("alpha", "baker", ".", "charlie");
		Path path4 = Paths.get("alpha", "baker", "charlie");
		Path path5 = path1.toAbsolutePath();
		LOGGER.debug("path1: {}", path1);
		LOGGER.debug("path2: {}", path2);
		LOGGER.debug("path3: {}", path3);
		LOGGER.debug("path4: {}", path4);
		LOGGER.debug("path5: {}", path5);
		// A Path is only equal to itself, and another path with identical elements (like 1 and 3).
		// Just because they would evaluate to the same file DOES NOT MATTER!
		// The equals() method does not access the drive or normalize, so it compares solely on the object!
		assertEquals(path1, path1);
		assertNotEquals(path1, path2);
		assertEquals(path1, path3);
		assertNotEquals(path1, path4);
		assertNotEquals(path1, path5);
	}
	
}




