package it.polito.elite.teaching.cv;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * The main class for a JavaFX application. It creates and handle the main
 * window with its resources (style, graphics, etc.).
 * 
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @since 2013-10-20
 * 
 */
public class FXHelloCV extends Application
{
	// the root element in the FXML window
	private BorderPane rootElement;
	
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FXHelloCV.fxml"));
			// store the root element so that the controllers can use it
			this.rootElement = (BorderPane) loader.load();
			// create and style a scene
			Scene scene = new Scene(this.rootElement, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("JavaFX meets OpenCV");
			primaryStage.setScene(scene);
			// show the GUI
			primaryStage.show();
			
			// set a reference of this class for its controller
			FXHelloCVController controller = loader.getController();
			controller.setMainApp(this);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter for the rootElement
	 * 
	 * @return the {@link BorderPane} that represents the root element
	 */
	public BorderPane getRootElement()
	{
		return this.rootElement;
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
