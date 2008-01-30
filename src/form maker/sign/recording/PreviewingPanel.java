package sign.recording;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JPanel;

import sign.HelpFrame;
import sign.VideoButtonsPanel;
import sign.VideoComponent;
import sign.VideoPanel;

/**
 * @author Martin Gerdzhev
 * @version $Id: PreviewingPanel.java 118 2008-01-29 17:56:25Z martin $
 */
public class PreviewingPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 7611980184226677110L;
	private static PreviewingPanel	instance;
	private File					recordedFile;
	private boolean					initialized;
	private VideoComponent			vComponent;

	public static PreviewingPanel getInstance(File file)
	{
		if (instance == null)
			instance = new PreviewingPanel(file);
		return instance;
	}

	private PreviewingPanel(File file)
	{
		recordedFile = file;
		initialized = false;
		this.addComponentListener(new CompListener());
	}

	private class CompListener implements ComponentListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
		 */
		public void componentHidden(ComponentEvent e)
		{
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
		 */
		public void componentMoved(ComponentEvent e)
		{
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
		 */
		public void componentResized(ComponentEvent e)
		{
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
		 */
		public void componentShown(ComponentEvent e)
		{
			if (!initialized)
			{
				//FIXME The vertical spacing is too big
				vComponent = new VideoComponent(recordedFile, new PreviewListener());
				instance.add(vComponent);
				initialized = true;
			}

		}

	}

	private class PreviewListener implements ActionListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event)
		{
			VideoPanel vPanel = vComponent.getVideoPanel();
			VideoButtonsPanel vButtonsPanel = vComponent.getVideoButtonsPanel();
			if (event.getSource() == vButtonsPanel.getSlideTimer())// if the timer triggered the event
			{
				if (vPanel.isDone())
				{
					vPanel.stop();
					vButtonsPanel.getSlideTimer().stop();
					vButtonsPanel.setPlayPauseIcon(vButtonsPanel.getPlayIcon());
					return;
				}
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
					//TODO add some help here
//					if (frame.getHelp() == null)
//					{
//						frame.setHelp(new HelpFrame(HelpFrame.A6, frame.getHelpLocation()));
//					}
//					else
//					{
//						frame.getHelp().dispose();
//						frame.setHelp(new HelpFrame(HelpFrame.A6, frame.getHelpLocation()));
//					}
				}
			}

		}

	}

	/**
	 * Getter method to return the value of initialized
	 * 
	 * @return the initialized
	 */
	public boolean isInitialized()
	{
		return this.initialized;
	}

	/**
	 * Setter method to set the value of initialized
	 * 
	 * @param initialized
	 *            the value to set
	 */
	public void setInitialized(boolean initialized)
	{
		this.initialized = initialized;
	}
}
