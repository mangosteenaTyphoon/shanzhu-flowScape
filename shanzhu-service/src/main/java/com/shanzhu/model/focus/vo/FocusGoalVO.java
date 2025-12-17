package com.shanzhu.model.focus.vo;

import com.shanzhu.entity.focus.FocusCategoryDO;
import com.shanzhu.entity.focus.FocusTagDO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专注目标 VO
 */
@Data
public class FocusGoalVO {

    /**
     * 主键ID
     */
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
     * 分类ID
     */
    private Long categoryId;

    /**
     * 开始日期
     */
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    private LocalDateTime endDate;

    /**
     * 目标状态
     */
    private String status;

    /**
     * 最终进度百分比 (0-100)
     */
    private Integer finalProgress;

    /**
     * 完成状态
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
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // ============ 以下为额外的展示字段 ============

    /**
     * 标签列表
     */
    private List<FocusTagDO> tags;

    /**
     * 分类信息
     */
    private FocusCategoryDO category;

    /**
     * 预期持续时间（格式化后的字符串）
     * 如：56分钟 或 2.5小时
     */
    private String expectedDuration;

    /**
     * 实际持续时间（格式化后的字符串）
     * 如：56分钟 或 2.5小时
     */
    private String actualDuration;

    /**
     * 目标综合评分（满分4，可根据权重和任务表现计算）
     */
    private Double goalScore;

    /**
     * 目标执行总结（文字描述目标达成情况、问题、改进措施等）
     */
    private String goalSummary;
}