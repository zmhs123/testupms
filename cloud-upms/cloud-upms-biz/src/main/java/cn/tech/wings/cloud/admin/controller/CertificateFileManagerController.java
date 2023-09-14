/*
 *    Copyright (c) 2018-2025, cloud All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: cloud
 */

package cn.tech.wings.cloud.admin.controller;

import cn.hutool.http.HttpRequest;
import cn.tech.wings.cloud.common.core.constant.CommonConstants;
import cn.tech.wings.cloud.common.core.util.FileUploadUtil;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.log.annotation.SysLog;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 证书文件管理
 *
 * @author zm_hs
 * @date 2023/4/19 0019 下午 02:45
 * @return
 */
@RestController
@RequestMapping("/certificate")
@Api(value = "certificate", tags = "证书文件管理")
@RequiredArgsConstructor
public class CertificateFileManagerController {

	private static Logger logger = LogManager.getLogger(CertificateFileManagerController.class);
	private final RedisTemplate redisTemplate;

	/**
	 * 证书生成路径
	 */
	@Value("${license.licensePath}")
	private String licensePath;

	/**
	 * 密钥库存储路径
	 */
	@Value("${license.publicKeysStorePath}")
	private String publicKeysStorePath;

	@Value("${accurate.domain}")
	private String domain;

	/**
	 * 获取证书配置路径
	 *
	 * @return cn.tech.wings.cloud.common.core.util.R
	 * @author zm_hs
	 * @date 2023/4/19 0019 下午 02:49
	 */
	@GetMapping("/loadPath")
	public R loadPath() {
		Map<String, String> map = new HashMap<>();
		map.put("licensePath", licensePath);
		map.put("publicKeysStorePath", publicKeysStorePath);
		return R.ok(map);
	}

	/**
	 * 获取证书有效期
	 *
	 * @return cn.tech.wings.cloud.common.core.util.R
	 * @author zm_hs
	 * @date 2023/4/19 0019 下午 02:49
	 */
	@GetMapping("/loadValidTime")
	public R loadValidTime() {
		String validTime = "";
		if (redisTemplate.hasKey(CommonConstants.CERTIFICATE_VALID_TIME)) {
			validTime = (String) redisTemplate.opsForValue().get(CommonConstants.CERTIFICATE_VALID_TIME);
		}
		return R.ok(validTime);
	}

	/**
	 * 上传证书文件
	 *
	 * @param licFile
	 * @param keyFile
	 * @return cn.tech.wings.cloud.common.core.util.R
	 * @author zm_hs
	 * @date 2023/4/19 0019 下午 03:13
	 */
	@PostMapping(value = "/upload")
	@SysLog("上传证书文件")
	@PreAuthorize("@pms.hasPermission('base_upload_certificate')")
	public R upload(@RequestPart("licFile") MultipartFile licFile, @RequestPart(value = "keyFile", required = false) MultipartFile keyFile) {
		try {
			if (null != licFile) {
				String licFilePath = licensePath.substring(0, licensePath.lastIndexOf(File.separator) + 1);
				String licFileName = licensePath.substring(licensePath.lastIndexOf(File.separator) + 1);
				//替换证书文件
				FileUploadUtil.saveFileToServer(licFile, licFilePath, licFileName);
			}
			if (null != keyFile) {
				String keyFilePath = publicKeysStorePath.substring(0, publicKeysStorePath.lastIndexOf(File.separator) + 1);
				String keyFileName = publicKeysStorePath.substring(publicKeysStorePath.lastIndexOf(File.separator) + 1);
				FileUploadUtil.saveFileToServer(keyFile, keyFilePath, keyFileName);
			}
			//证书文件替换成功后，需要重新安装证书
			String url = domain + "code/certificate";
			String result = HttpRequest.post(url).timeout(20000).execute().body();
			logger.info("重新安装证书" + result);
			R r = JSONObject.parseObject(result, R.class);
			if (r.getCode() == CommonConstants.SUCCESS)
				return R.ok(r.getData());
//			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上传证书文件出错！");
		}
		return R.failed("上传证书文件出错！");
	}
}
