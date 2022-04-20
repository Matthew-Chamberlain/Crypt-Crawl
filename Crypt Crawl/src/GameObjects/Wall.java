package GameObjects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import Game.CurrentData;
import Game.GameObject;
import Game.ID;
import Window.Handler;

public class Wall extends GameObject{ // the wall class inherits methods and variables from the game objects class
	
	public Wall(int x, int y, ID id, Handler handler) {
		super(x, y, id); // sets the x, y and ID variables to ones defined in the game objects class, they can be set to the ones in the game objects class as all game objects need
		
	}
	
	// this method is used to create a rectangle around the object to act as a hit box
	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32); // returns a rectangle at the given coordinates, at a given size
	}

	//this method is responsible for drawing the object to the screen.
	//this method is called once every tick of the game loop so the graphics can be continually updated
	public void render(Graphics g) {
		if(CurrentData.Level == 1) { // runs the indented code if the current level is equal to 1
			g.setColor(Color.CYAN); // sets the colour of the walls. The colour of the wall is always cyan on the first level
			g.fillRect(x, y, 32, 32); // fills the rectangle of a given position and size with the colour above
		}
		else { // runs the indented code if the current level is not equal to 1 
			g.setColor(new Color(CurrentData.red, CurrentData.green, CurrentData.blue)); // sets the colour of the wall to a colour that is a combination of different amounts of red, green and blue. The colours of the walls are random after the first level
			g.fillRect(x, y, 32, 32); // fills the rectangle of a given position and size with the colour defined above
		}
		
		
	}
	
	// this method is used to update things each tick of the game loop, however as the wall is static and nothing changes it is not needed, but needs to be included as it is inherited from the game object class
	public void tick() {
		
	}
}