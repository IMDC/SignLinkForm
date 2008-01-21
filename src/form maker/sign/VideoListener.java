package sign;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * A listener class for the video manipulation buttons and menu items
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: VideoListener.java 86 2007-12-04 19:18:33Z martin $
 */
public class VideoListener implements ActionListener
{
	private VideoPanel			vPanel;
	private VideoButtonsPanel	vButtonsPanel;
	private VideoComponent		vComponent;
	private ArrayList<Sign>	signs;
	private MenuFrame			frame;
	private int					selected	= -1;

	/**
	 * A constructor that accepts a video component to listen for
	 * 
	 * @param aComponent -
	 *            the video component
	 */
	protected VideoListener(VideoComponent aComponent)
	{
		super();
		vComponent = aComponent;
	}

	/**
	 * method to perform the necessary actions
	 */
	public void actionPerformed(ActionEvent event)
	{
		vPanel = vComponent.getVideoPanel();
		vButtonsPanel = vComponent.getVideoButtonsPanel();
		frame = (MenuFrame) vComponent.getRootPane().getParent();
		if (event.getSource() == vButtonsPanel.getSlideTimer())// if the timer triggered the event
		{
			if (vPanel.isDone())
			{
				vPanel.stop();
				vButtonsPanel.getSlideTimer().stop();
				vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPlayIcon());
				return;
			}
			signs = frame.getSComp().getSignList();
			vButtonsPanel.getVSlide().setValue(vPanel.getTime());
			System.out.println(vPanel.getTime());
			vButtonsPanel.repaint();
		}
		else
		{
			if (event.getActionCommand().equals("edit video"))
			{
				// TODO fill in
			}

			if (event.getActionCommand().equals("play pause"))
			{
				if (vButtonsPanel.getPlayPauseIcon().equals(vButtonsPanel.getPlayIcon()))
				{
					vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPauseIcon());
					vPanel.repaint();
					vPanel.play();
					vButtonsPanel.getSlideTimer().start();
				}
				else if (vButtonsPanel.getPlayPauseIcon().equals(vButtonsPanel.getPauseIcon()))
				{
					if (selected > -1)
						signs.get(selected).setPreviewed(false);
					vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPlayIcon());
					vPanel.repaint();
					vPanel.stop();
					vButtonsPanel.getSlideTimer().stop();
				}
			}

			if (event.getActionCommand().equals("rewind"))
			{
				if (!vButtonsPanel.getSlideTimer().isRunning())
					vButtonsPanel.getSlideTimer().start();
				if (vPanel.getRate() != 1)
				{
					vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPauseIcon());
					vPanel.repaint();
				}
				vPanel.rewind();
			}

			if (event.getActionCommand().equals("fast forward"))
			{
				if (!vButtonsPanel.getSlideTimer().isRunning())
					vButtonsPanel.getSlideTimer().start();
				if (vPanel.getRate() != 1)
				{
					vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPauseIcon());
					vPanel.repaint();
				}
				vPanel.fastForward();
			}

			if (event.getActionCommand().equals("frame back"))
			{
				if (!vButtonsPanel.getSlideTimer().isRunning())
					vButtonsPanel.getSlideTimer().start();
				vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPlayIcon());
				vPanel.stepBackwards();
				vPanel.stop();
				vPanel.repaint();
			}

			if (event.getActionCommand().equals("frame forward"))
			{
				if (!vButtonsPanel.getSlideTimer().isRunning())
					vButtonsPanel.getSlideTimer().start();
				vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPlayIcon());
				vPanel.stepForward();
				vPanel.stop();
				vPanel.repaint();
			}
			if (event.getActionCommand().equals("help"))
			{
				if (frame.getHelp() == null)
				{
					frame.setHelp(new HelpFrame(HelpFrame.A6, frame.getHelpLocation()));
				}
				else
				{
					frame.getHelp().dispose();
					frame.setHelp(new HelpFrame(HelpFrame.A6, frame.getHelpLocation()));
				}
			}
		}
	}

	protected VideoComponent getVideoComponent()
	{
		return vComponent;
	}

	protected void setVideoComponent(VideoComponent vComp)
	{
		vComponent = vComp;
	}

}