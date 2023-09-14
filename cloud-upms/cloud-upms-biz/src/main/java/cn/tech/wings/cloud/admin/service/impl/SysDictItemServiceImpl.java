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
package cn.tech.wings.cloud.admin.service.impl;

import cn.tech.wings.cloud.admin.api.entity.SysDict;
import cn.tech.wings.cloud.admin.api.entity.SysDictItem;
import cn.tech.wings.cloud.admin.mapper.SysDictItemMapper;
import cn.tech.wings.cloud.admin.service.SysDictItemService;
import cn.tech.wings.cloud.admin.service.SysDictService;
import cn.tech.wings.cloud.common.core.constant.CacheConstants;
import cn.tech.wings.cloud.common.core.constant.enums.DictTypeEnum;
import cn.tech.wings.cloud.common.core.exception.ErrorCodes;
import cn.tech.wings.cloud.common.core.util.MsgUtils;
import cn.tech.wings.cloud.common.core.util.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典项
 *
 * @author cloud
 * @date 2019/03/19
 */
@Service
@AllArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

	private final SysDictService dictService;

	/**
	 * 删除字典项
	 * @param id 字典项ID
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public R removeDictItem(Long id) {
		// 根据ID查询字典ID
		SysDictItem dictItem = this.getById(id);
		SysDict dict = dictService.getById(dictItem.getDictId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_DELETE_SYSTEM));
		}
		return R.ok(this.removeById(id));
	}

	/**
	 * 更新字典项
	 * @param item 字典项
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#item.dictType")
	public R updateDictItem(SysDictItem item) {
		// 查询字典
		SysDict dict = dictService.getById(item.getDictId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_UPDATE_SYSTEM));
		}
		return R.ok(this.updateById(item));
	}


	@Override
	public Map<String,Object> ListAll(){
		List<SysDict> list = dictService.list();
		Map<String,Object> item = new HashMap<>();
		for (SysDict sysDict : list) {
			//获取所有字典项
			List<SysDictItem> sysDictItems = this.baseMapper.selectList(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getDictId, sysDict.getId()).orderByDesc(SysDictItem::getSortOrder));
			//组成给前端的数据
			List<Map<String, Object>> itemList = new ArrayList<>();
			for (SysDictItem sysDictItem : sysDictItems) {
				Map<String,Object> item1 = new HashMap<>();
				String label = sysDictItem.getLabel();
				String value = sysDictItem.getItemValue();
				item1.put("label",label);
				item1.put("value",value);
				itemList.add(item1);
			}
			item.put(sysDict.getDictType(),itemList);
		}
		return item;
	}
}
