package ecp.bsp.system.framework.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.stereotype.Component;

import ecp.bsp.system.framework.query.data.entity.PageQueryCondition;
import ecp.bsp.system.framework.query.data.entity.DynamicGridQueryEntity;
import ecp.bsp.system.commons.utils.StringUtils;
import ecp.bsp.system.framework.query.data.constant.QueryConst;
/**
 * 鏌ヨ鏉′欢鏋勫缓鍣ㄥ疄鐜扮被
 * 
 * @author huanglinhui,zengqingyue
 *
 */
@Scope(value="prototype")
@Component("pageQueryConditionBuilder")
public class PageQueryConditionBuilder extends BaseQueryConditionBuilder {
	
	private Object[] queryCondition ;
	private Class<?>  dtoClass;
	private PageQueryCondition pageQueryCondition = new PageQueryCondition();
	private String sql;
	
	/*
	 * (non-Javadoc)
	 * @see com.eastcompeace.oa.base.BaseQueryConditionBuilder#getQueryConditionString()
	 */
	public Object[] getQueryCondition() {
		return this.queryCondition;	
	}
	public void setSql(String sql){
		this.sql = sql;
	}

	/* (non-Javadoc)
	 * @see com.eastcompeace.oa.base.BaseQueryConditionBuilder#analyseConditionResource()
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void analyseConditionResource() {
		StringBuilder queryConditionBuilder = new StringBuilder();
		List<DynamicGridQueryEntity> dynamicGridQueryEntitys = (List<DynamicGridQueryEntity>) this.getConditionResource();
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		for(DynamicGridQueryEntity dynamicGridQueryEntity : dynamicGridQueryEntitys){
			if(dynamicGridQueryEntity.getIsRaw()){
				conditionMap.put(dynamicGridQueryEntity.getFieldName(), dynamicGridQueryEntity.getStringCondition());
			}else{
				// 閽堝SQL瀹炰綋瀛楁闇�閫氳繃瑙ｆ瀽杞崲涓烘暟鎹簱琛ㄥ瓧娈碉紝鏁版嵁搴撹〃瀛楁鍒欑洿鎺ュ簲鐢ㄤ簬SQL鏌ヨ.
				String tableFieldName = dynamicGridQueryEntity.getIsEntityField() ? 
						StringUtils.getTableFieldName(this.getDtoClass().getName(), dynamicGridQueryEntity.getFieldName()) : dynamicGridQueryEntity.getFieldName();
				if (StringUtils.isValidateString(tableFieldName)) {
					String key = StringUtils.getRandomValue();
					if (StringUtils.isValidateString(queryConditionBuilder.toString())) {
						queryConditionBuilder.append(" AND ");
					}
					// 瀛楃涓叉煡璇㈠寘鍚ā绯婃煡璇㈠拰绮剧‘鏌ヨ锛宑omboBox绫诲瀷鐨勯渶瑕佺簿纭煡璇�
					if(dynamicGridQueryEntity.getType().equals("string")){
						//濡傛灉涓嶅尯鍒嗗ぇ灏忓啓鍒欏皢鏉′欢瀛楁璁剧疆涓哄ぇ鍐�
						String fieldCondition = dynamicGridQueryEntity.getIsCaseSensitive() ? ":"+key : "UPPER(:"+key + ")";
						tableFieldName = dynamicGridQueryEntity.getIsCaseSensitive() ? tableFieldName : "UPPER(" + tableFieldName +")";
						String param = dynamicGridQueryEntity.getIsLike() ? " like "+fieldCondition : " = "+fieldCondition;
						conditionMap.put(key, dynamicGridQueryEntity.getStringCondition());
						queryConditionBuilder.append(tableFieldName).append(param);
					}
					if(dynamicGridQueryEntity.getType().equals("int") || dynamicGridQueryEntity.getType().equals("integer")){
						queryConditionBuilder.append(tableFieldName).append(" >= :"+key+"startNumber AND ")
											 .append(tableFieldName).append(" <= :"+key+"endNumber ");
						conditionMap.put(key+"startNumber", dynamicGridQueryEntity.getStartIntNumber());
						conditionMap.put(key+"endNumber", dynamicGridQueryEntity.getEndIntNumber());
					}
					if(dynamicGridQueryEntity.getType().equals("date")){
						queryConditionBuilder.append(tableFieldName).append(" >= :"+key+"startDate  AND ")
											 .append(tableFieldName).append(" <= :"+key+"endDate ");
						conditionMap.put(key+"startDate", dynamicGridQueryEntity.getStartDate());
						//缁撴潫鏃堕棿鏄竴澶╃殑鏈�悗鏃跺垎绉�
						dynamicGridQueryEntity.getEndDate().setHours(23);
						dynamicGridQueryEntity.getEndDate().setMinutes(59);
						dynamicGridQueryEntity.getEndDate().setMinutes(59);
						conditionMap.put(key+"endDate", dynamicGridQueryEntity.getEndDate());
					}
					if(dynamicGridQueryEntity.getType().equals("checkbox") && dynamicGridQueryEntity.getCheckboxCondition() != null){
						String param = " IN ( ";
						String[] checkboxValueArray = dynamicGridQueryEntity.getCheckboxCondition().split(",");
						for (int i = 0; i < checkboxValueArray.length; i++) {
							if (i == 0)
								param += ":" + key;
							else
								param += " , :" + key;
							
							conditionMap.put(key, checkboxValueArray[i]);
							key = StringUtils.getRandomValue();
						}
						param += " ) ";
						queryConditionBuilder.append(tableFieldName).append(param);
					}
				}
			}
		}
		this.createQuery(queryConditionBuilder,conditionMap);
	}
	
	private void createQuery(StringBuilder queryConditionBuilder,Map<String,Object> conditionMap){
		this.pageQueryCondition = new PageQueryCondition ();
		// 濡傛灉鏈寚瀹氭煡璇㈡潯浠舵彃鍏ヤ綅缃紝鍒欏皢鏌ヨ鏉′欢娣诲姞鍒版渶澶栧眰鍚庨潰.
		// 濡傛灉鎸囧畾浜嗘煡璇㈡潯浠朵綅缃紝鍒欏皢鏌ヨ鏉′欢鎻掑叆鎸囧畾浣嶇疆.
		String sql = this.sql;
		if (sql.indexOf(QueryConst.PARAM_CONDITION_POS_FLAG) < 0) {
			// 鏌ヨ鏉′欢鍔犲湪SQL璇彞锛孲QL璇彞鐨勬煡璇㈡潯浠堕渶瑕佺‘瀹氭潯浠剁殑琛ㄥ埆鍚嶏紝濡傜敤鎴稩D锛屽彲鑳芥槸A.EMPLOYEE_ID锛屼篃鍙兘鏄疊.EMPLOYEE_ID锛岃〃鍒悕鏄疉鎴栨槸B鏃犳硶纭畾锛屾墍浠�
			// 鍦⊿QL澶栧湪濂椾竴灞係QL 鈥淪ELECT * FROM (鍘烻QL)鈥濓紝鏌ヨ鏉′欢灏变笉闇�琛ㄥ埆鍚嶃�
			sql = new StringBuilder("SELECT * FROM (")
			.append(this.sql).append(") MyQuery "+((null ==queryConditionBuilder || queryConditionBuilder.toString().equals(StringUtils.EMPTY_STRING))?"":" WHERE "))
			.append(queryConditionBuilder.toString())
			.toString();
		} else {
			// 鏌ヨ鏉′欢浣嶇疆宸插簲纭畾鍦⊿QL鐨勬煇涓綅缃紝杩欎釜浣嶇疆鐨勬潯浠跺繀椤绘槸涓嶉渶瑕佽〃鍒悕鐨勶紝鍦ㄥ畾涔塖QL鏃跺氨宸茬粡绾﹀畾锛屾墍浠ヨ繖閲屼笉闇�鍐嶅濂椾竴灞係QL銆�
			// 濡傛灉鏃犳煡璇㈡潯浠讹紝鍒欐煡璇㈡潯浠舵爣璇嗙瑕佺敤绌哄�瀛楃涓叉垨绌烘牸涓叉浛鎹紝
			// 濡傛灉瀛樺湪鏌ヨ鏉′欢锛屽垯鐢ㄢ� " WHERE " + 鏌ヨ鏉′欢鈥�鏇挎崲鏌ヨ鏉′欢鏍囪瘑绗︺�
			String queryConditionString = 
					(null ==queryConditionBuilder || queryConditionBuilder.toString().equals(StringUtils.EMPTY_STRING)) 
					? StringUtils.EMPTY_STRING : " WHERE " + queryConditionBuilder.toString();
			sql = sql.replace(QueryConst.PARAM_CONDITION_POS_FLAG, queryConditionString);			
		}
		
		Object[] parameters = NamedParameterUtils.buildValueArray(sql, conditionMap); 
		String sqlToUse = NamedParameterUtils.substituteNamedParameters(sql, new MapSqlParameterSource(conditionMap));
		
		// 灏嗘彁鍙栫殑杈撳嚭鍙傛暟淇濆瓨鍒板睘鎬т腑.
		pageQueryCondition.setExecuteSql(sqlToUse);
		pageQueryCondition.setParameters(parameters);
	}

	@Override
	public PageQueryCondition getPageQueryCondition() {
		// TODO Auto-generated method stub
		return this.pageQueryCondition;
	}
	@Override
	public void setDtoClass(Class<?>  dtoClass) {
		this.dtoClass = dtoClass	;
	}
	public Class<?> getDtoClass() {
		return this.dtoClass;
	}
	
}
