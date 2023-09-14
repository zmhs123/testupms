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

package cn.tech.wings.cloud.res.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author LCN on 2017/11/11
 */
@Component
@RefreshScope
@Getter
public class ConfigReader {

	/**
	 * 事务默认数据的位置，有最大时间
	 */
	private static final String KEY_PREFIX = "res:default:";

	/**
	 * 负载均衡模块存储信息
	 */
	private static final String KEY_PREFIX_LOADBALANCE = "res:loadbalance:";

	/**
	 * 补偿事务永久存储数据
	 */
	private static final String TX_MANAGER_COMPENSATE = "res:compensate:";

	/**
	 *	文件上传路径
	 **/
	@Value("${cloud.res.uploadFilePath}")
	private String uploadFilePath;

	/**
	 *	文件上传路径类型
	 **/
	@Value("${cloud.res.uploadFilePathType}")
	private String uploadFilePathType;

	/**
	 *	图片后缀
	 **/
	@Value("${cloud.res.imageSuffix}")
	private String imageSuffix;

	/**
	 *	图片大小
	 **/
	@Value("${cloud.res.imageFileSize}")
	private String imageFileSize;

	/**
	 *	视频后缀
	 **/
	@Value("${cloud.res.videoSuffix}")
	private String videoSuffix;

	/**
	 *	视频大小
	 **/
	@Value("${cloud.res.videoFileSize}")
	private String videoFileSize;

	/**
	 *	视频后缀
	 **/
	@Value("${cloud.res.fileSuffix}")
	private String fileSuffix;

	/**
	 *	视频大小
	 **/
	@Value("${cloud.res.fileFileSize}")
	private String fileFileSize;

	/**
	 *	ossurl
	 **/
	@Value("${cloud.res.ossUrl}")
	private String ossUrl;


	/**
	 * 上传实现方式  oss : minio
	 */
	@Value("${cloud.res.implementation}")
	private String implementation;



}
