package sign;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Martin Gerdzhev
 * @version $Id: SignLinkStudio.java 103 2008-01-14 16:46:27Z martin $
 */
public class SignLinkStudio
{

	public static void main(String[] args)
	{

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// copy the QTJava.zip library if not in java lib\ext location
		boolean qtFound = false;
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1)
		{ // windows
			// location where to put the QTJava.zip
			String destPath = System.getProperty("java.ext.dirs");
			String classPath = System.getenv("PATH");
			System.out.println("PATH: " + classPath);
			String[] dpArr = destPath.split(System.getProperty("path.separator"));
			String[] cpArr = classPath.split(System.getProperty("path.separator"));
			File destFile = null;

			for (int i = 0; i < dpArr.length; i++)
			{
				if (dpArr[i].contains("jre"))
				{
					destPath = dpArr[i];
					System.out.println("java ext lib: " + destPath);
					destFile = new File(destPath + File.separator + "QTJava.zip");
					if (destFile.exists())
						qtFound = true;
					break;
				}
			}
			if (!qtFound)
			{
				for (int i = 0; i < cpArr.length; i++)
				{
					if (cpArr[i].contains("QTSystem"))
					{
						classPath = cpArr[i];
						System.out.println("QTLocation: " + classPath);
						break;
					}
				}
				try
				{
					System.out.println("Copying QTJava.zip to " + destPath);
					SignUtils.copy(new File(classPath + "QTJava.zip"), destFile);
					JOptionPane
					.showMessageDialog(
							null,"SignLink Studio Installled Successfully. \nPlease Restart the Application.", "Please restart SignLink Studio", JOptionPane.INFORMATION_MESSAGE);
					SignUtils.cleanUpAndExit(0);
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Copy of QTJava.zip failed");
					qtFound = false;
				}
			}
		}
		else
		{ // it's a mac
			if (new File("/System/Library/Java/Extensions/QTJava.zip").exists())
				qtFound = true;
		}
		if (!qtFound)
		{
			System.out.println("QuickTime is not installed. Please install QuickTime.");
			JOptionPane
					.showMessageDialog(
							null,
							"QuickTime is not installed.\nIn order to run SignLink Studio, please install QuickTime.\nAvailable from: http://www.apple.com/quicktime/download/",
							"QuickTime is not installed", JOptionPane.WARNING_MESSAGE);
			SignUtils.cleanUpAndExit(1);
		}
		// try
		// {
		// File temp = SignUtils.createTempFolder("tmp");
		// System.out.println("TEMP PATH: "+temp.getAbsolutePath());
		// System.out.println("Moving of temp:
		// "+SignUtils.moveDir(SignUtils.FLEXFOLDER, temp));
		// SignUtils.setFLEXFOLDER(temp);
		// }
		// catch (IOException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		HelpFrame.setHelpEnabled(false); // sets whether asl tooltips are available(help videos)
		WelcomeFrame.getInstance();
	}
}
