package sign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TimingTab.java 94 2007-12-18 21:31:47Z martin $
 *
 */
public class TimingTab extends JPanel
{
	private class HelpListener implements ComponentListener
	{
		private SignLink	frame;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
		 */
		public void componentHidden(final ComponentEvent arg0)
		{
			frame = (SignLink) TimingTab.this.getRootPane().getParent();
			if (frame.getHelp() != null)
			{
				frame.getHelp().dispose();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
		 */
		public void componentMoved(final ComponentEvent arg0)
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
		 */
		public void componentResized(final ComponentEvent arg0)
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
		 */
		public void componentShown(final ComponentEvent arg0)
		{
			frame = (SignLink) TimingTab.this.getRootPane().getParent();
			frame.setHelpFile(HelpFrame.A5);
			frame.invalidate();
			frame.validate();
		}
	}

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 4073898897581569578L;
	private SignlinkIcons images = SignlinkIcons.getInstance();
	private final TimingPanelControlPanel	tPanel;
	private final JLabel					img					= new JLabel(images.iconBarLowerIcon);
	private final TimingVideoListener		vListen;
	private final VideoComponent			vComponent;
	private final VideoPanel				vPanel;
	private int								tempStartPos;
	private int								tempEndPos;

	protected TimingTab(final VideoComponent vComp, final int time, final int pos)
	{
		vComponent = vComp;
		vPanel = vComponent.getVideoPanel();
		vPanel.setTimeMiliseconds(time);
		vListen = new TimingVideoListener(vComponent);
		tPanel = new TimingPanelControlPanel(vListen, pos);
		vComponent.setTimingControlPanel(tPanel);
		vPanel.setBackground(Color.WHITE);
		final JPanel north = new JPanel();
		north.setLayout(new BorderLayout());
		north.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
		north.add(vPanel, BorderLayout.CENTER);
		north.add(img, BorderLayout.SOUTH);
		north.setBackground(Color.WHITE);
		tPanel.setBackground(Color.WHITE);
		this.addComponentListener(new HelpListener());
		this.setLayout(new BorderLayout());
		this.add(north, BorderLayout.NORTH);
		this.add(tPanel, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
	}

	protected TimingTab(final VideoComponent vComp, final int time, final int startPos, final int endPos)
	{
		vComponent = vComp;
		vPanel = vComponent.getVideoPanel();
		vPanel.setTimeMiliseconds(time);
		vListen = new TimingVideoListener(vComponent);
		tPanel = new TimingPanelControlPanel(vListen, startPos, endPos);
		vComponent.setTimingControlPanel(tPanel);
		vPanel.setBackground(Color.WHITE);
		final JPanel north = new JPanel();
		north.setLayout(new BorderLayout());
		north.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
		north.add(vPanel, BorderLayout.CENTER);
		north.add(img, BorderLayout.SOUTH);
		north.setBackground(Color.WHITE);
		tPanel.setBackground(Color.WHITE);
		this.addComponentListener(new HelpListener());
		this.setLayout(new BorderLayout());
		this.add(north, BorderLayout.NORTH);
		this.add(tPanel, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
	}

	protected TimingPanelControlPanel getTPanel()
	{
		return tPanel;
	}


	/**
	 * Getter method to return the value of tempStartPos
	 * 
	 * @return the tempStartPos
	 */
	protected int getTempStartPos()
	{
		return this.tempStartPos;
	}

	/**
	 * Setter method to set the value of tempStartPos
	 * 
	 * @param tempStartPos
	 *            the value to set
	 */
	protected void setTempStartPos(int tempStartPos)
	{
		this.tempStartPos = tempStartPos;
	}

	/**
	 * Getter method to return the value of tempEndPos
	 * 
	 * @return the tempEndPos
	 */
	protected int getTempEndPos()
	{
		return this.tempEndPos;
	}

	/**
	 * Setter method to set the value of tempEndPos
	 * 
	 * @param tempEndPos
	 *            the value to set
	 */
	protected void setTempEndPos(int tempEndPos)
	{
		this.tempEndPos = tempEndPos;
	}
	
	/**
	 * method to clear all the fields in the panel
	 */
	protected void clear()
	{
		tPanel.clear();
	}
	
	protected void setTime(int time)
	{
		tPanel.getVSlide().setValue(time);
		vPanel.setTimeMiliseconds(time);
		tPanel.setMin(tPanel.getVComponent().getVideoPanel().getMin()); // getting initial minutes for label
		tPanel.setSec(tPanel.getVComponent().getVideoPanel().getSec()); // getting initial seconds for label
		tPanel.setMil(tPanel.getVComponent().getVideoPanel().getMil()); // getting initial milliseconds for label
		tPanel.getVText().setText(tPanel.getMin() + ":" + tPanel.getSec() + ":" + tPanel.getMil()); // setting initial values in the textbox
		
	}
	
	protected void setStartPos(int start)
	{
		tPanel.setStartPosition(start);
	}
	
	protected void setStartEndPos(int start, int end)
	{
		tPanel.setStartEndPositions(start, end);
	}
}
