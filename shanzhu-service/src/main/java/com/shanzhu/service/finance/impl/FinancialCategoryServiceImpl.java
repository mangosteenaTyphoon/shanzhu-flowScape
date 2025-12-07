package com.shanzhu.service.finance.impl;

import com.shanzhu.entity.finance.FinancialCategoryDO;
import com.shanzhu.entity.finance.FinancialRecordsDO;
import com.shanzhu.mapper.finance.FinanceCategoryMapper;
import com.shanzhu.mapper.finance.FinancialRecordsMapper;
import com.shanzhu.model.finance.vo.FinancialCategoryVO;
import com.shanzhu.service.finance.FinancialCategoryService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialCategoryServiceImpl implements FinancialCategoryService {

    @Resource
    private FinanceCategoryMapper financeCategoryMapper;


    /**
     * @return 查询财务分类信息
     */
    @Override
    public List<FinancialCategoryVO> queryFinancialCategory() {
      // 先查询出所有的分类 
        List<FinancialCategoryDO> financialCategoryDOS = financeCategoryMapper.selectList(null);
        // 再将分类信息封装到VO中
        List<FinancialCategoryVO> financialCategoryVOS = new ArrayList<FinancialCategoryVO>();
        ArrayList<FinancialCategoryVO> financialCategoryParentVOS = new ArrayList<>();
        ArrayList<FinancialCategoryVO> financialCategoryChildVOS = new ArrayList<>();
        for (FinancialCategoryDO financialCategoryDO : financialCategoryDOS) {
            if(financialCategoryDO.getParentCode().equals("0")){
                FinancialCategoryVO financialCategoryVO = new FinancialCategoryVO();
                BeanUtils.copyProperties(financialCategoryDO, financialCategoryVO);
                financialCategoryParentVOS.add(financialCategoryVO);
            }else{
                FinancialCategoryVO financialCategoryVO = new FinancialCategoryVO();
                BeanUtils.copyProperties(financialCategoryDO, financialCategoryVO);
                financialCategoryChildVOS.add(financialCategoryVO);
            }

        }
        for (FinancialCategoryVO financialCategoryParentVO : financialCategoryParentVOS) {
            ArrayList<FinancialCategoryVO> financialCategoryChildren = new ArrayList<>();
            for (FinancialCategoryVO financialCategoryChildVO : financialCategoryChildVOS) {
                if(financialCategoryParentVO.getCode().equals(financialCategoryChildVO.getParentCode())){
                    financialCategoryChildren.add(financialCategoryChildVO);
                }

            }
            financialCategoryParentVO.setChildren(financialCategoryChildren);
        }
        return financialCategoryParentVOS;

    }
}
