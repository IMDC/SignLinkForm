package sign.recording;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

import javax.media.Processor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import sign.HelpFrame;
import sign.SignlinkIcons;

/**
 * The RecordingWindow which allows users to record video for use in Signlink Studio
 * 
 * @author Martin Gerdzhev
 * @version $Id: RecordingWindow.java 118 2008-01-29 17:56:25Z martin $
 */
public class RecordingWindow extends JFrame
{

	private static final long		serialVersionUID	= 2496444944755748873L;
	private static final String		WINDOW_TITLE		= "Record a video - Signlink Studio";
	private SignlinkIcons			images				= SignlinkIcons.getInstance();
	private JPanel					recordingPanel;
	private DisplayDataSource		displayDataSource;
	private RecordingDataSource		recordDataSource;
	private static RecordingWindow	instance;
	private File					recordingFile;
	public static final String		NEXT_ACTION			= "next";
	public static final String		CANCEL_ACTION		= "cancel";
	private static final String		NEXT_BUTTON_TEXT	= "Next >";
	private static final String		CANCEL_BUTTON_TEXT	= "Cancel";
	private JButton					nextButton;
	private JButton					cancelButton;
	private JTabbedPane				tabbedPane;
	public static int				WIDTH				= 500;
	public static int				HEIGHT				= 475;

	public static RecordingWindow getInstance()
	{
		if (instance == null)
		{
			instance = new RecordingWindow();
		}
		return instance;
	}

	private RecordingWindow()
	{
		this.chooseFileName();
		this.initComponents();
		this.addButtons();
	}

	/**
	 * 
	 */
	private void addButtons()
	{
		// TODO Auto-generated method stub
		nextButton = new JButton(NEXT_BUTTON_TEXT);
		nextButton.setEnabled(false);
		cancelButton = new JButton(CANCEL_BUTTON_TEXT);
		nextButton.setToolTipText("Next");
		nextButton.setActionCommand(NEXT_ACTION);
		cancelButton.setToolTipText("Cancel");
		cancelButton.setActionCommand(CANCEL_ACTION);
		nextButton.addActionListener(RecordingListener.getInstance());
		cancelButton.addActionListener(RecordingListener.getInstance());
		JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		controlButtonsPanel.add(nextButton);
		controlButtonsPanel.add(cancelButton);
		this.getContentPane().add(controlButtonsPanel, BorderLayout.SOUTH);

	}

	/**
	 * 
	 */
	private void chooseFileName()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new MOVFilter());
		int returnVal = fileChooser.showSaveDialog(this);
		while (returnVal != JFileChooser.APPROVE_OPTION)
		{
			JOptionPane.showMessageDialog(this, "Please select a file to record to", "Select a file", JOptionPane.WARNING_MESSAGE);
			returnVal = fileChooser.showSaveDialog(this);
		}
		if (fileChooser.getSelectedFile().getName().toLowerCase().endsWith(".mov"))
		{

			recordingFile = new File(fileChooser.getSelectedFile().getAbsolutePath().substring(0,
					fileChooser.getSelectedFile().getAbsolutePath().lastIndexOf("."))
					+ ".mov");
		}
		else
			recordingFile = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".mov");
	}

	public void addDisplayDataSource()
	{
		this.displayDataSource = DisplayDataSource.getInstance(this);
		Processor displayProcessor = this.displayDataSource.getDisplayProcessor();
		JPanel centerPanel = new JPanel();
		centerPanel.add(displayProcessor.getVisualComponent());
		recordingPanel.add(centerPanel, BorderLayout.CENTER);
	}

	private void initComponents()
	{
		this.setIconImage(images.signEdIcon16);
		this.setTitle(WINDOW_TITLE);
		this.setResizable(false);
		this.setSize(WIDTH, HEIGHT);
		tabbedPane = new JTabbedPane();
		
		recordingPanel = new JPanel();
		initRecordingPanel(recordingPanel);
		Color recordingPanelBackground = recordingPanel.getBackground();
		tabbedPane.setBackground(recordingPanelBackground);
		tabbedPane.addTab("Record", recordingPanel);
		//FIXME can't figure out how to get the background on the tabbed pane working.
//		UIManager.put("TabbedPane.background", Color.red);

//		tabbedPane.setBackground(recordingPanelBackground);
//		tabbedPane.setBackgroundAt(0, recordingPanelBackground);

		PreviewingPanel previewingPanel = PreviewingPanel.getInstance(recordingFile);
		previewingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tabbedPane.addTab("Preview", previewingPanel);
//		tabbedPane.setBackgroundAt(1, recordingPanelBackground);

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		this.getContentPane().add(Box.createHorizontalStrut(30), BorderLayout.WEST);
		this.setPreviewEnabled(false);

		JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
		JButton helpButton = new JButton(images.helpImageIcon);
		helpButton.setActionCommand("help");
// //TODO add a listener for the help button
// //helpButton.addActionListener(listen);
		helpButton.setPreferredSize(new Dimension(22, 22));
		helpPanel.add(helpButton);
//		helpButton.setEnabled(HelpFrame.isHelpEnabled());
		helpButton.setEnabled(false);
		this.getContentPane().add(helpPanel, BorderLayout.EAST);

		addWindowListener(RecordingListener.getInstance());
		this.setVisible(true);
	}

	public void setPreviewEnabled(boolean enabled)
	{
		tabbedPane.setEnabledAt(1, enabled);
	}

	private void initRecordingPanel(JPanel panel)
	{
		panel.setLayout(new BorderLayout(0, 5));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setPreferredSize(new Dimension(350, 400));
		panel.setSize(new Dimension(350, 400));

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		RecordButtonsPanel recordButtonsPanel = RecordButtonsPanel.getInstance(this);
		southPanel.add(recordButtonsPanel, BorderLayout.CENTER);
		southPanel.add(Box.createVerticalStrut(15), BorderLayout.SOUTH);
		southPanel.add(Box.createHorizontalStrut(15), BorderLayout.EAST);
		southPanel.add(Box.createHorizontalStrut(15), BorderLayout.WEST);
		panel.add(southPanel, BorderLayout.SOUTH);
		panel.add(Box.createVerticalStrut(15), BorderLayout.NORTH);
		panel.add(Box.createHorizontalStrut(15), BorderLayout.EAST);
		panel.add(Box.createHorizontalStrut(15), BorderLayout.WEST);
	}

	public RecordingDataSource getRecordDataSource()
	{
		return recordDataSource;
	}

	public void setRecordDataSource(RecordingDataSource recordDataSource)
	{
		this.recordDataSource = recordDataSource;
	}

	/**
	 * Getter method to return the value of recordingFile
	 * 
	 * @return the recordingFile
	 */
	public File getRecordingFile()
	{
		return this.recordingFile;
	}

}