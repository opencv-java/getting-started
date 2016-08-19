package com.opencv.fx.controller;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import com.opencv.fx.settings.Settings;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The controller of the main window. Add another controller for each new window
 * 
 * @author Maximilian Zuleger -<max-z.de>
 * @version 1.0 (2016-8-18)
 * @since 1.0 (2016-8-18)
 * 
 */
public class FXHelloCVController {
	// the FXML button
	@FXML
	private Button button;
	// the FXML image view
	@FXML
	private ImageView currentFrame;
	
	@FXML
	private Text fps;
	
	@FXML
	private Pane ImageViewPane;

	// the OpenCV object that realizes the video capture
	private VideoCapture capture = new VideoCapture();
	// a flag to change the button behavior
	private static boolean cameraActive = false;

	// Settings

	// Set video device
	private static int videodevice = 0;
	
	//static onUpdate Vars
	private static long fps_var;
	
	private LinkedList<Long> fps_avg = new LinkedList<Long>();
	private int fps_avgof = 10;
	
	private static long startTime;
	private static long endTime;
	private static long timenow;
	
	private Settings settings;
	private Stage settings_stage;
	
	public void initController(int video_device, Stage settings_stage) {
		videodevice = video_device;
		this.settings_stage = settings_stage;
		settings = Settings.getInstance();
	}

	/**
	 * The action triggered by pushing the button on the GUI
	 * 
	 * @param event
	 *            the push button event
	 */
	@FXML
	protected void startCamera(ActionEvent event) {
		try {
			if (!cameraActive) {
				// start the video capture
				this.capture.open(videodevice);

				// is the video stream available?
				if (this.capture.isOpened()) {
					cameraActive = true;
					Thread processFrame = new Thread(new Runnable() {

						@Override
						public void run() {
							while (cameraActive) {
								try {
									//Start Frame
									startFrame();
									//Grab Frame
									Mat matToShow = grabFrame();
									//Process Frame
									matToShow = processMat(matToShow);
									// convert the Mat object (OpenCV) to Image (JavaFX)
									Image imageToShow = mat2Image(matToShow);
									//Update ImageView
									setFrametoImageView(imageToShow);
									//EndFrame
									endFrame();
									//Update the UI
									updateUIObjects();
								} catch (Exception e1) {
									System.out.println("Error on Update Frame " + e1);
								}							

							}
							System.out.println("Thread processFrame closed");
							try {
								capture.release();
								updateUIObjects();
								setFrametoImageView(null);
							} catch (Exception e) {	
							}

						}
						private void startFrame() {
							startTime = System.nanoTime();	
						}
						private void endFrame() {
							endTime = System.nanoTime();
							timenow = (endTime - startTime) / 1000000;	
							fps_var = FPSCalculator(timenow);	
						}

					});
					processFrame.setDaemon(true);
					processFrame.setName("processFrame");
					processFrame.start();

					// update the button content
					this.button.setText("Stop Camera");
				} else {
					// log the error
					throw new Exception("Impossible to open the camera connection");
				}
			} else {
				// the camera is not active at this point
				cameraActive = false;
				// update again the button content
				this.button.setText("Start Camera");
			}
		} catch (Exception e) {
			e.printStackTrace();
			cameraActive = false;
			this.button.setText("Start Camera");
		}
	}
	
	private long FPSCalculator(long time){
		//FPS avg calculator 
		long avg = 0;
		fps_avg.addFirst(time);
		if (fps_avg.size() == fps_avgof+1) fps_avg.removeLast();
		if (fps_avg.size() == fps_avgof){
			for (long i : fps_avg){
				avg += i;
			}
			avg = avg/fps_avgof;
			return 1000/avg;
		}
		return 1000/time;
	}
	

	/**
	 * Always Update UI from main thread
	 */
	private void setFrametoImageView(Image frame) {
		Platform.runLater(() -> {
			currentFrame.setImage(frame);
			currentFrame.setFitWidth(ImageViewPane.getWidth());
			currentFrame.setFitHeight((ImageViewPane.getHeight()));
			// set Image height/width by window size
		});
		
	}

	/**
	 * Always Update UI from main thread
	 */
	private void updateUIObjects() {
		Platform.runLater(() -> {
			// Update UI Objects like: Textfield.setText() , Button.set..() ,
			// Window.Resize...()
			//Set FPS
			fps.setText(""+fps_var);
		});
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Mat} to show
	 */
	private Mat grabFrame() {
		// init everything
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (frame.empty()) {
					throw new Exception("Frame is empty");
				}

			} catch (Exception e) {
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

		return frame;
	}
	
	/**
	 * Process a Frame
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat processMat(Mat matToShow) {
		// convert the image to gray scale
		if (settings.getProp().getProperty("gray").equals("1")){ Imgproc.cvtColor(matToShow, matToShow, Imgproc.COLOR_BGR2GRAY); }
		
		return matToShow;
	}

	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 * 
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	private Image mat2Image(Mat frame) {
		try {
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		} catch (Exception e) {
			System.out.println("Cant convert mat" + e);
			return null;
		}
	}

	public BufferedImage matToBufferedImage(Mat matBGR) {
		int width = matBGR.width(), height = matBGR.height(), channels = matBGR.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		matBGR.get(0, 0, sourcePixels);
		BufferedImage image;
		if (matBGR.channels() > 1) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		} else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
		return image;
	}
	
	@FXML
	private void OpenSettingsWindow(Event event) {
		settings_stage.show();
	}
	
	@FXML
	private void CloseWindow(Event event) {
		setClosed();
		System.out.println("Closing Window");
		Platform.exit();
	}
	
	public void setClosed() {
		//Store Settings
		settings.store();
		//Close thread on window close
		cameraActive = false;
	}
	

}
