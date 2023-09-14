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
package cn.tech.wings.cloud.res.core.constant;

/**
 * jwt相关配置
 *
 * @author di
 * @date 2017-08-23 9:23
 */
public interface ResConstants {


    //文件夹文件类型
    String FILE_TYPE_PIC = "pic";
    String FILE_TYPE_VIDEO = "video";
    String FILE_TYPE_FILE = "file";

    //文件路径存储类型
    String FILE_PATH_TYPE_S = "sid";
    String FILE_PATH_TYPE_SY = "sidYear";
    String FILE_PATH_TYPE_SYM = "sidYearMonth";
    String FILE_PATH_TYPE_SYMD = "sidYearMonthDay";

    String SECRET = "defaultSecret";

    Long EXPIRATION = 604800L;

    String AUTH_PATH = "/mallApi/auth";

    //相册名称后缀
    String ALBUM_NAME_NEXT_ROOT="_图片根";

    //视频名称后缀
    String ALBUM_NAME_NEXT_VIDEO_ROOT="_视频根";
}
