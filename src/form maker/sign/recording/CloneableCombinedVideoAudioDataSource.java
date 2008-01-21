package sign.recording;

import java.awt.Component;
import java.io.IOException;

import javax.media.CaptureDeviceInfo;
import javax.media.IncompatibleSourceException;
import javax.media.Manager;
import javax.media.NoDataSourceException;
import javax.media.NoProcessorException;
import javax.media.NotConfiguredError;
import javax.media.Processor;
import javax.media.control.FormatControl;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.SourceCloneable;

/**
 * A singleton class to hold the video and audio datasource and processor, to
 * initialize and to start the processing.
 * 
 * @author Laurel Williams
 * 
 * @version $Id: CloneableCombinedVideoAudioDataSource.java 63 2007-11-19
 *          20:44:11Z laurel $
 * 
 */
public class CloneableCombinedVideoAudioDataSource {

	private DataSource dataSource;

	private Processor processor;

	private static CloneableCombinedVideoAudioDataSource instance;

	public synchronized static CloneableCombinedVideoAudioDataSource getInstance(
			Component parent) {
		if (instance == null) {
			instance = new CloneableCombinedVideoAudioDataSource(parent);
		}
		return instance;
	}

	private CloneableCombinedVideoAudioDataSource(Component parent) {
		try {
			dataSource = this.setCloneableDataSource();
			this.startProcessing();
		} catch (NoDataSourceException ndse) {
			RecordingException.showMessageAndThrowRecordingException(parent,
					ndse, "Exception locating video or audio data source: ");
		} catch (NoProcessorException npe) {
			RecordingException.showMessageAndThrowRecordingException(parent,
					npe, "Exception creating processor: ");
		} catch (IOException ioe) {
			RecordingException.showMessageAndThrowRecordingException(parent,
					ioe, "IO Exception: ");
		} catch (IncompatibleSourceException ise) {
			RecordingException.showMessageAndThrowRecordingException(parent,
					ise, "Incompatible Source Exception: ");
		}
	}

	/**
	 * A method to set the main source
	 * 
	 * @throws IncompatibleSourceException
	 */
	private DataSource setCloneableDataSource() throws NoDataSourceException,
			IOException, IncompatibleSourceException {
		DataSource[] dataSources = new DataSource[2];

		CaptureDeviceDialog captureDeviceDialog = CaptureDeviceDialog
				.getInstance();
		CaptureDeviceInfo videoCaptureDeviceInfo = captureDeviceDialog
				.getVideoDevice();
		dataSources[0] = Manager.createDataSource(videoCaptureDeviceInfo
				.getLocator());
		FormatControl[] videoFormatControls = ((CaptureDevice) dataSources[0])
				.getFormatControls();
		videoFormatControls[0].setFormat(captureDeviceDialog.getVideoFormat());

		if (CaptureDeviceDialog.AUDIO_ON) {
			CaptureDeviceInfo audioCaptureDeviceInfo = captureDeviceDialog
					.getAudioDevice();
			dataSources[1] = Manager.createDataSource(audioCaptureDeviceInfo
					.getLocator());
			FormatControl[] audioFormatControls = ((CaptureDevice) dataSources[1])
					.getFormatControls();
			audioFormatControls[0].setFormat(captureDeviceDialog
					.getAudioFormat());

			DataSource mergedDataSource = Manager
					.createMergingDataSource(dataSources);
			return Manager.createCloneableDataSource(mergedDataSource);
		}
		else {
			return Manager.createCloneableDataSource(dataSources[0]); //only the video data source is returned
		}

	}

	/**
	 * Clones the datasource.
	 * 
	 * @return a clone of the datasource
	 */
	protected DataSource cloneDataSource() {
		return ((SourceCloneable) this.dataSource).createClone();
	}

	/**
	 * A method to start showing an image from the camera and playing audio
	 * 
	 * @throws IOException
	 * @throws NoProcessorException
	 */
	private void startProcessing() throws IOException, NoProcessorException {
		this.processor = Manager.createProcessor(this.dataSource);
		configureAndRealizeProcessor(this.processor);
		this.processor.start();
	}

	/**
	 * A utility method for configuring and realizing a processor.
	 * 
	 * @param processor
	 * @throws NoProcessorException
	 * @throws NotConfiguredError
	 */

	public static void configureAndRealizeProcessor(Processor processor)
			throws NoProcessorException, NotConfiguredError {
		DeviceStateHelper playhelper = new DeviceStateHelper(processor);
		if (!playhelper.configure(10000))
			throw new NoProcessorException("cannot configure processor");

		processor.setContentDescriptor(null);
		if (!playhelper.realize(10000))
			throw new NoProcessorException("cannot realize processor");
	}
}
