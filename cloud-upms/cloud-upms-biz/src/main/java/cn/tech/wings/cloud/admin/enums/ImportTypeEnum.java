package cn.tech.wings.cloud.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImportTypeEnum {
	/**
	 * 跳过
	 */
	TG(0, "跳过"),

	/**
	 * 覆盖
	 */
	FG(1, "覆盖");

	/**
	 * 类型
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String description;
}
