package signlinkform;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: SignSliderUI.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class SignSliderUI extends BasicSliderUI
{
	private SignSlider	slide;

	public SignSliderUI(JSlider slider)
	{
		super(slider);
		slide = (SignSlider) slider;
		thumbRect = slider.getBounds();
		thumbRect.width = 2;
	}

	protected void calculateThumbLocation()
	{
		int valuePosition = this.xPositionForValue(slide.getValue());
		thumbRect.x = valuePosition - (thumbRect.width / 2);
		thumbRect.y = trackRect.y;

	}

	protected Rectangle getTrackRect()
	{
		return trackRect;
	}

	protected int getTrackStart()
	{
		return (int) trackRect.getX();
	}

	protected int getTrackWidth()
	{
		return (int) trackRect.getWidth();
	}

	protected int getValueForX(int x)
	{
		return this.valueForXPosition(x);
	}

	protected int getXforValue(int value)
	{
		return this.xPositionForValue(value);
	}

	/**
	 * A method to paint the slider thumb
	 */
	public void paintThumb(Graphics g)
	{
		Rectangle thumb = thumbRect;
		g.setColor(Color.BLACK);
		g.fillPolygon(new int[] { thumb.x - 5, thumb.x + 15, thumb.x + 6, thumb.x + 6, thumb.x + 4, thumb.x + 4 }, new int[] {
				thumb.y - 10, thumb.y - 10, thumb.y, thumb.y + trackRect.height - 2, thumb.y + trackRect.height - 2, thumb.y }, 6);
	}

	/**
	 * A method to paint the slider track
	 */
	public void paintTrack(Graphics g)
	{
		Rectangle track = trackRect;
		g.setColor(Color.WHITE);
		g.fillRect(track.x - 10, track.y - 10, track.width + 15, track.height + 15);
		g.setColor(new Color(0.9f, 0.9f, 0.9f));
		g.fillRect(track.x, track.y, track.width - 1, track.height - 1);
		g.setColor(Color.BLACK);
		g.drawRect(track.x, track.y, track.width - 2, track.height - 2);
		g.drawRect(track.x - 1, track.y - 1, track.width, track.height);
		if (slide.getSigns().size() > 0)
		{
			g.setColor(Color.BLUE);
			for (int i = 0; i < slide.getSigns().size(); i++)
			{ // changed here to getcoordfromvalue
				g.fill3DRect(this.xPositionForValue(slide.getSigns().get(i).getMStart()), (int) getTrackRect().getY() + 1, this
						.xPositionForValue(slide.getSigns().get(i).getMEnd())
						- this.xPositionForValue(slide.getSigns().get(i).getMStart()), (int) getTrackRect().getHeight() - 3, false);
			}
		}
		if (slide.getActiveStart() != -1 && slide.getActiveEnd() != -1)
		{
			g.setColor(Color.RED);
			g.fill3DRect(slide.getActiveStart(), (int) getTrackRect().getY() + 1, slide.getActiveEnd() - slide.getActiveStart(),
					(int) getTrackRect().getHeight() - 3, false);
		}
	}

}
