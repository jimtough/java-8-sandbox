package com.jimtough.ch04;

/**
 * Example of an interface that includes generic types
 * 
 * @author JTOUGH
 *
 * @param <T> Generic type T (can be any class or interface)
 * @param <N> Generic type N, which includes a constraint (N is or extends the Number class)
 */
interface GenericInterface<T, N extends Number> {

	// Suppress annoying "Potential heap pollution via varargs" compiler warning
	@SuppressWarnings("unchecked")
	// A simple method of this generic interface
	double sum(N... n);

	// Another simple method of this generic interface
	void logMe(T t);

	// A generic method whose generic type is unrelated to the generic types
	// for this interface
	<V extends Number> V max(V v1, V v2);
	
}
