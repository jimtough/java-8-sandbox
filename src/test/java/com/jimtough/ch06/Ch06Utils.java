package com.jimtough.ch06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods and constants used in tests for this chapter
 * 
 * @author JTOUGH
 */
class Ch06Utils {

	private Ch06Utils() {}
	
	static final String A = "alpha";
	static final String B = "bravo";
	static final String C = "charlie";
	static final String D = "delta";
	static final String E = "echo";
	static final String F = "foxtrot";
	static final String G = "golf";

	/**
	 * Creates a new {@code List<String>} that contains each of the phonetic alphabet
	 * string constants from this class, in alphabetical order
	 * @return Non-null, non-empty {@code List<String>}
	 */
	static List<String> newAlphabeticalStringList() {
		List<String> stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
		return stringList;
	}
	
	static final String ONE =   "1";
	static final String TWO =   "2";
	static final String THREE = "3";
	static final String FOUR =  "4";
	static final String FIVE =  "5";

	/**
	 * Creates a new {@code List<String>} that contains each of the integer string constants
	 * from this class in alphabetical order
	 * @return Non-null, non-empty {@code List<String>}
	 */
	static List<String> newIntegerStringsList() {
		List<String> stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(ONE,TWO,THREE,FOUR,FIVE));
		return stringList;
	}
	
}
