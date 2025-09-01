package com.shanzhu.mapper.finance;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.entity.finance.FinancialRecordsDO;
import com.shanzhu.model.finance.vo.FinancialCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * 财务管理主 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FinancialRecordsMapper extends BaseMapper<FinancialRecordsDO> {

    int insertBatch(@Param("list") ArrayList<FinancialRecordsDO> financialRecordsDOS);

    Integer deleteAll();

    List<FinancialCategoryVO> queryFinancialCategory();
}