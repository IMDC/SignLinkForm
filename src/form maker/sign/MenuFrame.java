package sign;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is a frame class for the sign-link editor. It is to hold the menus.
 * 
 * @author Martin Gerdzhev
 * @version $Id: MenuFrame.java 118 2008-01-29 17:56:25Z martin $
 */
public class MenuFrame extends JFrame
{
	
	private SignlinkIcons images = SignlinkIcons.getInstance();

	class WListen extends WindowAdapter
	{
		@Override
		public void windowClosing(final WindowEvent e)
		{
			if (MenuFrame.this.isModified())
			{
				int option = JOptionPane.showConfirmDialog(null, "The Project is not saved. Do you want to save the project?", "Warning",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.NO_OPTION)
				{

				}
				else if (option == JOptionPane.CANCEL_OPTION)
				{
					System.out.println("Cancel option");
					return;
				}
				else
				// YES_OPTION
				{
					if (MenuFrame.this.getXmlFile() != null)
						listen.save(MenuFrame.this.getXmlFile().getAbsolutePath());
					else
						listen.saveAs();
					MenuFrame.this.setModified(false);
				}
			}
			MenuFrame.this.cleanUp();
			MenuFrame.this.setVisible(false);
			MenuFrame.this.dispose();
			//SignUtils.cleanUpAndExit(0);
		}
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4884379081794300655L;
	protected static final int	WIDTH				= 800;
	protected static final int	HEIGHT				= 670;
	private ButtonsListener		listen;
	private TextListener		textListen;
	private SignListener		signListen;
// private VideoEditListener videoListen;
	private JMenuBar			bar;
	private JMenu				file;
	private JMenu				editText;
	private JMenu				signLinks;
	private JMenu				video;
	private JMenu				aslWeb;
	private VideoComponent		vComponent;
	private TextPanel			tPanel;
	private SignLinkComponent	sComp;
	private HelpFrame			help;
	// for resaving a file to check if this is not null and overwrite
	private File				xmlFile;
	// whether anything has been modified since last save
	private boolean				modified;

	/**
	 * A constructor that puts everything together
	 * 
	 * @param aComponent -
	 *            the video component
	 */
	public MenuFrame(final File file)
	{
		super("Signlink Studio");
		this.setModified(true);
		vComponent = new VideoComponent(file);
		tPanel = new TextPanel(this);
		listen = new ButtonsListener(this);
		final ButtonsPanel bPanel = new ButtonsPanel(listen);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setIconImage(images.signEdIcon16);
		sComp = new SignLinkComponent(this);

		textListen = new TextListener(tPanel);
		signListen = new SignListener(this);
		vComponent.getVideoButtonsPanel().getVSlide().setSigns(sComp.getSignList());
		this.createMenus();
		final JPanel center = new JPanel();
		center.add(vComponent);
		center.add(Box.createHorizontalStrut(21));
		center.add(tPanel);
		this.addWindowListener(new WListen());
		this.setLayout(new BorderLayout());
		this.add(bPanel, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(sComp, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	/**
	 * method that closes all the quicktime sessions dealing with the currently open video.
	 */
	public void cleanUp()
	{
		if (SignLink.isInitialized())
		{
			SignLink.getInstance(MenuFrame.this).cleanUp();
		}
		vComponent.getVideoPanel().dispose();
		vComponent.getVideoPanel().closeSession();
	}

	public MenuFrame(final File movieFile, final File xml, final int duration, final int timeScale, final String transcriptText,
			final ArrayList<Sign> signsXML)
	{
		super("Signlink Studio");
		xmlFile = xml;
		vComponent = new VideoComponent(movieFile);
		this.setModified(false);
		// check first if the movie file is the same as the one in the .sgn file
		while (vComponent.getVideoPanel().getDuration() != duration || vComponent.getVideoPanel().getTimeScale() != timeScale)
		{
			JOptionPane
					.showMessageDialog(
							null,
							"The selected movie file does not match the movie used to create this project. Please locate the file or click cancel to go back to the main menu.",
							"Movie File does not match!", JOptionPane.WARNING_MESSAGE);
			final JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(new QTFileFilter());
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				vComponent = new VideoComponent(jfc.getSelectedFile());
				this.setModified(true);
			}
			else
			{
				WelcomeFrame.getInstance().setVisible(true);
				this.dispose();
				return;
			}
		}
		WelcomeFrame.getInstance().dispose();
		tPanel = new TextPanel(this, transcriptText);
		listen = new ButtonsListener(this);
		final ButtonsPanel bPanel = new ButtonsPanel(listen);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setIconImage(images.signEdIcon16);

		if (signsXML.size() > 0)
			tPanel.getAddTextLinkButton().setEnabled(true);
		for (int i = 0; i < signsXML.size(); i++)
		{
			vComponent.getVideoPanel().setTimeMiliseconds(signsXML.get(i).getFTime());
			signsXML.get(i).setPreview(
					new ImageIcon(vComponent.getVideoPanel().getImage(signsXML.get(i).getX(), signsXML.get(i).getY(),
							signsXML.get(i).getWidth(), signsXML.get(i).getHeight()).getImage().getScaledInstance(90, 70, 0)));
		}
		vComponent.getVideoPanel().setTimeMiliseconds(0);
		sComp = new SignLinkComponent(this, signsXML);

		textListen = new TextListener(tPanel);
		signListen = new SignListener(this);
// videoListen = new VideoEditListener();
		vComponent.getVideoButtonsPanel().getVSlide().setSigns(sComp.getSignList());
		this.createMenus();
		final JPanel center = new JPanel();
		center.add(vComponent);
		center.add(Box.createHorizontalStrut(21));
		center.add(tPanel);
		this.addWindowListener(new WListen());
		this.setLayout(new BorderLayout());
		this.add(bPanel, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(sComp, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	/**
	 * a method to create the ASL Web menu
	 * 
	 * @return JMenu - the completed menu
	 */
	protected JMenu createAslWebMenu()
	{
		/*
		 * creates the menu and labels
		 */
		final JMenu aslWebMenu = new JMenu("ASL Web");
		final JMenuItem exportAsl = new JMenuItem("Export ASL Web Page...");
		/*
		 * adds menu items to the menu
		 */
		aslWebMenu.add(exportAsl);
		/*
		 * sets the actions associated with the menus
		 */
		exportAsl.setActionCommand("export");
		/*
		 * adds listeners to the menu items
		 */
		exportAsl.addActionListener(listen);

		return aslWebMenu;
	}

	/**
	 * a method to create the editText menu
	 * 
	 * @return JMenu - the completed menu
	 */
	protected JMenu createEditTextMenu()
	{
		/*
		 * creates the menu and labels
		 */
		final JMenu editTextMenu = new JMenu("Edit Text");
		final JMenuItem cutMenuItem = new JMenuItem("Cut");
		final JMenuItem copyMenuItem = new JMenuItem("Copy");
		final JMenuItem pasteMenuItem = new JMenuItem("Paste");
		final JMenuItem clearMenuItem = new JMenuItem("Clear");
		/*
		 * adds menu items to the menu
		 */
		editTextMenu.add(cutMenuItem);
		editTextMenu.add(copyMenuItem);
		editTextMenu.add(pasteMenuItem);
		editTextMenu.add(clearMenuItem);
		/*
		 * sets the actions associated with the menus
		 */
		cutMenuItem.setActionCommand("cut");
		copyMenuItem.setActionCommand("copy");
		pasteMenuItem.setActionCommand("paste");
		clearMenuItem.setActionCommand("clear");
		/*
		 * adds listeners to the menu items
		 */
		cutMenuItem.addActionListener(textListen);
		copyMenuItem.addActionListener(textListen);
		pasteMenuItem.addActionListener(textListen);
		clearMenuItem.addActionListener(textListen);

		return editTextMenu;
	}

	/**
	 * a method to create the file menu
	 * 
	 * @return JMenu - the completed menu
	 */
	protected JMenu createFileMenu()
	{
		/*
		 * creates the menu and labels
		 */
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem newItem = new JMenuItem("New");
		final JMenuItem openItem = new JMenuItem("Open");
		final JMenuItem saveItem = new JMenuItem("Save");
		final JMenuItem saveAsItem = new JMenuItem("Save as ...");
		final JMenuItem quitItem = new JMenuItem("Quit");
		/*
		 * adds menu items to the menu
		 */
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.add(quitItem);
		/*
		 * sets the actions associated with the menus
		 */
		newItem.setActionCommand("new");
		openItem.setActionCommand("open");
		saveItem.setActionCommand("save");
		saveAsItem.setActionCommand("save as");
		quitItem.setActionCommand("exit");
		/*
		 * adds listeners to the menu items
		 */
		newItem.addActionListener(listen);
		openItem.addActionListener(listen);
		saveItem.addActionListener(listen);
		saveAsItem.addActionListener(listen);
		quitItem.addActionListener(listen);

		return fileMenu;
	}

	/**
	 * A method to create the menus
	 */
	public void createMenus()
	{
		bar = new JMenuBar(); // creates the menubar
		setJMenuBar(bar); // sets the menubar

		file = createFileMenu();
		editText = createEditTextMenu();
		signLinks = createSignLinksMenu();
		// video = createVideoMenu();
		aslWeb = createAslWebMenu();

		bar.add(file);
		bar.add(editText);
		bar.add(signLinks);
		// bar.add(video);
		bar.add(aslWeb);
	}

	/**
	 * a method to create the signLinks menu
	 * 
	 * @return JMenu - the completed menu
	 */
	protected JMenu createSignLinksMenu()
	{
		/*
		 * creates the menu and labels
		 */
		final JMenu signLinksMenu = new JMenu("SignLinks");
		final JMenuItem newSignLink = new JMenuItem("New Signlink");
		final JMenuItem editSignLink = new JMenuItem("Edit Signlink");
		final JMenuItem clearSignLink = new JMenuItem("Clear Signlink");
		newSignLink.setEnabled(false);
		editSignLink.setEnabled(false);
		clearSignLink.setEnabled(false);
		/*
		 * adds menu items to the menu
		 */
		signLinksMenu.add(newSignLink);
		signLinksMenu.add(editSignLink);
		signLinksMenu.add(clearSignLink);
		/*
		 * sets the actions associated with the menus
		 */
		newSignLink.setActionCommand("new sign");
		editSignLink.setActionCommand("edit sign");
		clearSignLink.setActionCommand("clear sign");
		/*
		 * adds listeners to the menu items
		 */
		newSignLink.addActionListener(signListen);
		editSignLink.addActionListener(signListen);
		clearSignLink.addActionListener(signListen);

		return signLinksMenu;
	}

	/**
	 * a method to create the Video menu
	 * 
	 * @return JMenu - the completed menu
	 */
	protected JMenu createVideoMenu()
	{
		/*
		 * creates the menu and labels
		 */
		final JMenu videoMenu = new JMenu("Video");
		final JMenuItem editVideo = new JMenuItem("Edit Video...");
		/*
		 * adds menu items to the menu
		 */
		videoMenu.add(editVideo);
		/*
		 * sets the actions associated with the menus
		 */
		editVideo.setActionCommand("edit video");
		/*
		 * adds listeners to the menu items
		 */
		// editVideo.addActionListener(videoListen);
		return videoMenu;
	}

	protected JMenu getAslWeb()
	{
		return aslWeb;
	}

	protected JMenu getEditText()
	{
		return editText;
	}

	protected JMenu getFile()
	{
		return file;
	}

	protected HelpFrame getHelp()
	{
		return this.help;
	}

	protected Point getHelpLocation()
	{
		return new Point((int) this.getLocationOnScreen().getX() + MenuFrame.WIDTH, (int) this.getLocationOnScreen().getY());
	}

	/**
	 * @return the sComp
	 */
	public SignLinkComponent getSComp()
	{
		return sComp;
	}

	protected JMenu getSignLinks()
	{
		return signLinks;
	}

	/**
	 * Getter method to return the value of
	 * 
	 * @return the textListen
	 */
	public TextListener getTextListen()
	{
		return this.textListen;
	}

	/**
	 * Getter method to return the value of
	 * 
	 * @return the tPanel
	 */
	public TextPanel getTPanel()
	{
		return this.tPanel;
	}

	/**
	 * A method that returns the videoComponent
	 * 
	 * @return - the video component
	 */
	protected VideoComponent getVComponent()
	{
		return vComponent;
	}

	protected JMenu getVideo()
	{
		return video;
	}

	protected File getXmlFile()
	{
		return this.xmlFile;
	}

	protected void setAslWeb(final JMenu aslWeb)
	{
		this.aslWeb = aslWeb;
	}

	protected void setEditText(final JMenu editText)
	{
		this.editText = editText;
	}

	protected void setFile(final JMenu file)
	{
		this.file = file;
	}

	protected void setHelp(final HelpFrame help)
	{
		this.help = help;
	}

	/**
	 * @param comp
	 *            the sComp to set
	 */
	public void setSComp(final SignLinkComponent comp)
	{
		sComp = comp;
	}

	protected void setSignLinks(final JMenu signLinks)
	{
		this.signLinks = signLinks;
	}

	protected void setVideo(final JMenu video)
	{
		this.video = video;
	}

	protected void setXmlFile(final File xmlFile)
	{
		this.xmlFile = xmlFile;
	}

	/**
	 * Getter method to return the value of modified
	 * 
	 * @return the modified
	 */
	public boolean isModified()
	{
		return this.modified;
	}

	/**
	 * Setter method to set the value of modified
	 * 
	 * @param modified
	 *            the value to set
	 */
	public void setModified(boolean modified)
	{
		this.modified = modified;
	}
}
