package com.shanzhu.mapper.focus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.entity.focus.FocusGoalTagRelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 目标与标签关联 Mapper接口
 */
@Mapper
public interface FocusGoalTagRelMapper extends BaseMapper<FocusGoalTagRelDO> {
    
}