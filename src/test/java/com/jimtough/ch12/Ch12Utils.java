package com.jimtough.ch12;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods and constants used in tests for this chapter
 * 
 * @author JTOUGH
 */
public class Ch12Utils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Ch12Utils.class);
	
	private Ch12Utils() {}

	public static final String H2_JDBC_DRIVER_NAME = "org.h2.Driver";
	public static final String H2_JDBC_URL = "jdbc:h2:mem:ch12test";
	public static final String H2_JDBC_USERNAME = "sa";
	public static final String H2_JDBC_PASSWORD = "";

	/**
	 * Create a new connection to an in-memory H2 database.
	 * If this is the first connection, then it will create a new empty database.
	 * @return Non-null
	 * @throws SQLException May be thrown
	 */
	public static Connection createH2Connection() throws SQLException {
		Connection conn = DriverManager.getConnection(H2_JDBC_URL, H2_JDBC_USERNAME, H2_JDBC_PASSWORD);
		assertNotNull(conn);
		assertTrue(conn.isValid(5));
		return conn;
	}

	private static void createTables(Connection conn) throws SQLException {
		final String createProductsTableDDL =
			"CREATE TABLE products (" +
				"id int AUTO_INCREMENT PRIMARY KEY, " +
				"name VARCHAR2(255), " +
				"price NUMBER(9,2) " +
			")";
		conn.createStatement().executeUpdate(createProductsTableDDL);
	}

	private static void logInsert(String table, PreparedStatement ps) throws SQLException {
		ResultSet rs = ps.getGeneratedKeys();
		assertTrue(rs.next());
		long key = rs.getLong(1);
		LOGGER.debug("New row in table [{}] has generated key [{}]", table, key);
	}
	
	private static void insertData(Connection conn) throws SQLException {
		final String productsInsertStatement = "INSERT INTO products (name, price) VALUES (?,?)";
		final Object[][] productsData = new Object[][] {
			{"cute necklace", 49.99},
			{"ugly sweater", 69.99},
			{"comfortable shoes", 129.99}
		};
		PreparedStatement psProducts = conn.prepareStatement(productsInsertStatement, Statement.RETURN_GENERATED_KEYS);
		for (int row=0; row<productsData.length; row++) {
			for (int col=0; col<productsData[row].length; col++) {
				LOGGER.debug("insert | row: {} | col: {} | value: {}", row, col, productsData[row][col]);
				psProducts.setObject(col+1, productsData[row][col]);
			}
			assertEquals(1, psProducts.executeUpdate());
			logInsert("products", psProducts);
		}
	}
	
	public static void createDatabaseAndInsertTestData(Connection conn) throws SQLException {
		createTables(conn);
		insertData(conn);
	}
	
}
