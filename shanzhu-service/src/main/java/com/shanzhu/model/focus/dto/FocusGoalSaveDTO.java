package com.shanzhu.model.focus.dto;

import com.shanzhu.entity.focus.FocusGoalDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FocusGoalSaveDTO extends FocusGoalDO {

    private String[]  tagIds;
}
