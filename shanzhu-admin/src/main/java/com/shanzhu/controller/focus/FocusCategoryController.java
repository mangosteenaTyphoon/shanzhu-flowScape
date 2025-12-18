package com.shanzhu.controller.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.annotation.Log;
import com.shanzhu.entity.focus.FocusCategoryDO;
import com.shanzhu.enums.LogTypeEnum;
import com.shanzhu.model.finance.dto.FocusCategoryDTO;
import com.shanzhu.model.web.ApiResponseModel;
import com.shanzhu.model.web.basecontroller.ApiResponseController;
import com.shanzhu.service.focus.FocusCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专注分类控制器
 */
@Tag(name = "专注分类管理")
@RestController
@RequestMapping("/focus/category")
@Validated
public class FocusCategoryController extends ApiResponseController {
    
    @Resource
    private FocusCategoryService focusCategoryService;
    
    /**
     * 分页查询专注分类
     */
    @Operation(summary = "分页查询专注分类")
    @PostMapping("/page")
    public ApiResponseModel<IPage<FocusCategoryDO>> queryPage(@RequestBody FocusCategoryDTO focusCategoryDTO) {
        return success(focusCategoryService.queryPage(focusCategoryDTO));
    }
    
    /**
     * 根据ID查询专注分类
     */
    @Operation(summary = "根据ID查询专注分类")
    @GetMapping("/{id}")
    public ApiResponseModel<FocusCategoryDO> queryById(@PathVariable("id") Long id) {
        return success(focusCategoryService.queryById(id));
    }
    
    /**
     * 保存专注分类
     */
    @Operation(summary = "保存专注分类")
    @PostMapping
    @Log(description = "保存专注分类", type = LogTypeEnum.SAVE)
    public ApiResponseModel<Boolean> save(@RequestBody @Validated FocusCategoryDO focusCategory) {
        return success(focusCategoryService.save(focusCategory));
    }
    
    /**
     * 删除专注分类
     */
    @Operation(summary = "删除专注分类")
    @DeleteMapping
    @Log(description = "删除专注分类", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择要删除的数据") List<Long> ids) {
        focusCategoryService.deleteByIds(ids);
        return success("删除成功");
    }
}
