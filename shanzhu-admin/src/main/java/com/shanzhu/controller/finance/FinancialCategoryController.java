package com.shanzhu.controller.finance;


import com.shanzhu.model.finance.vo.FinancialCategoryVO;
import com.shanzhu.model.web.ApiResponseModel;
import com.shanzhu.model.web.basecontroller.ApiResponseController;
import com.shanzhu.service.finance.FinancialCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "财务分类管理")
@RequestMapping("/financial/category")
@RestController
@Validated
public class FinancialCategoryController extends ApiResponseController {

    @Resource
    private FinancialCategoryService financialCategoryService;

    //财务分类查询
    @GetMapping("/queryFinancialCategory")
    public ApiResponseModel<List<FinancialCategoryVO>> queryFinancialCategory(){
        return success(financialCategoryService.queryFinancialCategory());

    }


}
