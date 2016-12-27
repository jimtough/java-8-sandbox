package com.jimtough.ch05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods and constants used in tests for this chapter
 * 
 * @author JTOUGH
 */
public class Ch05Utils {

	private Ch05Utils() {}
	
	public static final String A = "alpha";
	public static final String B = "bravo";
	public static final String C = "charlie";
	public static final String D = "delta";
	public static final String E = "echo";
	public static final String F = "foxtrot";
	public static final String G = "golf";

	/**
	 * Creates a new {@code List<String>} that contains each of the string constants from this class
	 * in alphabetical order
	 * @return Non-null, non-empty {@code List<String>}
	 */
	public static List<String> newAlphabeticalStringList() {
		List<String> stringList = new ArrayList<>();
		stringList.addAll(Arrays.asList(A,B,C,D,E,F,G));
		return stringList;
	}
	
}
