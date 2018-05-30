package mis.security.dto;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;

@Component
public class PostDTO extends BaseDTO {
	
	private String postId;
	
	private String postName;
	
	private String postSummary;
	
	private String id;
	
	private String text;
	
	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return this.postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPostSummary() {
		return this.postSummary;
	}

	public void setPostSummary(String postSummary) {
		this.postSummary = postSummary;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
