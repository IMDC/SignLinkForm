/**
 * 
 */
package sign;

import java.util.ArrayList;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: XMLParser.java 52 2007-11-08 03:56:17Z martin $
 */
public abstract class XMLParser
{
	public static String readASLProjectTag(String text)
	{
		return text.substring(text.indexOf(">") + 1, text.lastIndexOf("<"));
	}

	/**
	 * returns a string array of size 4 with 0 - movieFileName, 1 - duration, 2 - timescale, 3 - between the 2 tags
	 * 
	 * @param text
	 * @return a string array of size 4 with 0 - movieFileName, 1 - duration, 2 - timescale, 3 - between the 2 tags
	 */
	public static String[] readMovieTag(String text)
	{
		String[] finalText = new String[4];
		text = text.substring(text.indexOf("\"") + 1, text.lastIndexOf("<"));
		finalText[0] = text.substring(0, text.indexOf("\"")); // movie File Name
		text = text.substring(text.indexOf("duration=\"") + 10);
		finalText[1] = text.substring(0, text.indexOf("\"")); // duration
		text = text.substring(text.indexOf("timescale=\"") + 11);
		finalText[2] = text.substring(0, text.indexOf("\"")); // timescale
		finalText[3] = text.substring(text.indexOf("<")); // the text between the tags

		return finalText;
	}

	public static ArrayList<String> readSignIconsTag(String text)
	{
		ArrayList<String> finalText = new ArrayList<String>();
		String otherText = text.substring(text.indexOf("<transcript") + 13, text.indexOf("</transcript>"));
		finalText.add(otherText); // text between transcript tags
		text = text.substring(text.indexOf(">") + 1, text.indexOf("</signicons>"));
		String[] str = text.split("</signicon>");
		for (int i = 0; i < str.length-1; i++) //it writes the last one as /n<signicon>
		{
			finalText.add(str[i] + "</signicon>");
		}
		return finalText;
	}

	public static String writeASLProjectTag(String text)
	{
		return "<aslproject>\n" + text + "</aslproject>\n";
	}

	public static String writeInlineBrowserTag(boolean newWindow)
	{
		if (newWindow)
			return "<browser newwindow=\"Y\" />\n";
		else
			return "<browser newwindow=\"N\" />\n";
	}

	public static String writeInlineFrameOverlayTag(int x, int y, int width, int height)
	{
		return "<frameoverlay left=\"" + x + "\" top=\"" + y + "\" height=\"" + height + "\" width=\"" + width + "\" />\n";
	}

	public static String writeInlineMovieTimeTag(int start, int end, int frameTime)
	{
		return "<movietime start=\"" + start + "\" end=\"" + end + "\" frametime=\"" + frameTime + "\" />\n";
	}

	public static String writeLabelTag(String label)
	{
		return "<label>" + label + "</label>\n";
	}

	public static String writeMovieTag(String text, String src, String id, int dur, int tScale)
	{
		return "<movie src=\"" + src + "\" ID=\"" + id + "\" duration=\"" + dur + "\" timescale=\"" + tScale + "\">\n" + text
				+ "</movie>\n";
	}

	public static String writeSignIconsTag(String text, String path)
	{
		return "<signicons path=\"" + path + "\">\n" + text + "</signicons>\n";
	}

	public static String writeSignIconTag(String text, String src, String id)
	{
		return "<signicon src=\"" + src + "\" ID=\"" + id + "\">\n" + text + "</signicon>\n";
	}

	public static String writeTranscriptTag(String text)
	{
		return "<transcript>\n" + text + "</transcript>\n";
	}

	public static String writeURLTag(String url)
	{
		return "<url>" + url + "</url>\n";
	}

}
