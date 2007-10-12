package sign;

import java.util.ArrayList;

import javax.swing.JSlider;

public class SignSlider extends JSlider
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7113730238611494245L;
	private ArrayList<SignLink>	signs;
	private int					activeStart;
	private int					activeEnd;
	private SignSliderUI		sUI;

	public SignSlider()
	{
		super();
		signs = new ArrayList<SignLink>();
		activeStart = -1;
		activeEnd = -1;
	}

	public SignSlider(final int st, final int maximum, final int cur)
	{
		super(st, maximum, cur);
		signs = new ArrayList<SignLink>();
		activeStart = -1;
		activeEnd = -1;
	}

	protected int getActiveEnd()
	{
		return activeEnd;
	}

	protected int getActiveStart()
	{
		return activeStart;
	}

	public int getCoordfromValue(final int value)
	{
		return sUI.getXforValue(value);
	}

	/**
	 * @return the signs
	 */
	public ArrayList<SignLink> getSigns()
	{
		return signs;
	}

	protected int getSliderWidth()
	{
		return this.getWidth();
	}

	protected int getStartGap()
	{
		return (getSliderWidth() - getTrackWidth()) / 2;
	}

	protected SignSliderUI getSUI()
	{
		return this.sUI;
	}

	protected int getThumbPosition()
	{
		return this.getCoordfromValue(this.getValue());
	}

	protected int getTrackStart()
	{
		return sUI.getTrackStart();
	}

	protected int getTrackWidth()
	{
		return sUI.getTrackWidth();
	}

	public int getValueFromCoord(final int coord)
	{
		return sUI.getValueForX(coord);
	}

	protected void setActiveEnd(final int num)
	{
		activeEnd = num;
		repaint();
	}

	protected void setActiveStart(final int num)
	{
		activeStart = num;
		repaint();
	}

	/**
	 * @param signs
	 *            the signs to set
	 */
	public void setSigns(final ArrayList<SignLink> signs)
	{
		this.signs = signs;
		this.revalidate();
		this.repaint();
	}

	protected void setSUI(final SignSliderUI sui)
	{
		this.sUI = sui;
	}

	public void setValueFromCoord(final int coord)
	{
		this.setValue(sUI.getValueForX(coord));
		this.fireStateChanged();
	}
}
