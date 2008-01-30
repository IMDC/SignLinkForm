package sign.recording;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.media.NoDataSinkException;
import javax.media.NoProcessorException;
import javax.media.NotRealizedError;
import javax.swing.JOptionPane;

import sign.CreateWindow;

/**
 * A listener class for the recording actions
 * 
 * @author Martin Gerdzhev
 * @version $Id: RecordButtonsListener.java 47 2007-10-30 19:33:51Z laurel $
 */
class RecordingListener implements ActionListener, WindowListener
{
	private static RecordingListener	instance;
	private static RecordingDataSource	recordingDataSource;
	private long						time	= 0;
	private RecordingWindow				recordingWindow;
	private String						min		= "00";
	private String						sec		= "00";
	private String						mil		= "000";

	public synchronized static RecordingListener getInstance()
	{
		if (instance == null)
		{
			instance = new RecordingListener();
		}
		return instance;
	}

	/**
	 * performs the necessary actions
	 */
	public void actionPerformed(ActionEvent actionEvent)
	{
		String actionCommand = actionEvent.getActionCommand();
		recordingWindow = RecordingWindow.getInstance();
		RecordButtonsPanel recordButtonsPanel = RecordButtonsPanel.getInstance(recordingWindow);
		if (actionEvent.getSource() == recordButtonsPanel.getSlideTimer())
		{
			time = recordingWindow.getRecordDataSource().getTime() / 1000000;
			recordButtonsPanel.getVSlide().setValue((int)time);
			min = "" + (time / 1000) / 60;
			if (min.length() < 2)
				min = "0" + min;
			sec = "" + ((time / 1000) % 60);
			if (sec.length() < 2)
				sec = "0" + sec;
			mil = "" + time % 1000;
			if (mil.length() < 2)
				mil = "00" + mil;
			else if (mil.length() < 3)
				mil = "0" + mil;
			recordButtonsPanel.getTimerText().setText(min + ":" + sec + ":" + mil);
			if (time >= RecordButtonsPanel.TIMERMAX) // if time reaches 5 minutes stop
			{
				JOptionPane.showMessageDialog(null, "Maximum time reached", "Recording Stopped", JOptionPane.INFORMATION_MESSAGE);
				stopAction(recordButtonsPanel);
			}
		}
		else if (actionCommand.equals(RecordButtonsPanel.RECORD_ACTION))
		{
			recordAction(recordButtonsPanel);
		}
		else if (actionCommand.equals(RecordButtonsPanel.STOP_ACTION))
		{
			stopAction(recordButtonsPanel);
		}
		else if (actionCommand.equals(RecordingWindow.NEXT_ACTION))
		{

		}
		else if (actionCommand.equals(RecordingWindow.CANCEL_ACTION))
		{
			this.windowClosing(null);
		}
	}

	/**
	 * @param recordButtonsPanel
	 */
	private void stopAction(RecordButtonsPanel recordButtonsPanel)
	{
		recordButtonsPanel.getStopButton().setEnabled(false);
		this.stopRecording();
		recordButtonsPanel.getSlideTimer().stop();
		recordButtonsPanel.getRecordButton().setEnabled(true);
		recordingWindow.setPreviewEnabled(true);
	}

	/**
	 * @param recordButtonsPanel
	 */
	private void recordAction(RecordButtonsPanel recordButtonsPanel)
	{
		recordButtonsPanel.getRecordButton().setEnabled(false);
		recordingWindow.setPreviewEnabled(false);
		File file = recordingWindow.getRecordingFile();
		if (file.exists())
			file.delete();
		this.recordToFile(file);
		recordButtonsPanel.getSlideTimer().start();
		recordButtonsPanel.getStopButton().setEnabled(true);
	}

// @Override
	public void windowActivated(WindowEvent arg0)
	{
	}

// @Override
	public void windowClosed(WindowEvent arg0)
	{
	}

// @Override
	public void windowClosing(WindowEvent windowEvent)
	{
		recordingWindow.setVisible(false);
		recordingWindow.dispose();
		this.stopRecording();
		CreateWindow.getInstance().setVisible(true);
	}

// @Override
	public void windowDeactivated(WindowEvent arg0)
	{
	}

// @Override
	public void windowDeiconified(WindowEvent arg0)
	{
	}

// @Override
	public void windowIconified(WindowEvent arg0)
	{
	}

// @Override
	public void windowOpened(WindowEvent arg0)
	{
	}

	private void recordToFile(File file)
	{
		try
		{
			// note that RecordingDataSource must be recreated new each time.
			recordingDataSource = new RecordingDataSource(recordingWindow);
			recordingDataSource.startProcessing(file);
		}
		catch (NoProcessorException npe)
		{
			RecordingException.showMessageAndThrowRecordingException(recordingWindow, npe, "Exception creating video processor: ");
		}
		catch (IOException ioe)
		{
			RecordingException.showMessageAndThrowRecordingException(recordingWindow, ioe, "IO Exception: ");
		}
		catch (NoDataSinkException ndse)
		{
			ndse.printStackTrace();
			RecordingException.showMessageAndThrowRecordingException(recordingWindow, ndse, "No DataSink ");
		}
		catch (NotRealizedError nre)
		{
			nre.printStackTrace();
			RecordingException.showMessageAndThrowRecordingException(recordingWindow, nre, "DataSink not realized ");
		}

	}

	private void stopRecording()
	{
		try
		{
			if (recordingDataSource != null)
				recordingDataSource.stopProcessing();
		}
		catch (IOException ioe)
		{
			RecordingException.showMessageAndThrowRecordingException(recordingWindow, ioe, "IO Exception: ");
		}
	}

}