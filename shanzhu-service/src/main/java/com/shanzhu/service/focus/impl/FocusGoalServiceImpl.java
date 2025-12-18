package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.config.focus.TaskScoreConfig;
import com.shanzhu.entity.focus.FocusCategoryDO;
import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.entity.focus.FocusTagDO;
import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.mapper.focus.FocusGoalMapper;
import com.shanzhu.model.focus.dto.FocusGoalDTO;
import com.shanzhu.model.focus.dto.FocusGoalSaveDTO;
import com.shanzhu.model.focus.dto.FocusTagRelDTO;
import com.shanzhu.model.focus.vo.FocusGoalVO;
import com.shanzhu.service.focus.FocusCategoryService;
import com.shanzhu.service.focus.FocusGoalService;
import com.shanzhu.service.focus.FocusTagRelService;
import com.shanzhu.service.focus.FocusTagService;
import com.shanzhu.service.focus.FocusTaskService;
import com.shanzhu.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专注目标服务实现类
 */
@Slf4j
@Service
public class FocusGoalServiceImpl extends ServiceImpl<FocusGoalMapper, FocusGoalDO> implements FocusGoalService {


    @Resource
    private FocusTagRelService focusTagRelService;

    @Resource
    private FocusCategoryService focusCategoryService;

    @Resource
    private FocusTagService focusTagService;

    @Resource
    private TaskScoreConfig taskScoreConfig;

    @Resource
    @Lazy
    private FocusTaskService focusTaskService;

    @Override
    public IPage<FocusGoalVO> queryPage(FocusGoalDTO focusGoalDTO) {
        // 1. 查询分页数据
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

        IPage<FocusGoalDO> goalPage = this.page(page, queryWrapper);

        // 2. 转换为 VO 并填充额外信息
        IPage<FocusGoalVO> voPage = new Page<>(goalPage.getCurrent(), goalPage.getSize(), goalPage.getTotal());
        List<FocusGoalVO> voList = goalPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    /**
     * 将 FocusGoalDO 转换为 FocusGoalVO
     */
    private FocusGoalVO convertToVO(FocusGoalDO goalDO) {


        // 复制基本属性
        FocusGoalVO vo = new FocusGoalVO();
        BeanUtils.copyProperties(goalDO, vo);

        // 填充标签信息
        List<Long> tagIds = focusTagRelService.queryTagIdsByEntityIdAndType(goalDO.getId(), "goal");
        if (tagIds != null && !tagIds.isEmpty()) {
            List<FocusTagDO> tags = focusTagService.listByIds(tagIds);
            vo.setTags(tags);
        }

        // 填充分类信息
        if (goalDO.getCategoryId() != null) {
            FocusCategoryDO category = focusCategoryService.getById(goalDO.getCategoryId());
            vo.setCategory(category);
        }

        // 转换时长格式
        vo.setExpectedDuration(formatDuration(goalDO.getExpectedDurationSec()));
        vo.setActualDuration(formatDuration(goalDO.getActualDurationSec()));

        return vo;
    }

    /**
     * 格式化时长
     * @param seconds 秒数
     * @return 格式化后的字符串（如：56分钟 或 2.5小时）
     */
    private String formatDuration(Integer seconds) {
        if (seconds == null || seconds == 0) {
            return "0分钟";
        }

        // 小于1小时（3600秒），显示为分钟
        if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "分钟";
        }

        // 大于等于1小时，显示为小时（保留1位小数）
        double hours = seconds / 3600.0;
        return String.format("%.1f小时", hours);
    }

    @Override
    public FocusGoalDO queryById(Long id) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusGoalDO::getId, id)
                .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(FocusGoalSaveDTO focusGoalSaveDTO) {
        // 获取并验证用户ID
        String userId = LoginUserContext.getUserId();
        if (userId == null || userId.isEmpty()) {
            log.error("保存专注目标失败：无法获取当前登录用户ID");
            throw new RuntimeException("用户未登录或登录已过期，请重新登录");
        }

        // 设置用户ID
        focusGoalSaveDTO.setUserId(Long.valueOf(userId));
        FocusGoalDO focusGoalDO = new FocusGoalDO();
        BeanUtils.copyProperties(focusGoalSaveDTO, focusGoalDO);

        boolean result = false;
        if (focusGoalSaveDTO.getId() == null) {
            result = super.save(focusGoalDO);
            if (result) {
                focusGoalSaveDTO.setId(focusGoalDO.getId());
            }
        } else {
            QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FocusGoalDO::getId, focusGoalSaveDTO.getId())
                    .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());

            // 先查询是否存在
            FocusGoalDO existingGoal = this.getOne(queryWrapper);
            if (existingGoal != null) {
                // 更新现有记录
                result = this.updateById(focusGoalDO);
            } else {
                log.warn("目标不存在或无权限操作, goalId: {}, userId: {}", focusGoalSaveDTO.getId(), LoginUserContext.getUserId());
                return false;
            }
        }

        if (result) {
            createTagRelations(focusGoalDO.getId(), focusGoalSaveDTO.getTagIds());

            // 如果目标状态为已完成，计算目标评分
            if ("completed".equals(focusGoalSaveDTO.getStatus())) {
                Double goalScore = calculateGoalScore(focusGoalDO.getId());
                if (goalScore != null) {
                    focusGoalDO.setGoalScore(goalScore);
                    this.updateById(focusGoalDO);
                }
            }
        }
        return result;
    }

    /**
     * 创建或更新标签关联关系
     * @param goalId 目标ID
     * @param tagIds 标签ID数组
     */
    private void createTagRelations(Long goalId, String[] tagIds) {
        // 删除原有的标签关联关系
        focusTagRelService.deleteByEntityIdAndType(goalId, "goal");

        // 保存新的标签关联关系
        if (tagIds != null && tagIds.length > 0) {
            // 创建FocusTagRelDTO对象用于保存标签关联
            FocusTagRelDTO tagRelDTO = new FocusTagRelDTO();
            tagRelDTO.setEntityId(goalId);
            tagRelDTO.setEntityType("goal");

            // 将String数组转换为List<Long>
            List<Long> tagIdList = new ArrayList<>();
            for (String tagId : tagIds) {
                try {
                    tagIdList.add(Long.valueOf(tagId));
                } catch (NumberFormatException e) {
                    // 忽略无效的标签ID
                    log.warn("Invalid tagId: {}", tagId, e);
                }
            }

            if (!tagIdList.isEmpty()) {
                tagRelDTO.setTagIds(tagIdList);
                // 保存标签关联关系
                focusTagRelService.saveEntityTagRelation(tagRelDTO);
            }
        }
    }



    @Override
    public void deleteByIds(List<Long> ids) {
        QueryWrapper<FocusGoalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(FocusGoalDO::getId, ids)
                .eq(FocusGoalDO::getUserId, LoginUserContext.getUserId());
        this.remove(queryWrapper);
    }

    /**
     * 计算目标综合评分
     *
     * @param goalId 目标ID
     * @return 目标评分（百分制，范围 0-100）
     */
    public Double calculateGoalScore(Long goalId) {
        try {
            // 查询目标下的所有任务
            QueryWrapper<FocusTaskDO> taskQueryWrapper = new QueryWrapper<>();
            taskQueryWrapper.lambda().eq(FocusTaskDO::getGoalId, goalId);
            List<FocusTaskDO> tasks = focusTaskService.list(taskQueryWrapper);

            if (tasks == null || tasks.isEmpty()) {
                log.warn("目标{}下没有任务，无法计算评分", goalId);
                return 0.0;
            }

            // 计算总权重（用于权重归一化）
            int totalWeight = tasks.stream()
                    .mapToInt(task -> task.getWeight() != null ? task.getWeight() : 0)
                    .sum();

            if (totalWeight == 0) {
                log.warn("目标{}下任务总权重为0，无法计算评分", goalId);
                return 0.0;
            }

            // 计算加权评分 Σ(wi × qi × pi)
            double totalScore = 0.0;

            for (FocusTaskDO task : tasks) {
                // wi = 权重百分比转小数（归一化处理）
                double weight = (task.getWeight() != null ? task.getWeight() : 0) / (double) totalWeight;

                // qi = 质量分数
                double qualityScore = getQualityScore(task.getQualityGrade());

                // pi = 时效系数
                double timeCoefficient = getTimeCostCoefficient(task.getStatus());

                // 计算当前任务贡献分
                double taskScore = weight * qualityScore * timeCoefficient;
                totalScore += taskScore;

                log.debug("任务{}评分计算: 权重={}, 质量分={}, 时效系数={}, 贡献分={}",
                        task.getTitle(), weight, qualityScore, timeCoefficient, taskScore);
            }

            // 确保评分在合理范围内 [0, 100]
            totalScore = Math.max(0.0, Math.min(100.0, totalScore));

            log.info("目标{}评分计算完成，最终评分: {}", goalId, totalScore);
            return totalScore;

        } catch (Exception e) {
            log.error("计算目标{}评分时出现异常", goalId, e);
            return null;
        }
    }

    /**
     * 获取质量等级对应的分数
     *
     * @param qualityGrade 质量等级 (A/B/C/D)
     * @return 对应分数（百分制）
     */
    private double getQualityScore(String qualityGrade) {
        if (qualityGrade == null || qualityGrade.trim().isEmpty()) {
            return taskScoreConfig.getQualityGrade().getOrDefault("C", 50.0);
        }

        String grade = qualityGrade.toUpperCase();
        Double score = taskScoreConfig.getQualityGrade().get(grade);
        if (score != null) {
            return score;
        }

        Double defaultScore = taskScoreConfig.getQualityGrade().getOrDefault("C", 50.0);
        log.warn("未知的质量等级: {}, 使用默认分数{}", qualityGrade, defaultScore);
        return defaultScore;
    }

    /**
     * 获取任务状态对应的时效系数
     *
     * @param status 任务状态
     * @return 时效系数
     */
    private double getTimeCostCoefficient(String status) {
        if (status == null || status.trim().isEmpty()) {
            return taskScoreConfig.getStatus().getOrDefault("incompleteoverdue", 0.5);
        }

        String statusKey = status.toLowerCase();
        Double coefficient = taskScoreConfig.getStatus().get(statusKey);
        if (coefficient != null) {
            return coefficient;
        }

        Double defaultCoefficient = taskScoreConfig.getStatus().getOrDefault("incompleteoverdue", 0.5);
        log.warn("未知的任务状态: {}, 使用默认时效系数{}", status, defaultCoefficient);
        return defaultCoefficient;
    }
}