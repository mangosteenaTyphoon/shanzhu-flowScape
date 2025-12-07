package com.shanzhu.controller.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.annotation.Log;
import com.shanzhu.entity.focus.FocusTaskDO;
import com.shanzhu.enums.LogTypeEnum;
import com.shanzhu.model.focus.dto.FocusTaskDTO;
import com.shanzhu.model.web.ApiResponseModel;
import com.shanzhu.model.web.basecontroller.ApiResponseController;
import com.shanzhu.service.focus.FocusTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专注任务控制器
 */
@Tag(name = "专注任务管理")
@RestController
@RequestMapping("/focus/task")
@Validated
public class FocusTaskController extends ApiResponseController {
    
    @Resource
    private FocusTaskService focusTaskService;
    
    /**
     * 分页查询专注任务
     */
    @Operation(summary = "分页查询专注任务")
    @PostMapping("/page")
    public ApiResponseModel<IPage<FocusTaskDO>> queryPage(@RequestBody FocusTaskDTO focusTaskDTO) {
        return success(focusTaskService.queryPage(focusTaskDTO));
    }
    
    /**
     * 查询专注任务列表
     */
    @Operation(summary = "查询专注任务列表")
    @PostMapping("/list")
    public ApiResponseModel<List<FocusTaskDO>> queryList(@RequestBody FocusTaskDO focusTask) {
        return success(focusTaskService.queryList(focusTask));
    }
    
    /**
     * 根据ID查询专注任务
     */
    @Operation(summary = "根据ID查询专注任务")
    @GetMapping("/{id}")
    public ApiResponseModel<FocusTaskDO> queryById(@PathVariable("id") Long id) {
        return success(focusTaskService.queryById(id));
    }
    
    /**
     * 保存专注任务
     */
    @Operation(summary = "保存专注任务")
    @PostMapping
    @Log(description = "保存专注任务", type = LogTypeEnum.SAVE)
    public ApiResponseModel<Boolean> save(@RequestBody @Validated FocusTaskDO focusTask) {
        return success(focusTaskService.save(focusTask));
    }
    
    /**
     * 删除专注任务
     */
    @Operation(summary = "删除专注任务")
    @DeleteMapping
    @Log(description = "删除专注任务", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择要删除的数据") List<Long> ids) {
        focusTaskService.deleteByIds(ids);
        return success("删除成功");
    }
}