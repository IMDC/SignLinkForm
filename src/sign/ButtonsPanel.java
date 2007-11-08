package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * A Panel class to hold the top buttons
 * 
 * @author Martin Gerdzhev
 * @version $Id: ButtonsPanel.java 52 2007-11-08 03:56:17Z martin $
 */
public class ButtonsPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7127969993381321380L;
	private JButton				newButton;
	private ImageIcon			newIcon;
	private JButton				openButton;
	private ImageIcon			openIcon;
	private JButton				saveButton;
	private ImageIcon			saveIcon;
	private JButton				exportButton;
	private ImageIcon			exportIcon;
	private JButton				exitButton;
	private ImageIcon			exitIcon;

	public ButtonsPanel()
	{
		this.setLayout(new BorderLayout());
		addIcons();
		addListener(new ButtonsListener());
		addDescription();
		addButtons();
	}

	/**
	 * adds the buttons to the pane
	 */
	private void addButtons()
	{
		final JPanel aPanel = new JPanel();
		aPanel.add(newButton);
		aPanel.add(openButton);
		aPanel.add(saveButton);
		aPanel.add(new JSeparator(SwingConstants.VERTICAL));
		aPanel.add(new JSeparator(SwingConstants.VERTICAL));
		aPanel.add(new JSeparator(SwingConstants.VERTICAL));
		aPanel.add(exportButton);
		aPanel.add(new JSeparator(SwingConstants.VERTICAL));
		aPanel.add(new JSeparator(SwingConstants.VERTICAL));
		aPanel.add(new JSeparator(SwingConstants.VERTICAL));
		aPanel.add(exitButton);
		this.add(aPanel, BorderLayout.WEST);
	}

	/**
	 * Adds a tooltip and an action command to the buttons
	 */
	private void addDescription()
	{
		newButton.setToolTipText("New Project");
		newButton.setActionCommand("new");
		openButton.setToolTipText("Open Project");
		openButton.setActionCommand("open");
		saveButton.setToolTipText("Save Project");
		saveButton.setActionCommand("save");
		exportButton.setToolTipText("Export Project");
		exportButton.setActionCommand("export");
		exitButton.setToolTipText("Exit");
		exitButton.setActionCommand("exit");
	}

	/**
	 * A method to add Icons to the buttons and create the buttons
	 */
	private void addIcons()
	{
		newIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/bFileNew.jpg")));
		newButton = new JButton(newIcon);
		newButton.setPreferredSize(new Dimension(40, 40));
		openIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/bFileOpen.jpg")));
		openButton = new JButton(openIcon);
		openButton.setPreferredSize(new Dimension(40, 40));
		saveIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/bFileSave.jpg")));
		saveButton = new JButton(saveIcon);
		saveButton.setPreferredSize(new Dimension(40, 40));
		exportIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/export_icon.jpg")));
		exportButton = new JButton(exportIcon);
		exportButton.setPreferredSize(new Dimension(40, 40));
		exitIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/bQuit.jpg")));
		exitButton = new JButton(exitIcon);
		exitButton.setPreferredSize(new Dimension(40, 40));
	}

	/**
	 * A Method to add listeners to the buttons
	 * 
	 * @param b -
	 *            an action listener for the buttons
	 */
	private void addListener(final ButtonsListener b)
	{
		newButton.addActionListener(b);
		openButton.addActionListener(b);
		saveButton.addActionListener(b);
		exportButton.addActionListener(b);
		exitButton.addActionListener(b);
	}

}
