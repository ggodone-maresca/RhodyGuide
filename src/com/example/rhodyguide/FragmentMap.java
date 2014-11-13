package com.example.rhodyguide;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
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
    	
    	here = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
    	
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 20));
        map.addMarker(new MarkerOptions()
                .title("You are here")
                .snippet("This motherfucker works")
                .position(here));
    }   
}