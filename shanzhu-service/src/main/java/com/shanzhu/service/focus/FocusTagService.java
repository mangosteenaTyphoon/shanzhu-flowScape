package com.shanzhu.service.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shanzhu.entity.focus.FocusTagDO;
import com.shanzhu.model.focus.dto.FocusTagDTO;

import java.util.List;

/**
 * 专注标签服务接口
 */
public interface FocusTagService extends IService<FocusTagDO> {
    
    /**
     * 分页查询专注标签
     * @param focusTagDTO 查询条件
     * @return 分页结果
     */
    IPage<FocusTagDO> queryPage(FocusTagDTO focusTagDTO);
    
    /**
     * 根据ID查询专注标签
     * @param id 主键ID
     * @return 专注标签信息
     */
    FocusTagDO queryById(Long id);
    
    /**
     * 保存专注标签
     * @param focusTagDO 专注标签信息
     * @return 结果信息
     */
    boolean save(FocusTagDO focusTagDO);
    
    /**
     * 根据ID删除专注标签
     * @param ids 主键ID列表
     */
    void deleteByIds(List<Long> ids);
}