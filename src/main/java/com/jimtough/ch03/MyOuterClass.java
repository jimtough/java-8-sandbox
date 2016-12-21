package com.jimtough.ch03;

import com.jimtough.ch03.MyOuterClass.FooGetter.FooBarGetter;
import com.jimtough.ch03.MyOuterClass.FooGetter.FooBarGetter.FooBarBazGetter;

/**
 * Used to experiment with the various types of inner/local classes that are supported
 * 
 * @author JTOUGH
 */
public class MyOuterClass {
	
	public static String baz = "outer (static var) baz";

	public String foo = "outer foo";
	public String bar = "outer bar";

	/**
	 * Inner interface (implicitly static)
	 */
	public interface FooGetter {
		String getFoo();
		interface FooBarGetter extends FooGetter {
			String getBar();
			interface FooBarBazGetter extends FooBarGetter {
				String getBaz();
			}
		}
	}
	
	/**
	 * Non-static inner class
	 */
	public class MyInnerClass implements FooBarGetter {
		private final String foo = "inner foo";
		public String getFoo() {return foo;}
		public String getBar() {return bar;}
		// Will not compile following line. 
		// Interfaces cannot be declared inside a non-static inner class.
		//public interface Stuff {}
	}

	/**
	 * Static inner class
	 */
	public static class MyStaticInnerClass {
		public String getBaz() {return baz;}
		// OK to declare an interface inside a static inner class.
		interface Stuff {}
	}

	/**
	 * Method of outer class
	 */
	public FooBarGetter doSomethingLocal() {
		String foo = "local foo";
		class MyLocalClass implements FooBarGetter {
			public String getFoo() {return foo;}
			public String getBar() {return bar;}
			// Will not compile following line. 
			// Interfaces cannot be declared inside a local class.
			//interface Stuff {}
		}
		MyLocalClass local = new MyLocalClass();
		return local;
	}

	/**
	 * Method of outer class
	 */
	public FooBarBazGetter doSomethingAnonymous() {
		final String foo = "anonymous foo";
		String bar = "anonymous bar";
		FooBarBazGetter anonymous = new FooBarBazGetter() {
			public String getFoo() {return foo;}
			public String getBar() {
				// Will not compile following line. Variable bar is 'effectively final'.
				//bar = "modified anonymous bar"; 
				return bar;
			}
			public String getBaz() {return baz;}
			// Will not compile following line. 
			// Interfaces cannot be declared inside an anonymous class.
			//interface Stuff {}
		};
		// Following line causes compilation error in local inner class.
		// Variable bar is 'effectively final'.
		//bar = "";
		return anonymous;
	}
	
}
