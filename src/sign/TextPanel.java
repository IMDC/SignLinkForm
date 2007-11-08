package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TextPanel.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class TextPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8816004196947731916L;
	private TextComponent		textComp;
	private JButton				addTextLinkButton;
	private JButton				clearTextLinkButton;
	private JCheckBox			checkShowText;
	private TextListener		listen;
	private JScrollPane			scrollPane;
	private final JPanel		optional;
	private TextLinkFrame		tFrame;
	private int					linkCounter;

	protected TextPanel(final MenuFrame frame)
	{
		listen = new TextListener(this);
		optional = new JPanel();
		optional.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new EtchedBorder(), "Text Editor"));
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(390, 370));
		addCheckBox();
		addTextComponent();
		addButtons();
		showOptionalPanel(false);
		this.add(optional, BorderLayout.CENTER);
		tFrame = new TextLinkFrame(frame, listen);
		linkCounter = 0;
	}

	protected TextPanel(final MenuFrame frame, final String text) // Called when importing an xml file
	{
		listen = new TextListener(this);
		optional = new JPanel();
		optional.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new EtchedBorder(), "Text Editor"));
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(390, 370));
		addCheckBox();
		addTextComponent(text);
		addButtons();
		if (text.length() > 0)
		{
			checkShowText.setSelected(true);
			showOptionalPanel(true);
		}
		else
			showOptionalPanel(false);
		this.add(optional, BorderLayout.CENTER);
		tFrame = new TextLinkFrame(frame, listen);
		linkCounter = 0;
	}

	private void addButtons()
	{
		addTextLinkButton = new JButton("Add Text Link");
		addTextLinkButton.setActionCommand("add link");
		addTextLinkButton.addActionListener(listen);
		addTextLinkButton.setEnabled(false);
		clearTextLinkButton = new JButton("Clear Text Link");
		clearTextLinkButton.setActionCommand("clear link");
		clearTextLinkButton.addActionListener(listen);
		clearTextLinkButton.setEnabled(false);

		final JButton helpButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/sHelp.jpg"))));
		helpButton.setActionCommand("help");
		helpButton.addActionListener(listen);
		helpButton.setPreferredSize(new Dimension(22, 22));

		final JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		south.add(addTextLinkButton);
		south.add(clearTextLinkButton);
		south.add(Box.createRigidArea(new Dimension(130, 24)));
		south.add(helpButton);
		optional.add(south, BorderLayout.SOUTH);
	}

	private void addCheckBox()
	{
		checkShowText = new JCheckBox("Show Optional Text Editor");
		checkShowText.setActionCommand("check");
		checkShowText.addActionListener(listen);
		this.add(checkShowText, BorderLayout.NORTH);
	}

	private void addTextComponent()
	{
		textComp = new TextComponent();
		textComp.addCaretListener(listen);
		scrollPane = new JScrollPane(textComp);
		optional.add(scrollPane, BorderLayout.CENTER);
	}

	private void addTextComponent(final String text)
	{
		textComp = new TextComponent(text);
		textComp.addCaretListener(listen);
		scrollPane = new JScrollPane(textComp);
		optional.add(scrollPane, BorderLayout.CENTER);
	}

	protected void decrementLinkCounter()
	{
		linkCounter--;
		if (linkCounter <= 0)
		{
			linkCounter = 0;
			this.getClearTextLinkButton().setEnabled(false);
		}
	}

	/**
	 * Getter method to return the value of
	 * 
	 * @return the addTextLinkButton
	 */
	public JButton getAddTextLinkButton()
	{
		return this.addTextLinkButton;
	}

	/**
	 * Getter method to return the value of
	 * 
	 * @return the clearTextLinkButton
	 */
	public JButton getClearTextLinkButton()
	{
		return this.clearTextLinkButton;
	}

	/**
	 * Getter method to return the textListener
	 * 
	 * @return the listen
	 */
	public TextListener getListen()
	{
		return this.listen;
	}

	protected TextComponent getTextComponent()
	{
		return textComp;
	}

	public TextLinkFrame getTFrame()
	{
		return tFrame;
	}

	protected void incrementLinkCounter()
	{
		linkCounter++;
		if (linkCounter > 0)
		{
			this.getClearTextLinkButton().setEnabled(true);
		}
	}

	/**
	 * Setter method to set the value of
	 * 
	 * @param addTextLinkButton
	 *            the addTextLinkButton to set
	 */
	public void setAddTextLinkButton(final JButton addTextLinkButton)
	{
		this.addTextLinkButton = addTextLinkButton;
	}

	/**
	 * Setter method to set the value of
	 * 
	 * @param clearTextLinkButton
	 *            the clearTextLinkButton to set
	 */
	public void setClearTextLinkButton(final JButton clearTextLinkButton)
	{
		this.clearTextLinkButton = clearTextLinkButton;
	}

	/**
	 * Setter method to set the TextListener
	 * 
	 * @param listen
	 *            the listen to set
	 */
	public void setListen(final TextListener listen)
	{
		this.listen = listen;
	}

	public void setTFrame(final TextLinkFrame frame)
	{
		tFrame = frame;
	}

	protected void showOptionalPanel(final boolean condition)
	{
		if (condition)
		{
			addTextLinkButton.setVisible(true);
			clearTextLinkButton.setVisible(true);
			scrollPane.setVisible(true);
		}
		else
		{
			addTextLinkButton.setVisible(false);
			clearTextLinkButton.setVisible(false);
			scrollPane.setVisible(false);
		}
	}

	/**
	 * Getter method to return the value of linkCounter
	 * 
	 * @return the linkCounter
	 */
	protected int getLinkCounter()
	{
		return this.linkCounter;
	}

	/**
	 * method to update any existing textlinks with id = id to newID and new link if the link has changed
	 * 
	 * @param id
	 *            the old id number
	 * @param newID
	 *            the new id number
	 */
	protected void updateLink(int id, int newID)
	{
		HTMLDocument.Iterator it = ((HTMLDocument) textComp.getStyledDocument()).getIterator(HTML.Tag.A);
		while (it.isValid())
		{
			AttributeSet attribute = it.getAttributes();
			int val = Integer.parseInt(attribute.getAttribute(HTML.Attribute.ID).toString());
			if (val == id)
			{
				break;
			}
			it.next();
		}
		if (!it.isValid())
			return;
		System.out.println("iterStart:" + it.getStartOffset() + " iterEnd:" + it.getEndOffset());

		MenuFrame frame = (MenuFrame) this.getRootPane().getParent();
		int selected = frame.getSComp().getSelected();
		String html = "";
		// the text around this link and the closing </a> is added inside the listen.replaceLink()
		if (frame.getSComp().getSignList().get(selected).isNewWindow())
			html = "<a id=\"" + frame.getSComp().getSignList().get(selected).getMStart() + "\" href=\""
					+ frame.getSComp().getSignList().get(selected).getUrl() + "\" target=\"_blank\">";
		else
			html = "<a id=\"" + frame.getSComp().getSignList().get(selected).getMStart() + "\" href=\""
					+ frame.getSComp().getSignList().get(selected).getUrl() + "\">";
		listen.replaceLink(html, it.getStartOffset());
	}
}
