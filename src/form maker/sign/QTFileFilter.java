/**
 * 
 */
package sign;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: QTFileFilter.java 52 2007-11-08 03:56:17Z martin $
 */
public class QTFileFilter extends FileFilter
{
	// Accept all directories and all mpg, mpeg, mov, or avi or mp4 files.
	public boolean accept(File f)
	{
		if (f.isDirectory())
		{
			return true;
		}

		String extension = QTFileFilterUtils.getExtension(f);
		if (extension != null)
		{
			if (extension.equals(QTFileFilterUtils.mpg) || extension.equals(QTFileFilterUtils.mpeg)
					|| extension.equals(QTFileFilterUtils.mov)
					|| extension.equals(QTFileFilterUtils.avi)
					|| extension.equals(QTFileFilterUtils.mp4))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		return false;
	}

	// The description of this filter
	public String getDescription()
	{
		return "Quicktime Movies";
	}
}
