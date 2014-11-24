package com.example.rhodyguide;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	public final static String FIRST = "com.example.rhodyguide.FIRST";
	public final static String LAST = "com.example.rhodyguide.LAST";
	public final static String USERID = "com.example.rhodyguide.USERID";

	private String login, password;
	
	private Server server;
	
	private Thread thread;
	
	private final Activity activity = this;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
               
        getActionBar().hide();
    }
    
    /** Called when the user clicks the Submit button */
    public void submitUser(View v) {
        
    	login = getUser();
    	password = getPassword();
    	
    	final Intent intent = new Intent(this, MapActivity.class);
    	
    	final Activity activity = this;
    	    	    	
    	thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		    	
		        server = new Server(activity);		    	
		    	server.connect();
		    	
		    	String[] name = new String[2];

		    	try {
		    		if (server.checkUser(login, password)){
		    			
		    			toast("User Valid");
		    			
		    			int userID = server.getID(login, password);
		    			
		    			name = server.getName(login, password);
		    			
		    	    	intent.putExtra("USERID", userID);
		    	    	intent.putExtra("FIRST", name[0]);
		    	    	intent.putExtra("LAST", name[1]);
		    	    	startActivity(intent);
		    		}
		    		else
		    			toast("User Invalid");
		    		
		    	} catch(SQLException e){
		    		e.printStackTrace();
		    	}
		    }
		});
		thread.start(); 
    }
    
    private void toast(CharSequence someMessage){
    	final CharSequence message = someMessage;
    	activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});
    }
    
    /** Called when the user clicks the Register button */
    public void registerUser(View v) {
    	
    	login = getUser();
    	password = getPassword();
    	
    	thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		    	server = new Server(activity);		    	
		    	server.connect();
		    	server.newUser(login, password);
		    }
    	});
    }    
    
    /** Called when the user clicks the Guest button */
    public void guestUser(View v) {
    	    	
    	Intent intent = new Intent(this, MapActivity.class);
    	startActivity(intent);
    }    
    
    private String getUser() {
    	return ((EditText)findViewById(R.id.user)).getText().toString();
    }
    
    private String getPassword() {
    	return ((EditText)findViewById(R.id.pass)).getText().toString();
    }
    
}
