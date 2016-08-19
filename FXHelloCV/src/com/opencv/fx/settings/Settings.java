package com.opencv.fx.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
	private static Settings instance = null;
	private Properties prop = new Properties();
	private InputStream input = null;
	protected Settings() {
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Properties getProp() {
		return prop;
	}
	
	public void store(){
		try {
			prop.store(new FileOutputStream("config.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Settings getInstance(){
		if (instance == null) instance = new Settings();
		return instance;
	}
}
