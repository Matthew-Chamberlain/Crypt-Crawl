package menu;

import java.io.Serializable;

public class Scores implements Serializable{ // serializable has been implemented in order to sort an array list of the type Scores // this class meets objective 13

	private int score; // creates a private  integer variable for the score
	private String name; // creates a private string variable for the name
	private int floor; // creates a private integer variable for the floor number
	
	//the constructor for the scores class is used to set the variables for the score, name and floor number equal to the ones parsed in.
	public Scores(String name, int score, int floor) {
		this.score = score; // sets the score variable equal to the one parsed in
		this.name = name; // sets the name variable equal to the one parsed in
		this.floor = floor; // sets the floor number variable equal to the one that was parsed in
	}
	// this method returns the value of the score variable when called
	public int getScore() {
		return score;
	}
	// this method returns the value of the floor number variable when called
	public int getFloor() {
		return floor;
	}
	// this method returns the value of the floor number variable when called
	public String getName() {
		return name;
	}
}
