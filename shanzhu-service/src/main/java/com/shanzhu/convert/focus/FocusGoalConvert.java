package com.shanzhu.convert.focus;

import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.model.focus.dto.FocusGoalSaveDTO;
import com.shanzhu.model.focus.vo.FocusGoalVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author shanzhu
 * @Date 2025/12/9 16:42
 */
@Mapper(componentModel = "spring")
public interface FocusGoalConvert {

    /**
     * 将FocusGoalSaveDTO转换为FocusGoalDO
     * @param focusGoalSaveDTO 源对象
     * @return 目标对象
     */
    FocusGoalDO convertToDo(FocusGoalSaveDTO focusGoalSaveDTO);

    /**
     * 将FocusGoalDO转换为FocusGoalSaveDTO
     * @param focusGoalDO 源对象
     * @return 目标对象
     */
    FocusGoalSaveDTO convertToDTO(FocusGoalDO focusGoalDO);

    /**
     * 将FocusGoalDO转换为FocusGoalVO
     * @param focusGoalDO 源对象
     * @return 目标对象
     */

    FocusGoalVO convertToVo(FocusGoalDO focusGoalDO);

    /**
     * 批量转换FocusGoalDO列表为FocusGoalVO列表
     * @param focusGoalDOList 源对象列表
     * @return 目标对象列表
     */
    List<FocusGoalVO> convertToVoList(List<FocusGoalDO> focusGoalDOList);
}
