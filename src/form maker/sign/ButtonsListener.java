package sign;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * A listener Class for the file manipulation buttons and menu items
 * 
 * @author Martin Gerdzhev
 * @version $Id: ButtonsListener.java 66 2007-11-22 16:51:10Z martin $
 */

public class ButtonsListener implements ActionListener
{
	private MenuFrame			frame;
	private JFrame				exportFrame;
	public static final String	SIGNICONSLOCATION	= "signlink_images" + File.separator;

	/**
	 * A constructor for the listener class
	 * 
	 * @param aComponent -
	 *            the video component that it is supposed to open
	 */
	protected ButtonsListener(MenuFrame mFrame)
	{
		super();
		frame = mFrame;
	}
	


	/**
	 * the method that performs the necessary actions
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals("exit"))
		{
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
			frame.getVComponent().getVideoPanel().closeSession();
			SignUtils.cleanUpAndExit(0);
		}

		if (event.getActionCommand().equals("new"))
		{
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
			if (frame.getXmlFile() != null)
				this.save(frame.getXmlFile().getAbsolutePath());
			else
				this.saveAs();
			frame.setModified(false);
		}

		if (event.getActionCommand().equals("save as"))
		{
			this.saveAs();
			frame.setModified(false);
		}

		if (event.getActionCommand().equals("export"))
		{
			final JProgressBar progressBar;
			final JLabel label;
			int width = 400;
			int height = 100;
			final JButton doneButton;
			if (frame.isModified())
			{
				if (frame.getXmlFile() != null)
					this.save(frame.getXmlFile().getAbsolutePath());
				else
					this.saveAs();
				frame.setModified(false);
			}
			JPanel progressPanel = new JPanel();
			progressBar = new JProgressBar(0, 100);
			progressBar.setIndeterminate(false);
			progressBar.setStringPainted(true);
			progressPanel.add(progressBar);
			label = new JLabel();
			label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			exportFrame = new JFrame("Exporting File...");
			exportFrame.setSize(width, height);
			exportFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/SignEd_icon_16.jpg")));
			exportFrame.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - width / 2, (int) Toolkit
					.getDefaultToolkit().getScreenSize().getHeight()
					/ 2 - height / 2);
			exportFrame.getContentPane().add(progressPanel, BorderLayout.CENTER);
			exportFrame.getContentPane().add(label, BorderLayout.NORTH);

			doneButton = new JButton("Done");

			exportFrame.getContentPane().add(doneButton, BorderLayout.SOUTH);
			doneButton.addActionListener(this);
			doneButton.setVisible(false);

			exportFrame.setVisible(true);
			frame.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			String xmlName = frame.getXmlFile().getName();
			String exportPath = frame.getXmlFile().getParent() + File.separator + xmlName.substring(0, xmlName.lastIndexOf("."));
			File exportFolder = new File(exportPath);
			if (!exportFolder.exists())
			{
				exportFolder.mkdir();
			}
			final MovieExporter mExport = new MovieExporter(frame.getVComponent().getVideoPanel().getFile().getAbsolutePath(), exportPath
					+ File.separator + xmlName.substring(0, xmlName.lastIndexOf(".") + 1) + "flv", "flv");
			final SWFExporter swfExport = new SWFExporter(frame.getSComp().getSignList(), frame.getXmlFile().getParent() + File.separator
					+ SIGNICONSLOCATION, exportPath, xmlName.substring(0, xmlName.lastIndexOf(".")) + ".flv", frame.getTPanel()
					.getTextComponent().getExportText());

			label.setText("Transcoding movie to flv...");

			new Thread()
			{
				public void run()
				{
					boolean mExp = mExport.export();

					if (!mExp)
					{
						label.setText("Export Failed!!!\nFailed to transcode movie to flv.");
						frame.getRootPane().setCursor(null);
						doneButton.setVisible(true);
						return;
					}
					progressBar.setValue(progressBar.getMaximum() / 2);
					progressBar.setString("50%");
					label.setText("Creating sfw output file...");
					boolean sExp = swfExport.export();

					if (!sExp)
					{
						label.setText("Export Failed!!!\nFailed to build swf output file.");
						frame.getRootPane().setCursor(null);
						doneButton.setVisible(true);
						return;
					}
					progressBar.setValue(progressBar.getMaximum());
					progressBar.setString("Done");
					label.setText("Export Successful");
					frame.getRootPane().setCursor(null);
					doneButton.setVisible(true);
				}
			}.start();

		}

		if (event.getActionCommand().equals("Done")) // the done button in the export frame isclicked
		{
			exportFrame.dispose();
		}

	}

	protected void saveAs()
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

	protected void save(String destinationFileURL)
	{
		System.out.println(frame.getVComponent().getVideoPanel().getFile().getAbsolutePath());
		if (frame.getVComponent().getVideoPanel().getFile().getAbsolutePath().endsWith(".flexTemp" + File.separator + "tmp.mp4"))
		{
			File destFile = new File(destinationFileURL.substring(0, destinationFileURL.lastIndexOf(".sgn")) + ".mp4");
			try
			{
				SignUtils.copy(frame.getVComponent().getVideoPanel().getFile(), destFile);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new XMLExporter(destFile.getAbsolutePath(), destinationFileURL, frame.getVComponent().getVideoPanel().getDuration(), frame
					.getVComponent().getVideoPanel().getTimeScale(), frame.getSComp().getSignList(), frame.getTPanel().getTextComponent()
					.getExportText());
		}
		else
		{
			new XMLExporter(frame.getVComponent().getVideoPanel().getFile().getAbsolutePath(), destinationFileURL, frame.getVComponent()
					.getVideoPanel().getDuration(), frame.getVComponent().getVideoPanel().getTimeScale(), frame.getSComp().getSignList(),
					frame.getTPanel().getTextComponent().getExportText());
		}
	}
}
