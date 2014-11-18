package com.example.rhodyguide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditScheduleFragment extends Fragment implements OnClickListener{
	
	
	private int userID;
	private final Server server = new Server(getActivity());
	private Course[] courses;
		
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_edit_schedule, container, false);
        
        ImageButton button = (ImageButton) rootView.findViewById(R.id.add);
        
        button.setOnClickListener(this);
        
        userID = ((MapActivity)getActivity()).getUserID();
        
        courses = server.getCourses(userID);
        
        printCourses();
        
        return rootView;
    }
	
	private void printCourses(){
		
		int howMany = courses.length;
		
		for (int i = 0; i < howMany; i++){
			
			String subject = courses[i].getSubject();
			String catalog = courses[i].getCatalog();
			String section = courses[i].getSection();
			
			TextView box = new TextView(getActivity());
			box.setText(Html.fromHtml("<b>"+subject+" "+catalog+"</b> "+section));	
		}
	}
		
	private void edit(View v){
				
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		
	    LayoutInflater inflater = getActivity().getLayoutInflater();

		alert.setTitle("Title");
		alert.setMessage("Message");

		alert.setView(inflater.inflate(R.layout.addclass, null));

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				 Dialog f = (Dialog) dialog;
                 // This is the input I can't get text from
                 EditText subject = (EditText) f.findViewById(R.id.subject);
                 EditText catalog = (EditText) f.findViewById(R.id.catalog);
                 EditText section = (EditText) f.findViewById(R.id.section);
                 
                 String courseSubject = subject.getText().toString();
                 String courseNumber = catalog.getText().toString();	
                 String courseSection = section.getText().toString();	
				
                 server.newCourse(userID, courseSubject, courseNumber, courseSection);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});

		alert.show();
	}

	@Override
	public void onClick(View v) {
		edit(v);
	}
}
