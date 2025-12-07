package com.shanzhu.model.finance.dto;

import com.shanzhu.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专注分类 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FocusCategoryDTO extends BaseDTO {
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 类型 (goal, task, both)
     */
    private String type;
}
