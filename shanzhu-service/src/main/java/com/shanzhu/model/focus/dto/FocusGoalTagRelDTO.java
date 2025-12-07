package com.shanzhu.model.focus.dto;

import com.shanzhu.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 目标与标签关联 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FocusGoalTagRelDTO extends BaseDTO {
    
    /**
     * 目标ID
     */
    private Long goalId;
    
    /**
     * 标签ID列表
     */
    private List<Long> tagIds;
}