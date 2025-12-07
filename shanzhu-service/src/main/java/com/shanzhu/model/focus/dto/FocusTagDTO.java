package com.shanzhu.model.focus.dto;

import com.shanzhu.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专注标签 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FocusTagDTO extends BaseDTO {
    
    /**
     * 标签名称
     */
    private String name;
}