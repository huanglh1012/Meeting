package ecp.bsp.system.framework.query.impl.limitPage;

import org.apache.commons.lang.StringUtils;

public class MySqlLpageImpl implements LimitPage {

	@Override
	public String createQueryPageSQL(String sql,Integer start,Integer limit) {
		if (StringUtils.isEmpty(sql)) {
			return null;
		}
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT temp.* ,(@rownum:=@rownum+1) num FROM ( ");
		paginationSQL.append(sql);
		paginationSQL.append(") temp, (select(@rownum:=0)) B where (@rownum:=@rownum+1) < " + (start+limit));
		paginationSQL.append(" ) temp2 WHERE num >= " + start);
		return paginationSQL.toString();
	}

	@Override
	public String createTotalCountSql(String sql) {
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
		totalSQL.append(sql);
		totalSQL.append(") totalTable ");
		return totalSQL.toString();
	}
}
