package com.shanzhu.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.event.FocusTaskChangeEvent;
import com.shanzhu.service.focus.FocusGoalService;
import com.shanzhu.service.focus.FocusTaskService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 专注任务事件监听器
 * 监听任务变更事件，自动更新关联目标的进度和状态
 */
@Slf4j
@Component
public class FocusTaskEventListener {

    @Resource
    private FocusGoalService focusGoalService;
    
    @Resource
    private FocusTaskService focusTaskService;

    /**
     * 监听任务变更事件，自动更新目标进度和状态
     * 使用异步处理，避免影响主业务性能
     */
    @EventListener
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void handleTaskChangeEvent(FocusTaskChangeEvent event) {
        try {
            log.info("📢 收到任务变更事件: taskId={}, goalId={}, changeType={}", 
                    event.getTaskId(), event.getGoalId(), event.getChangeType());
            
            if (event.getGoalId() != null) {
                // 更新目标进度
                updateGoalProgress(event.getGoalId());
                
                // 更新目标状态
                updateGoalStatus(event.getGoalId());
                
                log.info("✅ 目标自动同步完成: goalId={}", event.getGoalId());
            }
        } catch (Exception e) {
            log.error("❌ 处理任务变更事件失败: taskId={}, goalId={}, error={}", 
                     event.getTaskId(), event.getGoalId(), e.getMessage(), e);
        }
    }

    /**
     * 计算并更新目标进度
     * 根据任务的权重和进度百分比计算目标的最终进度
     */
    private void updateGoalProgress(Long goalId) {
        // 查询目标下所有任务
        QueryWrapper<FocusTaskDO> taskWrapper = new QueryWrapper<>();
        taskWrapper.lambda().eq(FocusTaskDO::getGoalId, goalId);
        List<FocusTaskDO> tasks = focusTaskService.list(taskWrapper);
        
        if (tasks.isEmpty()) {
            log.debug("目标下无任务，跳过进度更新: goalId={}", goalId);
            return;
        }
        
        // 按权重计算总进度
        int totalWeight = tasks.stream()
                .mapToInt(task -> task.getWeight() != null ? task.getWeight() : 0)
                .sum();
                
        if (totalWeight <= 0) {
            log.warn("目标下任务权重总和为0，无法计算进度: goalId={}", goalId);
            return;
        }
                
        int weightedProgress = tasks.stream()
                .mapToInt(task -> {
                    int progress = task.getProgressRate() != null ? task.getProgressRate() : 0;
                    int weight = task.getWeight() != null ? task.getWeight() : 0;
                    return progress * weight;
                })
                .sum();
                
        int finalProgress = weightedProgress / totalWeight;
        
        // 计算总持续时间（实际消耗时间）
        int totalActualDuration = tasks.stream()
                .mapToInt(task -> task.getActualConsumedSec() != null ? task.getActualConsumedSec() : 0)
                .sum();
        
        // 计算预期持续时间（所有任务的预期时间总和）
        int totalExpectedDuration = tasks.stream()
                .mapToInt(task -> task.getExpectedDurationSec() != null ? task.getExpectedDurationSec() : 0)
                .sum();
        
        // 更新目标
        FocusGoalDO goal = focusGoalService.getById(goalId);
        if (goal != null) {
            boolean changed = false;
            
            // 更新最终进度
            if (!Integer.valueOf(finalProgress).equals(goal.getFinalProgress())) {
                goal.setFinalProgress(finalProgress);
                changed = true;
            }
            
            // 更新实际持续时间
            if (!Integer.valueOf(totalActualDuration).equals(goal.getActualDurationSec())) {
                goal.setActualDurationSec(totalActualDuration);
                changed = true;
            }
            
            // 更新预期持续时间（如果目标的预期时间为null或0）
            if ((goal.getExpectedDurationSec() == null || goal.getExpectedDurationSec() == 0) 
                && totalExpectedDuration > 0) {
                goal.setExpectedDurationSec(totalExpectedDuration);
                changed = true;
            }
            
            if (changed) {
                focusGoalService.updateById(goal);
                log.info("🎯 目标进度已更新: goalId={}, finalProgress={}%, actualDuration={}秒", 
                        goalId, finalProgress, totalActualDuration);
            }
        } else {
            log.warn("⚠️ 目标不存在: goalId={}", goalId);
        }
    }

    /**
     * 根据任务状态自动更新目标状态
     * - 所有任务完成 -> 目标完成
     * - 有任务进行中 -> 目标激活
     * - 所有任务取消 -> 目标归档
     */
    private void updateGoalStatus(Long goalId) {
        QueryWrapper<FocusTaskDO> taskWrapper = new QueryWrapper<>();
        taskWrapper.lambda().eq(FocusTaskDO::getGoalId, goalId);
        List<FocusTaskDO> tasks = focusTaskService.list(taskWrapper);
        
        if (tasks.isEmpty()) {
            return;
        }
        
        // 统计任务状态
        long completedCount = tasks.stream().filter(task -> "done".equals(task.getStatus())).count();
        long inProgressCount = tasks.stream().filter(task -> "in_progress".equals(task.getStatus())).count();
        long cancelledCount = tasks.stream().filter(task -> "cancelled".equals(task.getStatus())).count();
        long todoCount = tasks.stream().filter(task -> "todo".equals(task.getStatus())).count();
        
        FocusGoalDO goal = focusGoalService.getById(goalId);
        if (goal != null) {
            String newStatus = null;
            String completionStatus = null;
            
            // 判断目标状态
            if (completedCount == tasks.size()) {
                // 所有任务都完成
                newStatus = "completed";
                completionStatus = determineCompletionStatus(goal, tasks);
            } else if (cancelledCount == tasks.size()) {
                // 所有任务都取消
                newStatus = "archived";
            } else if (inProgressCount > 0) {
                // 有任务正在进行
                newStatus = "active";
            } else if (todoCount > 0 && !"draft".equals(goal.getStatus())) {
                // 有待办任务且目标不是草稿状态
                newStatus = "active";
            }
            
            // 判断是否有延期任务
            boolean hasDelayedTasks = tasks.stream()
                    .anyMatch(task -> task.getActualEndDate() != null 
                              && task.getPlanEndDate() != null
                              && task.getActualEndDate().isAfter(task.getPlanEndDate()));
            
            boolean updated = false;
            
            // 更新目标状态
            if (newStatus != null && !newStatus.equals(goal.getStatus())) {
                goal.setStatus(newStatus);
                updated = true;
            }
            
            // 更新完成状态
            if (completionStatus != null && !completionStatus.equals(goal.getCompletionStatus())) {
                goal.setCompletionStatus(completionStatus);
                updated = true;
            }
            
            // 更新是否有延期任务标志
            if (goal.getHasDelayedTasks() == null || goal.getHasDelayedTasks() != hasDelayedTasks) {
                goal.setHasDelayedTasks(hasDelayedTasks);
                updated = true;
            }
            
            if (updated) {
                focusGoalService.updateById(goal);
                log.info("🔄 目标状态已更新: goalId={}, status={}, completionStatus={}, hasDelayedTasks={}", 
                        goalId, goal.getStatus(), goal.getCompletionStatus(), hasDelayedTasks);
            }
        }
    }
    
    /**
     * 判断目标完成状态
     */
    private String determineCompletionStatus(FocusGoalDO goal, List<FocusTaskDO> tasks) {
        if (goal.getEndDate() == null) {
            return "on_time"; // 没有设置结束时间，默认按时完成
        }
        
        // 获取最晚完成的任务时间
        var latestTaskEndTime = tasks.stream()
                .filter(task -> task.getActualEndDate() != null)
                .map(task -> task.getActualEndDate())
                .max((t1, t2) -> t1.compareTo(t2));
        
        if (latestTaskEndTime.isEmpty()) {
            return "on_time";
        }
        
        var actualEndTime = latestTaskEndTime.get();
        
        if (actualEndTime.isBefore(goal.getEndDate())) {
            return "early"; // 提前完成
        } else if (actualEndTime.isEqual(goal.getEndDate())) {
            return "on_time"; // 按时完成
        } else {
            return "delayed_completed"; // 延期完成
        }
    }
}
