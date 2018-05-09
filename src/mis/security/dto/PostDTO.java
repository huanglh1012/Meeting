package mis.security.dto;

import org.springframework.stereotype.Component;

import ecp.bsp.system.core.BaseDTO;
@Component
public class PostDTO extends BaseDTO {
	
	private String postId;
	
	private String postName;
	
	private String postSummary;
	
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
}
