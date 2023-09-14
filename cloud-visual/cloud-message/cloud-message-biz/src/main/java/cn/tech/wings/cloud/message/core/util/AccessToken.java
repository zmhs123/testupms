package cn.tech.wings.cloud.message.core.util;

import java.util.Date;


/**
 * 微信通用接口凭证
 *
 * @author mgp
 * @date 2016-08-08
 */
public class AccessToken {
	/**
	 * 获取到的凭证
	 */
	private String token;
	/**
	 * 凭证有效时间，单位：秒
	 */
	private int expiresIn;
	/**
	 * 凭证有效时间，单位：秒
	 */
	private Date expiresDate;
	/**
	 * 生成签名时使用
	 */
	private String jsapiTicket;

	public Date getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}
}
