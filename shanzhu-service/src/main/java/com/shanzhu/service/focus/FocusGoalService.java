package com.shanzhu.service.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.model.focus.dto.FocusGoalDTO;

import java.util.List;

/**
 * 专注目标服务接口
 */
public interface FocusGoalService extends IService<FocusGoalDO> {
    
    /**
     * 分页查询专注目标
     * @param focusGoalDTO 查询条件
     * @return 分页结果
     */
    IPage<FocusGoalDO> queryPage(FocusGoalDTO focusGoalDTO);
    
    /**
     * 查询专注目标列表
     * @param focusGoal 查询条件
     * @return 列表结果
     */
    List<FocusGoalDO> queryList(FocusGoalDO focusGoal);
    
    /**
     * 根据ID查询专注目标
     * @param id 主键ID
     * @return 专注目标信息
     */
    FocusGoalDO queryById(Long id);
    
    /**
     * 保存专注目标
     * @param focusGoalDO 专注目标信息
     * @return 结果信息
     */
    boolean save(FocusGoalDO focusGoalDO);
    
    /**
     * 根据ID删除专注目标
     * @param ids 主键ID列表
     */
    void deleteByIds(List<Long> ids);
}