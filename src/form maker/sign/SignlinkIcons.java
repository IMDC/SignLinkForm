package sign;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 * A singleton class to hold a single reference to images used in the signlink studio software 
 * 
 * @author Laurel Williams
 * @version $Id: $
 */
public class SignlinkIcons {
	
	private static final Toolkit DEFAULT_TOOLKIT = Toolkit.getDefaultToolkit();
	@SuppressWarnings("unchecked")
	private final Class thisClass = this.getClass();

	//icon urls
	private static final String SIGNED_ICON_16 = "/icons/SignEd_icon_16.jpg";
	private static final String RECORD_ICON = "/icons/sRecord.jpg";
	private static final String STOP_ICON = "/icons/sStopRecPreview.jpg";
	private static final String PLAY_ICON = "/icons/sPlay.jpg";
	private static final String NEW_ICON = "/icons/bFileNew.jpg";
	private static final String OPEN_ICON = "/icons/bFileOpen.jpg";
	private static final String SAVE_ICON = "/icons/bFileSave.jpg";
	private static final String EXPORT_ICON = "/icons/export_icon.jpg";
	private static final String QUIT_ICON = "/icons/bQuit.jpg";
	private static final String RECORD_VIDEO_ICON = "/icons/bRecordVideo.jpg";
	private static final String OPEN_VIDEO_ICON = "/icons/bOpenVideo.jpg";
	private static final String CANCEL_VIDEO_ICON = "/icons/bCancel.jpg";
	private static final String HELP_ICON = "/icons/sHelp.jpg";
	private static final String SET_FRAME_ICON = "/icons/sSetFrame.jpg";
	private static final String STEP_BACKWARD_ICON = "/icons/sStepBackward.jpg";
	private static final String STEP_FORWARD_ICON = "/icons/sStepForward.jpg";
	private static final String NEW_SIGNLINK_ICON = "/icons/signlink(16x16)-(for editor).jpg";
	private static final String EDIT_SIGNLINK_ICON = "/icons/sEditSignlink.jpg";
	private static final String PREVIEW_ICON = "/icons/sPlaySignlink.jpg";
	private static final String DELETE_ICON = "/icons/sDelete.jpg";
	private static final String DONE_ICON = "/icons/sOK-Done.jpg";
	private static final String CANCEL_ICON = "/icons/sCancel.jpg";
	private static final String SIGNLINK_REDLINK_ICON = "/icons/images/redlink.gif";	
	private static final String HANDLE_STEP_BACKWARD_ICON = "/icons/sHandleStepBackward.jpg";
	private static final String HANDLE_STEP_FORWARD_ICON = "/icons/sHandleStepForward.jpg";
	private static final String HANDLE_START_ICON = "/icons/sHandleStart.jpg";
	private static final String HANDLE_END_ICON = "/icons/sHandleEnd.jpg";
	private static final String FAST_FORWARD_ICON = "/icons/sFastForward.jpg";
	private static final String REWIND_ICON = "/icons/sRewind.jpg";
	private static final String WELCOME_ICON = "/icons/welcome_top.jpg";
	private static final String ICON_BAR_LOWER_ICON = "/icons/icon_bar_lower.gif";
	
	//icon images to be used within the code
	//used everywhere
	public final Image signEdIcon16 = DEFAULT_TOOLKIT.getImage(thisClass.getResource(SIGNED_ICON_16));
	public final ImageIcon helpImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(HELP_ICON)));

	//used in buttons panel
	public final ImageIcon newImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(NEW_ICON)));
	public final ImageIcon openImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(OPEN_ICON)));
	public final ImageIcon saveImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(SAVE_ICON)));
	public final ImageIcon exportImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(EXPORT_ICON)));
	public final ImageIcon quitImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(QUIT_ICON)));

	//used in recording window
	public final ImageIcon recordImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(RECORD_ICON)));
	public final ImageIcon stopImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(STOP_ICON)));
	public final ImageIcon playImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(PLAY_ICON)));

	//used in the CreateWindow
	public final ImageIcon recordVideoImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(RECORD_VIDEO_ICON)));
	public final ImageIcon openVideoImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(OPEN_VIDEO_ICON)));
	public final ImageIcon cancelVideoImageIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(CANCEL_VIDEO_ICON)));

	//used in the WelcomeFrame
	public final ImageIcon welcomeIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(WELCOME_ICON)));
	
	//used in Image Component
	public final ImageIcon setFrameIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(SET_FRAME_ICON)));
	public final ImageIcon frameBackIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(STEP_BACKWARD_ICON)));
	public final ImageIcon frameForwardIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(STEP_FORWARD_ICON)));
	
	//used in SignButtonsPannel
	public final ImageIcon newSignLinkIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(NEW_SIGNLINK_ICON)));
	public final ImageIcon editSignLinkIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(EDIT_SIGNLINK_ICON)));
	public final ImageIcon previewSignLinkIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(PREVIEW_ICON)));
	public final ImageIcon deleteSignLinkIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(DELETE_ICON)));
	
	//used in SignLink and TextLinkFrame
	public final ImageIcon doneIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(DONE_ICON)));
	public final ImageIcon cancelIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(CANCEL_ICON)));
	public final ImageIcon redLinkIcon	= new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(SIGNLINK_REDLINK_ICON)).getScaledInstance(
					90, 20, 0));
	
	//used in TimingPanelControlPanel
	public final ImageIcon handleStepForwardIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(HANDLE_STEP_FORWARD_ICON)));
	public final ImageIcon handleStepBackwardIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(HANDLE_STEP_BACKWARD_ICON)));
	public final ImageIcon handleStartIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(HANDLE_START_ICON)));
	public final ImageIcon handleEndIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(HANDLE_END_ICON)));
	
	//used in VideoButtonsPanel
	public final ImageIcon fastForwardIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(FAST_FORWARD_ICON)));
	public final ImageIcon rewindIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(REWIND_ICON)));
	
	//used in TimingTab
	public final ImageIcon iconBarLowerIcon = new ImageIcon(DEFAULT_TOOLKIT.getImage(thisClass.getResource(ICON_BAR_LOWER_ICON)));
	
	private static SignlinkIcons instance = null;	
	public static SignlinkIcons getInstance() {
		if (instance == null) instance = new SignlinkIcons();
		return instance;
	}
	
	//intentionally private (singleton class)
	private SignlinkIcons() {
		super();
	}

}
