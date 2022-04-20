package Window;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {

	private BufferedImage image; // creates a private buffered image variable
	
	// this method is used to load and return an image from the file path that is parsed // this is an example of loosely coupled modules
	public BufferedImage loadImage(String path) {
		
		// a try catch blocked is used to catch any errors that may occur from loading the image
		try {
			image = ImageIO.read(getClass().getResource(path)); // reads the image at the path provided and stores it in a buffered image variable
		//catches an input/output error that might occur when the image is being inputed into the system
		} catch (IOException e) { // example of good exception handling	
			e.printStackTrace();
		}
		return image; // returns the image
	}
	
}