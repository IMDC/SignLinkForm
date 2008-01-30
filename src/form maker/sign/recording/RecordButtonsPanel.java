package sign.recording;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import sign.SignSlider;
import sign.SignSliderUI;
import sign.SignlinkIcons;

/**
 * A panel class that holds the buttons & boxes & the slider needed for recording
 * 
 * @author Martin Gerdzhev
 * @version $Id: RecordButtonsPanel.java 117 2008-01-29 16:38:20Z martin $
 */
public class RecordButtonsPanel extends JPanel
{
	private static final long			serialVersionUID	= -4699338259162317239L;
	private JButton						recordButton;
	private JButton						stopButton;
	public static final int				TIMERMAX			= 300000;

	private SignlinkIcons				images				= SignlinkIcons.getInstance();

	public static final String			RECORD_ACTION		= "record";
	public static final String			PLAY_ACTION			= "play";
	public static final String			STOP_ACTION			= "stop";

	private SignSlider					vSlide;
	private Timer						slideTimer;
	protected JTextField				timerText;
	private JPanel						recordButtonsPanel;
	private JPanel						timerPanel;
	private RecordingWindow				mainFrame;

	private static RecordButtonsPanel	instance;

	public static RecordButtonsPanel getInstance(RecordingWindow recordingWindow)
	{
		if (instance == null)
		{
			instance = new RecordButtonsPanel(recordingWindow);
		}
		return instance;
	}

	/**
	 * Constructor that adds everything to itself
	 * 
	 * @param recordingWindow -
	 *            the main frame used for getting other components
	 */
	private RecordButtonsPanel(RecordingWindow recordingWindow)
	{
		recordButtonsPanel = new JPanel();
		timerPanel = new JPanel();
		mainFrame = recordingWindow;
		this.setLayout(new BorderLayout());
		createButtons();
		addDescription();
		addButtonListeners();
		addTextField();
		addButtons();
		addSlider();
	}

	/**
	 * A method to create the buttons on this panel
	 */
	private void createButtons()
	{
		recordButton = new JButton(images.recordImageIcon);
		stopButton = new JButton(images.stopImageIcon);
		stopButton.setEnabled(false);
	}

	/**
	 * A method to add description and Action commands to the buttons
	 */
	protected void addDescription()
	{
		recordButton.setToolTipText("Record");
		recordButton.setActionCommand(RECORD_ACTION);
		stopButton.setToolTipText("Stop");
		stopButton.setActionCommand(STOP_ACTION);

	}

	/**
	 * A method that gets the RecordButton
	 * 
	 * @return - the recordStopButton
	 */
	protected JButton getRecordButton()
	{
		return recordButton;
	}

	/**
	 * @return - the stop button
	 */
	protected JButton getStopButton()
	{
		return stopButton;
	}

	/**
	 * A method that gets the main frame
	 * 
	 * @return
	 */
	protected RecordingWindow getMainFrame()
	{
		return mainFrame;
	}

	/**
	 * A method that adds listeners to the buttons
	 */
	protected void addButtonListeners()
	{
		RecordingListener recordingButtonListener = RecordingListener.getInstance();
		recordButton.addActionListener(recordingButtonListener);
		stopButton.addActionListener(recordingButtonListener);

	}

	/**
	 * A method that adds a textField for displaying the current time in the video and a message about maximum time
	 */
	protected void addTextField()
	{
		timerText = new JTextField();
		timerText.setMinimumSize(new Dimension(65, 25));
		timerText.setMaximumSize(new Dimension(65, 25));
		timerText.setPreferredSize(new Dimension(65, 25));
		timerText.setText("00:00:000");
		Font font = new Font("arial", Font.PLAIN, 12);
		timerText.setFont(font);
		timerText.setEditable(false);
		timerText.setBackground(Color.WHITE);
		
		timerPanel.add(timerText);
		JLabel timerMessageText = new JLabel("Max: 5 minutes");
		timerMessageText.setFont(font);
		timerPanel.add(timerMessageText);
		this.add(timerPanel, BorderLayout.EAST);
	}

	/**
	 * A method that adds the buttons to the panel
	 */
	protected void addButtons()
	{
		recordButtonsPanel.add(recordButton);
		recordButtonsPanel.add(stopButton);
		this.add(recordButtonsPanel, BorderLayout.WEST);
	}

	/**
	 * a method to add a slider to the panel
	 */
	protected void addSlider()
	{
		vSlide = new SignSlider(0, TIMERMAX, 0);
		final SignSliderUI sUI = new SignSliderUI(vSlide);
		vSlide.setPreferredSize(new Dimension(320, 38));
		vSlide.setSize(new Dimension(320, 38));
		vSlide.setBackground(Color.WHITE);
		vSlide.setUI(sUI);
		vSlide.setSUI(sUI);
		vSlide.setRecording(true);
		slideTimer = new Timer(50, RecordingListener.getInstance());
		vSlide.setEnabled(false);
		vSlide.setVisible(true);
		final JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		north.setBackground(Color.WHITE);
		north.add(vSlide);
		north.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		// this panel is needed for the purposes of drawing vSlide correctly if border is used around vSlide it breaks
		JPanel northB = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		northB.add(north);
		this.add(northB, BorderLayout.NORTH);

	}

	/**
	 * Getter method to return the value of timerText
	 * 
	 * @return the timerText
	 */
	public JTextField getTimerText()
	{
		return this.timerText;
	}

	/**
	 * Setter method to set the value of timerText
	 * 
	 * @param timerText
	 *            the value to set
	 */
	public void setTimerText(JTextField timerText)
	{
		this.timerText = timerText;
	}

	/**
	 * Getter method to return the value of slideTimer
	 * 
	 * @return the slideTimer
	 */
	public Timer getSlideTimer()
	{
		return this.slideTimer;
	}

	/**
	 * Getter method to return the value of vSlide
	 * 
	 * @return the vSlide
	 */
	public SignSlider getVSlide()
	{
		return this.vSlide;
	}
}
