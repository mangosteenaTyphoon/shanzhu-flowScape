create table sys_dict_type
(
    id          bigint       not null comment '主键id'
        primary key,
    name        varchar(30)  null comment '字典类型名称',
    code        varchar(30)  null comment '字典类型编码',
    type        char         null comment '字典类型',
    remark      varchar(200) null comment '字典备注',
    create_id   bigint       null comment '创建人id',
    create_time datetime     null comment '创建时间',
    update_id   bigint       null comment '最后一次更新人id',
    update_time datetime     null comment '最后一次更新时间',
    del_flag    char         null comment '逻辑删除标识',
    status      char         null comment '字典状态'
)
    comment '字典类型表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1771163166122561537, '系统状态', 'sys_status', '0', '系统通用状态标识', 1, '2024-03-22 21:13:00', 1, '2024-07-19 15:27:12', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1771164641267666946, '字典标签样式', 'sys_dict_tag_style', '0', '字典配置配置中，样式列的字典', 1, '2024-03-22 21:18:52', 1, '2024-03-22 21:20:00', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1771165529122131969, '字典类型', 'sys_dict_type', '0', '区分字典为一般字典还是树型字典', 1, '2024-03-22 21:22:23', null, null, '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1773700957867982850, '菜单类型', 'sys_menu_type', '0', '系统菜单配置类型，分为 目录、页面、链接、权限', 1, '2024-03-29 21:17:17', 1, '2024-04-01 09:59:35', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1774249964684034050, '链接菜单打开方式', 'sys_link_menu_open_type', '0', '链接菜单打开方式，分为系统内嵌套和浏览器新页面', 1, '2024-03-31 09:38:50', 1, '2024-03-31 09:41:45', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1774252683993923586, '系统是否', 'sys_whether', '0', '系统是否选项字典', 1, '2024-03-31 09:49:39', null, null, '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1780864852875984898, '部门类型', 'sys_dept_type', '0', '保存部门操作时的类型选项，分为部门和岗位', 1, '2024-04-18 15:44:02', 1, '2024-04-20 21:35:36', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1794262937292853250, '用户性别', 'user_gender', '0', '系统用户性别字典', 1, '2024-05-25 15:03:15', 1, '2024-05-25 15:03:26', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1814191109734584322, '公告状态', 'sys_notice_status', '0', '系统公告状态字典', 1, '2024-07-19 14:50:41', null, null, '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1814191516905033729, '公告类型', 'sys_notice_type', '0', '系统公告类型字典', 1, '2024-07-19 14:52:18', 1, '2024-07-19 14:52:32', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1814602561218945026, '用户范围', 'sys_notice_user_scope', '0', '通知公告接收消息的用户范围', 1, '2024-07-20 18:05:39', null, null, '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1814603011422953473, '优先级别', 'sys_notice_priority', '0', '通知公告优先程度', 1, '2024-07-20 18:07:26', 1, '2024-07-29 22:13:21', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1823522921661214721, '日志执行结果', 'sys_log_status', '0', '日志记录程序执行是否成功', 1, '2024-08-14 08:51:59', 1, '2024-09-23 09:05:30', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1840380676900876290, '用户注册类型', 'sys_user_register_type', '0', '用户注册类型：管理员注册、用户自助注册、批量导入注册', 1, '2024-09-29 21:18:41', 1, '2025-01-17 16:12:24', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1869716543582638081, '树形字典测试', 'test_tree', '1', '测试用，可删除', 1, '2024-12-19 20:08:56', 1, '2025-02-21 19:19:57', '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1892895886177673218, '附件状态', 'sys_attachment_status', '0', '系统附件上传状态', 1, '2025-02-21 19:15:22', null, null, '0', '0');
INSERT INTO shanzhu_restarts.sys_dict_type (id, name, code, type, remark, create_id, create_time, update_id, update_time, del_flag, status) VALUES (1892896049893941249, '上传方式', 'sys_attachment_upload_mode', '0', '系统附件上传方式', 1, '2025-02-21 19:16:01', null, null, '0', '0');
