package com.example.rhodyguide;

/**
 * Defines a building on campus
 * 
 * @author Christopher Glasz
 */
public class Building {

	/**
	 * Building name
	 */
	private String building;

	/**
	 * Coordinates for building
	 */
	private double x, y;

	/**
	 * Constructor initializes variables
	 * 
	 * @param building
	 * @param x
	 * @param y
	 */
	public Building(String building, double x, double y) {

		this.building = building;
		this.x = x;
		this.y = y;

	}

	/**
	 * Accessor method for the building name
	 * 
	 * @return building name
	 */
	public String getBuilding() {
		return building;
	}

	/**
	 * Mutator method for the building name
	 * 
	 * @param building
	 *            building name
	 */
	public void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * Accessor method for x
	 * 
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Mutator method for x
	 * 
	 * @param x
	 *            x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Accessor method for y
	 * 
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Mutator method for y
	 * 
	 * @param y
	 *            y
	 */
	public void setY(double y) {
		this.y = y;
	}

}
