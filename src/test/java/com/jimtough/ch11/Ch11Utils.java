package com.jimtough.ch11;

import static java.lang.Math.atan;
import static java.lang.Math.cbrt;
import static java.lang.Math.tan;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility methods and constants used in tests for this chapter
 * 
 * @author JTOUGH
 */
public class Ch11Utils {
	
	private Ch11Utils() {}

	static final String A = "alpha";
	static final String B = "bravo";
	static final String C = "charlie";
	static final String D = "delta";
	static final String E = "echo";
	static final String F = "foxtrot";
	static final String G = "golf";
	
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

	static class SimpleThreadFactory implements ThreadFactory {
		private final AtomicInteger count = new AtomicInteger(0);
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "T-" + count.incrementAndGet());
			t.setDaemon(true);
			return t;
		}
	}
	
}
