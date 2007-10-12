package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import quicktime.std.StdQTException;
import sign.XMLImporter.Sign;

/**
 * A class to deal with the creation of a signlink
 * 
 * @author Martin Gerdzhev
 */
public class SignLink extends JFrame implements Comparable<Object>
{
	class CloseListener extends WindowAdapter
	{
		public void windowClosing(final WindowEvent arg0)
		{
			if (SignLink.this.isEdited())
			{
				SignLink.this.rollbackChanges();
			}
			SignLink.this.close();
		}
	}

	/**
	 * A listener class to listen for button clicking and take the data from all the fields when clicked
	 * 
	 * @author Martin Gerdzhev
	 */
	class DoneListener implements ActionListener
	{

		public void actionPerformed(final ActionEvent arg0)
		{
			if (arg0.getActionCommand().equals("cancel"))
			{
				if (SignLink.this.isEdited())
				{
					SignLink.this.rollbackChanges();
				}
				SignLink.this.close();
			}
			if (arg0.getActionCommand().equals("done"))
			{
				SignLink.this.setStartTime(SignLink.this.timingTab.getTPanel().getTimingComponent().getStartTime());
				SignLink.this.setEndTime(SignLink.this.timingTab.getTPanel().getTimingComponent().getEndTime());
				SignLink.this.openInNewBrowserWindow(SignLink.this.addressTab.getChecked());
				SignLink.this.setLinkAddress(SignLink.this.addressTab.getLink());
				SignLink.this.setLabel(SignLink.this.addressTab.getLabel());
				// changed from setvaluefromcoord to setvalue
				menuFrame.getVComponent().getVideoButtonsPanel().getVSlide().setValue(SignLink.this.getStartTime());
				menuFrame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(false);
				menuFrame.getSignLinks().getMenuComponent(0).setEnabled(false);
				menuFrame.getSComp().getSButtonsPanel().getPreviewButton().setEnabled(true);
				menuFrame.getSComp().getSButtonsPanel().getEditPropertiesButton().setEnabled(true);
				menuFrame.getSComp().getSButtonsPanel().getDeleteButton().setEnabled(true);
				menuFrame.getSignLinks().getMenuComponent(0).setEnabled(false);
				menuFrame.getSignLinks().getMenuComponent(1).setEnabled(true);
				menuFrame.getSignLinks().getMenuComponent(2).setEnabled(true);
				menuFrame.getTPanel().getAddTextLinkButton().setEnabled(true);
				if (SignLink.this.isEdited())
				{
					menuFrame.getSComp().updateSign(SignLink.this);
					//update any text links that exist and are pointing to this signlink
					menuFrame.getTPanel().updateLink(timingTab.getTPanel().getVSlide().getValueFromCoord(timingTab.getTempStartPos()),SignLink.this.getStartTime());
				}
				else
					menuFrame.getSComp().addSign(SignLink.this);
				SignLink.this.commitChanges();
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
	}

	/**
	 * 
	 */
	private static final long		serialVersionUID		= 3459041326973073778L;
	private static final int		WIDTH					= 395;
	private static final int		HEIGHT					= 630;
	private final JTabbedPane		tabs;
	private JButton					doneButton;
	private JButton					cancelButton;
	private final ImageIcon			underPreviewIcon		= new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/redlink.gif")).getScaledInstance(90, 20, 0));
	private final JLabel			underPreviewIconLabel	= new JLabel(underPreviewIcon);
	private final DoneListener		doneListen;
	private final LinkAddressTab	addressTab;
	private final TimingTab			timingTab;
	private final ImageTab			imageTab;
	private final MenuFrame			menuFrame;
	private ImageIcon				preview;
	private final VideoComponent	tComponent, iComponent;
	private final GlassPanel		glass;
	private final JPanel			north;
	private final JPanel			previewPanel;
	private int						startTime;
	private int						endTime;
	private boolean					newBrowserWindow;
	private String					label;
	private String					linkAddress;
	private boolean					edited;
	private boolean					previewed;
	private HelpFrame				help;
	private String					helpFile;
	private int						iframeTime;
	private int						iFrameX;
	private int						iFrameY;
	private int						iFrameWidth;
	private int						iFrameHeight;

	/**
	 * default constructor that creates the 3 tabs for the signlink creation
	 */
	protected SignLink(final MenuFrame mFrame)
	{
		super("Signlink Properties");
		menuFrame = mFrame;
		//glass = new GlassPanel(34, 135, 160, 120);
		glass = new GlassPanel();
		this.setGlassPane(glass);
		north = new JPanel();
		north.setLayout(new BorderLayout());
		previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout(5, 5));
		doneListen = new DoneListener();
		addressTab = new LinkAddressTab();
		final int pos = menuFrame.getVComponent().getVideoButtonsPanel().getVSlide().getThumbPosition();
		final File f = menuFrame.getVComponent().getVideoPanel().getFile();
		tComponent = new VideoComponent(f);
		iComponent = new VideoComponent(f);
		final int time = menuFrame.getVComponent().getVideoPanel().getTime();
		timingTab = new TimingTab(tComponent, time, pos);
		timingTab.getTPanel().getVSlide().setSigns(menuFrame.getSComp().getSignList());
		imageTab = new ImageTab(iComponent, time, timingTab);
		preview = imageTab.getImage(0, 0, 160, 120);
		preview = new ImageIcon(preview.getImage().getScaledInstance(90, 70, 0));
		setIFrameX(0);
		setIFrameY(0);
		setIFrameWidth(VideoPanel.WIDTH);
		setIFrameHeight(VideoPanel.HEIGHT);
		setiFrameTime(timingTab.getTPanel().getVSlide().getValue());
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.add(timingTab, "Timing");
		tabs.add(imageTab, "Image");
		previewPanel.add(new JLabel(preview), BorderLayout.CENTER);
		previewPanel.add(underPreviewIconLabel, BorderLayout.SOUTH);
		tabs.setSelectedIndex(0);
		final JPanel pr = new JPanel();
		pr.add(previewPanel);
		north.add(pr, BorderLayout.WEST);
		north.add(addressTab, BorderLayout.CENTER);
		this.add(north, BorderLayout.NORTH);
		this.add(tabs);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/SignEd_icon_16.jpg")));
		addButtons();
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.addWindowListener(new CloseListener());
		this.setVisible(true);
	}

	protected SignLink(final MenuFrame mFrame, final Sign signInfo, final ArrayList<SignLink> signs)
	{
		super("Signlink Properties");
		menuFrame = mFrame;
//		glass = new GlassPanel(34, 135, 160, 120);
		glass = new GlassPanel();
		this.setGlassPane(glass);
		north = new JPanel();
		north.setLayout(new BorderLayout());
		previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout(5, 5));
		doneListen = new DoneListener();
		addressTab = new LinkAddressTab();
		addressTab.setLink(signInfo.getUrl());
		addressTab.setLabel(signInfo.getLabel());
		addressTab.setChecked(signInfo.isNewWindow());
		addressTab.commitChanges();
		final SignSlider tempSlider = menuFrame.getVComponent().getVideoButtonsPanel().getVSlide();
		final int startPos = tempSlider.getCoordfromValue(signInfo.getMStart());
		final int endPos = tempSlider.getCoordfromValue(signInfo.getMEnd());
		final File f = menuFrame.getVComponent().getVideoPanel().getFile();
		tComponent = new VideoComponent(f);
		iComponent = new VideoComponent(f);
		timingTab = new TimingTab(tComponent, signInfo.getMStart(), startPos, endPos);
		timingTab.getTPanel().getVSlide().setSigns(signs);
		timingTab.commitChanges();
		imageTab = new ImageTab(this, iComponent, (int) signInfo.getFTime(), timingTab, signInfo.getX(), signInfo.getY(), signInfo
				.getWidth(), signInfo.getHeight());
		imageTab.commitChanges();
		preview = imageTab.getImage(signInfo.getX(), signInfo.getY(), signInfo.getWidth(), signInfo.getHeight());
		preview = new ImageIcon(preview.getImage().getScaledInstance(90, 70, 0));
		setiFrameTime(signInfo.getFTime());
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.add(timingTab, "Timing");
		tabs.add(imageTab, "Image");
		previewPanel.add(new JLabel(preview), BorderLayout.CENTER);
		previewPanel.add(underPreviewIconLabel, BorderLayout.SOUTH);
		tabs.setSelectedIndex(0);
		final JPanel pr = new JPanel();
		pr.add(previewPanel);
		north.add(pr, BorderLayout.WEST);
		north.add(addressTab, BorderLayout.CENTER);
		this.add(north, BorderLayout.NORTH);
		this.add(tabs);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/SignEd_icon_16.jpg")));
		addButtons();
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.addWindowListener(new CloseListener());

		this.setStartTime(this.timingTab.getTPanel().getTimingComponent().getStartTime());
		this.setEndTime(this.timingTab.getTPanel().getTimingComponent().getEndTime());
		this.openInNewBrowserWindow(this.addressTab.getChecked());
		this.setLinkAddress(this.addressTab.getLink());
		this.setLabel(this.addressTab.getLabel());
		this.close();
	}

	/**
	 * Method to add the done & cancel buttons to the frame
	 */
	private void addButtons()
	{
		doneButton = new JButton("Done", new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/sOK-Done.jpg"))));
		cancelButton = new JButton("Cancel", new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/sCancel.jpg"))));
		doneButton.addActionListener(doneListen);
		cancelButton.addActionListener(doneListen);
		doneButton.setActionCommand("done");
		cancelButton.setActionCommand("cancel");

		final JButton helpButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/sHelp.jpg"))));
		helpButton.setActionCommand("help");
		helpButton.addActionListener(new DoneListener());
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
		try
		{
			if (SignLink.this.tComponent.getVideoPanel().getMovie() != null
					&& !SignLink.this.tComponent.getVideoPanel().getMovie().isDone())
				SignLink.this.tComponent.getVideoPanel().stop();
			if (SignLink.this.iComponent.getVideoPanel().getMovie() != null
					&& !SignLink.this.iComponent.getVideoPanel().getMovie().isDone())
				SignLink.this.iComponent.getVideoPanel().stop();
		}
		catch (final StdQTException e1)
		{
			e1.printStackTrace();
		}
		SignLink.this.tComponent.getVideoPanel().closeSession();
		SignLink.this.iComponent.getVideoPanel().closeSession();
		try
		{
			SignLink.this.menuFrame.getVComponent().getVideoPanel().getMovie().setActive(true);
		}
		catch (final StdQTException e)
		{
			e.printStackTrace();
		}
		SignLink.this.menuFrame.setEnabled(true);
		SignLink.this.setEdited(false);
		SignLink.this.dispose();
	}

	public int compareTo(final Object arg0)
	{
		if (this.getStartTime() > ((SignLink) arg0).getStartTime())
			return 1;
		else if (this.getStartTime() > ((SignLink) arg0).getStartTime())
			return -1;
		else
			return 0;
	}

	public void edit()
	{
		SignLink.this.tComponent.getVideoPanel().openSession();
		SignLink.this.iComponent.getVideoPanel().openSession();
		this.setVisible(true);
		tabs.setSelectedIndex(0);
		this.setEdited(true);
	}

	protected int getEndTime()
	{
		return endTime;
	}

	protected HelpFrame getHelp()
	{
		return this.help;
	}

	protected String getHelpFile()
	{
		return this.helpFile;
	}

	protected int getIFrameHeight()
	{
		return this.iFrameHeight;
	}

	protected int getiFrameTime()
	{
		return this.iframeTime;
	}

	protected int getIFrameWidth()
	{
		return this.iFrameWidth;
	}

	protected int getIFrameX()
	{
		return this.iFrameX;
	}

	protected int getIFrameY()
	{
		return this.iFrameY;
	}

	protected String getLabel()
	{
		return label;
	}

	protected String getLinkAddress()
	{
		return linkAddress;
	}

	protected ImageIcon getPreview()
	{
		return preview;
	}

	protected int getStartTime()
	{
		return startTime;
	}

	public boolean isEdited()
	{
		return edited;
	}

	protected boolean isPreviewed()
	{
		return previewed;
	}

	protected boolean openInNewBrowserWindow()
	{
		return newBrowserWindow;
	}

	protected void openInNewBrowserWindow(final boolean window)
	{
		newBrowserWindow = window;
	}

	public void setEdited(final boolean edited)
	{
		this.edited = edited;
	}

	protected void setEndTime(final int end)
	{
		endTime = end;
	}

	protected void setHelpFile(final String helpFile)
	{
		this.helpFile = helpFile;
	}

	protected void setIFrameHeight(final int frameHeight)
	{
		this.iFrameHeight = frameHeight;
	}

	protected void setiFrameTime(final int frameTime)
	{
		this.iframeTime = frameTime;
	}

	protected void setIFrameWidth(final int frameWidth)
	{
		this.iFrameWidth = frameWidth;
	}

	protected void setIFrameX(final int frameX)
	{
		this.iFrameX = frameX;
	}

	protected void setIFrameY(final int frameY)
	{
		this.iFrameY = frameY;
	}

	protected void setLabel(final String aLabel)
	{
		label = aLabel;
	}

	protected void setLinkAddress(final String address)
	{
		linkAddress = address;
	}

	protected void setPreview(final ImageIcon icon)
	{
		preview = new ImageIcon(icon.getImage().getScaledInstance(90, 70, 0));
		previewPanel.remove(previewPanel.getComponentAt(0, 0));
		previewPanel.add(new JLabel(preview));
		previewPanel.revalidate();
		previewPanel.repaint();
		this.invalidate();
		this.validate();
	}

	protected void setPreviewed(final boolean previewed)
	{
		this.previewed = previewed;
	}

	protected void setStartTime(final int start)
	{
		startTime = start;
	}

	/**
	 * Method to undo the changes made to the imageTab when the cancel button is pressed
	 */
	protected void rollbackChanges()
	{
		imageTab.rollbackChanges();
		addressTab.rollBackChanges();
		timingTab.rollbackChanges();
	}

	/**
	 * Method to commit the changes made to the imageTab when the done button is pressed
	 */
	protected void commitChanges()
	{
		imageTab.commitChanges();
		addressTab.commitChanges();
		timingTab.commitChanges();
		menuFrame.setModified(true);
	}

}
