package signlinkform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class QuestionType extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel,jPanel1;
	private JButton checkboxButton;
	private JButton radioButton;
	private JButton cancelButton;
	private QuestionList qList;

	/**
	 * This method initializes 
	 * 
	 */
	public QuestionType(QuestionList qList) {
		super();
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 -125, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 -125);
		initialize();
		this.qList = qList;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setTitle("Choose Question Type");
        this.setSize(255, 255);
        this.setResizable(false);

        jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout());
        
		jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
		jPanel.setBorder(new TitledBorder(new EtchedBorder(), "Choose Question Type"));
		checkboxButton = new JButton("Checkbox");
		checkboxButton.setIcon(QuestionPanel.check);
		checkboxButton.setAlignmentX(CENTER_ALIGNMENT);
		radioButton = new JButton("Radio");
		radioButton.setIcon(QuestionPanel.radio);
		radioButton.setAlignmentX(CENTER_ALIGNMENT);
		cancelButton = new JButton("Cancel");
		cancelButton.setAlignmentX(CENTER_ALIGNMENT);
		
		jPanel.add(Box.createVerticalStrut(10));
		jPanel.add(checkboxButton);
		jPanel.add(Box.createVerticalStrut(5));
		jPanel.add(radioButton);
		Dimension size = new Dimension(100,100);
		jPanel.setPreferredSize(size);
		jPanel.setMaximumSize(size);
		jPanel.setMinimumSize(size);

		        
        jPanel1.add(jPanel,BorderLayout.CENTER);
        
        JPanel jp1 = new JPanel();
        jp1.add(cancelButton);
        
        jPanel1.add(jp1,BorderLayout.SOUTH);
        
		
		this.setContentPane(jPanel1);
		
		ButtonListener blisten = new ButtonListener();
		cancelButton.addActionListener(blisten);
		radioButton.addActionListener(blisten);
		checkboxButton.addActionListener(blisten);
	}




	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == QuestionType.this.cancelButton){
				QuestionType.this.setVisible(false);
			} else if (e.getSource() == QuestionType.this.radioButton){
				QuestionType.this.qList.addQuestion('a');
				QuestionType.this.setVisible(false);
			} else if (e.getSource() == QuestionType.this.checkboxButton){
				QuestionType.this.qList.addQuestion('c');
				QuestionType.this.setVisible(false);
			}
			
		}
		
	}
	
} 
