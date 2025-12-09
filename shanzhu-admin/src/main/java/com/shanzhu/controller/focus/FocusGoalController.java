package com.shanzhu.controller.focus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.annotation.Log;
import com.shanzhu.entity.focus.FocusGoalDO;
import com.shanzhu.enums.LogTypeEnum;
import com.shanzhu.model.focus.dto.FocusGoalDTO;
import com.shanzhu.model.focus.vo.FocusGoalVO;
import com.shanzhu.model.web.ApiResponseModel;
import com.shanzhu.model.web.basecontroller.ApiResponseController;
import com.shanzhu.service.focus.FocusGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专注目标控制器
 */
@Tag(name = "专注目标管理")
@RestController
@RequestMapping("/focus/goal")
@Validated
public class FocusGoalController extends ApiResponseController {
    
    @Resource
    private FocusGoalService focusGoalService;
    
    /**
     * 分页查询专注目标
     */
    @Operation(summary = "分页查询专注目标")
    @PostMapping("/page")
    public ApiResponseModel<IPage<FocusGoalVO>> queryPage(@RequestBody FocusGoalDTO focusGoalDTO) {
        return success(focusGoalService.queryPage(focusGoalDTO));
    }
    
    /**
     * 查询专注目标列表
     */
    @Operation(summary = "查询专注目标列表")
    @PostMapping("/list")
    public ApiResponseModel<List<FocusGoalDO>> queryList(@RequestBody FocusGoalDO focusGoal) {
        return success(focusGoalService.queryList(focusGoal));
    }
    
    /**
     * 根据ID查询专注目标
     */
    @Operation(summary = "根据ID查询专注目标")
    @GetMapping("/{id}")
    public ApiResponseModel<FocusGoalDO> queryById(@PathVariable("id") Long id) {
        return success(focusGoalService.queryById(id));
    }
    
    /**
     * 保存专注目标
     */
    @Operation(summary = "保存专注目标")
    @PostMapping
    @Log(description = "保存专注目标", type = LogTypeEnum.SAVE)
    public ApiResponseModel<Boolean> save(@RequestBody @Validated FocusGoalDO focusGoal) {
        return success(focusGoalService.save(focusGoal));
    }
    
    /**
     * 删除专注目标
     */
    @Operation(summary = "删除专注目标")
    @DeleteMapping
    @Log(description = "删除专注目标", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择要删除的数据") List<Long> ids) {
        focusGoalService.deleteByIds(ids);
        return success("删除成功");
    }
}