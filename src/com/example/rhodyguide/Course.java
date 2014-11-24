package com.example.rhodyguide;

public class Course {
	
	private String subject, catalog, section;
	
	public Course(String subject, String catalog, String section){
		
		this.subject = subject;
		this.catalog = catalog;
		this.section = section;		
		
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
}
