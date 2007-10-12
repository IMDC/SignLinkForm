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
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A factory class for the CreateWindow containing CreateWindowListener and
 * GetInstance.
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: CreateWindow.java 16 2007-10-03 15:30:29Z laurel $
 */
public class CreateWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String RECORD_ACTION = "record";
	private static final String CANCEL_ACTION = "cancel";
	private static final String HELP_ACTION = "help";
	private static final String OPEN_ACTION = "open";

	private final CreateWindowListener createWindowListener;
	private final JPanel centerPanel;

	private HelpFrame helpFrame;
	private final WelcomeFrame welcomeframe;

	private static final int CREATE_WINDOW_WIDTH = 400;
	private static final int CREATE_WINDOW_HEIGHT = 300;

	private static CreateWindow instance;

	public synchronized static CreateWindow getInstance() {
		if (instance == null) {
			instance = new CreateWindow();
		}
		return instance;
	}

	private CreateWindow() {
		super("Create New Signlink Studio File");
		this.setUndecorated(true);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/SignEd_icon_16.jpg")));
		this.getRootPane().setBorder(
				BorderFactory.createCompoundBorder(BorderFactory
						.createLineBorder(Color.BLACK), BorderFactory
						.createRaisedBevelBorder()));
		this.setBackground(WelcomeFrame.bkg);
		this.setSize(CREATE_WINDOW_WIDTH, CREATE_WINDOW_HEIGHT);
		this.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth()
				/ 2 - CREATE_WINDOW_WIDTH / 2, (int) Toolkit
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

	/**
	 * 
	 */
	private void addComponents() {
		final JButton recordButton = new JButton(new ImageIcon(Toolkit
				.getDefaultToolkit().getImage(
						this.getClass().getResource("/bRecordVideo.jpg"))));
		recordButton.setActionCommand(RECORD_ACTION);
		recordButton.addActionListener(createWindowListener);
		recordButton.setPreferredSize(new Dimension(50, 50));
		recordButton.setBackground(WelcomeFrame.bkg);
		final JLabel recordLabel = new JLabel("Record New Video");
		final JPanel recordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,
				10, 10));
		recordPanel.setBackground(WelcomeFrame.bkg);
		recordPanel.add(recordButton);
		recordPanel.add(recordLabel);

		final JButton openButton = new JButton(new ImageIcon(Toolkit
				.getDefaultToolkit().getImage(
						this.getClass().getResource("/bOpenVideo.jpg"))));
		openButton.setActionCommand(OPEN_ACTION);
		openButton.addActionListener(createWindowListener);
		openButton.setPreferredSize(new Dimension(50, 50));
		openButton.setBackground(WelcomeFrame.bkg);
		final JLabel openLabel = new JLabel("Open Existing Video ...");
		final JPanel openPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,
				10));
		openPanel.setBackground(WelcomeFrame.bkg);
		openPanel.add(openButton);
		openPanel.add(openLabel);

		final JButton cancelButton = new JButton(new ImageIcon(Toolkit
				.getDefaultToolkit().getImage(
						this.getClass().getResource("/bCancel.jpg"))));
		cancelButton.setActionCommand(CANCEL_ACTION);
		cancelButton.addActionListener(createWindowListener);
		cancelButton.setPreferredSize(new Dimension(50, 50));
		cancelButton.setBackground(WelcomeFrame.bkg);
		final JLabel cancelLabel = new JLabel("Cancel");
		final JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,
				10, 10));
		cancelPanel.setBackground(WelcomeFrame.bkg);
		cancelPanel.add(cancelButton);
		cancelPanel.add(cancelLabel);

		final JButton helpButton = new JButton(new ImageIcon(Toolkit
				.getDefaultToolkit().getImage(
						this.getClass().getResource("/sHelp.jpg"))));
		helpButton.setActionCommand(HELP_ACTION);
		helpButton.addActionListener(createWindowListener);
		helpButton.setPreferredSize(new Dimension(22, 22));
		helpButton.setBackground(WelcomeFrame.bkg);
		final JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,
				10));
		helpPanel.setBackground(WelcomeFrame.bkg);
		helpPanel.add(helpButton);

		centerPanel.add(recordPanel);
		centerPanel.add(openPanel);
		centerPanel.add(cancelPanel);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(helpPanel, BorderLayout.WEST);
	}

	private class CreateWindowListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(final ActionEvent actionEvent) {
			String actionCommand = actionEvent.getActionCommand();
			if (actionCommand.equals(CANCEL_ACTION)) {
				CreateWindow.this.dispose();
				if (helpFrame != null)
					helpFrame.dispose();
				welcomeframe.setVisible(true);
				welcomeframe.requestFocus();
				welcomeframe.getHelp().setVisible(true);
			} else if (actionCommand.equals(HELP_ACTION)) {
				if (helpFrame == null) {
					helpFrame = new HelpFrame(HelpFrame.WELCOME, new Point(
							(int) CreateWindow.this.getLocationOnScreen()
									.getX()
									+ CREATE_WINDOW_WIDTH,
							(int) CreateWindow.this.getLocationOnScreen()
									.getY()));
				} else if (!helpFrame.isVisible()) {
					helpFrame.dispose();
					helpFrame = new HelpFrame(HelpFrame.WELCOME, new Point(
							(int) CreateWindow.this.getLocationOnScreen()
									.getX()
									+ CREATE_WINDOW_WIDTH,
							(int) CreateWindow.this.getLocationOnScreen()
									.getY()));
				}
			} else if (actionCommand.equals(OPEN_ACTION)) {
				final File f = VideoPanel.prompt();
				if (f != null) {
					if (helpFrame != null)
						helpFrame.dispose();
					new MenuFrame(f);
					CreateWindow.this.setVisible(false);
					CreateWindow.this.dispose();
				}
			} else if (actionCommand.equals(RECORD_ACTION)) {
				// do stuff
			}
		}
	}

}
