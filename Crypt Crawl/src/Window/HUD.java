package Window;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Game.CurrentData;
import Game.ID;

public class HUD {
	
	public int health; // this is a variable that will store the health to be displayed on the HUD
	public int maxHealth; // this will store the maximum health of the player
	public int healthPercentage; // this will store the percentage of the users health out of there maximum health so it can be displayed on the health bar
	public int Exp; // this will store the current experience points the user is on
	public int maxExp; // this will store the maximum amount of experience points the user will need to level up
	public int expPercentage; // this will store the percentage of the users experience out of there maximum experience so it can be displayed on the experience bar
	
	public static  int score; // creates a static variable to contain the score the player obtains. it is static so that it can easily be accessed by other classes
	
	//this tick method is called once every tick of the game loop which allows the HUD to be continually updated so it always displays the correct information
	// the point of this method is to assign the correct values to the variables such as the health, so they can be displayed in the render method.
	// the method is parsed the handler so that it has access to the list of game objects currently in the game so things like the players health can be accessed
	public void tick(Handler handler) {
		// this for loop loops through all of the game objects currently in the list
		for (int i = 0; i < handler.object.size(); i++) {
			// this if function checks whether the ID of the current object is equal to one of the classes the player can play as, so the right health and experience points are obtained
			if (handler.object.get(i).getID() == ID.Warrior || handler.object.get(i).getID() == ID.Rogue || handler.object.get(i).getID() == ID.Mage) {
				maxHealth = handler.object.get(i).getMaxHealth(); // sets the max health variable equal to the max health of the class the user is playing as
				health = handler.object.get(i).getHealth(); // sets the health variable equal to the player currently has
				health = Game.clamp(health, 0, maxHealth); // calls the clamp method in the game class, parsing the health variable, 0, and the max health variable. this method is used to ensure the health bar does not display anything below 0
				healthPercentage = (int) (((float) 280 / maxHealth) * (float) health); // this variable is used to store the health as a percentage of the maximum health so that it can be displayed on a bar of fixed size 
				// this if function checks whether the health of the player is 0
				if(health == 0) {
					handler.object.get(i).setActive(false); // sets the player objects active variable to false, so it is removed when the player dies
				}
				Exp = handler.object.get(i).getEXP(); // gets the current experience points the user is on and stores it in the Exp variable
				maxExp = handler.object.get(i).getMaxEXP();	 // gets the maximum experience points needed for the player to level up and stores it in the max EXP variable
				expPercentage = (int) (((float) 280 / maxExp) * (float) Exp); // this variable is used to store the experience points as a percentage of the maximum experience points so that it can be displayed on a bar of fixed size
			}
		}
	}
	//this method is used to draw the elements such as the health and experience bars to the screen, it is parsed a graphics object to accomplish this
	//this method is called once every tick of the game loop so that the bars are continually updated to correctly display right information
	public void render(Graphics g) { 
			
		g.setColor(new Color(100, 155, 105, 122)); // sets the background colour and transparency of one layer of the health bar 
		g.fillRect(15, 15, 280, 32); // fills a rectangle with the colour above with specified coordinates and size
		g.setColor(new Color(5, 255, 5, 122)); // sets the colour and transparency of the bar that will show the health the player is on
		g.fillRect(15, 15, healthPercentage, 32); // fills the rectangle with the colour above and the specified coordinates and size, the width of the bar is the health as a percentage of the maximum health
		g.setColor(new Color(255, 255, 255, 122)); // sets the colour of the border for the health and transparency
		g.drawRect(15, 15, 280, 32); // draws a rectangle with no filled inside, the border is the colour specified above
		
		g.setColor(new Color(255, 255, 255, 255)); // sets the colour of the text
		g.setFont(new Font("TimesRoman", Font.BOLD, 20)); // sets the font of the text
		g.drawString("Score: " + score, 657, 35); // draws the text to the screen, this text displays the score the user is on
		g.drawString("Floor: " + CurrentData.Level, 500, 35); // draws text to the screen, this text displays which floor the user is on
		
		g.drawString("Level: " + CurrentData.playerLevel, 35, 550); // draws text on the screen, this text display what level the player is
		
		g.setColor(new Color(100, 155, 105, 122)); // sets the background colour and transparency of the experience points bar
		g.fillRect(35, 557, 280, 32); // fills the rectangle with the colour above at the specified coordinates and size
		
		g.setColor(new Color(255, 250, 255, 230)); // sets the colour and transparency of the bar that will show the players current experience points
		g.fillRect(35, 557, expPercentage, 32); // fills the rectangle with the colour above at the specified coordinates and size. The width of the bar is a experience points as a percentage of the maximum experience points
		
		g.setColor(new Color(255, 255, 255, 122)); // sets the colour and transparency of the border for the experience points bar
		g.drawRect(35, 557, 280, 32); // draws the border of the experience bar with the colour above.
		
	}
}
