package com.shanzhu.service.focus;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanzhu.entity.focus.FocusTagRelDO;
import com.shanzhu.model.focus.dto.FocusTagRelDTO;

import java.util.List;

/**
 * 标签与目标/任务的通用关联服务接口
 */
public interface FocusTagRelService extends IService<FocusTagRelDO> {
    
    /**
     * 根据实体ID和类型查询关联的标签ID列表
     * @param entityId 实体ID
     * @param entityType 实体类型
     * @return 标签ID列表
     */
    List<Long> queryTagIdsByEntityIdAndType(Long entityId, String entityType);
    
    /**
     * 根据标签ID和实体类型查询关联的实体ID列表
     * @param tagId 标签ID
     * @param entityType 实体类型
     * @return 实体ID列表
     */
    List<Long> queryEntityIdsByTagIdAndType(Long tagId, String entityType);
    
    /**
     * 保存实体与标签的关联关系
     * @param focusTagRelDTO 关联信息
     * @return 结果信息
     */
    Boolean saveEntityTagRelation(FocusTagRelDTO focusTagRelDTO);
    
    /**
     * 根据实体ID和类型删除关联关系
     * @param entityId 实体ID
     * @param entityType 实体类型
     */
    void deleteByEntityIdAndType(Long entityId, String entityType);
    
    /**
     * 根据标签ID删除关联关系
     * @param tagId 标签ID
     */
    void deleteByTagId(Long tagId);
}