package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Student;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.StudentService;
import com.utility.Cp;

@WebServlet("/student/*")
public class StudentController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StudentService stdsv = new StudentService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		List<Student> stlist = stdsv.getAllStudent();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		ObjectMapper obj = new ObjectMapper();
		String json = obj.writeValueAsString(stlist);
		response.getWriter().write(json);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		BufferedReader reader = req.getReader();
		StringBuilder requestBody = new StringBuilder();
		String line;
		Student newUser = null;
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			newUser = objectMapper.readValue(requestBody.toString(), Student.class);
			System.out.println(newUser);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing JSON");
		}

		boolean addData = this.stdsv.addData(newUser);
		System.out.println(addData);
		if (addData) {
			resp.setStatus(HttpServletResponse.SC_CREATED);
			resp.getWriter().write("User added successfully");
		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to add user");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();
		System.out.println(pathInfo);
		if (pathInfo != null) {
			// Extract the userId from the pathInfo
			String[] pathParts = pathInfo.split("/");
			if (pathParts.length > 1) {
				int userId = Integer.parseInt(pathParts[1]);
				System.out.println(userId);
				boolean delete = stdsv.delete(userId, resp);
				if (delete) {
					resp.getWriter().write("Id Deleted Successfully.");
				} else {
					resp.getWriter().write("Something Went Wrong.");
				}
			}
		} else {
			resp.getWriter().write("If you want to delete somethig please provide some Id");
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();

		if (pathInfo != null) {
			// Extract the student ID from the pathInfo
			String[] pathParts = pathInfo.split("/");
			if (pathParts.length > 1) {
				int studentId = Integer.parseInt(pathParts[1]);

				// Read JSON data from the request body
				BufferedReader reader = req.getReader();
				StringBuilder requestBody = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					requestBody.append(line);
				}

				// Convert JSON data to Java object using Jackson
				ObjectMapper objectMapper = new ObjectMapper();
				Student updatedStudent = objectMapper.readValue(requestBody.toString(), Student.class);

				// Perform the update operation
				boolean update = stdsv.update(updatedStudent, studentId, resp);

				if (update) {
					resp.getWriter().write("Student Updated Successfully.");
				} else {
					resp.getWriter().write("Something Went Wrong.");
				}
			}
		}else {
			resp.getWriter().write("Please provide an ID to update.");
		}
		}
}

//	@Override
//	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//	    String pathInfo = req.getPathInfo();
//
//	    if (pathInfo != null) {
//	        // Extract the entityId from the pathInfo
//	        String[] pathParts = pathInfo.split("/");
//	        if (pathParts.length > 1) {
//	            int entityId = Integer.parseInt(pathParts[1]);
//	            System.out.println(entityId);
//
//	            // Read JSON data from the request body
//	            BufferedReader reader = req.getReader();
//	            StringBuilder requestBody = new StringBuilder();
//	            String line;
//	            StudentService existingEntity = null;
//	             Object updatedEntity = null;
//	            while ((line = reader.readLine()) != null) {
//	                requestBody.append(line);
//	            }
//
//	            // Convert JSON data to Java object using Jackson
//	            ObjectMapper objectMapper = new ObjectMapper();
//	             updatedEntity = objectMapper.readValue(requestBody.toString(), Student.class);
//
//	            // Retrieve existing entity from the database
//	             existingEntity = StudentService.getById(entityId);
//
//	            if (existingEntity != null) {
//	                // Update fields of the existing entity with the new data
//	                existingEntity.setName(updatedEntity.getName());
//	                existingEntity.setDescription(updatedEntity.getDescription());
//	                // Update other fields as needed
//
//	                // Perform the update operation in the database
//	                boolean update = StudentService.update(existingEntity);
//
//	                if (update) {
//	                    resp.getWriter().write("Entity Updated Successfully.");
//	                } else {
//	                    resp.getWriter().write("Something Went Wrong.");
//	                }
//	            } else {
//	                resp.getWriter().write("Entity Not Found.");
//	            }
//	        }
//	    } else {
//	        resp.getWriter().write("Please provide an ID to update.");
//	    }
//	}
