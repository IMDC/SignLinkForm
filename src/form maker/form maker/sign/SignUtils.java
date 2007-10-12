/**
 * 
 */
package sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Martin Gerdzhev
 */
public abstract class SignUtils
{
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
}
