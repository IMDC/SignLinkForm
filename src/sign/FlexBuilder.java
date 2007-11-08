/**
 * 
 */
package sign;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class that deals with building the actionscript classes into one final swf file
 * 
 * @author Martin Gerdzhev
 * @version $Id: FlexBuilder.java 52 2007-11-08 03:56:17Z martin $
 */
public class FlexBuilder
{
	private String	flex;
	private String	fontInfo;
	private String	compiler;

	public FlexBuilder()
	{
		try
		{
			flex = SignUtils.FLEXFOLDER.getCanonicalPath();
			flex = flex.replaceAll("\\\\", "/");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fontInfo = flex + "/frameworks/";
		compiler = flex + "/lib/mxmlc.jar";
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") == -1)
		{ // not windows (It's a mac)
			fontInfo += "macFonts.ser";
		}
		else
			fontInfo += "winFonts.ser";
	}

	public boolean buildSWF(String input, String output)
	{

		String error = "";
		try
		{
			// System.out.println("Starting");
			Process builder = Runtime.getRuntime().exec(
					new String[] { "java", "-jar", compiler, "-compiler.as3", "-compiler.optimize", "-compiler.show-actionscript-warnings",
							"-compiler.strict", "-compiler.verbose-stacktraces", "-compiler.source-path", input, "-compiler.library-path",
							input + "frameworks/libs", "-load-config=", "-use-network=false", "-compiler.fonts.local-fonts-snapshot",
							fontInfo, "-output", output, input + "Main.as" });
			Scanner err = new Scanner(builder.getErrorStream());
			while (err.hasNextLine())
			{
				// System.out.println("printing");
				error = err.nextLine();
				System.out.print(error);
			}
			builder.waitFor();
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
		}
		if (error.length() == 0)
			return true;
		return false;
	}
}
