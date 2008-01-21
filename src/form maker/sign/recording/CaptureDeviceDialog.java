package sign.recording;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sign.CreateWindow;

/**
 * 
 * @author Budi Kurniawan
 * @author Laurel Williams
 * 
 * Code adapted from:
 * http://www.javaworld.com/javaworld/jw-05-2001/jw-0504-jmf2.html?page=1
 * 
 * @version $Id: $
 */
public class CaptureDeviceDialog extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;
	
	private static final String			OK_ACTION			= "OK";
	private static final String			CANCEL_ACTION			= "Cancel";
	
	private static final int FORMAT_STRING_MAX_LENGTH = 100;

	boolean configurationChanged = false;
	private Vector<CaptureDeviceInfo> audioDevices;
	private Vector<CaptureDeviceInfo> videoDevices;
	private Vector<Format> audioFormats = new Vector<Format>();
	private Vector<Format> videoFormats = new Vector<Format>();
	private JComboBox audioDeviceCombo;
	private JComboBox videoDeviceCombo;
	private JComboBox audioFormatCombo;
	private JComboBox videoFormatCombo;
	private static Dimension PREFERRED_VIDEO_DIMENSIONS = new Dimension(320,
			240); // this to be set with a preferences page later on.
	public static boolean AUDIO_ON = false; // this to be set via a preference page later on.

	private static CaptureDeviceDialog			instance;

	public static CaptureDeviceDialog getInstance()
	{
		if (instance == null)
		{
			instance = new CaptureDeviceDialog(CreateWindow.getInstance(), "Capture Device", true);
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private CaptureDeviceDialog(JFrame parent, String title, boolean mode) {
		super(parent, title, mode);
		initUI();


		Vector<CaptureDeviceInfo> captureDevices = CaptureDeviceManager
				.getDeviceList(null);
		if (captureDevices != null && captureDevices.size() > 0) {
			sortCaptureDevices(captureDevices);
		} else {
			RecordingException.showMessageAndThrowRecordingException(this,
					new RuntimeException(), "No capture devices found");
		}

	}

	private void initUI() {
		this.setLayout(new BorderLayout());
		JPanel devicePanel = new JPanel();
		devicePanel.setLayout(new GridBagLayout());

		if (AUDIO_ON) {
			initAudioUI(devicePanel);
			audioDeviceCombo.addItemListener(this);
		}
		
		initVideoUI(devicePanel);
		videoDeviceCombo.addItemListener(this);

		this.add(devicePanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		JButton OKbutton = new JButton(OK_ACTION);
		buttonPanel.add(OKbutton);

		JButton cancelButton = new JButton(CANCEL_ACTION);
		buttonPanel.add(cancelButton);

		this.add(buttonPanel, BorderLayout.SOUTH);
		OKbutton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	private void sortCaptureDevices(Vector<CaptureDeviceInfo> captureDevices) {
		if (AUDIO_ON)
			sortAudioCaptureDevices(captureDevices);
		sortVideoDevices(captureDevices);
	}

	private void sortVideoDevices(Vector<CaptureDeviceInfo> captureDevices) {
		videoDevices = new Vector<CaptureDeviceInfo>();
		for (int i = 0; i < captureDevices.size(); i++) {
			CaptureDeviceInfo captureDeviceInfo = (CaptureDeviceInfo) captureDevices
					.elementAt(i);
			Format[] formats = captureDeviceInfo.getFormats();
			for (int j = 0; j < formats.length; j++) {
				if (formats[j] instanceof VideoFormat) {
					videoDevices.addElement(captureDeviceInfo);
					videoDeviceCombo.addItem(captureDeviceInfo.getName());
					break;
				}
			}
		}
		if (videoDevices.size() <= 0) {
			RecordingException.showMessageAndThrowRecordingException(this,
					new RuntimeException(), "No video capture devices found");
		}
	}

	private void sortAudioCaptureDevices(
			Vector<CaptureDeviceInfo> captureDevices) {
		if (!AUDIO_ON)
			return;
		audioDevices = new Vector<CaptureDeviceInfo>();
		for (int i = 0; i < captureDevices.size(); i++) {
			CaptureDeviceInfo captureDeviceInfo = (CaptureDeviceInfo) captureDevices
					.elementAt(i);
			Format[] formats = captureDeviceInfo.getFormats();
			for (int j = 0; j < formats.length; j++) {
				if (formats[j] instanceof AudioFormat) {
					audioDevices.addElement(captureDeviceInfo);
					audioDeviceCombo.addItem(captureDeviceInfo.getName());
					break;
				}
			}
		}
		if (audioDevices.size() <= 0) {
			RecordingException.showMessageAndThrowRecordingException(this,
					new RuntimeException(), "No audio capture devices found");
		}

	}

	private void initVideoUI(JPanel devicePanel) {
		JLabel videoDeviceLabel = new JLabel("Video Device(s):");
		GridBagConstraints gbConstraints = setGridConstraints(2, 0, 1, 1,
				GridBagConstraints.NONE);
		devicePanel.add(videoDeviceLabel, gbConstraints);

		videoDeviceCombo = new JComboBox();
		gbConstraints = setGridConstraints(2, 1, 2, 1,
				GridBagConstraints.HORIZONTAL);
		devicePanel.add(videoDeviceCombo, gbConstraints);

		JLabel videoFormatLabel = new JLabel("Video Format(s):");
		gbConstraints = setGridConstraints(3, 0, 1, 1, GridBagConstraints.NONE);
		devicePanel.add(videoFormatLabel, gbConstraints);

		videoFormatCombo = new JComboBox();
		gbConstraints = setGridConstraints(3, 1, 2, 1,
				GridBagConstraints.HORIZONTAL);
		devicePanel.add(videoFormatCombo, gbConstraints);
	}

	private void initAudioUI(JPanel devicePanel) {
		if (!AUDIO_ON) return;
		JLabel audioDeviceLabel = new JLabel("Audio Device(s):");
		GridBagConstraints gbConstraints = setGridConstraints(0, 0, 1, 1,
				GridBagConstraints.NONE);
		devicePanel.add(audioDeviceLabel, gbConstraints);

		audioDeviceCombo = new JComboBox();
		gbConstraints = setGridConstraints(0, 1, 2, 1,
				GridBagConstraints.HORIZONTAL);
		devicePanel.add(audioDeviceCombo, gbConstraints);

		JLabel audioFormatLabel = new JLabel("Audio Format(s):");
		gbConstraints = setGridConstraints(1, 0, 1, 1, GridBagConstraints.NONE);
		devicePanel.add(audioFormatLabel, gbConstraints);

		audioFormatCombo = new JComboBox();
		gbConstraints = setGridConstraints(1, 1, 2, 1,
				GridBagConstraints.HORIZONTAL);
		devicePanel.add(audioFormatCombo, gbConstraints);
	}

	private void populateAudioFormats() {
		if (!AUDIO_ON)
			return;
		audioFormats.removeAllElements();
		audioFormatCombo.removeAllItems();

		int selectedIndex = audioDeviceCombo.getSelectedIndex();
		if (selectedIndex != -1) { // a device is selected
			CaptureDeviceInfo captureDeviceInfo = (CaptureDeviceInfo) audioDevices
					.elementAt(selectedIndex);
			if (captureDeviceInfo != null) {
				Format[] formats = captureDeviceInfo.getFormats();
				for (int j = 0; j < formats.length; j++) {
					if (formats[j] instanceof AudioFormat) {
						AudioFormat audioFormat = (AudioFormat) formats[j];
						if (correctAudioFormat(audioFormat)) {
							String audioFormatString = audioFormat.toString();
							if (audioFormatString.length() > FORMAT_STRING_MAX_LENGTH) audioFormatString = audioFormatString.substring(0, FORMAT_STRING_MAX_LENGTH) + "...";
							audioFormatCombo.addItem(audioFormatString);
							audioFormats.add(audioFormat);
						}
					}
				}
			}
		}
		if (audioFormats.size() <= 0) {
			RecordingException
					.showMessageAndThrowRecordingException(this,
							new RuntimeException(),
							"No audio capture devices meet the desired specifications.");
		}
	}

	private void populateVideoFormats() {
		videoFormats.removeAllElements();
		videoFormatCombo.removeAllItems();

		int selectedIndex = videoDeviceCombo.getSelectedIndex();
		if (selectedIndex != -1) { // a device is selected
			CaptureDeviceInfo captureDeviceInfo = (CaptureDeviceInfo) videoDevices
					.elementAt(selectedIndex);
			if (captureDeviceInfo != null) {
				Format[] formats = captureDeviceInfo.getFormats();
				for (int j = 0; j < formats.length; j++) {
					if (formats[j] instanceof VideoFormat) {
						VideoFormat videoFormat = (VideoFormat) formats[j];
						if (correctVideoFormat(videoFormat)) {
							String videoFormatString = videoFormat.toString();
							if (videoFormatString.length() > FORMAT_STRING_MAX_LENGTH) videoFormatString = videoFormatString.substring(0, FORMAT_STRING_MAX_LENGTH) + "...";
							videoFormatCombo.addItem(videoFormatString);
							videoFormats.add(videoFormat);
						}
					}
				}
			}
		}
		if (videoDevices.size() <= 0) {
			RecordingException.showMessageAndThrowRecordingException(this,
					new RuntimeException(),
					"No video formats meet the required specifications.");
		}
	}

	private boolean correctVideoFormat(VideoFormat format) {
		return (format.getSize().equals(PREFERRED_VIDEO_DIMENSIONS));
	}

	private boolean correctAudioFormat(AudioFormat format) {
		return true; // to be determined
	}

	public CaptureDeviceInfo getAudioDevice() {
		if (!AUDIO_ON) return null;
		CaptureDeviceInfo cdi = null;
		if (audioDeviceCombo != null) {
			int i = audioDeviceCombo.getSelectedIndex();
			cdi = (CaptureDeviceInfo) audioDevices.elementAt(i);
		}
		return cdi;
	}

	public CaptureDeviceInfo getVideoDevice() {
		CaptureDeviceInfo cdi = null;
		if (videoDeviceCombo != null) {
			int i = videoDeviceCombo.getSelectedIndex();
			cdi = (CaptureDeviceInfo) videoDevices.elementAt(i);
		}
		return cdi;
	}

	public Format getAudioFormat() {
		if (!AUDIO_ON) return null;
		Format format = null;
		if (audioFormatCombo != null) {
			int i = audioFormatCombo.getSelectedIndex();
			format = (Format) audioFormats.elementAt(i);
		}
		return format;
	}

	public Format getVideoFormat() {
		Format format = null;
		if (videoFormatCombo != null) {
			int i = videoFormatCombo.getSelectedIndex();
			format = (Format) videoFormats.elementAt(i);
		}
		return format;
	}

	public boolean hasConfigurationChanged() {
		return configurationChanged;
	}

	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand().toString();
		if (command.equals(OK_ACTION)) {
			configurationChanged = true;
			try
			{
				RecordingWindow recordingFrame = RecordingWindow
						.getInstance();
				recordingFrame.addDisplayDataSource();
				recordingFrame.setSize(400, 400);
				recordingFrame.setLocationRelativeTo(null);
				recordingFrame.setVisible(true);
			}
			catch (RecordingException re)
			{
				CreateWindow.getInstance().setVisible(true);
			}
		}
		else if (command.equals(CANCEL_ACTION)) {
			CreateWindow.getInstance().setVisible(true);
		}
		this.dispose();
	}

	public void itemStateChanged(ItemEvent ie) {
		if (ie.getSource().equals(audioDeviceCombo)) {
			populateAudioFormats();
		} else {
			populateVideoFormats();
		}
		this.pack();
	}

	private GridBagConstraints setGridConstraints(int row, int column,
			int width, int height, int fill) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = row;
		constraints.gridx = column;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.fill = fill; // GridBagConstraints.NONE .HORIZONTAL
		// .VERTICAL .BOTH
		constraints.insets = new Insets(5, 5, 5, 5);
		return constraints;
	}
}
