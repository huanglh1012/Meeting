package mis.shortmessage.myenum;

public enum MessageSendStateEnum {
	/**
	 * 未发送
	 */
	UNSEND,
	/**
	 * 发送失败
	 */
	SEND_FAILED,
	/**
	 * 发送成功
	 */
	SEND_SUCCESSED;
	
	/**
	 * 根据枚举顺序取枚举值.
	 * 
	 * @param ordinal
	 *             枚举顺序.
	 * @return 枚举顺序取枚举值.
	 */
	public static MessageSendStateEnum valueOf(int ordinal) { 
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		}
		
		return values()[ordinal];
	}
}
