package cn.tech.wings.cloud.res.controller;

import cn.hutool.core.convert.Convert;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import cn.tech.wings.cloud.res.api.entity.Accessory;
import cn.tech.wings.cloud.res.api.entity.Album;
import cn.tech.wings.cloud.res.api.entity.WaterMark;
import cn.tech.wings.cloud.res.core.util.FileUploadUtil;
import cn.tech.wings.cloud.res.core.util.FileUtil;
import cn.tech.wings.cloud.res.core.util.MinioTemplate;
import cn.tech.wings.cloud.res.service.AccessoryService;
import cn.tech.wings.cloud.res.service.AlbumService;
import cn.tech.wings.cloud.res.service.WaterMarkService;
import com.alibaba.cloud.commons.lang.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 运营端文件管理
 *
 * @author dijuli
 * @Date 2019-10-09 09:54:59
 */
@Api(value = "运营端文件管理", tags = {"运营端文件管理"})
@RestController
@RequestMapping("/accessory")
@AllArgsConstructor
public class AccessoryController {

	private final AlbumService albumService;

	private final AccessoryService accessoryService;

	private final FileUploadUtil fileUploadUtil;

	private final WaterMarkService waterMarkService;

	private MinioTemplate minioTemplate;

	/**
	 * 上传图片
	 *
	 * @return
	 */
	@ApiOperation("上传文件")
	@PostMapping("/upload")
	@Inner(false)
	public R<Map<String, Object>> swf_upload(
			@ApiParam("文件") @RequestParam(value = "file", required = false) @RequestPart("file") MultipartFile file,
			MultipartHttpServletRequest request,
			@ApiParam("相册id") @RequestParam(value = "album_id") String album_id,
			@ApiParam("水印标记") @RequestParam(value = "watermark", required = false) String watermark,
			@ApiParam("相册编码") @RequestParam(value = "album_code") String album_code
	) {
		Map<String, Object> reMap = new HashMap<>();

		//校验参数有效性
		if (StringUtils.isEmpty(album_id) && StringUtils.isEmpty(album_code)) {
			//返回无操作权限页面
			return R.failed("参数错误");
		}
		Album a = null;
		String ownerIdStr = "pt";
		if (!StringUtils.isEmpty(album_id)) {
			//相册内上传图片
			a = albumService.getById(Convert.toInt(album_id));
			if (a == null) {
				return R.failed("参数错误");
			}
		} else if (!StringUtils.isEmpty(album_code)) {
			//根据相册编码上传图片,这种上传无需校验权限。
			a = albumService.getByAlbumCode(album_code);
			if (a == null) {
				return R.failed("相册编码错误");
			}
		}

		//创建文件上传路径
		String uploadFilePath = fileUploadUtil.getFilePath();
		String path = request.getSession().getServletContext().getRealPath("/") + uploadFilePath;
		FileUtil.createFolder(path);

		String extend = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
		long fileSize = file.getSize();
		Map p = fileUploadUtil.getUploadParams(a.getResType());

		long p_f_size = (long) p.get("fileSize");
		if (fileSize > p_f_size) {
			return R.failed("上传文件过大,大于:" + p_f_size / 1024 / 1024 + "M");
		}
		String p_exts = (String) p.get("exts");
		//暂时先注释掉
		if (p_exts.indexOf(extend) == -1) {
			return R.failed("文件格式不正确");
		}
		//更新文件夹容量
		if (!albumService.updateSize(a, fileSize)) {
			//文件夹容量不足
			return R.failed("容量不足,请升级空间后再试！");
		}
		try {
			//1、图片上传到系统服务器
			Map<String, Object> map = fileUploadUtil.saveFileToServer(file, path, null, null);

			//2.加水印
			if("1".equals(watermark) && ownerIdStr!=null && fileUploadUtil.isImg((String)map.get("mime"))) {
				List<WaterMark> wms = this.waterMarkService.getMgrWaterMark();
				if (wms.size() > 0) {
					WaterMark mark = (WaterMark) wms.get(0);
					if (mark.getWmTextOpen()) {
						String targetImg = path + "/" + map.get("fileName");
						int pos = mark.getWmTextPos();
						String text = mark.getWmText();
						String markContentColor = mark.getWmTextColor();
						int width = map.get("width")==null?0:(int)map.get("width");
						//int height = CommUtil.null2Int(map.get("height"));
						int height = map.get("height")==null?0:(int)map.get("height");
						fileUploadUtil.waterMarkWithTextW(request, targetImg, targetImg, text, markContentColor,
								new Font(mark.getWmTextFont(), 1, mark.getWmTextFontSize()), pos, 100.0F, width, height);
					}
				}
			}
			String osskey = uploadFilePath + "/" + UUID.randomUUID().toString().replaceAll("-", "");
			if (map.get("width") != null) {
				osskey += "_" + map.get("width") + "-" + map.get("height");
			}
			String key = fileUploadUtil.uploadFile(new File(path + "/" + map.get("fileName")), osskey + "." + map.get("mime"));

			//4、保存图片
			Accessory image = new Accessory();
			image.setSize(fileSize);
			image.setAddTime(new Date());
			image.setExt((String) map.get("mime"));
			image.setPath(key);
			image.setWidth(Convert.toInt(map.get("width")));
			image.setHeight(Convert.toInt(map.get("height")));
			image.setName((String) map.get("oldName"));
			image.setAlbumId(a.getId());
			this.accessoryService.save(image);
			reMap.put("readPath", key);
			reMap.put("accessoryId", image.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.ok(reMap);
	}


	/**
	 * minio文件下载
	 *
	 * @author whg
	 * @Date 2022-4-26
	 */
	@ApiOperation("文件下载")
	@ResponseBody
	@GetMapping("/download")
	public void download(@ApiParam(value = "文件路径", required = true) @RequestParam(value = "filePath") String filePath
			, HttpServletResponse response) {
		minioTemplate.download(filePath, response);
	}


}
