package Game;

import java.awt.image.BufferedImage;
import Window.BufferedImageLoader;

public class Texture {
	
	 SpriteSheet ps, mb, bs, es; // creates a few variables of the custom data type sprite sheet, ps is for the player sprite sheet, mb is for the magic bolt sprite sheet, bs is for the boss sprite sheet and es is for the enemy sprite sheet
	 private BufferedImage player_sheet, magicBolt_sheet, boss_sheet, enemy_sheet; // creates 4 buffered image variables 
	 
	 //examples of single dimensional array
	 public BufferedImage[] player = new BufferedImage[20]; // creates a buffered image array called player that can hold 20 elements. This is because there are 20 different images in the sprite sheet that will make up all of the players animations
	 public BufferedImage[] magicBolt = new BufferedImage[4]; // creates a buffered image array for the magic bolt that can hold 4 elements. This is because there are 4 images that make up the sprite sheet for the magic attack
	 public BufferedImage[] enemy = new BufferedImage[8]; // creates a buffered image array for the enemy that can hold 8 elements. This is because there are 8 images in the sprite sheet that will make up the animations for the enemies
	 public BufferedImage[] boss = new BufferedImage[8]; // creates a buffered image array for the boss enemy that can hold 8 elements. This is because there are 8 images in the sprite sheet that will make up the animations for the boss enemy
	 
	 // the constructor for this class is responsible for loading in all the images of the sprite sheets and assigning them to each of the sprite sheet variables. The constructor also calls the get textures method which is used for splitting the sprite sheets into individual images 
	 public Texture() {
		 
		 BufferedImageLoader loader = new BufferedImageLoader(); // creates an instance of the buffered image loader class
		 //these are examples of paremetised file paths
		 player_sheet = loader.loadImage("/" + CurrentData.selectedClass + " Spritesheet.gif"); // loads the sprite sheet for the class the user is playing as by calling the load image method and parsing in a file path, the file path changes depending on the class the user has selected // this line meets objective 5
		 magicBolt_sheet = loader.loadImage("/Magicbolt.gif"); // loads the sprite sheet image for the magic bolt by calling the load image method and parsing a file path
		 boss_sheet = loader.loadImage("/Boss Sprite Sheet.gif"); // loads the sprite sheet image for the boss enemy by calling the load image method and parsing a file path
		 enemy_sheet = loader.loadImage("/Enemy Sprite Sheet.gif"); // loads the sprite sheet image for the basic enemies by calling the load image method and parsing a file path
		 
		 ps = new SpriteSheet(player_sheet); // stores the sprite sheet for the player in the player sheet object
		 mb = new SpriteSheet(magicBolt_sheet); // stores the sprite sheet for the magic bolt in the magic bolt sheet object
		 bs = new SpriteSheet(boss_sheet); // stores the sprite sheet for the boss enemy in the boss sheet object
		 es = new SpriteSheet(enemy_sheet); // stores the sprite sheet for the basic enemy in the enemy sheet object
		 
		 getTextures(); // calls the get textures method
	 }
	 
	 // this method is responsible for getting all of the individual images from each of the sprite sheets and then storing them as elements in their respective buffered image arrays
	 // when the grab image method in the sprite sheet class is called it is parsed the column and row that the desired image is on and the width and height of the image
	 private void getTextures() {
		 //Walking down
		 player[0] = ps.grabImage(1, 1, 32, 32);
		 player[1] = ps.grabImage(2, 1, 32, 32);
		 
		 //Walking left
		 player[2] = ps.grabImage(1, 2, 32, 32);
		 player[3] = ps.grabImage(2, 2, 32, 32);
		 
		 //Walking right
		 player[4] = ps.grabImage(3, 2, 32, 32);
		 player[5] = ps.grabImage(4, 2, 32, 32);
		 
		 //Walking Up
		 player[6] = ps.grabImage(1, 3, 32, 32);
		 player[7] = ps.grabImage(2, 3, 32, 32);
		 
		 //Down attack
		 player[8] = ps.grabImage(1, 5, 32, 64);
		 player[9] = ps.grabImage(2, 5, 32, 64);
		 player[10] = ps.grabImage(3, 5, 32, 64);
		 
		 //Up attack
		 player[11] = ps.grabImage(1, 8, 32, 64);
		 player[12] = ps.grabImage(2, 8, 32, 64);
		 player[13] = ps.grabImage(3, 8, 32, 64);
		 
		 //Left attack
		 player[14] = ps.grabImage(2, 6, 64, 32);
		 player[15] = ps.grabImage(4, 6, 64, 32);
		 player[16] = ps.grabImage(6, 6, 64, 32);	
		 
		 //Right attack
		 player[17] = ps.grabImage(2, 9, 64, 32);
		 player[18] = ps.grabImage(4, 9, 64, 32);
		 player[19] = ps.grabImage(6, 9, 64, 32);
		 
		 magicBolt[0] = mb.grabImage(1, 1, 32, 32); // magic bolt up
		 magicBolt[1] = mb.grabImage(2, 1, 32, 32); // magic bolt down
		 magicBolt[2] = mb.grabImage(1, 2, 32, 32); // magic bolt left
		 magicBolt[3] = mb.grabImage(2, 2, 32, 32); // magic bolt right
		 
		 //Walking Up
		 boss[0] = bs.grabImage(1, 1, 32, 32);
		 boss[1] = bs.grabImage(2, 1, 32, 32);
		 
		 //Walking Right
		 boss[2] = bs.grabImage(1, 2, 32, 32);
		 boss[3] = bs.grabImage(2, 2, 32, 32);
		
		 //Walking Left
		 boss[4] = bs.grabImage(1, 3, 32, 32);
		 boss[5] = bs.grabImage(2, 3, 32, 32);
		 
		 //Walking Down
		 boss[6] = bs.grabImage(1, 4, 32, 32);
		 boss[7] = bs.grabImage(2, 4, 32, 32);
		 
		 //Walking Up
		 enemy[0] = es.grabImage(1, 1, 32, 32);
		 enemy[1] = es.grabImage(2, 1, 32, 32);
		 
		 //Walking Right
		 enemy[2] = es.grabImage(1, 2, 32, 32);
		 enemy[3] = es.grabImage(2, 2, 32, 32);
		 
		 //Walking Left
		 enemy[4] = es.grabImage(1, 3, 32, 32);
		 enemy[5] = es.grabImage(2, 3, 32, 32);
		 
		 //Walking Down
		 enemy[6] = es.grabImage(1, 4, 32, 32);
		 enemy[7] = es.grabImage(2, 4, 32, 32);
	 }
}