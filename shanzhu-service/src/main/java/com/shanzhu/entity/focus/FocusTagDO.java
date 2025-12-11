package com.shanzhu.entity.focus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shanzhu.model.BaseEntity;
import lombok.*;

/**
 * 专注标签实体类
 */
@TableName("biz_focus_tag")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FocusTagDO extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 颜色值
     */
    private String color;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 逻辑删除
     */
    private String delFlag;
}