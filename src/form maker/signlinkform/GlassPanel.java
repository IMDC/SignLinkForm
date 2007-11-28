package signlinkform;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: GlassPanel.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class GlassPanel extends JComponent
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -907673538147001547L;
	private int					startX;
	private int					startY;
	private int					endX;
	private int					endY;
	private int					width;
	private int					height;
	private final Rectangle		moveBox;

	public GlassPanel()
	{
		startX = 0;
		startY = 0;
		width = 160;
		height = 120;
		endX = startX + width;
		endY = startY + height;
		moveBox = new Rectangle(startX - 3, startY - 3, width + 6, height + 6);
	}
	public GlassPanel(final int x, final int y, final int w, final int h)
	{
		startX = x;
		startY = y;
		width = w;
		height = h;
		endX = x + w;
		endY = y + h;
		moveBox = new Rectangle(startX - 3, startY - 3, width + 6, height + 6);
	}

	protected int getEndX()
	{
		return endX;
	}

	protected int getEndY()
	{
		return endY;
	}

	protected Rectangle getMoveBox()
	{
		return moveBox;
	}

	protected int getStartX()
	{
		return startX;
	}

	protected int getStartY()
	{
		return startY;
	}

	protected int getViewHeight()
	{
		return height;
	}

	protected int getViewWidth()
	{
		return width;
	}

	protected void paintComponent(final Graphics g)
	{
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(moveBox.x, moveBox.y, moveBox.width, 3); // top line
		g2.fillRect(moveBox.x, moveBox.y, 3, moveBox.height); // left line
		g2.fillRect(startX + width, moveBox.y, 3, moveBox.height); // right line
		g2.fillRect(moveBox.x, startY + height, moveBox.width, 3); // bottom line
		g2.setColor(Color.BLACK);
		g2.drawRect(startX, startY, width, height); // the actual rectangle
		g2.drawRect(moveBox.x, moveBox.y, moveBox.width, moveBox.height); // the outside rectangle
	}

	protected void setEndX(final int x)
	{
		endX = x;
		width = endX - startX;
		moveBox.width = width + 6;
		repaint();
	}

	protected void setEndY(final int y)
	{
		endY = y;
		height = endY - startY;
		moveBox.height = height + 6;
		repaint();
	}

	protected void setStartX(final int x)
	{
		startX = x;
		moveBox.x = startX - 3;
		endX = startX + width;
		repaint();
	}

	protected void setStartY(final int y)
	{
		startY = y;
		moveBox.y = startY - 3;
		endY = startY + height;
		repaint();
	}

}
