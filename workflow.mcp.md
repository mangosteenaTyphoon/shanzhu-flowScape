# 山竹项目开发工作流程规范

## 概述

本文档定义了山竹项目中前后端代码开发的标准工作流程，确保团队成员能够按照统一规范高效协作。

## 工作流程

### 总体流程

1. 后端代码生成与创建
2. 前端代码生成与创建

### 详细步骤

#### 第一阶段：后端代码开发

1. **接收SQL定义**
   - 用户提供数据库表的SQL定义语句
   
2. **分析与生成代码**
   - 根据SQL结构分析表字段和约束
   - 生成完整的后端MVC代码组件：
     - Entity实体类 (DO)
     - Mapper接口
     - Service接口及实现类
     - Controller控制器
     - DTO数据传输对象
   
3. **代码展示与确认**
   - 向用户展示生成的代码
   - 等待用户确认，期间可根据反馈进行调整
   
4. **文件创建**
   - 用户确认无误后，创建后端代码文件
   - 严格按照项目包结构规范：
     - Entity类位于 `com.shanzhu.entity.[module]` 包下
     - Mapper接口位于 `com.shanzhu.mapper.[module]` 包下
     - Service接口位于 `com.shanzhu.service.[module]` 包下
     - Service实现类位于 `com.shanzhu.service.[module].impl` 包下
     - Controller类位于 `com.shanzhu.controller.[module]` 包下
     - DTO类位于 `com.shanzhu.model.[module].dto` 包下
   - 其中 `[module]` 为具体模块名称，如 `focus`、`finance` 等，由用户提供

#### 第二阶段：前端代码开发

1. **接收指令**
   - 用户发出生成前端代码的指令
   
2. **分析与生成代码**
   - 基于已有的后端接口生成前端代码
   - 生成完整的前端组件：
     - API接口文件
     - 类型定义文件
     - 页面组件文件
     - 路由配置文件（可选）
   
3. **代码展示与确认**
   - 向用户展示生成的前端代码
   - 等待用户确认，期间可根据反馈进行调整
   
4. **文件创建**
   - 用户确认无误后，创建前端代码文件
   - 严格按照项目目录结构规范：
     - API文件位于 `src/api/[module]/[submodule]` 目录下
     - 页面组件位于 `src/views/[module]/[submodule]` 目录下
     - 路由配置位于 `src/router/modules` 目录下（如果需要）
   - 其中 `[module]` 和 `[submodule]` 为具体模块和子模块名称，由用户提供
   - 注意：系统支持通过后端动态管理菜单，因此可能不需要手动添加路由配置

## 代码规范

### 后端规范

1. **包名规范**
   - 所有功能相关类必须位于对应模块包下
   - 包名必须准确，不能有拼写错误

2. **目录结构**
   - 代码需按照功能模块放置在对应的子目录下

3. **返回值类型**
   - Service层 save 方法返回值类型为 Boolean

### 前端规范

1. **页面样式**
   - 参考系统用户管理页面 (`system/user/SystemUser.vue`) 的样式和布局
   - 使用统一的搜索区域、表格展示、模态框等组件
   - 遵循 Ant Design Vue 组件规范

2. **文件结构**
   - API接口文件按模块组织
   - 页面组件按功能模块组织
   - 路由配置统一管理

3. **交互规范**
   - 表格支持分页、筛选、排序
   - 表单具有完整的验证机制
   - 操作按钮具有加载状态提示
   - 统一的错误处理和消息提示

## 注意事项

1. **严格遵循两步流程**
   - 必须先展示代码，获得用户确认后再创建文件
   - 不得跳过确认环节直接创建文件

2. **版本管理**
   - 文件创建完成后提醒在IDEA中进行版本管理

3. **错误处理**
   - 对于导入路径、组件引用等常见错误要及时修复
   - 保持与项目现有代码风格的一致性

4. **路由注册**
   - 新增页面需要在主路由文件中正确注册（除非系统支持动态菜单管理）

## 示例

### SQL输入示例
```sql
CREATE TABLE biz_x_table (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  title VARCHAR(255) NOT NULL COMMENT '标题'
) COMMENT 'X模块示例表';
```

其中用户指定了这是"x"模块下的SQL文件

### 后端代码生成示例
生成 XTableDO.java、XTableMapper.java、XTableService.java 等相关文件，放置在对应的 x 模块包下

### 前端代码生成示例
生成 index.vue 页面组件、API 接口文件、路由配置文件等，放置在对应的 x 模块目录下

## 附录

### 相关文件路径参考
- 后端实体类模板: `/src/main/java/com/shanzhu/entity/[module]/XTableDO.java`
- 后端Mapper模板: `/src/main/java/com/shanzhu/mapper/[module]/XTableMapper.java`
- 后端Service模板: `/src/main/java/com/shanzhu/service/[module]/XTableService.java`
- 后端Controller模板: `/src/main/java/com/shanzhu/controller/[module]/XTableController.java`
- 前端页面模板: `/src/views/[module]/[submodule]/index.vue`
- 前端API模板: `/src/api/[module]/[submodule]/index.ts`
- 路由配置模板: `/src/router/modules/[module][Submodule].ts`（可选）