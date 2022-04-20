package GameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import Game.AStar;
import Game.CurrentData;
import Game.GameObject;
import Game.ID;
import Game.Texture;
import Window.Animation;
import Window.Game;
import Window.HUD;
import Window.Handler;
import Window.MapArrays;

public class Enemy extends GameObject { // example of inheritance

	Handler handler; // creates an instance of the handler class
	MapArrays floors; // creates an instance of the map arrays class
	
	Texture tex = Game.getInstance(); // calls the get instance method in the game class

	//public static String facing; // defines a string variable which will hold the direction the enemy is facing
	private long lastAttackTimer, attackcd = 3000, attackTimer = attackcd; // long variables that will be used to limit how face the enemy can attack
	private Animation enemyWalkUp, enemyWalkRight, enemyWalkLeft, enemyWalkDown, bossWalkUp, bossWalkRight, bossWalkLeft, bossWalkDown; // creates a number of variables with the data dyata type animation. they will be used to store each of the animations for the enemy
	Random rand = new Random(); // creates a new random object

	// the purpose of the constructor is to set up the enemy by assigning variables such as the variable for the health and damage and also creating the animations
	public Enemy(int x, int y, ID id, Handler handler, int health, MapArrays floors, String key, int damage, boolean hit) {
		super(x, y, id); // uses the constructor in the game object class to assign these variables
		this.handler = handler; // sets the handler object equal to the one that was parsed
		this.health = health; // sets the health variable equal to the one that was parsed
		this.floors = floors; // sets the floors object equal to the one that was parsed
		this.key = key; // sets the key variable equal to the one parsed, the key variable is the direction the enemy is facing
		this.damage = damage; // sets the damage variable equal to the one that was parsed
		maxHealth = health; // sets the max health variable equal to the health that was parsed
		this.hit = hit; // sets the hit boolean equal to the state parsed
		
		// creates the animations for the enemy movement by calling the animation constructor, each one is parsed the speed of the animation and the images needed for it from the textures class
		enemyWalkUp = new Animation(10, tex.enemy[0], tex.enemy[1]);
		enemyWalkRight = new Animation(10, tex.enemy[2], tex.enemy[3]);
		enemyWalkLeft = new Animation(10, tex.enemy[4], tex.enemy[5]);
		enemyWalkDown = new Animation(10, tex.enemy[6], tex.enemy[7]);
		
		// creates the animations for the boss enemy movement by calling the animation constructor, each one is parsed the speed of the animation and the images needed for it from the textures class
		bossWalkUp = new Animation(10, tex.boss[0], tex.boss[1]);
		bossWalkRight = new Animation(10, tex.boss[2], tex.boss[3]);
		bossWalkLeft = new Animation(10, tex.boss[4], tex.boss[5]);
		bossWalkDown = new Animation(10, tex.boss[6], tex.boss[7]);
	}

	// this method draws a rectangle around the enemy, to act as a hit box
	public Rectangle getBounds() {
		Rectangle rectangle;
		if (id == ID.Enemy) {
			rectangle = new Rectangle(x, y, 32, 32); // if the ID of the object is Enemy then the hit box is 32*32
		} else {
			rectangle = new Rectangle(x, y, 64, 64); // if the ID of the object is Boss Enemy then the hit box is 64*64
		}
		return rectangle; // returns the rectangle
	}

	// the hurt method is used to reduce the enemy health after it is hit by one of the players attacks, it takes an integer for the damage
	// method meets objective 8
	public void hurt(int damage) {
		health -= damage; // sets the health equal to the health - the damage
		if (health > 0) //give 10 score if the enemy is hit, but is still alive
			HUD.score += 10;
		if (health <= 0) { 
			active = false; // set active to false so the object is removed
			die(); // calls the die method
		}
	}
	
	// the attack method is responsible for doing the boss enemy's attacks, the method also ensures that there is a delay between the attacks // this meets objective 10
	public void attack() {
		attackTimer += System.currentTimeMillis() - lastAttackTimer; // adds the current time to the attack timer
		lastAttackTimer = System.currentTimeMillis(); // sets the last attack to the current time
		if (attackTimer < attackcd) // if the attack timer is less than the attack cool down then not enough timer has passed to attack again
			return;
		attacking = true; // set attack to true
		if (attacking == true) { // if attacking is equal to true add a number of magic bolt objects with the ID boss attack. the velocities for each are slightly different to ensure that the attacks are fired of in a ring from the boss enemy
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, 3, 0, key, handler, damage));
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, -3, 0, key, handler, damage));
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, 0, 3, key, handler, damage));
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, 0, -3, key, handler, damage));
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, 2, -2, key, handler, damage));
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, 2, 2, key, handler, damage));
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, -2, 2, key, handler, damage));
			handler.addObject(new MagicBolt(x + 24, y + 22, ID.BossAttack, -2, -2, key, handler, damage));
		}
		attackTimer = 0; // resets the attack timer
	}

	// the die method is responsible for giving score and experience to the user and changing the enemy counter for the room the enemy died in. In addition it is also responsible for adding the trap door
	private void die() {
		if (id == ID.BossEnemy) { // if the enemy was a boss enemy then add a trap door when the enemy dies
			handler.addObject(new Door(96, 96, ID.TrapDoor, handler)); // this meets objective 12 
			HUD.score += 400; // adds 400 to the score // this meets objective 9
			CurrentData.currentEXP += 500; // adds 500 to the current experience points variable
			CurrentData.floorCleared = true; // sets the floor cleared variable to true
		} else { // if the enemy was a basic enemy add 100 to the score and experience points
			HUD.score += 100; 
			CurrentData.currentEXP += 100; // this meets objective 9
		}
		int heartDrop = rand.nextInt(100) + 1; // creates a random number between 1 and 100
		if (heartDrop <= 10) { // if the number was less than 10 (10% chance) add a health drop on the enemies location
			handler.addObject(new HealthPickup(x, y, ID.Heart));
		}
		// the following if functions are used to check what the room the user if currently in and subtract one from the enemy count of the room the user is in when an enemy dies
		if (CurrentData.currentFloor == floors.middle) {
			floors.middleEnemies -= 1;
			CurrentData.enemyCount = floors.middleEnemies;
		} else if (CurrentData.currentFloor == floors.middleRight1) {
			floors.middleRight1Enemies -= 1;
			CurrentData.enemyCount = floors.middleRight1Enemies;
		} else if (CurrentData.currentFloor == floors.middleRight2) {
			floors.middleRight2Enemies -= 1;
			CurrentData.enemyCount = floors.middleRight2Enemies;
		} else if (CurrentData.currentFloor == floors.middleRight3) {
			floors.middleRight3Enemies -= 1;
			CurrentData.enemyCount = floors.middleRight3Enemies;
		} else if (CurrentData.currentFloor == floors.bottomRight1) {
			floors.bottomRight1Enemies -= 1;
			CurrentData.enemyCount = floors.bottomRight1Enemies;
		} else if (CurrentData.currentFloor == floors.bottomRight2) {
			floors.bottomRight2Enemies -= 1;
			CurrentData.enemyCount = floors.bottomRight2Enemies;
		} else if (CurrentData.currentFloor == floors.bottomRight3) {
			floors.bottomRight3Enemies -= 1;
			CurrentData.enemyCount = floors.bottomRight3Enemies;
		} else if (CurrentData.currentFloor == floors.topRight1) {
			floors.topRight1Enemies -= 1;
			CurrentData.enemyCount = floors.topRight1Enemies;
		} else if (CurrentData.currentFloor == floors.topRight2) {
			floors.topRight2Enemies -= 1;
			CurrentData.enemyCount = floors.topRight2Enemies;
		} else if (CurrentData.currentFloor == floors.topRight3) {
			floors.topRight3Enemies -= 1;
			CurrentData.enemyCount = floors.topRight3Enemies;
		} else if (CurrentData.currentFloor == floors.middleTop1) {
			floors.middleTop1Enemies -= 1;
			CurrentData.enemyCount = floors.middleTop1Enemies;
		} else if (CurrentData.currentFloor == floors.middleTop2) {
			floors.middleTop2Enemies -= 1;
			CurrentData.enemyCount = floors.middleTop2Enemies;
		} else if (CurrentData.currentFloor == floors.middleTop3) {
			floors.middleTop3Enemies -= 1;
			CurrentData.enemyCount = floors.middleTop3Enemies;
		} else if (CurrentData.currentFloor == floors.middleBottom1) {
			floors.middleBottom1Enemies -= 1;
			CurrentData.enemyCount = floors.middleBottom1Enemies;
		} else if (CurrentData.currentFloor == floors.middleBottom2) {
			floors.middleBottom2Enemies -= 1;
			CurrentData.enemyCount = floors.middleBottom2Enemies;
		} else if (CurrentData.currentFloor == floors.middleBottom3) {
			floors.middleBottom3Enemies -= 1;
			CurrentData.enemyCount = floors.middleBottom3Enemies;
		} else if (CurrentData.currentFloor == floors.middleLeft1) {
			floors.middleLeft1Enemies -= 1;
			CurrentData.enemyCount = floors.middleLeft1Enemies;
		} else if (CurrentData.currentFloor == floors.middleLeft2) {
			floors.middleLeft2Enemies -= 1;
			CurrentData.enemyCount = floors.middleLeft2Enemies;
		} else if (CurrentData.currentFloor == floors.middleLeft3) {
			floors.middleLeft3Enemies -= 1;
			CurrentData.enemyCount = floors.middleLeft3Enemies;
		} else if (CurrentData.currentFloor == floors.bottomLeft1) {
			floors.bottomLeft1Enemies -= 1;
			CurrentData.enemyCount = floors.bottomLeft1Enemies;
		} else if (CurrentData.currentFloor == floors.bottomLeft2) {
			floors.bottomLeft2Enemies -= 1;
			CurrentData.enemyCount = floors.bottomLeft2Enemies;
		} else if (CurrentData.currentFloor == floors.bottomLeft3) {
			floors.bottomLeft3Enemies -= 1;
			CurrentData.enemyCount = floors.bottomLeft3Enemies;
		} else if (CurrentData.currentFloor == floors.topLeft1) {
			floors.topLeft1Enemies -= 1;
			CurrentData.enemyCount = floors.topLeft1Enemies;
		} else if (CurrentData.currentFloor == floors.topLeft2) {
			floors.topLeft2Enemies -= 1;
			CurrentData.enemyCount = floors.topLeft2Enemies;
		} else if (CurrentData.currentFloor == floors.topLeft3) {
			floors.topLeft3Enemies -= 1;
			CurrentData.enemyCount = floors.topLeft3Enemies;
		}
		// this code helps meet objective 4
		if (CurrentData.enemyCount == 0) { // checks whether all the enemies have been killed
			for (int i = 0; i < CurrentData.currentFloor.length; i++) { // loops through the 2D array of the room that is currently loaded
				for (int j = 0; j < CurrentData.currentFloor[i].length; j++) {
					if (CurrentData.currentFloor[i][j] == 2) {
						handler.addObject(new Door(j * 32 - 4, i * 32 - 14, ID.DoorUp, handler)); // if the element is a 2 add an up door at the specified location
					}
					if (CurrentData.currentFloor[i][j] == 3) {
						handler.addObject(new Door(j * 32 - 4, i * 32 - 14, ID.DoorDown, handler)); // if the element is a 3 add an down door at the specified location
					}
					if (CurrentData.currentFloor[i][j] == 4) {
						handler.addObject(new Door(j * 32 - 4, i * 32 - 14, ID.DoorLeft, handler)); // if the element is a 4 add an left door at the specified location
					}
					if (CurrentData.currentFloor[i][j] == 5) {
						handler.addObject(new Door(j * 32 - 4, i * 32 - 14, ID.DoorRight, handler));  // if the element is a 5 add an right door at the specified location
					}
				}
			}
		}
	}
	// the tick is method is responsible for updating import things for the enemies such as the AStar algorithm that will be used to determine the shortest path to the player, the enemies movement and the direction they are facing and other things
	public void tick() {
		double playerX, playerY; // double variables that will be used to store the players x and y coordinates

		for (int i = 0; i < handler.object.size(); i++) { // loops through all the objects in the game objects list

			GameObject tempObject = handler.object.get(i); // stores the selected element in a temporary game object
			

			if (tempObject.getID() == ID.Warrior || tempObject.getID() == ID.Rogue || tempObject.getID() == ID.Mage) {
				playerX = tempObject.getX(); // gets the players x coordinate
				playerY = tempObject.getY(); // gets the players y coordinate
				
				Point point = new Point(x, y); //creates a point at the top left of the enemy
				
				if(id == ID.BossEnemy) {
					point.translate(32, 32); // if the enemy is a boss enemy then translate the point by 32 down and to the right, so the boss does not clip through some walls
				}
				
				else if(id == ID.Enemy) {
					if(key.equals("left") || key.equals("up")) {
						point.translate(32, 32);  // if the enemy is moving to the left or upwards translate the point to the bottom right of the enemy so it does not clip through some walls
					}
				}
				int playerCurrX = (int) Math.round((playerX + 4) /32); // the player current x is the the players x position along the 2D array
				int playerCurrY = (int) Math.round((playerY + 14)/32); // the players current y is the players y position along the 2D array
      			int currX = Math.round((point.x + 4) /32); //the current x is the enemies position along the 2D array
      			int	currY = Math.round((point.y + 14) /32); // the current y is the enemies position along the 2D array

				Point point2 = new Point(currX, currY); // stores the players position in the 2D array in a point
		
				List<Point> path = AStar.findPath(point2, new Point(playerCurrX,playerCurrY)); // calls the find path in the AStar class parsing the enemies position, the starting point and the players position, the end point.
				
				int pathX = 0;
				int pathY = 0;
				
				try {
					pathX = path.get(1).x; // gets the next node x coordinate
					pathY = path.get(1).y; // gets the next node y coordinate
			
				}
				catch(NullPointerException e){
					
				}catch(IndexOutOfBoundsException e) {
					
				}
				
				try {
					if(hit == false) { // if the enemy is not being hit run the code
						velX = 0; 
						velY = 0;
						// this code helps meet objective 7
						if(pathX > path.get(0).x) { // if the next x coordinates are greater than the current x coordinate move right
							velX = 1; // set x velocity to 1 
						} else if(pathX < path.get(0).x) { // if the next x coordinates are less than the current x coordinate move left
							velX = -1; // set x velocity to -1
						} else if(pathY > path.get(0).y) { // if next y coordinate is greater than the current y coordinate move downwards
							velY = 1; // set y velocity to 1
						} else if(pathY < path.get(0).y) { // if next y coordinate is less than next y coordinate move upwards
							velY = -1; // sets y velocity to -1
						}	
					}
				}
				catch(NullPointerException e) {
					
				} catch (IndexOutOfBoundsException e) {}
				
				
				x += velX; // responsible for moving the enemies horizontally, adds the x velocity onto the x coordinate
				y += velY; // responsible for moving the enemies vertically, adds the y velocity onto the y coordinate
				
				
				if(velX<0) { //if velX is negative then the enemy is facing left
					key = "left";
				}
				if(velX>0) { // if velX is positive then the enemy is facing right
					key = "right"; 
				}
				if(velY<0) { // if velY is negative then the enemy is facing upwards
					key = "up";
				}
				if(velY>0) { // if velY is positive then the enemy is facing downwards
					key = "down"; 
				}
			}
		}
		// once the player accumulates 1600 or more points and the floor isn't yet been cleared then the middle room enemy count is set to 1 // this meets objective 10
		if (HUD.score >= 1600 * CurrentData.Level && CurrentData.floorCleared == false) {
			floors.middleEnemies = 1;
		}


		if (id == ID.BossEnemy) { // calls the attack method if the id of the enemy is a boss enemy 
			attack();
		}
		collision(); // calls the collision method
		
		// calls the run animation method for each of the enemy animations
		enemyWalkDown.runAnimation(); 
		enemyWalkLeft.runAnimation();
		enemyWalkRight.runAnimation();
		enemyWalkUp.runAnimation();
		bossWalkDown.runAnimation();
		bossWalkLeft.runAnimation();
		bossWalkRight.runAnimation();
		bossWalkUp.runAnimation();
		
	}
	//the render method is responsible for drawing the different animations to the screen along with the health bars for the enemies
	public void render(Graphics g) {
		int healthPercentage; // creates an integer for the health percentage, to store the health as a percentage of the max health 
		if (id == ID.Enemy) {
			healthPercentage = (int) (((float) 32 / maxHealth) * (float) health); // this calculation gets the health as a percentage of the max health, 32 is divided by the max health as that is the width of the bar
		} else {
			healthPercentage = (int) (((float) 64 / maxHealth) * (float) health); // this calculation gets the health as a percentage of the max health, 644 is divided by the max health as that is the width of the bar
		}
		if (id == ID.Enemy) { // if the enemy is a basic enemy and if the enemy is moving horizontally, run the indented code
			if (velY != 0) {
				if (key.equals("down")) {
					enemyWalkDown.drawAnimation(g, x, y, 32, 32); // if the enemy is facing down draw the walk down animation by calling the draw animation method
				} else if (key.equals("up")) {
					enemyWalkUp.drawAnimation(g, x, y, 32, 32); // if the enemy is facing up draw the walk up animation by calling the draw animation method
				}
			} else {
				if (key.equals("down")) {
					g.drawImage(tex.enemy[6], x, y, 32, 32, null); // if the y velocity is 0 and if the enemy is facing down draw the down idle image for the enemy 
				} else if (key.equals("up")) {
					g.drawImage(tex.enemy[0], x, y, 32, 32, null); // if the y velocity is 0 and if the enemy is facing up draw the up idle image for the enemy 
				}
			}
			if (velX != 0) { //if the enemy is moving horizontally run the indented code
				if (key.equals("left")) {
					enemyWalkLeft.drawAnimation(g, x, y, 32, 32); // // if the enemy is facing left draw the walk left animation by calling the draw animation method
				} else if (key.equals("right")) {
					enemyWalkRight.drawAnimation(g, x, y, 32, 32); // if the enemy is facing right draw the walk right animation by calling the draw animation method
				}
			} else {
				if (key.equals("left")) {
					g.drawImage(tex.enemy[4], x, y, 32, 32, null); // if the x velocity is 0 and if the enemy is facing left draw the left idle image for the enemy 
				} else if (key.equals("right")) {
					g.drawImage(tex.enemy[2], x, y, 32, 32, null); // if the x velocity is 0 and if the enemy is facing right draw the right idle image for the enemy 
				}
			}

			g.setColor(new Color(100, 155, 105, 122)); // set the background colour for the health bar
			g.fillRect(x, y - 15, 32, 8); //  fill the rectangle at the given position and size with the colour above

			g.setColor(new Color(255, 5, 5, 122)); // set the colour for the health part of the bar
			g.fillRect(x, y - 15, healthPercentage, 8); // fill the bar at the given size and position with the colour above, the width of the bar represents the amount of health the enemy is on

			g.setColor(new Color(255, 255, 255, 122)); // sets the colour  for the border of the bar
			g.drawRect(x, y - 15, 32, 8); // draws the outline of a rectangle at the given position and size
		} else if(id == ID.BossEnemy){
			
			// draws animations and idle images for the boss, this is similar to drawing the animations of the enemy but the width and height are 64*64
			// if the boss is moving vertically draw the up or down walking animation depending on the direction the enemy is facing
			if (velY != 0) {
				if (key.equals("down")) {
					bossWalkDown.drawAnimation(g, x, y, 64, 64);
				} else if (key.equals("up")) {
					bossWalkUp.drawAnimation(g, x, y, 64, 64);
				}
			// if the boss enemy is not moving but is facing up or down draw the correct idle image depending on the direction the enemy is facing
			} else {
				if (key.equals("down")) {
					g.drawImage(tex.boss[6], x, y, 64, 64, null);
				} else if (key.equals("up")) {
					g.drawImage(tex.boss[0], x, y, 64, 64, null);
				}
			}
			// if the boss is moving horizontally draw the left or right animation depending on the direction the enemy is facing
			if (velX != 0) {
				if (key.equals("left")) {
					bossWalkLeft.drawAnimation(g, x, y, 64, 64);
				} else if (key.equals("right")) {
					bossWalkRight.drawAnimation(g, x, y, 64, 64);
				}
			// of the boss enemy is not moving but is facing left or right draw the appropriate idle image
			} else {
				if (key.equals("left")) {
					g.drawImage(tex.boss[4], x, y, 64, 64, null);
				} else if (key.equals("right")) {
					g.drawImage(tex.boss[2], x, y, 64, 64, null);
				}
			}

			g.setColor(new Color(100, 155, 105, 122)); // sets the background colour of the boss enemies health bar
			g.fillRect(x, y - 15, 64, 8); // fills the bar with the colour above

			g.setColor(new Color(255, 5, 5, 122)); // sets the colour of the health part of the bar
			g.fillRect(x, y - 15, healthPercentage, 8); // fills the bar with the colour, the width of the health rectangle depends on the health

			g.setColor(new Color(255, 255, 255, 122)); // sets the colour of the border of the bar
			g.drawRect(x, y - 15, 64, 8); // draws an outline around the bar of the colour above
			
		}

	}
	// the collision method tells the enemy how to behave if its hitbox and another objects hitbox intersect
	private void collision() {
		for (int i = 0; i < handler.object.size(); i++) { // loops through all  the elements in the game objects list

			GameObject tempObject = handler.object.get(i); // stores the selected game object in a temporary game object

			// if the ID for the temporary object is a wall or any kind or door then run the indented code
			if (tempObject.getID() == ID.Wall || tempObject.getID() == ID.DoorUp || tempObject.getID() == ID.DoorDown
					|| tempObject.getID() == ID.DoorLeft || tempObject.getID() == ID.DoorRight
					|| tempObject.getID() == ID.DoorClosed) {
				if (getBounds().intersects(tempObject.getBounds())) { // checks to see whether the hit box for the enemy and temporary object intersect
					if(hit == true) { // only run the indented code if the hit variable is true
						if(key.equals("left") || key.equals("right")){
							x -= velX; // stops the enemy going through the wall by subtracting the x velocity from the x coordinate
						}
						if(key.equals("up") || key.equals("down")){
							y -= velY; // stops the enemy going through the wall by subtracting the y velocity from the y coordinate
						}
					}	
				}
			}
		}
	}
}