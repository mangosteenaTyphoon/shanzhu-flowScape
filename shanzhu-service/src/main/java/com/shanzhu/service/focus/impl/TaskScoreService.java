package com.shanzhu.service.focus;

import com.shanzhu.config.focus.TaskScoreConfig;
import com.shanzhu.entity.focus.FocusTaskDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务分数计算服务类
 * 提供质量等级和任务状态分数的计算功能
 */
@Service
@Slf4j
public class TaskScoreService {

    @Autowired
    private TaskScoreConfig taskScoreConfig;

    /**
     * 获取质量等级分数
     * @param grade 质量等级（A、B、C、D）
     * @return 对应的分数，默认为0.0
     */
    public Double getQualityGradeScore(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            log.warn("质量等级为空，返回默认分数0.0");
            return 0.0;
        }

        Double score = taskScoreConfig.getQualityGrade().getOrDefault(grade.toUpperCase(), 0.0);
        log.debug("质量等级 {} 对应分数: {}", grade, score);
        return score;
    }

    /**
     * 获取任务状态分数
     * @param status 任务状态
     * @return 对应的分数，默认为0.0
     */
    public Double getStatusScore(String status) {
        if (status == null || status.trim().isEmpty()) {
            log.warn("任务状态为空，返回默认分数0.0");
            return 0.0;
        }

        Double score = taskScoreConfig.getStatus().getOrDefault(status, 0.0);
        log.debug("任务状态 {} 对应分数: {}", status, score);
        return score;
    }

    /**
     * 计算任务综合分数（质量等级分数 × 状态系数）
     * @param qualityGrade 质量等级
     * @param status 任务状态
     * @return 综合分数
     */
    public Double calculateTotalScore(String qualityGrade, String status) {
        Double gradeScore = getQualityGradeScore(qualityGrade);
        Double statusScore = getStatusScore(status);
        Double totalScore = gradeScore * statusScore;

        log.info("计算综合分数: 质量等级[{}]={}, 状态[{}]={}, 总分={}",
                qualityGrade, gradeScore, status, statusScore, totalScore);

        return totalScore;
    }

    /**
     * 计算任务综合分数（基于FocusTaskDO对象）
     * @param task 任务对象
     * @return 综合分数
     */
    public Double calculateTotalScore(FocusTaskDO task) {
        if (task == null) {
            log.warn("任务对象为空，返回默认分数0.0");
            return 0.0;
        }

        return calculateTotalScore(task.getQualityGrade(), task.getStatus());
    }

    /**
     * 获取质量等级描述
     * @param grade 质量等级
     * @return 等级描述
     */
    public String getQualityGradeDescription(String grade) {
        if (grade == null) return "未设置";

        switch (grade.toUpperCase()) {
            case "A": return "优秀";
            case "B": return "良好";
            case "C": return "合格";
            case "D": return "不及格";
            default: return "未知等级";
        }
    }

    /**
     * 获取任务状态描述
     * @param status 任务状态
     * @return 状态描述
     */
    public String getStatusDescription(String status) {
        if (status == null) return "未设置";

        switch (status) {
            case "todo": return "待办";
            case "in_progress": return "进行中";
            case "done": return "已完成";
            case "cancelled": return "已取消";
            case "completedOverdueAllowed": return "超期完成（可接受范围内）";
            case "completedOverdue": return "逾期完成";
            case "incompleteOverdue": return "逾期未完成";
            default: return status;
        }
    }
}
