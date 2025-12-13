package com.shanzhu.event.focus;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 专注任务变更事件
 * 用于在任务数据发生变化时通知相关监听器自动更新目标进度
 */
@Getter
public class FocusTaskChangeEvent extends ApplicationEvent {
    
    /**
     * 任务ID
     */
    private final Long taskId;
    
    /**
     * 关联的目标ID
     */
    private final Long goalId;
    
    /**
     * 变更类型：CREATE（新增）、UPDATE（更新）、DELETE（删除）
     */
    private final String changeType;
    
    /**
     * 旧的进度百分比
     */
    private final Integer oldProgressRate;
    
    /**
     * 新的进度百分比
     */
    private final Integer newProgressRate;
    
    /**
     * 旧的实际消耗时间（秒）
     */
    private final Integer oldActualConsumedSec;
    
    /**
     * 新的实际消耗时间（秒）
     */
    private final Integer newActualConsumedSec;
    
    /**
     * 旧的任务状态
     */
    private final String oldStatus;
    
    /**
     * 新的任务状态
     */
    private final String newStatus;
    
    /**
     * 构造函数
     */
    public FocusTaskChangeEvent(Object source, Long taskId, Long goalId, String changeType,
                               Integer oldProgressRate, Integer newProgressRate,
                               Integer oldActualConsumedSec, Integer newActualConsumedSec,
                               String oldStatus, String newStatus) {
        super(source);
        this.taskId = taskId;
        this.goalId = goalId;
        this.changeType = changeType;
        this.oldProgressRate = oldProgressRate;
        this.newProgressRate = newProgressRate;
        this.oldActualConsumedSec = oldActualConsumedSec;
        this.newActualConsumedSec = newActualConsumedSec;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
    
    /**
     * 便捷的构造函数用于新增操作
     */
    public static FocusTaskChangeEvent createEvent(Object source, Long taskId, Long goalId,
                                                  Integer progressRate, Integer actualConsumedSec, String status) {
        return new FocusTaskChangeEvent(source, taskId, goalId, "CREATE",
                null, progressRate, null, actualConsumedSec, null, status);
    }
    
    /**
     * 便捷的构造函数用于更新操作
     */
    public static FocusTaskChangeEvent updateEvent(Object source, Long taskId, Long goalId,
                                                  Integer oldProgressRate, Integer newProgressRate,
                                                  Integer oldActualConsumedSec, Integer newActualConsumedSec,
                                                  String oldStatus, String newStatus) {
        return new FocusTaskChangeEvent(source, taskId, goalId, "UPDATE",
                oldProgressRate, newProgressRate, oldActualConsumedSec, newActualConsumedSec,
                oldStatus, newStatus);
    }
    
    /**
     * 便捷的构造函数用于删除操作
     */
    public static FocusTaskChangeEvent deleteEvent(Object source, Long taskId, Long goalId,
                                                  Integer progressRate, Integer actualConsumedSec, String status) {
        return new FocusTaskChangeEvent(source, taskId, goalId, "DELETE",
                progressRate, null, actualConsumedSec, null, status, null);
    }
}
