package com.example.rhodyguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.os.Build;

public class Login extends Activity {

	private String login, password;
	
	private Server server;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        server = new Server();
        
        getActionBar().hide();
    }
    
    /** Called when the user clicks the Submit button */
    public void submitUser(View v) {
        
    	login = getUser();
    	password = getPassword();
    	
    	Log.e("Clicked", "Submit Clicked");
    }    
    
    /** Called when the user clicks the Register button */
    public void registerUser(View v) {
    	
    	login = getUser();
    	password = getPassword();
    	
    	Log.e("Clicked", "Register Clicked");
    }    
    
    /** Called when the user clicks the Guest button */
    public void guestUser(View v) {
    	
    	Log.e("Clicked", "Guest Clicked");
    	
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
