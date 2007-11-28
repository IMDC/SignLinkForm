package signlinkform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TimingVideoListener.java 67 2007-11-22 18:31:28Z martin $
 *
 */
public class TimingVideoListener implements ActionListener
{
	private VideoPanel				vPanel;
	private VideoComponent			vComponent;
	private TimingPanelControlPanel	tControlPanel;
	private SignSlider				slider;
	private boolean					preview	= false;

	/**
	 * A constructor that accepts a video component to listen for
	 * 
	 * @param aComponent -
	 *            the video component
	 */
	protected TimingVideoListener(VideoComponent aComponent)
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
		tControlPanel = (TimingPanelControlPanel) vComponent.getVideoButtonsPanel();
		slider = tControlPanel.getVSlide();
		if (event.getSource() == tControlPanel.getSlideTimer())
		{
			if (vPanel.isDone())
			{
				vPanel.stop();
				tControlPanel.getSlideTimer().stop();
				tControlPanel.setPlayPauseIcon(tControlPanel.getPlayIcon());
				return;
			}
			if (preview)
			// if the preview button is clicked to go from the beginning of the signlink to the end
			{
				if (slider.getValue() >= slider.getValueFromCoord(tControlPanel.getTimingComponent().getRightEnd()))
				{
					tControlPanel.getPlayPauseButton().doClick();
					slider.setValueFromCoord(tControlPanel.getTimingComponent().getRightEnd());
					// changed setTime to setTimeMil
					vPanel.setTimeMiliseconds(slider.getValue());
				}
			}
			slider.setValue(vPanel.getTime());
			System.out.println(vPanel.getTime());
			tControlPanel.repaint();

		}
		else
		{
			if (event.getActionCommand().equals("edit video"))
			{
				// TODO fill in
			}

			if (event.getActionCommand().equals("play pause"))
			{
				if (tControlPanel.getPlayPauseIcon().equals(tControlPanel.getPlayIcon()))
				{
					tControlPanel.setPlayPauseIcon(tControlPanel.getPauseIcon());
					vPanel.repaint();
					vPanel.play();
					tControlPanel.getSlideTimer().start();
				}
				else if (tControlPanel.getPlayPauseIcon().equals(tControlPanel.getPauseIcon()))
				{
					preview = false; // set the preview mode to false
					tControlPanel.setPlayPauseIcon(tControlPanel.getPlayIcon());
					vPanel.repaint();
					vPanel.stop();
					tControlPanel.getSlideTimer().stop();
				}
			}

			if (event.getActionCommand().equals("rewind"))
			{
				if (!tControlPanel.getSlideTimer().isRunning())
					tControlPanel.getSlideTimer().start();
				if (vPanel.getRate() != 1)
				{
					tControlPanel.setPlayPauseIcon(tControlPanel.getPauseIcon());
					vPanel.repaint();
				}
				vPanel.rewind();
			}

			if (event.getActionCommand().equals("fast forward"))
			{
				if (!tControlPanel.getSlideTimer().isRunning())
					tControlPanel.getSlideTimer().start();
				if (vPanel.getRate() != 1)
				{
					tControlPanel.setPlayPauseIcon(tControlPanel.getPauseIcon());
					vPanel.repaint();
				}
				vPanel.fastForward();
			}

			if (event.getActionCommand().equals("frame back"))
			{
				if (!tControlPanel.getSlideTimer().isRunning())
					tControlPanel.getSlideTimer().start();
				tControlPanel.setPlayPauseIcon(tControlPanel.getPlayIcon());
				vPanel.stepBackwards();
				vPanel.stop();
				vPanel.repaint();
			}

			if (event.getActionCommand().equals("frame forward"))
			{
				if (!tControlPanel.getSlideTimer().isRunning())
					tControlPanel.getSlideTimer().start();
				tControlPanel.setPlayPauseIcon(tControlPanel.getPlayIcon());
				vPanel.stepForward();
				vPanel.stop();
				vPanel.repaint();
			}

			if (event.getActionCommand().equals("beginning"))
			{
				slider.setValue(tControlPanel.getTimingComponent().getStartTime());
				vPanel.setTimeMiliseconds(slider.getValue());
				String min = vPanel.getMin();
				String sec = vPanel.getSec();
				String mil = vPanel.getMil();
				tControlPanel.getVText().setText(min + ":" + sec + ":" + mil);
			}

			if (event.getActionCommand().equals("ending"))
			{
				slider.setValue(tControlPanel.getTimingComponent().getEndTime());
				vPanel.setTimeMiliseconds(slider.getValue());
				String min = vPanel.getMin();
				String sec = vPanel.getSec();
				String mil = vPanel.getMil();
				tControlPanel.getVText().setText(min + ":" + sec + ":" + mil);
			}

			if (event.getActionCommand().equals("beginning frame back"))
			{
				tControlPanel.getTimingComponent().initializeMinMax();
				slider.setValue(tControlPanel.getTimingComponent().getStartTime());
				vPanel.setTimeMiliseconds(slider.getValue());
				if (tControlPanel.getTimingComponent().getMinTime() == -1)
				{
					if (vPanel.getTime() > TimingComponent.MINTIME + 100)
					{
						vPanel.stepBackwards();
					}
					else
					{
						vPanel.setTimeMiliseconds(TimingComponent.MINTIME);
					}
				}
				else
				{
					if (tControlPanel.getTimingComponent().getMinTime() + 100 <= slider.getValue())
					{
						vPanel.stepBackwards();
					}
					else
					{
						vPanel.setTimeMiliseconds(tControlPanel.getTimingComponent().getMinTime());
					}
				}
				slider.setValue(vPanel.getTime());
				tControlPanel.getTimingComponent().setLeftEnd(slider.getValue());
			}

			if (event.getActionCommand().equals("beginning frame forward"))
			{
				tControlPanel.getTimingComponent().initializeMinMax();
				slider.setValue(tControlPanel.getTimingComponent().getStartTime());
				vPanel.setTimeMiliseconds(slider.getValue());

				if (tControlPanel.getTimingComponent().getEndTime() - vPanel.getTime() > TimingComponent.MINSIGN + 100)
				{
					vPanel.stepForward();
				}
				else
				{
					vPanel.setTimeMiliseconds(tControlPanel.getTimingComponent().getEndTime() - TimingComponent.MINSIGN);
				}

				slider.setValue(vPanel.getTime());
				tControlPanel.getTimingComponent().setLeftEnd(slider.getValue());
			}

			if (event.getActionCommand().equals("ending frame back"))
			{
				tControlPanel.getTimingComponent().initializeMinMax();
				slider.setValue(tControlPanel.getTimingComponent().getEndTime());
				vPanel.setTimeMiliseconds(slider.getValue());

				if (vPanel.getTime() - tControlPanel.getTimingComponent().getStartTime() > TimingComponent.MINSIGN + 100)
				{
					vPanel.stepBackwards();
				}
				else
				{
					vPanel.setTimeMiliseconds(tControlPanel.getTimingComponent().getStartTime() + TimingComponent.MINSIGN);
				}

				slider.setValue(vPanel.getTime());
				tControlPanel.getTimingComponent().setRightEnd(slider.getValue());
			}

			if (event.getActionCommand().equals("ending frame forward"))
			{
				tControlPanel.getTimingComponent().initializeMinMax();
				slider.setValue(tControlPanel.getTimingComponent().getEndTime());
				vPanel.setTimeMiliseconds(slider.getValue());
				System.out.println("maxtime:" + tControlPanel.getTimingComponent().getMaxTime());

				if (tControlPanel.getTimingComponent().getMaxTime() == -1)
				{
					vPanel.stepForward();
				}
				else
				{
					if (tControlPanel.getTimingComponent().getMaxTime() - 100 >= slider.getValue())
					{
						vPanel.stepForward();
					}
					else
					{
						vPanel.setTimeMiliseconds(tControlPanel.getTimingComponent().getMaxTime());
					}
				}

				slider.setValue(vPanel.getTime());
				tControlPanel.getTimingComponent().setRightEnd(slider.getValue());
			}

			if (event.getActionCommand().equals("preview"))
			{
				slider.setValue(tControlPanel.getTimingComponent().getStartTime());
				vPanel.setTimeMiliseconds(slider.getValue());
				preview = true;
				if (tControlPanel.getPlayPauseIcon() == tControlPanel.getPlayIcon())
					tControlPanel.getPlayPauseButton().doClick();
			}
		}
	}

	protected VideoComponent getVideoComponent()
	{
		return vComponent;
	}
}
