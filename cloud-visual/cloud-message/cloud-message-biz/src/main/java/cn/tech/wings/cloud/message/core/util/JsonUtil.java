package cn.tech.wings.cloud.message.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * json工具类
 * @Author mgp
 * @Date 2022/6/24
 **/
public class JsonUtil {

	public static Map<String, Object> parseJSON2Map(String rs) {
		Map<String, Object> map = new HashMap<String, Object>();
		/*JSONParser parser = new JSONParser();
		// 最外层解析
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(rs);
			for (Object k : json.keySet()) {
				Object v = json.get(k);
				// 如果内层还是数组的话，继续解析
				if (v instanceof JSONArray) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Iterator<JSONObject> it = ((JSONArray) v).iterator();
					while (it.hasNext()) {
						JSONObject json2 = it.next();
						list.add(parseJSON2Map(json2.toString()));
					}
					map.put(k.toString(), list);
				} else {
					map.put(k.toString(), v);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}*/
		return map;
	}

	/**
	 * 对象转换为字符串
	 * @Author mgp
	 * @Date 2022/6/24
	 **/
	public static String objectToStr(Object obj){
		ObjectMapper mapper = new ObjectMapper();
		String str = null;
		try {
			str = mapper.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

}
