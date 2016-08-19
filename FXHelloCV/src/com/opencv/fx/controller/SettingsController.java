package com.opencv.fx.controller;

import java.util.ResourceBundle;

import com.opencv.fx.settings.Settings;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

/**
 * The controller of the settings window. Add another controller for each new window
 * 
 * @author Maximilian Zuleger -<max-z.de>
 * @version 1.0 (2016-8-18)
 * @since 1.0 (2016-8-18)
 * 
 */
public class SettingsController implements Initializable {
	Settings settings;
	
	public SettingsController() {
		settings = Settings.getInstance();
	}
		
    @Override
    public void initialize(java.net.URL location, ResourceBundle resources)
    {
        loadSettings();
    }
    
	public void loadSettings(){
		//Init load Settings from file
		optionGray.setSelected(stringtobool(settings.getProp().getProperty("gray")));
	}
	
	private boolean stringtobool(String s){
		if (s.equals("1")) return true;
		return false;
	}

	@FXML
	private CheckBox optionGray;
	@FXML
	private void optionGraySet(Event event) {
		if (optionGray.isSelected()){
			settings.getProp().setProperty("gray", "1");
		} else {
			settings.getProp().setProperty("gray", "0");
		}
	}	
	
	
	
//	@FXML
//	private Button button;
//	
//	@FXML
//	private void OpenSettingsWindow(Event event) {
//		
//	}	

}
