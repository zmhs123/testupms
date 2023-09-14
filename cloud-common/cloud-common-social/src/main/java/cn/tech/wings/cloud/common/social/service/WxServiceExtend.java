package cn.tech.wings.cloud.common.social.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.tech.wings.cloud.common.social.constant.WxMaApiConstant;
import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import com.google.gson.JsonObject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.SimplePostRequestExecutor;

/**
 * @author: ZHENGXIAOCONG
 * @Description:
 * @date 2022/3/1013:05
 */
public class WxServiceExtend {

	public static WxMaPhoneNumberInfo phoneNumberInfo(String appId, String code) {
		String phone_info = null;
		try {
			WxMaService wxMaService = SpringContextHolder.getBean(WxMaService.class);
			wxMaService.switchover(appId);
			JsonObject param = new JsonObject();
			param.addProperty("code", code);
			String response = wxMaService.execute(SimplePostRequestExecutor.create(wxMaService.getRequestHttp()), WxMaApiConstant.POST_PHONE_NUMBER, param.toString());
			JSONObject responseObj = JSONUtil.parseObj(response);
			phone_info = responseObj.getStr("phone_info");
		} catch (WxErrorException e) {
//			throw new Exception(e.getError().getErrorCode(), e.getError().getErrorMsg());
		}
		return WxMaPhoneNumberInfo.fromJson(phone_info);
	}
}
