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
}