package com.qinjiangbo.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest2 {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		Map map = new HashMap(2);  
	    map.put("name", "Jame Gosling");  
	    map.put("userId", 18345678546L);
	    map.put("password", "1111111");
//	    System.out.println(map.size());
//	      
	    String line = "insert into user values(${userId}, ${ name }, ${ password })";  
//	        
//	    String regex = "\\$\\{[^\\}]+\\}";  
//	      
//	    Pattern p = Pattern.compile(regex);  
//	    Matcher m = p.matcher(line); 
//	      
//	    String g;  
//	    while (m.find()) {  
//	        g = m.group();
//	        System.out.println(g);
//	        g = g.substring(2, g.length() - 1).replaceAll("^\\s+|\\s+$", "");
//	        line = m.replaceFirst(map.get(g) + "");  
//	        m = p.matcher(line);
//	    }  
	    line = replace(line, map);
	    System.out.println(line);  
	}
	
	@SuppressWarnings("rawtypes")
	private static String replace(String sql, Map params) {
		/* special characters need \\ to escape */
		Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}");
		Matcher matcher = pattern.matcher(sql);
		String matchStr = null;
		while(matcher.find()) {
			matchStr = matcher.group();
			String originStr = matchStr;
			matchStr = matchStr.substring(2, matchStr.length() - 1).replaceAll("^\\s+|\\s+$", "");
			Object paramValue = params.get(matchStr);
			//String object needs quotas
			if(paramValue instanceof String
					|| paramValue instanceof Character) {
				paramValue = "'"+paramValue+"'";
			}
			sql = sql.replace(originStr, paramValue.toString());
			matcher = pattern.matcher(sql);
		}
		return sql;
	}
}
