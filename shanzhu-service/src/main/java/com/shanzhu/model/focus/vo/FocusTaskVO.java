package com.shanzhu.model.focus.vo;

import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.entity.focus.FocusTagDO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专注任务 VO
 */
@Data
public class FocusTaskVO {

    /**
     * 主键ID
     */
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
    private LocalDateTime planStartDate;

    /**
     * 计划结束日期
     */
    private LocalDateTime planEndDate;

    /**
     * 实际开始日期
     */
    private LocalDateTime actualStartDate;

    /**
     * 实际结束日期
     */
    private LocalDateTime actualEndDate;

    /**
     * 进度百分比 (0-100)
     */
    private Integer progressRate;

    /**
     * 权重 (0-100)
     */
    private Integer weight;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 优先级
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
     * 任务质量等级（A=优秀, B=良好, C=合格, D=不及格）
     */
    private String qualityGrade;

    /**
     * 任务总结
     */
    private String taskSummary;

    // ============ 以下为额外的展示字段 ============

    /**
     * 标签列表
     */
    private List<FocusTagDO> tags;

    /**
     * 所属目标信息
     */
    private FocusGoalDO goal;

    /**
     * 预期持续时间（格式化后的字符串）
     * 如：56分钟 或 2.5小时
     */
    private String expectedDuration;

    /**
     * 实际消耗时间（格式化后的字符串）
     * 如：56分钟 或 2.5小时
     */
    private String actualConsumedTime;
}