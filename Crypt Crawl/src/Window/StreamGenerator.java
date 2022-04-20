package Window;

import java.io.InputStream;
	
public class StreamGenerator {
	
	//this method takes a file location and returns an input stream which is used to read data from a source, this method will mainly be used to read in data from sound files.
	public InputStream newStream(String loc) {
		InputStream stream; // creates an input stream variable
		stream = getClass().getResourceAsStream(loc); // returns an input stream from the specified rescource
		return stream; // returns the stream
	}
}
