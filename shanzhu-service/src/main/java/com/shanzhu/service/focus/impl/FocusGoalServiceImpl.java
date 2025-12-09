package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.mapper.focus.FocusGoalMapper;
import com.shanzhu.model.focus.dto.FocusGoalDTO;
import com.shanzhu.service.focus.FocusCategoryService;
import com.shanzhu.service.focus.FocusGoalService;
import com.shanzhu.service.focus.FocusTagRelService;
import com.shanzhu.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 专注目标服务实现类
 */
@Service
public class FocusGoalServiceImpl extends ServiceImpl<FocusGoalMapper, FocusGoalDO> implements FocusGoalService {

    @Resource
    private FocusTagRelService focusTagRelService;

    @Resource
    private FocusCategoryService focusCategoryService;
    
    @Override
    public IPage<FocusGoalDO> queryPage(FocusGoalDTO focusGoalDTO) {
        IPage<FocusGoalDO> page = new Page<>(focusGoalDTO.getPageNum(), focusGoalDTO.getPageSize());
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusGoalDTO.getTitle())) {
            queryWrapper.lambda().like(FocusGoalDO::getTitle, focusGoalDTO.getTitle());
        }
        
        if (StringUtils.hasText(focusGoalDTO.getStatus())) {
            queryWrapper.lambda().eq(FocusGoalDO::getStatus, focusGoalDTO.getStatus());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusGoalDO::getId);
        
        return this.page(page, queryWrapper);
    }
    
    @Override
    public List<FocusGoalDO> queryList(FocusGoalDO focusGoal) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(focusGoal.getTitle())) {
            queryWrapper.lambda().like(FocusGoalDO::getTitle, focusGoal.getTitle());
        }
        
        if (StringUtils.hasText(focusGoal.getStatus())) {
            queryWrapper.lambda().eq(FocusGoalDO::getStatus, focusGoal.getStatus());
        }
        
        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        
        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusGoalDO::getId);
        
        return this.list(queryWrapper);
    }
    
    @Override
    public FocusGoalDO queryById(Long id) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusGoalDO::getId, id)
                .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        return this.getOne(queryWrapper);
    }

    // 整体逻辑 先新增 目标 再新增 标签关联 然后新增 分类关联
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(FocusGoalDO focusGoalDO) {
        // 设置用户ID
        focusGoalDO.setUserId(Long.valueOf(LoginUserContext.getUserId()));
        boolean result = false;
        if (focusGoalDO.getId() == null) {
            // 新增
             result = this.save(focusGoalDO);
        } else {
            // 更新
            QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FocusGoalDO::getId, focusGoalDO.getId())
                    .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
            result = this.update(focusGoalDO, queryWrapper);
        }
        // 如果保存成功，处理关联关系
        if (result) {
            // 处理标签关联关系
            createTagRelations(focusGoalDO);

            // 分类关联已直接存储在FocusGoalDO的categoryId字段中，无需额外处理
        }
        return result;
    }

    /**
     * 创建或更新标签关联关系
     * @param focusGoalDO 专注目标实体
     */
    private void createTagRelations(FocusGoalDO focusGoalDO) {
        // 删除原有的标签关联关系
        focusTagRelService.deleteByEntityIdAndType(focusGoalDO.getId(), "goal");

        // 注意：由于FocusGoalDO中没有tagIds字段，这里无法直接从focusGoalDO获取标签ID列表
        // 需要通过其他方式传递标签ID列表，比如修改Controller层使用DTO接收参数
        // 暂时留空，等待进一步设计
    }
    
    @Override
    public void deleteByIds(List<Long> ids) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(FocusGoalDO::getId, ids)
                .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        this.remove(queryWrapper);
    }
}