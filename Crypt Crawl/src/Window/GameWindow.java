package Window;
import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

public class GameWindow extends Canvas {
	
	//the purpose of this constructor is to create the game window that the game will be played on
	public GameWindow(int WIDTH, int HEIGHT, String title, Game game) {
		
		JFrame mainGame = new JFrame(title); // creates the JFrame ands sets its text to the title parsed
		
		mainGame.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // sets the preferred size of the window equal to the width and height parsed
		mainGame.setMaximumSize(new Dimension(WIDTH, HEIGHT)); // sets the maximum size of the window equal to the width and height parsed
		mainGame.setMinimumSize(new Dimension(WIDTH, HEIGHT)); // sets the minimum size of the window equal to the width and height parsed
	
		mainGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // sets the window to fully close when the exit button is pressed
		mainGame.setResizable(false); // makes is so the window size cannot be changed so that the player can only see what is inside the room drawn // this is an example of defensive programming
		mainGame.setLocationRelativeTo(null); // makes it so the window opens in the centre of the screen
		mainGame.add(game); // adds the game to the frame
		mainGame.setVisible(true); // sets the frame to be visible
		game.start(mainGame); //calls the method that starts the thread in which the game is run
	}
}
