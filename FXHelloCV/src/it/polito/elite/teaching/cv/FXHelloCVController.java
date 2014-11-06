package it.polito.elite.teaching.cv;

import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

/**
 * The controller for our application, where the application logic is
 * implemented. It handles the button for starting/stopping the camera and the
 * acquired video stream.
 * 
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @since 2013-10-20
 * 
 */
public class FXHelloCVController
{
	// the FXML button
	@FXML
	private Button button;
	
	// the main app
	private FXHelloCV mainApp;
	// a timer for acquiring the video stream
	private Timer timer;
	// the OpenCV object that realizes the video capture
	private VideoCapture capture = new VideoCapture();
	// a flag to change the button behavior
	private boolean cameraActive = false;
	
	/**
	 * The action triggered by pushing the button on the GUI
	 * 
	 * @param event
	 *            the push button event
	 */
	@FXML
	protected void startCamera(ActionEvent event)
	{
		// check: the main class is accessible?
		if (this.mainApp != null)
		{
			// get the ImageView object for showing the video stream
			final ImageView frameView = (ImageView) mainApp.getRootElement().lookup("#currentFrame");
			// bind an image property with the container for frames
			final ObjectProperty<Image> imageProp = new SimpleObjectProperty<>();
			frameView.imageProperty().bind(imageProp);
			
			if (!this.cameraActive)
			{
				// start the video capture
				this.capture.open(0);
				
				// is the video stream available?
				if (this.capture.isOpened())
				{
					this.cameraActive = true;
					
					// grab a frame every 33 ms (30 frames/sec)
					TimerTask frameGrabber = new TimerTask() {
						@Override
						public void run()
						{
							// update the image property => update the frame
							// shown in the UI
							imageProp.set(grabFrame());
						}
					};
					this.timer = new Timer();
					this.timer.schedule(frameGrabber, 0, 33);
					
					// update the button content
					this.button.setText("Stop Camera");
				}
				else
				{
					// log the error
					System.err.println("Impossible to open the camera connection...");
				}
			}
			else
			{
				// the camera is not active at this point
				this.cameraActive = false;
				// update again the button content
				this.button.setText("Start Camera");
				// stop the timer
				if (this.timer != null)
				{
					this.timer.cancel();
					this.timer = null;
				}
				// release the camera
				this.capture.release();
			}
		}
	}
	
	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private Image grabFrame()
	{
		// init everything
		Image imageToShow = null;
		Mat frame = new Mat();
		
		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);
				
				// if the frame is not empty, process it
				if (!frame.empty())
				{
					// convert the image to gray scale
					Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
					// convert the Mat object (OpenCV) to Image (JavaFX)
					imageToShow = mat2Image(frame);
				}
				
			}
			catch (Exception e)
			{
				// log the error
				System.err.println("ERROR: " + e.getMessage());
			}
		}
		
		return imageToShow;
	}
	
	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 * 
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	private Image mat2Image(Mat frame)
	{
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer
		Highgui.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	/**
	 * Set the reference to the main class of the application
	 * 
	 * @param mainApp
	 *            the {@FXHelloCV} object to set
	 */
	public void setMainApp(FXHelloCV mainApp)
	{
		this.mainApp = mainApp;
	}
	
}
