package GameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Game.GameObject;
import Game.ID;
import Game.Texture;
import Window.Game;
import Window.Handler;

public class MagicBolt extends GameObject { //example of inheritance 
	
	Texture tex = Game.getInstance(); // creates an instance of the texture class
	
	Handler handler; // creates an instance of the handler class
	
	public MagicBolt(int x, int y, ID id, int velX, int velY, String key, Handler handler, int damage) {
		super(x, y, id); // sets the x coordinate, y coordinate, and ID to the ones defined in the game objects class as these are variables that are used by all game obejcts
		this.velX = velX; // sets the x velocity variable equal to the x velocity parsed
		this.velY = velY; // sets the y velocity variable equal to the y velocity parsed
		this.key = key; // sets the key variable equal to the one parsed, the key variable is the direction the object is facing
		this.handler = handler; // sets the handler equal to the handler parsed
		this.setDamage(damage); // calls the set damage method, setting it to the damage number parsed
		
	}

	// the tick method is used to update things every tick of the game loop. In the case of the magic attack, the x coordinate and y coordinates are updated and the collisions method is called to check whether the object is colliding with other objects
	public void tick() {
		x += velX; // sets the x coordinate equal to the x coordinate + the x velocity
		y += velY; // sets the y coordinate equal to the y coordinate + the y velocity
		
		collision(); // calls the collision method
		
	}

	// the render method is used to draw images to the screen, it needs to be called every tick of the game loop in case images need to be updated 
	public void render(Graphics g) {
		if(id == ID.MagicBolt) {
			if(key.equals("Up")) { // if the player is looking up, draw the up attack image at the given coordinates and size
				g.drawImage(tex.magicBolt[0], x, y - 16, 32, 32, null);
			}
			else if(key.equals("Down")) { // if the player is looking down, draw the down attack image at the given coordinates and size
				g.drawImage(tex.magicBolt[1], x, y + 16, 32, 32, null);
			}
			else if(key.equals("Left")) { // if the player is looking left, draw the left attack image at the given coordinates and size
				g.drawImage(tex.magicBolt[2], x, y, 32, 32, null);
			}
			else if(key.equals("Right")) { // if the player is looking right, draw the right attack image at the given coordinates and size
				g.drawImage(tex.magicBolt[3], x, y, 32, 32, null);
			}
		}
		else { // if the ID is not equal to magic attack, run the indented code.
			g.setColor(Color.BLUE); // set the colour of the object to blue
			g.fillRect(x, y, 16, 16); // fills a rectangle at the given coordinates and of a given size with the colour above
		}
	}

	// the get bounds method is used to draw a rectangle around the object to act as a hit box for the object
	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32); // draws a 32 * 32 rectangle at the given coordinates  
	}
	
	// the collision method is used to check whether the objects hit box has intersected with another objects hit box and run the appropriate code depending on the object that is hit
	private void collision() {
		
		for (int i = 0; i < handler.object.size(); i++) { // for loop that loops through all the game objects in order to check what object has been collidided with

			GameObject tempObject = handler.object.get(i); // stores the current game object from the list in a temporary game object variable
			if(id == ID.MagicBolt) { // checks whether the ID is equal to a magic bolt
				if (tempObject.getID() == ID.Enemy || tempObject.getID() == ID.BossEnemy) { // if the ID of the temporary object is equal to an enemy or a boss enemy, run the indented code 
					if (getBounds().intersects(tempObject.getBounds())) { // if the hit boxes for the two objects intersect with each other the indented code is run
						
						// casts the temporary game object to an temporary enemy object so that its methods can be used
						Enemy tempEnemy = (Enemy) tempObject;
						tempEnemy.hurt(getDamage()); // calls the hurt method for the object hit, parsing the damage of the magic attack
						active = false; // sets the magic attacks active variable to false so it can be removed
					}
				}
			}
			// checks whether the magic attack is colliding with either a door or a wall. if it is the magic attack object is removed
			if (tempObject.getID() == ID.Wall || tempObject.getID() == ID.DoorUp || tempObject.getID() == ID.DoorDown || tempObject.getID() == ID.DoorLeft || tempObject.getID() == ID.DoorRight) {
				if (getBounds().intersects(tempObject.getBounds())) {
					active = false;
				}
			}
		}
	}
}