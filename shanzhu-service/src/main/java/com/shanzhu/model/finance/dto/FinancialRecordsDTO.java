package com.shanzhu.entity.finance;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.shanzhu.model.BaseEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 财务管理主 DO
 *
 * @author  山竹
 */
@TableName("biz_financial_records")
@KeySequence("biz_financial_records_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExcelModel(sheetName = "收支账单")
public class FinancialRecordsDTO extends BaseEntity {
    /**
     * 主鍵ID
     */
    @TableId
    private Integer id;
    /**
     * 交易时间
     */
    @ExcelColumn(title = "日期", index = 0, format = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime transactionDate;
    /**
     * 交易类型
     */
    @ExcelColumn(title = "类型", index = 1)
    private String transactionType;
    /**
     * 金额
     */
    @ExcelColumn(title = "金额", index = 2)
    private BigDecimal amount;
    /**
     * 一级分类
     */
    @ExcelColumn(title = "一级分类", index = 3)
    private String categoryOne;
    /**
     * 二级分类
     */
    @ExcelColumn(title = "二级分类", index = 4)
    private String categoryTwo;
    /**
     * 账户名称
     */
    @ExcelColumn(title = "账户1", index = 5)
    private String accountName;

    /**
     * 备注
     */
    @ExcelColumn(title = "备注", index = 7)
    private String remark;
    /**
     * 货币类型
     */
    @ExcelColumn(title = "货币", index = 8)
    private String currency;
    /**
     * 标签Id
     */
    @ExcelColumn(title = "标签", index = 9)
    private String tags;
    /**
     * 账本ID
     */
    @ExcelColumn(title = "账本", index = 10)
    private String ledgerBookName;
    /**
     * 位置
     */
    @ExcelColumn(title = "位置", index = 11)
    private String location;
    /**
     * 退款金额
     */
    @ExcelColumn(title = "退款", index = 12)
    private BigDecimal refund;
    /**
     * 图片附件数组
     */
    @ExcelColumn(title = "图片", index = 13)
    private String imageUrls;
}