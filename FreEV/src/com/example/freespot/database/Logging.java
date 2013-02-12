package com.example.freespot.database;

public class Logging {
	  private long id;
	  private String name;
	  private int totalCosts;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  public int getTotalCosts() {
		    return totalCosts;
		  }

	  public void setTotalCosts(int totalCosts) {
	    this.totalCosts = totalCosts;
	  }
	  

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return name;
	  }
	} 