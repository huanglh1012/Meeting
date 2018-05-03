package mis.security.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the message_send_state database table.
 * 
 */
@Entity
@Table(name="message_send_state")
public class MessageSendStateEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String messageSendStateId;
	private BigDecimal messageSendStateCode;
	private String messageSendStateName;

	public MessageSendStateEntity() {
	}


	@Id
	@Column(name="MESSAGE_SEND_STATE_ID", unique=true, nullable=false, length=32)
	public String getMessageSendStateId() {
		return this.messageSendStateId;
	}

	public void setMessageSendStateId(String messageSendStateId) {
		this.messageSendStateId = messageSendStateId;
	}


	@Column(name="MESSAGE_SEND_STATE_CODE", nullable=false, precision=10)
	public BigDecimal getMessageSendStateCode() {
		return this.messageSendStateCode;
	}

	public void setMessageSendStateCode(BigDecimal messageSendStateCode) {
		this.messageSendStateCode = messageSendStateCode;
	}


	@Column(name="MESSAGE_SEND_STATE_NAME", nullable=false, length=128)
	public String getMessageSendStateName() {
		return this.messageSendStateName;
	}

	public void setMessageSendStateName(String messageSendStateName) {
		this.messageSendStateName = messageSendStateName;
	}

}