package com.jimtough.ch09;

import static com.jimtough.ch09.Ch09Utils.*;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ObjectOutputStreamTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectOutputStreamTest.class);
	
	@Rule
	public TestName testName = new TestName();
    
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------

	@Test
	public void testWriteMapToObjectOutputStreamThenReadItBack() throws Exception {
		Path dataFilePath = TARGET_DIR.resolve("ch09-ObjectOutputStreamTest.ser");
		Map<String,String> fruitToColourMap = new LinkedHashMap<>();
		fruitToColourMap.put("banana", "yellow");
		fruitToColourMap.put("apple", "red");
		fruitToColourMap.put("orange", "orange");
		fruitToColourMap.put("grape", "purple");
		
		try (FileOutputStream fos = new FileOutputStream(dataFilePath.toFile());
				ObjectOutputStream oos = new ObjectOutputStream(fos);) {
			oos.writeObject(fruitToColourMap);
		}
		// Map reference is gone.
		fruitToColourMap = null;
		
		// Now we have to read it back from the object data file.
		try (FileInputStream fis = new FileInputStream(dataFilePath.toFile());
				ObjectInputStream ois = new ObjectInputStream(fis);) {
			Object o = ois.readObject();
			LOGGER.debug("type of 'o': [{}]", o.getClass().getName());
			assertTrue(o instanceof Map);
			@SuppressWarnings("unchecked")
			Map<String,String> map = (Map<String,String>)o;
			map.forEach((key,value)->LOGGER.debug("fruit: {} | colour: {}", key, value));
		}
	}

}
