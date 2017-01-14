package com.jimtough.ch10;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods and constants used in tests for this chapter
 * 
 * @author JTOUGH
 */
public class Ch10Utils {
	
	private Ch10Utils() {}
	
	static final Path TARGET_DIR = Paths.get("target");
	static final Path TEST_RESOURCES_DIR = Paths.get("src/test/resources");
	
	static final String BIG_SAMPLE_TEXT_FILE_NAME = "The Call of the Wild.txt";
	static final Path BIG_SAMPLE_TEXT_FILE_PATH = TEST_RESOURCES_DIR.resolve(BIG_SAMPLE_TEXT_FILE_NAME);

}
