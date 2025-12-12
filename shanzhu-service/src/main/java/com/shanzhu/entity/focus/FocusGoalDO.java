package com.shanzhu.entity.focus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.shanzhu.model.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 专注目标实体类
 */
@TableName("biz_focus_goal")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FocusGoalDO extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 目标标题
     */
    private String title;

    /**
     * 目标描述
     */
    private String description;

    /**
     * 分类ID (逻辑关联，非外键约束)
     */
    private Long categoryId;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endDate;

    /**
     * 目标状态 (draft: 草稿, active: 进行中, completed: 已完成, archived: 已归档)
     */
    private String status;

    /**
     * 最终进度百分比 (0-100)
     */
    private Integer finalProgress;

    /**
     * 完成状态 (early: 提前完成, on_time: 按时完成, delayed_completed: 延期完成, delayed_incomplete: 延期未完成)
     */
    private String completionStatus;

    /**
     * 是否存在延期任务
     */
    private Boolean hasDelayedTasks;

    /**
     * 预期持续时间（秒）
     */
    private Integer expectedDurationSec;

    /**
     * 实际持续时间（秒）
     */
    private Integer actualDurationSec;

    /**
     * 逾期完成时间（秒）
     */
    private Integer overdueCompletionTimeSec;

    /**
     * 逻辑删除
     */
    private String delFlag;
}