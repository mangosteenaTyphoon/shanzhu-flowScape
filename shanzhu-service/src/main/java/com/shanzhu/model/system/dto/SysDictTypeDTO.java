package com.shanzhu.model.system.dto;

import com.shanzhu.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictTypeDTO extends BaseDTO {
    private String name;
    private String code;
    private String status;
    private List<LocalDate> startEndTime;
}
