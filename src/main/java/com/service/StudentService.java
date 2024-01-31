package com.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

import com.entity.Student;
import com.utility.Cp;

public class StudentService {

	public boolean addData(Student st) {

		Connection con = Cp.getConnection();
		System.out.println(con);
		PreparedStatement stmt;
		int executeUpdate = 0;
		try {
			stmt = con.prepareStatement("insert into student (first_name,last_name,phone_number,city) values(?,?,?,?)");
			stmt.setString(1, st.getFirstName());
			stmt.setString(2, st.getLastName());
			stmt.setString(3, st.getPhoneNumber());
			stmt.setString(4, st.getCity());
			executeUpdate = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (executeUpdate > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void writeJsonResponse(HttpServletResponse rsp, Object o) {
		rsp.setContentType("application/json");
	}

	public List<Student> getAllStudent() {
		Connection con = Cp.getConnection();
		List<Student> list = null;
		try {
			PreparedStatement stmt = con.prepareStatement("select * from student");
			ResultSet set = stmt.executeQuery();
			list = new ArrayList<Student>();
			while (set.next()) {
				Student student = new Student();

				student.setId(set.getInt(1));
				student.setFirstName(set.getString(2));
				student.setLastName(set.getString(3));
				student.setPhoneNumber(set.getString(4));
				student.setCity(set.getString(5));
				student.setCountry(set.getString(6));
				list.add(student);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean delete(int userId, HttpServletResponse res) {
		boolean f = false;
		try {
			Connection con = Cp.getConnection();

			PreparedStatement stmt = con.prepareStatement("select * from student where id = ?");
			stmt.setInt(1, userId);
			ResultSet set = stmt.executeQuery();
			if (set.next()) {
				String q = "delete from student where id = ?";
				PreparedStatement pst = con.prepareStatement(q);
				pst.setInt(1, userId);
				pst.executeUpdate();
				f = true;
			} else {
				res.getWriter().write("User not found with id " + userId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public boolean update(Student updatedStudent, int userId, HttpServletResponse res) {
	    boolean success = false;
	    try {
	        Connection con = Cp.getConnection();

	        // Check if the user with the given ID exists
	        PreparedStatement checkStmt = con.prepareStatement("select * from student where id = ?");
	        checkStmt.setInt(1, userId);
	        ResultSet checkResultSet = checkStmt.executeQuery();

	        if (checkResultSet.next()) {
	            // The user exists, proceed with the update
	            String updateQuery = "update student set first_name=?, last_name=?, phone_number=?, city=? where id=?";
	            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
	            
	            updateStmt.setString(1, updatedStudent.getFirstName());
	            updateStmt.setString(2, updatedStudent.getLastName());
	            updateStmt.setString(3, updatedStudent.getPhoneNumber());
	            updateStmt.setString(4, updatedStudent.getCity());
	            updateStmt.setInt(5, userId);  // ID of the user to update

	            int rowsUpdated = updateStmt.executeUpdate();

	            if (rowsUpdated > 0) {
	                success = true;
	            } else {
	                res.getWriter().write("Failed to update user with id " + userId);
	            }
	        } else {
	            res.getWriter().write("User not found with id " + userId);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return success;
	}
}
