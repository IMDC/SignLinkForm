package sign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import sign.*;

public class QuestionList extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList <FormElement> formQuestions;  //  @jve:decl-index=0:
	// shows each question
	private ArrayList<QuestionPanel> qPanel;
	private JPanel scroll;
	
	// buttons at bottom of page
	private JPanel buttonPanel = null;
	private JButton newButton = null;
	private JButton deleteAllButton = null;
	private JButton doneButton = null;
	private JButton cancelButton = null;
	private JPanel jPanel = null;
	private JScrollPane jScrollPane = null; 

	/**
	 * This method initializes 
	 * 
	 */
	private QuestionList() {
		super();
		
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 -175, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 -270);
		initialize();
		this.setVisible(true);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.qPanel = new ArrayList<QuestionPanel>();
        this.setSize(new Dimension(350, 540));
        //this.setResizable(false);
        this.setTitle("List of Form Elements");       

		// main panel of screen
        this.jPanel = new JPanel();
		this.jPanel.setLayout(new BorderLayout());
		
		// buttons on bottom of screen
		addButtonPanel();
		
		// scroll pane
		formQuestions = new ArrayList<FormElement>();
		scroll = new JPanel();
		scroll.setLayout(new BoxLayout(scroll,BoxLayout.Y_AXIS));
		this.jScrollPane = new JScrollPane(scroll);
		
		this.jScrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Form Questions"));
		this.jPanel.add(this.jScrollPane);
		
		this.setContentPane(this.jPanel);

		ButtonListener blisten = new ButtonListener();
		this.cancelButton.addActionListener(blisten);
		this.newButton.addActionListener(blisten);
		this.doneButton.addActionListener(blisten);
		this.deleteAllButton.addActionListener(blisten);
		
		this.formQuestions = new ArrayList<FormElement>();
        
	}
	
	private void addButtonPanel() {
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel,BoxLayout.X_AXIS));
		this.newButton = new JButton("New");
		this.deleteAllButton = new JButton("Delete All");
		this.doneButton = new JButton("Done");
		this.cancelButton = new JButton("Cancel");
		
		this.buttonPanel.add(Box.createHorizontalStrut(10));
		this.buttonPanel.add(this.newButton);
		this.buttonPanel.add(Box.createHorizontalStrut(10));
		this.buttonPanel.add(this.deleteAllButton);
		this.buttonPanel.add(Box.createHorizontalStrut(20));
		this.buttonPanel.add(this.doneButton);
		this.buttonPanel.add(Box.createHorizontalStrut(10));
		this.buttonPanel.add(this.cancelButton);
		this.jPanel.add(this.buttonPanel, BorderLayout.SOUTH);
	}

	public void reOrder(int i, Integer newi){
		QuestionPanel qpanel = this.qPanel.remove(i-1);
		//System.out.println("curr "+qpanel.getCurrentNumber());
		//System.out.println("new place "+newi.intValue());
		this.qPanel.add(newi.intValue()-1, qpanel);
		this.scroll.removeAll();
		for (int j = 0; j < this.qPanel.size(); j++){
			//System.out.println(this.qPanel.get(j).getCurrentNumber());
			this.qPanel.get(j).setCurrentNumber(j+1);
			scroll.add(this.qPanel.get(j));
		}
		scroll.revalidate();
		scroll.repaint();
		
	}



	public static void main(String args[]) throws Exception
	{
		QuestionList qlist = new QuestionList();
		qlist.setVisible(true);
	
	}
	public void addQuestion(char type){
		FormElement newele = new FormElement(type);
		formQuestions.add(newele);
		//System.out.println("presize "+this.qPanel.size());
		int max;
		if (this.qPanel.isEmpty()){
			max = 1;
		} else {
			max = this.qPanel.size()+1;
		}
		this.qPanel.add(new QuestionPanel(new ImageIcon(),max,max,this,"Question "+max,newele));
		Integer[] orderArray = new Integer[this.qPanel.size()];
		for (int i=0; i<this.qPanel.size(); i++){
			orderArray[i] = i+1;
		}
		for (int i=0; i<this.qPanel.size()-1; i++) {
			this.qPanel.get(i).setOrderArray(orderArray);
			this.qPanel.get(i).setCurrentNumber(i+1);
		}
		this.scroll.add(this.qPanel.get(this.qPanel.size()-1));
		scroll.revalidate();
		scroll.repaint();
		
	}


	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == QuestionList.this.cancelButton){
				QuestionList.this.setVisible(false);
				System.exit(0);
			} else if (e.getSource() == QuestionList.this.newButton){
				QuestionType qType = new QuestionType(QuestionList.this);
				qType.setVisible(true);
			}
			
			
		}
		
	}
	private static QuestionList	instance;

	public synchronized static QuestionList getInstance()
	{
		if (instance == null)
		{
			instance = new QuestionList();
		}

		return instance;
	}
} 


