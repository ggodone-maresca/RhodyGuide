package com.example.rhodyguide;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLngBounds;
import android.os.Handler;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.CameraUpdate;
import android.os.Message;
import com.google.android.gms.maps.model.VisibleRegion;
 
public class FragmentMap extends Fragment {
	
	private GPSTracker mGPS;
	private LatLng here;
	private GoogleMap map;
	
	/**
	 * 
	 * All the map constraints are
	 * from http://stackoverflow.com/questions/14977078/limit-scrolling-and-zooming-google-maps-android-api-v2
	 * MINZOOM: The minimum zooming level
	 * NORTH/SOUTH/EAST/WEST: The map constraints
	 * mOverscrollHandler: Starts the overscroll handler
	 *
	 */
	public final int MINZOOM = 14;
	public final double SOUTH = 41.466344,
    			WEST = -71.569177,
    			NORTH = 41.511234,
    			EAST = -71.496689;;
	private OverscrollHandler mOverscrollHandler = new OverscrollHandler();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
		View view = inflater.inflate(R.layout.fragment_map, null, false);
    	setUpMapIfNeeded();

		return view;
    }
    
    public void setUpMapIfNeeded() {
    	
    	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    	
		mGPS = new GPSTracker(this.getActivity());	    
    	mGPS.getLocation();
    	
    	double x = mGPS.getLatitude();
    	double y = mGPS.getLongitude();
    	
    	if (x == 0 || y == 0){
    		x = 41.486427;
    		y = -71.530722;
    	}
    	
    	here = new LatLng(x, y);

    	// Opens the map at the URI Library
    	here = new LatLng(41.487352, -71.528847);
    	//here = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
    	
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 15));
        map.addMarker(new MarkerOptions()
                .title("You are here")
                .snippet("Rhody Guide")
                .position(here));
        try {
			showBuildings();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    
    private void showBuildings() throws InterruptedException, ExecutionException{
    	
//    	final CountDownLatch latch = new CountDownLatch(1);
//    	final Building[][] temp = new Building[1][];
//    	Thread thread2 = new Thread(new Runnable() {
//			@Override
//			public void run() {		
//		    	
//		    	Server server = new Server();
//		    	server.connect();
//		    	
//		    	temp[0] = new Building[server.howManyBuildings()];
//		    	temp[0] = server.pullBuildings();
//		    	for (int i=0; i< temp[0].length; i++){
//					System.out.println("Building: "+temp[0][i]);
//		    	}
//
//		    	latch.countDown();
//			}
//		});
//		thread2.start();
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	 
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
	    		    	
    	for (int pin=0; pin<buildings.length; pin++) {
    		
    		double x = buildings[pin].getX();
    		double y = buildings[pin].getY();
    		
            LatLng pinLocation = new LatLng(x, y);
            map.addMarker(new MarkerOptions()
	            .position(pinLocation)
	            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue))
	            .title(buildings[pin].getBuilding())
	            .snippet("")
            );
        }    	
    }
            
        // Starts the overscroll handler
//        mOverscrollHandler.sendEmptyMessageDelayed(0,100);
 //   }
    
    /**
     * Returns the correction for Lat and Lng if camera is trying to get outside of visible map
     * @param cameraBounds Current camera bounds
     * @return Latitude and Longitude corrections to get back into bounds.
     */
    private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
        double latitude=0.0,
        		longitude=0.0;
        // If the camera is too south
        if(cameraBounds.southwest.latitude < SOUTH) {
            latitude = SOUTH - cameraBounds.southwest.latitude;
        }
        
        // If the camera is too west
        if(cameraBounds.southwest.longitude < WEST) {
            longitude = WEST - cameraBounds.southwest.longitude;
        }
        
        // If the camera is too north
        if(cameraBounds.northeast.latitude > NORTH) {
            latitude = NORTH - cameraBounds.northeast.latitude;
        }
        
        // If the camera is too east
        if(cameraBounds.northeast.longitude > EAST) {
            longitude = EAST - cameraBounds.northeast.longitude;
        }
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
            if(position.zoom < MINZOOM) zoom = MINZOOM;
            else if(position.zoom < MINZOOM - 2) zoom--;
            LatLng correction = getLatLngCorrection(region.latLngBounds);
            if(zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
                zoom = (zoom==0)?position.zoom:zoom;
                double lat = position.target.latitude + correction.latitude;
                double lon = position.target.longitude + correction.longitude;
                CameraPosition newPosition = new CameraPosition(new LatLng(lat,lon), zoom, position.tilt, position.bearing);
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
                map.moveCamera(update);
            }
            /* Recursively call handler every 100ms */
            sendEmptyMessageDelayed(0,100);
        }
    }
}