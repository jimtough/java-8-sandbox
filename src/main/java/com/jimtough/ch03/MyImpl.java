package com.jimtough.ch03;

public class MyImpl implements InterfaceA, InterfaceB1, InterfaceB2 {

	public static final String IMPL_BAZ = "impl baz";
	
	@Override
	public String getBaz() {
		return IMPL_BAZ;
	}

	// Because InterfaceB1 and InterfaceB2 both provide a default method
	// with the same signature, this class must manually override the method.
	// In this case, the implementation chooses to explicitly execute one of
	// the default methods from the two conflicting interfaces.
	@Override
	public String getBar() {
		return InterfaceB2.super.getBar();
	}

}
