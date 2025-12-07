package com.shanzhu.mapper.focus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.entity.focus.FocusTaskDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 专注任务 Mapper接口
 */
@Mapper
public interface FocusTaskMapper extends BaseMapper<FocusTaskDO> {
    
}