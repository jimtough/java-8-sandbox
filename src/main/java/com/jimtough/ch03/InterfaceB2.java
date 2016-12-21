package com.jimtough.ch03;

public interface InterfaceB2 {
	
	String BAR = "InterfaceB2 bar";

	/**
	 * Get the string that represents bar
	 * @return Non-null, non-empty string
	 */
	default String getBar() {
		return BAR;
	}

}
