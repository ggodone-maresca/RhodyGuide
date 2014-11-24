package com.example.rhodyguide;

public class Building {
	
	private String building;
	private double x, y;

	public Building(String building, double x, double y){
		
		this.building = building;
		this.x = x;
		this.y = y;
		
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	

}
