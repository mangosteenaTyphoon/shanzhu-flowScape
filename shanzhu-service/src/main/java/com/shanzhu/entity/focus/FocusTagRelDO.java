package com.shanzhu.entity.focus;

import com.baomidou.mybatisplus.annotation.TableName;
import com.shanzhu.model.BaseEntity;
import lombok.*;

/**
 * 标签与目标/任务的通用关联实体类
 */
@TableName("biz_focus_tag_rel")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FocusTagRelDO {
    
    /**
     * 实体ID
     */
    private Long entityId;
    
    /**
     * 标签ID (关联到 tag 表的 ID)
     */
    private Long tagId;
    
    /**
     * 实体类型 (goal: 目标, task: 任务)
     */
    private String entityType;
}