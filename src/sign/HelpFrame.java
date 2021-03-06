/**
 * 
 */
package sign;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: HelpFrame.java 52 2007-11-08 03:56:17Z martin $
 */
public class HelpFrame extends JFrame
{

	private static final long	serialVersionUID	= 5137465102736706914L;
	public static final String	WELCOME				= "/introsigned.mp4";

	public static final String	A					= "/addingtext.mp4";
	public static final String	A1					= "/exportmov.mp4";
	public static final String	A2					= "/filecannotfind.mp4";
	public static final String	A3					= "/imagefilecannotfind.mp4";
	public static final String	A4					= "/linkaddress.mp4";
	public static final String	A5					= "/linktime.mp4";
	public static final String	A6					= "/mainvideowin.mp4";
	public static final String	A7					= "/saveyourproject.mp4";
	public static final String	A8					= "/signlinkhelp.mp4";
	public static final String	A9					= "/signlinkimage.mp4";
	public static final String	A10					= "/Signlinklist.mp4";
	public static final String	A11					= "/signlinkproperties.mp4";
	public static final String	A12					= "/specifyfolder.mp4";
	public static final String	A13					= "/Tips1.mp4";
	public static final String	A14					= "/Tips2.mp4";
	public static final String	A15					= "/UofT_aa_med.mp4";
	public static final String	A16					= "/videocannotstart.mp4";
	public static final String	A17					= "/visitaslpahca.mp4";
	public static final String	A18					= "/yourvideocannotfind.mp4";

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
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/SignEd_icon_16.jpg")));
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
