package com.shanzhu.model.focus.dto;

import com.shanzhu.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 专注任务 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FocusTaskDTO extends BaseDTO {
    
    /**
     * 任务标题
     */
    private String title;
    
    /**
     * 所属目标ID
     */
    private Long goalId;
    
    /**
     * 任务状态
     */
    private String status;
    
    /**
     * 计划开始日期
     */
    private LocalDate planStartDate;
    
    /**
     * 计划结束日期
     */
    private LocalDate planEndDate;
}