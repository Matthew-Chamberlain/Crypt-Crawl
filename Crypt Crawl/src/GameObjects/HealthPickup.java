package GameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Game.GameObject;
import Game.ID;

public class HealthPickup extends GameObject { // inherits methods and variables from the game object class

	public HealthPickup(int x, int y, ID id) {
		super(x, y, id); // sets the x coordinate, y coordinate and ID of the health pickup equal to the ones defined in the game objects class 
	}

	// this method is used to update things each tick of the game loop, however as the wall is static and nothing changes it is not needed, but needs to be included as it is inherited from the game object class
	public void tick() {
		
	}

	// the render method is responsible for drawing the health pickup to the screen, it is called once every tick of the game loop to ensure that the correct things are being rendered
	public void render(Graphics g) {
		g.setColor(Color.RED); // sets the colour of the pickup to red
		g.fillRect(x, y, 16, 16); // fills the rectangle of the given position and size with the colour above 
	}
	
	// this method drawas a rectangle around the object to act as a hit box 
	public Rectangle getBounds() {
		return new Rectangle(x, y, 16, 16); // returns a rectangle of a specified position and size
	}

}
