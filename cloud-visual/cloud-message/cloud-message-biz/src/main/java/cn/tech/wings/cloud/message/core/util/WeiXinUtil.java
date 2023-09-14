package cn.tech.wings.cloud.message.core.util;

import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 公众平台通用接口工具类
 * @author liuyq
 * @date 2019年11月18日11:30:24
 */
@Slf4j
public class WeiXinUtil {

	private static ObjectMapper objectMapper = null;

	private static final String appId = "wx1e4c71ccbc89282a";
	private static final String appSecret = "d847ec4a56c6589971add0e3357a6719";

	public static ObjectMapper getIntegerance() {
		if (objectMapper == null){
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}

	public static String toJson(Object obj) {
		StringWriter stringWriter = new StringWriter();
		try {
			getIntegerance().writeValue(stringWriter, obj);
			String ss = stringWriter.toString();
			ss = thkey(ss).replaceAll("\";:", "\":");
			return ss;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				stringWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String thkey(String str) {
		int ei = str.indexOf("\":");
		if (ei != -1) {
			String substr = str.substring(0, ei);
			int si = substr.lastIndexOf("\"");
			String subkey = substr.substring(si, ei);
			str = str.replace(subkey + "\":", subkey.toLowerCase() + "\";:");
			str = thkey(str);
		}
		return str;
	}

	/**
	 * 发起https请求并获取结果
	 *
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static String httpRequestStr(String requestUrl, String requestMethod, String outputStr) {
		String result = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)){
                httpUrlConn.connect();
            }

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			result = buffer.toString();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发起https请求并获取结果
	 *
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		String str = httpRequestStr(requestUrl, requestMethod, outputStr);
		JSONObject jsonObject = JSONObject.parseObject(str);
		return jsonObject;
	}
	public static Map httpRequest2(String requestUrl, String requestMethod, String outputStr) {
		String str = httpRequestStr(requestUrl, requestMethod, outputStr);
		Map map = JsonUtil.parseJSON2Map(str);
		return map;
	}

	// 获取服务号access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	/**
	 * 获取服务号access_token
	 *
	 * @param appid
	 *            凭证
	 * @param secret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String secret) {
		return getAccessToken(appid,secret,true);
	}
	public static AccessToken getAccessToken(String appid, String secret,boolean cache) {
		String key = "Cache_AccessToken_GZH:" + appid;
		RedisTemplate redisUtils = null;
		if(cache){
			//开启缓存
			redisUtils = SpringContextHolder.getBean(RedisTemplate.class);
		}
		System.out.println("get getAccessToken++++++++++:" + appid);
		AccessToken a = null;
		Map amap = null;
		if(redisUtils!=null){
			amap = (Map)redisUtils.opsForValue().get(key);
		}
		if (amap != null) {
			System.out.println("get Cache_AccessToken_GZH" + appid + "++++++++++:" + amap.toString());
			a = new AccessToken();
			a.setExpiresDate( new Date((Long) amap.get("expiresDate")));
			if (amap.get("expiresIn") instanceof Integer) {
				a.setExpiresIn(Integer.parseInt(amap.get("expiresIn").toString()));
			}
			a.setJsapiTicket((String) amap.get("jsapi_ticket"));
			a.setToken((String) amap.get("token"));
		}else if (a == null || a.getExpiresDate().getTime() < (new Date()).getTime()) {

			String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", secret);
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			System.out.println("get jsonObject" + requestUrl + "++++++++++:" + jsonObject.toString());
			// 如果请求成功
			if (null != jsonObject) {
				try {
					a = new AccessToken();
					a.setToken(jsonObject.getString("access_token"));
					int expires_in = jsonObject.getInteger("expires_in");
					a.setExpiresIn(expires_in);
					a.setExpiresDate(new Date(System.currentTimeMillis() + (expires_in-20)*1000));
					a.setJsapiTicket(getJsapiTicket(a.getToken()));

					amap = new HashMap();
					amap.put("expiresDate", a.getExpiresDate());
					amap.put("expiresIn", a.getExpiresIn());
					amap.put("jsapi_ticket", a.getJsapiTicket());
					amap.put("token", a.getToken());

					if(redisUtils!=null){
						//设置缓存 2小时
						redisUtils.opsForValue().set(key, amap,7000);
					}

					System.out.println("set Cache_AccessToken_GZH" + appid + "++++++++++:" + amap.toString());
				} catch (JSONException e) {
				}
			}
		}
		System.out.println("etAccessToken++++++++++:token:" + a.getToken()+" time:"+a.getExpiresDate());
		return a;
	}
	/**
	 * 获取服务号access_token测试用
	 *
	 * @param appid
	 *            凭证
	 * @param secret
	 *            密钥
	 * @return
	 */
	private static AccessToken getAccessTokenTest(String appid, String secret) {
		AccessToken a = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", secret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				a = new AccessToken();
				a.setToken(jsonObject.getString("access_token"));
				a.setExpiresIn(7200);
				Date now = new Date();
				a.setExpiresDate(new Date(now.getTime() + 7100000));
				String jsapi_ticket = getJsapiTicket(a.getToken());
				a.setJsapiTicket(jsapi_ticket);
			} catch (JSONException e) {
			}
		}
		// }
		return a;
	}

	private static String getJsapiTicket(String token) {
		try {
			if (StringUtils.hasLength(token)) {
				String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", token);
				JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
				// 如果请求成功
				if (null != jsonObject) {
					System.out.println("jsapi result:" + jsonObject.toString());
					return jsonObject.getString("ticket");
				}
			}
		} catch (Exception e) {
		}
		return "";
	}


	// 获取企业号access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url_qyh = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CORPID&corpsecret=CORPSECRET";
	public final static String jsapi_ticket_url_qyh = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";

	/**
	 * 获取企业号accessToken
	 *
	 * @Title: getAccessTokenQyh
	 * @Description:
	 * @param corpid
	 * @param secret
	 * @return
	 * @return: AccessToken
	 * @author: zengmin
	 * @date: 2017年5月14日 下午4:40:08
	 */
	public static AccessToken getAccessTokenQyh(String corpid, String secret) {
		String key = "Cache_AccessToken_QYH" + corpid;
		RedisTemplate redisUtils = SpringContextHolder.getBean(RedisTemplate.class);
		System.out.println("get getAccessTokenQYH++++++++++:" + corpid);
		AccessToken a = null;
		Map amap = null;
		if(redisUtils!=null){
			amap = (Map)redisUtils.opsForValue().get(key);
		}
		if (amap != null) {
			System.out.println("get Cache_AccessToken_QYH" + corpid + "++++++++++:" + amap.toString());
			a = new AccessToken();
			a.setExpiresDate((Date) amap.get("expiresDate"));
            if (amap.get("expiresIn") instanceof Integer) {
                a.setExpiresIn(Integer.parseInt(amap.get("expiresIn").toString()));
            }
			a.setJsapiTicket((String) amap.get("jsapi_ticket"));
			a.setToken((String) amap.get("token"));
		}
		if (a == null || a.getExpiresDate().getTime() < (new Date()).getTime()) {
			String requestUrl = access_token_url_qyh.replace("CORPID", corpid).replace("CORPSECRET", secret);
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			// 如果请求成功
			if (null != jsonObject) {
				try {
					a = new AccessToken();
					a.setToken(jsonObject.getString("access_token"));
					a.setExpiresIn(7200);
					Date now = new Date();
					a.setExpiresDate(new Date(now.getTime() + 7100000));
					String jsapi_ticket = getJsapiTicketQyh(a.getToken());
					a.setJsapiTicket(jsapi_ticket);

					amap = new HashMap();
					amap.put("expiresDate", a.getExpiresDate());
					amap.put("expiresIn", a.getExpiresIn());
					amap.put("jsapi_ticket", a.getJsapiTicket());
					amap.put("token", a.getToken());
					//设置缓存 2小时
					redisUtils.opsForValue().set(key, amap,7000);

					System.out.println("set Cache_AccessToken_QYH" + corpid + "++++++++++:" + amap.toString());
				} catch (JSONException e) {
				}
			}
		}
		return a;
	}

	private static String getJsapiTicketQyh(String token) {
		try {
			if (StringUtils.hasLength(token)) {
				String requestUrl = jsapi_ticket_url_qyh.replace("ACCESS_TOKEN", token);
				JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
				// 如果请求成功
				if (null != jsonObject) {
					System.out.println("jsapi result:" + jsonObject.toString());
					return jsonObject.getString("ticket");
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	// 菜单创建（POST） 限100（次/天）

	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	// 删除菜单 限100（次/天）

	public static String delete_menu_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 *
	 * @param accessToken
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int deleteMenu(String accessToken) {
		int result = 0;
		// 拼装创建菜单的url
		String url = delete_menu_url.replace("ACCESS_TOKEN", accessToken);

		String s = httpRequestStr(url, "GET", null);
		JSONObject jsonObject = JSONObject.parseObject(s);
		if (null != jsonObject) {
			if (0 != jsonObject.getInteger("errcode")) {
				result = jsonObject.getInteger("errcode");
			}
		}
		return result;
	}

	public static int deleteMenu(String corpid, String secret) {
		AccessToken a = getAccessToken(corpid, secret);
		if (null == a) {
			return -1;
		}
		return deleteMenu(a.getToken());
	}

	public static String view_menu_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

	/**
	 * 查看菜单
	 *
	 * @param accessToken
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static String viewMenu(String accessToken) {
		int result = 0;
		// 拼装创建菜单的url
		String url = view_menu_url.replace("ACCESS_TOKEN", accessToken);

		String s = httpRequestStr(url, "GET", null);
		return s;
	}

	public static String viewMenu(String corpid, String secret) {
		AccessToken a = getAccessToken(corpid, secret);
		if (null == a) {
			return "";
		}
		return viewMenu(a.getToken());
	}

	// 获取部门列表
	public final static String getorg_url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN";

	public static JSONObject getOrgs(String corpid, String secret) {
		AccessToken at = WeiXinUtil.getAccessTokenQyh(corpid, secret);
		String requestUrl = getorg_url.replace("ACCESS_TOKEN", at.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		return jsonObject;
	}

	// 获取部门成员
	public final static String getuserbyorgid_url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=ORGID&fetch_child=0&status=1";

	public static JSONObject getUserByOrgId(String corpid, String secret, String orgId) {
		AccessToken at = WeiXinUtil.getAccessTokenQyh(corpid, secret);
		String requestUrl = getuserbyorgid_url.replace("ACCESS_TOKEN", at.getToken()).replace("ORGID", orgId);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		return jsonObject;
	}

	// 获取成员
	public final static String getuserbyUserid_url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";

	public static JSONObject getUserByUserId(String corpid, String secret, String userid) {
		AccessToken at = WeiXinUtil.getAccessTokenQyh(corpid, secret);
		String requestUrl = getuserbyUserid_url.replace("ACCESS_TOKEN", at.getToken()).replace("USERID", userid);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		return jsonObject;
	}

	// 创建成员
	public final static String createUser_url = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN";

	public static JSONObject createUser(String corpid, String secret, String data) {
		AccessToken at = WeiXinUtil.getAccessTokenQyh(corpid, secret);
		String requestUrl = createUser_url.replace("ACCESS_TOKEN", at.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "POST", data);
		return jsonObject;
	}

	// 创建成员
	public final static String updateUser_url = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN";

	public static JSONObject updateUser(String corpid, String secret, String data) {
		AccessToken at = WeiXinUtil.getAccessTokenQyh(corpid, secret);
		String requestUrl = updateUser_url.replace("ACCESS_TOKEN", at.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "POST", data);
		return jsonObject;
	}

	// 批量删除成员
	public final static String deleteUser_url = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=ACCESS_TOKEN&userid=USERID";

	public static JSONObject deleteUser(String corpid, String secret, String userId) {
		AccessToken at = WeiXinUtil.getAccessTokenQyh(corpid, secret);
		String requestUrl = deleteUser_url.replace("ACCESS_TOKEN", at.getToken()).replace("USERID", userId);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		return jsonObject;
	}

	public final static String batchDeleteUser_url = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=ACCESS_TOKEN";

	public static JSONObject deleteUserAll(String corpid, String secret, String data) {
		AccessToken at = WeiXinUtil.getAccessTokenQyh(corpid, secret);
		String requestUrl = batchDeleteUser_url.replace("ACCESS_TOKEN", at.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "POST", data);
		return jsonObject;
	}




	public final static String send_temp_msg_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	/*public static String sendTempMsg(String openid) {
		// Map data = new LinkedMap();
		// data.put("first", new TemplateWXMsgKey("您好，您已成功支付。", "#173177"));
		// data.put("keyword1", new TemplateWXMsgKey("王府井百货大楼", "#173177"));
		// data.put("keyword2", new TemplateWXMsgKey("20150822324432",
		// "#173177"));
		// data.put("keyword3", new TemplateWXMsgKey("90元", "#173177"));
		// data.put("keyword4", new TemplateWXMsgKey("2015-8-22 11:00",
		// "#173177"));
		// data.put("remark", new TemplateWXMsgKey("感谢您的光临！点击详情查看订单明细。",
		// "#173177"));
		//
		Map twm = new LinkedMap();
		twm.put("touser", openid);
		twm.put("template_id", "Ias6HawLv41a_HQDM_D7ZbESi4xEDxUh1ER6uycqfEU");
		twm.put("url", "http://www.baidu.com");
		twm.put("topcolor", "#FF0000");
		twm.put("data", null);

		String jsontempmsg = JSONObject.parseObject(twm).toString();
		return sendTempMsg(openid, jsontempmsg);
	}*/

	public static String sendTempMsg(String openid, String content) {
		AccessToken at = WeiXinUtil.getAccessToken(appId, appSecret);
		String requestUrl = send_temp_msg_url.replace("ACCESS_TOKEN", at.getToken());
		System.out.println(content);
		String s = httpRequestStr(requestUrl, "POST", content);
		int result = 0;
		JSONObject jsonObject = JSONObject.parseObject(s);
		if (null != jsonObject) {
			if (0 != jsonObject.getInteger("errcode")) {
				result = jsonObject.getInteger("errcode");
				//log.error("发送信息失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return s;
	}


	public static char getValidateCode(String id17) {
		// 数字本体码权重
		int[] weight = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		// mod11,对应校验码字符值
		char[] validate = { '1', '0', '3', '9', '8', '7', '6', '5', '4', '3', '2' };
		int sum = 0;
		int mode = 0;
		for (int i = 0; i < id17.length(); i++) {
			sum = sum + Integer.parseInt(String.valueOf(id17.charAt(i))) * weight[i];
		}
		mode = sum % 11;
		return validate[mode];
	}


	public static final String FWH_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * 发送服务号客服消息
	 *
	 * @Title: sendKfMsgFwh
	 * @Description:
	 * @param  appid,
	 * @param secret
	 * @param content
	 * @return
	 * @return: boolean
	 * @author: zengmin
	 * @date: 2017年4月17日 下午3:21:30
	 */
	public static boolean sendKfMsgFwh(String appid, String secret, String content) {
		AccessToken at = getAccessToken(appid, secret);
		String requestUrl = FWH_MSG_URL.replace("ACCESS_TOKEN", at.getToken());
		System.out.println(content);
		String s = httpRequestStr(requestUrl, "POST", content);
		JSONObject jsonObject = JSONObject.parseObject(s);
		if (null != jsonObject) {
			if (0 != jsonObject.getInteger("errcode")) {
				System.out.println("发送信息失败 errcode:{" + jsonObject.getInteger("errcode") + "} errmsg:{"
						+ jsonObject.getString("errmsg") + "}");
				return false;
			}
		}
		return true;
	}

	/**
	 * 发送服务号客服消息--测试使用
	 *
	 * @Title: sendKfMsgFwhTest
	 * @Description:
	 * @param content
	 * @param content
	 * @return
	 * @return: boolean
	 * @author: zengmin
	 * @date: 2017年4月17日 下午3:21:30
	 */
	public static boolean sendKfMsgFwhTest(String content) {
		AccessToken at = WeiXinUtil.getAccessTokenTest(appId, appSecret);
		String requestUrl = FWH_MSG_URL.replace("ACCESS_TOKEN", at.getToken());
		System.out.println(content);
		String s = httpRequestStr(requestUrl, "POST", content);
		JSONObject jsonObject = JSONObject.parseObject(s);
		if (null != jsonObject) {
			if (0 != jsonObject.getInteger("errcode")) {
				System.out.println("发送信息失败 errcode:{" + jsonObject.getInteger("errcode") + "} errmsg:{"
						+ jsonObject.getString("errmsg") + "}");
				return false;
			}
		}
		return true;
	}

	/**
	 * sha1签名算法
	 *
	 * @param signatureParam
	 * @return
	 */
	public static String sha1(String signatureParam) {
		/*try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";*/

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(signatureParam.getBytes("UTF-8"));
			return byteToHex(crypt.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
