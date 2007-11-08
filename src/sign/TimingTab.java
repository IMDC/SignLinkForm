package sign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TimingTab.java 52 2007-11-08 03:56:17Z martin $
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
			frame.setHelpFile(HelpFrame.A1);
			frame.invalidate();
			frame.validate();
		}
	}

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 4073898897581569578L;
	private final TimingPanelControlPanel	tPanel;
	private final JLabel					img					= new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
																		this.getClass().getResource("/icon_bar_lower.gif"))));
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
	 * Method to undo the changes made to the imageTab when the cancel button is pressed
	 */
//	protected void rollbackChanges()
//	{
//		tPanel.getTimingComponent().setLeftEnd(tPanel.getVSlide().getValueFromCoord(this.getTempStartPos()));
//		tPanel.getTimingComponent().setRightEnd(tPanel.getVSlide().getValueFromCoord(this.getTempEndPos()));
//		vPanel.setTimeMiliseconds(tPanel.getVSlide().getValueFromCoord(this.getTempStartPos()));
//		tPanel.getVSlide().setValueFromCoord(this.getTempStartPos());
//
//		System.out.println("RollBack - startPos:" + this.getTempStartPos());
//		System.out.println("RollBack - leftEnd:" + tPanel.getTimingComponent().getLeftEnd());
//	}
//
//	/**
//	 * Method to commit the changes made to the imageTab when the done button is pressed
//	 */
//	protected void commitChanges()
//	{
//		this.setTempStartPos(tPanel.getTimingComponent().getLeftEnd());
//		this.setTempEndPos(tPanel.getTimingComponent().getRightEnd());
//		vPanel.setTimeMiliseconds(tPanel.getVSlide().getValueFromCoord(this.getTempStartPos()));
//		tPanel.getVSlide().setValueFromCoord(this.getTempStartPos());
//		System.out.println("Commit - startPos:" + this.getTempStartPos());
//	}

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
		
	}
	
	protected void setStartPos(int start)
	{
		tPanel.setStartPosition(start);
//		tPanel.getVSlide().setValue(tPanel.getVComponent().getVideoPanel().getTime());
	}
	
	protected void setStartEndPos(int start, int end)
	{
		tPanel.setStartEndPositions(start, end);
//		tPanel.getVSlide().setValue(tPanel.getVComponent().getVideoPanel().getTime());
	}
}
