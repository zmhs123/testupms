package cn.tech.wings.cloud.res.api.feign;

import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.core.constant.ServiceNameConstants;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.res.api.model.result.AccessoryFeignResult;
import cn.tech.wings.cloud.res.api.model.result.UploadResult;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: gaoyunfei
 * @Date: 2019/9/20 17:47
 */


@FeignClient(contextId = "AlbumApi", value = ServiceNameConstants.CLOUD_RES)
public interface AlbumApi {

	/**
	 * 通过key获取私有库访问路径
	 * @param paths
	 * @return
	 */
	@GetMapping("/accessory/paths")
    R<Map<String,String>> getPrivatePaths(@RequestParam(name = "paths", required = false) List<String> paths, @RequestHeader(SecurityConstants.FROM) String from);


	/**
	 * 通过key获取附件信息
	 * @param paths
	 * @return
	 */
	@GetMapping("/accessory/detail")
    R<List<AccessoryFeignResult>> detail(@RequestParam(name = "paths") List<String> paths, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 批量上传文件到私有库
	 * @param request
	 * @param files
	 * @param paths
	 * @return
	 */
	@PostMapping(value = "/accessory/upload/private",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<List<UploadResult>> uploadPrivate(HttpServletRequest request,
                                        @ApiParam("文件") @RequestParam(value = "files", required = false) @RequestPart("files") List<MultipartFile> files,
                                        @ApiParam("文件路径") @RequestParam(value = "paths") List<String> paths,
                                        @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 批量上传文件到公有库
	 * @param request
	 * @param files
	 * @param paths
	 * @return
	 */
	@PostMapping(value = "/accessory/upload/public",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<List<UploadResult>> uploadPublic(MultipartHttpServletRequest request,
                                       @ApiParam("文件") @RequestParam(value = "file", required = false) @RequestPart("files") List<MultipartFile> files,
                                       @ApiParam("文件路径") @RequestParam(value = "paths") List<String> paths, @RequestHeader(SecurityConstants.FROM) String from);


}
