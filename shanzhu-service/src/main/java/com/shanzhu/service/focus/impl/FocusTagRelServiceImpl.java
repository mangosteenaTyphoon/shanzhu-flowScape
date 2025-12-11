package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusTagRelDO;
import com.shanzhu.mapper.focus.FocusTagRelMapper;
import com.shanzhu.model.focus.dto.FocusTagRelDTO;
import com.shanzhu.service.focus.FocusTagRelService;
import com.shanzhu.utils.security.LoginUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签与目标/任务的通用关联服务实现类
 */
@Service
@Slf4j
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

        // 参数校验
        if (entityId == null || entityType == null) {
            log.warn("保存标签关联关系失败，参数不完整: entityId={}, entityType={}", entityId, entityType);
            return false;
        }

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

            boolean result = super.saveBatch(relList);

            if (result) {
                log.info("保存标签关联关系成功: entityId={}, entityType={}, tagCount={}",
                        entityId, entityType, tagIds.size());
            } else {
                log.warn("保存标签关联关系失败: entityId={}, entityType={}", entityId, entityType);
            }

            return result;
        }

        log.info("清空标签关联关系: entityId={}, entityType={}", entityId, entityType);
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