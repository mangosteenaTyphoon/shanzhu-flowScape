package com.shanzhu.service.focus;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanzhu.entity.focus.FocusGoalTagRelDO;
import com.shanzhu.model.focus.dto.FocusGoalTagRelDTO;

import java.util.List;

/**
 * 目标与标签关联服务接口
 */
public interface FocusGoalTagRelService extends IService<FocusGoalTagRelDO> {
    
    /**
     * 根据目标ID查询关联的标签ID列表
     * @param goalId 目标ID
     * @return 标签ID列表
     */
    List<Long> queryTagIdsByGoalId(Long goalId);
    
    /**
     * 根据标签ID查询关联的目标ID列表
     * @param tagId 标签ID
     * @return 目标ID列表
     */
    List<Long> queryGoalIdsByTagId(Long tagId);
    
    /**
     * 保存目标与标签的关联关系
     * @param focusGoalTagRelDTO 关联信息
     * @return 结果信息
     */
    Boolean saveGoalTagRelation(FocusGoalTagRelDTO focusGoalTagRelDTO);
    
    /**
     * 根据目标ID删除关联关系
     * @param goalId 目标ID
     */
    void deleteByGoalId(Long goalId);
    
    /**
     * 根据标签ID删除关联关系
     * @param tagId 标签ID
     */
    void deleteByTagId(Long tagId);
}