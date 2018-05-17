INSERT INTO DEPARTMENT VALUES ('-1', '', '总公司');

INSERT INTO SECURITY VALUES ('0', '1001', '发起会议');
INSERT INTO SECURITY VALUES ('1', '1002', '修改会议');
INSERT INTO SECURITY VALUES ('2', '1003', '删除会议');
INSERT INTO SECURITY VALUES ('3', '1004', '结束会议');

INSERT INTO SEX VALUES ('0', '01', '男');
INSERT INTO SEX VALUES ('1', '02', '女');

INSERT INTO MEETING_MEMBER_ROLE VALUES ('0', '01', '会议发起人');
INSERT INTO MEETING_MEMBER_ROLE VALUES ('1', '02', '会议主持人');
INSERT INTO MEETING_MEMBER_ROLE VALUES ('2', '03', '其它参会人员');

INSERT INTO ATTACHMENT_CATEGORY VALUES ('0', '01', '普通附件');
INSERT INTO ATTACHMENT_CATEGORY VALUES ('1', '02', '会议记录');

INSERT INTO MEETING_STATE VALUES ('0', '01', '已发起');
INSERT INTO MEETING_STATE VALUES ('1', '02', '已结束');

INSERT INTO MESSAGE_SEND_STATE VALUES ('0', '01', '未发送');
INSERT INTO MESSAGE_SEND_STATE VALUES ('1', '02', '发送失败');
INSERT INTO MESSAGE_SEND_STATE VALUES ('2', '03', '发送成功');