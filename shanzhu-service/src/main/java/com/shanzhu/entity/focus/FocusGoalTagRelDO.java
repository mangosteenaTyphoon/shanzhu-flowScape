package com.shanzhu.entity.focus;

import com.baomidou.mybatisplus.annotation.TableName;
import com.shanzhu.model.BaseEntity;
import lombok.*;

/**
 * 目标与标签关联实体类
 */
@TableName("biz_focus_goal_tag_rel")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FocusGoalTagRelDO  {
    
    /**
     * 目标ID
     */
    private Long goalId;
    
    /**
     * 标签ID (逻辑关联到 tag.id)
     */
    private Long tagId;
}