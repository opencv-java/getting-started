package it.polito.elite.teaching.cv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * The controller for our application, where the application logic is
 * implemented. It handles the button for starting/stopping the camera and the
 * acquired video stream.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author Maximilian Zuleger - some important fixes <max-z.de>
 * @version 1.6 (2016-8-16)
 * @since 1.0 (2013-10-20)
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

	public void initController(int vd) {
		videodevice = vd;
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
									//Grab Frame
									Mat matToShow = grabFrame();
									//Process Frame
									matToShow = processMat(matToShow);
									// convert the Mat object (OpenCV) to Image (JavaFX)
									Image imageToShow = mat2Image(matToShow);
									//Update ImageView
									setFrametoImageView(imageToShow);
									//Update the UI
									updateUIObjects();

								} catch (Exception e1) {
									System.out.println("Error on Update " + e1);
								}
								// Sleep for lower FPS/updateRate
								// try {
								// Thread.sleep(10);
								// } catch (InterruptedException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// }

							}
							System.out.println("Thread processFrame closed");
							try {
								capture.release();
								updateUIObjects();
								setFrametoImageView(null);
							} catch (Exception e) {
							}

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
			fps.setText(""+capture.get(5));
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
				if (!frame.empty()) {

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
		Imgproc.cvtColor(matToShow, matToShow, Imgproc.COLOR_BGR2GRAY);
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

	public void setClosed() {
		//Close thread on window close
		cameraActive = false;
	}

}
