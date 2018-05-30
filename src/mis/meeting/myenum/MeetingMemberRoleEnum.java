package mis.meeting.myenum;

public enum MeetingMemberRoleEnum {
	/**
	 * 会议发起人
	 */
	MEETING_CREATOR,
	
	/**
	 * 会议主持人
	 */
	MEETING_PRESENTER,
	
	/**
	 * 参会人员
	 */
	MEETING_PARTICIPANT;
	
	/**
	 * 根据枚举顺序取枚举值.
	 * 
	 * @param ordinal
	 *             枚举顺序.
	 * @return 枚举顺序取枚举值.
	 */
	public static MeetingMemberRoleEnum valueOf(int ordinal) { 
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		}
		
		return values()[ordinal];
	}
}
