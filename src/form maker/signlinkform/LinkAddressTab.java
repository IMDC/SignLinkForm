package signlinkform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

/**
 * A panel class to be a tab in the signlink frame it holds the GUI components to deal with the links
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: LinkAddressTab.java 53 2007-11-08 21:47:32Z martin $
 */
public class LinkAddressTab extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8638064346328537964L;
	private static final int	LABELSIZE			= 40;
	private JLabel				linkLabel;
	private JLabel				labelLabel;
	private JCheckBox			newBrowserCheck;
	private JTextField			labelBox;
	private JTextField			link;
	private final Box			mainPanel;
	private String				tempLinkLabel;
	private String				tempLabelLabel;
	private boolean				tempBrowserCheck;

	/**
	 * A default constructor that adds all the subcomponents to the panel
	 */
	protected LinkAddressTab()
	{
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		mainPanel = new Box(BoxLayout.Y_AXIS);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		createLabels();
		createTextFields();
		createCheckBox();
		addToPanel();
	}

	/**
	 * method to add all the created components to the panel
	 */
	private void addToPanel()
	{
		mainPanel.add(linkLabel);
		mainPanel.add(new JPanel().add(link));
		mainPanel.add(newBrowserCheck);
		mainPanel.add(labelLabel);
		mainPanel.add(new JPanel().add(labelBox));
		// this.add(Box.createVerticalStrut(5),BorderLayout.NORTH);
		// this.add(Box.createHorizontalStrut(20),BorderLayout.EAST);
		this.add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * method to create the checkbox
	 */
	private void createCheckBox()
	{
		newBrowserCheck = new JCheckBox("Open in new browser window");
	}

	/**
	 * Method to create the labels
	 */
	private void createLabels()
	{
		linkLabel = new JLabel("Link Address:");
		labelLabel = new JLabel("Label (optional):");
	}

	/**
	 * Method to create the textfields
	 */
	private void createTextFields()
	{

		link = new JTextField("http://", 20);
		link.setPreferredSize(new Dimension(515, 23));
		link.setMaximumSize(new Dimension(515, 23));
		labelBox = new JTextField(20);
		labelBox.setMaximumSize(new Dimension(515, 23));
		labelBox.setPreferredSize(new Dimension(515, 23));
		((AbstractDocument) labelBox.getDocument()).setDocumentFilter(new DocumentSizeFilter(LABELSIZE));

	}

	/**
	 * method to get whether the checkbox is checked
	 * 
	 * @return true if checked
	 */
	protected boolean getChecked()
	{
		return newBrowserCheck.isSelected();
	}

	/**
	 * method to get the text from the label textfield
	 * 
	 * @return the label text
	 */
	protected String getLabel()
	{
		return labelBox.getText();
	}

	/**
	 * method to get the text from the link textfield
	 * 
	 * @return the link text
	 */
	protected String getLink()
	{
		return link.getText();
	}

	protected void setChecked(final boolean check)
	{
		newBrowserCheck.setSelected(check);
	}

	protected void setLabel(final String label)
	{
		labelBox.setText(label);
	}

	protected void setLink(final String aLink)
	{
		link.setText(aLink);
	}

	/**
	 * method to clear all the fields from the panel
	 */
	protected void clear()
	{
		link.setText("http://");
		labelBox.setText("");
		newBrowserCheck.setSelected(false);
	}
//	/**
//	 * Method to undo the changes made to the imageTab when the cancel button is pressed
//	 */
//	protected void rollBackChanges()
//	{
//		this.setLabel(this.getTempLabelLabel());
//		this.setLink(this.getTempLinkLabel());
//		this.setChecked(this.isTempBrowserCheck());
//	}
//
//	/**
//	 * Method to commit the changes made to the imageTab when the done button is pressed
//	 */
//	protected void commitChanges()
//	{
//		this.setTempLabelLabel(this.getLabel());
//		this.setTempLinkLabel(this.getLink());
//		this.setTempBrowserCheck(this.getChecked());
//	}

	/**
	 * Getter method to return the value of tempLinkLabel
	 * 
	 * @return the tempLinkLabel
	 */
	protected String getTempLinkLabel()
	{
		return this.tempLinkLabel;
	}

	/**
	 * Setter method to set the value of tempLinkLabel
	 * 
	 * @param tempLinkLabel
	 *            the value to set
	 */
	protected void setTempLinkLabel(String tempLinkLabel)
	{
		this.tempLinkLabel = tempLinkLabel;
	}

	/**
	 * Getter method to return the value of tempLabelLabel
	 * 
	 * @return the tempLabelLabel
	 */
	protected String getTempLabelLabel()
	{
		return this.tempLabelLabel;
	}

	/**
	 * Setter method to set the value of tempLabelLabel
	 * 
	 * @param tempLabelLabel
	 *            the value to set
	 */
	protected void setTempLabelLabel(String tempLabelLabel)
	{
		this.tempLabelLabel = tempLabelLabel;
	}

	/**
	 * Getter method to return the value of tempBrowserCheck
	 * 
	 * @return the tempBrowserCheck
	 */
	protected boolean isTempBrowserCheck()
	{
		return this.tempBrowserCheck;
	}

	/**
	 * Setter method to set the value of tempBrowserCheck
	 * 
	 * @param tempBrowserCheck
	 *            the value to set
	 */
	protected void setTempBrowserCheck(boolean tempBrowserCheck)
	{
		this.tempBrowserCheck = tempBrowserCheck;
	}
}
