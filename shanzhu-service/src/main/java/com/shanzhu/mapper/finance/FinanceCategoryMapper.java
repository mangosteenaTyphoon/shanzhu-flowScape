package com.shanzhu.mapper.finance;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.entity.finance.FinancialCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 财务管理主 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FinanceCategoryMapper extends BaseMapper<FinancialCategoryDO> {

    int insertBatch(List<FinancialCategoryDO> financeCategoryDOs);

    int deleteAll();
}
