package sign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import sign.recording.RecordingException;
import sign.recording.RecordingWindow;

/**
 * A factory class for the CreateWindow containing CreateWindowListener and GetInstance.
 * 
 * @author Martin Gerdzhev
 * @version $Id: CreateWindow.java 49 2007-11-06 19:47:12Z laurel $
 */
public class CreateWindow extends JFrame implements WindowListener {


	private static final long			serialVersionUID		= 1L;

	private static final String			RECORD_ACTION			= "record";
	private static final String			CANCEL_ACTION			= "cancel";
	private static final String			HELP_ACTION				= "help";
	private static final String			OPEN_ACTION				= "open";

	private final CreateWindowListener	createWindowListener;
	private final JPanel				centerPanel;

	private HelpFrame					helpFrame;
	private final WelcomeFrame			welcomeframe;

	private static final int			CREATE_WINDOW_WIDTH		= 400;
	private static final int			CREATE_WINDOW_HEIGHT	= 300;

	private static CreateWindow			instance;

	public static CreateWindow getInstance()
	{
		if (instance == null)
		{
			instance = new CreateWindow();
		}
		return instance;
	}

	private CreateWindow()
	{
		super("Create New Signlink Studio File");
		this.addWindowListener(this);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/SignEd_icon_16.jpg")));
		this.getRootPane().setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createRaisedBevelBorder()));
		this.setBackground(WelcomeFrame.bkg);
		this.setSize(CREATE_WINDOW_WIDTH, CREATE_WINDOW_HEIGHT);
		this.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - CREATE_WINDOW_WIDTH / 2, (int) Toolkit
				.getDefaultToolkit().getScreenSize().getHeight()
				/ 2 - CREATE_WINDOW_HEIGHT / 2);
		this.setLayout(new BorderLayout());
		welcomeframe = WelcomeFrame.getInstance();
		centerPanel = new JPanel(new GridLayout(4, 1, 10, 0));
		centerPanel.setBackground(WelcomeFrame.bkg);
		createWindowListener = new CreateWindowListener();
		this.addComponents();
		this.setVisible(true);
	}

	private void addComponents()
	{
		if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
			final JButton recordButton = new JButton(new ImageIcon(Toolkit
					.getDefaultToolkit().getImage(
							this.getClass().getResource("/bRecordVideo.jpg"))));
			recordButton.setActionCommand(RECORD_ACTION);
			recordButton.addActionListener(createWindowListener);
			recordButton.setPreferredSize(new Dimension(50, 50));
			recordButton.setBackground(WelcomeFrame.bkg);
			final JLabel recordLabel = new JLabel("Record New Video");
			final JPanel recordPanel = new JPanel(new FlowLayout(
					FlowLayout.LEFT, 10, 10));
			recordPanel.setBackground(WelcomeFrame.bkg);
			recordPanel.add(recordButton);
			recordPanel.add(recordLabel);
			centerPanel.add(recordPanel);
		} else {
			System.out.println("recording not available");
		}

		final JButton openButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/bOpenVideo.jpg"))));
		openButton.setActionCommand(OPEN_ACTION);
		openButton.addActionListener(createWindowListener);
		openButton.setPreferredSize(new Dimension(50, 50));
		openButton.setBackground(WelcomeFrame.bkg);
		final JLabel openLabel = new JLabel("Open Existing Video ...");
		final JPanel openPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		openPanel.setBackground(WelcomeFrame.bkg);
		openPanel.add(openButton);
		openPanel.add(openLabel);
		centerPanel.add(openPanel);

		final JButton cancelButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/bCancel.jpg"))));
		cancelButton.setActionCommand(CANCEL_ACTION);
		cancelButton.addActionListener(createWindowListener);
		cancelButton.setPreferredSize(new Dimension(50, 50));
		cancelButton.setBackground(WelcomeFrame.bkg);
		final JLabel cancelLabel = new JLabel("Cancel");
		final JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		cancelPanel.setBackground(WelcomeFrame.bkg);
		cancelPanel.add(cancelButton);
		cancelPanel.add(cancelLabel);
		centerPanel.add(cancelPanel);

		final JButton helpButton = new JButton(new ImageIcon(Toolkit
				.getDefaultToolkit().getImage(
						this.getClass().getResource("/sHelp.jpg"))));
		helpButton.setActionCommand(HELP_ACTION);
		helpButton.addActionListener(createWindowListener);
		helpButton.setPreferredSize(new Dimension(22, 22));
		helpButton.setBackground(WelcomeFrame.bkg);
		final JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		helpPanel.setBackground(WelcomeFrame.bkg);
		helpPanel.add(helpButton);

		this.add(centerPanel, BorderLayout.CENTER);
		this.add(helpPanel, BorderLayout.WEST);
	}

	private class CreateWindowListener implements ActionListener
	{

		public void actionPerformed(final ActionEvent actionEvent)
		{
			String actionCommand = actionEvent.getActionCommand();
			if (actionCommand.equals(CANCEL_ACTION))
			{
				this.closeCreateWindow();
			}
			else if (actionCommand.equals(HELP_ACTION))
			{
				if (helpFrame == null)
				{
					this.createHelpFrame();
				}
				else if (!helpFrame.isVisible())
				{
					helpFrame.dispose();
					this.createHelpFrame();
				}
			}
			else if (actionCommand.equals(OPEN_ACTION))
			{
				final File f = VideoPanel.prompt();
				if (f != null)
				{
					this.disposeHelpFrame();
					// if file is not mp4 convert it to mp4
					String fileName = f.getName();
					if (!fileName.toUpperCase().endsWith("MP4"))
					{
						CreateWindow.this.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						final JProgressBar progressBar;
						final JLabel label;
						int width = 400;
						int height = 100;
						final JFrame exportFrame;
						JPanel progressPanel = new JPanel();
						progressBar = new JProgressBar(0, 100);
						progressBar.setIndeterminate(true);
						progressPanel.add(progressBar);
						label = new JLabel(
								"File type is not of the proper type.\nPlease Wait while file is being converted...");
						label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
						exportFrame = new JFrame("Importing File...");
						exportFrame.setSize(width, height);
						exportFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/SignEd_icon_16.jpg")));
						exportFrame.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - width / 2, (int) Toolkit
								.getDefaultToolkit().getScreenSize().getHeight()
								/ 2 - height / 2);
						exportFrame.getContentPane().add(progressPanel, BorderLayout.CENTER);
						exportFrame.getContentPane().add(label, BorderLayout.NORTH);

						exportFrame.setVisible(true);

						final File newFile = new File("./.flexTemp/tmp.mp4");
						final MovieExporter mExport = new MovieExporter(f
								.getAbsolutePath(), newFile.getAbsolutePath(),
								"mp4");

						new Thread()
						{
							public void run()
							{
								boolean mExp = mExport.export();

								if (!mExp)
								{
									label
											.setText("Cannot import movie!!!\nIt is possible the movie is corrupted or there is not enough space on disk.");
									CreateWindow.this.getRootPane().setCursor(null);
									try
									{
										Thread.sleep(10000);
									}
									catch (InterruptedException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									exportFrame.dispose();
									return;
								}
								exportFrame.dispose();
								CreateWindowListener.this.disposeCreateWindow();
								CreateWindow.this.getRootPane().setCursor(null);
								new MenuFrame(newFile);
								return;
							}
						}.start();
					}
					else
					{
						this.disposeCreateWindow();
						new MenuFrame(f);
					}
				}
			}
			else if (actionCommand.equals(RECORD_ACTION))
			{
				this.disposeCreateWindow();
				this.disposeHelpFrame();
				try
				{
					RecordingWindow recordingFrame = RecordingWindow
							.getInstance();
					recordingFrame.addDisplayDataSource();
					recordingFrame.setSize(400, 400);
					recordingFrame.setLocationRelativeTo(null);
					recordingFrame.setVisible(true);
				}
				catch (RecordingException re)
				{
					CreateWindow.getInstance().setVisible(true);
				}
			}
		}

		private void disposeCreateWindow()
		{
			CreateWindow.this.setVisible(false);
			CreateWindow.this.dispose();
		}

		private void createHelpFrame()
		{
			helpFrame = new HelpFrame(HelpFrame.WELCOME, new Point((int) CreateWindow.this.getLocationOnScreen().getX()
					+ CREATE_WINDOW_WIDTH, (int) CreateWindow.this.getLocationOnScreen().getY()));
		}

		private void disposeHelpFrame()
		{
			if (helpFrame != null)
				helpFrame.dispose();
		}
		
		private void revealWelcomeWindow() {
			welcomeframe.setVisible(true);
			welcomeframe.requestFocus();
			welcomeframe.getHelp().setVisible(true);
		}
		
		public void closeCreateWindow() {
			this.disposeCreateWindow();
			this.disposeHelpFrame();
			this.revealWelcomeWindow();
		}
	}


	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent windowEvent) {
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}

	public void windowClosing(WindowEvent e) {
		this.createWindowListener.closeCreateWindow();
	}
}
