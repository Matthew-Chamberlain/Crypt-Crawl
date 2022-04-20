package Window;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

import Game.AStar;
import Game.CurrentData;
import Game.ID;
import Game.KeyInput;
import Game.Sound;
import Game.Texture;
import GameObjects.Door;
import GameObjects.Enemy;
import GameObjects.Player;
import GameObjects.Wall;
import menu.ClassSelect;
import menu.GameOver;

public class Game extends Canvas implements Runnable{ // canvas is extended as some method from the canvas class need to be used, runnable is implemented as a thread is being used, runnable implements a run method that is needed to run the thread // runnable is an example of an interface
	
	public static final int WIDTH = 800, HEIGHT = 640; // the width and height of the window are constant values that cannot be changed
	//this is an example of a constant
	
	private Thread thread; // creates a thread
	private boolean running = false; // creates a boolean variable running
	
	private static Handler handler; // creates a static handler object 
	private HUD hud; // creates a hud object
	private static MapArrays floors; // creates a static map arrays object
	static Texture tex; // creates a static texture object
	private JFrame Game; // creates a private JFrame 
	private Sound sound; // creates a private sound object
	
	static Random rand = new Random(); // creates a new random object
	
	// the constructor for the game class is used to set variables to their default values so that the game can start
	public Game() {

		tex = new Texture(); // creates a new instance of the texture class //example of composition
		
		handler = new Handler(); // creates a new instance of the handler class // example of composition
		
		this.addKeyListener(new KeyInput(handler)); // adds a key listener to the game calling the constructor of the keyinput class parsing the handler // example of composition
		
		new GameWindow(WIDTH, HEIGHT, "Crypt Crawl", this); // calls the constructor for the game window class in order to set up the window

		hud = new HUD(); // creates an instance of the hud class // example of composition
		
		floors = new MapArrays(); // creates an instance of the map arrays class // example of composition
		
		//sound = new Sound(); // creates a new instance of the sound class // example of composition
		Sound.play("/Music.wav");
		
		HUD.score = 0; // sets the score variable in the hud class to 0
		CurrentData.currentFloor = floors.middle; // sets the current floor to the middle room
		CurrentData.roomsX = 0; // sets the x room variable to 0
		CurrentData.roomsY = 0; // sets the y room variable to 0
		CurrentData.enemyCount = floors.middleEnemies; // sets the enemy count to the enemy count of the middle room
		CurrentData.currentHealth = 0; // sets the current health to 0
		CurrentData.enemyCount = 0; // sets the enemy count to 0
		CurrentData.Level = 1; // sets the level to 1
		CurrentData.alive = true; // sets the alive variable to true
		CurrentData.playerLevel = 1; // sets the player level equal to 1
		CurrentData.currentEXP = 0; // sets the current experience points variable to 0
		
		AStar.initialise(CurrentData.currentFloor); // calls the initialise method in the A star class, parsing the current floor
		
		Player.key = "Down"; // sets the players facing direction to down
		int damage= 0; // initialises the damage variable
		// this code helps meet objective 5
		if(CurrentData.selectedClass == "Warrior") { // if the class is a warrior set the damage to 50
			damage = 50;
		}
		else if(CurrentData.selectedClass == "Rogue") { // if the class is a rogue set the damage to 40
			damage = 40;
		}
		else { // if the class is a mage set the damage to 20
			damage = 20;
		}
		switchRoom(CurrentData.currentFloor, 378, 288, Player.key, CurrentData.enemyCount, damage); // calls the switch room method, parsing in the current room, x and y coordinates, the direction the player is facing, the enemy count, and the damage
		
		
	}
	
	//this method is used to start the thread
	public synchronized  void start(JFrame game) {
		thread = new Thread(this); // creates a new thread 
		thread.start(); // runs the thread 
		running = true; // sets running to true
		Game = game; // sets the game JFrame equal to the one parsed in
	}
	
	// this method is responsible for ending the thread
	public synchronized void stop() {
		try {
			thread.join(); // waits for the thread to die
			running = false; // sets the running variable to false
		}catch(InterruptedException e) { // catches an interrupted exception
			e.printStackTrace();
		}
	}
	
	// the run method is responsible for running the game loop, and by extension the game, every tick of the game loop the tick and render methods are called which are needed for the game to run and be displayed
	public void run() {
		this.requestFocus(); // this ensures that the game gets the focus of the keyboard and mouse when the game opens
		long lastTime = System.nanoTime(); // gets the current time in nanoseconds
		double amountOfTicks = 60.0; // sets the number of ticks
		double ns = 1000000000 / amountOfTicks; // how many times 60 divides into one billion nanosecond (around 1 second)
		double delta = 0; // delta is the change in time
		long timer = System.currentTimeMillis(); // gets the current time in milliseconds
		int frames = 0; // sets the frame variable
		while(running){ // runs the indented code when running is equal to true
			long now = System.nanoTime(); // gets current time in nanoseconds during the current loop
			delta += (now - lastTime) / ns; // adds the amount of change in time since the last loop
			lastTime = now; // sets last time to now to prepare for the next loop
			while(delta >= 1) { // one tick of time has passed in the game which ensures there is a steady pause in the game loop
				//ensures that around 60 frames are loader per one second interval
			tick(); // calls the tick method
			delta--; // lowers the delta back to 0
			}
			if(running) {
				render(); // calls the render method
			}
				
			frames++; // notes that a frame has passed
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000; // add one thousand to the timer for next time 
				frames = 0; // reset the frame counter for the next second
			}
			if(CurrentData.alive == false) { // runs the code if the alive variable is false
				GameOver gameOver = new GameOver(HUD.score, CurrentData.Level); // calls the constructor the game over class, parsing the score of the user and the floor they got to
				Game.setVisible(false); // sets the frame to be invisible
				Game.dispose(); // causes the JFrame to be destroyed
				stop(); // calls the stop method to stop the thread
			}
		}
		stop();	// calls the stop method to stop the thread
	}
	
	// the purpose of the tick mehthod is to call the tick method of the handler and the hud every tick of the game loop which is necessary for updating important information that is needed for the game to be displayed and function correctly
	private void tick() {
		handler.tick(); // calls the tick method of the handler
		hud.tick(handler); // calls the tick method of the HUD parsing the handler
	}
	
	// the render method is used to render the visuals of the game. This includes the rendering the heads up display and the game objects
	private void render() {
		BufferStrategy bs = this.getBufferStrategy(); // creates a buffered strategy
		if(bs == null) { // if the value of the buffered strategy is null
			this.createBufferStrategy(3); // creates a buffered strategy that contains 3 frames. In a triple buffer strategy all the three frame are already loaded before they are rendered which makes swapping betweeb them faster and will make the gameplay smoother
			return;
		}
		
		Graphics g = bs.getDrawGraphics(); // creates a graphic object by calling the get draw graphics method that will allow objects to be drawn on the screen
		
		g.setColor(Color.black); // sets the background colour of the frame to black
		g.fillRect(0, 0, WIDTH, HEIGHT); // fills the background with the colour
		
		handler.render(g); // renders all of the game objects
		
		hud.render(g); // renders the heads up display
		
		g.dispose(); // destroys the graphics object
		bs.show(); // shows the current frame on the JFrame
	}
	
	// the clamp method is used to limit a given value between a given minimum and maximum value. This will be used to limit the health of the user between the maximum health and 0, so the health bar does not go over or below the maximum and minimum values
	public static int clamp(int val, int min, int max) {
		if(val >= max) // if the value is greater or equal to the max value, return the max value
			return val = max;
		else if(val <= min) // if the value is less than or equal to the min value, return the min value
			return val = min;
		else // if the value is between the minimum and maximum value, return the value
			return val;
	}
	
	// this method is responsible for drawing the room that the user is swapping to. It places all of the objects in the correct position.
	// this method helps meet objective 2 and 3 // this method is an example of modularisation of code
	public static void switchRoom(int[][] currentFloor, int x, int y, String key, int enemyCount, int damage) {
		int health; // initialises the health variable
		int EXP = 0; // sets the EXP variable to 0
		int xPosition; // initialises the x position variable 
		int yPosition; // initialises the y position variable
		int maxEXP = 1000*CurrentData.playerLevel; // sets the maximum EXP variable equal to 1000* the players current level, so it requires more and more experience points to level up // this meets objective 9
		handler.clearGraphics(); // clears the list of game objects so nothing can be drawn to the screen
		AStar.updateGrid(currentFloor); // calls the update grid method in the AStar class
		// this code meets objective 5
		if(CurrentData.selectedClass == "Warrior") { // if the player is a warrior set the health to 200 + 10* the players level - , this ensures that when the player levels up their damage will increase by 10
			health = 200 + (10 * (CurrentData.playerLevel - 1));
			handler.addObject(new Player(x, y, ID.Warrior, handler, floors, key, health - CurrentData.currentHealth, health, damage, EXP + CurrentData.currentEXP, maxEXP)); // adds the player to the game at the specified location with the correct health, damage and experience points
			}
		else if(CurrentData.selectedClass == "Rogue") { // if the player is a rogue set the health to 140 + 10* the players level - , this ensures that when the player levels up their damage will increase by 10
			health = 140 + (10 * (CurrentData.playerLevel - 1)); // adds the player to the game at the specified location with the correct health, damage and experience points
			handler.addObject(new Player(x, y, ID.Rogue, handler, floors, key, health - CurrentData.currentHealth, health, damage, EXP + CurrentData.currentEXP, maxEXP));
		}
		else if(CurrentData.selectedClass == "Mage") {
			health = 80 + (10 * (CurrentData.playerLevel - 1)); // if the player is a mage set the health to 80 + 10* the players level - , this ensures that when the player levels up their damage will increase by 10
			handler.addObject(new Player(x, y, ID.Mage, handler, floors, key, health - CurrentData.currentHealth, health, damage, EXP + CurrentData.currentEXP, maxEXP)); // adds the player to the game at the specified location with the correct health, damage and experience points
		}
		//this meets objective 12
		if(HUD.score >= 1600 * CurrentData.Level && CurrentData.currentFloor == floors.middle && CurrentData.floorCleared == true) { // draws the trap door to the screen if the player has above 1600 score * the current floor number (so it scales with how far the player is into the game) and if the player is in the middle room and if the player has beaten the bossm monster 
			handler.addObject(new Door(96, 96, ID.TrapDoor, handler));
		}
		
		// loops through the 2D array of the floor the user is currently in, depending on the value of the element a different object will be drawn
		for (int i = 0; i < CurrentData.currentFloor.length; i++){
		     for (int j = 0; j < CurrentData.currentFloor[i].length; j++){
		    	 
		    	 if(CurrentData.currentFloor[i][j] == 0){ // if the value of the element is a 0, add a wall at the specified position
		    		 handler.addObject(new Wall(j*32 - 4, i*32 - 14, ID.Wall, handler));
		    	 }
		    	 if(CurrentData.currentFloor[i][j] == 2 ) { // if the value of the element is a 2 and the enemy count of the room is 0, add an up door at the specified location
		    		 if(CurrentData.enemyCount == 0) {
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorUp, handler));	
		    		 }
		    		 else { // if the enemy count of the room is not equal to 0, add a closed door
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorClosed, handler));
		    		 }
		    		 
		    	 }
		    	 if(CurrentData.currentFloor[i][j] == 3 ) { // if the value of the element is a 3 and the enemy count of the room is 0, add a down door at the specified location
		    		 if(CurrentData.enemyCount == 0) {
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorDown, handler));
		    		 }
		    		 else {// if the enemy count of the room is not equal to 0, add a closed door
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorClosed, handler));
		    		 }
		    	 }
		    	 if(CurrentData.currentFloor[i][j] == 4 ) { // if the value of the element is a 4 and the enemy count of the room is 0, add a left door at the specified location
		    		 if(CurrentData.enemyCount == 0) {
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorLeft, handler));
		    		 }
		    		 else {// if the enemy count of the room is not equal to 0, add a closed door
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorClosed, handler));
		    		 }
		    	 }
		    	 if(CurrentData.currentFloor[i][j] == 5 ) { // if the value of the element is a 5 and the enemy count of the room is 0, add a right door at the specified location
		    		 if(CurrentData.enemyCount == 0) {
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorRight, handler));
		    		 }
		    		 else {// if the enemy count of the room is not equal to 0, add a closed door
		    			 handler.addObject(new Door(j*32 -4, i*32 - 14, ID.DoorClosed, handler));
		    		 }
		    	 }
		     }
		} // this meets objective 10
		if(CurrentData.currentFloor == floors.middle) { // if the current room is the middle room run the indented code
			for(int i = 0; i < CurrentData.enemyCount; i++) { // loops from 0 to the enemy count of the room
				//this meets objective 12
				health = 600 + (10 * (CurrentData.Level - 1)); // set the health to 600 + 10 * the current floor number, so the boss has more health the higher the floor number
				damage = 40 + (10 * (CurrentData.Level - 1)); // set the damage to 40 + 10 * the current floor number, so the boss does more damage the higher the floor number
				handler.addObject(new Enemy(368, 298, ID.BossEnemy, handler, health, floors, "down", damage, false)); // add a boss enemy at the location with the correct damage and health
			}
		}
		else { // if the current floor is not equal to the middle room, run the indented code
			for(int i = 0; i < CurrentData.enemyCount; i++) { // loop from 0 to the current rooms enemy count
				xPosition = rand.nextInt(25); // set the x position to a random number between 0 and 25
				yPosition = rand.nextInt(20); // set the y position to a random number between 0 and 20
				while(CurrentData.currentFloor[yPosition][xPosition] != 1) { // while the position given by the y position and x  position variables on the 2D array is not a one, keep assigning random values to the x and y position variables
					xPosition = rand.nextInt(25);
					yPosition = rand.nextInt(20);
				}
				if(CurrentData.currentFloor[yPosition][xPosition] == 1) { // if the current position given by the y position and x position variables is equal to 1
					//this meets objective 12
					health = 100 + (10 * (CurrentData.Level - 1)); // set the health of an enemy equal to 100 + 10 * the current floor number so the enemies get harder to kill the higher the floor number
					damage = 20 + (10 * (CurrentData.Level - 1)); // set the damage of an enemy equal to 20 + 10 * the current floor number so the enemies deal more damage the higher the floor number
					handler.addObject(new Enemy((xPosition * 32) - 4, (yPosition * 32) - 14, ID.Enemy, handler, health, floors, "down", damage, false)); // add an enemy at the specified location with the correct health and damage
				}
			}
		}
	}
	// this method is used to get an instance of the texture method, so it can be used to load the images for the player and enemies.
	public static Texture getInstance() {
		return tex;
	}
}