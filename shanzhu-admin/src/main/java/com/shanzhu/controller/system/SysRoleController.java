package com.shanzhu.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.annotation.Log;
import com.shanzhu.enums.LogTypeEnum;
import com.shanzhu.model.security.CurrentRole;
import com.shanzhu.model.validation.MaxPageSizeLimit;
import com.shanzhu.model.web.ApiResponseModel;
import com.shanzhu.model.web.basecontroller.ApiResponseController;
import com.shanzhu.entity.system.SysRole;
import com.shanzhu.model.system.dto.SysRoleDTO;
import com.shanzhu.service.system.menu.SysMenuService;
import com.shanzhu.service.system.role.SysRoleService;
import com.shanzhu.utils.security.LoginUserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("system/role")
@Validated
public class SysRoleController extends ApiResponseController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysRole>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysRoleDTO sysRoleDTO) {
        return success(sysRoleService.queryPage(sysRoleDTO));
    }

    @Operation(summary = "根据主键查询")
    @GetMapping("{id}")
    public ApiResponseModel<SysRole> queryById(@PathVariable("id") String id) {
        return success(sysRoleService.queryById(id));
    }

    @Operation(summary = "保存角色信息")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存角色信息", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysRole sysRole) {
        return success(sysRoleService.save(sysRole));
    }

    @Operation(summary = "更新角色状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新角色状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysRoleService.updateStatus(id, currentStatus));
    }

    @Operation(summary = "删除角色数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除角色数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysRoleService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "获取当前用户下的角色列表")
    @GetMapping("option")
    public ApiResponseModel<List<CurrentRole>> getRoleOption() {
        return success(LoginUserContext.getRoleList().stream().filter(role -> !"ROLE_admin".equals(role.getCode())).toList());
    }
}
