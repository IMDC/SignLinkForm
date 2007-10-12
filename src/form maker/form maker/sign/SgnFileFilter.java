/**
 * 
 */
package sign;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Martin Gerdzhev
 *
 */
public class SgnFileFilter extends FileFilter
{
	// Accept all directories and all mpg, mpeg, mov, or avi or mp4 files.
	public boolean accept(File f)
	{
		if (f.isDirectory())
		{
			return true;
		}

		String extension = f.getName().substring(f.getName().lastIndexOf(".")+1);
		if (extension != null)
		{
			if (extension.toLowerCase().equals("sgn"))
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
		return "SignLink Studio Source Files";
	}
}
