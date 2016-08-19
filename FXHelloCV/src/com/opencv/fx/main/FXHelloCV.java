package com.opencv.fx.main;


import org.opencv.core.Core;

import com.opencv.fx.controller.FXHelloCVController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * The main class for a JavaFX application.
 * 
 * @author1 Maximilian Zuleger - <max-z.de>
 * @version 1.0 (2016-8-18)
 * @since 1.0 (2016-8-18)
 * 		
 */
public class FXHelloCV extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../FX/FXHelloCV.fxml"));
			// store the root element so that the controllers can use it
			BorderPane rootElement = (BorderPane) loader.load();
			// create and style a scene
			Scene scene = new Scene(rootElement, 800, 600);
			scene.getStylesheets().add(getClass().getResource("../FX/application.css").toExternalForm());
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("Capture Webcam with OpenCV and FX");
			primaryStage.setScene(scene);
			// show the GUI
			primaryStage.show();
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FX/SettingsWindow.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Settings");
			stage.setScene(new Scene(root1));
			
			// init the controller
			final FXHelloCVController controller = loader.getController();
			
			// Set video device
			int videodevice = 1;
			controller.initController(videodevice, stage);
			
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	  controller.setClosed();
		        	  System.out.println("Closed");
		          }
		      })); 
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * For launching the application...
	 * 
	 * @param args
	 *            optional params
	 */
	public static void main(String[] args)
	{
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		launch(args);
	}
}
