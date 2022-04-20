package menu;

import java.util.*;
import java.io.*;

public class HighScoreManager { // this class meets objective 13 // this class has example of modules with common purposes grouped
	
	private ArrayList<Scores> scores; // creates a private Scores array list  
	
	private static final String fileName = "High Scores.dat"; //creates a final string which is the name of the file used to store high scores // this is an example of a parametrised file path // this is an example of a constant
	
	ObjectOutputStream outputStream = null; // creates an object output stream and sets it to null, an object output stream is used to write java objects to an output stream as opposed to raw bytes
	ObjectInputStream inputStream = null; // creates an object input stream and sets it to null, an object input stream is used to read java objects from an input stream instead of raw bytes.

	// this method is the constructor for high score manager class, it is used to initialise the scores array list
	public HighScoreManager() {
		scores = new ArrayList<Scores>(); // initialises score array list
	}
	
	// this method is to get the scores from the file, first the load score file method is run to read the file then the sort method is used to get them in order from the highest score to the lowest, then the array list is returned
	public ArrayList<Scores> getScores(){
		loadScoreFile(); // calls load score file method
		sort(); // calls the sort method
		return scores; // returns the sorted array list of scores
	}
	 // this method is used to sort the scores in order from biggest score to smallest
	private void sort() {
		ScoreComparator comparator = new ScoreComparator(); // creates an instance of the score comparator class
		Collections.sort(scores, comparator); //this is a function that will sort the array list scores with the help of the comparator object
	}
	
	// this method takes in the data that needs to be saved into files and calls the methods necessary to put this information into the file, it is parsed the name  of the player, their score and the floor they reached
	public void addScore(String name, int score, int floor) {
		loadScoreFile(); // calls the load score file method
		scores.add(new Scores(name, score, floor)); // adds a new element to the scores array by calling the scores constructor, parsing the name score and floor of the player
		updateScoreFile(); // calls the update score file method
	}
	
	// this method is responsible for reading the file that contains the high scores and storing them in an array list, // this meets objective 13
	public void loadScoreFile() {
		// a try catch block is used here to catch any errors that might arise from reading a file
		try {
			inputStream = new ObjectInputStream(new FileInputStream(fileName)); // reads java objects from the file and stores them in an input stream
			scores = (ArrayList<Scores>) inputStream.readObject(); // reads the objects in the input stream and casts them  to an array list of scores // example of reading information from a file
		}
		//this catch block is used to catch an error that would occur if the file is not found
		catch (FileNotFoundException e) { // this is an example of good exception handling 
         System.out.println(e);
		}
		//this catch block is used to catch an error that would occur when the data is inputed into the system
		catch (IOException e) {
         System.out.println(e);
		}
		// this catch block is used when a class is being loaded and the class cannot be found
		catch (ClassNotFoundException e) {
         System.out.println(e);
		} 
		// the finally block always executes when the try block ends
		finally {
			try {
				if (outputStream != null) { // this code runs if the contents of the output stream are not null 
						outputStream.flush(); // clears out the output stream of any data
						outputStream.close(); // closes the output stream
				}
			} 
			// this catch block is used to catch any errors caused by inputs or outputs
			catch (IOException e) { // this is an example of good exception handling
				System.out.println(e);
			}
		}
	}
	
	// this class is used to write data to the score file // this meets objective 13
	public void updateScoreFile() {
		// a try catch block has been used here to catch any errors that may occur due to outputing data from the system to the file
		 try {
			 outputStream = new ObjectOutputStream(new FileOutputStream(fileName)); //creates an output stream to the file
			 outputStream.writeObject(scores); // writes the scores array list to the file // example of writing data to a tesxt file
		// this catch block is used to catch an error when the file that is being written to cannot be found
		 } catch (FileNotFoundException e) { // this is an example of good exception handling
			 System.out.println(e);
		// this catch block is used to catch an error when the data is being written to the file
		 } catch (IOException e) {
			 System.out.println(e);
		 } finally { // the finally block always runs when the try block ends
			 try { 
				 if (outputStream != null) { // the code runs when the output stream is not null
					 	outputStream.flush(); // clears any data from the stream
					 	outputStream.close(); // closes the stream
				 }
			// catches any input/output errors	 
			 } catch (IOException e) { // this is an example of good exception handling 
				 System.out.println("[Update] Error: " + e.getMessage());
			 }
		 }
	}
	
	//this method is used to split the array list of scores into individual score strings, the scores are then put into a list of strings and returned so that they can be displayed
	public List<String> getHighScoreString() { // this meets objective 13
		List<String> highscoreString = new ArrayList<String>(); // creates a string list
		String score; // creates a string variable

		//ArrayList<Scores> scores; // creates an array list of scores
		scores = getScores(); // calls the get score method and stores the returned values in the scores array list

		// loops through all the elements in the list
		for(int i = 0; i < scores.size(); i++) {
			score = (i + 1) + ".\t" + scores.get(i).getName() + "\t\t Score: " + scores.get(i).getScore() + "\t Floor: " + scores.get(i).getFloor() +  "\n"; // the string variable for the score contains the score, name and floor of each element in the list
	        highscoreString.add(score) ; // adds the string to the list	
		}
		return highscoreString; // returns the list
	}
}