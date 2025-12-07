package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusTagDO;
import com.shanzhu.mapper.focus.FocusTagMapper;
import com.shanzhu.model.focus.dto.FocusTagDTO;
import com.shanzhu.service.focus.FocusTagService;
import com.shanzhu.utils.security.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 专注标签服务实现类
 */
@Service
public class FocusTagServiceImpl extends ServiceImpl<FocusTagMapper, FocusTagDO> implements FocusTagService {
    
    @Override
    public IPage<FocusTagDO> queryPage(FocusTagDTO focusTagDTO) {
        IPage<FocusTagDO> page = new Page<>(focusTagDTO.getPageNum(), focusTagDTO.getPageSize());
        QueryWrapper<FocusTagDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusTagDTO.getName())) {
            queryWrapper.lambda().like(FocusTagDO::getName, focusTagDTO.getName());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusTagDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusTagDO::getId);
        
        return this.page(page, queryWrapper);
    }
    
    @Override
    public List<FocusTagDO> queryList(FocusTagDO focusTag) {
        QueryWrapper<FocusTagDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusTag.getName())) {
            queryWrapper.lambda().like(FocusTagDO::getName, focusTag.getName());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusTagDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusTagDO::getId);
        
        return this.list(queryWrapper);
    }
    
    @Override
    public FocusTagDO queryById(Long id) {
        QueryWrapper<FocusTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusTagDO::getId, id)
                .eq(FocusTagDO::getUserId, LoginUserContext.getUserId());
        return this.getOne(queryWrapper);
    }
    
    @Override
    public boolean save(FocusTagDO focusTagDO) {
        // 设置用户ID
        focusTagDO.setUserId(Long.valueOf(LoginUserContext.getUserId()));
        
        if (focusTagDO.getId() == null) {
            // 新增
            return this.save(focusTagDO);
        } else {
            // 更新
            QueryWrapper<FocusTagDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FocusTagDO::getId, focusTagDO.getId())
                    .eq(FocusTagDO::getUserId, LoginUserContext.getUserId());
            return this.update(focusTagDO, queryWrapper);
        }

    }
    
    @Override
    public void deleteByIds(List<Long> ids) {
        QueryWrapper<FocusTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(FocusTagDO::getId, ids)
                .eq(FocusTagDO::getUserId, LoginUserContext.getUserId());
        this.remove(queryWrapper);
    }
}