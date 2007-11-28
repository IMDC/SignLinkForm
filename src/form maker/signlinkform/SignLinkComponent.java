package signlinkform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: SignLinkComponent.java 65 2007-11-22 16:49:31Z martin $
 *
 */
public class SignLinkComponent extends JComponent
{
	private class HelpListener implements ActionListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(final ActionEvent arg0)
		{
			if (frame.getHelp() == null)
			{
				frame.setHelp(new HelpFrame(HelpFrame.WELCOME, frame.getHelpLocation()));
			}
			else
			{
				frame.getHelp().dispose();
				frame.setHelp(new HelpFrame(HelpFrame.WELCOME, frame.getHelpLocation()));
			}

		}

	}

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
				SignLinkPanel.this.jumpToSlide(panelList.indexOf(arg0.getSource()));
				// enable and disable buttons accordingly
				frame.getSComp().getSButtonsPanel().getPreviewButton().setEnabled(true);
				frame.getSComp().getSButtonsPanel().getEditPropertiesButton().setEnabled(true);
				frame.getSComp().getSButtonsPanel().getDeleteButton().setEnabled(true);
				frame.getSignLinks().getMenuComponent(1).setEnabled(true);
				frame.getSignLinks().getMenuComponent(2).setEnabled(true);
				frame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(false);
				frame.getSignLinks().getMenuComponent(0).setEnabled(false);
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
		private static final long		serialVersionUID	= 6596167148442606982L;
		private final ArrayList<JPanel>	panelList;
		private final SListener			listen;

		public SignLinkPanel()
		{
			this.setPreferredSize(new Dimension(10, 100));
			this.setBorder(BorderFactory.createRaisedBevelBorder());
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelList = new ArrayList<JPanel>();
			listen = new SListener();
		}

		protected void addSign(final Sign aSign, final int location, boolean fromXML)
		{
			final JLabel previewLabel = new JLabel(aSign.getPreview());
			final JLabel labelLabel = new JLabel("<html><p style=\"text-align:center\">" + aSign.getLabel() + "</p></html>",
					SwingConstants.CENTER);
			labelLabel.setPreferredSize(new Dimension(150, 30));
			linkLabel.setText(aSign.getUrl());
			final JPanel linkPanel = new JPanel();
			JPanel previewPanel = new JPanel(new BorderLayout());
			JPanel bla = new JPanel();
			bla.add(previewLabel);
			previewPanel.add(bla, BorderLayout.CENTER);
			previewPanel.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
			previewPanel.add(Box.createHorizontalStrut(20), BorderLayout.WEST);

			double size = 0;
			linkPanel.setLayout(new BorderLayout());
			linkPanel.add(previewPanel, BorderLayout.CENTER);
			linkPanel.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
			linkPanel.add(labelLabel, BorderLayout.SOUTH);
			linkPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			linkPanel.addMouseListener(listen);
			defaultBkgColor = linkPanel.getBackground();
			size = linkPanel.getPreferredSize().getWidth();
		//	System.out.println(size);
			if (location == -1)
			{
				panelList.add(linkPanel);
			}
			else
			{
				panelList.add(location, linkPanel);
			}
			this.setPreferredSize(new Dimension((int) (this.getPreferredSize().width + size + 5), this.getPreferredSize().height));
			this.updateLinks();
			if (!fromXML)
			{
				this.deSelect();
				this.select(panelList.indexOf(linkPanel));
			}
		}

		/**
		 * method to deselect all the signLinks
		 */
		protected void deSelect()
		{
			for (int i = 0; i < panelList.size(); i++)
			{
				((JPanel) panelList.get(i).getComponent(0)).getComponent(0).setBackground(defaultBkgColor);
			}
			selected = -1;
			linkLabel.setText(" ");
			final SignSlider slide = frame.getVComponent().getVideoButtonsPanel().getVSlide();
			slide.setActiveStart(-1);
			slide.setActiveEnd(-1);
			frame.getVComponent().getImageLabel().setVisible(false);

		}

		protected void jumpToSlide(final int index)
		{
			final Sign sign = getSign(index);
			// jump to start time of the selected link in main window
			final SignSlider slide = frame.getVComponent().getVideoButtonsPanel().getVSlide();
			// changed from setvaluefromcoord to setvalue
			slide.setValue(sign.getMStart());
			frame.getVComponent().getVideoPanel().setTimeMiliseconds(slide.getValue());
			slide.getChangeListeners()[0].stateChanged(null);
		}

		protected void removeSign(final int position)
		{
			final double size = panelList.remove(position).getPreferredSize().getWidth();

			this.remove(position);
			this.setPreferredSize(new Dimension((int) (this.getPreferredSize().width - size - 5), this.getPreferredSize().height));
			SignLinkPanel.this.deSelect(); // update the selection
			updateLinks();
		}

		/**
		 * method to select a SignLink
		 * 
		 * @param index
		 */
		protected void select(final int index)
		{
			SignLinkComponent.this.setSelected(index);
			((JPanel) panelList.get(selected).getComponent(0)).getComponent(0).setBackground(Color.RED);
			final Sign sign = getSign(selected);
			linkLabel.setText(sign.getUrl());
			final SignSlider slide = frame.getVComponent().getVideoButtonsPanel().getVSlide();
			// and color it red
			// added getCoordfromValue
			slide.setActiveStart(slide.getCoordfromValue(sign.getMStart()));
			slide.setActiveEnd(slide.getCoordfromValue(sign.getMEnd()));
			frame.getVComponent().getImageLabel().setVisible(true);

		}

		protected void updateLink(final Sign aSign)
		{
			SignLinkComponent.this.removeSign(selected);
			SignLinkComponent.this.addSign(aSign);
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
	private static final long		serialVersionUID	= 543746612032196055L;
	private final SignLinkPanel		sPanel;
	private final SignButtonsPanel	sButtonsPanel;
	private ArrayList<Sign>		signList;
	private final MenuFrame			frame;
	private final JScrollPane		scroll;
	private int						selected			= -1;
	private Color					defaultBkgColor;
	private final JLabel			linkLabel;
	private JPanel					helpButtonPanel;

	public SignLinkComponent(final MenuFrame mFrame)
	{
		sPanel = new SignLinkPanel();
		sButtonsPanel = new SignButtonsPanel(mFrame);
		signList = new ArrayList<Sign>();
		frame = mFrame;
		scroll = new JScrollPane(sPanel);
		scroll.setPreferredSize(new Dimension(590, 160));
		linkLabel = new JLabel(" ");
		this.setLayout(new BorderLayout(10, 5));
		this.setPreferredSize(new Dimension(10, 190));
		this.setBorder(new TitledBorder(new EtchedBorder(), "Sign Link"));
		this.add(linkLabel, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.WEST);
		this.add(sButtonsPanel, BorderLayout.CENTER);
		addHelpButton();
	}

	public SignLinkComponent(final MenuFrame mFrame, final ArrayList<Sign> signs)
	{
		sPanel = new SignLinkPanel();
		sButtonsPanel = new SignButtonsPanel(mFrame);
		signList = signs;
		linkLabel = new JLabel(" ");
		for (int i = 0; i < signs.size(); i++)
			sPanel.addSign(signs.get(i), -1, true);
		frame = mFrame;
		scroll = new JScrollPane(sPanel);
		scroll.setPreferredSize(new Dimension(590, 160));
		this.setLayout(new BorderLayout(10, 5));
		this.setPreferredSize(new Dimension(10, 190));
		this.setBorder(new TitledBorder(new EtchedBorder(), "Sign Link"));
		this.add(linkLabel, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.WEST);
		this.add(sButtonsPanel, BorderLayout.CENTER);
		addHelpButton();
	}

	private void addHelpButton()
	{
		final JButton helpButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/icons/sHelp.jpg"))));
		helpButton.setActionCommand("help");
		helpButton.addActionListener(new HelpListener());
		helpButton.setPreferredSize(new Dimension(22, 22));
		helpButtonPanel = new JPanel(new FlowLayout());
		helpButtonPanel.add(Box.createVerticalStrut(250));
		helpButtonPanel.add(helpButton);
		this.add(helpButtonPanel, BorderLayout.EAST);
	}

	/**
	 * Adding a new signLink and sorting it at the same time
	 * 
	 * @param sign -
	 *            the signLink to add
	 */
	public void addSign(final Sign sign)
	{
		for (int i = 0; i < signList.size(); i++)
		{
			if (sign.compareTo(signList.get(i)) <= 0)
			{
				signList.add(i, sign);
				sPanel.addSign(sign, i, false);
				frame.getVComponent().getVideoButtonsPanel().getVSlide().setSigns(signList);
				return;
			}
		}
		signList.add(sign);
		sPanel.addSign(sign, -1, false);
		frame.getVComponent().getVideoButtonsPanel().getVSlide().setSigns(signList);
	}

	public void deSelect()
	{
		sPanel.deSelect();
	}

	protected SignButtonsPanel getSButtonsPanel()
	{
		return sButtonsPanel;
	}

	public int getSelected()
	{
		return selected;
	}

	/**
	 * Returns a signLink with the specified index in the list
	 * 
	 * @param index -
	 *            the location in the list the signLink is at
	 * @return Signlink
	 */
	public Sign getSign(final int index)
	{
		return signList.get(index);
	}

	/**
	 * @return the signList
	 */
	public ArrayList<Sign> getSignList()
	{
		return signList;
	}

	/**
	 * Removing a signLink
	 * 
	 * @param index -
	 *            the location in the list the signLink is at
	 */
	public void removeSign(final int index)
	{
		signList.remove(index);
		sPanel.removeSign(index);
		frame.getVComponent().getVideoButtonsPanel().getVSlide().setSigns(signList);
	}

	public void select(final int index)
	{
		sPanel.select(index);
	}

	public void setSelected(final int selected)
	{
		this.selected = selected;
	}

	/**
	 * @param signList
	 *            the signList to set
	 */
	public void setSignList(final ArrayList<Sign> signList)
	{
		this.signList = signList;
	}

	public void updateSign(final Sign sign)
	{
		sPanel.updateLink(sign);
	}
}
