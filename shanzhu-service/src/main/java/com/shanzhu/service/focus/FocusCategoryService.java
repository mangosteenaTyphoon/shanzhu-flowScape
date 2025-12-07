package com.shanzhu.service.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.shanzhu.entity.focus.FocusCategoryDO;
import com.shanzhu.model.finance.dto.FocusCategoryDTO;

import java.util.List;

/**
 * 专注分类服务接口
 */
public interface FocusCategoryService extends IService<FocusCategoryDO> {
    
    /**
     * 分页查询专注分类
     * @param focusCategoryDTO 查询条件
     * @return 分页结果
     */
    IPage<FocusCategoryDO> queryPage(FocusCategoryDTO focusCategoryDTO);
    
    /**
     * 查询专注分类列表
     * @param focusCategory 查询条件
     * @return 列表结果
     */
    List<FocusCategoryDO> queryList(FocusCategoryDO focusCategory);
    
    /**
     * 根据ID查询专注分类
     * @param id 主键ID
     * @return 专注分类信息
     */
    FocusCategoryDO queryById(Long id);
    
    /**
     * 保存专注分类
     *
     * @param focusCategory 专注分类信息
     * @return 结果信息
     */
    boolean save(FocusCategoryDO focusCategory);
    
    /**
     * 根据ID删除专注分类
     * @param ids 主键ID列表
     */
    void deleteByIds(List<Long> ids);
}
