package signlinkform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FormMaker {

	public static String readLine(String msg) throws IOException{
		String CurLine = ""; // Line read from standard in

		System.out.println(msg);
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		
		CurLine = in.readLine();
		return CurLine;
	}
	public static FormElement createQuestion() throws IOException {
		FormElement f;
		QuestionElement q;
		ArrayList<AnswerElement> a = new ArrayList<AnswerElement>();
		String type;
		char t;
		
		type = readLine("Enter Question Type [r]ecord, [c]heckbox, r[a]dio: ");
		t = type.charAt(0);	// get the first character only
		t = Character.toLowerCase(t); // make sure it is lover case
		
		
		q = new QuestionElement(readLine("Enter Question: "));
		
		if (t != 'r') {
			String ans = "";
			int num = 0;
			while (!(ans.equals("-"))) {
				ans = readLine("Enter Answer ('-' to quit): )");
				
				if (!(ans.equals("-"))) {
					AnswerElement ansEle = new AnswerElement(ans);
					num++;
					a.add(ansEle);
					
				}
			}
		}
				
		f = new FormElement(q,a,t);
		return f;
	}
	
	private static ArrayList<FormElement> getQuestions() throws Exception{
		ArrayList<FormElement> head = new ArrayList<FormElement>();

		
		for (int i=0; i<3; i++){
			head.add(createQuestion());
				
		}
		
		return head;
	}
	
	public static void outputQuestions(ArrayList<FormElement> questions){
		//FormElement[] curr = (FormElement[]) questions.toArray();
		
		for (int i = 0; i< questions.size(); i++) {
			QuestionElement ques = questions.get(i).getQuestion();
			System.out.println(ques.getVideoName());
			char type = questions.get(i).getQtype();
			System.out.print("Question type: ");
			System.out.println(type);
			if (type != 'r') {
				System.out.println("Answers");
				for (int j = 0; j<questions.get(i).getAnswer().size(); j++){
					System.out.println(questions.get(i).getAnswer().get(j).getVideoName());
					System.out.println(questions.get(i).getAnswer().get(j).getAnsNum());
				}
			}
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		ArrayList<FormElement> questions;
		
		questions = getQuestions();
		
		outputQuestions(questions);
		
		
	}

}
