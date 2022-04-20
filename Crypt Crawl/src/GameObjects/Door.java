package GameObjects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Game.CurrentData;
import Game.GameObject;
import Game.ID;
import Window.Handler;
import Window.Game;

public class Door extends GameObject { //inheriting methods and variables from  GameObject class

	Handler handler; // creates an instance of the handler class
	
	public Door(int x, int y, ID id, Handler handler) {
		super(x, y, id); // sets the x, y, and id variables equal to the ones in the game object class
		this.handler = handler; // sets the handler equal to the handler parsed
	}
	
	// this method is used to create a rectangle around the object in order to act as a hit box for the object
	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32); // returns a rectangle at a given position and size
	}

	// the tick method is called once for every tick of the game loop. The purpose of the method is to update check whether there are enemies in the room, in order to draw the right type of door, an open door or a closed door
	// this method helps meet objective 4
	public void tick() {
		if(CurrentData.enemyCount == 0) { // checks whether the enemy count of the current room is equal to 0
			for (int i = 0; i < handler.object.size(); i++) { // looping through all the objects in a list
				GameObject tempObject = handler.object.get(i); // stores the currently selected object in a temporary object
				if(tempObject.getID() == ID.DoorClosed) { // runs the indented code if the ID of the temporary object is a closed door
					handler.removeObject(tempObject); // removes the object from the list
				}
			}
		}
	}	

	// this render method is used to draw different types of doors to the screen. The render method is called evey tick of the game loop so the correct thing is rendered
	public void render(Graphics g) {
		if(id == ID.DoorClosed) { // if the ID of the door is a closed door, the indented code is run
			g.setColor(Color.DARK_GRAY); // sets the colour of the door to dark grey to indicate closed
			g.fillRect(x, y, 32, 32); // fills the rectangle of a given position and size with the colour above
		}
		else if(id == ID.TrapDoor) { //if the ID of the door is a trap door, the indented code is run
			g.setColor(Color.BLUE); // sets the colour of the trap door to blue
			g.fillRect(x, y, 32, 32); // fills the rectangle of a given position and size with the colour above
		}
		else { // if the ID is equal to any other type of door, the indented code is run
			g.setColor(Color.LIGHT_GRAY); // sets the colour of the door to light grey, to indicate that it is open
			g.fillRect(x, y, 32, 32); // fills the rectangle of a given position and size with the colour above
		}
	}
}