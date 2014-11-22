package com.example.rhodyguide;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import android.app.Activity;
import android.os.AsyncTask;

public class Server extends AsyncTask<String, Void, String> {
	
	private boolean isSetUp = false;
	private Connection connection;
	
//	private static String url = "jdbc:mysql://abouthillier.vps.cs.uri.edu:3306/abouthil_rhodyguide";
//	private static String username = "abouthil";
//	private static String password = "=Gqk0TOf*D]I";
//	
	
	private static String ipAddress = "172.20.104.99";
	private static String url = "jdbc:mysql://"+ipAddress+"/Test";
	private static String username = "root";
	
	private static String url2 = ""+url+"?user="+username+"";
		
	public Server(Activity activity) {}
	
	public Server() {
		// TODO Auto-generated constructor stub
	}

	public void setup() {
		
        try {
			try {
			    System.out.println("Loading driver...");
			    Class.forName("com.mysql.jdbc.Driver");
			    System.out.println("Driver loaded!");
			} catch (ClassNotFoundException e) {
			    throw new RuntimeException("Cannot find the driver in the classpath!", e);
			}
			connect();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void connect() {
		connection = null;
		try {
		    System.out.println("Connecting database...");
		    connection = DriverManager.getConnection(url2);
		    System.out.println("Database connected!");
			isSetUp = true;
		} catch (SQLException e) {
		    throw new RuntimeException("Cannot connect to the database!", e);
		} 
	}
	
	public boolean checkUser(String userName, String password)
		    throws SQLException {	
		
		System.out.println("In checkUser: "+userName);
		System.out.println("In checkUser: "+password);
		
		boolean isUser = false;
		
		if (isSetUp){
			
			int count = 0;
			Statement stmt = null;
		    String query = "SELECT COUNT(*) FROM users "
		    		+ "WHERE username LIKE '"+userName+"' "
    					+ "AND password LIKE '"+password+"'";
		    
		    try {
		        stmt = connection.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        		        
		        while (rs.next())
		        	  count = rs.getInt(1);
		        
		        if (count < 1)
		        	isUser = false;
	         	else if (count == 1)
		        	isUser = true;
		        		        
		    } catch (SQLException e ) {
		    	System.out.println("Connection: "+e.getMessage());
		    	isUser = false;
		    } finally {
		        if (stmt != null)
		        	stmt.close(); 	
		    }
		}
//		else {
//			setup();
//			isUser = checkUser(userName, password);
//		}
		return isUser;
	}
	
	public String[] getName(String userName, String password){
		
		System.out.println("In getName: "+userName);
		System.out.println("In getName: "+password);
		
		String[] name = new String[2];
		String first = "", last = "";
		
		Statement stmt = null;
	    String query = "SELECT * FROM users "
	    		+ "WHERE username LIKE '"+userName+"' "
					+ "AND password LIKE '"+password+"'";	    		    
	    try {
	    	if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        		        
	        while (rs.next()){
	        	first = rs.getString("first_name");
	        	last = rs.getString("last_name");
	        }
	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	
	    }
		name[0] = first;
		name[1] = last;
		return name;
	}

	public void newUser(String userName, String password){
				
		Statement stmt = null;
	    String query = "INSERT INTO users "
	    		+ "(username, password) VALUES ('"+userName+"' "
					+ ", '"+password+"')";	    		    
	    try {
	    	if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();
	        stmt.executeUpdate(query);
	        		        	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	
	    }
	}
	
	public int getID(String userName, String password){
		
		int userID = 0;
		Statement stmt = null;
	    String query = "SELECT ID FROM users "
	    		+ "WHERE username LIKE '"+userName+"' "
				+ "AND password LIKE '"+password+"'";	    		    
	    try {
	    	if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        
	        while(rs.next()){
	        	userID = rs.getInt("ID");
	        }
	        		        	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	
	    }
		
		return userID;
	}
	
	public void newCourse(int UserID, String courseSubject, String courseNumber,
			String courseSection) {
		
		Statement stmt = null;
	    String query = "INSERT INTO courses "
	    		+ "(ID, course_subject, catalog_number, section) "
	    		+ "VALUES ('"+UserID+"', '"+courseSubject+"', "
	    				+ "'"+courseNumber+"', '"+courseSection+"')";	    		    
	    try {
	    	if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();
	        stmt.executeUpdate(query);
	        		        	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	
	    }
	}
	
	public int howManyCourses(int userID){
	
		int count = 0;
		Statement stmt = null;
		String query = "SELECT COUNT(*) FROM courses WHERE ID LIKE "+userID+"";
		
		try {
			if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();	        
	        ResultSet rs = stmt.executeQuery(query);
	        
	        while (rs.next())
	        	count = rs.getInt(1);
	        	        		        	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	    }	
		return count;	
	}
	
	public Course[] getCourses(int userID){
		
		Course[] courses = new Course[howManyCourses(userID)];
		Statement stmt = null;
		String query = "SELECT * FROM courses WHERE ID LIKE "+userID+"";
		
		try {
			if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();	        
	        ResultSet rs = stmt.executeQuery(query);
	        
	        int i = 0;	        
	        while (rs.next()){
	        	
	        	String subject = rs.getString("course_subject");
	        	String catalog = rs.getString("catalog_number");
	        	String section = rs.getString("section");
	        	
	        	courses[i] = new Course(subject, catalog, section);
	        }
	        	        		        	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	
	    }	
		return courses;		
	}
	
	private void connectAgain(){
		Thread tread = new Thread(new Runnable() {
			@Override
			public void run() {
				connect();
			}
		});
	}
	
	@Override
	protected String doInBackground(String... params) {
		return null;
	}
}