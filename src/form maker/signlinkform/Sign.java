/**
 * 
 */
package signlinkform;

import javax.swing.ImageIcon;

/**
 * A Class containing everything necessary to build a signlink class
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: $
 */
public class Sign implements Comparable<Object>
{
	private int		mStart;
	private int		mEnd;
	private int		fTime;
	private int		x;
	private int		y;
	private int		width;
	private int		height;
	private String	url;
	private String	label;
	private boolean	newWindow;
	private ImageIcon preview;
	private boolean previewed;

	/**
	 * constructor that sets all the variables
	 * 
	 * @param frameTime
	 * @param frameX
	 * @param frameY
	 * @param frameWidth
	 * @param frameHeight
	 * @param startTime
	 * @param endTime
	 * @param newBrowserWindow
	 * @param label
	 * @param linkAddress
	 * @param preview
	 */
	public Sign(int frameTime, int frameX, int frameY, int frameWidth, int frameHeight, int startTime, int endTime,
			boolean newBrowserWindow, String label, String linkAddress, ImageIcon preview)
	{
		this.fTime = frameTime;
		this.x = frameX;
		this.y = frameY;
		this.width = frameWidth;
		this.height = frameHeight;
		this.mStart = startTime;
		this.mEnd = endTime;
		this.newWindow = newBrowserWindow;
		this.label = label;
		this.url = linkAddress;
	}

	/**
	 * Default constructor
	 */
	public Sign()
	{
		this.fTime = 0;
		this.x = 0;
		this.y = 0;
		this.width = 32;
		this.height = 24;
		this.mStart = 0;
		this.mEnd = 0;
		this.newWindow = false;
		this.label = "";
		this.url = "";
	}

	protected int getFTime()
	{
		return this.fTime;
	}

	protected int getHeight()
	{
		return this.height;
	}

	protected String getLabel()
	{
		return this.label;
	}

	protected int getMEnd()
	{
		return this.mEnd;
	}

	protected int getMStart()
	{
		return this.mStart;
	}

	protected String getUrl()
	{
		return this.url;
	}

	protected int getWidth()
	{
		return this.width;
	}

	protected int getX()
	{
		return this.x;
	}

	protected int getY()
	{
		return this.y;
	}

	protected boolean isNewWindow()
	{
		return this.newWindow;
	}

	protected void setFTime(int time)
	{
		this.fTime = time;
	}

	protected void setHeight(int height)
	{
		this.height = height;
	}

	protected void setLabel(String label)
	{
		this.label = label;
	}

	protected void setMEnd(int end)
	{
		this.mEnd = end;
	}

	protected void setMStart(int start)
	{
		this.mStart = start;
	}

	protected void setNewWindow(boolean newWindow)
	{
		this.newWindow = newWindow;
	}

	protected void setUrl(String url)
	{
		this.url = url;
	}

	protected void setWidth(int width)
	{
		this.width = width;
	}

	protected void setX(int x)
	{
		this.x = x;
	}

	protected void setY(int y)
	{
		this.y = y;
	}
	
	public int compareTo(final Object arg0)
	{
		if (this.getMStart() > ((Sign) arg0).getMStart())
			return 1;
		else if (this.getMStart() > ((Sign) arg0).getMStart())
			return -1;
		else
			return 0;
	}

	/**
	 * Getter method to return the value of preview
	 * @return the preview
	 */
	public ImageIcon getPreview()
	{
		return this.preview;
	}

	/**
	 * Setter method to set the value of preview
	 * @param preview the value to set
	 */
	public void setPreview(ImageIcon preview)
	{
		this.preview = preview;
	}

	/**
	 * Getter method to return the value of previewed
	 * @return the previewed
	 */
	public boolean isPreviewed()
	{
		return this.previewed;
	}

	/**
	 * Setter method to set the value of previewed
	 * @param previewed the value to set
	 */
	public void setPreviewed(boolean previewed)
	{
		this.previewed = previewed;
	}

}
