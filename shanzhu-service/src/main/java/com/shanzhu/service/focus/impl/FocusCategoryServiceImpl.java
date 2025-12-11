package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shanzhu.entity.focus.FocusCategoryDO;
import com.shanzhu.mapper.focus.FocusCategoryMapper;
import com.shanzhu.model.finance.dto.FocusCategoryDTO;
import com.shanzhu.service.focus.FocusCategoryService;
import com.shanzhu.utils.security.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 专注分类服务实现类
 */
@Service
public class FocusCategoryServiceImpl extends ServiceImpl<FocusCategoryMapper, FocusCategoryDO> implements FocusCategoryService {

    @Override
    public IPage<FocusCategoryDO> queryPage(FocusCategoryDTO focusCategoryDTO) {
        IPage<FocusCategoryDO> page = new Page<>(focusCategoryDTO.getPageNum(), focusCategoryDTO.getPageSize());
        QueryWrapper<FocusCategoryDO> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (StringUtils.hasText(focusCategoryDTO.getName())) {
            queryWrapper.lambda().like(FocusCategoryDO::getName, focusCategoryDTO.getName());
        }

        if (StringUtils.hasText(focusCategoryDTO.getType())) {
            queryWrapper.lambda().eq(FocusCategoryDO::getType, focusCategoryDTO.getType());
        }

        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusCategoryDO::getUserId, LoginUserContext.getUserId());

        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusCategoryDO::getId);

        return this.page(page, queryWrapper);
    }

    @Override
    public List<FocusCategoryDO> queryList(FocusCategoryDO focusCategory) {
        QueryWrapper<FocusCategoryDO> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (StringUtils.hasText(focusCategory.getName())) {
            queryWrapper.lambda().like(FocusCategoryDO::getName, focusCategory.getName());
        }

        if (StringUtils.hasText(focusCategory.getType())) {
            queryWrapper.lambda().eq(FocusCategoryDO::getType, focusCategory.getType());
        }

        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusCategoryDO::getUserId, LoginUserContext.getUserId());

        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusCategoryDO::getId);

        return this.list(queryWrapper);
    }

    @Override
    public FocusCategoryDO queryById(Long id) {
        QueryWrapper<FocusCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusCategoryDO::getId, id)
                .eq(FocusCategoryDO::getUserId, LoginUserContext.getUserId());
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean save(FocusCategoryDO focusCategory) {
        // 设置用户ID
        focusCategory.setUserId(Long.valueOf(LoginUserContext.getUserId()));
        if (focusCategory.getId() == null) {
            // 新增 - 调用父类的 save 方法
            return super.save(focusCategory);
        } else {
            // 更新
            QueryWrapper<FocusCategoryDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FocusCategoryDO::getId, focusCategory.getId())
                    .eq(FocusCategoryDO::getUserId, LoginUserContext.getUserId());
            return super.update(focusCategory, queryWrapper);
        }
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        QueryWrapper<FocusCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(FocusCategoryDO::getId, ids)
                .eq(FocusCategoryDO::getUserId, LoginUserContext.getUserId());
        this.remove(queryWrapper);
    }
}
