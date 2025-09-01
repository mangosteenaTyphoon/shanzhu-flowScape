package com.shanzhu.entity.finance;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class FinancialRecordsDO extends BaseEntity {
    /**
     * 主鍵ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 交易时间
     */
    private LocalDateTime transactionDate;
    /**
     * 交易类型
     */ 
    private String transactionType;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 分类编码
     */
    private String categoryCode;
    /**
     * 账户ID
     */
    private String accountName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 货币类型
     */
    private String currency;
    /**
     * 标签Id
     */
    private int tagsId;
    /**
     * 账本名称
     */
    private String ledgerBookName;
    /**
     * 位置
     */
    private String location;
    /**
     * 退款金额
     */
    private BigDecimal refund;
    /**
     * 图片附件数组
     */
    private String imageUrls;
}