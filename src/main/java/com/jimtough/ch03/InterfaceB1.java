package com.jimtough.ch03;

public interface InterfaceB1 {
	
	String BAR = "InterfaceB1 bar";

	/**
	 * Get the string that represents bar
	 * @return Non-null, non-empty string
	 */
	default String getBar() {
		return BAR;
	}

}
