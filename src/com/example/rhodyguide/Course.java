package com.example.rhodyguide;

public class Course {
	
	/**
	 * Strings
	 */
	private String subject, catalog, section, start, end, building, room, 
					M = "", T = "", W = "", Th = "", F = "";
	
	public Course(int userID, String subject, String catalog, String section,
					int M, int T, int W, int Th, int F, String start, String end,
					String building, String room){
		
		this.subject = subject;
		this.catalog = catalog;
		this.section = section;
		
		if (M == 1)
			this.M = "M";
		if (T == 1)
			this.T = "T";
		if (W == 1)
			this.W = "W";
		if (Th == 1)
			this.Th = "Th";
		if (F == 1)
			this.F = "F";
		
		this.start = start;
		this.end = end;
		this.building = building;
		this.room = room;	
	}
	
	public String getDays() {
		return M+T+W+Th+F;
	}
	
	public String getLocation() {
		return building+" Room: "+room;
	}
	
	public String getCourse(){
		return subject+" "+catalog+"_"+section;
	}
	
	public String getTimes(){
		return start+" - "+end;
	}
}
