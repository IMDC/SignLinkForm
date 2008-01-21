/**
 * 
 */
package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import quicktime.std.StdQTException;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: TextLinkFrame.java 94 2007-12-18 21:31:47Z martin $
 */
public class TextLinkFrame extends JFrame
{
	
	private SignlinkIcons images = SignlinkIcons.getInstance();

	class BListener extends WindowAdapter implements ActionListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(final ActionEvent arg0)
		{
			if (arg0.getActionCommand().equals("close"))
			{
				if (help != null)
					help.dispose();
				TextLinkFrame.this.close();
			}
			if (arg0.getActionCommand().equals("help"))
			{
				if (help != null)
					help.dispose();
				help = new HelpFrame(HelpFrame.A10, new Point((int) TextLinkFrame.this.getLocationOnScreen().getX() + WIDTH,
						(int) TextLinkFrame.this.getLocationOnScreen().getY()));
			}

		}

		public void windowClosed(final WindowEvent arg0)
		{
			TextLinkFrame.this.close();
		}
	}
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 2275195538624681945L;
	private final MenuFrame			frame;
	private JPanel					south;
	private final BListener			listen;
	private boolean					removing;
	private final TextLinkComponent	TLComponent;
	private HelpFrame				help;
	
	private TextListener			textListen;

	public TextLinkFrame(final MenuFrame mFrame, final TextListener tListen)
	{
		super("SignLink List");
		frame = mFrame;
		this.setSize(new Dimension(475, 390));
		this.setIconImage(images.signEdIcon16);
		this.setLayout(new BorderLayout());
		listen = new BListener();
		textListen = tListen;
		this.addWindowListener(listen);
		TLComponent = new TextLinkComponent(frame);
		
		final JButton helpButton = new JButton(images.helpImageIcon);
		helpButton.setActionCommand("help");
		helpButton.addActionListener(listen);
		helpButton.setPreferredSize(new Dimension(22, 22));
		final JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		helpPanel.add(helpButton);
		helpButton.setEnabled(HelpFrame.isHelpEnabled());
		
		this.add(TLComponent, BorderLayout.CENTER);
		this.add(helpPanel,BorderLayout.WEST);
		addButtons();
		this.setVisible(false);
		this.setResizable(false);
	}

	/**
	 * Method to add the done and cancel buttons to the bottom of the frame
	 */
	private void addButtons()
	{
		south = new JPanel();
		final JButton closeButton = new JButton("Close", images.cancelIcon);
		final JButton doneButton = new JButton("Done", images.doneIcon);
		closeButton.setActionCommand("close");
		doneButton.setActionCommand("link added");
		closeButton.addActionListener(listen);
		doneButton.addActionListener(textListen);
		south.add(doneButton);
		south.add(closeButton);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(south, BorderLayout.SOUTH);

	}

	/**
	 * invoked to close the window
	 */
	private void close()
	{
		TextLinkFrame.this.setVisible(false);
		try
		{
			this.frame.getVComponent().getVideoPanel().getMovie().setActive(true);
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.frame.setEnabled(true);
	}

	public int getSelected()
	{
		return this.TLComponent.getSelected();
	}

	/**
	 * Getter method to return the value of
	 * 
	 * @return the textListen
	 */
	public TextListener getTextListen()
	{
		return this.textListen;
	}

	public boolean isRemoving()
	{
		return removing;
	}

	public void setRemoving(final boolean removing)
	{
		this.removing = removing;
	}

	/**
	 * Setter method to set the value of
	 * 
	 * @param textListen
	 *            the textListen to set
	 */
	public void setTextListen(final TextListener textListen)
	{
		this.textListen = textListen;
	}

	public void setVisible(final boolean flag)
	{
		super.setVisible(flag);
		TLComponent.setVisible(flag);
	}
}
