/**
 * 
 */
package sign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: ImageComponent.java 92 2007-12-18 18:48:20Z laurel $
 */
public class ImageComponent extends JPanel
{

	private SignlinkIcons images = SignlinkIcons.getInstance();
	
	private static final long	serialVersionUID	= -8748584304199515978L;
	private static final int	IMAGEWIDTH			= 12;
	private static final int	IMAGEHEIGHT			= 12;
	private int					position;
	private int					value;
	/**
	 * Default constructor that sets the position of the camera on the slider to the beginning of the slider
	 */
	public ImageComponent(final int w, final int h, final int pos)
	{
		this.setPreferredSize(new Dimension(w, h));
		this.setSize(w, h);
		this.setBackground(Color.WHITE);
		position = pos - IMAGEWIDTH / 2;
	}

	/**
	 * Getter method to return the position of the camera on the slider
	 * 
	 * @return the position
	 */
	public int getPosition()
	{
		return this.position;
	}

	protected int getValue()
	{
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g)
	{
		super.paintComponent(g);
		g.fillPolygon(new int[] { position, position + IMAGEWIDTH / 2, position + IMAGEWIDTH }, new int[] { 5, 0, 5 }, 3);
		g.drawImage(images.setFrameIcon.getImage(), position, 5, IMAGEWIDTH, IMAGEHEIGHT, this);
	}

	/**
	 * Setter method to set the position of the camera on the slider
	 * 
	 * @param position
	 *            the position to set
	 */
	public void setPosition(final int pos)
	{
		this.position = pos - IMAGEWIDTH / 2;
		this.repaint();
	}

	protected void setValue(int value)
	{
		this.value = value;
	}
	
}
