package com.example.rhodyguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Defines a GPS Tracker
 * 
 * @author Christopher Glasz
 */
public final class GPSTracker implements LocationListener {

	/**
	 * The context
	 */
	private final Context mContext;

	/**
	 * Flag for GPS status
	 */
	public boolean isGPSEnabled = false;

	/**
	 * Flag for network status
	 */
	boolean isNetworkEnabled = false;

	/**
	 * flag for GPS status
	 */
	boolean canGetLocation = false;

	/**
	 * Location
	 */
	Location location;

	/**
	 * Coordinates
	 */
	double latitude, longitude;

	/**
	 * The minimum distance to change Updates in meters
	 */
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

	/**
	 * The minimum time between updates in milliseconds
	 */
	private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

	/**
	 * Declaring a Location Manager
	 */
	protected LocationManager locationManager;

	/**
	 * Constructor initializes variables and gets the location
	 * 
	 * @param context
	 *            the context
	 */
	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	/**
	 * Accessor method for the user's current location
	 * 
	 * @return the user's current location
	 */
	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v("isGPSEnabled", "=" + isGPSEnabled);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

			if (isGPSEnabled == false && isNetworkEnabled == false) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					location = null;
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					location = null;
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener Calling this function will stop the app using GPS
	 */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Accessor method for latitude
	 * 
	 * @return latitude
	 */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Accessor method for longitude
	 * 
	 * @return longitude
	 */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return true if GPS/wifi is enabled
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Method to call upon location change
	 * 
	 * @param location
	 *            new location
	 */
	public void onLocationChanged(Location location) {
	}

	/**
	 * Method to call upon provider disable
	 * 
	 * @param provider
	 *            the provider
	 */
	public void onProviderDisabled(String provider) {
		Toast.makeText(mContext, "GPS Disabled", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Method to call upon provider enable
	 * 
	 * @param provider
	 *            the provider
	 */
	public void onProviderEnabled(String provider) {
	}

	/**
	 * Method to call upon statuc change
	 * 
	 * @param provider
	 *            the provider
	 * @param status
	 *            the statuc
	 * @param extras
	 *            the bundle
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
