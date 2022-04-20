package menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Game.CurrentData;
import Game.Sound;
import Window.BufferedImageLoader;
import Window.Game;

public class ClassSelect { // theis class helps meet objective 1
	
	private BufferedImage image; // creates a private buffered image variable so that it can be used in the item state changed method
	private String selectedClass; // creates a private string variable so that it can be used in the item state changed method
	
	//this method is responsible for creating the window and panel for the class select screen
	public ClassSelect() {
		JFrame classSelectFrame = new JFrame("Class Selection"); // creates and names the class select window
		classSelectFrame.setSize(800,600); // sets the size of the window
		classSelectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes it so the window is fully exited when the exit button is pressed instead of just hiding
		classSelectFrame.setResizable(false); // makes it so the window size cannot be changed so all the components stay in the correct location
        classSelectFrame.setLocationRelativeTo(null); // makes it so the window opens in the centre of the screen
        
        JPanel classSelectPanel = new JPanel(); // creates a new JPanel for the components to be added to
        classSelectFrame.add(classSelectPanel); // adds the panel to the frame
        
        classSelectFrame.setVisible(true); // set the frame to be visible
        
        
        placeComponents(classSelectFrame, classSelectPanel); // calls the place components method parsing the class select frame and panel so that components can be added and the frame can be set to invisible when the player selects their class
	}
	//this method is responsible for adding the components to the JPanel and also hiding the class select window and loading the game  
	public void placeComponents(JFrame classSelectFrame, JPanel classSelectPanel){
		classSelectPanel.setLayout(null); // makes it so the components can be placed anywhere on the panel.
		//example of a single dimensional array
		String[] classes = new String[] {"Warrior", "Rogue", "Mage"}; // creates a one dimensional string array with three elements, one for each of the classes
		JComboBox<String> classSelection = new JComboBox<String>(classes); //creates a string combobox and adds the array of class names to the combobox
		classSelection.setBounds(200,375,200,50); // sets the position and size of the combobox
		classSelectPanel.add(classSelection); // adds the combo box to the panel
		
		JButton select = new JButton("Select Class"); // creates a button and adds text to it.
		select.setBounds(410, 375, 200, 50); // sets the position and size of the button
		classSelectPanel.add(select); // adds the button to the panel
		
		BufferedImageLoader loader = new BufferedImageLoader(); // creates a new instance of the buffered image loader class
	
		JLabel picture = new JLabel(); // creates a new JLabel
		picture.setBounds(205, 10, 400, 350); // sets the position and size of the label
		classSelectPanel.add(picture);
		
		//this code helps meet objective 5 // this is an example of casting 
		selectedClass = (String) classSelection.getSelectedItem(); // gets the item currently selected from the combobox and casts it to a string and stores it in a variable
		// this is an example of a paremetised file path
		image = loader.loadImage("/" + selectedClass + ".png"); // calls the load image method from the buffered image loader class parsing the file path of an image as a string and storing the image returned in a buffered image variable
		picture.setIcon(new ImageIcon(image)); // sets the icon of the label to the image
		classSelection.addItemListener(new ItemListener(){ // adds an item listner to the combobox that listens for when the item selected changes
			//this method resets the selected class variable to the current item selected from the combobox after it has been changed and changes the image to match the selected class
			public void itemStateChanged(ItemEvent e) {
				selectedClass = (String) classSelection.getSelectedItem(); // sets the selected class variable to the currently selected item from the combo box
				image = loader.loadImage("/" + selectedClass + ".png"); // calls the load image method from the buffered image loader parsing the file path as a string, the file path is dependent on the item selected from the combobox // this is an example of a paremtised file path
				picture.setIcon(new ImageIcon(image)); // sets the icon of the label to the loaded image.

			}
		});
		select.addActionListener(new ActionListener() // creates an action listener for the select class button
		{
			//This method hides the class select frame and stores the item selected from the combobox in a string. 
			//This method also starts the background music for the game by calling the play method in the sound class and parsing the file path as a string.
			//This method also calls the constructor for the game class
			//This method is run once the button has been clicked
			//this code meets objective 5
			public void actionPerformed(ActionEvent event) {
				classSelectFrame.setVisible(false); // sets the class select frame to invisible 
				CurrentData.selectedClass = (String) classSelection.getSelectedItem(); // gets the currently selected class from the combobox and stores it in a variable in the current data class
				Sound.play("/Music.wav"); // calls the play method in the sound class and parses the file path
				Game startGame = new Game(); // calls the constructor for the game class.
			}
		});
	}
}