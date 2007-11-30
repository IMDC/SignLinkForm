package sign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TimingPanelControlPanel.java 65 2007-11-22 16:49:31Z martin $
 *
 */
public class TimingPanelControlPanel extends VideoButtonsPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2078253719580405631L;
	private TimingComponent		tComp;
	private JButton				beginningFrameBack;
	private JButton				beginning;
	private JButton				beginningFrameForward;
	private JButton				endingFrameBack;
	private JButton				ending;
	private JButton				endingFrameForward;
	private JButton				previewButton;
	private int					position;
	private int					endPosition;

	public TimingPanelControlPanel(final ActionListener vList, final int pos)
	{

		super(vList, true);
		position = pos;
		endPosition = -1;
		this.setBackground(Color.WHITE);
		this.addButtons();
		this.addSlider();
	}

	public TimingPanelControlPanel(final ActionListener vList, final int startPos, final int endPos)
	{

		super(vList, true);
		position = startPos;
		endPosition = endPos;
		this.setBackground(Color.WHITE);
		this.addButtons();
		this.addSlider();
	}

	private void addButtons()
	{
		final ImageIcon frameBack = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/icons/sHandleStepBackward.jpg")));
		final ImageIcon frameForward = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/icons/sHandleStepForward.jpg")));
		final ImageIcon beginningIcon = new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/icons/sHandleStart.jpg")));
		final ImageIcon endingIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/sHandleEnd.jpg")));
		final ImageIcon previewIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/sPlaySignlink.jpg")));

		beginningFrameForward = new JButton(frameForward);
		beginningFrameForward.setPreferredSize(new Dimension(20, 20));
		beginningFrameForward.setActionCommand("beginning frame forward");
		beginningFrameForward.addActionListener(getListen());
		beginningFrameForward.setBackground(Color.GRAY);

		beginningFrameBack = new JButton(frameBack);
		beginningFrameBack.setPreferredSize(new Dimension(20, 20));
		beginningFrameBack.setActionCommand("beginning frame back");
		beginningFrameBack.addActionListener(getListen());
		beginningFrameBack.setBackground(Color.GRAY);

		beginning = new JButton(beginningIcon);
		beginning.setPreferredSize(new Dimension(20, 20));
		beginning.setActionCommand("beginning");
		beginning.addActionListener(getListen());
		beginning.setBackground(Color.GRAY);

		endingFrameForward = new JButton(frameForward);
		endingFrameForward.setPreferredSize(new Dimension(20, 20));
		endingFrameForward.setActionCommand("ending frame forward");
		endingFrameForward.addActionListener(getListen());
		endingFrameForward.setBackground(Color.GRAY);

		endingFrameBack = new JButton(frameBack);
		endingFrameBack.setPreferredSize(new Dimension(20, 20));
		endingFrameBack.setActionCommand("ending frame back");
		endingFrameBack.addActionListener(getListen());
		endingFrameBack.setBackground(Color.GRAY);

		ending = new JButton(endingIcon);
		ending.setPreferredSize(new Dimension(20, 20));
		ending.setActionCommand("ending");
		ending.addActionListener(getListen());
		ending.setBackground(Color.GRAY);

		previewButton = new JButton("Preview", previewIcon);
		previewButton.setActionCommand("preview");
		previewButton.addActionListener(getListen());
		previewButton.setBackground(Color.GRAY);

		this.getPlayPauseButton().setBackground(Color.GRAY);
		this.getFrameBackButton().setBackground(Color.GRAY);
		this.getFrameForwardButton().setBackground(Color.GRAY);

		final JPanel bPanel = new JPanel();
		final JPanel nPanel = new JPanel();
		final JPanel sPanel = new JPanel();
		nPanel.add(beginningFrameBack);
		nPanel.add(beginning);
		nPanel.add(beginningFrameForward);
		nPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		nPanel.add(endingFrameBack);
		nPanel.add(ending);
		nPanel.add(endingFrameForward);
		nPanel.setBackground(Color.GRAY);
		nPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		bPanel.add(getVText());
		bPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		bPanel.add(this.getPlayPauseButton());
		bPanel.add(new JSeparator(SwingConstants.VERTICAL));
		bPanel.add(this.getFrameBackButton());
		bPanel.add(new JSeparator(SwingConstants.VERTICAL));
		bPanel.add(this.getFrameForwardButton());
		bPanel.add(new JSeparator(SwingConstants.VERTICAL));
		bPanel.add(previewButton);
		bPanel.setBackground(Color.GRAY);
		bPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		sPanel.setBackground(Color.GRAY);
		sPanel.setLayout(new BorderLayout());
		sPanel.add(bPanel, BorderLayout.CENTER);
		sPanel.setPreferredSize(new Dimension(324, 80));
		sPanel.setMinimumSize(new Dimension(324, 80));
		sPanel.setMaximumSize(new Dimension(324, 80));
		sPanel.add(nPanel, BorderLayout.NORTH);

		JPanel cPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		cPanel.setBackground(Color.WHITE);
		cPanel.add(sPanel);

		this.add(cPanel, BorderLayout.CENTER);
	}

	protected void addSlider()
	{// changed getDuration to getDurationMilliSec && getTimeValue to getTime
		setVSlide(new SignSlider(0, getVComponent().getVideoPanel().getDurationMilliSec(), getVComponent().getVideoPanel().getTime()));
		setMin(getVComponent().getVideoPanel().getMin()); // getting initial minutes for label
		setSec(getVComponent().getVideoPanel().getSec()); // getting initial seconds for label
		setMil(getVComponent().getVideoPanel().getMil()); // getting initial milliseconds for label
		getVText().setText(getMin() + ":" + getSec() + ":" + getMil()); // setting initial values in the textbox
		getVSlide().setPreferredSize(new Dimension(320, 38));
		getVSlide().setSize(new Dimension(320, 38));
		final SignSliderUI sUI = new SignSliderUI(getVSlide());
		getVSlide().setUI(sUI);
		getVSlide().setSUI(sUI);
		setSlideTimer(new Timer(50, getListen()));
		getVSlide().setLayout(new BorderLayout());
		if (endPosition == -1)
			tComp = new TimingComponent(320, 8, getVSlide(), position);
		else
			tComp = new TimingComponent(320, 8, getVSlide(), position, endPosition);
		getVSlide().add(tComp, BorderLayout.SOUTH);
		getVSlide().addChangeListener(
				new TPSliderListener(getVSlide(), ((TimingVideoListener) getListen()).getVideoComponent(), getVText()));
		getVSlide().setBackground(Color.WHITE);
		getVSlide().addMouseListener(
				new CompMouseListener(getVSlide(), ((TimingVideoListener) getListen()).getVideoComponent(), getVText()));
		getVSlide().addMouseMotionListener(
				new MMotionListener(getVSlide(), ((TimingVideoListener) getListen()).getVideoComponent(), getVText()));
		getVSlide().revalidate();
		final JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		north.setBackground(Color.WHITE);
		north.add(getVSlide());
		north.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		north.setPreferredSize(new Dimension(324, 42));
		// this panel is needed for the purposes of drawing vSlide correctly if border is used around vSlide it breaks
		JPanel northB = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		northB.setBackground(Color.WHITE);
		northB.add(north);
		this.add(northB, BorderLayout.NORTH);
	}

	protected TimingComponent getTimingComponent()
	{
		return tComp;
	}

	protected void clear()
	{
		position = -1;
		endPosition = -1;
	}

	protected void setStartPosition(int start)
	{
		this.position = start;
		tComp.setLeftRight(this.position, getVSlide().getCoordfromValue(getVSlide().getValueFromCoord(position) + TimingComponent.MINSIGN));
	}

	protected void setStartEndPositions(int start, int end)
	{
		this.position = start;
		this.endPosition = end;
		tComp.setLeftRight(start, end);
	}
}
