package com.shanzhu.model.finance.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shanzhu.model.BaseEntity;
import lombok.*;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialCategoryVO extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 分类图标（URL 或图标名称）
     */
    private String icon;

    /**
     * 分类颜色（如：#FF5722）
     */
    private String color;

    /**
     * 分类排序（数值越小越靠前）
     */
    private Integer sort;

    /**
     * 分类备注
     */
    private String remark;


    /**
     * 逻辑删除标志（0:未删除, 1:已删除）
     */
    private String delFlag;

    /**
     * 父亲分类code
     */
    private String parentCode;

    /**
     * 子级分类
     */
    private List<FinancialCategoryVO> children;


}
