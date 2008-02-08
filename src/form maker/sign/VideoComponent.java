package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * A component class that holds the video panel and the video buttons panel
 * 
 * @author Martin Gerdzhev
 * @version $Id: VideoComponent.java 118 2008-01-29 17:56:25Z martin $
 */
public class VideoComponent extends JComponent
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5269734684510730896L;
	private SignlinkIcons		images				= SignlinkIcons.getInstance();
	private VideoPanel			vPanel;
	private VideoButtonsPanel	vButtonsPanel;
	private JLabel				imageLabel;

	/**
	 * default constructor
	 */
	public VideoComponent(final File file)
	{
		initVideoComponent(file, new VideoListener(this));
	}

	public VideoComponent(final File file, final ActionListener listen)
	{
		initVideoComponent(file, listen);
	}

	/**
	 * @param file
	 * @param listen
	 */
	private void initVideoComponent(final File file, final ActionListener listen)
	{
		imageLabel = new JLabel(images.iconBarLowerIcon);
		imageLabel.setVisible(false);
		imageLabel.setPreferredSize(new Dimension(320, 15));
		vPanel = new VideoPanel();
		vPanel.openFromFile(file);
		vButtonsPanel = new VideoButtonsPanel(this, listen);
		this.setPreferredSize(new Dimension(368, 370));
		this.setSize(368, 370);
		this.setLayout(new BorderLayout());

		this.add(vPanel, BorderLayout.NORTH);
		this.add(imageLabel, BorderLayout.CENTER);
		this.add(vButtonsPanel, BorderLayout.SOUTH);
	}

	public JLabel getImageLabel()
	{
		return imageLabel;
	}

	public VideoButtonsPanel getVideoButtonsPanel()
	{
		return vButtonsPanel;
	}

	public VideoPanel getVideoPanel()
	{
		return vPanel;
	}

	public void setImageLabel(final JLabel imageLabel)
	{
		this.imageLabel = imageLabel;
	}

	protected void setTimingControlPanel(final TimingPanelControlPanel tButt)
	{
		vButtonsPanel = tButt;
	}

	protected void setVideoButtonsPanel(final VideoButtonsPanel vButt)
	{
		vButtonsPanel = vButt;
	}

	protected void setVideoPanel(final VideoPanel vPan)
	{
		vPanel = vPan;
		this.revalidate();
		this.repaint();
	}
}
