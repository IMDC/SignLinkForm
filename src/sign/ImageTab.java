package sign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: ImageTab.java 52 2007-11-08 03:56:17Z martin $
 *
 */
public class ImageTab extends JPanel
{
	class ButtonsListener implements ActionListener
	{

		public void actionPerformed(final ActionEvent event)
		{
			if (event.getActionCommand().equals("frame back"))
			{
				if (vPanel.getTime() > vSlide.getMinimum())
				{
					vPanel.stepBackwards();
					if (vPanel.getTime() < vSlide.getMinimum())
						vPanel.setTimeMiliseconds(vSlide.getMinimum());
					vSlide.setValue(vPanel.getTime());
					vPanel.repaint();
				}
			}

			if (event.getActionCommand().equals("frame forward"))
			{
				if (vPanel.getTime() < vSlide.getMaximum())
				{
					vPanel.stepForward();
					if (vPanel.getTime() > vSlide.getMaximum())
						vPanel.setTimeMiliseconds(vSlide.getMaximum());
					vSlide.setValue(vPanel.getTime());
					vPanel.repaint();
				}
			}

			if (event.getActionCommand().equals("set frame"))
			{
				frame.setPreview(frame.getImage(glass.getStartX() - vPanel.getX() - OFFSETX, glass.getStartY() - OFFSETY - vPanel.getY(),
						glass.getViewWidth(), glass.getViewHeight()));
				iComp.setValue(vPanel.getTime());
				iComp.setPosition(vSlide.getCoordfromValue(iComp.getValue()));
				// frame.getSign().setFTime(vPanel.getTime());
			}

		}

	}

	class CListener implements ComponentListener
	{
		public void componentHidden(final ComponentEvent arg0)
		{
			frame = (SignLink) ImageTab.this.getRootPane().getParent();
			glass = (GlassPanel) frame.getGlassPane();
			frame.getGlassPane().setVisible(false);
			if (frame.getHelp() != null)
			{
				frame.getHelp().dispose();
			}
		}

		public void componentMoved(final ComponentEvent arg0)
		{

		}

		public void componentResized(final ComponentEvent arg0)
		{

		}

		public void componentShown(final ComponentEvent arg0)
		{
			OFFSETX = ImageTab.this.getX() + ImageTab.this.getParent().getX();
			OFFSETY = ImageTab.this.getY() + ImageTab.this.getParent().getY();
			frame = (SignLink) ImageTab.this.getRootPane().getParent();
			glass = (GlassPanel) frame.getGlassPane();
			if (!isInitialized())
			{
				glass.setStartX(glass.getStartX() + vPanel.getX() + OFFSETX);
				glass.setStartY(glass.getStartY() + vPanel.getY() + OFFSETY);
// vSlide.setActiveStart(vSlide.getCoordfromValue(vSlide.getMinimum()));
// vSlide.setActiveEnd(vSlide.getCoordfromValue(vSlide.getMaximum()));
// vSlide.setValue(iComp.getValue());
// vSlide.repaint();
				setInitialized(true);
// commitChanges();
			}
			frame.getGlassPane().setVisible(true);
			frame.setHelpFile(HelpFrame.A1);
			if (vSlide.getMinimum() != tPanel.getVSlide().getValueFromCoord(tPanel.getTimingComponent().getLeftEnd())
					|| vSlide.getMaximum() != tPanel.getVSlide().getValueFromCoord(tPanel.getTimingComponent().getRightEnd()))
			{
				vSlide.setMinimum(tPanel.getVSlide().getValueFromCoord(tPanel.getTimingComponent().getLeftEnd()));
				vSlide.setMaximum(tPanel.getVSlide().getValueFromCoord(tPanel.getTimingComponent().getRightEnd()));
				vSlide.setValue(vSlide.getMinimum());
				vSlide.setActiveStart(vSlide.getCoordfromValue(vSlide.getMinimum()));
				vSlide.setActiveEnd(vSlide.getCoordfromValue(vSlide.getMaximum()));
				vPanel.setTimeMiliseconds(vSlide.getValue());
				final String min = vComponent.getVideoPanel().getMin(); // getting initial minutes for label
				final String sec = vComponent.getVideoPanel().getSec(); // getting initial seconds for label
				final String mil = vComponent.getVideoPanel().getMil(); // getting initial milliseconds for label
				vText.setText(min + ":" + sec + ":" + mil); // setting initial values in the textbox
				iComp.setPosition(vSlide.getCoordfromValue(iComp.getValue()));
			}
			System.out.println("VPanelX: " + (vPanel.getX() + ImageTab.this.getX()));
			System.out.println("VPanelY: " + (ImageTab.this.getY() + ImageTab.this.getParent().getY()));
		}
	}

	/**
	 * Listener Class that takes care of all the actions associated with the glass pane, i.e. moving, resizing, imposing restrictions on the
	 * minimum bounds, aspect ratio, sides bounds to the movies's outer bounds
	 * 
	 * @author Martin Gerdzhev
	 */
	class GlassListener implements MouseMotionListener, MouseListener
	{
		private boolean	isMoving	= false;
		private boolean	isResizing	= false;
		private int		lastX;
		private int		lastY;
		private int		endX;
		private int		endY;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */

		public void mouseClicked(final MouseEvent arg0)
		{
			System.out.println("mouseX: " + arg0.getX() + " mouseY: " + arg0.getY());
			System.out.println("glassMoveBoxX: " + glass.getMoveBox().getX() + " glassMoveBoxY:" + glass.getMoveBox().getY());
			System.out.println("glassX: " + glass.getStartX() + " glassY: " + glass.getStartY() + " width: " + glass.getViewWidth()
					+ " height: " + glass.getViewHeight());
		}

		public void mouseDragged(final MouseEvent arg0)
		{

			if (isResizing) // done
			{
				endX = glass.getEndX();
				endY = glass.getEndY();
				if (ImageTab.this.getRootPane().getCursor().getType() == Cursor.NW_RESIZE_CURSOR)
				{// top left corner

					if (arg0.getX() + OFFSETX - glass.getStartX() < arg0.getY() + OFFSETY - glass.getStartY())
					{

						glass.setStartX(arg0.getX() + OFFSETX);
						glass.setEndX(endX);
						glass.setStartY((int) (endY - glass.getViewWidth() / VideoPanel.ASPECT));
						glass.setEndY(endY);
					}
					else if (arg0.getX() + OFFSETX - glass.getStartX() >= arg0.getY() + OFFSETY - glass.getStartY())
					{
						glass.setStartY((int) (arg0.getY()) + OFFSETY);
						glass.setEndY(endY);
						glass.setStartX((int) (endX - (glass.getViewHeight() * VideoPanel.ASPECT)));
						glass.setEndX(endX);
					}
					if (glass.getViewWidth() < WIDTH || glass.getViewHeight() < HEIGHT)
					{ // minimum dimension on the resize box
						glass.setStartX(endX - WIDTH);
						glass.setEndX(endX);
						glass.setStartY(endY - HEIGHT);
						glass.setEndY(endY);
					}
					if (glass.getStartX() < vPanel.getX() + OFFSETX)
					{ // restrict on the left
						glass.setStartX(vPanel.getX() + OFFSETX);
						glass.setEndX(endX);
						glass.setStartY((int) (endY - glass.getViewWidth() / VideoPanel.ASPECT));
						glass.setEndY(endY);
					}
					if (glass.getStartY() < vPanel.getY() + OFFSETY)
					{ // restrict on the top
						glass.setStartY(vPanel.getY() + OFFSETY);
						glass.setEndY(endY);
						glass.setStartX((int) (endX - (glass.getViewHeight() * VideoPanel.ASPECT)));
						glass.setEndX(endX);
					}
				}
				else if (ImageTab.this.getRootPane().getCursor().getType() == Cursor.NE_RESIZE_CURSOR)
				{ // top right corner
					if (glass.getEndX() - (arg0.getX() + OFFSETX) < arg0.getY() + OFFSETY - glass.getStartY())
					{
						glass.setEndX(arg0.getX() + OFFSETX);
						glass.setStartY((int) (endY - glass.getViewWidth() / VideoPanel.ASPECT));
						glass.setEndY(endY);
					}
					else
					{
						glass.setStartY(arg0.getY() + OFFSETY);
						glass.setEndY(endY);
						glass.setEndX((int) (glass.getViewHeight() * VideoPanel.ASPECT + glass.getStartX()));
					}
					if (glass.getViewWidth() < WIDTH || glass.getViewHeight() < HEIGHT)
					{// minimum dimension on the resize box
						glass.setEndX(glass.getStartX() + WIDTH);
						glass.setStartY(endY - HEIGHT);
						glass.setEndY(endY);
					}
					if (glass.getEndX() > OFFSETX + vPanel.getX() + VideoPanel.WIDTH)
					{ // restrict on the right
						glass.setEndX(OFFSETX + vPanel.getX() + VideoPanel.WIDTH);
						glass.setStartY((int) (endY - glass.getViewWidth() / VideoPanel.ASPECT));
						glass.setEndY(endY);
					}
					if (glass.getStartY() < vPanel.getY() + OFFSETY)
					{ // restrict on the top
						glass.setStartY(vPanel.getY() + OFFSETY);
						glass.setEndY(endY);
						glass.setEndX((int) (glass.getStartX() + glass.getViewHeight() * VideoPanel.ASPECT));
					}
				}
				else if (ImageTab.this.getRootPane().getCursor().getType() == Cursor.SW_RESIZE_CURSOR)
				{// bottom left corner
					if (arg0.getX() + OFFSETX - glass.getStartX() < glass.getEndY() - (arg0.getY() + OFFSETY))
					{
						glass.setStartX(arg0.getX() + OFFSETX);
						glass.setEndX(endX);
						glass.setEndY((int) (glass.getViewWidth() / VideoPanel.ASPECT + glass.getStartY()));
					}
					else
					{
						glass.setEndY(arg0.getY() + OFFSETY);
						glass.setStartX((int) (endX - (glass.getViewHeight() * VideoPanel.ASPECT)));
						glass.setEndX(endX);
					}
					if (glass.getViewWidth() < WIDTH || glass.getViewHeight() < HEIGHT)
					{// minimum dimension on the resize box
						glass.setStartX(endX - WIDTH);
						glass.setEndX(endX);
						glass.setEndY(glass.getStartY() + HEIGHT);
					}
					if (glass.getStartX() < OFFSETX + vPanel.getX())
					{ // restrict on the left
						glass.setStartX(OFFSETX + vPanel.getX());
						glass.setEndX(endX);
						glass.setEndY((int) (glass.getViewWidth() / VideoPanel.ASPECT + glass.getStartY()));
					}
					if (glass.getEndY() > OFFSETY + vPanel.getY() + VideoPanel.HEIGHT)
					{ // restrict on the bottom
						glass.setEndY(OFFSETY + vPanel.getY() + VideoPanel.HEIGHT);
						glass.setStartX((int) (endX - (glass.getViewHeight() * VideoPanel.ASPECT)));
						glass.setEndX(endX);
					}
				}
				else
				// bottom right corner
				{
					if (arg0.getX() + OFFSETX - glass.getEndX() > arg0.getY() + OFFSETY - glass.getEndY())
					{
						glass.setEndX(arg0.getX() + OFFSETX);
						glass.setEndY((int) (glass.getViewWidth() / VideoPanel.ASPECT + glass.getStartY()));
					}
					else
					{
						glass.setEndY(arg0.getY() + OFFSETY);
						glass.setEndX((int) (glass.getViewHeight() * VideoPanel.ASPECT + glass.getStartX()));
					}
					if (glass.getViewWidth() < WIDTH || glass.getViewHeight() < HEIGHT)
					{// minimum dimension on the resize box
						glass.setEndX(glass.getStartX() + WIDTH);
						glass.setEndY(glass.getStartY() + HEIGHT);
					}
					if (glass.getEndX() > OFFSETX + vPanel.getX() + VideoPanel.WIDTH)
					{ // restrict on the right
						glass.setEndX(OFFSETX + vPanel.getX() + VideoPanel.WIDTH);
						glass.setEndY((int) (glass.getViewWidth() / VideoPanel.ASPECT + glass.getStartY()));
					}
					if (glass.getEndY() > OFFSETY + vPanel.getY() + VideoPanel.HEIGHT)
					{ // restrict on the bottom
						glass.setEndY(OFFSETY + vPanel.getY() + VideoPanel.HEIGHT);
						glass.setEndX((int) (glass.getViewHeight() * VideoPanel.ASPECT + glass.getStartX()));
					}
				}
			}
			else if (isMoving) // done
			{
				System.out.println("I'm moving");
				if (glass.getStartX() >= vPanel.getX() + OFFSETX && glass.getEndX() <= VideoPanel.WIDTH + vPanel.getX() + OFFSETX)
				{
					glass.setStartX(glass.getStartX() - (lastX - arg0.getX()));
					lastX = arg0.getX();
				}
				if (glass.getStartX() < vPanel.getX() + OFFSETX)
				{
					glass.setStartX(vPanel.getX() + OFFSETX);
				}
				if (glass.getEndX() > VideoPanel.WIDTH + vPanel.getX() + OFFSETX)
				{
					glass.setStartX(VideoPanel.WIDTH + vPanel.getX() + OFFSETX - (int) glass.getViewWidth());

				}
				if (glass.getStartY() >= OFFSETY + vPanel.getY() && glass.getEndY() <= VideoPanel.HEIGHT + OFFSETY + vPanel.getY())
				{
					glass.setStartY((int) glass.getStartY() - (lastY - arg0.getY()));
					lastY = arg0.getY();
				}
				if (glass.getStartY() < OFFSETY + vPanel.getY())
				{
					glass.setStartY(OFFSETY + vPanel.getY());
				}
				if (glass.getEndY() > VideoPanel.HEIGHT + OFFSETY + vPanel.getY())
				{
					glass.setStartY(VideoPanel.HEIGHT + OFFSETY + vPanel.getY() - (int) glass.getViewHeight());
				}
			}
		}

		public void mouseEntered(final MouseEvent arg0)
		{

		}

		public void mouseExited(final MouseEvent arg0)
		{

		}

		public void mouseMoved(final MouseEvent arg0) // done
		{
			// For resizing
			if (arg0.getX() >= glass.getMoveBox().getX() - OFFSETX && arg0.getX() < glass.getMoveBox().getX() + RDIST - OFFSETX) // left
			// end
			{
				if (arg0.getY() >= glass.getMoveBox().getY() - OFFSETY
						&& arg0.getY() <= glass.getMoveBox().getMaxY() - OFFSETY - glass.getMoveBox().getHeight() / 2) // top
				// left corner
				{
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
				}
				else if (arg0.getY() > glass.getMoveBox().getY() - OFFSETY + glass.getMoveBox().getHeight() / 2
						&& arg0.getY() <= glass.getMoveBox().getMaxY() - OFFSETY)
				{// bottom left corner
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
				}
				else
					ImageTab.this.getRootPane().setCursor(Cursor.getDefaultCursor());
			}
			// For Resizing
			else if (arg0.getX() > glass.getMoveBox().getMaxX() - RDIST - OFFSETX && arg0.getX() <= glass.getMoveBox().getMaxX() - OFFSETX) // right
			// end
			{
				if (arg0.getY() >= glass.getMoveBox().getY() - OFFSETY
						&& arg0.getY() <= glass.getMoveBox().getMaxY() - OFFSETY - glass.getMoveBox().getHeight() / 2)
				{// top right corner
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
				}
				else if (arg0.getY() > glass.getMoveBox().getY() - OFFSETY + glass.getMoveBox().getHeight() / 2
						&& arg0.getY() <= glass.getMoveBox().getMaxY() - OFFSETY)
				{// bottom right corner
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				}
				else
					ImageTab.this.getRootPane().setCursor(Cursor.getDefaultCursor());
			}
			// For Resizing
			else if (arg0.getY() <= glass.getMoveBox().getY() + RDIST - OFFSETY && arg0.getY() >= glass.getMoveBox().getY() - OFFSETY) // top
			// end
			{
				if (arg0.getX() >= glass.getMoveBox().getX() - OFFSETX
						&& arg0.getX() <= glass.getMoveBox().getMaxX() - glass.getMoveBox().getWidth() / 2 - OFFSETX)
				{
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
				}
				else if (arg0.getX() < glass.getMoveBox().getMaxX() - OFFSETX
						&& arg0.getX() >= glass.getMoveBox().getX() - OFFSETX + glass.getMoveBox().getWidth() / 2)
				{
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
				}
				else
					ImageTab.this.getRootPane().setCursor(Cursor.getDefaultCursor());
			}
			// For Resizing
			else if (arg0.getY() <= glass.getMoveBox().getMaxY() - OFFSETY && arg0.getY() >= glass.getMoveBox().getMaxY() - RDIST - OFFSETY) // bottom
			// end
			{
				if (arg0.getX() >= glass.getMoveBox().getX() - OFFSETX
						&& arg0.getX() <= glass.getMoveBox().getMaxX() - glass.getMoveBox().getWidth() / 2 - OFFSETX)
				{
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
				}
				else if (arg0.getX() <= glass.getMoveBox().getMaxX() - OFFSETX
						&& arg0.getX() > glass.getMoveBox().getX() - OFFSETX + glass.getMoveBox().getWidth() / 2)
				{
					ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				}
				else
					ImageTab.this.getRootPane().setCursor(Cursor.getDefaultCursor());
			}
			// For Moving
			else if (arg0.getX() > glass.getMoveBox().getX() + RDIST - OFFSETX
					&& arg0.getX() < glass.getMoveBox().getMaxX() - RDIST - OFFSETX
					&& arg0.getY() < glass.getMoveBox().getMaxY() - RDIST - OFFSETY
					&& arg0.getY() > glass.getMoveBox().getY() + RDIST - OFFSETY)
			{
				ImageTab.this.getRootPane().setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
			// Not on top of the box
			else
				ImageTab.this.getRootPane().setCursor(Cursor.getDefaultCursor());
		}

		public void mousePressed(final MouseEvent arg0)
		{
			if (ImageTab.this.getRootPane().getCursor().getType() == Cursor.SE_RESIZE_CURSOR
					|| ImageTab.this.getRootPane().getCursor().getType() == Cursor.SW_RESIZE_CURSOR
					|| ImageTab.this.getRootPane().getCursor().getType() == Cursor.NE_RESIZE_CURSOR
					|| ImageTab.this.getRootPane().getCursor().getType() == Cursor.NW_RESIZE_CURSOR) // resizing
			{
				isResizing = true;
				System.out.println("resize");
			}
			if (ImageTab.this.getRootPane().getCursor().getType() == Cursor.MOVE_CURSOR) // moving
			{
				isMoving = true;
				System.out.println("move");
			}
			lastX = arg0.getX();
			lastY = arg0.getY();
		}

		public void mouseReleased(final MouseEvent arg0)
		{
			isResizing = false;
			isMoving = false;
			// frame.setPreview(ImageTab.this.getImage());
		}

	}

	/**
	 * 
	 */
	private static final long				serialVersionUID	= -7564619491742688165L;
	// Distance from title bar to beginning of the container for VPanel
	public static int						OFFSETY;
	// Distance from left border to beginning of the container for VPanel
	public static int						OFFSETX;
	// distance from the outside of the glass rectangle to the area for moving or the width or height for resizing it
	public static final int					RDIST				= 10;
	public static final int					WIDTH				= 32;
	public static final int					HEIGHT				= 24;
	private final VideoPanel				vPanel;
	private final TimingPanelControlPanel	tPanel;
	private final ButtonsListener			vListen;
	private final VideoComponent			vComponent;
	private SignSlider						vSlide;
	private JTextField						vText;
	private SignLink						frame;
	private GlassPanel						glass;
	private ImageComponent					iComp;
	private boolean							initialized			= false;

	protected ImageTab(final SignLink sFrame, final VideoComponent vComp, final int time, final TimingTab tTab, final int glassX,
			final int glassY, final int glassW, final int glassH)
	{
		frame = sFrame;
		tPanel = tTab.getTPanel();
		vPanel = vComp.getVideoPanel();
		vListen = new ButtonsListener();
		vComponent = vComp;
		vPanel.setTimeMiliseconds(time);
		vPanel.setBackground(Color.RED);
		final JPanel north = new JPanel();
		north.setBackground(Color.WHITE);
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		north.add(Box.createVerticalStrut(5));
		north.add(vPanel);
		north.add(Box.createVerticalStrut(20));
		this.addComponentListener(new CListener());
		this.setLayout(new BorderLayout());
		this.add(north, BorderLayout.NORTH);
		this.addSliderTextFieldAndButtons();
		this.setBackground(Color.WHITE);
		final GlassListener gListen = new GlassListener();
		this.addMouseListener(gListen);
		this.addMouseMotionListener(gListen);
		this.setVisible(true);
		iComp.setPosition(vSlide.getCoordfromValue(time));
		iComp.setValue(time);
		vSlide.setValue(time);
		glass = (GlassPanel) sFrame.getGlassPane();
		glass.setStartX(glassX);
		glass.setEndX(glass.getStartX() + glassW);
		glass.setStartY(glassY);
		glass.setEndY(glass.getStartY() + glassH);
	}

	protected ImageTab(final VideoComponent vComp, final int time, final TimingTab tTab)
	{
		tPanel = tTab.getTPanel();
		vPanel = vComp.getVideoPanel();
		vListen = new ButtonsListener();
		vComponent = vComp;
		vPanel.setTimeMiliseconds(time);
		vPanel.setBackground(Color.RED);
		final JPanel north = new JPanel();
		north.setBackground(Color.WHITE);
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		north.add(Box.createVerticalStrut(5));
		north.add(vPanel);
		north.add(Box.createVerticalStrut(20));
		this.addComponentListener(new CListener());
		this.setLayout(new BorderLayout());
		this.add(north, BorderLayout.NORTH);
		this.addSliderTextFieldAndButtons();
		this.setBackground(Color.WHITE);
		final GlassListener gListen = new GlassListener();
		this.addMouseListener(gListen);
		this.addMouseMotionListener(gListen);
	}

	protected void clear()
	{
		setInitialized(false);
	}

	protected void setTime(int time)
	{
		vPanel.setTimeMiliseconds(time);
		vSlide.setValue(time);
		iComp.setPosition(vSlide.getCoordfromValue(time));
		iComp.setValue(time);
	}

	protected void setGlassDimensions(final int glassX, final int glassY, final int glassW, final int glassH)
	{
		glass.setStartX(glassX);
		glass.setEndX(glass.getStartX() + glassW);
		glass.setStartY(glassY);
		glass.setEndY(glass.getStartY() + glassH);
	}

	private void addSliderTextFieldAndButtons()
	{
		final JPanel center = new JPanel();
		final JPanel south = new JPanel();
		final JPanel panel = new JPanel();
		south.setPreferredSize(new Dimension(320, 40));
		south.setSize(new Dimension(320, 40));
		south.setBackground(Color.GRAY);
		center.setBackground(Color.WHITE);
		center.setPreferredSize(new Dimension(322, 50));
		center.setMaximumSize(new Dimension(322, 50));
		center.setMinimumSize(new Dimension(322, 50));
		center.setLayout(new BorderLayout(0, 0));
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(324, 110));

		vText = new JTextField();
		vText.setMinimumSize(new Dimension(65, 25));
		vText.setMaximumSize(new Dimension(65, 25));
		vText.setPreferredSize(new Dimension(65, 25));
		Font font = new Font("arial", Font.PLAIN, 12);
		vText.setFont(font);
		vText.setEditable(false);
		vText.setBackground(Color.WHITE);
		// getting initial minutes, seconds and milliseconds for label
		final String min = vComponent.getVideoPanel().getMin();
		final String sec = vComponent.getVideoPanel().getSec();
		final String mil = vComponent.getVideoPanel().getMil();
		vText.setText(min + ":" + sec + ":" + mil); // setting initial values in
		// the textbox setting the size of the slider to be the length of the signlink
		vSlide = new SignSlider(tPanel.getVSlide().getValueFromCoord(tPanel.getTimingComponent().getLeftEnd()), tPanel.getVSlide()
				.getValueFromCoord(tPanel.getTimingComponent().getRightEnd()), tPanel.getVSlide().getValueFromCoord(
				tPanel.getTimingComponent().getLeftEnd()));
		final SignSliderUI sUI = new SignSliderUI(vSlide);
		vSlide.setPreferredSize(new Dimension(320, 38));
		vSlide.setSize(new Dimension(320, 38));
		vSlide.setUI(sUI);
		vSlide.setSUI(sUI);
		vSlide.addChangeListener(new TPSliderListener(vSlide, vComponent, vText));
		vSlide.addMouseListener(new CompMouseListener(vSlide, vComponent, vText));
		vSlide.addMouseMotionListener(new MMotionListener(vSlide, vComponent, vText));
		iComp = new ImageComponent(320, 15, vSlide.getCoordfromValue(vSlide.getMinimum()));

		vSlide.setActiveStart(vSlide.getCoordfromValue(vSlide.getMinimum()));
		vSlide.setActiveEnd(vSlide.getCoordfromValue(vSlide.getMaximum()));
		vSlide.setEnabled(true);
		vSlide.setVisible(true);
		vSlide.setBackground(Color.WHITE);
		center.add(vSlide);
		center.add(iComp, BorderLayout.SOUTH);
		center.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		final ImageIcon frameBackIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/sStepBackward.jpg")));
		final JButton frameBackButton = new JButton(frameBackIcon);
		frameBackButton.setActionCommand("frame back");
		frameBackButton.addActionListener(vListen);
		frameBackButton.setPreferredSize(new Dimension(30, 30));
		frameBackButton.setBackground(Color.GRAY);
		final ImageIcon frameForwardIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/sStepForward.jpg")));
		final JButton frameForwardButton = new JButton(frameForwardIcon);
		frameForwardButton.setActionCommand("frame forward");
		frameForwardButton.addActionListener(vListen);
		frameForwardButton.setPreferredSize(new Dimension(30, 30));
		frameForwardButton.setBackground(Color.GRAY);
		final ImageIcon cameraIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/sSetFrame.jpg")));
		final JButton cameraButton = new JButton("Set Frame", cameraIcon);
		cameraButton.setActionCommand("set frame");
		cameraButton.addActionListener(vListen);
		cameraButton.setPreferredSize(new Dimension(120, 30));
		cameraButton.setBackground(Color.GRAY);
		south.add(vText);
		south.add(frameBackButton);
		south.add(frameForwardButton);
		south.add(Box.createHorizontalStrut(20));
		south.add(cameraButton);
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		panel.add(center, BorderLayout.NORTH);
		panel.add(south, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel cPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		cPanel.setBackground(Color.WHITE);
		cPanel.add(panel);
		this.add(cPanel, BorderLayout.CENTER);
	}

	/**
	 * Method to undo the changes made to the imageTab when the cancel button is pressed
	 */

	protected int getGlassX()
	{
		return glass.getStartX() - vPanel.getX() - OFFSETX;
	}

	protected int getGlassY()
	{
		return glass.getStartY() - OFFSETY - vPanel.getY();
	}

	protected int getGlassWidth()
	{
		return glass.getViewWidth();
	}

	protected int getGlassHeight()
	{
		return glass.getViewHeight();
	}

	protected int getFrameTime()
	{
		return iComp.getValue();
	}

	/**
	 * Getter method to return the value of initialized
	 * 
	 * @return the initialized
	 */
	public boolean isInitialized()
	{
		return this.initialized;
	}

	/**
	 * Setter method to set the value of initialized
	 * 
	 * @param initialized
	 *            the value to set
	 */
	public void setInitialized(boolean initialized)
	{
		this.initialized = initialized;
	}
}
