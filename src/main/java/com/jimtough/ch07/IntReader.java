package com.jimtough.ch07;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads and parses integers from a string
 * 
 * @author JTOUGH
 */
public class IntReader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IntReader.class);

	/**
	 * Read the first parseable integer value from the string and return the value
	 * @param s Non-null
	 * @return int
	 * @throws InputMismatchException
	 * @throws NoSuchElementException
	 */
	public int readFirstInt(String s) {
		try (Scanner scanner = new Scanner(s)) {
			int firstInt = scanner.nextInt();
			LOGGER.debug("firstInt: {}", firstInt);
			return firstInt;
		}
	}

	/**
	 * Read the first parseable integer value from the string and return the value
	 * @param s Non-null
	 * @return Integer, or null if no parseable integer is found in the string
	 */
	public Integer readFirstInteger(String s) {
		try (Scanner scanner = new Scanner(s)) {
			int firstInt = scanner.nextInt();
			LOGGER.debug("firstInt: {}", firstInt);
			return Integer.valueOf(firstInt);
		} catch (InputMismatchException ime) {
			LOGGER.warn("{} caught - message: {}", ime.getClass().getSimpleName(), ime.getMessage());
			return null;
		} catch (NoSuchElementException nsee) {
			LOGGER.warn("{} caught - message: {}", nsee.getClass().getSimpleName(), nsee.getMessage());
			return null;
		} catch (RuntimeException re) {
			LOGGER.warn("{} caught - message: {}", re.getClass().getSimpleName(), re.getMessage());
			return null;
		}
	}

	/**
	 * Read the first parseable integer value from the string and return the value
	 * @param s Non-null
	 * @return Integer, or null if no parseable integer is found in the string
	 */
	public Integer readFirstIntegerWithMulticatch(String s) {
		try (Scanner scanner = new Scanner(s)) {
			int firstInt = scanner.nextInt();
			LOGGER.debug("firstInt: {}", firstInt);
			return Integer.valueOf(firstInt);
		} catch (InputMismatchException ime) {
			LOGGER.warn("InputMismatchException caught - message: {}", ime.getMessage());
			return null;
		} catch (NoSuchElementException | NullPointerException e) {
			LOGGER.warn("{} caught - probably due to null/empty string", e.getClass().getSimpleName());
			return null;
		} catch (RuntimeException re) {
			LOGGER.warn("{} caught - message: {}", re.getClass().getSimpleName(), re.getMessage());
			return null;
		}
	}
	
}
