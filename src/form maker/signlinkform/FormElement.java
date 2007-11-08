package signlinkform;

import java.util.ArrayList;

public class FormElement {
	private static int id = 1;				// id for elements
	private int ele;						// id of this element
	private QuestionElement ques;			// this question
	private ArrayList <AnswerElement> ans;	// answers for this question
	private char qtype;						// type c - checkbox, r - record, a - radio button
	
	public FormElement(QuestionElement q, ArrayList<AnswerElement> a, char type) {
		this.ques = q;
		this.ans = a;
		ini(type);
	}
	
	public FormElement(char type){
		ini(type);
		this.ques = new QuestionElement();
		this.ans = new ArrayList<AnswerElement>();
	}
	
	private void ini(char type) {
		this.qtype = type;
		this.ele = FormElement.id;
		FormElement.id++;
	}
	/**
	 * returns the question for this form element
	 * @return
	 */
	public QuestionElement getQuestion(){
		return ques;
	}
	/**
	 * returns all the answers for this form element
	 * @return
	 */
	public ArrayList<AnswerElement> getAnswer() {
		return ans;
	}

	/**
	 * returns the type of question for this form element
	 * @return
	 */
	public char getQtype() {
		return qtype;
	}

	
	public int getID(){
		return id;
	}

	/**
	 * Allows you to change the type of question
	 * @param type - new type for question
	 */
	public void setQType(char type){
		this.qtype = type;
	}
	
	public int getEle(){
		return this.ele;
	}
	/**
	 * Adds an answer to this form element
	 * @param ans
	 */
	public void addAnswer(AnswerElement ans) {
		this.ans.add(ans);
	}
	
	/**
	 * Add question to this form element
	 * @param ques - question for this element
	 */
	public void setQues(QuestionElement ques) {
		this.ques = ques;
	}

}
