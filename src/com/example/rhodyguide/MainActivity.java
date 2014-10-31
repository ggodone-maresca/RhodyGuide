package com.example.rhodyguide;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class MainActivity extends Activity {

	private String login, password;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .commit();
        }
    }
    
    /** Called when the user clicks the Submit button */
    public void submitUser(View view) {
        
    	login = getUser();
    	password = getPassword();
    	
    }    
    
    /** Called when the user clicks the Register button */
    public void registerUser(View view) {
    	
    	login = getUser();
    	password = getPassword();
    }    
    
    /** Called when the user clicks the Guest button */
    public void guestUser(View view) {
    	
    	login = getUser();
    	password = getPassword();
    }    
    
    private String getUser() {
    	return ((EditText)findViewById(R.id.user)).getText().toString();
    }
    
    private String getPassword() {
    	return ((EditText)findViewById(R.id.pass)).getText().toString();
    }
    

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
