package com.jimtough.ch04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@code GenericInterface}
 * 
 * @author JTOUGH
 */
class GenericClass implements GenericInterface<String, Float> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericClass.class);
	
	@Override
	public double sum(Float... n) {
		double sum = 0D;
		for (float f : n) {
			sum += (double)f;
		}
		return sum;
	}

	@Override
	public void logMe(String t) {
		LOGGER.debug(t.toString());
	}

	@Override
	public <V extends Number> V max(V v1, V v2) {
		if (v1.doubleValue() < v2.doubleValue()) {
			return v2;
		}
		return v1;
	}
	
}
