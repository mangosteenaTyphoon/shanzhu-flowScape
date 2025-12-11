package com.shanzhu.entity.focus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shanzhu.model.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 专注任务实体类
 */
@TableName("biz_focus_task")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FocusTaskDO extends BaseEntity {

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
     * 所属目标ID
     */
    private Long goalId;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 计划开始日期
     */
    private LocalDate planStartDate;

    /**
     * 计划结束日期
     */
    private LocalDate planEndDate;

    /**
     * 实际开始日期
     */
    private LocalDate actualStartDate;

    /**
     * 实际结束日期
     */
    private LocalDate actualEndDate;

    /**
     * 进度百分比 (0-100)
     */
    private Integer progressRate;

    /**
     * 权重 (0-100)
     */
    private Integer weight;

    /**
     * 任务状态 (todo: 待办, in_progress: 进行中, done: 已完成, cancelled: 已取消)
     */
    private String status;

    /**
     * 优先级 (high: 高, medium: 中, low: 低)
     */
    private String priority;

    /**
     * 预期持续时间（秒）
     */
    private Integer expectedDurationSec;

    /**
     * 实际消耗时间（秒）
     */
    private Integer actualConsumedSec;

    /**
     * 逻辑删除
     */
    private String delFlag;
}