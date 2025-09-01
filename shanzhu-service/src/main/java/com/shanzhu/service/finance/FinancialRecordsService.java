package com.shanzhu.service.finance;

import org.springframework.web.multipart.MultipartFile;

/**
 * 财务管理主 Service 接口
 *
 * @author 芋道源码
 */
public interface FinancialRecordsService {
    /**
     * 根据文件获取财务数据
     *
     * @param file 创建信息
     * @return 编号
     */
    Integer readFileToFinancialRecords(MultipartFile file);

    Integer deleteAll();
}