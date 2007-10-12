/**
 * 
 */
package sign;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Martin Gerdzhev
 */
public class XMLExporter
{
	private String				sourceFileURL;
	private String				exportFileURL;
	private int					duration;
	private int					timeScale;
	private ArrayList<SignLink>	signLinks;
	private String				htmlText;
	
	private String				finalText;
	private String				exportMovieURL;

// IT'S ALIVE
	public XMLExporter(String sURL, String dURL, int dur, int tScale, ArrayList<SignLink> sLinks, String html)
	{
		sourceFileURL = sURL;
		exportFileURL = dURL;
		// the URL for the copy of the movie to be created and named the same way the sgn filename is
		exportMovieURL = exportFileURL.substring(0, exportFileURL.lastIndexOf(File.separator))
				+ sourceFileURL.substring(sourceFileURL.lastIndexOf(File.separator));
		duration = dur;
		timeScale = tScale;
		signLinks = sLinks;
		htmlText = html;
		this.writeToFile();
	}

	private String buildFinalText()
	{
		String text;
		System.out.println(htmlText);
		text = this.buildSigns(ButtonsListener.SIGNICONSLOCATION) + XMLParser.writeTranscriptTag(htmlText);
		text = XMLParser.writeMovieTag(text, exportMovieURL, "1", duration, timeScale);
		text = XMLParser.writeASLProjectTag(text);
		text = "!CREATED BY ASLWEB EDITOR V2!\n" + text;
		return text;
	}

	private String buildSign(String src, String id, int movieStart, int movieEnd, int frameTime, int frameX, int frameY, int frameWidth,
			int frameHeight, String url, boolean newWindow, String label)
	{
		String text = XMLParser.writeInlineMovieTimeTag(movieStart, movieEnd, frameTime)
				+ XMLParser.writeInlineFrameOverlayTag(frameX, frameY, frameWidth, frameHeight) + XMLParser.writeURLTag(url)
				+ XMLParser.writeInlineBrowserTag(newWindow) + XMLParser.writeLabelTag(label);
		return XMLParser.writeSignIconTag(text, src, id);
	}

	private String buildSigns(String path)
	{
		int mStart;
		int mEnd;
		int fTime;
		int fX;
		int fY;
		int fWidth;
		int fHeight;
		String url;
		boolean newWindow;
		String label;
		SignLink current;
		String finalText = "";
		for (int i = 0; i < signLinks.size(); i++)
		{
			current = signLinks.get(i);
			mStart = current.getStartTime();
			mEnd = current.getEndTime();
			fTime = current.getiFrameTime();
			fX = current.getIFrameX();
			fY = current.getIFrameY();
			fWidth = current.getIFrameWidth();
			fHeight = current.getIFrameHeight();
			url = current.getLinkAddress();
			newWindow = current.openInNewBrowserWindow();
			label = current.getLabel();
			finalText = finalText
					+ this.buildSign("" + (i + 1) + ".JPEG", "" + (i + 1), mStart, mEnd, fTime, fX, fY, fWidth, fHeight, url, newWindow,
							label);
		}
		return XMLParser.writeSignIconsTag(finalText, path);
	}

	private void writeToFile()
	{
		try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(exportFileURL)));
			File destMovieFile = new File(exportMovieURL);
			exportMovieURL = destMovieFile.getName(); // strip the movie path and leave just the name for the XML
			finalText = this.buildFinalText();
			System.out.println(finalText);
			if (!destMovieFile.exists()) // copy of the movie exists don't copy it again
				SignUtils.copy(new File(sourceFileURL), destMovieFile);
			while (finalText.length() > 0)
			{
				out.println(finalText.substring(0, finalText.indexOf("\n")));
				finalText = finalText.substring(finalText.indexOf("\n") + 1);
			}
			out.close();
		}
		catch (IOException e)
		{
		}
	}
}
