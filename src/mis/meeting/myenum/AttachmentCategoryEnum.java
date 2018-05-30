package mis.meeting.myenum;

public enum AttachmentCategoryEnum {
	/**
	 * 普通附件
	 */
	NORMAL_ATTACHMENT,
	
	/**
	 * 会议记录
	 */
	MEETING_RECORD_ATTACHMENT;
	
	/**
	 * 根据枚举顺序取枚举值.
	 * 
	 * @param ordinal
	 *             枚举顺序.
	 * @return 枚举顺序取枚举值.
	 */
	public static AttachmentCategoryEnum valueOf(int ordinal) { 
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		}
		
		return values()[ordinal];
	}
}
