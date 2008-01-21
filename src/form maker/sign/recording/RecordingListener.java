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
import javax.swing.JFileChooser;
import javax.swing.Timer;

import sign.CreateWindow;

/**
 * A listener class for the recording actions
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: RecordButtonsListener.java 47 2007-10-30 19:33:51Z laurel $
 */
class RecordingListener implements ActionListener, WindowListener {
	private static RecordingListener instance;
	private static RecordingDataSource recordingDataSource;

	File file;
	Timer recordTime;
	int time = 0;

	public synchronized static RecordingListener getInstance() {
		if (instance == null) {
			instance = new RecordingListener();
		}
		return instance;
	}

	/**
	 * performs the necessary actions
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		RecordButtonsPanel recordButtonsPanel = RecordButtonsPanel.getInstance(RecordingWindow.getInstance());
		if (actionCommand.equals(RecordButtonsPanel.RECORD_ACTION)) {
			recordButtonsPanel.getRecordButton().setEnabled(false);
			recordButtonsPanel.getPlayButton().setEnabled(false);
			file = new File("temp.mov");
			// recordTime.start();
			this.recordToFile(file);
			recordButtonsPanel.getStopButton().setEnabled(true);
		} else if (actionCommand.equals(RecordButtonsPanel.PLAY_ACTION)) {
			recordButtonsPanel.getPlayButton().setEnabled(false);
			recordButtonsPanel.getRecordButton().setEnabled(false);
			System.out.println("HERE");
			
//			final JFileChooser fileChooser = new JFileChooser();
//			fileChooser.setFileFilter(new MOVFilter());
//			int returnVal = fileChooser.showDialog(RecordingWindow.getInstance(), "choose");
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//				File file = fileChooser.getSelectedFile();
//				fileChooser.setSelectedFile(null);
//			}
			//open file and play video
			//recordTime??
		} else if (actionCommand.equals(RecordButtonsPanel.STOP_ACTION)) {
			recordButtonsPanel.getStopButton().setEnabled(false);
			// recordTime.stop();
			this.stopRecording();
			recordButtonsPanel.getRecordButton().setEnabled(true);
			recordButtonsPanel.getPlayButton().setEnabled(true);
		} else if (actionCommand.equals(RecordButtonsPanel.NEXT_ACTION)) {

		} else if (actionCommand.equals(RecordButtonsPanel.CANCEL_ACTION)) {
			this.windowClosing(null);
		}
	}

//	@Override
	public void windowActivated(WindowEvent arg0) {
	}

//	@Override
	public void windowClosed(WindowEvent arg0) {
	}

//	@Override
	public void windowClosing(WindowEvent windowEvent) {
		RecordingWindow recordingWindow = RecordingWindow.getInstance();
		recordingWindow.setVisible(false);
		recordingWindow.dispose();
		this.stopRecording();
		CreateWindow.getInstance().setVisible(true);
	}

//	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

//	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

//	@Override
	public void windowIconified(WindowEvent arg0) {
	}

//	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	private void recordToFile(File file) {
		RecordingWindow recordingWindow = RecordingWindow.getInstance();
		try {
			//note that RecordingDataSource must be recreated new each time.
			recordingDataSource = new RecordingDataSource(recordingWindow);
			recordingDataSource.startProcessing(file.getName());
		} catch (NoProcessorException npe) {
			RecordingException.showMessageAndThrowRecordingException(
					recordingWindow, npe,
					"Exception creating video processor: ");
		} catch (IOException ioe) {
			RecordingException.showMessageAndThrowRecordingException(
					recordingWindow, ioe, "IO Exception: ");
		} catch (NoDataSinkException ndse) {
			ndse.printStackTrace();
			RecordingException.showMessageAndThrowRecordingException(
					recordingWindow, ndse, "No DataSink ");
		} catch (NotRealizedError nre) {
			nre.printStackTrace();
			RecordingException.showMessageAndThrowRecordingException(
					recordingWindow, nre, "DataSink not realized ");
		}

	}

	private void stopRecording() {
		try {
			if (recordingDataSource != null) recordingDataSource.stopProcessing();
		} catch (IOException ioe) {
			RecordingException.showMessageAndThrowRecordingException(RecordingWindow.getInstance(), ioe,
					"IO Exception: ");
		}
	}

}