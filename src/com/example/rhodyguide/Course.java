package com.example.rhodyguide;

/**
 * Defines a course
 * 
 * @author Christopher Glasz
 */
public class Course {

	/**
	 * Strings representing fields
	 */
	private String subject, catalog, section, start, end, building, room,
			M = "", T = "", W = "", Th = "", F = "";

	/**
	 * Constructor sets fields
	 * 
	 * @param userID
	 *            userID
	 * @param subject
	 *            course subject (CSC, EGR, etc.)
	 * @param catalog
	 *            course number (100, 440, 305, etc.)
	 * @param section
	 *            section number (0001, 0003, etc.)
	 * @param M
	 *            monday flag
	 * @param T
	 *            tuesday flag
	 * @param W
	 *            wednesday flag
	 * @param Th
	 *            thursday flag
	 * @param F
	 *            friday flag
	 * @param start
	 *            start time
	 * @param end
	 *            end time
	 * @param building
	 *            building name
	 * @param room
	 *            room number
	 */
	public Course(int userID, String subject, String catalog, String section,
			int M, int T, int W, int Th, int F, String start, String end,
			String building, String room) {

		// class code
		this.subject = subject;
		this.catalog = catalog;
		this.section = section;

		// Sets day flags
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

		// time
		this.start = start;
		this.end = end;

		// location
		this.building = building;
		this.room = room;
	}

	/**
	 * Returns concatenated string of days of the format 'MWF' or 'TTh' etc
	 * 
	 * @return days
	 */
	public String getDays() {
		return M + T + W + Th + F;
	}

	/**
	 * Returns string for location of the format 'building Room: room'
	 * 
	 * @return location
	 */
	public String getLocation() {
		return building + " Room: " + room;
	}

	/**
	 * Returns string for courses of the format 'subject number section'
	 * 
	 * @return course
	 */
	public String getCourse() {
		return subject + " " + catalog + "_" + section;
	}

	/**
	 * Returns string for time of the format 'start - end'
	 * 
	 * @return time
	 */
	public String getTimes() {
		return start + " - " + end;
	}
}
