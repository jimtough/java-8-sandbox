package com.jimtough.ch03;

@FunctionalInterface
public interface MyFunctionalInterface {

	/**
	 * Return the greater of the two integer parameter values
	 * @param a int
	 * @param b int
	 * @return int
	 */
	int max(int a, int b);

	// The following method declaration would cause a compilation error.
	// A 'functional interface' must have a single abstract method.
	//
	//int max(int... i);
	
	// In Java 8, interfaces may contain static methods.
	// Having a static method does not prevent this interface from being a
	// 'functional interface'.
	/**
	 * Return {@code true} if parameter 'a' is greater than 'b', otherwise {@code false}
	 * @param a int
	 * @param b int
	 * @return boolean
	 */
	static boolean isAGreaterThanB(int a, int b) {
		return a > b;
	}

	// In Java 8, interfaces may contain 'default' method implementations.
	// Having one or more default methods does not prevent this interface
	// from being a 'functional interface'.
	/**
	 * Return the greater of the two integer parameter values
	 * @param a int
	 * @param b int
	 * @return int
	 */
	default int getMaxValue(int a, int b) {
		return Math.max(a, b);
	}

	// Declaring one or more abstract methods that are already members of the
	// Object class will not prevent this interface from being a 'functional
	// interface'.
	String toString();
	boolean equals(Object o);
	int hashCode();
	
}
