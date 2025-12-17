package com.shanzhu.config.focus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务分数配置类
 * 用于管理质量等级和任务状态对应的分数映射
 */
@ConfigurationProperties(prefix = "task.score")
@Component
@Data
public class TaskScoreConfig {

    /**
     * 质量等级分数映射（百分制）
     * 建议配置：A=100.0, B=75.0, C=50.0, D=25.0
     */
    private Map<String, Double> qualityGrade = new HashMap<>();

    /**
     * 任务状态时效系数映射（0-1之间的系数）
     * 建议配置：done=1.0, completedOverdueAllowed=0.9, completedOverdue=0.8, incompleteOverdue=0.5
     */
    private Map<String, Double> status = new HashMap<>();
}
