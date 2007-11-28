/**
 * 
 */
package signlinkform;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: $
 */
public class MovieExporter
{
	private String	ffmpeg	= SignUtils.FFMPEGFOLDER.getAbsolutePath() + File.separator;
	private String [] arguments;

	public MovieExporter(String inputFile, String outputFile, String type)
	{
		// setting the appropriate ffmpeg binary
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") == -1)
		{ // not windows (It's a mac)
			ffmpeg += "ffmpeg";
		}
		else
		{
			ffmpeg += "ffmpeg.exe";
		}
		if (type.equals("flv"))
		{
			arguments = new String[] { ffmpeg, "-i", inputFile, "-sameq", "-g", "4", outputFile };
		}
		else if (type.equals("mp4"))
		{
			//sets the dimensions to 320x240 and framerate to 15fps and no audio
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
	
	private boolean export(String [] args)
	{
		String error = "";
		try
		{
			Process builder = Runtime.getRuntime().exec(args);
			Scanner err = new Scanner(builder.getErrorStream());

			while (err.hasNextLine())
			{
				// System.out.println("printing");
				error = err.nextLine();
				System.out.print(error);

			}
			err.close();
			builder.waitFor();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	if (error.length() == 0)
			return true;
		//return false;

	}
}
