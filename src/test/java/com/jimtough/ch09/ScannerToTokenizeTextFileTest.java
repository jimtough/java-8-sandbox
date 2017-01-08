package com.jimtough.ch09;

import static com.jimtough.ch09.Ch09Utils.*;
import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScannerToTokenizeTextFileTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScannerToTokenizeTextFileTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testScannerTokenizesTextFile() throws Exception {
		TreeMap<String,AtomicInteger> tokenToCountMap = new TreeMap<>();
		try (
				FileReader fr = new FileReader(BIG_SAMPLE_TEXT_FILE_PATH_A.toString());
				Scanner scanner = new Scanner(fr);) {
			// This delimiter regex will match 'non-words'
			scanner.useDelimiter("\\W");
			while (scanner.hasNext()) {
				String token = scanner.next();
				if (!token.isEmpty()) {
					// convert to lowercase before storing in map
					String key = token.toLowerCase();
					if (!tokenToCountMap.containsKey(key)) {
						tokenToCountMap.put(key, new AtomicInteger(0));
					}
					tokenToCountMap.get(key).incrementAndGet();
				}
			}
		}
		
		assertFalse(tokenToCountMap.isEmpty());
		
		// The input file has been tokenized, and the token counts stored in the map.
		// Output the contents of the map.
		tokenToCountMap.forEach((key,value)->LOGGER.debug("{} | [{}]", value, key));
		
		// Now let's put things in order of counts for a quick-and-dirty view of the most used tokens
		tokenToCountMap
			// create a stream from the entry set of this map
			.entrySet().stream()
			// convert each entry to a string composed of zero-padded count + token
			.map((entry)->String.format("%04d|%s",entry.getValue().get(),entry.getKey()))
			// sort those converted values in ascending order
			.sorted()
			// log them!
			.forEach((s)->LOGGER.debug("{}", s));
	}

}
