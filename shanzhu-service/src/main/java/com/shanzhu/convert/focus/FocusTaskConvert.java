package com.shanzhu.convert.focus;

import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.model.focus.dto.FocusTaskSaveDTO;
import com.shanzhu.model.focus.vo.FocusTaskVO;
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
     * 批量转换FocusTaskSaveDTO列表为FocusTaskDO列表
     * @param focusTaskSaveDTOList 源对象列表
     * @return 目标对象列表
     */
    List<FocusTaskDO> convertToDoList(List<FocusTaskSaveDTO> focusTaskSaveDTOList);

    /**
     * 将FocusTaskDO转换为FocusTaskVO
     * @param taskDO 源对象
     * @return 目标对象
     */
    FocusTaskVO convertToVo(FocusTaskDO taskDO);

    /**
     * 批量转换FocusTaskDO列表为FocusTaskVO列表
     * @param taskDOList 源对象列表
     * @return 目标对象列表
     */
    List<FocusTaskVO> convertToVoList(List<FocusTaskDO> taskDOList);
}