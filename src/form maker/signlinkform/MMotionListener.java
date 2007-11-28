package signlinkform;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextField;
/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: MMotionListener.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class MMotionListener implements MouseMotionListener
{
	private SignSlider		vSlide;
	private VideoComponent	vComp;
	private String			min;
	private String			sec;
	private String			mil;
	private JTextField		vText;

	public MMotionListener(SignSlider slide, VideoComponent vComponent, JTextField text)
	{
		vSlide = slide;
		vText = text;
		vComp = vComponent;
	}

	public void mouseDragged(MouseEvent arg0)
	{
		if (vSlide.getValueIsAdjusting())
		{
			// changed setTime to setTimeMiliseconds
			vSlide.setValueFromCoord(arg0.getX());
			vComp.getVideoPanel().setTimeMiliseconds(vSlide.getValue());
			min = vComp.getVideoPanel().getMin();
			sec = vComp.getVideoPanel().getSec();
			mil = vComp.getVideoPanel().getMil();
			vText.setText(min + ":" + sec + ":" + mil);
		}

	}

	public void mouseMoved(MouseEvent arg0)
	{

	}

}
