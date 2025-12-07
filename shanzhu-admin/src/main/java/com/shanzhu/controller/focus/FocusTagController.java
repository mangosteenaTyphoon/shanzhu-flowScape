package com.shanzhu.controller.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.annotation.Log;
import com.shanzhu.entity.focus.FocusTagDO;
import com.shanzhu.enums.LogTypeEnum;
import com.shanzhu.model.focus.dto.FocusTagDTO;
import com.shanzhu.model.web.ApiResponseModel;
import com.shanzhu.model.web.basecontroller.ApiResponseController;
import com.shanzhu.service.focus.FocusTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专注标签控制器
 */
@Tag(name = "专注标签管理")
@RestController
@RequestMapping("/focus/tag")
@Validated
public class FocusTagController extends ApiResponseController {
    
    @Resource
    private FocusTagService focusTagService;
    
    /**
     * 分页查询专注标签
     */
    @Operation(summary = "分页查询专注标签")
    @PostMapping("/page")
    public ApiResponseModel<IPage<FocusTagDO>> queryPage(@RequestBody FocusTagDTO focusTagDTO) {
        return success(focusTagService.queryPage(focusTagDTO));
    }
    
    /**
     * 查询专注标签列表
     */
    @Operation(summary = "查询专注标签列表")
    @PostMapping("/list")
    public ApiResponseModel<List<FocusTagDO>> queryList(@RequestBody FocusTagDO focusTag) {
        return success(focusTagService.queryList(focusTag));
    }
    
    /**
     * 根据ID查询专注标签
     */
    @Operation(summary = "根据ID查询专注标签")
    @GetMapping("/{id}")
    public ApiResponseModel<FocusTagDO> queryById(@PathVariable("id") Long id) {
        return success(focusTagService.queryById(id));
    }
    
    /**
     * 保存专注标签
     */
    @Operation(summary = "保存专注标签")
    @PostMapping
    @Log(description = "保存专注标签", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated FocusTagDO focusTag) {
        return success(focusTagService.save(focusTag) ? "成功" : "失败");
    }
    
    /**
     * 删除专注标签
     */
    @Operation(summary = "删除专注标签")
    @DeleteMapping
    @Log(description = "删除专注标签", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择要删除的数据") List<Long> ids) {
        focusTagService.deleteByIds(ids);
        return success("删除成功");
    }
}