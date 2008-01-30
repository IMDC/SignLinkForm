package sign;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.GraphicsImporterDrawer;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTImageProducer;
import quicktime.app.view.QTJComponent;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTConstants4;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.media.DataRef;
import quicktime.util.QTHandle;
import quicktime.util.QTUtils;

/**
 * A class that opens and manipulates a Quicktime video
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: VideoPanel.java 118 2008-01-29 17:56:25Z martin $
 */
public class VideoPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8277238022738906275L;
	public static final int		TIMEERROR			= -1;
	public static final float	RATEERROR			= -1.0f;
	public static final int		WIDTH				= 320;
	public static final int		HEIGHT				= 240;
	public static final double	ASPECT				= (double) WIDTH / HEIGHT;

	/**
	 * Prompts the user for a movie file
	 * 
	 * @return the selected file
	 */
	protected static File prompt()
	{
		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new QTFileFilter());
		if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
		{
			return null;
		}
		return fc.getSelectedFile();
	}

	private Movie							movie;
	private quicktime.app.view.MoviePlayer	moviePlayer;
	private JComponent						movieComponent;
	private File							f;
	private String							min	= "00";
	private String							sec	= "00";
	private String							mil	= "000";

	/**
	 * default constructor that opens a session
	 */
	public VideoPanel()
	{
		openSession();
		movieComponent = new JPanel();
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.add(movieComponent);
	}

	public VideoPanel(final InputStream iStream) //needed for getting the movie files from the jar file
	{
		openSession();
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		// load the file into memory
		DataInputStream lcl_byteStream = new DataInputStream(iStream);

		MovieController movieControl;
		try
		{
			int li_size = lcl_byteStream.available();

			System.out.println("size of movie in mem = " + li_size);

			byte lba_byteArray[] = new byte[li_size + 1024];

			int li_result = 0;

			while (li_result != -1)
			{
				li_result += lcl_byteStream.read(lba_byteArray, li_result, 1024);
			}

			// create a QTHandle from it
			QTHandle lcl_qtHandle = new QTHandle(lba_byteArray);

			DataRef lcl_dr = new DataRef(lcl_qtHandle, StdQTConstants.kDataRefFileExtensionTag, ".mp4");
			movie = Movie.fromDataRef(lcl_dr, StdQTConstants.newMovieActive | StdQTConstants4.newMovieAsyncOK);
			movieControl = new MovieController(movie);
			this.add(QTFactory.makeQTComponent(movieControl).asComponent());

		}
		catch (final QTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * A method that closes the quicktime session
	 * 
	 * @return whether it successfully closed the session
	 */
	protected boolean closeSession()
	{
		// Open a Quicktime session
		try
		{
			QTSession.close();
			QTUtils.reclaimMemory();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	protected void dispose()
	{
		try
		{
			movie.disposeQTObject();
		}
		catch (final QTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * a method to fast forward a movie
	 */
	public void fastForward()
	{
		try
		{
			if (movie.getRate() == 1 || movie.getRate() == 0 || movie.getRate() == -10)
				movie.setRate(10);
			else
				// if (movie.getRate()==10)
				movie.setRate(1);
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected int getDuration()
	{
		try
		{
			return movie.getDuration();
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TIMEERROR;
		}
	}

	protected int getDurationMilliSec()
	{
		return this.getDuration() * 1000 / this.getTimeScale();
	}

	protected File getFile()
	{
		return f;
	}

	protected ImageIcon getImage(final int x, final int y, final int width, final int height)
	{
		GraphicsImporter gi;
		GraphicsImporterDrawer gid;
		ImageIcon icon = new ImageIcon();
		try
		{
			gi = new GraphicsImporter(StdQTConstants.kQTFileTypePicture);
			gid = new GraphicsImporterDrawer(gi);
			Pict pict = this.getMovie().getPict(this.getMovie().getTime());
			// add 512-byte header that pict would have as file
			final byte[] newPictBytes = new byte[pict.getSize() + 512];
			pict.copyToArray(0, newPictBytes, 512, newPictBytes.length - 512);
			pict = new Pict(newPictBytes);
// export it
			final DataRef ref = new DataRef(pict, StdQTConstants.kDataRefQTFileTypeTag, "PICT");
			gi.setDataReference(ref);
			final QDRect rect = gi.getSourceRect();
			final Dimension dim = new Dimension(rect.getWidth(), rect.getHeight());
			final QTImageProducer ip = new QTImageProducer(gid, dim);
// convert from MoviePlayer to java.awt.Image
			Image image = Toolkit.getDefaultToolkit().createImage(ip);
			// getting the dimensions of the image
			final CropImageFilter filter = new CropImageFilter(x, y, width, height);
			image = createImage(new FilteredImageSource(image.getSource(), filter));
// make a swing icon out of it and show it in a frame
			icon = new ImageIcon(image);
		}
		catch (final QTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return icon;
	}

	/**
	 * @return the current millisecond in the movie < 1000
	 */
	protected String getMil()
	{
		mil = "" + getTime() % 1000;
		if (mil.length() < 2)
			mil = "00" + mil;
		else if (mil.length() < 3)
			mil = "0" + mil;
		return mil;
	}

	/**
	 * @return the current minute in the movie
	 */
	protected String getMin()
	{
		min = "" + (getTime() / 1000) / 60;
		if (min.length() < 2)
			min = "0" + min;
		return min;
	}

	protected Movie getMovie()
	{
		return movie;
	}

	/**
	 * Gets a Movie component for the specified file
	 */
	protected JComponent getQuicktimeMovieComponent(final File f)
	{
		QTJComponent qtc = null;

		try
		{
			final QTFile qtFile = new QTFile(f);

			// Create the movie
			movie = Movie.fromFile(OpenMovieFile.asRead(qtFile));
			if ((movie.getBox().getWidth() != 320 || movie.getBox().getHeight() != 240))
			{ // resize the movie if not in the right dimensions
				System.out.println("Resizing Movie from: " + movie.getBox().getWidth() + "x" + movie.getBox().getHeight() + " to 320x240");
				movie.setBounds(new QDRect(320, 240));
				movie.setBox(new QDRect(320, 240));
			}
			// Create the movie player
			moviePlayer = new quicktime.app.view.MoviePlayer(movie);
			// Create the QuickTime Movie Component
			qtc = QTFactory.makeQTJComponent(moviePlayer);

			return qtc.asJComponent();
		}
		catch (final QTException err)
		{
			err.printStackTrace();
			return null;
		}
	}

	public float getRate()
	{
		try
		{
			return movie.getRate();
		}
		catch (final StdQTException e)
		{
			System.out.println("Couldn't get Rate");
			e.printStackTrace();
			return RATEERROR;
		}
	}

	/**
	 * @return the current second in the movie < 60
	 */
	protected String getSec()
	{
		sec = "" + (getTime() / 1000) % 60;
		if (sec.length() < 2)
			sec = "0" + sec;
		return sec;
	}

	/**
	 * Gets the current time of the movie in milliseconds
	 * 
	 * @return the current time
	 */
	public int getTime()
	{
		try
		{
			return movie.getTime() * 1000 / movie.getTimeScale();
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TIMEERROR;
		}
	}

	protected int getTimeScale()
	{
		try
		{
			return movie.getTimeScale();
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TIMEERROR;
		}
	}

	public int getTimeValue()
	{
		try
		{
			return movie.getTime();
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TIMEERROR;
		}
	}

	public boolean isDone()
	{
		try
		{
			return movie.isDone();
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Opens a Quicktime Movie
	 */
	public void open()
	{
		f = prompt();
		movieComponent = getQuicktimeMovieComponent(f);
		this.removeAll();
		this.add(movieComponent);
		this.invalidate();
		this.validate();
		this.repaint();
	}

	public void openFromFile(final File aFile)
	{
		f = aFile;
		movieComponent = getQuicktimeMovieComponent(aFile);
		this.removeAll();
		this.add(movieComponent);
		this.invalidate();
		this.validate();
		this.repaint();
	}

	/**
	 * Opens a Quicktime Session
	 */
	public void openSession()
	{
		// Open a Quicktime session
		try
		{
			QTSession.open();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Plays a Quicktime Movie
	 */
	public void play()
	{
		// Start playing the movie
		try
		{
			final float rate = movie.getPreferredRate();
			System.out.println("Movie rate: " + rate);
			System.out.println("Movie duration: " + movie.getDuration());
			System.out.println("Preferred Volume: " + movie.getPreferredVolume());
			movie.start();
		}
		catch (final Exception e)
		{
			System.out.println("Problem playing movie");
			e.printStackTrace();
		}
	}

	/**
	 * a method to rewind a movie
	 */
	public void rewind()
	{
		try
		{
			if (movie.getRate() == 1 || movie.getRate() == 0 || movie.getRate() == 10)
				movie.setRate(-10);
			else
				// if (movie.getRate()==-10)
				movie.setRate(1);
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setActive(final boolean flag)
	{
		try
		{
			movie.setActive(flag);
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMovie(final Movie aMovie)
	{
		movie = aMovie;
	}

	public void setTime(final int aTime)
	{
		try
		{
			movie.setTimeValue(aTime);
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setTimeMiliseconds(final int time)
	{
		try
		{
			movie.setTimeValue((int) ((double) time * movie.getTimeScale() / 1000));
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * a method to go a frame backwards.
	 */
	public void stepBackwards()
	{
		this.setTimeMiliseconds(this.getTime() - 100);
	}

	/**
	 * a method to go a frame forward.
	 */
	public void stepForward()
	{
		this.setTimeMiliseconds(this.getTime() + 100);
	}

	/**
	 * Stops a Quicktime Movies
	 */
	public void stop()
	{
		try
		{
			movie.stop();
		}
		catch (final StdQTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
