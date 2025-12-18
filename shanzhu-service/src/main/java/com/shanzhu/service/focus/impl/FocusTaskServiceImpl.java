package com.shanzhu.service.focus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.entity.focus.FocusTagDO;
import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.event.focus.FocusTaskChangeEvent;
import com.shanzhu.mapper.focus.FocusTaskMapper;
import com.shanzhu.model.focus.dto.FocusTagRelDTO;
import com.shanzhu.model.focus.dto.FocusTaskDTO;
import com.shanzhu.model.focus.dto.FocusTaskSaveDTO;
import com.shanzhu.model.focus.vo.FocusTaskVO;
import com.shanzhu.config.focus.TaskTimeoutConfig;
import com.shanzhu.service.focus.FocusGoalService;
import com.shanzhu.service.focus.FocusTagRelService;
import com.shanzhu.service.focus.FocusTagService;
import com.shanzhu.service.focus.FocusTaskService;
import com.shanzhu.utils.security.LoginUserContext;
import com.shanzhu.utils.spring.SpringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专注任务服务实现类
 */
@Service
@Slf4j
public class FocusTaskServiceImpl extends ServiceImpl<FocusTaskMapper, FocusTaskDO> implements FocusTaskService {

    @Resource
    private FocusTagRelService focusTagRelService;

    @Resource
    private FocusTagService focusTagService;

    @Resource
    private FocusGoalService focusGoalService;

    @Resource
    private TaskTimeoutConfig taskTimeoutConfig;

    @Override
    public IPage<FocusTaskVO> queryPage(FocusTaskDTO focusTaskDTO) {
        // 1. 查询分页数据
        IPage<FocusTaskDO> page = new Page<>(focusTaskDTO.getPageNum(), focusTaskDTO.getPageSize());
        QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (StringUtils.hasText(focusTaskDTO.getTitle())) {
            queryWrapper.lambda().like(FocusTaskDO::getTitle, focusTaskDTO.getTitle());
        }

        if (StringUtils.hasText(focusTaskDTO.getStatus())) {
            queryWrapper.lambda().eq(FocusTaskDO::getStatus, focusTaskDTO.getStatus());
        }

        if (focusTaskDTO.getGoalId() != null) {
            queryWrapper.lambda().eq(FocusTaskDO::getGoalId, focusTaskDTO.getGoalId());
        }

        // 只查询当前用户的数据
        queryWrapper.lambda().eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());

        // 按ID降序排列
        queryWrapper.lambda().orderByDesc(FocusTaskDO::getId);

        IPage<FocusTaskDO> taskPage = this.page(page, queryWrapper);

        // 2. 转换为 VO 并填充额外信息
        IPage<FocusTaskVO> voPage = new Page<>(taskPage.getCurrent(), taskPage.getSize(), taskPage.getTotal());
        List<FocusTaskVO> voList = taskPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    /**
     * 将 FocusTaskDO 转换为 FocusTaskVO
     */
    private FocusTaskVO convertToVO(FocusTaskDO taskDO) {

        // 复制基本属性
        FocusTaskVO vo = new FocusTaskVO();
        BeanUtils.copyProperties(taskDO, vo);

        // 填充标签信息
        List<Long> tagIds = focusTagRelService.queryTagIdsByEntityIdAndType(taskDO.getId(), "task");
        if (tagIds != null && !tagIds.isEmpty()) {
            List<FocusTagDO> tags = focusTagService.listByIds(tagIds);
            vo.setTags(tags);
        }

        // 填充所属目标信息
        if (taskDO.getGoalId() != null) {
            FocusGoalDO goal = focusGoalService.getById(taskDO.getGoalId());
            vo.setGoal(goal);
        }

        // 转换时长格式
        vo.setExpectedDuration(formatDuration(taskDO.getExpectedDurationSec()));
        vo.setActualConsumedTime(formatDuration(taskDO.getActualConsumedSec()));

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
    public FocusTaskDO queryById(Long id) {
        QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FocusTaskDO::getId, id)
                .eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(FocusTaskSaveDTO focusTaskSaveDTO) {
        // 获取旧任务数据（用于对比变化）
        FocusTaskDO oldTask = null;
        if (focusTaskSaveDTO.getId() != null) {
            oldTask = this.getById(focusTaskSaveDTO.getId());
        }

        // 设置用户ID
        focusTaskSaveDTO.setUserId(Long.valueOf(LoginUserContext.getUserId()));

        // 新增：任务状态完成时自动设置进度为100%
        if ("completed".equals(focusTaskSaveDTO.getStatus()) || "完成".equals(focusTaskSaveDTO.getStatus())) {
            focusTaskSaveDTO.setProgressRate(100);
            log.info("任务状态设置为完成，自动调整进度为100%: taskId={}, title={}",
                    focusTaskSaveDTO.getId(), focusTaskSaveDTO.getTitle());
        }

        // 新增：自动计算预期持续时间（基于计划开始和结束时间）
        if (focusTaskSaveDTO.getPlanStartDate() != null && focusTaskSaveDTO.getPlanEndDate() != null) {
            long seconds = Duration.between(focusTaskSaveDTO.getPlanStartDate(), focusTaskSaveDTO.getPlanEndDate()).getSeconds();
            focusTaskSaveDTO.setExpectedDurationSec((int) seconds);
            log.debug("自动计算预期持续时间: {} 秒", seconds);
        }

        // 新增：自动计算实际消耗时间（基于实际开始和结束时间）
        if (focusTaskSaveDTO.getActualStartDate() != null && focusTaskSaveDTO.getActualEndDate() != null) {
            long seconds = Duration.between(focusTaskSaveDTO.getActualStartDate(), focusTaskSaveDTO.getActualEndDate()).getSeconds();
            focusTaskSaveDTO.setActualConsumedSec((int) seconds);
            log.debug("自动计算实际消耗时间: {} 秒", seconds);
        }

        // 根据实际完成日期与计划结束日期对比，自动设置任务状态
        if (focusTaskSaveDTO.getActualEndDate() != null && focusTaskSaveDTO.getPlanEndDate() != null) {
            // 计算允许延迟的截止时间
            LocalDateTime allowedDelayDeadline = focusTaskSaveDTO.getPlanEndDate()
                    .plusSeconds(taskTimeoutConfig.getAllowedDelaySeconds());

            if (focusTaskSaveDTO.getActualEndDate().isAfter(allowedDelayDeadline)) {
                // 完成时间 > 计划结束时间 + 容忍延迟时间：逾期完成
                focusTaskSaveDTO.setStatus("completedOverdue");
                log.info("任务逾期完成，自动设置状态为逾期完成: taskId={}, title={}, 计划结束: {}, 实际结束: {}, 容忍延迟: {}分钟",
                        focusTaskSaveDTO.getId(), focusTaskSaveDTO.getTitle(),
                        focusTaskSaveDTO.getPlanEndDate(), focusTaskSaveDTO.getActualEndDate(),
                        taskTimeoutConfig.getAllowedDelayMinutes());
            } else if (focusTaskSaveDTO.getActualEndDate().isAfter(focusTaskSaveDTO.getPlanEndDate())) {
                // 计划结束时间 < 完成时间 <= 计划结束时间 + 容忍延迟时间：超期完成（可接受）
                focusTaskSaveDTO.setStatus("completedOverdueAllowed");
                log.info("任务超期完成（可接受范围），自动设置状态为超期完成: taskId={}, title={}, 计划结束: {}, 实际结束: {}, 容忍延迟: {}分钟",
                        focusTaskSaveDTO.getId(), focusTaskSaveDTO.getTitle(),
                        focusTaskSaveDTO.getPlanEndDate(), focusTaskSaveDTO.getActualEndDate(),
                        taskTimeoutConfig.getAllowedDelayMinutes());
            } else {
                // 完成时间 <= 计划结束时间：按时完成
                if ("completedOverdue".equals(focusTaskSaveDTO.getStatus()) ||
                        "completedOverdueAllowed".equals(focusTaskSaveDTO.getStatus()) ||
                        focusTaskSaveDTO.getStatus() == null ||
                        "in_progress".equals(focusTaskSaveDTO.getStatus())) {
                    focusTaskSaveDTO.setStatus("done");
                    log.info("任务按时完成，自动设置状态为已完成: taskId={}, title={}, 计划结束: {}, 实际结束: {}",
                            focusTaskSaveDTO.getId(), focusTaskSaveDTO.getTitle(),
                            focusTaskSaveDTO.getPlanEndDate(), focusTaskSaveDTO.getActualEndDate());
                }
            }
        }

        // 使用MapStruct转换DTO到DO
        FocusTaskDO focusTaskDO = new FocusTaskDO();
        BeanUtils.copyProperties(focusTaskSaveDTO, focusTaskDO);

        boolean result = false;
        if (focusTaskSaveDTO.getId() == null) {
            // 新增 - 使用 super.save() 避免递归
            result = super.save(focusTaskDO);

            // 更新DTO中的ID（用于后续标签关联）
            if (result) {
                focusTaskSaveDTO.setId(focusTaskDO.getId());
            }
        } else {
            // 更新
            QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FocusTaskDO::getId, focusTaskSaveDTO.getId())
                    .eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());

            // 先查询是否存在
            FocusTaskDO existingTask = this.getOne(queryWrapper);
            if (existingTask != null) {
                // 更新现有记录
                result = this.updateById(focusTaskDO);
            } else {
                log.warn("任务不存在或无权限操作, taskId: {}, userId: {}",
                        focusTaskSaveDTO.getId(), LoginUserContext.getUserId());
                return false;
            }
        }

        // 如果保存成功，处理标签关联关系
        if (result) {
            createTagRelations(focusTaskDO.getId(), focusTaskSaveDTO.getTagIds());

            // 🚀 新增：发布任务变更事件
            publishTaskChangeEvent(focusTaskSaveDTO, oldTask);
        }

        return result;
    }

    /**
     * 创建或更新标签关联关系
     * @param taskId 任务ID
     * @param tagIds 标签ID数组
     */
    private void createTagRelations(Long taskId, String[] tagIds) {
        // 删除原有的标签关联关系
        focusTagRelService.deleteByEntityIdAndType(taskId, "task");

        // 保存新的标签关联关系
        if (tagIds != null && tagIds.length > 0) {
            // 创建FocusTagRelDTO对象用于保存标签关联
            FocusTagRelDTO tagRelDTO = new FocusTagRelDTO();
            tagRelDTO.setEntityId(taskId);
            tagRelDTO.setEntityType("task");

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
        // 🚀 修改：删除前获取任务信息，用于发布事件
        List<FocusTaskDO> tasksToDelete = this.listByIds(ids);

        QueryWrapper<FocusTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(FocusTaskDO::getId, ids)
                .eq(FocusTaskDO::getUserId, LoginUserContext.getUserId());
        this.remove(queryWrapper);

        // 🚀 新增：发布删除事件
        publishTaskDeleteEvents(tasksToDelete);
    }

    /**
     * 🚀 新增：发布任务变更事件的方法
     */
    private void publishTaskChangeEvent(FocusTaskSaveDTO newTask, FocusTaskDO oldTask) {
        if (newTask.getGoalId() == null) {
            return; // 没有关联目标，不需要发布事件
        }

        try {
            FocusTaskChangeEvent event;

            if (oldTask == null) {
                // 新增任务
                event = FocusTaskChangeEvent.createEvent(
                        this,
                        newTask.getId(),
                        newTask.getGoalId(),
                        newTask.getProgressRate(),
                        newTask.getActualConsumedSec(),
                        newTask.getStatus()
                );
            } else {
                // 更新任务
                event = FocusTaskChangeEvent.updateEvent(
                        this,
                        newTask.getId(),
                        newTask.getGoalId(),
                        oldTask.getProgressRate(),
                        newTask.getProgressRate(),
                        oldTask.getActualConsumedSec(),
                        newTask.getActualConsumedSec(),
                        oldTask.getStatus(),
                        newTask.getStatus()
                );
            }

            // 发布事件
            SpringUtils.getApplicationContext().publishEvent(event);

            log.debug("📤 任务变更事件已发布: taskId={}, goalId={}, type={}",
                    newTask.getId(), newTask.getGoalId(), oldTask == null ? "CREATE" : "UPDATE");

        } catch (Exception e) {
            log.error("❌ 发布任务变更事件失败: taskId={}, goalId={}, error={}",
                    newTask.getId(), newTask.getGoalId(), e.getMessage(), e);
            // 事件发布失败不影响主业务
        }
    }

    /**
     * 🚀 新增：发布任务删除事件的方法
     */
    private void publishTaskDeleteEvents(List<FocusTaskDO> deletedTasks) {
        for (FocusTaskDO task : deletedTasks) {
            if (task.getGoalId() != null) {
                try {
                    FocusTaskChangeEvent event = FocusTaskChangeEvent.deleteEvent(
                            this,
                            task.getId(),
                            task.getGoalId(),
                            task.getProgressRate(),
                            task.getActualConsumedSec(),
                            task.getStatus()
                    );

                    SpringUtils.getApplicationContext().publishEvent(event);

                    log.debug("任务删除事件已发布: taskId={}, goalId={}",
                            task.getId(), task.getGoalId());

                } catch (Exception e) {
                    log.error("发布任务删除事件失败: taskId={}, goalId={}, error={}",
                            task.getId(), task.getGoalId(), e.getMessage(), e);
                }
            }
        }
    }
}