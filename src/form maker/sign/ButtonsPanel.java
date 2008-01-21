package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * A Panel class to hold the top buttons
 * 
 * @author Martin Gerdzhev
 * @version $Id: ButtonsPanel.java 92 2007-12-18 18:48:20Z laurel $
 */
public class ButtonsPanel extends JPanel
{
	private static final long	serialVersionUID	= -7127969993381321380L;
	
	private final SignlinkIcons images = SignlinkIcons.getInstance();
	
	private JButton				newButton;
	private JButton				openButton;
	private JButton				saveButton;
	private JButton				exportButton;
	private JButton				exitButton;

	public ButtonsPanel(ButtonsListener listen)
	{
		this.setLayout(new BorderLayout());
		addIcons();
		addListener(listen);
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
		newButton = new JButton(images.newImageIcon);
		newButton.setPreferredSize(new Dimension(40, 40));
		openButton = new JButton(images.openImageIcon);
		openButton.setPreferredSize(new Dimension(40, 40));
		saveButton = new JButton(images.saveImageIcon);
		saveButton.setPreferredSize(new Dimension(40, 40));
		exportButton = new JButton(images.exportImageIcon);
		exportButton.setPreferredSize(new Dimension(40, 40));
		exitButton = new JButton(images.quitImageIcon);
		exitButton.setPreferredSize(new Dimension(40, 40));
	}

	/**
	 * A Method to add listeners to the buttons
	 * 
	 * @param b -
	 *            an action listener for the buttons
	 */
	protected void addListener(final ButtonsListener b)
	{
		newButton.addActionListener(b);
		openButton.addActionListener(b);
		saveButton.addActionListener(b);
		exportButton.addActionListener(b);
		exitButton.addActionListener(b);
	}

}
