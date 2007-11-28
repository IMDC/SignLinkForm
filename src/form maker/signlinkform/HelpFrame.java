/**
 * 
 */
package signlinkform;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: HelpFrame.java 65 2007-11-22 16:49:31Z martin $
 */
public class HelpFrame extends JFrame
{

	private static final long	serialVersionUID	= 5137465102736706914L;
	public static final String	WELCOME				= "/HelpVideos/introsigned.mp4";

	public static final String	A					= "/HelpVideos/addingtext.mp4";
	public static final String	A1					= "/HelpVideos/exportmov.mp4";
	public static final String	A2					= "/HelpVideos/filecannotfind.mp4";
	public static final String	A3					= "/HelpVideos/imagefilecannotfind.mp4";
	public static final String	A4					= "/HelpVideos/linkaddress.mp4";
	public static final String	A5					= "/HelpVideos/linktime.mp4";
	public static final String	A6					= "/HelpVideos/mainvideowin.mp4";
	public static final String	A7					= "/HelpVideos/saveyourproject.mp4";
	public static final String	A8					= "/HelpVideos/signlinkhelp.mp4";
	public static final String	A9					= "/HelpVideos/signlinkimage.mp4";
	public static final String	A10					= "/HelpVideos/Signlinklist.mp4";
	public static final String	A11					= "/HelpVideos/signlinkproperties.mp4";
	public static final String	A12					= "/HelpVideos/specifyfolder.mp4";
	public static final String	A13					= "/HelpVideos/Tips1.mp4";
	public static final String	A14					= "/HelpVideos/Tips2.mp4";
	public static final String	A15					= "/HelpVideos/UofT_aa_med.mp4";
	public static final String	A16					= "/HelpVideos/videocannotstart.mp4";
	public static final String	A17					= "/HelpVideos/visitaslpahca.mp4";
	public static final String	A18					= "/HelpVideos/yourvideocannotfind.mp4";

	private final VideoPanel	video;
	private static boolean		helpEnabled				= true;

	HelpFrame(final String movieName, final Point location)
	{
		super("ASL ToolTip");
		if (!helpEnabled)
		{
			video = null;
			this.dispose();
			return;
		}
		video = new VideoPanel(this.getClass().getResourceAsStream(movieName));

		this.setLocation(location);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/SignEd_icon_16.jpg")));
		this.addWindowListener(new WindowAdapter()
		{/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(final WindowEvent e)
			{
				HelpFrame.this.setVisible(false);
				HelpFrame.this.dispose();
			}
		});
		this.add(video);
		this.pack();
		this.setVisible(true);
		video.play();
	}

	/**
	 * Getter method to return the value of helpEnabled
	 * @return the helpEnabled
	 */
	public static boolean isHelpEnabled()
	{
		return helpEnabled;
	}

	/**
	 * Setter method to set the value of helpEnabled
	 * @param helpEnabled the value to set
	 */
	public static void setHelpEnabled(boolean helpEnabled)
	{
		HelpFrame.helpEnabled = helpEnabled;
	}
}
