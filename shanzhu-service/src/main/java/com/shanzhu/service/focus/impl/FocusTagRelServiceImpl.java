package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusTagRelDO;
import com.shanzhu.mapper.focus.FocusTagRelMapper;
import com.shanzhu.model.focus.dto.FocusTagRelDTO;
import com.shanzhu.service.focus.FocusTagRelService;
import com.shanzhu.utils.security.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签与目标/任务的通用关联服务实现类
 */
@Service
public class FocusTagRelServiceImpl extends ServiceImpl<FocusTagRelMapper, FocusTagRelDO> implements FocusTagRelService {
    
    @Override
    public List<Long> queryTagIdsByEntityIdAndType(Long entityId, String entityType) {
        QueryWrapper<FocusTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusTagRelDO::getEntityId, entityId)
                .eq(FocusTagRelDO::getEntityType, entityType);
        
        List<FocusTagRelDO> relList = this.list(queryWrapper);
        return relList.stream()
                .map(FocusTagRelDO::getTagId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Long> queryEntityIdsByTagIdAndType(Long tagId, String entityType) {
        QueryWrapper<FocusTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusTagRelDO::getTagId, tagId)
                .eq(FocusTagRelDO::getEntityType, entityType);
        
        List<FocusTagRelDO> relList = this.list(queryWrapper);
        return relList.stream()
                .map(FocusTagRelDO::getEntityId)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveEntityTagRelation(FocusTagRelDTO focusTagRelDTO) {
        Long entityId = focusTagRelDTO.getEntityId();
        String entityType = focusTagRelDTO.getEntityType();
        List<Long> tagIds = focusTagRelDTO.getTagIds();
        
        // 先删除原有的关联关系
        deleteByEntityIdAndType(entityId, entityType);
        
        // 批量插入新的关联关系
        if (tagIds != null && !tagIds.isEmpty()) {
            List<FocusTagRelDO> relList = tagIds.stream()
                    .map(tagId -> FocusTagRelDO.builder()
                            .entityId(entityId)
                            .entityType(entityType)
                            .tagId(tagId)
                            .build())
                    .collect(Collectors.toList());
            
            return this.saveBatch(relList);
        }
        
        return true;
    }
    
    @Override
    public void deleteByEntityIdAndType(Long entityId, String entityType) {
        QueryWrapper<FocusTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusTagRelDO::getEntityId, entityId)
                .eq(FocusTagRelDO::getEntityType, entityType);
        this.remove(queryWrapper);
    }
    
    @Override
    public void deleteByTagId(Long tagId) {
        QueryWrapper<FocusTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusTagRelDO::getTagId, tagId);
        this.remove(queryWrapper);
    }
}