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
INSERT INTO ROLE VALUES ('1', '公司领导');
INSERT INTO ROLE VALUES (UPPER(REPLACE(UUID(),'-','')), '发起部门');
INSERT INTO ROLE VALUES (UPPER(REPLACE(UUID(),'-','')), '参会部门');

INSERT INTO SECURITY VALUES ('10000', '10000', '会议');
INSERT INTO SECURITY VALUES ('10001', '10001', '会议管理');
INSERT INTO SECURITY VALUES ('10002', '10002', '材料检索');
INSERT INTO SECURITY VALUES ('10003', '10003', '会议室设置');
INSERT INTO SECURITY VALUES ('1001', '1001', '会议发起');
INSERT INTO SECURITY VALUES ('1002', '1002', '会议修改');
INSERT INTO SECURITY VALUES ('1003', '1003', '会议删除');
INSERT INTO SECURITY VALUES ('1004', '1004', '结束会议');
INSERT INTO SECURITY VALUES ('1005', '1005', '材料上传');
INSERT INTO SECURITY VALUES ('1006', '1006', '材料删除');
INSERT INTO SECURITY VALUES ('1007', '1007', '材料下载');
INSERT INTO SECURITY VALUES ('1008', '1008', '会议室增加');
INSERT INTO SECURITY VALUES ('1009', '1009', '会议室修改');
INSERT INTO SECURITY VALUES ('1010', '1010', '会议室删除');

INSERT INTO SECURITY VALUES ('20000', '20000', '权限');
INSERT INTO SECURITY VALUES ('20001', '20001', '职务管理');
INSERT INTO SECURITY VALUES ('20002', '20002', '部门管理');
INSERT INTO SECURITY VALUES ('20003', '20003', '用户管理');
INSERT INTO SECURITY VALUES ('20004', '20004', '角色管理');
INSERT INTO SECURITY VALUES ('2001', '2001', '职务增加');
INSERT INTO SECURITY VALUES ('2002', '2002', '职务修改');
INSERT INTO SECURITY VALUES ('2003', '2003', '职务删除');
INSERT INTO SECURITY VALUES ('2004', '2004', '部门增加');
INSERT INTO SECURITY VALUES ('2005', '2005', '部门修改');
INSERT INTO SECURITY VALUES ('2006', '2006', '部门删除');
INSERT INTO SECURITY VALUES ('2007', '2007', '用户增加');
INSERT INTO SECURITY VALUES ('2008', '2008', '用户修改');
INSERT INTO SECURITY VALUES ('2009', '2009', '用户删除');
INSERT INTO SECURITY VALUES ('2010', '2010', '角色增加');
INSERT INTO SECURITY VALUES ('2011', '2011', '角色修改');
INSERT INTO SECURITY VALUES ('2012', '2012', '角色删除');
INSERT INTO SECURITY VALUES ('2013', '2013', '角色保存');
INSERT INTO SECURITY VALUES ('2014', '2014', '角色重置');
INSERT INTO SECURITY VALUES ('2015', '2015', '角色全选');

INSERT INTO SECURITY VALUES ('30000', '30000', '系统设置');
INSERT INTO SECURITY VALUES ('30001', '30001', '短信中心设置');
INSERT INTO SECURITY VALUES ('3001', '3001', '短信中心保存');

INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '10000');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '10001');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '10002');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '10003');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '20000');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '20001');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '20002');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '20003');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '20004');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '30000');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '30001');

INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1001');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1002');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1003');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1004');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1005');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1006');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1007');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1008');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1009');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '1010');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2001');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2002');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2003');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2004');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2005');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2006');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2007');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2008');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2009');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2010');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2011');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2012');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2013');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2014');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '2015');
INSERT INTO ROLE_SECURITY_RF VALUES (UPPER(REPLACE(UUID(),'-','')), '-1', '3001');

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