package signlinkform;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;
/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: CompMouseListener.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class CompMouseListener implements MouseListener
{
	private SignSlider		vSlide;
	private VideoComponent	vComp;
	private String			min;
	private String			sec;
	private String			mil;
	private JTextField		vText;

	public CompMouseListener(SignSlider slide, VideoComponent vComponent, JTextField vTextF)
	{
		vSlide = slide;
		vText = vTextF;
		vComp = vComponent;
	}

	public void mouseClicked(MouseEvent arg0)
	{
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mousePressed(MouseEvent arg0)
	{
		vSlide.setValueFromCoord(arg0.getX());
		// changed from setTime to setTimeMiliseconds
		vComp.getVideoPanel().setTimeMiliseconds(vSlide.getValue());
		min = vComp.getVideoPanel().getMin();
		sec = vComp.getVideoPanel().getSec();
		mil = vComp.getVideoPanel().getMil();
		vSlide.setValueIsAdjusting(true);
		vText.setText(min + ":" + sec + ":" + mil);

	}

	public void mouseReleased(MouseEvent arg0)
	{
		vSlide.setValueIsAdjusting(false);
		vSlide.setValueFromCoord(arg0.getX());
		// changed from setTime to setTimeMiliseconds
		vComp.getVideoPanel().setTimeMiliseconds(vSlide.getValue());
		min = vComp.getVideoPanel().getMin();
		sec = vComp.getVideoPanel().getSec();
		mil = vComp.getVideoPanel().getMil();
		vText.setText(min + ":" + sec + ":" + mil);
	}
}
