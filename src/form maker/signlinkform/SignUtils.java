/**
 * 
 */
package signlinkform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: SignUtils.java 52 2007-11-08 03:56:17Z martin $
 */
public abstract class SignUtils
{
	//this folder will be available when running the jar file. It will extract it from the folder onejar inside the jar file.
	public static final File FLEXFOLDER = new File("./.flexTemp");
	public static final File FFMPEGFOLDER = new File("./.flexTemp/ffmpeg");
	/** Fast & simple file copy. */
	public static void copy(File source, File dest) throws IOException
	{
		FileChannel in = null, out = null;
		try
		{
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(dest).getChannel();

			long size = in.size();
			MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);

			out.write(buf);
		}
		finally
		{
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	public static void removeTempFiles()
	{
		System.out.println("Temp directory deletion: " + deleteDir(FLEXFOLDER));
	}

	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns false.
	public static boolean deleteDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
				{
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Method that is called when the program exits to remove the temporary directory flex that is used to compile the flash code. Useful
	 * only when run from the Jar file.
	 * 
	 * @param exitCode
	 */
	public static void cleanUpAndExit(int exitCode)
	{
		removeTempFiles();
		System.exit(exitCode);
	}
}
