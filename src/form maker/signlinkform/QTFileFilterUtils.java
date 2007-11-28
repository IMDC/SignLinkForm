/**
 * 
 */
package signlinkform;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: QTFileFilterUtils.java 52 2007-11-08 03:56:17Z martin $
 */
import java.io.File;

import javax.swing.ImageIcon;

public class QTFileFilterUtils
{
	public final static String	mov		= "mov";
	public final static String	mp4		= "mp4";
	public final static String	avi		= "avi";
	public final static String	mpg		= "mpg";
	public final static String	mpeg	= "mpeg";

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path)
	{
		java.net.URL imgURL = QTFileFilterUtils.class.getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL);
		}
		else
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/*
	 * Get the extension of a file.
	 */
	public static String getExtension(File f)
	{
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
		{
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}