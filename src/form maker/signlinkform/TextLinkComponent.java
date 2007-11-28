/**
 * 
 */
package signlinkform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Martin Gerdzhev
 * 
 * @version $Id: TextLinkComponent.java 52 2007-11-08 03:56:17Z martin $
 */
public class TextLinkComponent extends JComponent
{
	private class SignLinkPanel extends JPanel
	{
		class SListener implements MouseListener
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
			 */
			public void mouseClicked(final MouseEvent arg0)
			{
				SignLinkPanel.this.deSelect();
				SignLinkPanel.this.select(panelList.indexOf(arg0.getSource()));
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
			 */
			public void mouseEntered(final MouseEvent arg0)
			{
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
			 */
			public void mouseExited(final MouseEvent arg0)
			{
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
			 */
			public void mousePressed(final MouseEvent arg0)
			{
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
			 */
			public void mouseReleased(final MouseEvent arg0)
			{
			}

		}

		/**
		 * 
		 */
		private static final long	serialVersionUID	= -2485702742231086409L;
		private ArrayList<JPanel>	panelList;

		private final SListener		listen;

		public SignLinkPanel()
		{
			this.setPreferredSize(new Dimension(410, 10));
			this.setBorder(BorderFactory.createRaisedBevelBorder());
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			panelList = new ArrayList<JPanel>();
			listen = new SListener();
		}

		/**
		 * method to deselect all the signLinks
		 */
		protected void deSelect()
		{
			// deselect only the component that holds the image
			for (int i = 0; i < panelList.size(); i++)
				panelList.get(i).getComponent(1).setBackground(defaultBkgColor);
			selected = -1;
		}

		private void populateLinks()
		{
			panelList = new ArrayList<JPanel>();
			for (int i = 0; i < signList.size(); i++)
			{
				final JLabel previewLabel = new JLabel(signList.get(i).getPreview());
				final JLabel labelLabel = new JLabel("<html><p style=\"text-align:center\">" + signList.get(i).getLabel() + "</p></html>");
				final JMultilineLabel linkLabel = new JMultilineLabel();
				linkLabel.setText(signList.get(i).getUrl());
				linkLabel.setMaxWidth(100);
				linkLabel.setMaximumSize(new Dimension(100, 30));
				final JPanel west = new JPanel();
				final JPanel center = new JPanel();
				center.setLayout(new BorderLayout());
				final JPanel linkPanel = new JPanel();
				double size = 0;
				linkPanel.setLayout(new BorderLayout());
				linkPanel.setMaximumSize(new Dimension(410, 85));
				center.add(linkLabel, BorderLayout.CENTER);
				west.add(previewLabel, BorderLayout.CENTER);
				center.add(labelLabel, BorderLayout.NORTH);
				linkPanel.add(center, BorderLayout.CENTER);
				linkPanel.add(west, BorderLayout.WEST);
				linkPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				linkPanel.addMouseListener(listen);
				defaultBkgColor = linkPanel.getBackground();
				size = linkPanel.getMinimumSize().getHeight();
				panelList.add(linkPanel);
				this.setPreferredSize(new Dimension(this.getPreferredSize().width, (int) (5 + signList.size() * (size + 5))));
				this.updateLinks();
			}
		}

		/**
		 * method to select a SignLink
		 * 
		 * @param index
		 */
		protected void select(final int index)
		{
			selected = index;
			// select only the component that holds the image
			panelList.get(selected).getComponent(1).setBackground(Color.RED);
		}

		protected void updateLinks()
		{
			this.removeAll();
			for (int i = 0; i < panelList.size(); i++)
			{
				this.add(panelList.get(i));
			}
			this.revalidate();
			this.repaint();
		}
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1917068803769090270L;
	private final JScrollPane	scroll;
	private int					selected			= -1;
	private Color				defaultBkgColor;
	private ArrayList<Sign>	signList;
	private final MenuFrame		frame;

	private final SignLinkPanel	sPanel;

	public TextLinkComponent(final MenuFrame mFrame)
	{
		frame = mFrame;
		sPanel = new SignLinkPanel();
		signList = new ArrayList<Sign>();
		scroll = new JScrollPane(sPanel);
		scroll.setPreferredSize(new Dimension(420, 260));
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
		this.setPreferredSize(new Dimension(450, 250));
		this.add(scroll);

	}

	public Color getDefaultBkgColor()
	{
		return defaultBkgColor;
	}

	public int getSelected()
	{
		return selected;
	}

	public Sign getSign(final int index)
	{
		return signList.get(index);
	}

	public ArrayList<Sign> getSignList()
	{
		return signList;
	}

	public void setDefaultBkgColor(final Color defaultBkgColor)
	{
		this.defaultBkgColor = defaultBkgColor;
	}

	public void setSelected(final int selected)
	{
		this.selected = selected;
	}

	public void setSignList(final ArrayList<Sign> signList)
	{
		this.signList = signList;
	}

	public void setVisible(final boolean flag)
	{
		super.setVisible(flag);
		if (flag)
		{
			signList = frame.getSComp().getSignList();
			sPanel.populateLinks();
			this.revalidate();
		}
	}
}
