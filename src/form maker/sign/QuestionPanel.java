package sign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class QuestionPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel imageHolder;
	private JLabel qType;
	private JButton editButton;
	private JButton deleteButton;
	private JComboBox order;
	private Integer[] orderArray;
	private int currentNumber;
	private JPanel buttonPanel;
	private Dimension size = new Dimension(70,25);
	public static final ImageIcon check = new ImageIcon(new ImageIcon("images/checkbox.png").getImage().getScaledInstance(30, 30, 0));
	public static final ImageIcon radio = new ImageIcon(new ImageIcon("images/radiobutton.png").getImage().getScaledInstance(30, 30, 0));
	
	
	private FormElement question;
	
	private QuestionList qList;
	
	public QuestionPanel(ImageIcon img, int current, int max, QuestionList qList, String title,FormElement question)
	{
		this.qList = qList;
		currentNumber = current;
		orderArray = new Integer[max];
		for (int i= 0; i < max; i++) {
			orderArray[i] = i+1;
		}
		this.question = question;
		
		
		imageHolder = new JLabel(img);
		imageHolder.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		imageHolder.setPreferredSize(new Dimension(80,80));
		imageHolder.setMaximumSize(new Dimension(80,80));
		imageHolder.setMinimumSize(new Dimension(80,80));
		
		switch (this.question.getQtype()){
		case 'c':
			qType = new JLabel(QuestionPanel.check);
			break;
		case 'a':
			qType = new JLabel(QuestionPanel.radio);
			break;
		}
		
		qType.setBorder(BorderFactory.createLineBorder(Color.black,	1));

		
		
		
		editButton = new JButton("Edit");
		editButton.setAlignmentX(CENTER_ALIGNMENT);
		editButton.setPreferredSize(size);
		editButton.setMaximumSize(size);
		editButton.setMinimumSize(size);
		deleteButton = new JButton("Delete");
		deleteButton.setAlignmentX(CENTER_ALIGNMENT);
		deleteButton.setPreferredSize(size);
		deleteButton.setMaximumSize(size);
		deleteButton.setMinimumSize(size);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		
		order = new JComboBox(this.orderArray);
		order.setSelectedIndex(this.currentNumber-1);
		order.setPreferredSize(size);
		order.setMaximumSize(size);
		order.setMinimumSize(size);
		order.setAlignmentX(CENTER_ALIGNMENT);
		
		buttonPanel.add(order);
		buttonPanel.add(Box.createVerticalStrut(5));
		buttonPanel.add(editButton);
		buttonPanel.add(Box.createVerticalStrut(5));
		buttonPanel.add(deleteButton);
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		this.add(imageHolder);
		this.add(qType);
		this.add(buttonPanel);
		this.setBorder(new TitledBorder(new EtchedBorder(), title));
		this.setPreferredSize(new Dimension(300,115));
		this.setMaximumSize(new Dimension(300,115));
		this.setMinimumSize(new Dimension(300,115));
		
		ButtonListener blisten = new ButtonListener();
		this.order.addActionListener(blisten);
		this.deleteButton.addActionListener(blisten);
		this.editButton.addActionListener(blisten);
		
	}
	
	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JButton getEditButton() {
		return editButton;
	}

	public JLabel getImageHolder() {
		return imageHolder;
	}
	public void setImageHolder(JLabel imageHolder) {
		this.imageHolder = imageHolder;
	}
	public JComboBox getOrder() {
		return order;
	}
	public void setOrder(JComboBox order) {
		this.order = order;
	}

	public int getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
		order.setSelectedIndex(currentNumber-1);
	}

	public void setOrderArray(Integer[] orderArray) {
		for (int i =this.orderArray.length;i < orderArray.length;i++){
			order.addItem(orderArray[i]);
		}
		this.orderArray = orderArray;		
	}
	
	//added by martin
	public void addOrderArrayElement()
	{
		this.orderArray = new Integer[orderArray.length+1];
		for (int i = 0;i<orderArray.length; i++)
		{
			orderArray[i] = i+1;
		}
		order.addItem(orderArray[orderArray.length-1]);
	}
	
	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == QuestionPanel.this.deleteButton) {
				System.out.println("delete "+QuestionPanel.this.currentNumber);
			} else if (e.getSource() == QuestionPanel.this.editButton){
				System.out.println("edit "+QuestionPanel.this.currentNumber);
			} else if (e.getSource() == QuestionPanel.this.order){
				if (((Integer)QuestionPanel.this.order.getSelectedItem()).intValue() != QuestionPanel.this.currentNumber){
					//System.out.println("reorder "+QuestionPanel.this.currentNumber+" "+QuestionPanel.this.order.getSelectedItem().toString());
					QuestionPanel.this.qList.reOrder(QuestionPanel.this.currentNumber,(Integer) QuestionPanel.this.order.getSelectedItem());
				}
			}
			
		}
		
	}
	
}
