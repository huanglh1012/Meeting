INSERT INTO DEPARTMENT VALUES ('-1', NULL, '总公司');
INSERT INTO DEPARTMENT VALUES ('0', '-1', '安全部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '业务部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '零售管理部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '非油品业务部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '财务资产部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '经理办公室');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '人力资源部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '党委办公室');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '城区经营管理部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '阳春经营管理部');
INSERT INTO DEPARTMENT VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '阳西经营管理部');

INSERT INTO POST VALUES ('-1', '综合管理员', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '副经理', '主持行政工作');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '副书记', '主持党务工作');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '经理助理', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '部门主任', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '部门副主任', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '主办', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '业务员A', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '业务员B', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '督查队长', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '经营管理部经理', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '经营管理部书记', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '经营管理部副经理', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '经营管理部副书记', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '站长', '');
INSERT INTO POST VALUES (UPPER(REPLACE(UUID(),'-','')), '普通员工', '');

INSERT INTO ROLE VALUES ('-1', '超级管理员');
INSERT INTO ROLE VALUES ('0', '管理员');
INSERT INTO ROLE VALUES (UPPER(REPLACE(UUID(),'-','')), '公司领导');
INSERT INTO ROLE VALUES (UPPER(REPLACE(UUID(),'-','')), '发起部门');
INSERT INTO ROLE VALUES (UPPER(REPLACE(UUID(),'-','')), '参会部门');

INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1001', '会议发起');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1002', '会议修改');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1003', '会议删除');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1004', '结束会议');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1005', '材料上传');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1006', '材料删除');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1007', '材料下载');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1008', '会议室增加');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1009', '会议室修改');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '1010', '会议室删除');

INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2001', '职务增加');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2002', '职务修改');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2003', '职务删除');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2004', '部门增加');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2005', '部门修改');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2006', '部门删除');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2007', '用户增加');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2008', '用户修改');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2009', '用户删除');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2010', '角色增加');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2011', '角色修改');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2012', '角色删除');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2013', '角色保存');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2014', '角色重置');
INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '2015', '角色全选');

INSERT INTO SECURITY VALUES (UPPER(REPLACE(UUID(),'-','')), '3001', '短信中心保存');

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

INSERT INTO EMPLOYEE VALUES ('100000','-1','0','0','admin','1234.abcd','管理员',NULL,NULL);
INSERT INTO EMPLOYEE_ROLE_RF VALUES (UPPER(REPLACE(UUID(),'-','')),'100000','-1');

SELECT * FROM SECURITY A WHERE A.SECURITY_ID IN (SELECT B.SECURITY_ID FROM ROLE_SECURITY_RF B,
EMPLOYEE_ROLE_RF C WHERE B.ROLE_ID = C.ROLE_ID AND C.EMPLOYEE_ID = ?) 