package com.shanzhu.convert.focus;

import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.model.focus.dto.FocusTaskSaveDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 专注任务转换器
 * @Author wangchao
 * @Date 2025/12/9
 */
@Mapper(componentModel = "spring")
public interface FocusTaskConvert {

    /**
     * 将FocusTaskSaveDTO转换为FocusTaskDO
     * @param focusTaskSaveDTO 源对象
     * @return 目标对象
     */
    FocusTaskDO convertToDo(FocusTaskSaveDTO focusTaskSaveDTO);

    /**
     * 将FocusTaskDO转换为FocusTaskSaveDTO
     * @param focusTaskDO 源对象
     * @return 目标对象
     */
    FocusTaskSaveDTO convertToDTO(FocusTaskDO focusTaskDO);

    /**
     * 批量转换FocusTaskSaveDTO列表为FocusTaskDO列表
     * @param focusTaskSaveDTOList 源对象列表
     * @return 目标对象列表
     */
    List<FocusTaskDO> convertToDoList(List<FocusTaskSaveDTO> focusTaskSaveDTOList);

    /**
     * 批量转换FocusTaskDO列表为FocusTaskSaveDTO列表
     * @param focusTaskDOList 源对象列表
     * @return 目标对象列表
     */
    List<FocusTaskSaveDTO> convertToDTOList(List<FocusTaskDO> focusTaskDOList);
}