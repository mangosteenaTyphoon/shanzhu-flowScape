package com.shanzhu.model.focus.dto;

import com.shanzhu.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 专注目标 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FocusGoalDTO extends BaseDTO {

    /**
     * 目标标题
     */
    private String title;

    /**
     * 目标状态
     */
    private String status;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 目标综合评分（满分4，可根据权重和任务表现计算）
     */
    private Double goalScore;

    /**
     * 目标执行总结（文字描述目标达成情况、问题、改进措施等）
     */
    private String goalSummary;
}