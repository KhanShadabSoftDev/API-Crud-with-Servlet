package com.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Cp {
	private static Connection conn;

	public static Connection getConnection() {

		if (conn == null) {
			try {
//				Class.forName("com.mysql.jdbc.cj.Driver");
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ServletApi","root","9326");
				
				
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return conn;

	}
}
