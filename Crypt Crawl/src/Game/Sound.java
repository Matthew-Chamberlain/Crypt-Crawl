package Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Window.StreamGenerator;

public class Sound {

	static StreamGenerator gen = new StreamGenerator(); // creates an instance of the stream generator class

	static Clip clip; //creates a clip which will store a sound resource
	
	// this method is used to play a sound file at specified file path that is parsed into the method.
	public static void play(String loc) {
	    try {
	    	AudioInputStream stream = AudioSystem.getAudioInputStream(gen.newStream(loc)); // generates an input stream between the program and the sound file at the given location
	        clip = AudioSystem.getClip(); // gets the clip from the stream and stores it in the clip variable
	        clip.open(stream); // opens the stream
	        clip.loop(Clip.LOOP_CONTINUOUSLY); // sets the clip to loop continuously
	        clip.start(); // starts the clip
	    // catches an exception from getting the stream    
	    } catch (Exception e1) {
	    	System.out.println(e1);
	    }
	}
	
	// this method is used to stop a sound file from playing. This will be used to stop the music once the game is over and the player has died
	public static void stop() {
		try {
			clip.stop(); // stops the clip
		
		// catches the a null pointer exception
		} catch(NullPointerException e) {
			
		}
		
	}
}
