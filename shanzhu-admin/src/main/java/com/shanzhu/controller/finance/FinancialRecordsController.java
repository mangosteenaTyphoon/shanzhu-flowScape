package com.shanzhu.controller.finance;


import com.shanzhu.model.web.ApiResponseModel;
import com.shanzhu.model.web.basecontroller.ApiResponseController;
import com.shanzhu.service.finance.FinancialRecordsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "财务记录管理")
@RestController
@RequestMapping("financial/records")
@Validated
public class FinancialRecordsController extends ApiResponseController {

    @Resource
    private FinancialRecordsService financialRecordsService;

    @PostMapping("/readFileToFinancialRecords")
    @Operation(summary = "通过文件导入财务数据")
    public ApiResponseModel<Integer> readFileToFinancialRecords(@RequestParam MultipartFile file) {
        return success(financialRecordsService.readFileToFinancialRecords(file));
    }

    //删除所有财务数据包括逻辑删除
    @PostMapping("/deleteAll")
    @Operation(summary = "删除所有财务数据")
    public ApiResponseModel<Integer> deleteAll() {
        return success(financialRecordsService.deleteAll());
    }
}