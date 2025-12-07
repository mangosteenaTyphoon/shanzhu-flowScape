package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusGoalTagRelDO;
import com.shanzhu.mapper.focus.FocusGoalTagRelMapper;
import com.shanzhu.model.focus.dto.FocusGoalTagRelDTO;
import com.shanzhu.service.focus.FocusGoalTagRelService;
import com.shanzhu.utils.security.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 目标与标签关联服务实现类
 */
@Service
public class FocusGoalTagRelServiceImpl extends ServiceImpl<FocusGoalTagRelMapper, FocusGoalTagRelDO> implements FocusGoalTagRelService {
    
    @Override
    public List<Long> queryTagIdsByGoalId(Long goalId) {
        QueryWrapper<FocusGoalTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusGoalTagRelDO::getGoalId, goalId);
        
        List<FocusGoalTagRelDO> relList = this.list(queryWrapper);
        return relList.stream()
                .map(FocusGoalTagRelDO::getTagId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Long> queryGoalIdsByTagId(Long tagId) {
        QueryWrapper<FocusGoalTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusGoalTagRelDO::getTagId, tagId);
        
        List<FocusGoalTagRelDO> relList = this.list(queryWrapper);
        return relList.stream()
                .map(FocusGoalTagRelDO::getGoalId)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveGoalTagRelation(FocusGoalTagRelDTO focusGoalTagRelDTO) {
        Long goalId = focusGoalTagRelDTO.getGoalId();
        List<Long> tagIds = focusGoalTagRelDTO.getTagIds();
        
        // 先删除原有的关联关系
        deleteByGoalId(goalId);
        
        // 批量插入新的关联关系
        if (tagIds != null && !tagIds.isEmpty()) {
            List<FocusGoalTagRelDO> relList = tagIds.stream()
                    .map(tagId -> FocusGoalTagRelDO.builder()
                            .goalId(goalId)
                            .tagId(tagId)
                            .build())
                    .collect(Collectors.toList());
            
            return this.saveBatch(relList);
        }
        
        return true;
    }
    
    @Override
    public void deleteByGoalId(Long goalId) {
        QueryWrapper<FocusGoalTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusGoalTagRelDO::getGoalId, goalId);
        this.remove(queryWrapper);
    }
    
    @Override
    public void deleteByTagId(Long tagId) {
        QueryWrapper<FocusGoalTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusGoalTagRelDO::getTagId, tagId);
        this.remove(queryWrapper);
    }
}