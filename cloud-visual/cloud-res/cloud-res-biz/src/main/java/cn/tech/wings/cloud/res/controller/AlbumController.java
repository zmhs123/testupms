package cn.tech.wings.cloud.res.controller;

import cn.hutool.core.convert.Convert;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import cn.tech.wings.cloud.res.api.entity.Accessory;
import cn.tech.wings.cloud.res.api.entity.Album;
import cn.tech.wings.cloud.res.api.model.AlbumTreeNode;
import cn.tech.wings.cloud.res.api.model.params.AccessoryParam;
import cn.tech.wings.cloud.res.api.model.params.AlbumParam;
import cn.tech.wings.cloud.res.api.model.result.AccessoryResult;
import cn.tech.wings.cloud.res.api.model.result.AlbumResult;
import cn.tech.wings.cloud.res.config.ConfigReader;
import cn.tech.wings.cloud.res.core.util.FileUploadUtil;
import cn.tech.wings.cloud.res.core.util.MinioTemplate;
import cn.tech.wings.cloud.res.factory.AlbumFactory;
import cn.tech.wings.cloud.res.service.AccessoryService;
import cn.tech.wings.cloud.res.service.AlbumService;
import cn.tech.wings.cloud.res.wrapper.AlbumWrapper;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 相册表控制器
 *
 * @author dijuli
 * @Date 2019-10-09 09:54:59
 */
@Api(value="素材管理",tags={"素材操作接口"})
@RestController
@RequestMapping("/album")
@AllArgsConstructor
public class AlbumController {

	private final AlbumService albumService;

	private final AccessoryService accessoryService;

	private final FileUploadUtil fileUploadUtil;
	private final ConfigReader configReader;
	/**
	 * 获取相册树
	 *f
	 * @author di
	 * @Date 2019年2月23日22:01:47
	 */
	@ApiOperation("获取相册树")
	@GetMapping("/tree")
//	@PreAuthorize("@pms.hasPermission('res_album_tree')")
	@Inner(false)
	public R<List<AlbumTreeNode>> getTree(
			@ApiParam("根相册编码") @RequestParam(value = "albumCode", required = false)  String albumCode
	) {
		//校验参数有效性
		if (StringUtils.isEmpty(albumCode)) {
			return R.failed("编码为空");
		}

		//获取交易中心

		AlbumParam p = new AlbumParam();
		p.setAlbumCode(albumCode);
		p.setTcCode("tcCode");
		AlbumResult treeRoot = albumService.findBySpec(p);
		if (treeRoot == null) {
			return R.failed("未获取到相册数据");
		}

		List<Map<String, Object>> albums = this.albumService.selectAlbumTree(treeRoot.getTreeid(),"tcCode");
		List<Map<String, Object>> menusWrap = new AlbumWrapper(albums).wrap();

		//构建树
		List<AlbumTreeNode> menuTreeNodes = AlbumFactory.buildTreeNodes(menusWrap);

		return R.ok(menuTreeNodes);
	}

	/**
	 * 查询素材
	 * @return
	 */
	@ApiOperation("获取素材列表")
	@GetMapping("/page")
//	@PreAuthorize("@pms.hasPermission('res_album_list')")
	@Inner(false)
	public R<IPage<AccessoryResult>> list(
		  @ApiParam("当前页数") @RequestParam(value = "page", required = false) Long page,
		  @ApiParam("每页记录数") @RequestParam(value = "size", required = false) Long size,
		  @ApiParam("查询关键词") @RequestParam(value = "sval", required = false) String sval,
		  @ApiParam("相册id") @RequestParam(value = "albumId", required = false) Integer albumId,
		  @ApiParam("图片高度") @RequestParam(value = "height", required = false) Integer height,
		  @ApiParam("图片宽度") @RequestParam(value = "width", required = false) Integer width
	){
		IPage result = new Page(page, size);
		//校验参数有效性
		Album a = null;
		if(albumId!=null){
			a = albumService.getById(albumId);
		}
		if(a==null){
			return R.failed();
		}

		AccessoryParam p = new AccessoryParam();
		p.setName(sval);
		p.setAlbumId(albumId);
		p.setHeight(height);
		p.setWidth(width);
		p.setTcCode("");
		IPage<AccessoryResult> pageBySpec = accessoryService.findPageBySpec(p, result);
		if("minio".equals(configReader.getImplementation())){
			if(pageBySpec.getRecords()!=null){
				pageBySpec.getRecords().forEach(obj->obj.setPath(MinioTemplate.LIBRARY+"/"+obj.getPath()));
			}
		}
		return R.ok(pageBySpec);
	}

	/**
	 * 添加更新文件夹
	 * @param albumParam
	 * @return
	 */
	@ApiOperation("保存文件夹")
	@PostMapping("/folder")
//	@PreAuthorize("@pms.hasPermission('res_album_save_folder')")
	@Inner(false)
	public R saveFolder(@RequestBody AlbumParam albumParam){
		//校验参数有效性
		if (albumParam.getParentid() == null || StringUtils.isEmpty(albumParam.getAlbumName())) {
			//父文件夹id不可为空，文件夹名称不可为空。
			return R.failed("参数错误");
		}
		Album pAlbum = albumService.getById(albumParam.getParentid());
		if (pAlbum == null) {
			return R.failed("参数错误");
		}


		Album album = null;
		if(albumParam.getId() != null){
			album = albumService.getById(Convert.toInt(albumParam.getId()));
		}
		if(album!=null){
			if(album.getTreeid().indexOf(pAlbum.getTreeid())!=-1){
				album.setAlbumName(albumParam.getAlbumName());
				albumService.updateById(album);
			}else{
				return R.failed("无权限操作");
			}
		}
		if(album==null){
			album = new Album();

			album.setOwnerId(pAlbum.getOwnerId());
			album.setAlbumName(albumParam.getAlbumName());
			album.setParentid(pAlbum.getId());
			album.setResType(pAlbum.getResType());
			album.setAlbumDefault(false);
			album.setTcCode("");
			albumService.save(album);
			album.setTreeid(pAlbum.getTreeid()+album.getId()+";");
			albumService.updateById(album);
			if(pAlbum.getIsleaf()==0){
				pAlbum.setIsleaf(1);
				albumService.updateById(pAlbum);
			}

		}

		return R.ok();
	}


	/**
	 * 删除文件夹
	 * 有子文件夹或者子文件不可删除
	 * @param id
	 * @return
	 */
	@ApiOperation("删除文件夹")
	@DeleteMapping("/{id}")
	@Inner(false)
	public R delFolder(
			@PathVariable(value = "id") @ApiParam("相册id") Integer id
	){

		//校验参数有效性
		if (id == null) {
			return R.failed("参数错误");
		}
		Album a = albumService.getById(id);
		if (a==null) {
			return R.failed("参数错误");
		}

		if(a.getAlbumDefault()==true){
			return R.failed("系统菜单不允许删除");
		}

		//2.校验操作的文件夹是否是基文件夹的子文件夹
		if(a.getIsleaf()!=0){
			return R.failed("存在子文件夹，请先删除子文件夹后，再进行此操作！");
		}

		//3.校验操作的文件夹下是否有子文件
		Page page = new Page(1, 20);
		AccessoryParam p = new AccessoryParam();
		p.setAlbumId(id);
		IPage<AccessoryResult> lp = accessoryService.findPageBySpec(p,page);
		if(lp.getTotal()>0){
			return R.failed("文件夹下存在文件，请先清空文件夹后，再进行此操作！");
		}

		albumService.deleteAlbum(a);

		return R.ok();
	}

	/**
	 * 删除附件
	 * 删除附件对象、删除物理地址。
	 * @param id
	 * @return
	 */
	@ApiOperation("删除素材")
	@DeleteMapping("/accessory/{id}")
//	@PreAuthorize("@pms.hasPermission('res_album_del_accessory')")
	@Inner(false)
	public R delAccessory(
			@PathVariable(value = "id") @ApiParam("素材id") String id
	){

		//校验参数有效性
		if(StringUtils.isEmpty(id)) {
			return R.failed("参数错误");
		}

		Integer ownerId = 0;

		String[] ids = id.split(",");
		for (String s:ids) {
			if (!StringUtil.isEmpty(s)) {
				Accessory acc = this.accessoryService.getById(Convert.toInt(s));
				if(acc!=null){
					Album a = albumService.getById(acc.getAlbumId());
					if (0==ownerId || ownerId!=null && ownerId.equals(a.getOwnerId()) ){
						accessoryService.deleteAccessory(a,acc);
						//删除oss上的文件
						fileUploadUtil.deleteFile(acc.getPath());
					}
				}
			}
		}

		return R.ok();
	}

	/**
	 * 移动附件到目标文件夹
	 * token需要对文件夹有操作权限
	 * @param mulitId
	 * @param to_album_id
	 * @return
	 */
	@ApiOperation("移动素材")
	@GetMapping("/accessory/move")
	@PreAuthorize("@pms.hasPermission('res_album_accessoy_move')")
	public R album_accessory_move(
			@ApiParam("素材id,多个id之间用") @RequestParam(value = "mulitId")String mulitId,
			@ApiParam("目标相册id") @RequestParam(value = "to_album_id")String to_album_id) {

		//校验参数有效性
		Album ta = albumService.getById(Convert.toInt(to_album_id));
		if(ta==null){
			//返回无操作权限页面
			return R.failed("参数错误");
		}

		String[] ids = mulitId.split(",");
		for (String id:ids) {
			if (!StringUtil.isEmpty(id)) {
				Accessory acc = this.accessoryService.getById(Convert.toInt(id));
				if(acc!=null){
					Album a = albumService.getById(acc.getAlbumId());
					//2.检测文件原来的文件夹的所有者，和目标文件夹的所有者是否相同。文件夹存储文件类型是否相同。
					if (a.getOwnerId().equals(ta.getOwnerId()) && a.getResType().equals(ta.getResType())) {
						accessoryService.updateById(acc);
					} else {
						continue;
					}
				}
			}
		}

		return R.ok();
	}



	@ApiOperation("修改素材名称")
	@PutMapping("/accessory")
//	@PreAuthorize("@pms.hasPermission('res_album_update_accessory')")
	public R updateAccessory(@RequestBody AccessoryParam accessoryParam) {
		Accessory accessory = null;
		if (accessoryParam.getId() != null) {
			accessory = accessoryService.getById(accessoryParam.getId());
		}
		if (accessory != null) {
			accessory.setName(accessoryParam.getName());
			accessoryService.updateById(accessory);
		} else {
			return R.failed("素材不存在");
		}
		return R.ok();
	}
}
