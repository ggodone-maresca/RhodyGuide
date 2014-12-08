package com.example.rhodyguide;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class Server extends AsyncTask<String, Void, String> {
	
	private boolean isSetUp = false;
	private static Connection connection = null;
	
//	private static String url = "jdbc:mysql://abouthillier.vps.cs.uri.edu/abouthil_rhodyguide";
//	private static String username = "abouthil";
//	private static String password = "=Gqk0TOf*D]I";
	
//	private static String ipAddress = "172.20.104.99";
//	private static String ipAddress = "131.128.185.84";
//	private static String ipAddress = "192.185.94.206";
	private static String ipAddress = "192.168.0.15";
	private static String url = "jdbc:mysql://"+ipAddress+"/Test";
	private static String username = "root";
	private static String password = "";
		
	private Activity activity;
		
	public Server(Activity activity) {
		this.activity = activity;
		setup();
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
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void connect() {
		try {
		    System.out.println("Connecting database...");
		    connection = DriverManager.getConnection(url, username, password);
		    System.out.println("Database connected!");
			isSetUp = true;
		} catch (SQLException e) {	
			toast("Cannot connect to database");
			System.out.println(e);
		} 
	}
	
	private void toast(CharSequence someMessage){
		final CharSequence message = someMessage;
		activity.runOnUiThread(new Runnable() {
			  public void run() {
				  Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			  }
		});
	}
	
	public boolean checkUser(String userName, String password)
		    throws SQLException {	
		
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

	         	if (count == 1)
		        	isUser = true;
		        		        
		    } catch (SQLException e ) {
		    	isUser = false;
		    } finally {
		        if (stmt != null)
		        	stmt.close(); 	
		    }
		}
		return isUser;
	}
	
	public String[] getName(String userName, String password){
		
		
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
	        
	        while(rs.next())
	        	userID = rs.getInt("ID");
	        		        	        
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
	
	public void newCourse(int userID, String courseSubject, 
			String courseNumber, String courseSection,
			boolean M, boolean T, boolean W, boolean Th, boolean F,
			String startTime, String endTime,
			String building_name, String room_number) {
		
		int m = 0, t = 0, w = 0, th = 0, f = 0;
		
		if (M)
			m = 1;
		if (T)
			t = 1;
		if (W)
			w = 1;
		if (Th)
			th = 1;
		if (F)
			f = 1;
		
		Statement stmt = null;
	    String query = "INSERT INTO courses "
	    		+ "(userID, course_subject, catalog_number, course_section,"
	    		+ "M, T, W, Th, F, start_time, end_time, building, room_number) "
	    		+ "VALUES ('"+userID+"', '"+courseSubject+"', '"+courseNumber+"', '"+courseSection+"',"
	    		+ " '"+m+"', '"+t+"', '"+w+"', '"+th+"', '"+f+"', '"+startTime+"', "
	    		+ "'"+endTime+"', '"+building_name+"', '"+room_number+"')";
	    
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
		String query = "SELECT COUNT(*) FROM courses WHERE userID LIKE "+userID+"";
		
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
	
	public Building[] getCourseLocations(int userID){
		
		Building[] buildings = new Building[howManyCourses(userID)];
		Statement stmt = null;
		String query = "SELECT `buildings`.building, x, y FROM `buildings` "
				+ "INNER JOIN `courses` ON `buildings`.building = `courses`.building "
				+ "WHERE userID LIKE "+userID;
		
		try {
			if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();	        
	        ResultSet rs = stmt.executeQuery(query);
	        
	        int i = 0;	        
	        while (rs.next()){	  
	        	
	        	String building = rs.getString("building");
	        	double x = rs.getDouble("x");
	        	double y = rs.getDouble("y");
	        		        	
	        	buildings[i] = new Building(building, x, y);
	        	i++;	        
	        }
	        
		} catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 	
	    }
		return buildings;		
	}
	
	public Course[] getCourses(int userID){
		
		Course[] courses = new Course[howManyCourses(userID)];
		Statement stmt = null;
		String query = "SELECT * FROM courses WHERE userID LIKE "+userID+"";
		
		try {
			if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();	        
	        ResultSet rs = stmt.executeQuery(query);
	        
	        int i = 0;	        
	        while (rs.next()){
	        	
	        	String subject = rs.getString("course_subject");
	        	String catalog = rs.getString("catalog_number");
	        	String section = rs.getString("course_section");
	        	int M = rs.getInt("M");
	        	int T = rs.getInt("T");
	        	int W = rs.getInt("W");
	        	int Th = rs.getInt("Th");
	        	int F = rs.getInt("F");
	        	String start = rs.getString("start_time");
	        	String end = rs.getString("end_time");
	        	String building = rs.getString("building");
	        	String room = rs.getString("room_number");
	        	
	        	courses[i] = new Course(userID, subject, 
										catalog, section,
										M, T, W, Th, F, start, end,
										building, room);
	        	i++;
	        }
	        	        		        	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 	
	    }
		return courses;		
	}
	
	public int howManyBuildings(){
	
		int count = 0;
		Statement stmt = null;
		String query = "SELECT COUNT(*) FROM buildings";
		
		try {
			System.out.println("Connection: "+connection);
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
					e.printStackTrace();
				} 
	    }	
		return count;	
	}
	
	public Building[] pullBuildings(){
			
		Building[] buildings = new Building[howManyBuildings()];
		Statement stmt = null;
		String query = "SELECT * FROM buildings";
		
		try {
			if (connection == null)
	    		connectAgain();
	        stmt = connection.createStatement();	        
	        ResultSet rs = stmt.executeQuery(query);
	        
	        int i = 0;	        
	        while (rs.next()){
	        	
	        	String building = rs.getString("building");
	        	double x = rs.getDouble("x");
	        	double y = rs.getDouble("y");
	        		        	
	        	buildings[i] = new Building(building, x, y);
	        	i++;
	        }
	        	        		        	        
	    } catch (SQLException e ) {
	    	System.out.println(e.getMessage());
	    } finally {
	        if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 	
	    }	
		return buildings;		
	}
	
	public void connectAgain(){
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				connect();
			}
		});
		thread2.start();
	}
	
	@Override
	protected String doInBackground(String... params) {
		return null;
	}
}