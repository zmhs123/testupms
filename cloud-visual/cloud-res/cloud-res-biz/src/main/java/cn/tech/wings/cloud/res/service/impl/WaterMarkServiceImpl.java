package cn.tech.wings.cloud.res.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.tech.wings.cloud.res.mapper.WaterMarkMapper;
import cn.tech.wings.cloud.res.service.WaterMarkService;
import cn.tech.wings.cloud.res.api.entity.WaterMark;
import cn.tech.wings.cloud.res.api.model.params.WaterMarkParam;
import cn.tech.wings.cloud.res.api.model.result.WaterMarkResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengchu_pang
 * @since 2020-11-20
 */
@Service
public class WaterMarkServiceImpl extends ServiceImpl<WaterMarkMapper, WaterMark> implements WaterMarkService {

    @Override
    public void add(WaterMarkParam param){
        WaterMark entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WaterMarkParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(WaterMarkParam param){
        WaterMark oldEntity = getOldEntity(param);
        WaterMark newEntity = getEntity(param);
        BeanUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);
    }

    @Override
    public List<WaterMarkResult> findListBySpec(WaterMarkParam param){
        return this.baseMapper.customList(param);
    }

    @Override
    public IPage<WaterMarkResult> findPageBySpec(Page page, WaterMarkParam param){
		return this.baseMapper.customPageList(page, param);
    }

//    @Override
//    public List<WaterMark> getAllByStoreId(Map<String, Object> params) {
//        return this.baseMapper.getAllByStoreId(params);
//    }

    @Override
    public List<WaterMark> getMgrWaterMark() {
        return this.baseMapper.getMgrWaterMark();
    }


    private Serializable getKey(WaterMarkParam param){
        return param.getId();
    }


    private WaterMark getOldEntity(WaterMarkParam param) {
        return this.getById(getKey(param));
    }

    private WaterMark getEntity(WaterMarkParam param) {
        WaterMark entity = new WaterMark();
        BeanUtil.copyProperties(param, entity);
        return entity;
    }

}
