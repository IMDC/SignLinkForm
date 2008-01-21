package sign.recording;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sign.SignSlider;
import sign.SignSliderUI;
import sign.SignlinkIcons;

/**
 * A panel class that holds the buttons & boxes & the slider
 * needed for recording
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: RecordButtonsPanel.java 106 2008-01-15 18:29:17Z laurel $
 */
public class RecordButtonsPanel extends JPanel
{
	private static final long	serialVersionUID	= -4699338259162317239L;
	private JButton recordButton;
	private JButton playButton;
	private JButton stopButton;
	private JButton nextButton;
	private JButton cancelButton;
	
	private SignlinkIcons images = SignlinkIcons.getInstance();
		
	private static final String NEXT_BUTTON_TEXT = "Next";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";
	
	public static final String RECORD_ACTION = "record";
	public static final String PLAY_ACTION = "play";
	public static final String STOP_ACTION = "stop";
	public static final String NEXT_ACTION = "next";
	public static final String CANCEL_ACTION = "cancel";
	
	private SignSlider vSlide;
	private Timer slideTimer;
	protected JTextField timerText;
	private boolean slide;
	private long time=0;
	private String min="00";
	private String sec="00";
	private String mil="000";
	private JPanel recordButtonsPanel;
	private JPanel controlButtonsPanel;
	private JPanel timerPanel;
	private RecordingWindow mainFrame;
	private TextListener textListen;

	private static RecordButtonsPanel instance;

	public static RecordButtonsPanel getInstance(RecordingWindow recordingWindow) {
		if (instance == null) {
			instance = new RecordButtonsPanel(recordingWindow);
		}
		return instance;
	}

	/**
	 * Constructor that adds everything to itself
	 * @param recordingWindow - the main frame used for getting other components
	 */
	private RecordButtonsPanel(RecordingWindow recordingWindow)
	{
		recordButtonsPanel = new JPanel();
		controlButtonsPanel = new JPanel();
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
		playButton = new JButton(images.playImageIcon);
		playButton.setEnabled(false);
		stopButton = new JButton(images.stopImageIcon);
		stopButton.setEnabled(false);
		nextButton = new JButton(NEXT_BUTTON_TEXT);
		nextButton.setEnabled(false);
		cancelButton = new JButton(CANCEL_BUTTON_TEXT);
	}
	
	/**
	 * A method to add description and Action commands to the buttons
	 */
	protected void addDescription()
	{
		recordButton.setToolTipText("Record");
		recordButton.setActionCommand(RECORD_ACTION);
		playButton.setToolTipText("Play");
		playButton.setActionCommand(PLAY_ACTION);
		stopButton.setToolTipText("Stop");
		stopButton.setActionCommand(STOP_ACTION);
		nextButton.setToolTipText("Next");
		nextButton.setActionCommand(NEXT_ACTION);
		cancelButton.setToolTipText("Cancel");
		cancelButton.setActionCommand(CANCEL_ACTION);
	}
	
	/**
	 * A method that gets the RecordButton
	 * @return - the recordStopButton
	 */
	protected JButton getRecordButton()
	{
		return recordButton;
	}
	
	/**
	 * A method that gets the PlayButton
	 * @return - the playButton
	 */
	protected JButton getPlayButton()
	{
		return playButton;
	}
	
	/**
	 * @return - the stop button
	 */
	protected JButton getStopButton() {
		return stopButton;
	}
	
	/**
	 * A method that gets the main frame
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
		playButton.addActionListener(recordingButtonListener);
		stopButton.addActionListener(recordingButtonListener);
		nextButton.addActionListener(recordingButtonListener);
		cancelButton.addActionListener(recordingButtonListener);
	}
	
	/**
	 * A method that adds a textField for displaying the current 
	 * time in the video and a message about maximum time
	 */
	protected void addTextField()
	{
		textListen = new TextListener();
		timerText = new JTextField(8);
		timerText.setText(min + ":" + sec + ":" + mil);
		timerText.setEditable(false);
		timerPanel.add(timerText);
		JLabel timerMessageText = new JLabel("Max: 5 minutes");
		timerPanel.add(timerMessageText);
		this.add(timerPanel, BorderLayout.EAST);
		timerText.addActionListener(textListen);
	}
	
	/**
	 * A class that changes the values of the text field
	 * @author Martin Gerdzhev
	 *
	 */
	class TextListener implements ActionListener
	{

		public void actionPerformed(ActionEvent event) {
			time = mainFrame.getRecordDataSource().getTime()/1000000;
			
			min = "" + (time/1000)/60;
			if (min.length()<2)
				min = "0" + min;
			sec ="" + ((time/1000)%60);
			if (sec.length()<2)
				sec = "0" + sec;
			mil ="" + time%1000;
			if (mil.length()<2)
				mil = "00" + mil;
			else if (mil.length()<3)
				mil = "0" + mil;
			timerText.setText(min + ":" + sec + ":" + mil);
			if (time >= 300000) //if time reaches 5 minutes stop
			{
				JOptionPane.showMessageDialog(null, "Maximum time reached", "Recording Stopped", JOptionPane.INFORMATION_MESSAGE);
				recordButton.doClick();
			}
		}
		
	}
	
	/**
	 * A method that adds the buttons to the panel
	 */
	protected void addButtons()
	{
		recordButtonsPanel.add(recordButton);
		recordButtonsPanel.add(playButton);
		recordButtonsPanel.add(stopButton);
		controlButtonsPanel.add(nextButton);
		controlButtonsPanel.add(cancelButton);
		this.add(recordButtonsPanel, BorderLayout.WEST);
		this.add(controlButtonsPanel, BorderLayout.SOUTH);
	}
	
	
	/**
	 * a method to add a slider to the panel
	 */
	protected void addSlider()
	{
		vSlide = new SignSlider(0, 100, 0);
		final SignSliderUI sUI = new SignSliderUI(vSlide);
		vSlide.setPreferredSize(new Dimension(320, 38));
		vSlide.setSize(new Dimension(320, 38));
		vSlide.setBackground(Color.WHITE);
		vSlide.setUI(sUI);
		vSlide.setSUI(sUI);
		//slideTimer = new Timer(50, vComponent.getVideoListener());
		vSlide.setEnabled(true);
		vSlide.setVisible(true);
		vSlide.addChangeListener(new SliderListener());
		this.add(vSlide, BorderLayout.NORTH);

	}
	
	class SliderListener implements ChangeListener
	{
		
		public void stateChanged(ChangeEvent e) 
		{/*
			try {
				if (slide)
					vComponent.getVideoPanel().getMovie().setTimeValue(vSlide.getValue());
				min = vComponent.getVideoPanel().getMin();
				sec = vComponent.getVideoPanel().getSec();
				mil = vComponent.getVideoPanel().getMil();
				vText.setText(min + ":" + sec + ":" + mil);
				slide = true;
			} catch (StdQTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		
	}
}
