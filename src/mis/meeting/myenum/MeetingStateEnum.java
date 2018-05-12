package mis.meeting.myenum;

public enum MeetingStateEnum {
	/**
	 * 会议发起中
	 */
	MEETING_OPEN,
	
	/**
	 * 会议已结束
	 */
	MEETING_CLOSE;
	
	/**
	 * 根据枚举顺序取枚举值.
	 * 
	 * @param ordinal
	 *             枚举顺序.
	 * @return 枚举顺序取枚举值.
	 */
	public static MeetingStateEnum valueOf(int ordinal) { 
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		}
		
		return values()[ordinal];
	}
}
