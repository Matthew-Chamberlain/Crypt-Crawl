package menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Window.BufferedImageLoader;

public class Controls { // this class helps meet objective 1
	
	
	// This method is responsible for creating the window for the control screen, and the panel which components will be placed on. This method is parsed the main menu frame. so it can parse it to the place components method.
	public Controls(JFrame menuFrame) {
		JFrame controlFrame = new JFrame("Control Screen"); // creates and names the JFrame for the control screen
		controlFrame.setSize(1000,700); // sets the size of the window
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // sets the window to properly close when the exit button is pressed
        controlFrame.setResizable(false); // makes it so the window size cannot be changed, so the components stay in the right places 
        controlFrame.setLocationRelativeTo(null); // makes it so the window opens in the centre of the users screen to draw attention to the window.
        
        JPanel controlPanel = new JPanel();  // creates the JPanel for the control screen
        controlFrame.add(controlPanel);  // adds the panel to the frame
        
        controlFrame.setVisible(true); // sets the frame to be visible to the user
        
        placeComponents(controlFrame, menuFrame, controlPanel); // calls the place components method. 
	}
    
	//this method is responsible for placing the components on the panel, such as buttons and images. This method is parsed the control screen JFrame and JPanel so that the components can be added to them
	//this method is also parsed the main menu frame so that it can be made visible again once a button is pressed.
    public void placeComponents(JFrame controlFrame, JFrame menuFrame, JPanel controlPanel) {
     	
    	controlPanel.setLayout(null); // makes it so the components can be placed anywhere on the panel rather than positions according to a default layout option.
     	
     	BufferedImage image = null; // initialises a buffered image variable that will be used to store an image
     	
     	BufferedImageLoader loader = new BufferedImageLoader(); // creates an instance of the buffered image loader class
     	
     	JLabel label = new JLabel(); // creates a new JLabel
     	label.setBounds(40, 100, 900, 430); // sets the position and size of the JLabel
     	controlPanel.add(label); // adds the label to the panel
     	
     	// this is an example of a paremetiesd file path
     	image = loader.loadImage("/Controls.png"); // calls the load image method in the buffered image loader class and parses the images location as a string
		
		label.setIcon(new ImageIcon(image)); // adds the image to the label
     	
     	JButton goBack = new JButton("Return to menu"); // creates a button to return to the main menu and adds the text to the button
     	goBack.setBounds(260, 600, 450, 50); // sets the position and size of the button
     	controlPanel.add(goBack); // adds the button to the panel
     	goBack.addActionListener(new ActionListener() // adds an action listener to the button that will recognise mouse clicks on the button
     	{
     		// this method will set the control frame to be invisible and set the menu frame to be visible once there is an action on the button such as a mouse click
     		public void actionPerformed(ActionEvent event) {
     		controlFrame.setVisible(false);	//sets the control frame to invisible 
     		menuFrame.setVisible(true); // sets the meny frame to visible 
     		}
     	});
    }
}