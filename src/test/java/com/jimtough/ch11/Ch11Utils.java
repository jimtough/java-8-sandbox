package com.jimtough.ch11;

import static java.lang.Math.atan;
import static java.lang.Math.cbrt;
import static java.lang.Math.tan;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods and constants used in tests for this chapter
 * 
 * @author JTOUGH
 */
public class Ch11Utils {
	
	private Ch11Utils() {}
	
	static final Path TARGET_DIR = Paths.get("target");
	static final Path TEST_RESOURCES_DIR = Paths.get("src/test/resources");
	
	static final String BIG_SAMPLE_TEXT_FILE_NAME = "The Call of the Wild.txt";
	static final Path BIG_SAMPLE_TEXT_FILE_PATH = TEST_RESOURCES_DIR.resolve(BIG_SAMPLE_TEXT_FILE_NAME);

	static void doCpuIntensiveOperation(final int numberOfIterations) {
		for (int i=0; i<numberOfIterations; i++) {
			double d = tan(atan(tan(atan(tan(atan(tan(atan(tan(atan(123456789.123456789))))))))));
			cbrt(d);
		}
	}
	
}
