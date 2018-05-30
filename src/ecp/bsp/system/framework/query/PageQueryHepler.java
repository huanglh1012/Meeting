package ecp.bsp.system.framework.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ecp.bsp.system.framework.query.impl.limitPage.LimitPageContext;
import ecp.bsp.system.framework.query.data.entity.DataDialect;
import ecp.bsp.system.framework.query.impl.limitPage.MySqlLpageImpl;
import ecp.bsp.system.framework.query.impl.limitPage.OracleLpageImpl;
import ecp.bsp.system.framework.query.impl.limitPage.SqlServerLpageImpl;
import ecp.bsp.system.framework.query.data.entity.DynamicGridQueryEntity;
import ecp.bsp.system.commons.utils.jackson.JSONUtil;
/**
 * 鍒嗛〉鏌ヨ宸ュ叿绫�鐢熸垚sql璇彞
 * 
 * @author zengqingyue
 * 
 * @since 2016-08-30
 *
 */
public class PageQueryHepler {

	/** 鏍规嵁鍒嗛〉锛屾煡璇㈡暟鎹�
	 * @param sql
	 * @param start
	 * @param limit
	 * @param type
	 * @return
	 */
	public static String createQueryPageSQL(String sql, Integer start,Integer limit,String type) {
		LimitPageContext limitpageContext;
		if (type.equals(DataDialect.SQLSERVER)) {
			limitpageContext = new LimitPageContext(new SqlServerLpageImpl());
		} else if (type.equals(DataDialect.MYSQL)) {
			limitpageContext = new LimitPageContext(new MySqlLpageImpl());
		} else {
			limitpageContext = new LimitPageContext(new OracleLpageImpl());
		}
		String pagesql = limitpageContext.createQueryPageSQL(sql,start,limit);
		
		return pagesql;
	}
	
	
	/** 鏌ヨ鎬绘暟
	 * @param sql
	 * @param type
	 * @return
	 */
	public static String createTotalSQL(String sql,String type) {
		LimitPageContext limitpageContext;
		if (type.equals(DataDialect.SQLSERVER)) {
			limitpageContext = new LimitPageContext(new SqlServerLpageImpl());
		} else if (type.equals(DataDialect.MYSQL)) {
			limitpageContext = new LimitPageContext(new MySqlLpageImpl());
		}  else {
			limitpageContext = new LimitPageContext(new OracleLpageImpl());
		}
		String countSQL = limitpageContext.createTotalCountSQL(sql);
		
		return countSQL;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<DynamicGridQueryEntity> createConditions(List queryCondition ){
		List<DynamicGridQueryEntity> results = new ArrayList<DynamicGridQueryEntity>();
		List<LinkedHashMap> dynamicGridQueryEntitys = new ArrayList<LinkedHashMap>();
	 	dynamicGridQueryEntitys = queryCondition ;
		for(LinkedHashMap dynamicGridQueryLinkedMap : dynamicGridQueryEntitys){
			String mapString = JSONUtil.toJSON(dynamicGridQueryLinkedMap);
			DynamicGridQueryEntity dynamicGridQueryEntity = JSONUtil.fromJSON(mapString, DynamicGridQueryEntity.class);
			results.add(dynamicGridQueryEntity);
		}
		
		return results;
	}
}
