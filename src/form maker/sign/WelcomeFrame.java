/**
 * 
 */
package sign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the First frame to show when starting the program
 * 
 * @author Martin Gerdzhev
 * @version $Id: WelcomeFrame.java 102 2008-01-14 15:20:35Z martin $
 */
public class WelcomeFrame extends JFrame
{

	private SignlinkIcons	images	= SignlinkIcons.getInstance();

	class BListen extends WindowAdapter implements ActionListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(final ActionEvent arg0)
		{
			if (arg0.getActionCommand().equals("quit"))
			{
				help.dispose();
				SignUtils.cleanUpAndExit(0);
				WelcomeFrame.this.dispose();
				System.exit(0);
			}
			else if (arg0.getActionCommand().equals("help") && !help.isVisible())
			{
				help.dispose();
				help = new HelpFrame(HelpFrame.WELCOME, new Point((int) WelcomeFrame.this.getLocationOnScreen().getX() + WIDTH,
						(int) WelcomeFrame.this.getLocationOnScreen().getY()));
			}
			else if (arg0.getActionCommand().equals("new"))
			{
				help.dispose();
				WelcomeFrame.this.dispose();
				CreateWindow.getInstance().setVisible(true);
			}
			else if (arg0.getActionCommand().equals("open"))
			{
				final JFileChooser jfc = new JFileChooser();
				jfc.setFileFilter(new SgnFileFilter());
				if (jfc.showOpenDialog(WelcomeFrame.this) == JFileChooser.APPROVE_OPTION)
				{
					help.dispose();
					new XMLImporter(jfc.getSelectedFile());
					// WelcomeFrame.this.dispose();
				}
			}

		}

		@Override
		public void windowClosing(final WindowEvent e)
		{
			help.dispose();
			WelcomeFrame.this.dispose();
			SignUtils.cleanUpAndExit(0);
		}

	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2049257639049010934L;
	public static final int		WIDTH				= 615;
	public static final int		HEIGHT				= 437;
	public static final Color	bkg					= new Color(0x63a2d6);
	private static WelcomeFrame	instance;

	public synchronized static WelcomeFrame getInstance()
	{
		if (instance == null)
		{
			instance = new WelcomeFrame();
		}

		return instance;
	}

	private final JPanel	centerPanel	= new JPanel();
	private final BListen	listen;
	private HelpFrame		help;

	private WelcomeFrame()
	{
		super("Welcome");
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2, (int) Toolkit.getDefaultToolkit()
				.getScreenSize().getHeight()
				/ 2 - HEIGHT / 2);
		this.setIconImage(images.signEdIcon16);
		listen = new BListen();
		this.addWindowListener(listen);
		addComponents();
		help = new HelpFrame(HelpFrame.WELCOME, new Point((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 + WIDTH / 2,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - HEIGHT / 2));
		this.pack();
		this.setVisible(true);
	}

	private void addComponents()
	{
		this.setLayout(new BorderLayout());
		this.add(new JLabel(images.welcomeIcon), BorderLayout.NORTH);
		centerPanel.setBackground(bkg);
		centerPanel.setLayout(new GridLayout(4, 1, 10, 0));
		final JButton newButton = new JButton(images.newImageIcon);
		newButton.setActionCommand("new");
		newButton.addActionListener(listen);
		newButton.setPreferredSize(new Dimension(50, 50));
		newButton.setBackground(bkg);
		final JLabel newLabel = new JLabel("Create New Signlink Studio File");
		final JPanel newPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		newPanel.setBackground(bkg);
		newPanel.add(newButton);
		newPanel.add(newLabel);

		final JButton openButton = new JButton(images.openImageIcon);
		openButton.setActionCommand("open");
		openButton.addActionListener(listen);
		openButton.setPreferredSize(new Dimension(50, 50));
		openButton.setBackground(bkg);
		final JLabel openLabel = new JLabel("Open Existing Signlink Studio File ...");
		final JPanel openPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		openPanel.setBackground(bkg);
		openPanel.add(openButton);
		openPanel.add(openLabel);

		final JButton quitButton = new JButton(images.quitImageIcon);
		quitButton.setActionCommand("quit");
		quitButton.addActionListener(listen);
		quitButton.setPreferredSize(new Dimension(50, 50));
		quitButton.setBackground(bkg);
		final JLabel quitLabel = new JLabel("Quit");
		final JPanel quitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		quitPanel.setBackground(bkg);
		quitPanel.add(quitButton);
		quitPanel.add(quitLabel);

		final JButton helpButton = new JButton(images.helpImageIcon);
		helpButton.setActionCommand("help");
		helpButton.addActionListener(listen);
		helpButton.setPreferredSize(new Dimension(22, 22));
		helpButton.setBackground(bkg);
		final JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		helpPanel.setBackground(bkg);
		helpPanel.add(helpButton);
		helpButton.setEnabled(HelpFrame.isHelpEnabled());

		centerPanel.add(newPanel);
		centerPanel.add(openPanel);
		centerPanel.add(quitPanel);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(helpPanel, BorderLayout.WEST);
	}

	public HelpFrame getHelp()
	{
		return this.help;
	}
}
