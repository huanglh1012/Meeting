package mis.meeting.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the meeting_room database table.
 * 
 */
@Entity
@Table(name="MEETING_ROOM",schema="meeting")
public class MeetingRoomEntity extends ecp.bsp.system.core.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String meetingRoomId;
	private String meetingRoomAddress;

	public MeetingRoomEntity() {
	}


	@Id
	@Column(name="MEETING_ROOM_ID", unique=true, nullable=false, length=32)
	public String getMeetingRoomId() {
		return this.getId();
	}

	public void setMeetingRoomId(String meetingRoomId) {
		this.setId(meetingRoomId);
	}


	@Column(name="MEETING_ROOM_ADDRESS", nullable=false, length=1024)
	public String getMeetingRoomAddress() {
		return this.meetingRoomAddress;
	}

	public void setMeetingRoomAddress(String meetingRoomAddress) {
		this.meetingRoomAddress = meetingRoomAddress;
	}

}