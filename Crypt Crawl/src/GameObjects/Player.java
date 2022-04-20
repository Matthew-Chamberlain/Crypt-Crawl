package GameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

import Game.CurrentData;
import Game.GameObject;
import Game.ID;
import Game.Sound;
import Game.Texture;
import Window.Animation;
import Window.Game;
import Window.Handler;
import Window.MapArrays;
import Window.StreamGenerator;

public class Player extends GameObject { // example of inheritance

	Handler handler; // creates an instance of the handler class
	MapArrays floors; // creates an instance of the map arrays class
	Animation animation; // creates an instance of the animation class
	Game game; // creates an instance of the game class
	Sound sound; // creates an instance of the sound class

	Texture tex = Game.getInstance(); // creates an instance of the the texture class
	long lastAttack = System.currentTimeMillis(); // sets a long variable equal to the current time 
	private Animation playerWalkDown, playerWalkUp, playerWalkLeft, playerWalkRight, downAttack, upAttack, leftAttack, // creates a number of animation objects
			rightAttack;

	private long lastAttackedTimer, flinchcd = 500, flinchTimer = flinchcd; // creates three long variables that will be used to limit how many times the player can be hit
	

	Random r = new Random(); // creates a new random object

	BufferedImage sprite; // creates a new buffered image called sprite

	int randomRoom; // creates an integer that will be used to load random rooms
	public static String key; // stores the direction the user is facing as a string
	public static boolean hit = false; // stores a boolean to determine whether the player has been hit, the default value is set to fault

	// the player constructor is used to assign the values to variable such as the health and damage when the player is added 
	public Player(int x, int y, ID id, Handler handler, MapArrays floors, String key, int health, int maxHealth, int damage, int EXP, int maxEXP) {
		super(x, y, id); // calls the constructor for the game object class to assign the x and y coordinates and the ID
		this.handler = handler; // sets the handler object equal to the handler object parsed
		this.floors = floors; // set the map arrays object equal to the map arrays object parsed
		this.health = health; // sets the health variable equal to the health variable parsed
		this.maxHealth = maxHealth; // sets the max health variable equal to the max health parsed
		this.damage = damage; // sets the damage equal to the damage parsed
		this.EXP = EXP; // sets the experience points variable equal to the one parsed
		this.maxEXP = maxEXP; // sets the max experience points variable equal to the one parsed
		this.setActive(true); // sets active to true

		//creates new animations by calling the constructor for the animation class and storing them in the animation objects
		playerWalkDown = new Animation(10, tex.player[0], tex.player[1]);
		playerWalkLeft = new Animation(10, tex.player[2], tex.player[3]);
		playerWalkRight = new Animation(10, tex.player[4], tex.player[5]);
		playerWalkUp = new Animation(10, tex.player[6], tex.player[7]);
		downAttack = new Animation(10, tex.player[8], tex.player[9], tex.player[10]);
		upAttack = new Animation(10, tex.player[11], tex.player[12], tex.player[13]);
		leftAttack = new Animation(10, tex.player[14], tex.player[15], tex.player[16]);
		rightAttack = new Animation(10, tex.player[17], tex.player[18], tex.player[19]);
		
	}
	// this method is used to create a rectangle around the player to act as a a hit box
	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

	// this method is used to update things such as the players position every tick of the game loop
	public void tick() {	
		
		//this code meets objective 6
		x += velX; // adds the x velocity to the x coordinate
		y += velY; // adds the y velocity to the y coordinate

		if (velX < 0) { // if the x velocity is less than 0 then set the direction the player is facing to left
			key = "Left";
		} else if (velX > 0) { // if the x velocity is greater than 0 then set the direction the player is facing to right
			key = "Right";
		}

		if (velY < 0) { // if the y velocity is less than 0 then set the direction the player is facing to up
			key = "Up";
		} else if (velY > 0) { // if the y velocity is greater than 0 then set the direction the player is facing to down
			key = "Down";
		}
			

		collision(); // calls the collision method

		//calls the run animation method for each of the animations
		playerWalkDown.runAnimation();
		playerWalkLeft.runAnimation();
		playerWalkRight.runAnimation();
		playerWalkUp.runAnimation();
		downAttack.runAnimation();
		upAttack.runAnimation();
		leftAttack.runAnimation();
		rightAttack.runAnimation();
		
		EXP = CurrentData.currentEXP; // sets the experience variable equal to the the amount of experience points the player is currently on
		if(EXP >= maxEXP) { // if the experience points the use has is greater than the maximum then call the level up method and reset the current experience points to 0
			levelUp();
			CurrentData.currentEXP = 0;
		}
	}
	// the collision method is used to determine what happens the players hit box intersects with the hit box of other objects
	private void collision() {
		for (int i = 0; i < handler.object.size(); i++) { // loops through all of the game objects

			GameObject tempObject = handler.object.get(i); // stores the current object in a temporary game object

			if (tempObject.getID() == ID.Enemy || tempObject.getID() == ID.BossEnemy) { // if the ID of the temporary object is enemy or boss enemy, run the indented code
				if (getBounds().intersects(tempObject.getBounds())) { // if the player hitbox intersects with the temporary objects hit box run the indented code
					int EnemyX = tempObject.getX(); // gets the x coordinate of the enemy 
					int EnemyY = tempObject.getY(); // gets the y coordinate of the enemy 
					String tempDirection = key; // stores the direction the player is facing in a temporary direction string
					hurt(tempObject.getDamage());
					
					if(tempObject.getKey().equals("right")) { // if the enemy is facing right, knock the player back right 
						velX = 12;
					}
					if(tempObject.getKey().equals("left")) { // if the enemy is facing left knock the player back left
						velX = -12;
					}
					if(tempObject.getKey().equals("down")) { // if the enemy is facing down knock the player back down
						velY = 12;
					}
					if(tempObject.getKey().equals("up")) { // if the enemy facing up knock the player back up
						velY = -12;
					}
					
					hit = true; // sets the hit variable to true
					tempObject.SetHit(true); // sets the hit variable of the enemy to true so they are slightly knocked back when attacking the player, so the player does not get trapped
					if(tempObject.getKey().equals("up")) { // if the enemy is facing up, knock back the enemy downwards
						tempObject.SetVelY(3);
						tempObject.setY(EnemyY += (tempObject.getVelY()));
					}
					else if(tempObject.getKey().equals("down")) { // if the enemy is facing down knock back the enemy upwards
						tempObject.SetVelY(-3);
						tempObject.setY(EnemyY += (tempObject.getVelY()));
					}
					else if(tempObject.getKey().equals("left")) { // if the enemy is facing left knock the enemy back right
						tempObject.SetVelX(3);
						tempObject.setX(EnemyX += (tempObject.getVelX()));
					}
					else if(tempObject.getKey().equals("right")) { // if the enemy is facing right knock the enemy back left
						tempObject.SetVelX(-3);
						tempObject.setX(EnemyX += (tempObject.getVelX()));
					}
					
					//starts a timer that knocks back the player for 100 milliseconds, once the timer is up the x and y velocities are set to 0, the direction the player is facing is set to the temporary direction so the player faces the same direction they were facing before they got hit, after they got hit
					//sets the hit variable of the player and enemy to false and set the x and y velocities of the enemy to 0
					ActionListener taskPerformer = new ActionListener() {
				           public void actionPerformed(ActionEvent evt) {
				        	   velX = 0;
				        	   velY = 0;
				        	   key = tempDirection;
				        	   tempObject.SetVelX(0);
				        	   tempObject.SetVelY(0);
				        	   tempObject.SetHit(false);
				        	   hit = false;
				           }
					};
					Timer timer = new Timer(100 ,taskPerformer);
					timer.setRepeats(false);
					timer.start();

				}
			} 
			else if (tempObject.getID() == ID.BossAttack) {// if the ID of the temporary object is a boss attack run the indented code
				if(getBounds().intersects(tempObject.getBounds())) { // if the hit box of the temporary object intersects the player hitbox call the players hurt method parsing the damage of the temporary object and set the active variable of the temporary object to false so it is removed 
					hurt(tempObject.getDamage());
					tempObject.setActive(false);
				}
			}
			else if (tempObject.getID() == ID.Heart) { // if the ID of the temporary object is heart run the indented code
				if(getBounds().intersects(tempObject.getBounds())) { // if the hit box of the player intersects the hit box for the temporary object run the indented code
					health += 20; // add 20 to the health variable
					CurrentData.currentHealth  -= 20; // take 20 away from the health missing
					if(health >= maxHealth) { // if the health is greater than the max health set the health to the max health and sets the missing health to 0
						health = maxHealth;
						CurrentData.currentHealth = 0;
					}
					handler.removeObject(tempObject); // remove the health pick up
				}
			}
			// this meets objective 12
			else if (tempObject.getID() == ID.TrapDoor) { // if the ID of the temporary object is a trap door run the indented code
				if(getBounds().intersects(tempObject.getBounds())) { // if the hit box for the temporary object intersects with the hit box for the player run the indented code
					CurrentData.currentFloor = floors.middle; // sets the current room to the middle room
					//CurrentData.previousLevel = CurrentData.Level;
					CurrentData.Level += 1; // adds one to the floor count
					CurrentData.red = r.nextFloat(); // creates a new random float to be used as a red value for the wall colours
					CurrentData.green = r.nextFloat(); // creates a new random float to be used as a green value for the wall colours
					CurrentData.blue = r.nextFloat(); // creates a new random float to be used as a blue value for the wall colours
					//resets the enemy count for each of the rooms
					floors.middleRight1Enemies = r.nextInt(6) + 2;
					floors.middleRight2Enemies = r.nextInt(6) + 2;
					floors.middleRight3Enemies = r.nextInt(6) + 2;
					floors.bottomRight1Enemies = r.nextInt(6) + 2;
					floors.bottomRight2Enemies = r.nextInt(6) + 2;
					floors.bottomRight3Enemies = r.nextInt(6) + 2;
					floors.topRight1Enemies = r.nextInt(6) + 2;
					floors.topRight2Enemies = r.nextInt(6) + 2;
					floors.topRight3Enemies = r.nextInt(6) + 2;
					floors.middleTop1Enemies = r.nextInt(6) + 2;
					floors.middleTop2Enemies = r.nextInt(6) + 2;
					floors.middleTop3Enemies = r.nextInt(6) + 2;
					floors.middleBottom1Enemies = r.nextInt(6) + 2;
					floors.middleBottom2Enemies = r.nextInt(6) + 2;
					floors.middleBottom3Enemies = r.nextInt(6) + 2;
					floors.middleLeft1Enemies = r.nextInt(6) + 2;
					floors.middleLeft2Enemies = r.nextInt(6) + 2;
					floors.middleLeft3Enemies = r.nextInt(6) + 2;
					floors.bottomLeft1Enemies = r.nextInt(6) + 2;
					floors.bottomLeft2Enemies = r.nextInt(6) + 2;
					floors.bottomLeft3Enemies = r.nextInt(6) + 2;
					floors.topLeft1Enemies = r.nextInt(6) + 2;
					floors.topLeft2Enemies = r.nextInt(6) + 2;
					floors.topLeft3Enemies = r.nextInt(6) + 2;
					CurrentData.previousRoom.clear(); // clears the list that contains all the rooms that have been previously visited // example of a list oper
					CurrentData.floorCleared = false; // sets the floor cleared variable to false
					Game.switchRoom(CurrentData.currentFloor, 378, 288, key, CurrentData.enemyCount, damage); // calls the switch room method in the game class to load a new room
				}
			}
			else if (tempObject.getID() == ID.DoorClosed) { // runs the indented code if the ID of the temporary object is equal to door closed
				if (getBounds().intersects(tempObject.getBounds())) { // runs the indented code if the hit box for the player intersects with the hit box of the temporary object
					x += -velX; // subtracts the x velocity from the x value
					y += -velY; // subtracts the y velocity from the y value
				}
			}
			else if (tempObject.getID() == ID.Wall) { // runs the indented code if the ID of the temporary object is equal to wall
				//this code helps meet objective 8
				if (getBounds().intersects(tempObject.getBounds())) { // runs if the indented code if the hit box for the player intersects with the hit box for the wall
					x += -velX; // subtracts the x velocity from the x value
					y += -velY; // subtracts the y velocity from the y value
				}

			} 
			// the following collisions with doors help meet objective 2 and 3
			else if (tempObject.getID() == ID.DoorRight && (CurrentData.roomsX == 0 && CurrentData.roomsY == 0)) { // runs the indented code if the ID of the temporary object is door right and if the player is in the middle roon
				if (getBounds().intersects(tempObject.getBounds())) { // if the hit box for the player intersects with the hit box for the temporary object run the indented code
					CurrentData.roomsX++; // adds one to the room x as it will be in the far left room in the 3x3 grid of rooms
					x = 40; // sets the x coordinate to 40
					y = 288; // sets the y coordinate to 288
					key = "Right"; // set the direction the player is facing to right
					randomRoom = r.nextInt(3 - 0) + 1; // sets a random number between 1 and 3
					//example of a list operation
					if (CurrentData.previousRoom.contains(floors.middleRight1)) { // if the middle room has already been loaded run indented code
						CurrentData.currentFloor = floors.middleRight1; // set the current floor to middle right 1 room
						CurrentData.enemyCount = floors.middleRight1Enemies; // set the current enemy count to middle right 1 enemy count 
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage); // call the switch room method so the new room is drawn
					} 
					else if (CurrentData.previousRoom.contains(floors.middleRight2)) { // if the list of previous rooms contains the middle right 2 rooms runs the indented code
						CurrentData.currentFloor = floors.middleRight2; // sets the current room to middle right 2
						CurrentData.enemyCount = floors.middleRight2Enemies; //sets the current enemy count to middle right 2 enemy count
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage); //calls the switch room method
					} 
					else if (CurrentData.previousRoom.contains(floors.middleRight3)) { // if the list of previous rooms contains the middle right 3 room run the indented code
						CurrentData.currentFloor = floors.middleRight3; // set the current room to middle room 3 
						CurrentData.enemyCount = floors.middleRight3Enemies; // sets the current enemy count to middle right 3 enemy count
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage); // calls the switch room method
					} 
					else if (CurrentData.previousRoom.contains(floors.topRight1)) { // if the list of previous rooms contains top right 1 room run the indented code
						CurrentData.currentFloor = floors.middleRight3; // set the current room to middle right 3 to ensure it lines up with the dungeon already created 
						CurrentData.enemyCount = floors.middleRight3Enemies; // set the enemy count to middle right 3 enemy count
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage); // calls the switch room method
					} 
					else if (CurrentData.previousRoom.contains(floors.topRight3)) { // if the previous room list contains the top right 3 room run the indented code
						randomRoom = r.nextInt(2 - 0) + 1; // set the variable to a random number between 1 and 2
						if (randomRoom == 1) { // if the random number is 1 load the middle right 1 room
							CurrentData.currentFloor = floors.middleRight1;
							CurrentData.previousRoom.add(floors.middleRight1); // example of a list operation
							CurrentData.enemyCount = floors.middleRight1Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						} else if (randomRoom == 2) { // if the random number is 2 load the middle right 2 room
							CurrentData.currentFloor = floors.middleRight2;
							CurrentData.previousRoom.add(floors.middleRight2);
							CurrentData.enemyCount = floors.middleRight2Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
					} else if (CurrentData.previousRoom.contains(floors.bottomRight1)) { // if the previous room list contains bottom right 1 load middle right 2 room
						CurrentData.currentFloor = floors.middleRight2;
						CurrentData.previousRoom.add(floors.middleRight2);
						CurrentData.enemyCount = floors.middleRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.bottomRight2)) { // if the list of previous room contains bottom right 2 room run the indented code
						randomRoom = r.nextInt(2 - 0) + 1; // set a random number between 1 and 2
						if (randomRoom == 1) { // if the random number 1 load the middle right 1 room
							CurrentData.currentFloor = floors.middleRight1;
							CurrentData.previousRoom.add(floors.middleRight1);
							CurrentData.enemyCount = floors.middleRight1Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
						else if (randomRoom == 2) { // if the random number is 2 then load the middle right 3 room
							CurrentData.currentFloor = floors.middleRight3;
							CurrentData.previousRoom.add(floors.middleRight3);
							CurrentData.enemyCount = floors.middleRight3Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
					} 
					else if (randomRoom == 1) { // if the random number is 1 load the middle right 1 room
						CurrentData.currentFloor = floors.middleRight1;
						CurrentData.previousRoom.add(floors.middleRight1);
						CurrentData.enemyCount = floors.middleRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (randomRoom == 2) { // if the random number is 2 load the middle right 2 room
						CurrentData.currentFloor = floors.middleRight2;
						CurrentData.previousRoom.add(floors.middleRight2);
						CurrentData.enemyCount = floors.middleRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (randomRoom == 3) { // if the random number is 3 load the middle right 3 room
						CurrentData.currentFloor = floors.middleRight3;
						CurrentData.previousRoom.add(floors.middleRight3);
						CurrentData.enemyCount = floors.middleRight3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			//A similar check to the above is done for each kind of door in each room, first it checks if any of the room variation have already been loaded, if they have then the correct one is loaded so the dungeon stays the same when leaver and reentering the same room
			//Next it checks if any of the rooms connected to any of the room variations have been loaded, depending on the which adjacent rooms have already been loaded the correct room variation is chosen, this is in order to keep the dungeon consistent with the current layout.
			//If no room that would predetermine the which room variation that should be loaded has already been loaded then a random number between 1 and 3 is chosen, depending on the number one of the three room variations is loaded
			else if (tempObject.getID() == ID.DoorLeft && (CurrentData.roomsX == 1 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX--;
					//handler.clearGraphics(null);
					x = 720;
					y = 288;
					key = "Left";
					CurrentData.currentFloor = floors.middle;
					CurrentData.enemyCount = floors.middleEnemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}

			} 
			else if (tempObject.getID() == ID.DoorDown && (CurrentData.roomsX == 1 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY--;
					//handler.clearGraphics(null);
					x = 378;
					y = 30;
					key = "Down";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.bottomRight1)) {
						CurrentData.currentFloor = floors.bottomRight1;
						CurrentData.enemyCount = floors.bottomRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.bottomRight3)) {
						CurrentData.currentFloor = floors.bottomRight3;
						CurrentData.enemyCount = floors.bottomRight3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.middleBottom3)) {
						CurrentData.currentFloor = floors.bottomRight3;
						CurrentData.enemyCount = floors.bottomRight3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.bottomRight1;
						CurrentData.previousRoom.add(floors.bottomRight1);
						CurrentData.enemyCount = floors.bottomRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.bottomRight3;
						CurrentData.previousRoom.add(floors.bottomRight3);
						CurrentData.enemyCount = floors.bottomRight3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			else if (tempObject.getID() == ID.DoorUp && (CurrentData.roomsX == 1 && CurrentData.roomsY == -1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY++;
					//handler.clearGraphics(null);
					x = 378;
					y = 550;
					key = "Up";
					CurrentData.currentFloor = floors.middleRight2;
					CurrentData.previousRoom.add(floors.middleRight2);
					CurrentData.enemyCount = floors.middleRight2Enemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			} 
			else if (tempObject.getID() == ID.DoorUp && (CurrentData.roomsX == 1 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY++;
					//handler.clearGraphics(null);
					x = 378;
					y = 550;
					key = "Up";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.topRight1)) {
						CurrentData.currentFloor = floors.topRight1;
						CurrentData.enemyCount = floors.topRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (CurrentData.previousRoom.contains(floors.topRight2)) {
						CurrentData.currentFloor = floors.topRight2;
						CurrentData.enemyCount = floors.bottomRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (CurrentData.previousRoom.contains(floors.middleTop2)) {
						CurrentData.currentFloor = floors.topRight1;
						CurrentData.enemyCount = floors.topRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (CurrentData.previousRoom.contains(floors.middleTop1)|| CurrentData.previousRoom.contains(floors.middleTop3)) {
						CurrentData.currentFloor = floors.topRight2;
						CurrentData.enemyCount = floors.topRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.topRight1;
						CurrentData.previousRoom.add(floors.topRight1);
						CurrentData.enemyCount = floors.topRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.topRight2;
						CurrentData.previousRoom.add(floors.topRight2);
						CurrentData.enemyCount = floors.topRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			else if (tempObject.getID() == ID.DoorDown && (CurrentData.roomsX == 1 && CurrentData.roomsY == 1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY--;
					//handler.clearGraphics(null);
					x = 378;
					y = 30;
					key = "Down";
					CurrentData.currentFloor = floors.middleRight3;
					CurrentData.previousRoom.add(floors.middleRight3);
					CurrentData.enemyCount = floors.middleRight3Enemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			} 
			else if (tempObject.getID() == ID.DoorUp && (CurrentData.roomsX == 0 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY++;
					//handler.clearGraphics(null);
					x = 378;
					y = 550;
					key = "Up";
					randomRoom = r.nextInt(3 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.middleTop1)) {
						CurrentData.currentFloor = floors.middleTop1;
						CurrentData.enemyCount = floors.middleTop1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleTop2)) {
						CurrentData.currentFloor = floors.middleTop2;
						CurrentData.enemyCount = floors.middleTop2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.middleTop3)) {
						CurrentData.currentFloor = floors.middleTop3;
						CurrentData.enemyCount = floors.middleTop3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.topRight1)) {
						CurrentData.currentFloor = floors.middleTop2;
						CurrentData.enemyCount = floors.middleTop2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.topRight2)) {
						randomRoom = r.nextInt(2 - 0) + 1;
						if (randomRoom == 1) {
							CurrentData.currentFloor = floors.middleTop1;
							CurrentData.previousRoom.add(floors.middleTop1);
							CurrentData.enemyCount = floors.middleTop1Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						} 
						else if (randomRoom == 2) {
							CurrentData.currentFloor = floors.middleTop3;
							CurrentData.previousRoom.add(floors.middleTop3);
							CurrentData.enemyCount = floors.middleTop3Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
					} 
					else if (CurrentData.previousRoom.contains(floors.topLeft1)) {
						CurrentData.currentFloor = floors.middleTop3;
						CurrentData.enemyCount = floors.middleTop3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.topLeft2)) {
						randomRoom = r.nextInt(2 - 0) + 1;
						if (randomRoom == 1) {
							CurrentData.currentFloor = floors.middleTop1;
							CurrentData.previousRoom.add(floors.middleTop1);
							CurrentData.enemyCount = floors.middleTop1Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						} 
						else if (randomRoom == 2) {
							CurrentData.currentFloor = floors.middleTop2;
							CurrentData.previousRoom.add(floors.middleTop2);
							CurrentData.enemyCount = floors.middleTop2Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
					} 
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.middleTop1;
						CurrentData.previousRoom.add(floors.middleTop1);
						CurrentData.enemyCount = floors.middleTop1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.middleTop2;
						CurrentData.previousRoom.add(floors.middleTop2);
						CurrentData.enemyCount = floors.middleTop2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (randomRoom == 3) {
						CurrentData.currentFloor = floors.middleTop3;
						CurrentData.previousRoom.add(floors.middleTop3);
						CurrentData.enemyCount = floors.middleTop3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			else if (tempObject.getID() == ID.DoorDown && (CurrentData.roomsX == 0 && CurrentData.roomsY == 1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY--;
					//handler.clearGraphics(null);
					x = 378;
					y = 30;
					key = "Down";
					CurrentData.currentFloor = floors.middle;
					CurrentData.enemyCount = floors.middleEnemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			} 
			else if (tempObject.getID() == ID.DoorRight && (CurrentData.roomsX == 0 && CurrentData.roomsY == 1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX++;
					//handler.clearGraphics(null);
					x = 40;
					y = 288;
					key = "Right";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.topRight1)) {
						CurrentData.currentFloor = floors.topRight1;
						CurrentData.enemyCount = floors.topRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (CurrentData.previousRoom.contains(floors.topRight3)) {
						CurrentData.currentFloor = floors.topRight3;
						CurrentData.enemyCount = floors.topRight3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (CurrentData.previousRoom.contains(floors.middleRight3)) {
						CurrentData.currentFloor = floors.topRight1;
						CurrentData.enemyCount = floors.topRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (CurrentData.previousRoom.contains(floors.middleRight2) || CurrentData.previousRoom.contains(floors.middleRight1)) {
						CurrentData.currentFloor = floors.topRight3;
						CurrentData.enemyCount = floors.topRight3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.topRight1;
						CurrentData.previousRoom.add(floors.topRight1);
						CurrentData.enemyCount = floors.topRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.topRight3;
						CurrentData.previousRoom.add(floors.topRight3);
						CurrentData.enemyCount = floors.topRight3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			else if (tempObject.getID() == ID.DoorLeft && (CurrentData.roomsX == 1 && CurrentData.roomsY == 1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX--;
					//handler.clearGraphics(null);
					x = 720;
					y = 288;
					key = "Left";
					CurrentData.currentFloor = floors.middleTop2;
					CurrentData.previousRoom.add(floors.middleTop2);
					CurrentData.enemyCount = floors.middleTop2Enemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			} 
			else if (tempObject.getID() == ID.DoorDown && (CurrentData.roomsX == 0 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY--;
					//handler.clearGraphics(null);
					x = 378;
					y = 30;
					key = "Down";
					randomRoom = r.nextInt(3 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.middleBottom1)) {
						CurrentData.currentFloor = floors.middleBottom1;
						CurrentData.enemyCount = floors.middleBottom1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);

					} 
					else if (CurrentData.previousRoom.contains(floors.middleBottom2)) {
						CurrentData.currentFloor = floors.middleBottom2;
						CurrentData.enemyCount = floors.middleBottom2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					} 
					else if (CurrentData.previousRoom.contains(floors.middleBottom3)) {
						CurrentData.currentFloor = floors.middleBottom3;
						CurrentData.enemyCount = floors.middleBottom3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);

					}
					else if (CurrentData.previousRoom.contains(floors.bottomRight3)) {
						CurrentData.currentFloor = floors.middleBottom3;
						CurrentData.enemyCount = floors.middleBottom3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.bottomLeft2)) {
						CurrentData.currentFloor = floors.middleBottom3;
						CurrentData.enemyCount = floors.middleBottom3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.middleBottom1;
						CurrentData.previousRoom.add(floors.middleBottom1);
						CurrentData.enemyCount = floors.middleBottom1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);

					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.middleBottom2;
						CurrentData.previousRoom.add(floors.middleBottom2);
						CurrentData.enemyCount = floors.middleBottom2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);

					}
					else if (randomRoom == 3) {
						CurrentData.currentFloor = floors.middleBottom3;
						CurrentData.previousRoom.add(floors.middleBottom3);
						CurrentData.enemyCount = floors.middleBottom3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);

					}
				}
			} 
			else if (tempObject.getID() == ID.DoorUp && (CurrentData.roomsX == 0 && CurrentData.roomsY == -1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY++;
					//handler.clearGraphics(null);
					x = 378;
					y = 550;
					key = "Up";
					CurrentData.currentFloor = floors.middle;
					CurrentData.enemyCount = floors.middleEnemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			} 
			else if (tempObject.getID() == ID.DoorRight && (CurrentData.roomsX == 0 && CurrentData.roomsY == -1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX++;
					//handler.clearGraphics(null);
					x = 40;
					y = 288;
					key = "Right";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.bottomRight1)) {
						CurrentData.currentFloor = floors.bottomRight1;
						CurrentData.enemyCount = floors.bottomRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.bottomRight2)) {
						CurrentData.currentFloor = floors.bottomRight2;
						CurrentData.enemyCount = floors.bottomRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleRight2)) {
						CurrentData.currentFloor = floors.bottomRight1;
						CurrentData.enemyCount = floors.bottomRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleRight3) || CurrentData.previousRoom.contains(floors.middleRight1)) {
						CurrentData.currentFloor = floors.bottomRight2;
						CurrentData.enemyCount = floors.bottomRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.bottomRight1;
						CurrentData.previousRoom.add(floors.bottomRight1);
						CurrentData.enemyCount = floors.bottomRight1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.bottomRight2;
						CurrentData.previousRoom.add(floors.bottomRight2);
						CurrentData.enemyCount = floors.bottomRight2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			}
			else if (tempObject.getID() == ID.DoorLeft && (CurrentData.roomsX == 1 && CurrentData.roomsY == -1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX--;
					//handler.clearGraphics(null);
					randomRoom = r.nextInt(2 - 0) + 1;
					x = 720;
					y = 288;
					key = "Left";
					if (CurrentData.previousRoom.contains(floors.middleBottom1)) {
						CurrentData.currentFloor = floors.middleBottom1;
						CurrentData.enemyCount = floors.middleBottom1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleBottom2)) {
						CurrentData.currentFloor = floors.middleBottom2;
						CurrentData.enemyCount = floors.middleBottom2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.middleBottom1;
						CurrentData.previousRoom.add(floors.middleBottom1);
						CurrentData.enemyCount = floors.middleBottom1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.middleBottom2;
						CurrentData.previousRoom.add(floors.middleBottom2);
						CurrentData.enemyCount = floors.middleBottom2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			}
			else if (tempObject.getID() == ID.DoorLeft && (CurrentData.roomsX == 0 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX--;
					//handler.clearGraphics(null);
					x = 720;
					y = 288;
					key = "Left";
					randomRoom = r.nextInt(3 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.middleLeft1)) {
						CurrentData.currentFloor = floors.middleLeft1;
						CurrentData.enemyCount = floors.middleLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleLeft2)) {
						CurrentData.currentFloor = floors.middleLeft2;
						CurrentData.enemyCount = floors.middleLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleLeft3)) {
						CurrentData.currentFloor = floors.middleLeft3;
						CurrentData.enemyCount = floors.middleLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.topLeft1)) {
						randomRoom = r.nextInt(2 - 0) + 1;
						if (randomRoom == 1) {
							CurrentData.currentFloor = floors.middleLeft1;
							CurrentData.previousRoom.add(floors.middleLeft1);
							CurrentData.enemyCount = floors.middleLeft1Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
						else if (randomRoom == 2) {
							CurrentData.currentFloor = floors.middleLeft3;
							CurrentData.previousRoom.add(floors.middleLeft3);
							CurrentData.enemyCount = floors.middleLeft3Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
					}
					else if (CurrentData.previousRoom.contains(floors.topLeft3)) {
						CurrentData.currentFloor = floors.middleLeft2;
						CurrentData.previousRoom.add(floors.middleLeft2);
						CurrentData.enemyCount = floors.middleLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.bottomLeft1)) {
						CurrentData.currentFloor = floors.middleLeft1;
						CurrentData.previousRoom.add(floors.middleLeft1);
						CurrentData.enemyCount = floors.middleLeft1Enemies;
					}
					else if (CurrentData.previousRoom.contains(floors.bottomLeft3)) {
						randomRoom = r.nextInt(2 - 0) + 1;
						if (randomRoom == 1) {
							CurrentData.currentFloor = floors.middleLeft2;
							CurrentData.previousRoom.add(floors.middleLeft2);
							CurrentData.enemyCount = floors.middleLeft2Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
						else if (randomRoom == 2) {
							CurrentData.currentFloor = floors.middleLeft3;
							CurrentData.previousRoom.add(floors.middleLeft3);
							CurrentData.enemyCount = floors.middleLeft3Enemies;
							Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
						}
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.middleLeft1;
						CurrentData.previousRoom.add(floors.middleLeft1);
						CurrentData.enemyCount = floors.middleLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.middleLeft2;
						CurrentData.previousRoom.add(floors.middleLeft2);
						CurrentData.enemyCount = floors.middleLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 3) {
						CurrentData.currentFloor = floors.middleLeft3;
						CurrentData.previousRoom.add(floors.middleLeft3);
						CurrentData.enemyCount = floors.middleLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			}
			else if (tempObject.getID() == ID.DoorRight && (CurrentData.roomsX == -1 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX++;
					//handler.clearGraphics(null);
					x = 40;
					y = 288;
					key = "Right";
					CurrentData.currentFloor = floors.middle;
					CurrentData.enemyCount = floors.middleEnemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			}
			else if (tempObject.getID() == ID.DoorDown && (CurrentData.roomsX == -1 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY--;
					//handler.clearGraphics(null);
					x = 378;
					y = 30;
					key = "Down";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.bottomLeft1)) {
						CurrentData.currentFloor = floors.bottomLeft1;
						CurrentData.enemyCount = floors.bottomLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.bottomLeft2)) {
						CurrentData.currentFloor = floors.bottomLeft2;
						CurrentData.enemyCount = floors.bottomLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleBottom2)) {
						CurrentData.currentFloor = floors.bottomLeft2;
						CurrentData.enemyCount = floors.bottomLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleBottom3)) {
						CurrentData.currentFloor = floors.bottomLeft2;
						CurrentData.enemyCount = floors.bottomLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.bottomLeft1;
						CurrentData.previousRoom.add(floors.bottomLeft1);
						CurrentData.enemyCount = floors.bottomLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.bottomLeft2;
						CurrentData.previousRoom.add(floors.bottomLeft2);
						CurrentData.enemyCount = floors.bottomLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			else if (tempObject.getID() == ID.DoorUp && (CurrentData.roomsX == -1 && CurrentData.roomsY == -1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY++;
					//handler.clearGraphics(null);
					x = 378;
					y = 550;
					key = "Up";
					CurrentData.currentFloor = floors.middleLeft1;
					CurrentData.previousRoom.add(floors.middleLeft1);
					CurrentData.enemyCount = floors.middleLeft1Enemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			}
			else if (tempObject.getID() == ID.DoorLeft && (CurrentData.roomsX == 0 && CurrentData.roomsY == -1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX--;
					//handler.clearGraphics(null);
					x = 720;
					y = 288;
					key = "Left";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.bottomLeft1)) {
						CurrentData.currentFloor = floors.bottomLeft1;
						CurrentData.enemyCount = floors.bottomLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.bottomLeft3)) {
						CurrentData.currentFloor = floors.bottomLeft3;
						CurrentData.enemyCount = floors.bottomLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleLeft1)) {
						CurrentData.currentFloor = floors.bottomLeft1;
						CurrentData.enemyCount = floors.bottomLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleLeft2) || CurrentData.previousRoom.contains(floors.middleLeft3)) {
						CurrentData.currentFloor = floors.bottomLeft3;
						CurrentData.enemyCount = floors.bottomLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.bottomLeft1;
						CurrentData.previousRoom.add(floors.bottomLeft1);
						CurrentData.enemyCount = floors.bottomLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.bottomLeft3;
						CurrentData.previousRoom.add(floors.bottomLeft3);
						CurrentData.enemyCount = floors.bottomLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			else if (tempObject.getID() == ID.DoorRight && (CurrentData.roomsX == -1 && CurrentData.roomsY == -1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX++;
					//handler.clearGraphics(null);
					x = 40;
					y = 288;
					key = "Right";
					CurrentData.currentFloor = floors.middleBottom1;
					CurrentData.previousRoom.add(floors.middleBottom1);
					CurrentData.enemyCount = floors.middleBottom1Enemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);

				}
			}
			else if (tempObject.getID() == ID.DoorUp && (CurrentData.roomsX == -1 && CurrentData.roomsY == 0)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY++;
					//handler.clearGraphics(null);
					x = 378;
					y = 550;
					key = "Up";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.topLeft1)) {
						CurrentData.currentFloor = floors.topLeft1;
						CurrentData.enemyCount = floors.topLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.topLeft2)) {
						CurrentData.currentFloor = floors.topLeft2;
						CurrentData.enemyCount = floors.topLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleTop3)) {
						CurrentData.currentFloor = floors.topLeft1;
						CurrentData.enemyCount = floors.topLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleTop1) || CurrentData.previousRoom.contains(floors.middleTop2)) {
						CurrentData.currentFloor = floors.topLeft2;
						CurrentData.enemyCount = floors.topLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.topLeft1;
						CurrentData.previousRoom.add(floors.topLeft1);
						CurrentData.enemyCount = floors.topLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.topLeft2;
						CurrentData.previousRoom.add(floors.topLeft2);
						CurrentData.enemyCount = floors.topLeft2Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			}
			else if (tempObject.getID() == ID.DoorDown && (CurrentData.roomsX == -1 && CurrentData.roomsY == 1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsY--;
					//handler.clearGraphics(null);
					randomRoom = r.nextInt(2 - 0) + 1;
					x = 378;
					y = 30;
					key = "Down";
					if (CurrentData.previousRoom.contains(floors.middleLeft1)) {
						CurrentData.currentFloor = floors.middleLeft1;
						CurrentData.enemyCount = floors.middleLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleLeft3)) {
						CurrentData.currentFloor = floors.middleLeft3;
						CurrentData.enemyCount = floors.middleLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.middleLeft1;
						CurrentData.previousRoom.add(floors.middleLeft1);
						CurrentData.enemyCount = floors.middleLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.middleLeft3;
						CurrentData.previousRoom.add(floors.middleLeft3);
						CurrentData.enemyCount = floors.middleLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			}
			else if (tempObject.getID() == ID.DoorLeft && (CurrentData.roomsX == 0 && CurrentData.roomsY == 1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX--;
					//handler.clearGraphics(null);
					x = 720;
					y = 288;
					key = "Left";
					randomRoom = r.nextInt(2 - 0) + 1;
					if (CurrentData.previousRoom.contains(floors.topLeft1)) {
						CurrentData.currentFloor = floors.topLeft1;
						CurrentData.enemyCount = floors.topLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.topLeft3)) {
						CurrentData.currentFloor = floors.topLeft3;
						CurrentData.enemyCount = floors.topLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleLeft1) || CurrentData.previousRoom.contains(floors.middleLeft3)) {
						CurrentData.currentFloor = floors.topLeft1;
						CurrentData.enemyCount = floors.topLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (CurrentData.previousRoom.contains(floors.middleLeft2)) {
						CurrentData.currentFloor = floors.topLeft3;
						CurrentData.enemyCount = floors.topLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 1) {
						CurrentData.currentFloor = floors.topLeft1;
						CurrentData.previousRoom.add(floors.topLeft1);
						CurrentData.enemyCount = floors.topLeft1Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
					else if (randomRoom == 2) {
						CurrentData.currentFloor = floors.topLeft3;
						CurrentData.previousRoom.add(floors.topLeft3);
						CurrentData.enemyCount = floors.topLeft3Enemies;
						Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
					}
				}
			} 
			else if (tempObject.getID() == ID.DoorRight && (CurrentData.roomsX == -1 && CurrentData.roomsY == 1)) {
				if (getBounds().intersects(tempObject.getBounds())) {
					CurrentData.roomsX++;
					//handler.clearGraphics(null);
					x = 40;
					y = 288;
					key = "Right";
					CurrentData.currentFloor = floors.middleTop3;
					CurrentData.previousRoom.add(floors.middleTop3);
					CurrentData.enemyCount = floors.middleTop3Enemies;
					Game.switchRoom(CurrentData.currentFloor, x, y, key, CurrentData.enemyCount, damage);
				}
			}
		}
	}

	// the check attack method is used to draw the hit box for the sword attack when the attack key is pressed, it is called from the key inputs class. the method is also used to damage the enemy and knock the enemy back when the sword hit box collides with the enemies hit box
	public void checkAttacks() {

		Rectangle cb = getBounds(); // gets the hit box for the player ands stores in in a rectangle - collision box
		Rectangle ar = new Rectangle(); // creates a new rectangle - attack range
		int arSize = 32; // creates an integer for the range of the attack
		ar.width = arSize; // sets the width of the attack range rectangle equal to the integer arSize
		ar.height = arSize; // sets the height of the attack range rectangle equal to the integer arSize

		//this meets objective 8
		if (key.equals("Up")) { // if the player is facing upwards place the rectangle 32 pixels above the players hit box
			ar.x = cb.x + cb.width / 2 - arSize / 2;
			ar.y = cb.y - arSize;
		} else if (key.equals("Down")) { // if the player is facing downwards place the rectangle 32 pixels below the players hit box
			ar.x = cb.x + cb.width / 2 - arSize / 2;
			ar.y = cb.y + cb.height;
		} else if (key.equals("Left")) { // if the player is facing left place the rectangle 32 pixels to the left of the players hit box
			ar.x = cb.x - arSize;
			ar.y = cb.y + cb.height / 2 - arSize / 2;
		} else if (key.equals("Right")) { // if the player is facing right place the rectangle 32 pixels to the right o the players hit box
			ar.x = cb.x + cb.width;
			ar.y = cb.y + cb.height / 2 - arSize / 2;
		} else {

			return; // else skip the rest of the method
		}

		for (int i = 0; i < handler.object.size(); i++) { // loops through all the objects in the list of game objects
			GameObject tempObject = handler.object.get(i); // stores the current item game object in a temporary game object
			
			if (tempObject.equals(this)) {
				continue;
			} else if (tempObject.getBounds().intersects(ar) && (tempObject.getID() == ID.Enemy|| tempObject.getID() == ID.BossEnemy)) { //runs the indented code if the ID of the temporary object has the ID boss enemy or enemy
				int EnemyX = tempObject.getX();  // gets the x coordinate of the enemy 
				int EnemyY = tempObject.getY(); // gets the y coordinate of the enemy 
				// this is an example of casting
				Enemy tempEnemy = (Enemy) tempObject; // casts the temporary object to the enemy data type so it can use some of its the methods
				tempEnemy.hurt(damage); // calls the hurt method in the enemy class parsing the damage
				tempEnemy.SetHit(true); // sets the enemy's hit variable to true
				if(key.equals("Up")) { // if the player is facing up knock back the enemy upwards
					tempEnemy.SetVelY(-3);
					tempEnemy.setY(EnemyY += (tempEnemy.getVelY()));
				}
				else if(key.equals("Down")) { // if the player is facing down knock back the enemy down 
					tempEnemy.SetVelY(3);
					tempEnemy.setY(EnemyY += (tempEnemy.getVelY()));
				}
				else if(key.equals("Left")) {  // if the player is facing left knock back the enemy left
					tempEnemy.SetVelX(-3);
					tempEnemy.setX(EnemyX += (tempEnemy.getVelX()));
				}
				else if(key.equals("Right")) { // if the player is facing right knock back the enemy right
					tempEnemy.SetVelX(3);
					tempEnemy.setX(EnemyX += (tempEnemy.getVelX()));
				}
				// creates a starts a timer that will keep the enemy being knocked back for half a second, after the timer is up the x and y velocities are set to 0 and hte hit boolean is set to false
				ActionListener taskPerformer = new ActionListener() {
			          public void actionPerformed(ActionEvent evt) {
			        	  tempEnemy.SetVelX(0);
			        	  tempEnemy.SetVelY(0);
			        	  tempEnemy.SetHit(false);
			          }
				};
				Timer timer = new Timer(500 ,taskPerformer);
				timer.setRepeats(false);
				timer.start();
			}
		}
	}

	// the render method for the player is responsible for drawing all of the animation and images to the screen
	public void render(Graphics g) {
		if (!attacking) { // if the player is not attacking then the walking animation will be played
			if (velY != 0) { //if the player is moving horizontally run the indented code
				if (key.equals("Down")) { // if the player is facing downwards then draw the downwards walk animation
					playerWalkDown.drawAnimation(g, x, y, 32, 32);
				} else if (key.equals("Up")) { // if the player is facing upwards then draw the upwards walk animation
					playerWalkUp.drawAnimation(g, x, y, 32, 32);
				}
			} else {
				if (key.equals("Down")) { // if the player is not moving but is facing down the facing down idle image is drawn
					g.drawImage(tex.player[0], x, y, 32, 32, null);
				} else if (key.equals("Up")) { // if the player is not moving but is facing up the facing up idle image is drawn
					g.drawImage(tex.player[6], x, y, 32, 32, null);
				}
			}
			if (velX != 0) { // if the player is moving horizontally
				if (key.equals("Left")) { // if the player is facing left then the walking left animation is drawn
					playerWalkLeft.drawAnimation(g, x, y, 32, 32);
				} else if (key.equals("Right")) { // if the player is facing right then the walking right animation is drawn
					playerWalkRight.drawAnimation(g, x, y, 32, 32);
				}
			} else {
				if (key.equals("Left")) { // if the player is not moving but facing left then the left idle image is drawn
					g.drawImage(tex.player[2], x, y, 32, 32, null);
				} else if (key.equals("Right")) { // if the player is not moving but facing right then the right idle image is drawn
					g.drawImage(tex.player[4], x, y, 32, 32, null);
				}
			}
		} else { // if the player is attacking
			if (key.equals("Down")) { // if the player is facing downwards then draw the down attack animation
				downAttack.drawAnimation(g, x, y, 32, 64);
			} else if (key.equals("Up")) { // if the player is facing upwards then draw the up attack animation
				upAttack.drawAnimation(g, x, y - 32, 32, 64);
			} else if (key.equals("Left")) { // if the player is facing left then draw the left attack animation
				leftAttack.drawAnimation(g, x - 32, y, 64, 32);
			} else if (key.equals("Right")) { // if the player is facing right then draw the right attack animation
				rightAttack.drawAnimation(g, x, y, 64, 32);
			}	
		}
	}

	// this method is called when the user is hit by an enemies attacks it takes in a damage and subtracts that damage from the current health the player is on, there is also a timer to prevent the user from being hit repeatedly
	// this method meets objective 7
	public void hurt(int damage) {
		flinchTimer += System.currentTimeMillis() - lastAttackedTimer; // adds the time since the last attack to the flinch timer
		lastAttackedTimer = System.currentTimeMillis(); // sets the last attack timer equal to the current time
		if (flinchTimer < flinchcd) // if the flinch timer is less than the flinch cool down then not enough time has passed for to the player to be able to be attacked again
			return;
		health -= damage; // takes the damage away from the current health
		CurrentData.currentHealth += damage; // adds the damage to the amount of health missing
		flinchTimer = 0; // resets the flinch timer
		if (health <= 0) { // if the health is less than or equal to 0 call the die method
			die();
		}
	}

	// the die method is called when the player loses all of their health it is used to set variable that are needed to end the game
	private void die() {
		Sound.stop(); // calls the stop method in the sound class to end the background music
		CurrentData.alive = false; // sets the alive variable to false
		
	}
	// this method is called when the player levels up, it is used to increase the players stats such as damage and health
	private void levelUp() {
		damage +=  5; // adds 5 to the damage 
		CurrentData.playerLevel += 1; // adds one to the players level
		maxHealth = 200 + (10 * (CurrentData.playerLevel - 1)); // adds 10 to the max health
		health = maxHealth; // resets the users health so they get a full heal
		CurrentData.currentHealth = 0; // sets the current missing health to 0
		maxEXP = 1000*CurrentData.playerLevel; // increase the maximum amount of experience needed to level up
	}
}