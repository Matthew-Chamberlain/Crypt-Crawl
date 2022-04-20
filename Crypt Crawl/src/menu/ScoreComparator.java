package menu;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Scores> { // this class meets objective 13 // comparator is an example of an interface
	
	//this method is used to compare two values, a different value is returned depending on which value is bigger
	public int compare(Scores score1, Scores score2) {
		
		int sc1 = score1.getScore(); // gets the value of the first score by calling the get score method
		int sc2 = score2.getScore(); // gets the value of the second score by calling the get score method
		
		if(sc1 > sc2) { // checks whether score 1 is greater than score 2, if it is then -1 is returned 
			return -1;
		}
		else if(sc1 < sc2) { // checks whether score 2 is greater than score 1, it is is then 1 is returned 
			return 1;
		}
		else { // returns 0 if the two scores are equal to each other
			return 0;
		}
	}
}
