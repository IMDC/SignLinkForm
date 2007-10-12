package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * A component class that holds the video panel and the video buttons panel
 * 
 * @author Martin Gerdzhev
 */
public class VideoComponent extends JComponent
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5269734684510730896L;
	private VideoPanel			vPanel;
	private VideoButtonsPanel	vButtonsPanel;
	private final VideoListener	vListen;
	private JLabel				imageLabel;

	public VideoComponent()
	{
		imageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icon_bar_lower.gif"))));
		imageLabel.setVisible(false);
		imageLabel.setPreferredSize(new Dimension(320, 15));
		vListen = new VideoListener(this);
		vPanel = new VideoPanel();
		vButtonsPanel = new VideoButtonsPanel(vListen);
		this.setPreferredSize(new Dimension(368, 370));
		this.setSize(368, 370);
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new EtchedBorder(), "Video"));
		this.add(vPanel, BorderLayout.NORTH);
		this.add(imageLabel, BorderLayout.CENTER);
		this.add(vButtonsPanel, BorderLayout.SOUTH);
	}

	/**
	 * default constructor
	 */
	public VideoComponent(final File file)
	{
		imageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icon_bar_lower.gif"))));
		imageLabel.setVisible(false);
		imageLabel.setPreferredSize(new Dimension(320, 15));
		vListen = new VideoListener(this);
		vPanel = new VideoPanel();
		vPanel.openFromFile(file);
		vButtonsPanel = new VideoButtonsPanel(vListen);
		this.setPreferredSize(new Dimension(368, 370));
		this.setSize(368, 370);
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new EtchedBorder(), "Video"));
		this.add(vPanel, BorderLayout.NORTH);
		this.add(imageLabel, BorderLayout.CENTER);
		this.add(vButtonsPanel, BorderLayout.SOUTH);
	}

	public JLabel getImageLabel()
	{
		return imageLabel;
	}

	protected VideoButtonsPanel getVideoButtonsPanel()
	{
		return vButtonsPanel;
	}

	protected VideoListener getVideoListener()
	{
		return vListen;
	}

	protected VideoPanel getVideoPanel()
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
