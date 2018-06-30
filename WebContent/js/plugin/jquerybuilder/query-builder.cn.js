/*!
 * jQuery QueryBuilder 2.1.0(依赖于QueryBuilder)
 * Locale: 中文 (cn)
 * Author: Zengqingyue
 */

(function(root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery', 'query-builder'], factory);
    }
    else {
        factory(root.jQuery);
    }
}(this, function($) {
"use strict";

var QueryBuilder = $.fn.queryBuilder;

QueryBuilder.regional['cn'] = {
  "__locale": "中文 (cn)",
  "__author": "Zengqingyue",
  "add_rule": "添加规则",
  "add_group": "添加组",
  "delete_rule": "删除",
  "delete_group": "删除",
  "conditions": {
    "AND": "并集",
    "OR": "或集"
  },
  "operators": {
    "equal": "等于",
    "not_equal": "不等于",
    "in": "在...之内",
    "not_in": "不在...之内",
    "less": "小于",
    "less_or_equal": "小于等于",
    "greater": "大于",
    "greater_or_equal": "大于等于",
    "between": "在...之间",
    "begins_with": "开始于",
    "not_begins_with": "不开始于",
    "contains": "包含",
    "not_contains": "不包含",
    "ends_with": "结束于",
    "not_ends_with": "不结束于",
    "is_empty": "空字符串",
    "is_not_empty": "不为空字符串",
    "is_null": "空值",
    "is_not_null": "不为空值"
  },
  "errors": {
    "no_filter": "没有选择过滤器",
    "empty_group": "组为空",
    "radio_empty": "未选择值",
    "checkbox_empty": "未选择值",
    "select_empty": "未选择值",
    "string_empty": "空值",
    "string_exceed_min_length": "必须包含最少 {0} 特性",
    "string_exceed_max_length": "必须不包含最多 {0} 特性",
    "string_invalid_format": "无效的转化 ({0})",
    "number_nan": "不是数字",
    "number_not_integer": "不是整型",
    "number_not_double": "不是浮点型",
    "number_exceed_min": "必须大于{0}",
    "number_exceed_max": "必须小于 {0}",
    "number_wrong_step": "必须是一个多 {0}",
    "datetime_empty": "空值",
    "datetime_invalid": "无效的日期转化 ({0})",
    "datetime_exceed_min": "必须晚于 {0}",
    "datetime_exceed_max": "必须早于 {0}",
    "boolean_not_valid": "无效的布尔类型",
    "operator_not_multiple": "操作 {0} 不能接受多个值"
  }
};

QueryBuilder.defaults({ lang_code: 'cn' });
}));