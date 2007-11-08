package sign;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A Panel class to hold the SignLink buttons
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: SignButtonsPanel.java 52 2007-11-08 03:56:17Z martin $
 */
public class SignButtonsPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8315662119657713534L;
	private JButton				newSignLinkButton;
	private ImageIcon			newSignLinkIcon;
	private JButton				editPropertiesButton;
	private ImageIcon			editPropertiesIcon;
	private JButton				previewButton;
	private ImageIcon			previewIcon;
	private JButton				deleteButton;
	private ImageIcon			deleteIcon;
	private final MenuFrame		menuFrame;

	/**
	 * A default constructor that adds all the buttons to the panel with their listeners
	 */
	public SignButtonsPanel(final MenuFrame mFrame)
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		menuFrame = mFrame;
		addIcons();
		addListener(new SignListener(menuFrame));
		addDescription();
		addButtons();
	}

	/**
	 * adds the buttons to the pane
	 */
	private void addButtons()
	{
		this.add(newSignLinkButton);
		this.add(Box.createVerticalStrut(5));
		this.add(editPropertiesButton);
		this.add(Box.createVerticalStrut(5));
		this.add(previewButton);
		this.add(Box.createVerticalStrut(5));
		this.add(deleteButton);
	}

	/**
	 * Adds a tooltip and an action command to the buttons
	 */
	private void addDescription()
	{
		newSignLinkButton.setToolTipText("New Signlink...");
		newSignLinkButton.setText("New Signlink...");
		newSignLinkButton.setActionCommand("new sign");

		editPropertiesButton.setToolTipText("Edit Properties");
		editPropertiesButton.setText("Edit Properties...");
		editPropertiesButton.setActionCommand("edit sign");

		previewButton.setToolTipText("Preview");
		previewButton.setText("Preview");
		previewButton.setActionCommand("preview sign");

		deleteButton.setToolTipText("Delete");
		deleteButton.setText("Delete");
		deleteButton.setActionCommand("clear sign");
	}

	/**
	 * A method to add Icons to the buttons and create the buttons
	 */
	private void addIcons()
	{
		newSignLinkIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/signlink(16x16)-(for editor).jpg")));
		newSignLinkButton = new JButton(newSignLinkIcon);
		newSignLinkButton.setPreferredSize(new Dimension(160, 30));
		newSignLinkButton.setMaximumSize(new Dimension(160, 30));
		newSignLinkButton.setMinimumSize(new Dimension(160, 30));
		newSignLinkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newSignLinkButton.setEnabled(false);

		editPropertiesIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/sEditSignlink.jpg")));
		editPropertiesButton = new JButton(editPropertiesIcon);
		editPropertiesButton.setPreferredSize(new Dimension(160, 30));
		editPropertiesButton.setMaximumSize(new Dimension(160, 30));
		editPropertiesButton.setMinimumSize(new Dimension(160, 30));
		editPropertiesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		editPropertiesButton.setEnabled(false);

		previewIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/sPlaySignlink.jpg")));
		previewButton = new JButton(previewIcon);
		previewButton.setPreferredSize(new Dimension(160, 30));
		previewButton.setMaximumSize(new Dimension(160, 30));
		previewButton.setMinimumSize(new Dimension(160, 30));
		previewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		previewButton.setEnabled(false);

		deleteIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/sDelete.jpg")));
		deleteButton = new JButton(deleteIcon);
		deleteButton.setPreferredSize(new Dimension(160, 30));
		deleteButton.setMaximumSize(new Dimension(160, 30));
		deleteButton.setMinimumSize(new Dimension(160, 30));
		deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteButton.setEnabled(false);
	}

	/**
	 * A Method to add listeners to the buttons
	 * 
	 * @param b -
	 *            an action listener for the buttons
	 */
	private void addListener(final SignListener b)
	{
		newSignLinkButton.addActionListener(b);
		editPropertiesButton.addActionListener(b);
		previewButton.addActionListener(b);
		deleteButton.addActionListener(b);
	}

	protected JButton getDeleteButton()
	{
		return deleteButton;
	}

	protected JButton getEditPropertiesButton()
	{
		return editPropertiesButton;
	}

	protected JButton getNewSignLinkButton()
	{
		return newSignLinkButton;
	}

	protected JButton getPreviewButton()
	{
		return previewButton;
	}
}
