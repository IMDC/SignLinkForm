/**
 * 
 */
package sign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: ImageComponent.java 52 2007-11-08 03:56:17Z martin $
 */
public class ImageComponent extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8748584304199515978L;
	private static final int	IMAGEWIDTH			= 12;
	private static final int	IMAGEHEIGHT			= 12;
	private final ImageIcon		cameraIcon			= new ImageIcon(Toolkit.getDefaultToolkit().getImage(
															this.getClass().getResource("/sSetFrame.jpg")));
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
		g.drawImage(cameraIcon.getImage(), position, 5, IMAGEWIDTH, IMAGEHEIGHT, this);
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
	
//	protected void commitChanges()
//	{
//		this.setTempPosition(this.getPosition());
//		this.setTempValue(this.getValue());
//	}
//	
//	protected void rollbackChanges()
//	{
//		position = this.getTempPosition();
//		this.setValue(this.getTempValue());
//	}

	/**
	 * Getter method to return the value of tempValue
	 * @return the tempValue
	 */
//	public int getTempValue()
//	{
//		return this.tempValue;
//	}

	/**
	 * Setter method to set the value of tempValue
	 * @param tempValue the value to set
	 */
//	public void setTempValue(int tempValue)
//	{
//		this.tempValue = tempValue;
//	}

	/**
	 * Getter method to return the value of tempPosition
	 * @return the tempPosition
	 */
//	public int getTempPosition()
//	{
//		return this.tempPosition;
//	}

	/**
	 * Setter method to set the value of tempPosition
	 * @param tempPosition the value to set
	 */
//	public void setTempPosition(int tempPosition)
//	{
//		this.tempPosition = tempPosition;
//	}

}
