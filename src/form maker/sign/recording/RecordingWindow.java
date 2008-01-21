package sign.recording;

import java.awt.BorderLayout;

import javax.media.Processor;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sign.SignlinkIcons;

/**
 * The RecordingWindow which allows users to record video for use in Signlink
 * Studio
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: RecordingWindow.java 106 2008-01-15 18:29:17Z laurel $
 */
public class RecordingWindow extends JFrame {

	private static final long serialVersionUID = 2496444944755748873L;

	private static final String WINDOW_TITLE = "Record a video - Signlink Studio";
	
	private SignlinkIcons images = SignlinkIcons.getInstance();
	
	private javax.swing.JPanel centerPanel;

	private javax.swing.JLabel messageLabel;

	private javax.swing.JPanel southPanel;

	private DisplayDataSource displayDataSource;
	
	private RecordingDataSource recordDataSource;

	private static RecordingWindow instance;

	public static RecordingWindow getInstance() {
		if (instance == null) {
			instance = new RecordingWindow();
		}
		return instance;
	}

	private RecordingWindow() {
		this.initComponents();
	}
	
	public void addDisplayDataSource() {
		this.displayDataSource = DisplayDataSource.getInstance(this);
		Processor displayProcessor = this.displayDataSource.getDisplayProcessor();
		centerPanel.add(displayProcessor.getVisualComponent(),
				BorderLayout.CENTER);
	}

	private void initComponents() {
		this.setIconImage(images.signEdIcon16);
		messageLabel = new javax.swing.JLabel();

		setTitle(WINDOW_TITLE);
		this.setResizable(false);
		
		this.southPanel = new javax.swing.JPanel();
		this.southPanel.setLayout(new BorderLayout());
		RecordButtonsPanel recordPanel = RecordButtonsPanel.getInstance(this);
		this.southPanel.add(recordPanel, BorderLayout.CENTER);
		JPanel contents = new JPanel(new BorderLayout(10,10));
		contents.add(this.southPanel, BorderLayout.SOUTH);
		
		this.centerPanel = new javax.swing.JPanel();
		this.centerPanel.setLayout(new BorderLayout());
		contents.add(centerPanel, BorderLayout.CENTER);
		//TODO WEST should be the help Panel 
		contents.add(Box.createHorizontalStrut(30),BorderLayout.WEST);
		contents.add(Box.createHorizontalStrut(30), BorderLayout.EAST);
		contents.add(Box.createHorizontalStrut(10), BorderLayout.NORTH);

		addWindowListener(RecordingListener.getInstance());
		this.add(contents);

		pack();
	}

	public RecordingDataSource getRecordDataSource() {
		return recordDataSource;
	}

	public void setRecordDataSource(RecordingDataSource recordDataSource) {
		this.recordDataSource = recordDataSource;
	}

}