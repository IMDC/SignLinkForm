package sign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TimingComponent.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class TimingComponent extends JPanel
{
	class MListener implements MouseListener, MouseMotionListener
	{
		public void mouseClicked(final MouseEvent arg0)
		{
			System.out.println("mouseX:" + arg0.getX());
		}

		public void mouseDragged(final MouseEvent arg0)
		{
			System.out.println("minTime:" + getMinTime() + " maxTime:" + getMaxTime());
			System.out.println("startTime:" + getStartTime() + " endTime:" + getEndTime());

			if (leftAdjusting)
			{
				System.out.println("leftEnd:" + getLeftEnd() + " mouseX:" + arg0.getX());
				if (getMinTime() == -1)
				{
					if (slide.getValueFromCoord(arg0.getX() + height / 2) < MINTIME)
					{
						setLeftEnd(MINTIME);
						System.out.println("left moving: <min allowed start time");
					}
					else if (slide.getValueFromCoord(arg0.getX() + height / 2) >= MINTIME
							&& slide.getValueFromCoord(arg0.getX() + height / 2) < getEndTime() - MINSIGN)
					{
						setLeftEnd(slide.getValueFromCoord(arg0.getX() + height / 2));
						System.out.println("left moving: within allowed range");
					}
					else if (slide.getValueFromCoord(arg0.getX() + height / 2) >= getEndTime() - MINSIGN)
					{
						setLeftEnd(getEndTime() - MINSIGN);
						System.out.println("left moving: <=min allowed length of sign");
					}
				}
				else if (getMinTime() > -1)
				{
					if (slide.getValueFromCoord(arg0.getX() + height / 2) < getMinTime())
					{
						setLeftEnd(getMinTime());
						System.out.println("left moving: <min allowed start time");
					}
					else if (slide.getValueFromCoord(arg0.getX() + height / 2) >= getMinTime()
							&& slide.getValueFromCoord(arg0.getX() + height / 2) < getEndTime() - MINSIGN)
					{
						setLeftEnd(slide.getValueFromCoord(arg0.getX() + height / 2));
						System.out.println("left moving: within allowed range");
					}
					else if (slide.getValueFromCoord(arg0.getX() + height / 2) >= getEndTime() - MINSIGN)
					{
						setLeftEnd(getEndTime() - MINSIGN);
						System.out.println("left moving: <=min allowed length of sign");
					}
				}
			}
			else if (rightAdjusting)
			{
				System.out.println("rightEnd:" + getRightEnd() + " mouseX:" + arg0.getX());
				if (getMaxTime() == -1)
				{
					if (slide.getValueFromCoord(arg0.getX() - height / 2) <= getStartTime() + MINSIGN)
					{
						setRightEnd(getStartTime() + MINSIGN);
						System.out.println("right moving: <=min allowed length of sign");
					}
					else if (slide.getValueFromCoord(arg0.getX() - height / 2) > getStartTime() + MINSIGN)
					{
						setRightEnd(slide.getValueFromCoord(arg0.getX() - height / 2));
						System.out.println("right moving: within allowed range");
					}
				}
				else if (getMaxTime() > -1)
				{
					if (slide.getValueFromCoord(arg0.getX() - height / 2) <= getStartTime() + MINSIGN)
					{
						setRightEnd(getStartTime() + MINSIGN);
						System.out.println("right moving: <=min allowed length of sign");
					}
					else if (slide.getValueFromCoord(arg0.getX() - height / 2) > getStartTime() + MINSIGN
							&& slide.getValueFromCoord(arg0.getX() - height / 2) <= getMaxTime())
					{
						setRightEnd(slide.getValueFromCoord(arg0.getX() - height / 2));
						System.out.println("right moving: within allowed range");
					}
					else if (slide.getValueFromCoord(arg0.getX() - height / 2) > getMaxTime())
					{
						setRightEnd(getMaxTime());
						System.out.println("right moving: >max allowed end time");
					}
				}
			}
		}

		public void mouseEntered(final MouseEvent arg0)
		{

		}

		public void mouseExited(final MouseEvent arg0)
		{

		}

		public void mouseMoved(final MouseEvent arg0)
		{
			tPanel = (TimingPanelControlPanel) slide.getParent().getParent().getParent();
		}

		public void mousePressed(final MouseEvent arg0)
		{

			if (arg0.getX() <= leftEnd && arg0.getX() >= leftEnd - height)
			{
				leftAdjusting = true;
			}

			if (arg0.getX() <= rightEnd + height && arg0.getX() >= rightEnd)
			{
				rightAdjusting = true;
			}

			TimingComponent.this.initializeMinMax();
		}

		public void mouseReleased(final MouseEvent arg0)
		{
			leftAdjusting = false;
			rightAdjusting = false;
		}
	}

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 4571223105971195820L;
	public static final int			OFFSET				= 2;					// Due to the border around the slider
	public static final int			MINTIME				= 3000;				// minimum time from start of movie to first sign
	public static final int			MINSIGN				= 500;					// minimum time between signlinks
	private final int				height;
	private final int				width;
	private int						leftEnd;
	private int						rightEnd;
	private final SignSlider		slide;
	private boolean					leftAdjusting;
	private boolean					rightAdjusting;
	private TimingPanelControlPanel	tPanel;
	private int						minTime				= -1;
	private int						maxTime				= -1;

	public TimingComponent(final int w, final int h, final SignSlider vSlide, final int pos)
	{
		super();
		width = w;
		height = h;
		slide = vSlide;
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		leftEnd = pos;
		slide.setActiveStart(leftEnd);
		rightEnd = slide.getCoordfromValue(slide.getValueFromCoord(pos) + MINSIGN);
		slide.setActiveEnd(rightEnd);
		this.setBackground(Color.WHITE);
		leftAdjusting = false;
		rightAdjusting = false;
		final MListener mListen = new MListener();
		this.addMouseListener(mListen);
		this.addMouseMotionListener(mListen);
	}

	public TimingComponent(final int w, final int h, final SignSlider vSlide, final int startPos, final int endPos)
	{
		super();
		width = w;
		height = h;
		slide = vSlide;
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		leftEnd = startPos;
		slide.setActiveStart(leftEnd);
		rightEnd = endPos;
		slide.setActiveEnd(rightEnd);
		this.setBackground(Color.WHITE);
		leftAdjusting = false;
		rightAdjusting = false;
		final MListener mListen = new MListener();
		this.addMouseListener(mListen);
		this.addMouseMotionListener(mListen);
	}

	protected void initializeMinMax()
	{
		int min = -1;
		int max = -1;
		SignLink current = (SignLink) this.getRootPane().getParent();
		if (current.isEdited())
		{
			min = slide.getSigns().indexOf(current) - 1;
			if (slide.getSigns().size() > min + 2)
				max = min + 2;
			else
				max = -1;
		}
		else
		{
			for (int i = 0; i < slide.getSigns().size(); i++)
			{
				if (slide.getSigns().get(i).getMStart() < this.getStartTime())
					min = i;
			}
			if (slide.getSigns().size() > min + 1)
				max = min + 1;
			else
				max = -1;
		}
		if (min > -1)
			setMinTime(slide.getSigns().get(min).getMEnd() + MINSIGN);
		else
			setMinTime(-1);
		if (max > -1)
			setMaxTime(slide.getSigns().get(max).getMStart() - MINSIGN);
		else
			setMaxTime(-1);
	}

	protected int getEndTime()
	{
		return slide.getValueFromCoord(rightEnd);
	}

	protected int getLeftEnd()
	{
		return leftEnd;
	}

	protected int getMaxTime()
	{
		return maxTime;
	}

	protected int getMinTime()
	{
		return minTime;
	}

	protected int getRightEnd()
	{
		return rightEnd;
	}

	protected int getStartTime()
	{
		return slide.getValueFromCoord(leftEnd);
	}

	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.GREEN);
		g.fillPolygon(new int[] { leftEnd, leftEnd, leftEnd - height }, new int[] { 0, height, height }, 3);
		g.setColor(Color.BLACK);
		g.drawPolygon(new int[] { leftEnd, leftEnd, leftEnd - height }, new int[] { 0, height, height }, 3);
		g.setColor(Color.RED);
		g.fillPolygon(new int[] { rightEnd, rightEnd, rightEnd + height }, new int[] { 0, height, height }, 3);
		g.setColor(Color.BLACK);
		g.drawPolygon(new int[] { rightEnd, rightEnd, rightEnd + height }, new int[] { 0, height, height }, 3);
	}

	protected void setLeftEnd(final int left)
	{

		int temp = slide.getCoordfromValue(left);
		if (temp < rightEnd && temp > slide.getTrackStart())
		{
			leftEnd = temp;
			slide.setValue(left);
			tPanel = (TimingPanelControlPanel) slide.getParent().getParent().getParent();
			tPanel.getVComponent().getVideoPanel().setTimeMiliseconds(left);
			slide.setActiveStart(leftEnd);
		}
	}

	protected void setMaxTime(final int maxTime)
	{
		this.maxTime = maxTime;
	}

	protected void setMinTime(final int minTime)
	{
		this.minTime = minTime;
	}

	protected void setRightEnd(final int right)
	{
		int temp = slide.getCoordfromValue(right);
		if (temp > leftEnd && temp < (slide.getTrackStart() + slide.getTrackWidth()))
		{
			rightEnd = temp;
			slide.setValue(right);
			tPanel = (TimingPanelControlPanel) slide.getParent().getParent().getParent();
			tPanel.getVComponent().getVideoPanel().setTimeMiliseconds(right);
			slide.setActiveEnd(rightEnd);
		}
	}
	
	protected void setLeftRight(final int left, final int right)
	{
		leftEnd = left;
		rightEnd = right;
		slide.setValue(left);
		tPanel = (TimingPanelControlPanel) slide.getParent().getParent().getParent();
		tPanel.getVComponent().getVideoPanel().setTimeMiliseconds(left);
		slide.setActiveStart(leftEnd);
		slide.setActiveEnd(rightEnd);
	}
}
