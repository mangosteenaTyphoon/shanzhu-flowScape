package com.shanzhu.model.focus.dto;

import com.shanzhu.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 标签与目标/任务的通用关联 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FocusTagRelDTO extends BaseDTO {
    
    /**
     * 实体ID
     */
    private Long entityId;
    
    /**
     * 实体类型 (goal: 目标, task: 任务)
     */
    private String entityType;
    
    /**
     * 标签ID列表
     */
    private List<Long> tagIds;
}