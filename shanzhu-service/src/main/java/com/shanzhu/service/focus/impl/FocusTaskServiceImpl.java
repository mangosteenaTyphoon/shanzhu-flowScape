package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.mapper.focus.FocusTaskMapper;
import com.shanzhu.model.focus.dto.FocusTaskDTO;
import com.shanzhu.service.focus.FocusTaskService;
import com.shanzhu.utils.security.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 专注任务服务实现类
 */
@Service
public class FocusTaskServiceImpl extends ServiceImpl<FocusTaskMapper, FocusTaskDO> implements FocusTaskService {
    
    @Override
    public IPage<FocusTaskDO> queryPage(FocusTaskDTO focusTaskDTO) {
        IPage<FocusTaskDO> page = new Page<>(focusTaskDTO.getPageNum(), focusTaskDTO.getPageSize());
        QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusTaskDTO.getTitle())) {
            queryWrapper.lambda().like(FocusTaskDO::getTitle, focusTaskDTO.getTitle());
        }
        
        if (StringUtils.hasText(focusTaskDTO.getStatus())) {
            queryWrapper.lambda().eq(FocusTaskDO::getStatus, focusTaskDTO.getStatus());
        }
        
        if (focusTaskDTO.getGoalId() != null) {
            queryWrapper.lambda().eq(FocusTaskDO::getGoalId, focusTaskDTO.getGoalId());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusTaskDO::getId);
        
        return this.page(page, queryWrapper);
    }
    
    @Override
    public List<FocusTaskDO> queryList(FocusTaskDO focusTask) {
        QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusTask.getTitle())) {
            queryWrapper.lambda().like(FocusTaskDO::getTitle, focusTask.getTitle());
        }
        
        if (StringUtils.hasText(focusTask.getStatus())) {
            queryWrapper.lambda().eq(FocusTaskDO::getStatus, focusTask.getStatus());
        }
        
        if (focusTask.getGoalId() != null) {
            queryWrapper.lambda().eq(FocusTaskDO::getGoalId, focusTask.getGoalId());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusTaskDO::getId);
        
        return this.list(queryWrapper);
    }
    
    @Override
    public FocusTaskDO queryById(Long id) {
        QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusTaskDO::getId, id)
                .eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());
        return this.getOne(queryWrapper);
    }
    
    @Override
    public Boolean save(FocusTaskDO focusTaskDO) {
        // 设置用户ID
        focusTaskDO.setUserId(LoginUserContext.getUserId());
        
        if (focusTaskDO.getId() == null) {
            // 新增
            return this.save(focusTaskDO);
        } else {
            // 更新
            QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FocusTaskDO::getId, focusTaskDO.getId())
                    .eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());
            return this.update(focusTaskDO, queryWrapper);
        }
    }
    
    @Override
    public void deleteByIds(List<Long> ids) {
        QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(FocusTaskDO::getId, ids)
                .eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());
        this.remove(queryWrapper);
    }
}