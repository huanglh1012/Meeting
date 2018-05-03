package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the post database table.
 * 
 */
@Entity
@Table(name="post", schema = "meeting")
public class PostEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String postId;
	private String postName;
	private String postSummary;

	public PostEntity() {
	}


	@Id
	@Column(name="POST_ID", unique=true, nullable=false, length=32)
	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}


	@Column(name="POST_NAME", nullable=false, length=128)
	public String getPostName() {
		return this.postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}


	@Column(name="POST_SUMMARY", length=1024)
	public String getPostSummary() {
		return this.postSummary;
	}

	public void setPostSummary(String postSummary) {
		this.postSummary = postSummary;
	}

}