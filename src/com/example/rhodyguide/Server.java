package com.example.rhodyguide;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;

import android.os.AsyncTask;
import android.util.Log;

public class Server {
	
	private boolean isSetUp = false;
	
	public Server() {
			
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		    		if (!isSetUp)
		    			setup();
		    	} catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});

		thread.start(); 
	}
	
	private void setup() {
	
		try {
		    System.out.println("Loading driver...");
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		
		String url = "jdbc:mysql://abouthillier.vps.cs.uri.edu:3306/abouthil_rhodyguide";
		String username = "abouthil";
		String password = "=Gqk0TOf*D]I";
		Connection connection = null;
		try {
		    System.out.println("Connecting database...");
		    connection = DriverManager.getConnection(url, username, password);
		    Log.e("Test", "Connected");
		    System.out.println("Database connected!");
		} catch (SQLException e) {
		    throw new RuntimeException("Cannot connect the database!", e);
		} finally {
		    System.out.println("Closing the connection.");
		    if (connection != null) 
		    	try { 
		    		connection.close(); 
		    	} catch (SQLException ignore) {}
		}
		
		isSetUp = true;
		
	}

}
