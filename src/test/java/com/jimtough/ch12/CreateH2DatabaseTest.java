package com.jimtough.ch12;

import static com.jimtough.ch12.Ch12Utils.*;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateH2DatabaseTest {

	@Rule
	public TestName testName = new TestName();
	@Rule
	public Timeout globalTimeout = new Timeout(30, TimeUnit.SECONDS);

	private static Connection conn;
	
	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		conn = createH2Connection();
		createDatabaseAndInsertTestData(conn);
	}

	@AfterClass
	public static void oneTimeTearDown() throws Exception {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
	
	@Before
	public void setUp() {}

	//--------------------------------------------------------------------
	
	@Test
	public void testXXX() throws Exception {
		// dummy test to allow oneTimeSetUp() to execute
	}
	
}
