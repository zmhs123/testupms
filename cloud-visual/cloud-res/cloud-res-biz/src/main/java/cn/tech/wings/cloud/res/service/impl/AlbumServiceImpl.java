package cn.tech.wings.cloud.res.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.tech.wings.cloud.res.mapper.AlbumMapper;
import cn.tech.wings.cloud.res.service.AlbumService;
import cn.tech.wings.cloud.res.api.entity.Album;
import cn.tech.wings.cloud.res.api.model.params.AlbumParam;
import cn.tech.wings.cloud.res.api.model.result.AlbumResult;
import cn.tech.wings.cloud.res.core.enums.ResTypeEnum;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 相册表 服务实现类
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

    @Override
    public void add(AlbumParam param){
        Album entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AlbumParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AlbumParam param){
        Album oldEntity = getOldEntity(param);
        Album newEntity = getEntity(param);
        BeanUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);
    }

    @Override
    public AlbumResult findBySpec(AlbumParam param){
        List<AlbumResult> lst = this.findListBySpec(param);
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }

    @Override
    public List<AlbumResult> findListBySpec(AlbumParam param){
        return this.baseMapper.findListBySpec(param);
    }

    @Override
    public IPage<AlbumResult> findPageBySpec(Page page, AlbumParam param){
        return this.baseMapper.customPageList(page, param);
    }

    @Override
//    @MasterSelect
    public void updatePicOrVideoSize(Map map){
        List<Integer> storeIdList = (ArrayList) map.get("storeIdList");
        Float picTotalSize = Float.parseFloat((String) map.get("picTotalSize"));
        Float videoTotalSize = Float.parseFloat((String)map.get("videoTotalSize"));
        Album album = new Album();
        if(storeIdList.size()>0){
            for(Integer storeId:storeIdList){
                AlbumParam albumParam = new AlbumParam();
                albumParam.setOwnerId(storeId);
                List<AlbumResult> albumResults = this.baseMapper.findListForPassService(albumParam);
                for(AlbumResult albumResult:albumResults){
                    if(albumResult.getResType().equals(ResTypeEnum.PIC.getValue())){
                        albumResult.setTotalSize(picTotalSize);
                        BeanUtil.copyProperties(albumResult, album);
                        this.baseMapper.updateById(album);
                    }
                    else if (albumResult.getResType().equals(ResTypeEnum.VIDEO.getValue())){
                        albumResult.setTotalSize(videoTotalSize);
                        BeanUtil.copyProperties(albumResult, album);
                        this.baseMapper.updateById(album);
                    }
                }
            }
        }
    }

    private Serializable getKey(AlbumParam param){
        return param.getId();
    }

    private Album getOldEntity(AlbumParam param) {
        return this.getById(getKey(param));
    }

    private Album getEntity(AlbumParam param) {
        Album entity = new Album();
        BeanUtil.copyProperties(param, entity);
        return entity;
    }

    private Album getEntity2(AlbumResult param) {
        Album entity = new Album();
        BeanUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 更新文件夹使用大小
     * @param a
     * @param size
     * @return
     */
    @Override
//    @MasterSelect
    public boolean updateSize(Album a, long size) {
        if(a==null || StringUtils.isEmpty(a.getTreeid())){
            return false;
        }
        Album root = this.getById(Convert.toInt(a.getTreeid().split(";")[0]));
        if(root==null){
            return false;
        }
        if(root.getTotalSize()==0){
            return true;
        }
        if(root.getTotalSize()>root.getUseSize()+size){
            //总容量为0时，为不做任何限制。
            root.setUseSize(root.getUseSize()+size);
            if (root.getUseSize()<0) {
                root.setUseSize(0L);
            }
            this.updateById(root);
            return true;
        }
        return false;
    }

    /**
     * 根据ResType，OwnerId获取根相册
     * @param ownerId
     * @param resType
     * @return
     */
    @Override
    public AlbumResult selectRootAlbumByOwnerIdResType(Integer ownerId, String resType){
        List<AlbumResult> lst = this.baseMapper.selectRootAlbumByOwnerIdResType(ownerId,resType);
        if (lst.size()>0) {
            return lst.get(0);
        }
        return null;
    }

    /**
     * 根据treeid 获取相册树
     * @param treeId
     * @param tcCode
     * @return
     */
    @Override
    public List<Map<String, Object>> selectAlbumTree(String treeId,String tcCode){
        return this.baseMapper.selectAlbumTree(treeId,tcCode);
    }

    @Override
    public Album getRootAlbumByTreeId(String treeid) {
        Album root = this.getById(Convert.toInt(treeid.split(";")[0]));
        return root;
    }

    @Override
    public Album getByAlbumCode(String album_code) {
        AlbumParam p = new AlbumParam();
        p.setAlbumCode(album_code);
        List<AlbumResult> lst = this.baseMapper.findListBySpec(p);
        if (lst.size()>0) {
            return getEntity2(lst.get(0));
        }
        return null;
    }

    @Override
//    @MasterSelect
    public boolean deleteAlbum(Album album) {
        try {
            AlbumParam param = new AlbumParam();
            param.setParentid(album.getParentid());
            if(album.getParentid()!=null && findListBySpec(param).size()<2){
                Album p = this.getById(album.getParentid());
                p.setIsleaf(0);
                this.updateById(p);
            }
            this.removeById(album.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
