package sign.recording;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.Control;
import javax.media.DataSink;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoProcessorException;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;

/**
 * A singleton class to hold the datasource and processor for saving the captured video to a file.
 * 
 * @author Laurel Williams
 * @version $Id: RecordingDataSource.java 119 2008-01-29 21:31:50Z laurel $
 */
public class RecordingDataSource
{
	private DataSource	recordingDataSource;
	private Processor	recordingProcessor;
	private DataSink	recordingDataSink;

	public RecordingDataSource(RecordingWindow parent)
	{
		CloneableCombinedVideoAudioDataSource cloneableDataSource = CloneableCombinedVideoAudioDataSource.getInstance(parent);
		this.recordingDataSource = cloneableDataSource.cloneDataSource();
		parent.setRecordDataSource(this);
	}

	public void startProcessing(File file) throws IOException, NoProcessorException, NoDataSinkException, NotRealizedError
	{

		this.recordingProcessor = Manager.createProcessor(this.recordingDataSource);

		DeviceStateHelper playhelper = new DeviceStateHelper(this.recordingProcessor);
		if (!playhelper.configure(10000))
			throw new NoProcessorException("cannot configure processor");

		(this.recordingProcessor.getTrackControls())[0].setFormat(CaptureDeviceDialog.getInstance().getVideoFormat());
		(this.recordingProcessor.getTrackControls())[0].setEnabled(true);
		//TODO will this work for a codec? requires a plugin??
		//		(this.recordingProcessor.getTrackControls())[0].setCodecChain(codecs);
		this.recordingProcessor.setContentDescriptor(new FileTypeDescriptor(FileTypeDescriptor.QUICKTIME));
		Control control = this.recordingProcessor.getControl("javax.media.control.FrameRateControl");
		if (control != null && control instanceof javax.media.control.FrameRateControl)
			((javax.media.control.FrameRateControl) control).setFrameRate(15.0f);

		if (!playhelper.realize(10000))
			throw new NoProcessorException("cannot realize processor");

		MediaLocator destinationMediaLocator = createFileMediaLocator(file);

		if (this.recordingProcessor.getDataOutput() == null)
			throw new IOException("No data output");
		this.recordingDataSink = Manager.createDataSink(this.recordingProcessor.getDataOutput(), destinationMediaLocator);
		this.recordingProcessor.start();
		this.recordingDataSink.open();
		this.recordingDataSink.start();
	}

	public void stopProcessing() throws IOException
	{
		// note that the order of closing in this method is important.
		// processor must be closed before the data sink, or file will not be saved.
		if (this.recordingProcessor != null)
		{
			this.recordingProcessor.close();
		}
		if (this.recordingDataSink != null)
		{
			this.recordingDataSink.close();
			this.recordingDataSink.stop();
		}
	}

	private MediaLocator createFileMediaLocator(File file) throws MalformedURLException
	{
		URL movieUrl = null;
		MediaLocator destinationMediaLocator = null;
		movieUrl = new URL("file://" + file.getAbsolutePath());
		destinationMediaLocator = new MediaLocator(movieUrl);
		return destinationMediaLocator;
	}

	public long getTime()
	{
		return this.recordingProcessor.getMediaNanoseconds();
	}

}
