package signlinkform;

public class AnswerElement {
	private String videoName;
	private int ansNum;
	private int startTime;
	private int endTime;
	private String linkValue;
	private String label;
	private boolean edited;
	private boolean inQues;
	

	public AnswerElement(String vname){
		this.ansNum = 0;
		this.videoName = vname;
		this.inQues = false;
		this.label = "";
		this.linkValue = "";
		this.edited = false;
		this.startTime = 0;
		this.endTime = 0;
	}
	public AnswerElement(String vname, int num, boolean inQ, String _label, String linkVal, int stime, int etime){
		this.ansNum = num;
		this.videoName = vname;
		this.inQues = inQ;
		this.label = _label;
		this.linkValue = linkVal;
		this.edited = false;
		this.startTime = stime;
		this.endTime = etime;
		
	}
	public AnswerElement() {
		this.ansNum = 0;
		this.videoName = "";
		this.inQues = false;
		this.label = "";
		this.linkValue = "";
		this.edited = false;
		this.startTime = 0;
		this.endTime = 0;
		
	}
	public int getEndTime() {
		return endTime;
	}
	public String getLinkValue() {
		return linkValue;
	}
	public int getStartTime() {
		return startTime;
	}	
	public void setLabel(String _label) {
		this.label = _label;
		this.edited = true;
	}
	public String getLabel(){
		return this.label;
	}
	public void setValue(String val) {
		this.linkValue = val;
		this.edited = true;
	}
	public String getValue(){
		return this.linkValue;
	}
	public boolean isEdited() {
		return this.edited;
	}
	public void setETime(int etime) {
		this.endTime = etime;
		this.edited = true;
	}
	public int getETime() {
		return this.endTime;
	}
	public void setSTime(int stime){
		this.startTime = stime;
		this.edited = true;
	}
	public int getSTime(){
		return this.startTime;
	}
	public boolean isInQues(){
		return this.inQues;
	}
	
	public String getVideoName() {
		return this.videoName;
	}
	
	public int getAnsNum () {
		return this.ansNum;
	}
	
	public void setAnsNum (int a) {
		this.ansNum = a;
		this.edited = true;
	}


}
