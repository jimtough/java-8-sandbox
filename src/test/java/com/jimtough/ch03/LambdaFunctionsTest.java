package com.jimtough.ch03;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Experiment with the basics of 'lambda functions'
 * 
 * @author JTOUGH
 */
public class LambdaFunctionsTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LambdaFunctionsTest.class);

	@FunctionalInterface
	private interface HelloWorldInterface {
		void sayHelloWorld();
	}

	@Test
	public void testHelloWorldInterface() {
		// A lambda expression with no parameters
		HelloWorldInterface lambda = () -> LOGGER.debug("Hello, world!");
		lambda.sayHelloWorld();
	}
	
	@Test
	public void testA() {
		// A lambda expression with inferred parameter types and simple lambda expression
		MyFunctionalInterface lambda = (x,y) -> Math.max(x, y);
		int max = lambda.max(7, 11);
		assertEquals(11, max);
	}

	@Test
	public void testB() {
		// A lambda expression with explicit parameter types and simple lambda expression
		MyFunctionalInterface lambda = (int x, int y) -> Math.max(x, y);
		assertEquals(9, lambda.max(9, 9));
	}

	@Test
	public void testC() {
		// A lambda expression with inferred parameter types and a block lambda
		// inside curly braces
		MyFunctionalInterface lambda = (x, y) -> {
			// Calling a public static method that was declared in MyFunctionalInterface
			if (MyFunctionalInterface.isAGreaterThanB(x, y)) {
				return x;
			}
			return y;
		};
		assertEquals(66, lambda.max(6, 66));
	}

	@Test
	public void testThisInsideLambdaBlock() {
		// When you use the 'this' keyword inside a lambda block,
		// it refers to the object that invoked the lambda function.
		// In this case, the JUnit test class: "MyFunctionalInterfaceTest".
		MyFunctionalInterface lambda = (x, y) -> {
			LOGGER.debug("this | class: {}", this.getClass().getSimpleName());
			return Math.max(x, y);
		};
		assertEquals(2, lambda.max(2, 1));
	}

	@Test
	public void testFinalAndEffectivelyFinal() {
		// Lambda block can reference an explicitly 'final' variable in enclosing scope
		final String hello = "hello";
		// Lambda block can reference an 'effectively final' variable in enclosing scope
		String world = "world";
		HelloWorldInterface lambda = () -> {
			//  The following line would cause a compilation error because it redeclares a
			//  local variable of the same name in the enclosing scope
			//String hello = "not allowed";
			String willBeSaid = "Final " + hello + ", " + world + "!";
			LOGGER.debug(willBeSaid);
		};
		// Follow line would cause a compilation error inside the lambda block.
		// The 'world' variable must be treated as 'effectively final', so changing
		// its value below would prevent that.
		//world = null;
		lambda.sayHelloWorld();
	}
	
}
