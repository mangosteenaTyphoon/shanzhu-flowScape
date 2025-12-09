package com.shanzhu.model.focus.dto;

import com.shanzhu.entity.focus.FocusTaskDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专注任务保存 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FocusTaskSaveDTO extends FocusTaskDO {

    /**
     * 标签ID数组
     */
    private String[] tagIds;
}