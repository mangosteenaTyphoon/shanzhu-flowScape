package com.shanzhu.mapper.focus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.entity.focus.FocusTagRelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签与目标/任务的通用关联 Mapper接口
 */
@Mapper
public interface FocusTagRelMapper extends BaseMapper<FocusTagRelDO> {
    
}