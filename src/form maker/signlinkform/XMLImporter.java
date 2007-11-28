/**
 * 
 */
package signlinkform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: XMLImporter.java 52 2007-11-08 03:56:17Z martin $
 */
public class XMLImporter
{

	private Scanner			in;
	private String			xmlFile;
	private String			movie;
	private int				duration;
	private int				timeScale;
	private File			file;
	private String			transcriptText;
	private ArrayList<Sign>	signs;
	private int				version;

	public XMLImporter(File f)
	{
		file = f;
		try
		{
			in = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlFile = readFile();
		String ver = xmlFile.substring(xmlFile.indexOf("\n") - 2, xmlFile.indexOf("\n") - 1);
		if (ver.equalsIgnoreCase("2"))
			version = 2;
		else
			version = 1;
		System.out.println("version:" + version);
		xmlFile = XMLParser.readASLProjectTag(xmlFile);
		xmlFile = readMovie();
		readSignIconAndTranscript(); // after this call everything is populated
		File movieFile = new File(movie);
		if (!movieFile.exists())
		{
			JOptionPane.showMessageDialog(null,
					"The movie file associated with this source file appears to have been moved. Please locate the file.",
					"Movie File Not Found!", JOptionPane.WARNING_MESSAGE);
			JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(new QTFileFilter());
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				movieFile = jfc.getSelectedFile();
			}
		}
		new MenuFrame(movieFile, f, duration, timeScale, transcriptText, signs);
	}

	private String readFile()
	{
		String temp = "";
		while (in.hasNextLine())
		{
			temp = temp + in.nextLine() + "\n";
		}
		System.out.println(temp);
		return temp;
	}

	private String readMovie()
	{
		String[] mov = XMLParser.readMovieTag(xmlFile);
		if (version == 2)
			movie = file.getParent() + File.separator + mov[0];
		else
			movie = mov[0];
		duration = Integer.parseInt(mov[1]);
		timeScale = Integer.parseInt(mov[2]);
		return mov[3];
	}

	/**
	 * 
	 */
	private void readSignIconAndTranscript()
	{
		ArrayList<String> text = XMLParser.readSignIconsTag(xmlFile);
		transcriptText = text.get(0);
		if (version == 1)
			transcriptText = transcriptText.substring(transcriptText.lastIndexOf("[") + 1, transcriptText.indexOf("]"));
		if (text.get(0).startsWith("\n"))
			transcriptText = transcriptText.substring(1);
		signs = new ArrayList<Sign>();
		for (int i = 0; i < text.size() - 1; i++)
		{
			String aSign = text.get(i + 1);
			signs.add(new Sign());
			if (version == 2)
			{
				signs.get(i).setMStart(Integer.parseInt(aSign.substring(aSign.indexOf("start=") + 7, aSign.indexOf("end") - 2)));
				signs.get(i).setMEnd(Integer.parseInt(aSign.substring(aSign.indexOf("end=") + 5, aSign.indexOf("frametime") - 2)));
				signs.get(i).setFTime(Integer.parseInt(aSign.substring(aSign.indexOf("frametime=") + 11, aSign.indexOf("/>") - 2)));
			}
			else
			{
				signs.get(i).setMStart(
						Integer.parseInt(aSign.substring(aSign.indexOf("start=") + 7, aSign.indexOf("end") - 2)) * 1000 / timeScale);
				signs.get(i).setMEnd(
						Integer.parseInt(aSign.substring(aSign.indexOf("end=") + 5, aSign.indexOf("frametime") - 2)) * 1000 / timeScale);
				String temp = aSign.substring(aSign.indexOf("frametime=") + 11, aSign.indexOf("/>") - 1);
				int power = Integer.parseInt(temp.substring(temp.indexOf("e+") + 2));
				temp = temp.substring(0, temp.indexOf("e+"));
				int fTime = (int) (Double.parseDouble(temp) * Math.pow(10, power) * 1000 / timeScale);
				signs.get(i).setFTime(fTime);
			}
			aSign = aSign.substring(aSign.indexOf("<frameoverlay"));
			signs.get(i).setX(Integer.parseInt(aSign.substring(aSign.indexOf("left=") + 6, aSign.indexOf("top") - 2)));
			signs.get(i).setY(Integer.parseInt(aSign.substring(aSign.indexOf("top=") + 5, aSign.indexOf("height") - 2)));
			signs.get(i).setHeight(Integer.parseInt(aSign.substring(aSign.indexOf("height=") + 8, aSign.indexOf("width") - 2)));
			String temp = aSign.substring(aSign.indexOf("width=") + 7, aSign.indexOf("/>") - 1);
			signs.get(i).setWidth(Integer.parseInt(temp.substring(0, temp.indexOf("\""))));
			signs.get(i).setUrl(aSign.substring(aSign.indexOf("<url>") + 5, aSign.indexOf("</url>")));
			aSign = aSign.substring(aSign.indexOf("newwindow=") + 11);
			String br = aSign.substring(0, 1);
			if (br.equals("Y"))
				signs.get(i).setNewWindow(true);
			else
				signs.get(i).setNewWindow(false);
			signs.get(i).setLabel(aSign.substring(aSign.indexOf("<label>") + 7, aSign.indexOf("</label>")));
		}
	}
}
