package com.example.rhodyguide;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

public class Server extends AsyncTask<String, Void, String> {
	
	private boolean isSetUp = false;
	private Connection connection;
	
//	private static String url = "jdbc:mysql://abouthillier.vps.cs.uri.edu:3306/abouthil_rhodyguide";
//	private static String username = "abouthil";
//	private static String password = "=Gqk0TOf*D]I";
	
	private static String url = "jdbc:mysql://172.20.104.99:3306/Test";
	private static String username = "testing";
	private static String password = "test";
	
	private Thread thread;
	private Activity activity;
	
	public Server(Activity activity) {
		
		this.activity = activity;
			
		thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		    		setup();
		    	} catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});
		thread.start(); 
	}
	
	public void setup() {
	
		try {
		    System.out.println("Loading driver...");
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		
		connection = null;
		try {
		    System.out.println("Connecting database...");
		    connection = DriverManager.getConnection(url, username, password);
		    System.out.println("Database connected!");
		} catch (SQLException e) {
		    throw new RuntimeException("Cannot connect the database!", e);
		} 
		
		isSetUp = true;
		
	}
	
	public void checkUser(String userName, String password)
		    throws SQLException {		
		if (isSetUp){
			
			int count = 0;
			Statement stmt = null;
		    String query = "SELECT COUNT(*) FROM users "
		    		+ "WHERE username LIKE '"+userName+"' "
    					+ "AND password LIKE '"+password+"'";
		    String message = "";
		    		    
		    try {
		        stmt = connection.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        		        
		        while (rs.next())
		        	  count = rs.getInt(1);
		        
		        if (count < 1){
		        	message = "Username/Password invalid";
	        	}
	         	else if (count == 1)
	        		message = "Correct";
		        
		        final String text = message;
		        System.out.println(message);
		        activity.runOnUiThread(new Runnable() {
	        		  public void run() {
	        		    Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
	        		  }
	        	});
		        
		    } catch (SQLException e ) {
		    	System.out.println(e.getMessage());
		    } finally {
		        if (stmt != null)
		        	stmt.close(); 	
		    }
		}
		else {
			setup();
			checkUser(userName, password);
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

