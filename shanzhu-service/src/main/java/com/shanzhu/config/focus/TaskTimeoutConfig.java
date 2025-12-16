package com.shanzhu.config.focus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 任务超时配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "focus.task.timeout")
public class TaskTimeoutConfig {

    /**
     * 可容许的任务延迟完成时间（分钟）
     * 默认30分钟
     */
    private Integer allowedDelayMinutes = 30;

    /**
     * 获取可容许的延迟完成时间（秒）
     * @return 延迟时间秒数
     */
    public long getAllowedDelaySeconds() {
        return allowedDelayMinutes * 60L;
    }
}
