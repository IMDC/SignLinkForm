package sign.recording;

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;

/**
 * This file was obtained from
 * http://java.about.com/library/weekly/uc_jmfmovie3.htm and we're not exactly
 * sure how it works. Some notes in javadoc comments.
 * 
 * @author Martin Gerdzhev
 * 
 * @version $Id: DeviceStateHelper.java 63 2007-11-19 20:44:11Z laurel $
 */
public class DeviceStateHelper implements ControllerListener {

	Player player = null;
	boolean configured = false;
	boolean realized = false;
	boolean prefetched = false;
	boolean eom = false;
	boolean failed = false;
	boolean closed = false;

	public DeviceStateHelper(Player inPlayer) {
		this.player = inPlayer;
		this.player.addControllerListener(this);
	}

	/**
	 * Waits for the processor to be configured for the number of milliseconds
	 * specified.
	 * 
	 * @param timeOutMillis
	 * @return true of the processor becomes configured within the timeout
	 *         period.
	 */
	public boolean configure(int timeOutMillis) {

		long startTime = System.currentTimeMillis();
		synchronized (this) {
			if (player instanceof Processor)
				((Processor) player).configure();
			else
				return false;
			while (!configured && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return configured;
	}

	/**
	 * Waits for the processor to be realized for the number of milliseconds
	 * specified.
	 * 
	 * @param timeOutMillis
	 * @return true if the processor is realized within the timeout period.
	 */
	public boolean realize(int timeOutMillis) {
		long startTime = System.currentTimeMillis();
		synchronized (this) {
			player.realize();
			while (!realized && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return realized;
	}

	public boolean prefetch(int timeOutMillis) {
		long startTime = System.currentTimeMillis();
		synchronized (this) {
			player.prefetch();
			while (!prefetched && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException ie) {
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return prefetched && !failed;
	}

	public boolean playToEndOfMedia(int timeOutMillis) {
		long startTime = System.currentTimeMillis();
		eom = false;
		synchronized (this) {
			player.start();
			while (!eom && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException ie) {
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return eom && !failed;
	}

	public void close() {
		synchronized (this) {
			player.close();
			while (!closed) {
				try {
					wait(100);
				} catch (InterruptedException ie) {
				}
			}
		}
		player.removeControllerListener(this);
	}

	public synchronized void controllerUpdate(ControllerEvent ce) {
		if (ce instanceof RealizeCompleteEvent) {
			realized = true;
		} else if (ce instanceof ConfigureCompleteEvent) {
			configured = true;
		} else if (ce instanceof PrefetchCompleteEvent) {
			prefetched = true;
		} else if (ce instanceof EndOfMediaEvent) {
			eom = true;
		} else if (ce instanceof ControllerErrorEvent) {
			failed = true;
		} else if (ce instanceof ControllerClosedEvent) {
			closed = true;
		} else {
			return;
		}
		notifyAll();
	}
}