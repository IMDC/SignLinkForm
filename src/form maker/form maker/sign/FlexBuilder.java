/**
 * 
 */
package sign;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Martin Gerdzhev
 */
public class FlexBuilder
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		buildSWF("D:/signlink/Martin flv player/flex/", "D:/signlink/Martin flv player/flex/testing.swf");

	}

	public static boolean buildSWF(String input, String output)
	{
		String	fontInfo	= "D:/signlink/BuildTests/";
		// TODO put code here to call flex builder to build the code
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") == -1)
		{ // not windows
			fontInfo += "macFonts.ser";
		}
		else
			fontInfo += "winFonts.ser";
// flex2.tools.Compiler.main(new String[] { "-compiler.as3", "-compiler.optimize", "-compiler.show-actionscript-warnings",
// "-compiler.strict", "-compiler.verbose-stacktraces", "-compiler.source-path", input,"-compiler.library-path",input+"frameworks/libs" ,
// "-load-config=", "-use-network=false", "-compiler.fonts.local-fonts-snapshot", fontInfo, "-output", output, input+"Main.as" });
		String error = "";
		try
		{
			//System.out.println("Starting");
			Process builder = Runtime.getRuntime().exec(
					new String[] { "java", "-jar", "D:/signlink/BuildTests/lib/mxmlc.jar", "-compiler.as3", "-compiler.optimize",
							"-compiler.show-actionscript-warnings", "-compiler.strict", "-compiler.verbose-stacktraces",
							"-compiler.source-path", input, "-compiler.library-path", input + "frameworks/libs", "-load-config=",
							"-use-network=false", "-compiler.fonts.local-fonts-snapshot", fontInfo, "-output", output, input + "Main.as" });
			Scanner err = new Scanner(builder.getErrorStream());

			while (err.hasNext())
			{
				// System.out.println("printing");
				error = err.next();
				System.out.print(error);

			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (error.length() == 0)
			return true;
		return false;
	}
}
