/**
 * 
 */
package signlinkform;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: JMultilineLabel.java 52 2007-11-08 03:56:17Z martin $
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JComponent;

public class JMultilineLabel extends JComponent
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -6020328045043350252L;
	private String					text;
	private final Insets			margin				= new Insets(5, 5, 5, 5);
	private int						maxWidth			= Integer.MAX_VALUE;
	private boolean					justify;
	private final FontRenderContext	frc					= new FontRenderContext(null, false, false);

	public int getMaxWidth()
	{
		return maxWidth;
	}

	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	public Dimension getPreferredSize()
	{
		return paintOrGetSize(null, getMaxWidth());
	}

	public String getText()
	{
		return text;
	}

	public boolean isJustified()
	{
		return justify;
	}

	private void morph()
	{
		revalidate();
		repaint();
	}

	protected void paintComponent(final Graphics g)
	{
		super.paintComponent(g);
		paintOrGetSize((Graphics2D) g, getWidth());
	}

	private Dimension paintOrGetSize(final Graphics2D g, int width)
	{
		final Insets insets = getInsets();
		width -= insets.left + insets.right + margin.left + margin.right;
		float w = insets.left + insets.right + margin.left + margin.right;
		final float x = insets.left + margin.left;
		float y = insets.top + margin.top;
		if (width > 0 && text != null && text.length() > 0)
		{
			final AttributedString as = new AttributedString(getText());
			as.addAttribute(TextAttribute.FONT, getFont());
			final AttributedCharacterIterator aci = as.getIterator();
			final LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
			float max = 0;
			while (lbm.getPosition() < aci.getEndIndex())
			{
				TextLayout textLayout = lbm.nextLayout(width);
				if (g != null && isJustified() && textLayout.getVisibleAdvance() > 0.80 * width)
					textLayout = textLayout.getJustifiedLayout(width);
				if (g != null)
					textLayout.draw(g, x, y + textLayout.getAscent());
				y += textLayout.getDescent() + textLayout.getLeading() + textLayout.getAscent();
				max = Math.max(max, textLayout.getVisibleAdvance());
			}
			w += max;
		}
		return new Dimension((int) Math.ceil(w), (int) Math.ceil(y) + insets.bottom + margin.bottom);
	}

	public void setJustified(final boolean justify)
	{
		final boolean old = this.justify;
		this.justify = justify;
		firePropertyChange("justified", old, this.justify);
		if (old != this.justify)
			repaint();
	}

	public void setMaxWidth(final int maxWidth)
	{
		if (maxWidth <= 0)
			throw new IllegalArgumentException();
		final int old = this.maxWidth;
		this.maxWidth = maxWidth;
		firePropertyChange("maxWidth", old, this.maxWidth);
		if (old != this.maxWidth)
			morph();
	}

	public void setText(final String text)
	{
		final String old = this.text;
		this.text = text;
		firePropertyChange("text", old, this.text);
		if ((old == null) ? text != null : !old.equals(text))
			morph();
	}
}
