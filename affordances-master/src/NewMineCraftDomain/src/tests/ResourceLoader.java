package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ResourceLoader {

	public BufferedReader getBufferedReader(String filename) {
		ClassLoader CLDR = this.getClass().getClassLoader();
		
		URL resourceURL = CLDR.getResource(filename);
		if (resourceURL == null) {
			throw new RuntimeException("File " + filename + " does not exist in directory " + CLDR.getResource(".").getFile());
		}
		
		InputStream in;
		try {
			in = resourceURL.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
			
		}
		if (in == null) {
			throw new RuntimeException("File " + filename + " does not exist in directory " + CLDR.getResource(".").getFile());
		}

		return new BufferedReader(new InputStreamReader(in));
	}
}
