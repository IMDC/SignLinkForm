package sign.recording;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * @author Laurel Williams
 * 
 * @version $Id: RecordingException.java 32 2007-10-17 20:03:58Z laurel $
 *
 */
public class RecordingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public static void showMessageAndThrowRecordingException(Component parent,
			Exception e, String info) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(parent, info + e.getMessage(),
				"Error", JOptionPane.WARNING_MESSAGE);
		throw new RecordingException();
	}
	
	public static void showMessageAndThrowRecordingException(Component parent,
			Error e, String info) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(parent, info + e.getMessage(),
				"Error", JOptionPane.WARNING_MESSAGE);
		throw new RecordingException();
	}

}
