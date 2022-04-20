package Game;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	private BufferedImage image; // creates a buffered image variable that can only be used in this class
	
	// the constructor of this class takes in a buffered image and then stores that in the private buffered image variable so it can be used later
	public SpriteSheet(BufferedImage image) {
		this.image = image;
	}
	
	// the grab image method is used to get a specific part of an image and store it in a new buffered image variable, which is then returned
	// this will be used for drawing the different animations for the player and enemies. Rather than all the images being separate the images can be on one sprite sheet and different animations can be different sub images
	// the method takes in the column and row in the sprite sheet the desired image is and the width and height of the image. the Column and row are multiplied by 32 as the images are 32*32 pixels. The width and height are taken away so that it starts the sub image at the correct place
	public BufferedImage grabImage(int col, int row, int width, int height) {
		BufferedImage img =  image.getSubimage((col * 32) - width, (row * 32) - height, width, height);
		return img;
	}
}