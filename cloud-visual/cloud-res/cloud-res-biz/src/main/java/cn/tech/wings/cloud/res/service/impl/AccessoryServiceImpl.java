package cn.tech.wings.cloud.res.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.tech.wings.cloud.res.mapper.AccessoryMapper;
import cn.tech.wings.cloud.res.service.AccessoryService;
import cn.tech.wings.cloud.res.service.AlbumService;
import cn.tech.wings.cloud.res.api.entity.Accessory;
import cn.tech.wings.cloud.res.api.entity.Album;
import cn.tech.wings.cloud.res.api.model.params.AccessoryParam;
import cn.tech.wings.cloud.res.api.model.result.AccessoryResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Service
public class AccessoryServiceImpl extends ServiceImpl<AccessoryMapper, Accessory> implements AccessoryService {

    @Autowired
    private AlbumService albumService;

    @Override
    public void add(AccessoryParam param){
        Accessory entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AccessoryParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AccessoryParam param){
        Accessory oldEntity = getOldEntity(param);
        Accessory newEntity = getEntity(param);
        BeanUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);
    }

    @Override
    public AccessoryResult findBySpec(AccessoryParam param){
        return null;
    }

    @Override
    public List<AccessoryResult> findListBySpec(AccessoryParam param){
        return null;
    }

    @Override
    public IPage<AccessoryResult> findPageBySpec(AccessoryParam param, IPage page){
        return this.baseMapper.customPageList(page, param);
    }

    @Override
    public void deleteAccessory(Album a, Accessory acc) {
        String filePath = acc.getPath();
        //更新相册使用容量
        albumService.updateSize(a,acc.getSize()*(-1));
        //删除文件
        this.baseMapper.deleteById(acc.getId());
        //删除oss物理文件

    }

    @Override
    public Accessory getObjById(Long wmImageId) {
        return this.baseMapper.selectById(wmImageId);
    }

    private Serializable getKey(AccessoryParam param){
        return param.getId();
    }

    private Accessory getOldEntity(AccessoryParam param) {
        return this.getById(getKey(param));
    }

    private Accessory getEntity(AccessoryParam param) {
        Accessory entity = new Accessory();
        BeanUtil.copyProperties(param, entity);
        return entity;
    }

}
