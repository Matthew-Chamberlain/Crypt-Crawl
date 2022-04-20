package menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu { // this class helps meet objective 1
	
	//This method is responsible for setting up and creating the window for the menu
	//It sets the size of the window, its location, and whether it's visible.
	
	public MainMenu() {
		
		JFrame menuFrame = new JFrame("Main Menu"); // creates the frame and sets the name of the window
		menuFrame.setSize(350, 500); // sets the size of the window
		menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // this ensures that the program is terminated when it is closed, instead of being hidden and still running
		menuFrame.setResizable(false); // ensures the window is a set size that cannot be resized so that the buttons are always in the centre
		menuFrame.setLocationRelativeTo(null); // ensures the window always opens in the centre of the screen.
		
		JPanel menuPanel = new JPanel(); // initiates the JPanel, a panel is a space in which other components can be attached.
		menuFrame.add(menuPanel); // adds the panel to the frame
		
		placeComponents(menuFrame, menuPanel); // calls the place components method.
		
		menuFrame.setVisible(true); // sets the frame to be visible 
	}
	// this method is responsible for starting the program
	public static void main(String[]args) {
		new MainMenu(); // calls the constructor the main menu class
	}
	
	//This method is responsible for creating and placing the components on the window.
	//These components are graphical interface tools such as buttons, text fields and comboboxes
	//The parameters for this class are the panel and frame that are needed to add objects to.
	public void placeComponents(JFrame menuFrame, JPanel menuPanel) {
		menuPanel.setLayout(null);
		//example of well designed user interface
		JButton newGame = new JButton("Start new Game"); // creates the start game button
		newGame.setBounds(90, 50, 150, 50); // sets the position size of the button
		menuPanel.add(newGame); // adds the button to the panel
		
		newGame.addActionListener(new ActionListener() // this adds an action listener to the button so it can register mouse clicks
		{
			// this method is used to run code when the new game button is pressed, it closes the current window and creates a new one for the class select screen 
			public void actionPerformed(ActionEvent event) {
				menuFrame.setVisible(false); //this sets the frame to not be visible 
				ClassSelect selectClass = new ClassSelect(); // this calls the constructor for the class select class.
			}
		});
		
		//example of well designed user interface
		JButton highScore = new JButton("High Scores"); // creates the highscore button
        highScore.setBounds(90, 150, 150, 50); // sets the positions and size of the button
        menuPanel.add(highScore); // adds the high score button to the JPanel
        highScore.addActionListener(new ActionListener() // adds an action listeners so the things in the method will happen when the button is clicked
        {
            public void actionPerformed(ActionEvent event) {
                menuFrame.setVisible(false); // sets the First JFrame to be invisible
                HighScores scores = new HighScores(menuFrame); // this calls the constructor for the high score class
            }
        });
        
        
        //example of well designed user interface
        JButton controlScreen = new JButton("Controls"); // creates the controlScreen button
        controlScreen.setBounds(90, 250, 150, 50); // set the size and position of the button
        menuPanel.add(controlScreen); // add the button to the panel
        controlScreen.addActionListener(new ActionListener() // add an action listener that will make the things in the method happen when the button is clicked
        {
            public void actionPerformed(ActionEvent event){
            	menuFrame.setVisible(false); // set the frame to be invisible
                Controls controlScreen = new Controls(menuFrame); // calls the control screen class parsing panel2, frame2, panel1 and frame1
            }
        });
        
        //example of a well designed user interface
        JButton exit = new JButton("Exit"); // creates the exit button
        exit.setBounds(90, 350, 150, 50); // sets the position and size of the button
        menuPanel.add(exit); // adds the exit button to the panel
        exit.addActionListener(new ActionListener() // creates an action listener that causes whats in the method to run when the button is pressed.
        {
            public void actionPerformed(ActionEvent event) {
                System.exit(0); // terminates the program after the button is pressed
            }
        });
			
	}
}