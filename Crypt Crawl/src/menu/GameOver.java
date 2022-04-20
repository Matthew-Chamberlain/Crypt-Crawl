package menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Game.CurrentData;
import Window.HUD;

public class GameOver { // this class meets objective 13 
	
	//this class is responsible for creating the two frames and panels that will be used. One window is a menu screen and the other is where the user will enter their name and their score will be saved.
	//the score that the user got and the floor that the user got to are parsed
	public GameOver(int score, int level) {
		JFrame gameOverFrame = new JFrame("Game Over"); // creates the window for the game over menu and adds text
		gameOverFrame.setSize(350, 500); // sets the size of the game over menu window
		gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // sets it so the window is fully exited upon closing
		gameOverFrame.setResizable(false); // makes it so the window size cannot be changed so the components stay in the correct places
		gameOverFrame.setLocationRelativeTo(null); // makes it so the window opens in the centre of the screen
		
		JPanel gameOverPanel = new JPanel(); // creates a panel for the game over menu
		gameOverFrame.add(gameOverPanel); // adds the panel to the frame
		
		JFrame inputTextFrame = new JFrame("Input Name"); // creates a frame where the player can save their score and name 
		inputTextFrame.setSize(350, 150); // sets the size of the frame
		inputTextFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // sets it so the window is fully exited upon closing
		inputTextFrame.setResizable(false); // makes it so the window size cannot be changed so the components stay in the correct places
		inputTextFrame.setLocationRelativeTo(null); // makes it so the window opens in the centre of the screen
		
		
		JPanel inputTextPanel = new JPanel(); // creates a panel for the window where the user saves their score
		inputTextFrame.add(inputTextPanel); // adds the panel to the frame
		inputTextPanel.setLayout(null); // makes it so the components can be placed anywhere on the panel
		
		gameOverFrame.setVisible(true); // sets the game over menu to be visible 
		inputTextFrame.setVisible(false); // sets the text input frame to be invisible 
		
		placeComponents(gameOverFrame, gameOverPanel, score, level, inputTextFrame, inputTextPanel); // calls the place components method parsing game over menu frame and panel, so components can be added, the score and floor the player got to and the frame and panel for the input text window, so components can be added to them.
		
		
	}
	// this method is responsible for placing components onto both the game over menu panel and the text input window panel. 
	// this method is also responsible for saving the score, name and floor of the user.
	public void placeComponents(JFrame gameOverFrame, JPanel gameOverPanel, int score, int level, JFrame inputTextFrame, JPanel inputTextPanel) {
		
		gameOverPanel.setLayout(null); //sets it so the components can be placed anywhere on the frame.
		
		JTextField inputName = new JTextField(); // creates a new text frame
		inputName.setBounds(103, 10, 150, 20); // sets the position and size of the frame 
		inputName.setHorizontalAlignment(JTextField.CENTER); // makes it so the text starts in the middle of the text field
		
		JButton saveScore = new JButton("Save Score"); // creates a button and adds text to it
		saveScore.setBounds(90, 10, 150, 110); // sets the position and size of the button
		gameOverPanel.add(saveScore); // adds the button to the game over menu panel
		
		JButton submitScore = new JButton("Submit Score"); // creates a button and adds text to it
		submitScore.setBounds(60, 50, 225, 35); // sets the position and size of the button
		inputTextPanel.add(submitScore); // adds the button to the text input window
		
		submitScore.addActionListener(new ActionListener() // adds an action listener to the submit score button
		{
			// this method will run once the save score button is pressed. It parses the score, name and floor number to the high score manager class so it can be sorted and written to a text file.
			public void actionPerformed(ActionEvent event) {
				String name; // Initiates a string variable called name
				name = inputName.getText(); // set the name variable equal to the text that has been written in the text file
				//example of defensive programming
				if(name != null && !name.isEmpty() && name.length() <= 3) { // runs the following code as long as the inputed name not empty and is no more than 3 characters
					HighScoreManager highScores = new HighScoreManager(); // creates an instance of the high score manager
					highScores.addScore(name, score, level); // calls the add score method in the high score manager class parsing the name, score and floor of the user
					inputTextFrame.setVisible(false); // sets the current frame to be invisible
					MainMenu mainMenu = new MainMenu(); // calls the constructor for the main menu class
				}
				// if the name inputed is empty, or is longer than 3 characters, the following code is run
				else {
					inputName.setText("Please input a valid Name"); // sets the text in the text field to display a message instructing the user to input the user to input their name again
				}
			}
		});
		
		saveScore.addActionListener(new ActionListener() // adds an action listener to the save score button
		{
			// this method is used to hide the game over menu and sets the text input frame to be visible. It also adds the text field to the panel
			public void actionPerformed(ActionEvent event) {
				gameOverFrame.setVisible(false); // sets the game over menu to be invisible
				inputTextFrame.setVisible(true); // sets the text input frame to be visible
				inputTextPanel.add(inputName); // adds the text field to the panel
			}
		});
		
		JButton goBack = new JButton("<html>Return to<br/> main menu</html>"); // creates a button and adds text to it
		goBack.setBounds(90, 350, 150, 110); // sets the size and position of the button
		gameOverPanel.add(goBack); // adds the button to the game over menu panel
		
		goBack.addActionListener(new ActionListener() //adds an action listener to the button 
		{
			//this method is responsible for reloading the main menu and hiding the game over menu. This method also sets the alive variable in the current data class to true so the game can be played again
			public void actionPerformed(ActionEvent event) {
				gameOverFrame.setVisible(false); // sets the game over menu frame to invisible
				CurrentData.alive = true; // sets the alive variable in the current data class to be true
				MainMenu mainMenu = new MainMenu(); // calls the constructor for the main menu class
			}
		});
		JLabel scores = new JLabel("<html>Score:<br/> " + score + "</html>"); // creates a JLabel and formats the text, the text contains the score variable
		scores.setBounds(90, 8, 400, 350); // sets the position and size of the label
		scores.setFont(scores.getFont().deriveFont(48.0f)); // sets the font and size of the text
		gameOverPanel.add(scores); // adds the label to the game over menu panel
		
		JLabel floors = new JLabel("<html>Floor:<br/> " + level + "</html>"); // creates a new JLavel and formats the text, the text contains the floor number the player reached
		floors.setBounds(90, 115, 400, 350); // sets the position and size of the label
		floors.setFont(floors.getFont().deriveFont(48.0f)); // sets the font and size of the text
		gameOverPanel.add(floors); // adds the label to the game over menu panel
	}
}