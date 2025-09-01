package com.shanzhu.service.finance.impl;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.shanzhu.entity.finance.FinancialCategoryDO;
import com.shanzhu.entity.finance.FinancialRecordsDO;
import com.shanzhu.entity.finance.FinancialRecordsDTO;
import com.shanzhu.mapper.finance.FinanceCategoryMapper;
import com.shanzhu.mapper.finance.FinancialRecordsMapper;
import com.shanzhu.service.finance.FinancialRecordsService;
import com.shanzhu.utils.date.DateUtils;
import com.shanzhu.utils.excel.ExcelUtils;
import com.shanzhu.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("ALL")
@Service
public class FinancialRecordsServiceImpl implements FinancialRecordsService {

    private static final Logger log = LoggerFactory.getLogger(FinancialRecordsService.class);
    @Resource
    private FinanceCategoryMapper financeCategoryMapper;

    @Resource
    private FinancialRecordsMapper financialRecordsMapper;
    /**
     * 根据文件获取财务数据
     *
     * @param file 创建信息
     * @return 编号
     */
    @Transactional
    @Override
    public Integer readFileToFinancialRecords(MultipartFile file) {
        this.deleteAll();
        List<FinancialRecordsDTO> financialRecordsDTOS = ExcelUtils.importExport(file, FinancialRecordsDTO.class, 0);
        ArrayList<FinancialRecordsDO> financialRecordsDOS = new ArrayList<>();
        // 获取所有的一二级分类
        List<FinancialCategoryDO> financeCategoryOneDO = new ArrayList<>();
        List<FinancialCategoryDO> financeCategoryTwoDO = new ArrayList<>();
         //根据导入数据新增分类
        List<FinancialCategoryDO> financialCategoryDOS = new ArrayList<>();
        for (FinancialRecordsDTO financialRecordsDTO : financialRecordsDTOS) {
            String oneCategoryCode = "";
            String twoCategoryCode = "";
            if (financialRecordsDTO.getCategoryOne() != null){
                //判断是否有一级分类
                oneCategoryCode = UUID.randomUUID().toString().replace("-","");
                FinancialCategoryDO financeCategoryOne = getFinanceCategoryDO(financialRecordsDTO, "0",oneCategoryCode);
                if(financialCategoryDOS.stream().noneMatch(item -> Objects.equals(item.getName(), financeCategoryOne.getName()))){
                    financialCategoryDOS.add(financeCategoryOne);
                }else{
                    FinancialCategoryDO oneCategory = financialCategoryDOS.stream()
                            .filter(item -> Objects.equals(item.getName(), financeCategoryOne)).findFirst().orElse(null);
                    if(oneCategory != null){
                        oneCategoryCode = oneCategory.getCode();
                    }
                    //打印出名字重复的日志
                    log.info("重复的一级分类为:{}",financeCategoryOne.getName());
                }
                // 赋值一级分类编码属性
                financialRecordsDTO.setCategoryOne(oneCategoryCode);
            }
            if (financialRecordsDTO.getCategoryTwo() != null){
                //首先该二级分类是否存在 如果存在 则跳过 否则添加
                twoCategoryCode = UUID.randomUUID().toString().replace("-","");
                FinancialCategoryDO financeCategoryTwo = getFinanceCategoryDO(financialRecordsDTO, oneCategoryCode,twoCategoryCode);
                if(financialCategoryDOS.stream().noneMatch(item -> Objects.equals(item.getName(), financeCategoryTwo.getName()))){
                    financialCategoryDOS.add(financeCategoryTwo);
                }else{
                    FinancialCategoryDO twoCategory = financialCategoryDOS.stream()
                            .filter(item -> Objects.equals(item.getName(), financeCategoryTwo)).findFirst().orElse(null);
                    if(twoCategory != null){
                        twoCategoryCode = twoCategory.getCode();
                    }
                    //打印出名字重复的日志
                    log.info("重复的二级分类为:{}",financeCategoryTwo.getName());
                }
                // 赋值二级分类编码属性
                financialRecordsDTO.setCategoryTwo(twoCategoryCode);
            }
            FinancialRecordsDO financialRecordsDO = new FinancialRecordsDO();
            BeanUtils.copyProperties(financialRecordsDTO, financialRecordsDO);
            financialRecordsDO.setCategoryCode(twoCategoryCode);
            financialRecordsDO.setCreateId(LoginUserContext.getUserId());
            financialRecordsDO.setTransactionDate(LocalDateTime.now());
            financialRecordsDO.setUpdateId(LoginUserContext.getUserId());
            financialRecordsDO.setUpdateTime(LocalDateTime.now());
            if(financialRecordsDO.getId() == null){
                financialRecordsDO.setId(IdWorker.getIdStr());
            }
            financialRecordsDOS.add(financialRecordsDO);
        }
        // 将其增加到数据库中
        if(financialCategoryDOS.size() > 0){
            int flag = financeCategoryMapper.insertBatch(financialCategoryDOS);
        }
        if(financialRecordsDOS.size() > 0){
            int flag = financialRecordsMapper.insertBatch(financialRecordsDOS);
        }

        return 1;
    }


    // 清空所有分类和财务数据
    @Transactional
    @Override
    public Integer deleteAll() {
        return financeCategoryMapper.deleteAll() + financialRecordsMapper.deleteAll();
    }




    @NotNull
    private static FinancialCategoryDO getFinanceCategoryDO(FinancialRecordsDTO sysUserVO, String parentCode, String code) {
        LocalDateTime now = DateUtils.now();
        FinancialCategoryDO financeCategoryOneDO = new FinancialCategoryDO();
        financeCategoryOneDO.setId(IdWorker.getIdStr());
        if(parentCode == "0"){
            financeCategoryOneDO.setName(sysUserVO.getCategoryOne());
        }else{
            financeCategoryOneDO.setName(sysUserVO.getCategoryTwo());
        }
        financeCategoryOneDO.setCreateId(LoginUserContext.getUserId());
        financeCategoryOneDO.setUpdateId(LoginUserContext.getUserId());
        financeCategoryOneDO.setDelFlag("0");
        financeCategoryOneDO.setCode(code);
        financeCategoryOneDO.setParentCode(parentCode);
        financeCategoryOneDO.setRemark("自动导入分类");
        financeCategoryOneDO.setCreateTime(now);
        financeCategoryOneDO.setUpdateTime(now);
        return financeCategoryOneDO;
    }
}
