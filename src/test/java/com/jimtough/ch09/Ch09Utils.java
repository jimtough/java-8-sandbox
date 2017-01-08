package com.jimtough.ch09;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods and constants used in tests for this chapter
 * 
 * @author JTOUGH
 */
public class Ch09Utils {

	private Ch09Utils() {}
	
	static final Path TARGET_DIR = Paths.get("target");
	
	private static final String BIG_SAMPLE_TEXT_FILE_NAME = "The Call of the Wild.txt";
	static final Path BIG_SAMPLE_TEXT_FILE_PATH_A = Paths.get("src/test/resources/", BIG_SAMPLE_TEXT_FILE_NAME);
	
}
