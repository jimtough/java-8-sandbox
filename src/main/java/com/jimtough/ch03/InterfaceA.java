package com.jimtough.ch03;

/**
 * Example of an interface that includes a 'default' method
 * 
 * @author JTOUGH
 */
public interface InterfaceA {

	String FOO = "interface foo";

	/**
	 * Get the string that represents foo
	 * @return Non-null, non-empty string
	 */
	default String getFoo() {
		return FOO;
	}
	
	/**
	 * Get the string that represents baz
	 * @return Non-null, non-empty string
	 */
	String getBaz();
	
}
