package com.example.rhodyguide;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Defines login activity
 * 
 * @author Christopher Glasz
 */
public class Login extends Activity {

	/**
	 * First name
	 */
	public final static String FIRST = "com.example.rhodyguide.FIRST";

	/**
	 * Last name
	 */
	public final static String LAST = "com.example.rhodyguide.LAST";

	/**
	 * User ID
	 */
	public final static String USERID = "com.example.rhodyguide.USERID";

	/**
	 * Login credentials
	 */
	private String login, password;

	/**
	 * The server
	 */
	private Server server;

	/**
	 * Thread we're running on
	 */
	private Thread thread;

	/**
	 * This activity
	 */
	private final Activity activity = this;

	/**
	 * Method to call on create
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().hide();
	}

	/**
	 * Called when the user clicks the Submit button
	 * 
	 * @param v
	 *            the view
	 */
	public void submitUser(View v) {

		login = getUser();
		password = getPassword();

		final Intent intent = new Intent(this, MapActivity.class);

		final Activity activity = this;

		thread = new Thread(new Runnable() {
			@Override
			public void run() {

				server = new Server(activity);
				server.connect();

				String[] name = new String[2];

				try {
					if (server.checkUser(login, password)) {

						toast("User Valid");

						int userID = server.getID(login, password);

						name = server.getName(login, password);

						intent.putExtra("USERID", userID);
						intent.putExtra("FIRST", name[0]);
						intent.putExtra("LAST", name[1]);
						startActivity(intent);
					} else
						toast("User Invalid");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/**
	 * Alert message
	 * 
	 * @param someMessage
	 *            message to display
	 */
	private void toast(CharSequence someMessage) {
		final CharSequence message = someMessage;
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Called when the user clicks the Register button
	 * 
	 * @param v
	 *            the view
	 */
	public void registerUser(View v) {

		login = getUser();
		password = getPassword();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				server = new Server(activity);
				server.connect();
				server.newUser(login, password);
			}
		});
	}

	/**
	 * Called when the user clicks the Guest button
	 * 
	 * @param v
	 *            the view
	 */
	public void guestUser(View v) {

		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

	/**
	 * Input the user
	 * 
	 * @return the user
	 */
	private String getUser() {
		return ((EditText) findViewById(R.id.user)).getText().toString();
	}

	/**
	 * Input the password
	 * 
	 * @return the password
	 */
	private String getPassword() {
		return ((EditText) findViewById(R.id.pass)).getText().toString();
	}

}
