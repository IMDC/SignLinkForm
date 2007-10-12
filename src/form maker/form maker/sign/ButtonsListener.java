package sign;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * A listener Class for the file manipulation buttons and menu items
 * 
 * @author Martin Gerdzhev
 */

public class ButtonsListener implements ActionListener
{
	private MenuFrame			frame;
	public static final String	SIGNICONSLOCATION	= "signlink_images" + File.separator;

	/**
	 * A constructor for the listener class
	 * 
	 * @param aComponent -
	 *            the video component that it is supposed to open
	 */
	protected ButtonsListener()
	{
		super();
	}

	/**
	 * the method that performs the necessary actions
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals("exit"))
		{
			// TODO set a fancy message to ask to save first etc.
			try
			{
				if (event.getSource().getClass() == Class.forName("javax.swing.JMenuItem"))
				{
					frame = (MenuFrame) ((JMenu) ((JPopupMenu) ((JMenuItem) event.getSource()).getParent()).getInvoker()).getRootPane()
							.getParent();
				}
				else
					// from the exit button or from the X button
					frame = (MenuFrame) ((JComponent) event.getSource()).getRootPane().getParent();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (frame.isModified())
			{
				int option = JOptionPane.showConfirmDialog(null,
						"You are about to exit SignLink Studio.\n The Project is not saved. Do you want to save the project?", "Warning",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.NO_OPTION)
				{
					frame.setVisible(false);
					frame.dispose();
				}
				else if (option == JOptionPane.CANCEL_OPTION)
				{
					return;
				}
				else
				// YES_OPTION
				{
					if (frame.getXmlFile() != null)
						this.save(frame.getXmlFile().getAbsolutePath());
					else
						this.saveAs();

					frame.setModified(false);
					frame.setVisible(false);
					frame.dispose();
				}
			}
			else
			// no modifications
			{
				int option = JOptionPane.showConfirmDialog(null, "You are about to exit SignLink Studio.\n Are you sure?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.NO_OPTION)
				{
					return;
				}
				else
				// yes option
				{
					frame.setVisible(false);
					frame.dispose();
				}
			}
			System.exit(0);
		}

		if (event.getActionCommand().equals("new"))
		{
			// TODO fill in
			try
			{
				if (event.getSource().getClass() == Class.forName("javax.swing.JMenuItem"))
				{
					frame = (MenuFrame) ((JMenu) ((JPopupMenu) ((JMenuItem) event.getSource()).getParent()).getInvoker()).getRootPane()
							.getParent();
				}
				else
					frame = (MenuFrame) ((JComponent) event.getSource()).getRootPane().getParent();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (frame.isModified())
			{
				int option = JOptionPane.showConfirmDialog(null, "The Project is not saved. Do you want to save the project?", "Warning",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.NO_OPTION)
				{
					frame.setVisible(false);
					frame.dispose();
					CreateWindow.getInstance().setVisible(true);
				}
				else if (option == JOptionPane.CANCEL_OPTION)
				{
					return;
				}
				else
				// YES_OPTION
				{
					if (frame.getXmlFile() != null)
						this.save(frame.getXmlFile().getAbsolutePath());
					else
						this.saveAs();

					frame.setModified(false);

					frame.setVisible(false);
					frame.dispose();
					CreateWindow.getInstance().setVisible(true);
				}
			}
			else
			{
				frame.setVisible(false);
				frame.dispose();
				CreateWindow.getInstance().setVisible(true);
			}
		}

		if (event.getActionCommand().equals("open")) // opening a new movie
		{
		}

		if (event.getActionCommand().equals("save"))
		{
			try
			{
				if (event.getSource().getClass() == Class.forName("javax.swing.JMenuItem"))
				{
					frame = (MenuFrame) ((JMenu) ((JPopupMenu) ((JMenuItem) event.getSource()).getParent()).getInvoker()).getRootPane()
							.getParent();
				}
				else
					frame = (MenuFrame) ((JComponent) event.getSource()).getRootPane().getParent();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (frame.getXmlFile() != null)
				this.save(frame.getXmlFile().getAbsolutePath());
			else
				this.saveAs();
			frame.setModified(false);
		}

		if (event.getActionCommand().equals("save as"))
		{
			try
			{
				if (event.getSource().getClass() == Class.forName("javax.swing.JMenuItem"))
				{
					frame = (MenuFrame) ((JMenu) ((JPopupMenu) ((JMenuItem) event.getSource()).getParent()).getInvoker()).getRootPane()
							.getParent();
				}
				else
					frame = (MenuFrame) ((JComponent) event.getSource()).getRootPane().getParent();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.saveAs();
			frame.setModified(false);
		}

		if (event.getActionCommand().equals("export"))
		{
			// TODO fill in
			try
			{
				if (event.getSource().getClass() == Class.forName("javax.swing.JMenuItem"))
				{
					frame = (MenuFrame) ((JMenu) ((JPopupMenu) ((JMenuItem) event.getSource()).getParent()).getInvoker()).getRootPane()
							.getParent();
				}
				else
					frame = (MenuFrame) ((JComponent) event.getSource()).getRootPane().getParent();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new SWFExporter(frame.getSComp().getSignList(), frame.getXmlFile().getParent() + File.separator + SIGNICONSLOCATION, frame
					.getXmlFile().getName().substring(0, frame.getXmlFile().getName().lastIndexOf("."))
					+ ".flv", frame.getTPanel().getTextComponent().getExportText());
		}
	}

	private void saveAs()
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new SgnFileFilter());
		String destinationFileURL = null;

		if (jfc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
		{
			if (jfc.getSelectedFile().getName().endsWith(".sgn"))
			{
				destinationFileURL = jfc.getSelectedFile().getAbsolutePath();
			}
			else if (jfc.getSelectedFile().getName().toLowerCase().endsWith(".sgn"))
			{
				destinationFileURL = jfc.getSelectedFile().getAbsolutePath().substring(0,
						jfc.getSelectedFile().getAbsolutePath().lastIndexOf("."))
						+ ".sgn";
			}
			else
				destinationFileURL = jfc.getSelectedFile().getAbsolutePath() + ".sgn";
			System.out.println("destination file:" + destinationFileURL);
			this.save(destinationFileURL);
			frame.setXmlFile(new File(destinationFileURL));
		}
	}

	private void save(String destinationFileURL)
	{
		new XMLExporter(frame.getVComponent().getVideoPanel().getFile().getAbsolutePath(), destinationFileURL, frame.getVComponent()
				.getVideoPanel().getDuration(), frame.getVComponent().getVideoPanel().getTimeScale(), frame.getSComp().getSignList(), frame
				.getTPanel().getTextComponent().getExportText());
	}
}
