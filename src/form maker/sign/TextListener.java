package sign;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * A listener class for text manipulation buttons and menu items
 * 
 * @author Martin Gerdzhev
 * @version $Id: TextListener.java 86 2007-12-04 19:18:33Z martin $
 */
public class TextListener implements ActionListener, CaretListener
{

	private TextPanel	tPanel;
	private MenuFrame	frame;

// private TextComponent tComponent;

	public TextListener(TextPanel textPanel)
	{
		tPanel = textPanel;
	}

	public void actionPerformed(ActionEvent event)
	{
		frame = (MenuFrame) tPanel.getRootPane().getParent();
		frame.setModified(true);

		if (event.getActionCommand().equals("cut"))
		{

			tPanel.getTextComponent().cut();
		}

		if (event.getActionCommand().equals("copy"))
		{
			tPanel.getTextComponent().copy();
		}

		if (event.getActionCommand().equals("paste"))
		{
			tPanel.getTextComponent().paste();
		}

		if (event.getActionCommand().equals("clear"))
		{
			tPanel.getTextComponent().setText("");
		}

		if (event.getActionCommand().equals("add link"))
		{
			tPanel.getTFrame().setVisible(true);
		}
		if (event.getActionCommand().equals("link added"))
		{
			tPanel.getTFrame().setVisible(false);
			int selected = tPanel.getTFrame().getSelected();
			int start = -1;
			System.out.println("caret Pos: " + tPanel.getTextComponent().getCaretPosition());
			start = tPanel.getTextComponent().findStartOfWord();
			if (tPanel.getAddTextLinkButton().getText().equals("Add Text Link"))
			{
				frame.getTPanel().incrementLinkCounter();
				String text = this.removeCurrentWord(start);
				String link = "";
				// add a id tag to specify which signlink has this url
				if (frame.getSComp().getSignList().get(selected).isNewWindow())
					link = "<a id=\"" + frame.getSComp().getSignList().get(selected).getMStart() + "\" href=\""
							+ frame.getSComp().getSignList().get(selected).getUrl() + "\" target=\"_blank\">" + text + "</a>";
				else
					link = "<a id=\"" + frame.getSComp().getSignList().get(selected).getMStart() + "\" href=\""
							+ frame.getSComp().getSignList().get(selected).getUrl() + "\">" + text + "</a>";
				this.insertLink(link, start);
				System.out.println("current word:" + tPanel.getTextComponent().getText());
			}
			else
			{
				String link = "";
				// add a id tag to specify which signlink has this url
				if (frame.getSComp().getSignList().get(selected).isNewWindow())
					link = "<a id=\"" + frame.getSComp().getSignList().get(selected).getMStart() + "\" href=\""
							+ frame.getSComp().getSignList().get(selected).getUrl() + "\" target=\"_blank\">";
				else
					link = "<a id=\"" + frame.getSComp().getSignList().get(selected).getMStart() + "\" href=\""
							+ frame.getSComp().getSignList().get(selected).getUrl() + "\">";
				this.replaceLink(link, start);
				System.out.println("current word:" + tPanel.getTextComponent().getText());
			}
		}
		if (event.getActionCommand().equals("clear link"))
		{
			int start = tPanel.getTextComponent().getCaretPosition();
			if (this.removeLink(start))
				frame.getTPanel().decrementLinkCounter();
		}
		if (event.getActionCommand().equals("check"))
		{
			JCheckBox box = (JCheckBox) event.getSource();

			if (box.isSelected())
				tPanel.showOptionalPanel(true);
			else
				tPanel.showOptionalPanel(false);
		}
		if (event.getActionCommand().equals("help"))
		{
			if (frame.getHelp() == null)
			{
				frame.setHelp(new HelpFrame(HelpFrame.A, frame.getHelpLocation()));
			}
			else
			{
				frame.getHelp().dispose();
				frame.setHelp(new HelpFrame(HelpFrame.A, frame.getHelpLocation()));
			}
		}
	}

	/**
	 * Inserts a hyperlink at the specified position
	 * 
	 * @param link -
	 *            the hyperlink to be inserted
	 * @param pos -
	 *            the position in the document to insert the hyperlink
	 */
	private void insertLink(String link, int pos)
	{
		System.out.println("pos:" + pos);
		try
		{
			if (pos == 1)
			{
				((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).insertAfterStart(((HTMLDocument) tPanel.getTextComponent()
						.getStyledDocument()).getElement(((HTMLDocument) tPanel.getTextComponent().getStyledDocument())
						.getDefaultRootElement(), StyleConstants.NameAttribute, HTML.Tag.P), link);
			}
			else
			{
				((HTMLEditorKit) tPanel.getTextComponent().getEditorKit()).insertHTML((HTMLDocument) tPanel.getTextComponent()
						.getStyledDocument(), pos, link + " ", 0, 0, HTML.Tag.A);
			}
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Removes the hyperlink at the specified position
	 * 
	 * @param pos -
	 *            the position in the document to remove the link from
	 */
	private boolean removeLink(int pos)
	{
		try
		{
			Element elem = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).getCharacterElement(pos);
			HTMLDocument.Iterator it = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).getIterator(HTML.Tag.A);
			while (it.isValid())
			{
				if (it.getEndOffset() >= pos)
				{
					break;
				}
				it.next();
			}
			if (!it.isValid())
				return false;
			System.out.println("elemStart:" + elem.getStartOffset() + " elemEnd:" + elem.getEndOffset());
			System.out.println("iterStart:" + it.getStartOffset() + " iterEnd:" + it.getEndOffset());
			if (it.getStartOffset() == elem.getStartOffset() && it.getEndOffset() == elem.getEndOffset())
			{
				AttributeSet attribute = it.getAttributes();
				System.out.println("Attribute id: " + attribute.getAttribute(HTML.Attribute.ID).toString());
				HTMLDocument doc = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument());
				String text = doc.getText(elem.getStartOffset(), elem.getEndOffset() - elem.getStartOffset());
				System.out.println(text);
				((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).replace(elem.getStartOffset(), elem.getEndOffset()
						- elem.getStartOffset(), text, null);
				return true;
			}
			return false;
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	protected boolean replaceLink(String html, int pos)
	{

		try
		{
			// ((HTMLDocument) tComponent.getStyledDocument()).replace(pos, text.length(), text, null);
			Element elem = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).getCharacterElement(pos);
			HTMLDocument.Iterator it = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).getIterator(HTML.Tag.A);
			while (it.isValid())
			{
				if (it.getEndOffset() >= pos)
				{
					break;
				}
				it.next();
			}
			if (!it.isValid())
				return false;
			System.out.println("elemStart:" + elem.getStartOffset() + " elemEnd:" + elem.getEndOffset());
			System.out.println("iterStart:" + it.getStartOffset() + " iterEnd:" + it.getEndOffset());
			if (it.getStartOffset() == elem.getStartOffset() && it.getEndOffset() == elem.getEndOffset())
			{
				AttributeSet attribute = it.getAttributes();
				System.out.println("Attribute id: " + attribute.getAttribute(HTML.Attribute.ID).toString());
				HTMLDocument doc = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument());
				String text = doc.getText(elem.getStartOffset(), elem.getEndOffset() - elem.getStartOffset());
				System.out.println(text);
				SimpleAttributeSet atr = new SimpleAttributeSet();
				atr.addAttribute(HTML.Tag.A, Boolean.TRUE);
				((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).replace(elem.getStartOffset(), elem.getEndOffset()
						- elem.getStartOffset(), "", null);
				this.insertLink(html + text + "</a>", it.getStartOffset());
				return true;
			}
			return false;
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Removes the word at the starting position specified by start
	 * 
	 * @param start -
	 *            the beginning of the word to remove
	 * @return the removed word
	 */
	private String removeCurrentWord(int start)
	{
		String text = "";
		try
		{
			int end = tPanel.getTextComponent().findEndOfWord();
			text = tPanel.getTextComponent().getDocument().getText(start, end - start);
			tPanel.getTextComponent().getDocument().remove(start, end - start);
			System.out.println("text: " + text);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
			return null;
		}
		return text;
	}

	/**
	 * This method is used for queing the signlink when the caret is on the hyperlink
	 */
	public void caretUpdate(CaretEvent e)
	{
		int id = -1;
		frame = (MenuFrame) tPanel.getRootPane().getParent();
		HTMLDocument.Iterator it = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).getIterator(HTML.Tag.A);
		int pos = e.getDot();
		while (it.isValid())
		{
			if (it.getEndOffset() >= pos)
			{
				break;
			}
			else
				it.next();
		}
		if (!it.isValid())
		{
			tPanel.getAddTextLinkButton().setText("Add Text Link");
			return;
		}
		if (it.getStartOffset() <= pos && it.getEndOffset() >= pos)
		{
			tPanel.getAddTextLinkButton().setText("Edit Link");
			AttributeSet attribute = it.getAttributes();
			id = Integer.parseInt(attribute.getAttribute(HTML.Attribute.ID).toString());
			if (id == -1)
				return;
			for (int i = 0; i < frame.getSComp().getSignList().size(); i++)
			{
				if (id == frame.getSComp().getSignList().get(i).getMStart())
				{
					frame.getSComp().deSelect();
					frame.getSComp().select(i);
					frame.getVComponent().getVideoButtonsPanel().getVSlide().setValue(id);
					frame.getVComponent().getVideoPanel().setTimeMiliseconds(id);
					frame.getVComponent().getVideoButtonsPanel().getVSlide().revalidate();

					frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(false);
					frame.getSComp().getSButtonsPanel().getPreviewButton().setEnabled(true);
					frame.getSComp().getSButtonsPanel().getEditPropertiesButton().setEnabled(true);
					frame.getSComp().getSButtonsPanel().getDeleteButton().setEnabled(true);
					frame.getSignLinks().getMenuComponent(1).setEnabled(true);
					frame.getSignLinks().getMenuComponent(2).setEnabled(true);

					String min = frame.getVComponent().getVideoPanel().getMin();
					String sec = frame.getVComponent().getVideoPanel().getSec();
					String mil = frame.getVComponent().getVideoPanel().getMil();
					frame.getVComponent().getVideoButtonsPanel().getVText().setText(min + ":" + sec + ":" + mil);
					return;
				}
			}
		}
		else
		{
			tPanel.getAddTextLinkButton().setText("Add Text Link");
			return;
		}
	}

	/**
	 * Method to remove the links with a certain id tag
	 * 
	 * @param id
	 *            the id of the links to remove
	 */
	public void removeLinks(int id)
	{
		// TODO needs more testing, but appears to be working
// tComponent = tPanel.getTextComponent();
		HTMLDocument.Iterator it = ((HTMLDocument) tPanel.getTextComponent().getStyledDocument()).getIterator(HTML.Tag.A);
		while (it.isValid())
		{
			AttributeSet attribute = it.getAttributes();
			int currentId = Integer.parseInt(attribute.getAttribute(HTML.Attribute.ID).toString());
			if (currentId == id)
			{
				removeLink(it.getStartOffset());
			}
			it.next();
		}
	}
}
