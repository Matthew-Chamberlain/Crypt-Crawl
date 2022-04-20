package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

import GameObjects.MagicBolt;
import GameObjects.Player;
import Window.Animation;
import Window.Handler;
import Window.StreamGenerator;
import Game.CurrentData;

public class KeyInput extends KeyAdapter{ // key adapter is a built in abstract class with methods for getting keyboard events. It has been extended so that the key input class can inherit its methods

	private Handler handler; // creates an instance of the handler class
	private Animation animation; // creates an instance of the animation class
	private long lastAttackTimer, attackcd, attackTimer; // creates three long variables that will be used to create the attack timer in order to limit how fast the user will be able to attack
	private Clip clip; // creates a variable called clip of the data type Clip
	private StreamGenerator gen = new StreamGenerator(); // creates an instance of the stream generator class

	// the constructor of this class is used to define the handler and also to set the values to the attack cool down, attack timer depending on the class the user has chosen 
	public KeyInput(Handler handler) {
		this.handler = handler; // sets the handler object equal to the handler that was parsed in
		
		if(CurrentData.selectedClass.equals("Warrior")) { // if the player is playing as a warrior then the attack cool down and timer will be 700 milliseconds
			attackcd = 700;
			attackTimer = attackcd;
		}
		else if (CurrentData.selectedClass.equals("Rogue")){ // if the player is playing as a rogue then the attack cool down and timer will be 400 milliseconds
			attackcd = 400;
			attackTimer = attackcd;
		}
		else if (CurrentData.selectedClass.equals("Mage")) { // if the player is playing as a mage then the attack cool down and timer will be set to 1000 milliseconds
			attackcd = 1000;
			attackTimer = attackcd;
		}
	}
	
	// the purpose of this method is to run different bits of code when different buttons are pressed down. These bits of code will be used to control things such as the players movement and attacking. 
	// the method is parsed a key event which is an event on the keyboard such as when a key is pressed or released
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); // gets the key code of the key that was pressed and stores it in an integer variable
		
		for(int i = 0; i < handler.object.size(); i++) { // this for loop loops through all the object currently in the game.
			GameObject tempObject = handler.object.get(i); // gets the object that the for loop is currently on and stores it as a temporary game object 
			
			if (tempObject.getID() == ID.Warrior || tempObject.getID() == ID.Rogue || tempObject.getID() == ID.Mage) { // runs the indented code if the ID of the temporary object is either warrior, rogue or mage
				
				if(Player.hit == false) { // runs the indented code if the players hit variable is false. THis will prevent the player from moving when they are getting knocked back
					if(tempObject.velX == 0) { // runs the indented code if the player is not moving horizontally, this will help prevent the player from moving diagonally
					// this code meets objective 6
						if(key == KeyEvent.VK_W) {  // if the key pressed was a 'w' set the y velocity to -5, so the player moves up the screen, sets the x velocity
							tempObject.SetVelY(-5);
							tempObject.SetVelX(0);
							tempObject.SetKey("Up"); // sets the players to be facing the up direction
						}
						if(key == KeyEvent.VK_S) { // runs the indented code of the player is pressing the s key
							tempObject.SetVelY(5); // sets the y velocity to 5 so the player moves down the screen 
							tempObject.SetVelX(0); // ensures the player can not move horizontally when moving down by setting the x velocity to 0
							tempObject.SetKey("Down"); // sets the direction the player is facing to down
						}
					}
					if(tempObject.velY == 0){ // runs the indented code if the players y velocity is 0, so the player cannot move vertically whilst also moving horizontally
						if(key == KeyEvent.VK_A) { // runs the indented code if the key the user is pressing the a key
							tempObject.SetVelX(-5); // sets the x velocity to -5 so the player moves left along the screen
							tempObject.SetVelY(0); // sets the y velocity to 0 so the player cannot move vertically while moving left
							tempObject.SetKey("Left"); // sets the direction the player is facing to left
						}
						if(key == KeyEvent.VK_D) { // runs the indented code if the player is pressing the d key
							tempObject.SetVelX(5); // sets the x velocity of the player to 5 so the player moves right along the screen 
							tempObject.SetVelY(0); // sets the y velocity to 0 so the player cannot move vertically while moving left
							tempObject.SetKey("Right"); // sets the direction the player is facing the right
						}
					}
				}
				
				if(tempObject.getID() == ID.Mage){ // runs the indented code if the ID of the temporary object is equal to mage
					if(key == KeyEvent.VK_J) { // runs the indented code if the key the user is pressing is j
						attackTimer += System.currentTimeMillis() - lastAttackTimer; // sets the attack timer equal to the current time of the system clock in milliseconds - the last attack timer
						lastAttackTimer = System.currentTimeMillis(); // sets the last attack timer equal to the current time of the system clock in milliseconds  
						if(attackTimer < attackcd) // if the attack timer is less than the attack cool down not enough timer has passed for anther attack,
							return;
						
						tempObject.SetAttacking(true); // sets the attacking variable of the player to true
						if(tempObject.isAttacking() == true) { // if the attacking variable of the player is true
							tempObject.SetVelX(0); // sets the players x velocity to 0
							tempObject.SetVelY(0); // sets the players y velocity to 0
							// loads and plays the sound for the magic attack. An try catch is used to catch any exceptions that might occur from loading a sound file
							try {
								AudioInputStream stream = AudioSystem.getAudioInputStream(gen.newStream("/Magic Attack.wav"));
							    clip = AudioSystem.getClip();
							    clip.open(stream);
							    clip.start();
							} catch (Exception e1) {
								System.out.println(e1);
							}
						}
						//adds a magic attack at the players x and y that shoots upwards
						if(tempObject.getKey().equals("Up")) {
							handler.addObject(new MagicBolt(tempObject.getX(), tempObject.getY(), ID.MagicBolt, 0, -5, tempObject.getKey(), handler, tempObject.getDamage()));
						}//adds a magic attack at the players x and y that shoots downwards
						else if(tempObject.getKey().equals("Down")) {
							handler.addObject(new MagicBolt(tempObject.getX(), tempObject.getY(), ID.MagicBolt, 0, 5, tempObject.getKey(), handler, tempObject.getDamage()));
						}//adds a magic attack at the players x and y that shoots left
						else if(tempObject.getKey().equals("Left")) {
							handler.addObject(new MagicBolt(tempObject.getX(), tempObject.getY(), ID.MagicBolt, -5, 0, tempObject.getKey(), handler, tempObject.getDamage()));
						}// adds a magic attack at the players x and y that shoots right
						else if(tempObject.getKey().equals("Right")) {
							handler.addObject(new MagicBolt(tempObject.getX(), tempObject.getY(), ID.MagicBolt, 5, 0, tempObject.getKey(), handler, tempObject.getDamage()));
						}
						attackTimer = 0; // resets the attack timer
						// creates a timer that will perform an action after a certain time has passed
						// the timer sets the attacking variable to false 350 milliseconds after the timer starts
						ActionListener taskPerformer = new ActionListener() {
					           public void actionPerformed(ActionEvent evt) {
					        	   tempObject.SetAttacking(false);
					           }
						};
						Timer timer = new Timer(350 ,taskPerformer);
						timer.setRepeats(false);
						timer.start();
					}
				}
				else if(tempObject.getID() == ID.Warrior || tempObject.getID() == ID.Rogue) { // runs the indented code if the ID of the temporary object is rogue or warrior
					if(key == KeyEvent.VK_J) {
						
						attackTimer += System.currentTimeMillis() - lastAttackTimer; // sets the attack timer equal to the attack timer + the current system time in milliseconds - the last attack timer
						lastAttackTimer = System.currentTimeMillis(); // sets the last attack timer equal to the current system time in milliseconds 
						if(attackTimer < attackcd) // if the attack timer is less than the attack cool down then not enough time has passed to attack
							return;
						Player tempPlayer = (Player) tempObject; // casts the temporary object to a player object so it can use its methods
						tempPlayer.checkAttacks(); // calls the check attack method which places a rectangle in front of the player to act as a hit box for the attack
						tempObject.SetAttacking(true); // sets the players attacking variable to true
						if(tempObject.isAttacking() == true) { // runs the indented code if the players attacking variable is true
							tempObject.SetVelX(0); // sets the players x velocity to 0
							tempObject.SetVelY(0); // sets the players y velocity to 0
							// this is used to load in the file for the stab attack. A try catch has been used to catch any errors that would occur from loading in the sound file
							try {
								AudioInputStream stream = AudioSystem.getAudioInputStream(gen.newStream("/Hit.wav"));
							    clip = AudioSystem.getClip();
							    clip.open(stream);
							    clip.start();
							} catch (Exception e1) {
								System.out.println(e1);
							}
						}
						attackTimer = 0; // resets the attack timer
						// creates a timer that will set the attacking variable of the player to false after 350 milliseconds have passed
						ActionListener taskPerformer = new ActionListener() {
					           public void actionPerformed(ActionEvent evt) {
					        	   tempObject.SetAttacking(false);
					           }
						};
						Timer timer = new Timer(350 ,taskPerformer);
						timer.setRepeats(false);
						timer.start();
					}
				} 
			}
		}
	}

	// the key released method is responsible for running code when keys are released. The code will do thing such as stopping the player from moving when a movement key is released
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode(); // gets the key code of the key released and stores it in an integer
		
		for(int i = 0; i < handler.object.size(); i++) { // loops through all the game objects in the list 
			GameObject tempObject = handler.object.get(i); // sets the object currently selected to a temporary object
			
			if (tempObject.getID() == ID.Warrior || tempObject.getID() == ID.Rogue || tempObject.getID() == ID.Mage) { // runs the indented code if the ID of the temporary variable is equal to warrior, rogue or mage
				
				//this code meets objective 6
				if(key == KeyEvent.VK_W) tempObject.SetVelY(0); // if the key released was the w set velocity y equal to 0 
				else if(key == KeyEvent.VK_S) tempObject.SetVelY(0); // if the key released was the s set velocity y equal to 0 
				else if(key == KeyEvent.VK_A) tempObject.SetVelX(0); // if the key released was the a set velocity x equal to 0 
				else if(key == KeyEvent.VK_D) tempObject.SetVelX(0); // if the key released was the d set velocity x equal to 0 
			}
		}
	}
}