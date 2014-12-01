package com.example.rhodyguide;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.android.gms.maps.model.LatLngBounds;

import android.os.Handler;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.CameraUpdate;

import android.os.Message;

import com.google.android.gms.maps.model.VisibleRegion;

/**
 * Defines a fragment map
 * 
 * @author Christopher Glasz
 */
public class FragmentMap extends Fragment {

	/**
	 * GPS
	 */
	private GPSTracker mGPS;

	/**
	 * Coordinates
	 */
	private LatLng here;

	/**
	 * Map
	 */
	private GoogleMap map;

	/**
	 * The root view
	 */
	private View rootView;

	/**
	 * All the map constraints are from
	 * http://stackoverflow.com/questions/14977078
	 * /limit-scrolling-and-zooming-google-maps-android-api-v2
	 * 
	 * The minimum zooming level
	 */
	private final int MINZOOM = 14;

	/**
	 * The map constraints
	 */
	private final double SOUTH = 41.466344, WEST = -71.569177,
			NORTH = 41.511234, EAST = -71.496689;
	/**
	 * Starts the overscroll handler
	 */
	private OverscrollHandler mOverscrollHandler = new OverscrollHandler();

	/**
	 * Method to be called upon create
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView != null) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null)
				parent.removeView(rootView);
		}
		try {
			rootView = inflater
					.inflate(R.layout.fragment_map, container, false);
		} catch (InflateException e) {
			/* map is already there, just return view as it is */
		}

		setUpMapIfNeeded();
		setupControls();

		mOverscrollHandler.sendEmptyMessageDelayed(0, 100);

		return rootView;
	}

	/**
	 * If needed, set up the map
	 */
	public void setUpMapIfNeeded() {

		if (map == null)
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();

		mGPS = new GPSTracker(this.getActivity());
		mGPS.getLocation();

		double x = mGPS.getLatitude();
		double y = mGPS.getLongitude();

		if (x == 0 || y == 0) {
			x = 41.486427;
			y = -71.530722;
		}

		here = new LatLng(x, y);

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 15));
		map.addMarker(new MarkerOptions().title("You are here").position(here));
	}

	/**
	 * Sets up the controls
	 */
	private void setupControls() {

		RadioGroup rgViews = (RadioGroup) rootView.findViewById(R.id.rg_views);
		rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_normal)
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				else if (checkedId == R.id.rb_satellite)
					map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				else if (checkedId == R.id.rb_terrain)
					map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			}
		});

		RadioGroup rgBuildings = (RadioGroup) rootView
				.findViewById(R.id.rg_buildings);
		rgBuildings.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_none)
					clearBuildings();
				else if (checkedId == R.id.rb_all) {
					try {
						clearBuildings();
						showBuildings();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (checkedId == R.id.rb_relevant) {
					try {
						clearBuildings();
						showClasses();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Clears the buildings from the map
	 */
	private void clearBuildings() {
		map.clear();
	}

	/**
	 * Shows all buildings
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private void showBuildings() throws InterruptedException,
			ExecutionException {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Building[]> callable = new Callable<Building[]>() {
			@Override
			public Building[] call() {

				Server server = new Server(getActivity());
				server.connect();
				return server.pullBuildings();
			}
		};

		Future<Building[]> future = executor.submit(callable);
		Building[] buildings = future.get();
		executor.shutdown();

		for (int pin = 0; pin < buildings.length; pin++) {

			double x = buildings[pin].getX();
			double y = buildings[pin].getY();

			LatLng pinLocation = new LatLng(x, y);
			map.addMarker(new MarkerOptions().position(pinLocation)
					.title(buildings[pin].getBuilding()).snippet(""));
		}
	}

	/**
	 * Shows all classes
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private void showClasses() throws InterruptedException, ExecutionException {

		final int userID = ((MapActivity) getActivity()).getUserID();

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Building[]> callable = new Callable<Building[]>() {
			@Override
			public Building[] call() {
				Server server = new Server(getActivity());
				server.connect();
				return server.getCourseLocations(userID);
			}
		};

		Future<Building[]> future = executor.submit(callable);
		Building[] buildings = future.get();
		executor.shutdown();

		for (int i = 0; i < buildings.length; i++) {

			LatLng pinLocation = new LatLng(buildings[i].getX(),
					buildings[i].getY());
			map.addMarker(new MarkerOptions().position(pinLocation).title(
					buildings[i].getBuilding()));
		}
	}

	/**
	 * Returns the correction for Lat and Lng if camera is trying to get outside
	 * of visible map
	 * 
	 * @param cameraBounds
	 *            Current camera bounds
	 * @return Latitude and Longitude corrections to get back into bounds.
	 */
	private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
		double latitude = 0.0, longitude = 0.0;

		// If the camera is too south
		if (cameraBounds.southwest.latitude < SOUTH)
			latitude = SOUTH - cameraBounds.southwest.latitude;

		// If the camera is too west
		if (cameraBounds.southwest.longitude < WEST)
			longitude = WEST - cameraBounds.southwest.longitude;

		// If the camera is too north
		if (cameraBounds.northeast.latitude > NORTH)
			latitude = NORTH - cameraBounds.northeast.latitude;

		// If the camera is too east
		if (cameraBounds.northeast.longitude > EAST)
			longitude = EAST - cameraBounds.northeast.longitude;
		return new LatLng(latitude, longitude);
	}

	/**
	 * Bounds the user to URI.
	 */
	private class OverscrollHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			CameraPosition position = map.getCameraPosition();
			VisibleRegion region = map.getProjection().getVisibleRegion();
			float zoom = 0;
			if (position.zoom < MINZOOM)
				zoom = MINZOOM;
			else if (position.zoom < MINZOOM - 2)
				zoom--;
			LatLng correction = getLatLngCorrection(region.latLngBounds);
			if (zoom != 0 || correction.latitude != 0
					|| correction.longitude != 0) {
				zoom = (zoom == 0) ? position.zoom : zoom;
				double lat = position.target.latitude + correction.latitude;
				double lon = position.target.longitude + correction.longitude;
				CameraPosition newPosition = new CameraPosition(new LatLng(lat,
						lon), zoom, position.tilt, position.bearing);
				CameraUpdate update = CameraUpdateFactory
						.newCameraPosition(newPosition);
				map.moveCamera(update);
			}
			/* Recursively call handler every 100ms */
			sendEmptyMessageDelayed(0, 100);

		}
	}

	/**
	 * Method to be called on destroy
	 */
	public void onDestroyView() {
		super.onDestroyView();
		Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
		FragmentTransaction ft = getActivity().getFragmentManager()
				.beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}
}