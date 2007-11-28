package signlinkform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
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
 * @version $Id: VideoButtonsPanel.java 67 2007-11-22 18:31:28Z martin $
 */
public class VideoButtonsPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2174365661316733013L;
	private final ImageIcon		playIcon			= new ImageIcon(Toolkit.getDefaultToolkit().getImage(
															this.getClass().getResource("/icons/sPlay.jpg")));
	private final ImageIcon		pauseIcon			= new ImageIcon(Toolkit.getDefaultToolkit().getImage(
															this.getClass().getResource("/icons/sStopRecPreview.jpg")));
	private JButton				playPauseButton;
	private JButton				rewindButton;
	private ImageIcon			rewindIcon;
	private JButton				fastForwardButton;
	private ImageIcon			fastForwardIcon;
	private JButton				frameBackButton;
	private ImageIcon			frameBackIcon;
	private JButton				frameForwardButton;
	private ImageIcon			frameForwardIcon;
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
	public VideoButtonsPanel(final ActionListener vListen)
	{
		listen = vListen;
		vComponent = ((VideoListener) listen).getVideoComponent();
		this.setLayout(new BorderLayout());
		addSlider();
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
		final VideoListener vListen = (VideoListener) listen;
		this.setVComponent(vListen.getVideoComponent());
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

		final JButton helpButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/icons/sHelp.jpg"))));
		helpButton.setActionCommand("help");
		helpButton.addActionListener(listen);
		helpButton.setPreferredSize(new Dimension(22, 22));

		bPanel.add(Box.createHorizontalStrut(10));
		bPanel.add(helpButton);
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

		rewindIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/sRewind.jpg")));
		rewindButton = new JButton(rewindIcon);
		rewindButton.setMinimumSize(new Dimension(30, 30));
		rewindButton.setMaximumSize(new Dimension(30, 30));
		rewindButton.setPreferredSize(new Dimension(30, 30));

		fastForwardIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/sFastForward.jpg")));
		fastForwardButton = new JButton(fastForwardIcon);
		fastForwardButton.setMinimumSize(new Dimension(30, 30));
		fastForwardButton.setMaximumSize(new Dimension(30, 30));
		fastForwardButton.setPreferredSize(new Dimension(30, 30));

		frameBackIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/sStepBackward.jpg")));
		frameBackButton = new JButton(frameBackIcon);
		frameBackButton.setMinimumSize(new Dimension(30, 30));
		frameBackButton.setMaximumSize(new Dimension(30, 30));
		frameBackButton.setPreferredSize(new Dimension(30, 30));

		frameForwardIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/sStepForward.jpg")));
		frameForwardButton = new JButton(frameForwardIcon);
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
	protected void addSlider()
	{ 
		vSlide = new SignSlider(0, vComponent.getVideoPanel().getDurationMilliSec(), 0);
		vSlide.setPreferredSize(new Dimension(320, 38));
		vSlide.setSize(new Dimension(320, 38));
		vSlide.setBackground(Color.WHITE);
		final SignSliderUI sUI = new SignSliderUI(vSlide);
		vSlide.setUI(sUI);
		vSlide.setSUI(sUI);
		vSlide.setName("Main");
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
		Font font = new Font("arial", Font.PLAIN, 12);
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

	protected ActionListener getListen()
	{
		return this.listen;
	}

	protected String getMil()
	{
		return this.mil;
	}

	protected String getMin()
	{
		return this.min;
	}

	protected JButton getPlayPauseButton()
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

	protected String getSec()
	{
		return this.sec;
	}

	protected Timer getSlideTimer()
	{
		return this.slideTimer;
	}

	protected VideoComponent getVComponent()
	{
		return vComponent;
	}

	/**
	 * @return JSlider - the video slider
	 */
	protected SignSlider getVSlide()
	{
		return this.vSlide;
	}

	protected JTextField getVText()
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

	protected void setFrameBackButton(JButton frameBackButton)
	{
		this.frameBackButton = frameBackButton;
	}

	protected void setFrameForwardButton(JButton frameForwardButton)
	{
		this.frameForwardButton = frameForwardButton;
	}

	protected void setListen(ActionListener listen)
	{
		this.listen = listen;
	}

	protected void setMil(String mil)
	{
		this.mil = mil;
	}

	protected void setMin(String min)
	{
		this.min = min;
	}

	protected void setPlayPauseButton(JButton playPauseButton)
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

	protected void setSec(String sec)
	{
		this.sec = sec;
	}

	protected void setSlideTimer(Timer slideTimer)
	{
		this.slideTimer = slideTimer;
	}

	protected void setVComponent(final VideoComponent vComp)
	{

		vComponent = vComp;
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
	protected ImageIcon getPlayIcon()
	{
		return this.playIcon;
	}

	/**
	 * Getter method to return the value of pauseIcon
	 * 
	 * @return the pauseIcon
	 */
	protected ImageIcon getPauseIcon()
	{
		return this.pauseIcon;
	}
}
