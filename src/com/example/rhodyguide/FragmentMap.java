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
 
public class FragmentMap extends Fragment {
	
	private GPSTracker mGPS;
	private LatLng here;
	private GoogleMap map;
    
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
    	
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 20));
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
    
}