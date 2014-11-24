package com.example.rhodyguide;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditScheduleFragment extends Fragment implements OnClickListener{
	
	private int userID;
	private Server server;
	private View rootView;
	private LinearLayout ll;
	private Spinner buildingList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		 
        rootView = inflater.inflate(R.layout.fragment_edit_schedule, container, false);
        
        ImageButton button = (ImageButton) rootView.findViewById(R.id.add);
        button.setOnClickListener(this);
        
        ll = (LinearLayout) rootView.findViewById(R.id.textViews);
        
        userID = ((MapActivity) getActivity()).getUserID();
		server = new Server(getActivity());
        
        try {
			printCourses();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return rootView;
    }
	
	private void printCourses() throws InterruptedException, ExecutionException{
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Course[]> callable = new Callable<Course[]>() {
			@Override
			public Course[] call() {
				server.connect();
				return server.getCourses(userID);	
			}
		};
		Future<Course[]> future = executor.submit(callable);
		Course[] courses = future.get();
		executor.shutdown();
		
		final int N = courses.length; // Total number of TextViews to add

		final TextView[] myTextViews = new TextView[N]; // create an empty array;
		
		for (int i = 0; i < N; i++) {
		    // Create a new TextView
		    final TextView rowTextView = new TextView(getActivity());
		    
			String subject = courses[i].getSubject();
			String catalog = courses[i].getCatalog();
			String section = courses[i].getSection();

		    // Set some properties of rowTextView or something
		    rowTextView.setText(Html.fromHtml("<b>"+subject+" "+catalog+"</b> "+section));
		    rowTextView.setHeight(50);

		    // Add the TextView to the LinearLayout
		    ll.addView(rowTextView);

		    // Save a reference to the TextView for later
		    myTextViews[i] = rowTextView;
		}
	}
	
	private void clearCourses() {
		if (((LinearLayout) ll).getChildCount() > 0) 
		    ((LinearLayout) ll).removeAllViews();
	}
		
	private void edit(View v){
				
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    View view = inflater.inflate(R.layout.addclass, null);

		alert.setTitle("Add a new Course");
		alert.setView(view);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				Dialog f = (Dialog) dialog;
				
				// Widgets
				EditText subject = (EditText) f.findViewById(R.id.subject);
				EditText catalog = (EditText) f.findViewById(R.id.catalog);
				EditText section = (EditText) f.findViewById(R.id.section);
				
				CheckBox Monday = (CheckBox) f.findViewById(R.id.m);
				CheckBox Tuesday = (CheckBox) f.findViewById(R.id.t);
				CheckBox Wednesday = (CheckBox) f.findViewById(R.id.w);
				CheckBox Thursday = (CheckBox) f.findViewById(R.id.th);
				CheckBox Friday = (CheckBox) f.findViewById(R.id.f);
				
				EditText startTime = (EditText) f.findViewById(R.id.startSet);
				EditText endTime = (EditText) f.findViewById(R.id.endSet);
				
				Spinner building = (Spinner) f.findViewById(R.id.building_name);
				EditText room = (EditText) f.findViewById(R.id.room_number);
				 
				// Variables from Widgets
				final String courseSubject = subject.getText().toString();
				final String courseNumber = catalog.getText().toString();	
				final String courseSection = section.getText().toString();
				
				final boolean M = Monday.isChecked();
				final boolean T = Tuesday.isChecked();
				final boolean W = Wednesday.isChecked();
				final boolean Th = Thursday.isChecked();
				final boolean F = Friday.isChecked();
				
				final String start = startTime.getText().toString();
				final String end = startTime.getText().toString();
				
				final String building_name = building.getSelectedItem().toString();
				final String room_number = room.getText().toString();

				Thread thread = new Thread(new Runnable(){
					public void run() {
						server.connect();
						server.newCourse(userID, courseSubject, 
										courseNumber, courseSection,
										M, T, W, Th, F, start, end,
										building_name, room_number);
					}
				});
				thread.start();
				
				try {
					clearCourses();
					printCourses();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		alert.show();
		
        buildingList = (Spinner) view.findViewById(R.id.building_name);
        
		try {
			addItemsOnSpinner();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void addItemsOnSpinner() throws InterruptedException, ExecutionException {
 		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Building[]> callable = new Callable<Building[]>() {
			@Override
			public Building[] call() {
				server.connect();
				return server.pullBuildings();	
			}
		};
		Future<Building[]> future = executor.submit(callable);
		Building[] buildings = future.get();
		executor.shutdown();
		
		String[] list = new String[buildings.length];
		
		for (int i = 0; i < buildings.length; i++)
			list[i] = buildings[i].getBuilding();
				
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
			android.R.layout.simple_spinner_item, list);
		
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingList.setAdapter(dataAdapter);	
  	}

	@Override
	public void onClick(View v) {
		edit(v);
	}
}
