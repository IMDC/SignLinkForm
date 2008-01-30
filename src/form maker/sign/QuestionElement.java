package sign;

public class QuestionElement {
	private String videoName;
	
	public QuestionElement(String vname) {
		this.videoName = vname;
	}
	
	public QuestionElement() {
		this.videoName = "";
	}

	public String getVideoName() {
		return this.videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	

}
