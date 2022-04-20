package menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HighScores { // this class helps meet objective 1
	// This method is responsible for creating the frame and panel for the high score screen
	// This method is parsed the JFrame from the main menu
	public HighScores(JFrame menuFrame) {
		JFrame scoresFrame = new JFrame("HighScores"); // creates and the window for the high score screen and sets its name
		scoresFrame.setSize(500,600); // sets the size of the window
        scoresFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ensures the window is terminated when it is closed
        scoresFrame.setResizable(false); // ensures that the window cannot be resized so all the components stay in the right place.
        scoresFrame.setLocationRelativeTo(null); // makes the window open in the centre of the screen.
        
        JPanel scoresPanel = new JPanel();   // creates the panel for the high score screen
        scoresFrame.add(scoresPanel); // adds the panel to the frame
        
        scoresFrame.setVisible(true); // sets the frame to be visible 
        
        placeComponents(scoresFrame, menuFrame, scoresPanel); // calls the place components methods parsing in scores frame, the scores panel and the frame from the main menu so it can be set to visible later.
	}
    
	// This method is responsible for adding any components such as buttons to the panel and also adding the text of players scores and names.
	// the parameters of this method are the score frame and the score panel so that things components can be added to the panel, main menu frame can be set to visible and the current frame can be set to invisible.
    public void placeComponents(JFrame scoresFrame, JFrame menuFrame, JPanel scoresPanel) {
    	scoresPanel.setLayout(null); // this makes it so the components can be placed any where on the frame instead of in a specific way according to a premade layout.
    	HighScoreManager highScores = new HighScoreManager(); // creates an instance of the high score manager class
    	List<String> scoresString = highScores.getHighScoreString(); // calls the methods responsible for getting the high scores string of the users and stores them in a list of strings
    	
    	//a for loop that loops from 0 to 20
    	for(int i = 0; i < 20; i ++) {
    		try {
    			JLabel scores = new JLabel("<html><pre>" + scoresString.get(i) + "<html>"); // for each loop a new label is created that stores an element from the list of scores dictated by the variable i
    			scores.setBounds(70, 20*i, 400, 200); // sets the position and size of the labels. the y position changes depending on how many times it has looped 
    			scoresPanel.add(scores); //adds the label to the panel
        	
    		}
    		catch(IndexOutOfBoundsException e) { // catches any out of bound exceptions
 
    		}
    	}
    	
    	
    	
    	JButton goBack = new JButton("Return to Menu"); // creates the button to return to the main menu
    	goBack.setBounds(110, 500, 250, 50); // sets the position and size of the button
    	scoresPanel.add(goBack); // adds the button to the panel
    	goBack.addActionListener(new ActionListener() //adds an action listener to the button
    	{
    		//this method is run when the button is pressed, it sets the current frame to be invisible and sets the main menu frame to visible 
            public void actionPerformed(ActionEvent event) {
                scoresFrame.setVisible(false); // sets the score window to be invisible 
                menuFrame.setVisible(true); // sets the main menu window to be visible
            }
        });
    }
}