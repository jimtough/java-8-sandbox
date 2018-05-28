package com.jimtough.ch09;

import static com.jimtough.ch09.Ch09Utils.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	public void testScannerTokenizesTextFile_A() throws Exception {
		TreeMap<String,AtomicInteger> tokenToCountMap = new TreeMap<>();
		try (
				FileReader fr = new FileReader(BIG_SAMPLE_TEXT_FILE_PATH_A.toString());
				BufferedReader br = new BufferedReader(fr);
				Scanner scanner = new Scanner(br);) {
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
		tokenToCountMap.forEach((key,value)->LOGGER.trace("{} | [{}]", value, key));
		
		// Now let's put things in order of counts for a quick-and-dirty view of the most used tokens
		tokenToCountMap
			// create a stream from the entry set of this map
			.entrySet().stream()
			// filter out anything that occurred less than 15 times, for brevity when logging output
			.filter(entry->entry.getValue().get() >= 15)
			// convert each entry to a string composed of zero-padded count + token
			.map((entry)->String.format("%04d|%s",entry.getValue().get(),entry.getKey()))
			// sort those converted values in ascending order
			.sorted()
			// log them!
			.forEach((s)->LOGGER.debug("{}", s));
	}
	
	@Test
	public void testScannerTokenizesTextFile_B() throws Exception {
		LOGGER.debug("Reading contents of text file, tokenizing and loading into a List...");
		List<String> listOfTokens = new ArrayList<>();
		try (
			FileReader fr = new FileReader(BIG_SAMPLE_TEXT_FILE_PATH_A.toString());
			BufferedReader br = new BufferedReader(fr);
			Scanner scanner = new Scanner(br);
		) {
			// This delimiter regex will match 'non-words'
			scanner.useDelimiter("\\W");
			while (scanner.hasNext()) {
				String token = scanner.next();
				if (!token.isEmpty()) {
					listOfTokens.add(token);
				}
			}
		}
		assertFalse(listOfTokens.isEmpty());
		LOGGER.debug("Contents of text file tokenized and loaded into a List");
		
		final Map<String,Long> tokenToCountMap;
		try (Stream<String> stream = listOfTokens.stream()) {
			tokenToCountMap = stream.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		} finally {
			// Allow this probably huge list to be garbage collected
			listOfTokens = null;
		}

		LOGGER.debug("Size of map key set: {}", tokenToCountMap.keySet().size());
		List<Map.Entry<String,Long>> mapEntryList = new ArrayList<>(tokenToCountMap.size());
		for (Map.Entry<String,Long> entry : tokenToCountMap.entrySet()) {
			mapEntryList.add(entry);
		}
		Comparator<Map.Entry<String,Long>> comparator = new Comparator<Map.Entry<String,Long>>() {
			@Override public int compare(Entry<String, Long> a, Entry<String, Long> b) {
				if (a.getValue() < b.getValue()) {
					return -1;
				} else if (a.getValue() > b.getValue()) {
					return 1;
				}
				return 0;
			}
		};
		Collections.sort(mapEntryList, comparator);
		for (Map.Entry<String,Long> entry : mapEntryList) {
			LOGGER.debug(String.format("%04d|%s",entry.getValue(),entry.getKey()));
		}
	}
}
