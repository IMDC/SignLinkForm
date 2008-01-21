package sign.recording;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;

import javax.media.Manager;
import javax.media.NoProcessorException;
import javax.media.Processor;
import javax.media.protocol.DataSource;

/**
 * A singleton class to hold the datasource and processor for displaying the
 * video in the software interface. 
 * 
 * @author Laurel Williams
 * 
 * @version $Id: DisplayDataSource.java 63 2007-11-19 20:44:11Z laurel $
 * 
 */
public class DisplayDataSource {

	private DataSource displayDataSource;

	private Processor displayProcessor;

	private static DisplayDataSource instance;

	public synchronized static DisplayDataSource getInstance(Component parent) {
		if (instance == null) {
			instance = new DisplayDataSource(parent);
		}
		return instance;
	}

	private DisplayDataSource(Component parent) {
		CloneableCombinedVideoAudioDataSource cloneableDataSource = CloneableCombinedVideoAudioDataSource
				.getInstance(parent);
		
		this.displayDataSource = cloneableDataSource.cloneDataSource();
		try {
			this.startProcessing();
		} catch (NoProcessorException npe) {
			RecordingException.showMessageAndThrowRecordingException(parent,
					npe, "Exception creating video processor: ");
		} catch (IOException ioe) {
			RecordingException.showMessageAndThrowRecordingException(parent,
					ioe, "IO Exception: ");
		}
	}

	protected void startProcessing() throws IOException, NoProcessorException {
		this.displayProcessor = Manager.createProcessor(this.displayDataSource);
		CloneableCombinedVideoAudioDataSource
				.configureAndRealizeProcessor(this.displayProcessor);
		this.displayProcessor.start();
		this.displayProcessor.getVisualComponent().setBackground(Color.gray);
	}

	public Processor getDisplayProcessor() {
		return displayProcessor;
	}
	
}
