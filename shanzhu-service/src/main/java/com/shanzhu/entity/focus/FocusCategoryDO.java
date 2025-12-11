package com.shanzhu.entity.focus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shanzhu.model.BaseEntity;
import lombok.*;

/**
 * 专注分类实体类
 */
@TableName("biz_focus_category")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FocusCategoryDO extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 颜色值
     */
    private String color;

    /**
     * 类型 (goal, task, both)
     */
    private String type;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 逻辑删除
     */
    private String delFlag;
}
