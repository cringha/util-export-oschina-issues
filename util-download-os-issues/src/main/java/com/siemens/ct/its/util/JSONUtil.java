package com.siemens.ct.its.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
 
public class JSONUtil {
	public static final String DATETIMEFORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss.SSS";

	public static int DEFAULT_PARSER_FEATURE;

	static {
		int features = 0;
		features |= Feature.AutoCloseSource.getMask();
		features |= Feature.InternFieldNames.getMask();
		// features |= Feature.UseBigDecimal.getMask();
		features |= Feature.AllowUnQuotedFieldNames.getMask();
		features |= Feature.AllowSingleQuotes.getMask();
		features |= Feature.AllowArbitraryCommas.getMask();
		features |= Feature.SortFeidFastMatch.getMask();
		features |= Feature.IgnoreNotMatch.getMask();
		DEFAULT_PARSER_FEATURE = features;
		
		JSON.DEFAULT_PARSER_FEATURE = features;
	}

	public static String toJSONString(Object obj , String format ) {
		return JSON.toJSONStringWithDateFormat(obj,
				format);

	}
	public static String toJSONString(Object obj) {
		return JSON.toJSONStringWithDateFormat(obj,
				JSONUtil.DATETIMEFORMAT_NORMAL);

	}

	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz, DEFAULT_PARSER_FEATURE);
	}
	public static <T> List<T> parseArray(String json, Class<T> clazz) {
		return JSON.parseArray( json  , clazz);
	}
	

	public static String exceptionToString(Throwable e) {
		if (e == null)
			return null;

		Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		return result.toString();

	}
}
