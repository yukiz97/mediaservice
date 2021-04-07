package mediaservice.models;

public class UploadModel {
	String fileName;
	String content;   
	
	public UploadModel() {
	}
	
	public UploadModel(String fileName, String content) {
		this.fileName = fileName;
		this.content = content;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
