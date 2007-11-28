package signlinkform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

//FIXME make it so that there is only one frame that is initialized always with the movie objects. and when creating new signlinks just swap the variables
/**
 * A class to deal with the creation of a signlink
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: SignLink.java 65 2007-11-22 16:49:31Z martin $
 */
public class SignLink extends JFrame implements ActionListener, WindowListener
{
	private static final long		serialVersionUID		= 3459041326973073778L;
	private static final int		WIDTH					= 395;
	private static final int		HEIGHT					= 630;
	private final JTabbedPane		tabs;
	private JButton					doneButton;
	private JButton					cancelButton;
	private final ImageIcon			underPreviewIcon		= new ImageIcon(Toolkit.getDefaultToolkit().getImage(
																	this.getClass().getResource("/icons/images/redlink.gif")).getScaledInstance(
																	90, 20, 0));
	private final JLabel			underPreviewIconLabel	= new JLabel(underPreviewIcon);
	private final LinkAddressTab	addressTab;
	private final TimingTab			timingTab;
	private final ImageTab			imageTab;
	private final MenuFrame			menuFrame;
	private final VideoComponent	tComponent, iComponent;
	private GlassPanel				glass;
	private final JPanel			north;
	private final JPanel			previewPanel;
	private boolean					edited;
	private HelpFrame				help;
	private String					helpFile;
	private Sign					sign;
	private static SignLink			instance;

	public synchronized static SignLink getInstance(MenuFrame mFrame)
	{
		if (instance == null)
		{
			System.out.println("Initializing values for first time");
			instance = new SignLink(mFrame);
		}
		else
		{
			System.out.println("Clearing values");
			instance.setup();
		}
		return instance;
	}

	public synchronized static SignLink getInstance(MenuFrame mFrame, final Sign signInfo, final ArrayList<Sign> signs)
	{
		if (instance == null)
		{
			System.out.println("Initializing values for first time");
			instance = new SignLink(mFrame, signInfo, signs);
		}
		else
		{
			System.out.println("Clearing values");
			instance.setup(signInfo, signs);
		}
		return instance;
	}

	/**
	 * default constructor that creates the 3 tabs for the signlink creation
	 */
	private SignLink(final MenuFrame mFrame)
	{
		super("Signlink Properties");
		menuFrame = mFrame;
		glass = new GlassPanel();
		this.setGlassPane(glass);
		north = new JPanel();
		north.setLayout(new BorderLayout());
		previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout(5, 5));
		addressTab = new LinkAddressTab();
		final int pos = menuFrame.getVComponent().getVideoButtonsPanel().getVSlide().getThumbPosition();
		final File f = menuFrame.getVComponent().getVideoPanel().getFile();
		tComponent = new VideoComponent(f);
		iComponent = new VideoComponent(f);
		final int time = menuFrame.getVComponent().getVideoPanel().getTime();
		timingTab = new TimingTab(tComponent, time, pos);
		timingTab.getTPanel().getVSlide().setSigns(menuFrame.getSComp().getSignList());
		imageTab = new ImageTab(iComponent, time, timingTab);
		sign = new Sign();
		sign.setPreview(new ImageIcon(this.getImage(0, 0, 160, 120).getImage().getScaledInstance(90, 70, 0)));
		sign.setX(0);
		sign.setY(0);
		sign.setWidth(VideoPanel.WIDTH);
		sign.setHeight(VideoPanel.HEIGHT);
		sign.setFTime(timingTab.getTPanel().getVSlide().getValue());
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.add(timingTab, "Timing");
		tabs.add(imageTab, "Image");
		previewPanel.add(new JLabel(sign.getPreview()), BorderLayout.CENTER);
		previewPanel.add(underPreviewIconLabel, BorderLayout.SOUTH);
		tabs.setSelectedIndex(0);
		final JPanel pr = new JPanel();
		pr.add(previewPanel);
		north.add(pr, BorderLayout.WEST);
		north.add(addressTab, BorderLayout.CENTER);
		this.add(north, BorderLayout.NORTH);
		this.add(tabs);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/SignEd_icon_16.jpg")));
		addButtons();
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.addWindowListener(this);
	}

	private SignLink(final MenuFrame mFrame, final Sign signInfo, final ArrayList<Sign> signs)
	{
		super("Signlink Properties");
		menuFrame = mFrame;
		this.setSign(signInfo);
		glass = new GlassPanel();
		this.setGlassPane(glass);
		north = new JPanel();
		north.setLayout(new BorderLayout());
		previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout(5, 5));
		addressTab = new LinkAddressTab();
		addressTab.setLink(signInfo.getUrl());
		addressTab.setLabel(signInfo.getLabel());
		addressTab.setChecked(signInfo.isNewWindow());
		final SignSlider tempSlider = menuFrame.getVComponent().getVideoButtonsPanel().getVSlide();
		final int startPos = tempSlider.getCoordfromValue(signInfo.getMStart());
		final int endPos = tempSlider.getCoordfromValue(signInfo.getMEnd());
		final File f = menuFrame.getVComponent().getVideoPanel().getFile();
		tComponent = new VideoComponent(f);
		iComponent = new VideoComponent(f);
		timingTab = new TimingTab(tComponent, signInfo.getMStart(), startPos, endPos);
		timingTab.getTPanel().getVSlide().setSigns(signs);
		imageTab = new ImageTab(this, iComponent, signInfo.getFTime(), timingTab, signInfo.getX(), signInfo.getY(), signInfo.getWidth(),
				signInfo.getHeight());
		sign.setPreview(new ImageIcon(this.getImage(signInfo.getX(), signInfo.getY(), signInfo.getWidth(), signInfo.getHeight()).getImage()
				.getScaledInstance(90, 70, 0)));
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.add(timingTab, "Timing");
		tabs.add(imageTab, "Image");
		previewPanel.add(new JLabel(sign.getPreview()), BorderLayout.CENTER);
		previewPanel.add(underPreviewIconLabel, BorderLayout.SOUTH);
		tabs.setSelectedIndex(0);
		final JPanel pr = new JPanel();
		pr.add(previewPanel);
		north.add(pr, BorderLayout.WEST);
		north.add(addressTab, BorderLayout.CENTER);
		this.add(north, BorderLayout.NORTH);
		this.add(tabs);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/SignEd_icon_16.jpg")));
		addButtons();
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.addWindowListener(this);
		this.close();
	}

	private void setup(Sign signInfo, ArrayList<Sign> signs)
	{
		addressTab.clear();
		timingTab.clear();
		imageTab.clear();

		this.setSign(signInfo);

		glass = new GlassPanel(signInfo.getX(), signInfo.getY(), signInfo.getWidth(), signInfo.getHeight());
		this.setGlassPane(glass);

		addressTab.setLink(signInfo.getUrl());
		addressTab.setLabel(signInfo.getLabel());
		addressTab.setChecked(signInfo.isNewWindow());

		final SignSlider tempSlider = menuFrame.getVComponent().getVideoButtonsPanel().getVSlide();
		final int startPos = tempSlider.getCoordfromValue(signInfo.getMStart());
		final int endPos = tempSlider.getCoordfromValue(signInfo.getMEnd());

		timingTab.getTPanel().getVSlide().setSigns(menuFrame.getSComp().getSignList());
		timingTab.setStartEndPos(startPos, endPos);
		timingTab.setTime(signInfo.getFTime());
		imageTab.setTime(signInfo.getFTime());
		tabs.setSelectedIndex(0);
		System.out.println("Glass Dimensions:" +signInfo.getX()+" "+ signInfo.getY()+" "+ signInfo.getWidth()+" "+ signInfo.getHeight());
		sign.setPreview(new ImageIcon(this.getImage(signInfo.getX(), signInfo.getY(), signInfo.getWidth(), signInfo.getHeight()).getImage()
				.getScaledInstance(90, 70, 0)));
		this.setPreview(sign.getPreview());
	}

	private void setup()
	{

		addressTab.clear();
		timingTab.clear();
		imageTab.clear();

		final int time = menuFrame.getVComponent().getVideoPanel().getTime();
		final int pos = menuFrame.getVComponent().getVideoButtonsPanel().getVSlide().getThumbPosition();

		glass = new GlassPanel();
		this.setGlassPane(glass);
		sign = new Sign();
		sign.setPreview(new ImageIcon(this.getImage(0, 0, 160, 120).getImage().getScaledInstance(90, 70, 0)));
		sign.setX(0);
		sign.setY(0);
		sign.setWidth(VideoPanel.WIDTH);
		sign.setHeight(VideoPanel.HEIGHT);
		sign.setFTime(timingTab.getTPanel().getVSlide().getValue());
		timingTab.getTPanel().getVSlide().setSigns(menuFrame.getSComp().getSignList());
		timingTab.setStartPos(pos);
		timingTab.setTime(time);
		imageTab.setTime(time);
		tabs.setSelectedIndex(0);
		sign.setPreview(new ImageIcon(this.getImage(0, 0, 160, 120).getImage().getScaledInstance(90, 70, 0)));
		this.setPreview(sign.getPreview());

	}

	/**
	 * Method to add the done & cancel buttons to the frame
	 */
	private void addButtons()
	{
		doneButton = new JButton("Done", new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/sOK-Done.jpg"))));
		cancelButton = new JButton("Cancel", new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/icons/sCancel.jpg"))));
		doneButton.addActionListener(this);
		cancelButton.addActionListener(this);
		doneButton.setActionCommand("done");
		cancelButton.setActionCommand("cancel");

		final JButton helpButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/icons/sHelp.jpg"))));
		helpButton.setActionCommand("help");
		helpButton.addActionListener(this);
		helpButton.setPreferredSize(new Dimension(22, 22));

		final JPanel south = new JPanel();
		south.add(helpButton);
		south.add(Box.createHorizontalStrut(170));
		south.add(doneButton);
		south.add(cancelButton);
		this.add(south, BorderLayout.SOUTH);
	}

	/**
	 * A method that is used to close the window
	 */
	protected void close()
	{
		menuFrame.setEnabled(true);
		menuFrame.getVComponent().getVideoPanel().setActive(true);
		this.setEdited(false);
		this.dispose();
	}

	protected HelpFrame getHelp()
	{
		return this.help;
	}

	protected String getHelpFile()
	{
		return this.helpFile;
	}

	public boolean isEdited()
	{
		return edited;
	}

	public void setEdited(final boolean edited)
	{
		this.edited = edited;
	}

	protected void setHelpFile(final String helpFile)
	{
		this.helpFile = helpFile;
	}

	protected void setPreview(final ImageIcon icon)
	{
		sign.setPreview(new ImageIcon(icon.getImage().getScaledInstance(90, 70, 0)));
		if (previewPanel.getComponentAt(0, 0) != null)
		{
			previewPanel.remove(previewPanel.getComponentAt(0, 0));
		}
		previewPanel.add(new JLabel(sign.getPreview()));
		previewPanel.revalidate();
		previewPanel.repaint();
		this.invalidate();
		this.validate();
	}

	/**
	 * Getter method to return the value of sign
	 * 
	 * @return the sign
	 */
	public Sign getSign()
	{
		return this.sign;
	}

	/**
	 * Setter method to set the value of sign
	 * 
	 * @param sign
	 *            the value to set
	 */
	public void setSign(Sign sign)
	{
		this.sign = sign;
	}

	protected ImageIcon getImage(final int x, final int y, final int width, final int height)
	{
		return iComponent.getVideoPanel().getImage(x, y, width, height);
	}

	/**
	 * A listener method to listen for button clicking and take the data from all the fields when clicked
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getActionCommand().equals("cancel"))
		{
			SignLink.this.close();
		}
		if (arg0.getActionCommand().equals("done"))
		{
			sign.setMStart(timingTab.getTPanel().getTimingComponent().getStartTime());
			sign.setMEnd(timingTab.getTPanel().getTimingComponent().getEndTime());
			sign.setNewWindow(addressTab.getChecked());
			sign.setUrl(addressTab.getLink());
			sign.setLabel(addressTab.getLabel());
			sign.setFTime(imageTab.getFrameTime());
			sign.setHeight(imageTab.getGlassHeight());
			sign.setWidth(imageTab.getGlassWidth());
			sign.setX(imageTab.getGlassX());
			sign.setY(imageTab.getGlassY());
			menuFrame.getVComponent().getVideoButtonsPanel().getVSlide().setValue(sign.getMStart());
			menuFrame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(false);
			menuFrame.getSignLinks().getMenuComponent(0).setEnabled(false);
			menuFrame.getSComp().getSButtonsPanel().getPreviewButton().setEnabled(true);
			menuFrame.getSComp().getSButtonsPanel().getEditPropertiesButton().setEnabled(true);
			menuFrame.getSComp().getSButtonsPanel().getDeleteButton().setEnabled(true);
			menuFrame.getSignLinks().getMenuComponent(0).setEnabled(false);
			menuFrame.getSignLinks().getMenuComponent(1).setEnabled(true);
			menuFrame.getSignLinks().getMenuComponent(2).setEnabled(true);
			menuFrame.getTPanel().getAddTextLinkButton().setEnabled(true);
			menuFrame.setModified(true);
			if (SignLink.this.isEdited())
			{
				menuFrame.getSComp().updateSign(SignLink.this.getSign());
				// update any text links that exist and are pointing to this signlink
				menuFrame.getTPanel().updateLink(timingTab.getTPanel().getVSlide().getValueFromCoord(timingTab.getTempStartPos()),
						SignLink.this.getSign().getMStart());
			}
			else
				menuFrame.getSComp().addSign(SignLink.this.getSign());
			SignLink.this.close();

		}
		if (arg0.getActionCommand().equals("help"))
		{
			if (help != null)
			{
				help.dispose();
			}
			help = new HelpFrame(helpFile, new Point((int) SignLink.this.getLocationOnScreen().getX() + WIDTH, (int) SignLink.this
					.getLocationOnScreen().getY()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0)
	{
		SignLink.this.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}
