package sign;

import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TPSliderListener.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class TPSliderListener implements ChangeListener
{
	private String				min;
	private String				sec;
	private String				mil;
	private JTextField			vText;
	private VideoComponent		vComp;
	private SignSlider			vSlide;
	private MenuFrame			frame;
	private ArrayList<Sign>	signs;
	private int					minTime;
	private int					maxTime;

	public TPSliderListener(SignSlider slide, VideoComponent vComponent, JTextField text)
	{
		vSlide = slide;
		vText = text;
		vComp = vComponent;
	}

	protected int getMaxTime()
	{
		return this.maxTime;
	}

	protected int getMinTime()
	{
		return this.minTime;
	}

	protected void setMaxTime(int maxTime)
	{
		this.maxTime = maxTime;
	}

	protected void setMinTime(int minTime)
	{
		this.minTime = minTime;
	}

	public void stateChanged(ChangeEvent e)
	{
		int minimum = -1; // get the signlink that's before the current time
		int maximum = -1; // get the signlink that's after the current time
		if (e != null && e.getSource().toString().contains("Main")) // if the slider from the main frame is calling the event
		{
			frame = (MenuFrame) vComp.getRootPane().getParent();
			signs = vSlide.getSigns();

			// room for improvement later
			if (frame.getSComp().getSelected() == -1) // if nothing is selected
			{
				if (Integer.parseInt(vComp.getVideoPanel().getSec()) >= 3)
				{ // activate buttons after the 3rd second
					/*
					 * get the minimum and maximum values that the new signlink can obtain
					 */
					for (int i = 0; i < vSlide.getSigns().size(); i++)
					{
						if (vSlide.getSigns().get(i).getMStart() < vSlide.getValue())
							minimum = i;
					}
					if (vSlide.getSigns().size() > minimum + 1)
						maximum = minimum + 1;
					if (minimum > -1)
						minTime = vSlide.getSigns().get(minimum).getMEnd() + ((int) (TimingComponent.MINSIGN));
					else
						minTime = -1;
					if (maximum > -1)
						maxTime = vSlide.getSigns().get(maximum).getMStart() - (int) (TimingComponent.MINSIGN );
					else
						maxTime = -1;
					// select the active and enable buttons for manipulating it
					for (int i = 0; i < signs.size(); i++)
					{
						if (vSlide.getValue() >= signs.get(i).getMStart() && vSlide.getValue() <= signs.get(i).getMEnd())
						{
							frame.getSComp().select(i);
							frame.getSComp().getSButtonsPanel().getPreviewButton().setEnabled(true);
							frame.getSComp().getSButtonsPanel().getEditPropertiesButton().setEnabled(true);
							frame.getSComp().getSButtonsPanel().getDeleteButton().setEnabled(true);
							frame.getSignLinks().getMenuComponent(1).setEnabled(true);
							frame.getSignLinks().getMenuComponent(2).setEnabled(true);
							break;
						}
					}
					System.out.println("MinTime: " + minTime);
					System.out.println("MaxTime: " + maxTime);
					System.out.println("SlideValue: " + vSlide.getValue());
					// Activate buttons according to the allowed min and max times
					if (minTime > -1 && maxTime > -1 && vSlide.getValue() > minTime && vSlide.getValue() < maxTime)
					{
						frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(true);
						frame.getSignLinks().getMenuComponent(0).setEnabled(true);
					}
					else if (minTime == -1 && maxTime > -1 && vSlide.getValue() < maxTime)
					{
						frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(true);
						frame.getSignLinks().getMenuComponent(0).setEnabled(true);
					}
					else if (maxTime == -1 && vSlide.getValue() > minTime)
					{
						frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(true);
						frame.getSignLinks().getMenuComponent(0).setEnabled(true);
					}
					else if (maxTime == -1 && minTime == -1)
					{
						frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(true);
						frame.getSignLinks().getMenuComponent(0).setEnabled(true);
					}
					else
					{
						frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(false);
						frame.getSignLinks().getMenuComponent(0).setEnabled(false);
					}
				}
				else
				{
					frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(false);
					frame.getSignLinks().getMenuComponent(0).setEnabled(false);
				}
			}
			else
			// if there is something selected
			{
				if (vSlide.getValue() > signs.get(frame.getSComp().getSelected()).getMEnd()
						|| vSlide.getValue() < signs.get(frame.getSComp().getSelected()).getMStart())
				{
					int selected = frame.getSComp().getSelected();
					if (signs.get(selected).isPreviewed()
							&& vComp.getVideoButtonsPanel().getPlayPauseButton().getIcon().equals(vComp.getVideoButtonsPanel().getPauseIcon()))
					// if the preview button is clicked to go from the beginning of the sign to the end
					{
						vComp.getVideoButtonsPanel().getPlayPauseButton().doClick();
						vSlide.setValue(signs.get(selected).getMEnd());
						vComp.getVideoPanel().setTime(vSlide.getValue());
					}
					signs.get(selected).setPreviewed(false);
					frame.getSComp().deSelect();
					frame.getSComp().getSButtonsPanel().getPreviewButton().setEnabled(false);
					frame.getSComp().getSButtonsPanel().getEditPropertiesButton().setEnabled(false);
					frame.getSComp().getSButtonsPanel().getDeleteButton().setEnabled(false);
					frame.getSignLinks().getMenuComponent(1).setEnabled(false);
					frame.getSignLinks().getMenuComponent(2).setEnabled(false);
				}
			}
		}
		// set new values for min, sec, mil for the textbox
		min = vComp.getVideoPanel().getMin();
		sec = vComp.getVideoPanel().getSec();
		mil = vComp.getVideoPanel().getMil();
		vText.setText(min + ":" + sec + ":" + mil);
		vSlide.repaint();
	}
}
