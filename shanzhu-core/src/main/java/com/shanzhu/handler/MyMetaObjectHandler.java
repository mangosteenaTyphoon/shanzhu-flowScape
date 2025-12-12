package com.shanzhu.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.shanzhu.utils.security.LoginUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 * 自动填充 BaseEntity 中的公共字段
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("==================== MyBatis-Plus 自动填充开始（INSERT） ====================");
        log.info("实体类: {}", metaObject.getOriginalObject().getClass().getName());

        // 获取当前登录用户ID
        String userId = LoginUserContext.getUserId();
        log.info("当前登录用户ID: {}", userId);

        if (userId == null || userId.isEmpty()) {
            log.warn("⚠️ 警告：当前登录用户ID为空，自动填充可能失败！");
        }

        // 自动填充创建人ID
        log.info("正在填充字段: createId = {}", userId);
        this.strictInsertFill(metaObject, "createId", String.class, userId);

        // 自动填充创建时间
        LocalDateTime now = LocalDateTime.now();
        log.info("正在填充字段: createTime = {}", now);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);

        // 自动填充更新人ID（插入时也需要设置）
        log.info("正在填充字段: updateId = {}", userId);
        this.strictInsertFill(metaObject, "updateId", String.class, userId);

        // 自动填充更新时间（插入时也需要设置）
        log.info("正在填充字段: updateTime = {}", now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);

        log.info("==================== MyBatis-Plus 自动填充完成（INSERT） ====================");
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("==================== MyBatis-Plus 自动填充开始（UPDATE） ====================");
        log.info("实体类: {}", metaObject.getOriginalObject().getClass().getName());

        // 获取当前登录用户ID
        String userId = LoginUserContext.getUserId();
        log.info("当前登录用户ID: {}", userId);

        if (userId == null || userId.isEmpty()) {
            log.warn("⚠️ 警告：当前登录用户ID为空，自动填充可能失败！");
        }

        // 自动填充更新人ID
        LocalDateTime now = LocalDateTime.now();
        log.info("正在填充字段: updateId = {}", userId);
        this.strictUpdateFill(metaObject, "updateId", String.class, userId);

        // 自动填充更新时间
        log.info("正在填充字段: updateTime = {}", now);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);

        log.info("==================== MyBatis-Plus 自动填充完成（UPDATE） ====================");
    }
}
