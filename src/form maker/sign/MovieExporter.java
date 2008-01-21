/**
 * 
 */
package sign;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Martin Gerdzhev
 * @version $Id: $
 */
public class MovieExporter
{
	private String		ffmpeg	= SignUtils.FFMPEGFOLDER.getAbsolutePath() + File.separator;
	private String[]	arguments;

	public MovieExporter(String inputFile, String outputFile, String type)
	{
		// setting the appropriate ffmpeg binary
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") == -1)
		{ // not windows (It's a mac)
			ffmpeg += "ffmpeg";
			try
			{
				// make ffmpeg executable
				Process builder = Runtime.getRuntime().exec("chmod +x " + ffmpeg);
				builder.waitFor();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			ffmpeg += "ffmpeg.exe";
		}
		if (type.equals("flv"))
		{
			// exports with the same quality and makes every 5th frame a keyframe (every 0.33s)
			arguments = new String[] { ffmpeg, "-i", inputFile, "-sameq", "-g", "5", outputFile };
		}
		else if (type.equals("mp4"))
		{
			// sets the dimensions to 320x240 and framerate to 15fps and no audio
			arguments = new String[] { ffmpeg, "-i", inputFile, "-s", "320x240", "-r", "15", "-an", outputFile };
		}

	}

	public boolean export()
	{
		if (export(arguments))
		{
			System.out.println("Transcoding successful");
			return true;
		}
		else
		{
			System.out.println("Transcoding failed");
			return false;
		}
	}

	private boolean export(String[] args)
	{
		try
		{
			Process process = Runtime.getRuntime().exec(args);
			InputHandler errorHandler = new InputHandler(process.getErrorStream(), "Error Stream");
			errorHandler.start();
			InputHandler inputHandler = new InputHandler(process.getInputStream(), "Output Stream");
			inputHandler.start();
			process.waitFor();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Class used for reading the output and error streams of ffmpeg
	 * 
	 * @author Martin Gerdzhev
	 */
	class InputHandler extends Thread
	{

		InputStream	input_;

		InputHandler(InputStream input, String name)
		{
			super(name);
			input_ = input;
		}

		public void run()
		{
			try
			{
				int c;
				while ((c = input_.read()) != -1)
				{
					System.out.write(c);
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}

	}
}
