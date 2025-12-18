package com.shanzhu.service.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.model.focus.dto.FocusTaskDTO;
import com.shanzhu.model.focus.dto.FocusTaskSaveDTO;
import com.shanzhu.model.focus.vo.FocusTaskVO;

import java.util.List;

/**
 * 专注任务服务接口
 */
public interface FocusTaskService extends IService<FocusTaskDO> {

    /**
     * 分页查询专注任务
     * @param focusTaskDTO 查询条件
     * @return 分页结果
     */
    IPage<FocusTaskVO> queryPage(FocusTaskDTO focusTaskDTO);

    /**
     * 根据ID查询专注任务
     * @param id 主键ID
     * @return 专注任务信息
     */
    FocusTaskDO queryById(Long id);

    /**
     * 保存专注任务
     * @param focusTaskSaveDTO 专注任务信息
     * @return 结果信息
     */
    boolean save(FocusTaskSaveDTO focusTaskSaveDTO);

    /**
     * 根据ID删除专注任务
     * @param ids 主键ID列表
     */
    void deleteByIds(List<Long> ids);
}