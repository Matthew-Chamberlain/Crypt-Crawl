package Window;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Animation { // this class meets objective 11
	
	private int speed; //sets an integer for the speed the animation will play at
	private int frames; // sets an integer for the number of frames in the animation
	
	private int index = 0;
	private int count = 0; // count is used to hold the number of the frame that is currently being used

	private BufferedImage[] images; // sets a Buffered Image array to store all the images needed in an animation
	private BufferedImage currentImage; // sets a buffered image that will store the current image the animation is on
	
	// this constructor is used to assign information to the variables that will be needed to draw the animation
	// it is parsed the desired speed of the specific animation and an unknown amount of buffered images, that are the frames of the animation
	public Animation(int speed, BufferedImage... args) {
		this.speed = speed; // sets the speed variable equal to the speed parsed
		images = new BufferedImage[args.length]; // sets the length of the buffered image array equal to the number of buffered images parsed into the method
		for(int i = 0; i < args.length; i++) { // loops through the number of images parsed into the method
			images[i] = args[i]; // stores the images into the array of buffered images
		}
		frames = args.length; // sets the frames variable equal to the number of images parsed in.
	}
	
	// this method is used to start the animation. The lower the speed variable the less time it will take for the index to be greater than it, so the animation will play faster
	public void runAnimation() {
		index++; // increases the index variable
		if(index > speed) { // runs the indented code if the index is greater than the speed
			index = 0; // sets the index to 0
			nextFrame(); // calls the next frame method
		}
		
	}
	// this method is responsible for loading the next frame of the animation
	private void nextFrame() {
		if(count < frames) { //this if function is used to check whether the animation has finished playing or not
			currentImage = images[count]; // sets the current image equal to the current frame of the animation
			count++; // increases the count variable by one
		}
		else { // if the animation has finished, set the count variable back to 0
			count = 0;
		}
	}
	
	//this method is used to draw the current image of the animation at the corect position on the screen // this method meets objective 11
	public void drawAnimation(Graphics g, int x, int y) {
		g.drawImage(currentImage, x, y, null); // draws the image specified at the coordinates given
	}
	
	// this method is used to draw an image at the given coordinates at a specific size, it can be used to draw an image bigger or smalelr than it normaly would be. // this method meets objective 11
	public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY) {
		g.drawImage(currentImage, x, y, scaleX, scaleY, null); // draws the image specified at the coordinates and size given
	}
}