package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.mapper.focus.FocusGoalMapper;
import com.shanzhu.model.focus.dto.FocusGoalDTO;
import com.shanzhu.service.focus.FocusGoalService;
import com.shanzhu.utils.security.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 专注目标服务实现类
 */
@Service
public class FocusGoalServiceImpl extends ServiceImpl<FocusGoalMapper, FocusGoalDO> implements FocusGoalService {
    
    @Override
    public IPage<FocusGoalDO> queryPage(FocusGoalDTO focusGoalDTO) {
        IPage<FocusGoalDO> page = new Page<>(focusGoalDTO.getPageNum(), focusGoalDTO.getPageSize());
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusGoalDTO.getTitle())) {
            queryWrapper.lambda().like(FocusGoalDO::getTitle, focusGoalDTO.getTitle());
        }
        
        if (StringUtils.hasText(focusGoalDTO.getStatus())) {
            queryWrapper.lambda().eq(FocusGoalDO::getStatus, focusGoalDTO.getStatus());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusGoalDO::getId);
        
        return this.page(page, queryWrapper);
    }
    
    @Override
    public List<FocusGoalDO> queryList(FocusGoalDO focusGoal) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusGoal.getTitle())) {
            queryWrapper.lambda().like(FocusGoalDO::getTitle, focusGoal.getTitle());
        }
        
        if (StringUtils.hasText(focusGoal.getStatus())) {
            queryWrapper.lambda().eq(FocusGoalDO::getStatus, focusGoal.getStatus());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusGoalDO::getId);
        
        return this.list(queryWrapper);
    }
    
    @Override
    public FocusGoalDO queryById(Long id) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusGoalDO::getId, id)
                .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        return this.getOne(queryWrapper);
    }
    
    @Override
    public boolean save(FocusGoalDO focusGoalDO) {
        // 设置用户ID
        focusGoalDO.setUserId(Long.valueOf(LoginUserContext.getUserId()));
        
        if (focusGoalDO.getId() == null) {
            // 新增
            return this.save(focusGoalDO);
        } else {
            // 更新
            QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FocusGoalDO::getId, focusGoalDO.getId())
                    .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
            return this.update(focusGoalDO, queryWrapper);
        }
    }
    
    @Override
    public void deleteByIds(List<Long> ids) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(FocusGoalDO::getId, ids)
                .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        this.remove(queryWrapper);
    }
}