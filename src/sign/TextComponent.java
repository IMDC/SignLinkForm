package sign;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * A class that is to be the text pane for the optional text and links
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: TextComponent.java 52 2007-11-08 03:56:17Z martin $
 */
public class TextComponent extends JTextPane
{

	class FollowLinkListener implements KeyListener
	{

		public void keyPressed(final KeyEvent arg0)
		{
			if (arg0.isControlDown())
			{
				TextComponent.this.setEditable(false);
			}
			if (!arg0.isControlDown())
			{
				TextComponent.this.setEditable(true);
			}
		}

		public void keyReleased(final KeyEvent arg0)
		{
			if (arg0.isControlDown())
			{
				TextComponent.this.setEditable(false);
				if (arg0.getKeyCode() == 67) // Control-C
				{
					System.out.println("COPY");
					TextComponent.this.copy();
				}
				else if (arg0.getKeyCode() == 86) // Control-V
				{
					System.out.println("PASTE");
					TextComponent.this.setEditable(true);
					TextComponent.this.paste();
					TextComponent.this.setEditable(false);
				}
				else if (arg0.getKeyCode() == 88) // Control-X
				{
					System.out.println("CUT");
					TextComponent.this.cut();
				}
			}
			if (!arg0.isControlDown())
				TextComponent.this.setEditable(true);
		}

		public void keyTyped(final KeyEvent arg0)
		{
			((MenuFrame)TextComponent.this.getRootPane().getParent()).setModified(true);
		}
	}

	class HyperListen implements HyperlinkListener
	{

		public void hyperlinkUpdate(final HyperlinkEvent arg0)
		{
			if (arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
			{
				System.out.println(arg0.getURL().toString());
				BareBonesBrowserLaunch.openURL(arg0.getURL().toString());
			}
		}
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5253391596755169611L;
	private final Clipboard		clip;

	public TextComponent()
	{
		clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		final KeyListener keyListen = new FollowLinkListener();
		this.setStyledDocument(new HTMLDocument());
		final HyperListen listen = new HyperListen();
		this.addHyperlinkListener(listen);
		this.setContentType("text/html");
		this.setEditorKitForContentType("text/html", new HTMLEditorKit());
		this.setText("<pre></pre>");
		this.addKeyListener(keyListen);
	}

	public TextComponent(final String text)
	{
		clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		final KeyListener keyListen = new FollowLinkListener();
		this.setStyledDocument(new HTMLDocument());
		final HyperListen listen = new HyperListen();
		this.addHyperlinkListener(listen);
		this.setContentType("text/html");
		this.setEditorKitForContentType("text/html", new HTMLEditorKit());
		//FIXME fix it!
		this.setText("<pre></pre>"); // this removes the /n in the text
		/*try
		{
			((HTMLDocument) this.getStyledDocument()).insertString(0, text, null);
		}
		catch (BadLocationException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		try
		{
			((HTMLDocument) this.getStyledDocument()).insertAfterStart(((HTMLDocument) this.getStyledDocument()).getElement(
					((HTMLDocument) this.getStyledDocument()).getDefaultRootElement(), StyleConstants.NameAttribute, HTML.Tag.PRE), text);
		}
		catch (BadLocationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.addKeyListener(keyListen);
	}

	public String getExportText()
	{
		//this.getText().trim();
		System.out.println(this.getText());
		if (this.getText().indexOf("<pre>")!=-1)
			return (this.getText().substring(this.getText().indexOf("<pre>") + 5, this.getText().lastIndexOf("</pre>")));
		else
			return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.text.JTextComponent#copy()
	 */
	@Override
	public void copy()
	{
		System.out.println(TextComponent.this.getSelectedText());
		clip.setContents(new StringSelection(TextComponent.this.getSelectedText()), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.text.JTextComponent#cut()
	 */
	@Override
	public void cut()
	{
		System.out.println(TextComponent.this.getSelectedText());
		if (TextComponent.this.getSelectedText() == null)
			clip.setContents(new StringSelection(TextComponent.this.getSelectedText()), null);
		try
		{
			TextComponent.this.getDocument().remove(TextComponent.this.getSelectionStart(),
					TextComponent.this.getSelectionEnd() - TextComponent.this.getSelectionStart());
		}
		catch (final BadLocationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * finds the ending position of the current word where the caret is located
	 * 
	 * @return the ending position
	 */
	protected int findEndOfWord()
	{
		int end = findStartOfWord();
		try
		{
			while (!Character.isWhitespace(this.getDocument().getText(end, 1).charAt(0)) && end < this.getDocument().getLength())
			{
				end++;
			}
			System.out.println("end: " + end);
		}
		catch (final BadLocationException e)
		{
			e.printStackTrace();
			return -1;
		}
		this.setCaretPosition(end);
		return end;
	}

	/**
	 * finds the starting position of the current word where the caret is located
	 * 
	 * @return the starting position
	 */
	protected int findStartOfWord()
	{
		int start = this.getCaretPosition();
		try
		{
			while (start > 0 && !Character.isWhitespace(this.getDocument().getText(start - 1, 1).charAt(0)))
			{
				start--;
				this.setCaretPosition(start);
			}
			System.out.println("start: " + start);
		}
		catch (final BadLocationException e)
		{
			e.printStackTrace();
			return -1;
		}
		return start;
	}
}
