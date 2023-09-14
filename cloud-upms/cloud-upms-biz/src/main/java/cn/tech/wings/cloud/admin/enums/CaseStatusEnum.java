package cn.tech.wings.cloud.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CaseStatusEnum {
	/**
	 * 未录制
	 */
	WLZ(0, "未录制"),

	/**
	 * 录制中
	 */
	LZZ(1, "录制中"),

	/**
	 * 已录制
	 */
	YLZ(2, "已录制");

	/**
	 * 类型
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String description;
}
