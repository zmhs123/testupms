/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.tech.wings.cloud.res.wrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 菜单列表的包装类
 *
 * @author di
 * @date 2017年2月19日15:07:29
 */
public class AlbumWrapper extends BaseControllerWrapper {

    public AlbumWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public AlbumWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //对相册类包装
//        map.put("statusName", ConstantFactory.me().getMenuStatusName((String) map.get("status")));

    }

}
