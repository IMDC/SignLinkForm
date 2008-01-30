package sign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * A panel class to hold the video manipulation buttons
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: VideoButtonsPanel.java 118 2008-01-29 17:56:25Z martin $
 */
public class VideoButtonsPanel extends JPanel
{
	private static final long	serialVersionUID	= -2174365661316733013L;
	private final SignlinkIcons images = SignlinkIcons.getInstance();
	
	private JButton				playPauseButton;
	private JButton				rewindButton;
	private JButton				fastForwardButton;
	private JButton				frameBackButton;
	private JButton				frameForwardButton;
	private SignSlider			vSlide;
	private JPanel				bPanel;
	private JTextField			vText;
	private Timer				slideTimer;
	private VideoComponent		vComponent;
	private ActionListener		listen;
	private String				min					= "00";
	private String				sec					= "00";
	private String				mil					= "000";

	/**
	 * Constructor that adds everything to the panel
	 * 
	 * @param aComponent -
	 *            the video component that is added to this panel
	 */
	public VideoButtonsPanel(VideoComponent vComp,final ActionListener vListen)
	{
		listen = vListen;
		vComponent = vComp;
		this.setLayout(new BorderLayout());
		addSlider(listen.toString());
		addTextField();
		addIcons();
		addListener(listen);
		vSlide.addChangeListener(new TPSliderListener(vSlide, vComponent, vText));
		vSlide.addMouseListener(new CompMouseListener(vSlide, vComponent, vText));
		vSlide.addMouseMotionListener(new MMotionListener(vSlide, vComponent, vText));
		addDescription();
		addButtons();
	}

	public VideoButtonsPanel(final ActionListener vListen, final boolean b)
	{
		listen = vListen;
		vComponent = ((TimingVideoListener) listen).getVideoComponent();
		this.setLayout(new BorderLayout());
		addTextField();
		addIcons();
		addListener(listen);
		addDescription();
	}

	/**
	 * adds the buttons to the pane
	 */
	private void addButtons()
	{
//		final VideoListener vListen = (VideoListener) listen;
//		this.setVComponent(vListen.getVideoComponent());
		bPanel = new JPanel();
		bPanel.add(Box.createHorizontalStrut(8));
		bPanel.add(vText);
		bPanel.add(Box.createHorizontalStrut(25));
		bPanel.add(playPauseButton);
		bPanel.add(new JSeparator(SwingConstants.VERTICAL));
		bPanel.add(rewindButton);
		bPanel.add(frameBackButton);
		bPanel.add(new JSeparator(SwingConstants.VERTICAL));
		bPanel.add(frameForwardButton);
		bPanel.add(fastForwardButton);

		final JButton helpButton = new JButton(images.helpImageIcon);
		helpButton.setActionCommand("help");
		helpButton.addActionListener(listen);
		helpButton.setPreferredSize(new Dimension(22, 22));

		bPanel.add(Box.createHorizontalStrut(10));
		bPanel.add(helpButton);
		helpButton.setEnabled(HelpFrame.isHelpEnabled());
		this.add(bPanel, BorderLayout.WEST);
	}

	/**
	 * Adds a tooltip and an action command to the buttons
	 */
	private void addDescription()
	{
		playPauseButton.setToolTipText("Play/Stop");
		playPauseButton.setActionCommand("play pause");
		rewindButton.setToolTipText("Rewind");
		rewindButton.setActionCommand("rewind");
		fastForwardButton.setToolTipText("Fast Forward");
		fastForwardButton.setActionCommand("fast forward");
		frameBackButton.setToolTipText("Frame Back");
		frameBackButton.setActionCommand("frame back");
		frameForwardButton.setToolTipText("Frame Forward");
		frameForwardButton.setActionCommand("frame forward");
	}

	/**
	 * A method to add Icons to the buttons and create the buttons
	 */
	private void addIcons()
	{
		playPauseButton = new JButton(this.getPlayIcon());
		playPauseButton.setMinimumSize(new Dimension(30, 30));
		playPauseButton.setMaximumSize(new Dimension(30, 30));
		playPauseButton.setPreferredSize(new Dimension(30, 30));

		rewindButton = new JButton(images.rewindIcon);
		rewindButton.setMinimumSize(new Dimension(30, 30));
		rewindButton.setMaximumSize(new Dimension(30, 30));
		rewindButton.setPreferredSize(new Dimension(30, 30));

		fastForwardButton = new JButton(images.fastForwardIcon);
		fastForwardButton.setMinimumSize(new Dimension(30, 30));
		fastForwardButton.setMaximumSize(new Dimension(30, 30));
		fastForwardButton.setPreferredSize(new Dimension(30, 30));

		frameBackButton = new JButton(images.frameBackIcon);
		frameBackButton.setMinimumSize(new Dimension(30, 30));
		frameBackButton.setMaximumSize(new Dimension(30, 30));
		frameBackButton.setPreferredSize(new Dimension(30, 30));

		frameForwardButton = new JButton(images.frameForwardIcon);
		frameForwardButton.setMinimumSize(new Dimension(30, 30));
		frameForwardButton.setMaximumSize(new Dimension(30, 30));
		frameForwardButton.setPreferredSize(new Dimension(30, 30));
	}

	/**
	 * A Method to add listeners to the buttons
	 * 
	 * @param b -
	 *            an action listener for the buttons
	 */
	protected void addListener(final ActionListener b)
	{
		playPauseButton.addActionListener(b);
		rewindButton.addActionListener(b);
		fastForwardButton.addActionListener(b);
		frameBackButton.addActionListener(b);
		frameForwardButton.addActionListener(b);
	}

	/**
	 * a method to add a slider to the panel
	 */
	protected void addSlider(String name)
	{ 
		vSlide = new SignSlider(0, vComponent.getVideoPanel().getDurationMilliSec(), 0);
		vSlide.setPreferredSize(new Dimension(320, 38));
		vSlide.setSize(new Dimension(320, 38));
		vSlide.setBackground(Color.WHITE);
		final SignSliderUI sUI = new SignSliderUI(vSlide);
		vSlide.setUI(sUI);
		vSlide.setSUI(sUI);
		vSlide.setName(name);
		slideTimer = new Timer(50, listen);
		vSlide.setEnabled(true);
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
	 * a method to create a textfield to hold the timer
	 */
	protected void addTextField()
	{
		vText = new JTextField();
		vText.setMinimumSize(new Dimension(65, 25));
		vText.setMaximumSize(new Dimension(65, 25));
		vText.setPreferredSize(new Dimension(65, 25));
		vText.setText("00:00:000");
		vText.setFont(font);
		vText.setEditable(false);
		vText.setBackground(Color.WHITE);
	}

	protected JButton getFrameBackButton()
	{
		return this.frameBackButton;
	}

	protected JButton getFrameForwardButton()
	{
		return this.frameForwardButton;
	}

	public ActionListener getListen()
	{
		return this.listen;
	}

	public String getMil()
	{
		return this.mil;
	}

	public String getMin()
	{
		return this.min;
	}

	public JButton getPlayPauseButton()
	{
		return this.playPauseButton;
	}

	/**
	 * A Method to get the icon of the play/pause button
	 * 
	 * @return the current icon for the play/pause button
	 */
	public ImageIcon getPlayPauseIcon()
	{
		return (ImageIcon) playPauseButton.getIcon();
	}

	public String getSec()
	{
		return this.sec;
	}

	public Timer getSlideTimer()
	{
		return this.slideTimer;
	}

	public VideoComponent getVComponent()
	{
		return vComponent;
	}

	/**
	 * @return JSlider - the video slider
	 */
	public SignSlider getVSlide()
	{
		return this.vSlide;
	}

	public JTextField getVText()
	{
		return vText;
	}

	protected void removeListener(final VideoListener b)
	{
		playPauseButton.removeActionListener(b);
		rewindButton.removeActionListener(b);
		fastForwardButton.removeActionListener(b);
		frameBackButton.removeActionListener(b);
		frameForwardButton.removeActionListener(b);
	}

	public void setFrameBackButton(JButton frameBackButton)
	{
		this.frameBackButton = frameBackButton;
	}

	public void setFrameForwardButton(JButton frameForwardButton)
	{
		this.frameForwardButton = frameForwardButton;
	}

	protected void setListen(ActionListener listen)
	{
		this.listen = listen;
	}

	public void setMil(String mil)
	{
		this.mil = mil;
	}

	public void setMin(String min)
	{
		this.min = min;
	}

	public void setPlayPauseButton(JButton playPauseButton)
	{
		this.playPauseButton = playPauseButton;
	}

	/**
	 * A Method to set the icon of the play/pause button
	 * 
	 * @param icon
	 *            the icon for the play/pause button
	 */
	public void setPlayPauseIcon(final ImageIcon icon)
	{
		playPauseButton.setIcon(icon);
		repaint();
	}

	public void setSec(String sec)
	{
		this.sec = sec;
	}

	protected void setSlideTimer(Timer slideTimer)
	{
		this.slideTimer = slideTimer;
	}

	protected void setVSlide(SignSlider slide)
	{
		this.vSlide = slide;
	}

	protected void setVText(final JTextField text)
	{
		vText = text;
	}

	/**
	 * Getter method to return the value of playIcon
	 * 
	 * @return the playIcon
	 */
	public ImageIcon getPlayIcon()
	{
		return images.playImageIcon;
	}

	/**
	 * Getter method to return the value of pauseIcon
	 * 
	 * @return the pauseIcon
	 */
	public ImageIcon getPauseIcon()
	{
		return images.stopImageIcon;
	}
}
