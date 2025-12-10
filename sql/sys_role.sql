create table sys_role
(
    id          bigint       not null comment '主键'
        primary key,
    name        varchar(60)  null comment '角色名称',
    code        varchar(100) null comment '角色编码',
    status      char         null comment '角色状态',
    del_flag    char         null comment '逻辑删除标识',
    create_id   bigint       null comment '创建人id',
    create_time datetime     null comment '创建时间',
    update_id   bigint       null comment '最近一次更新人id',
    update_time datetime     null comment '最近一次更新时间',
    remark      varchar(500) null comment '备注'
)
    comment '系统角色表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_role (id, name, code, status, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1, '超级管理员', 'ROLE_admin', '0', '0', null, '2024-05-16 21:32:55', 1, '2024-06-19 22:49:48', null);
INSERT INTO shanzhu_restarts.sys_role (id, name, code, status, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842149851067514881, '访客用户', 'ROLE_visitor', '0', '0', 1, '2024-10-04 18:28:45', 1, '2025-03-05 16:58:52', '');
INSERT INTO shanzhu_restarts.sys_role (id, name, code, status, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1861768418410799106, '角色测试', 'ROLE_TEST', '0', '0', 1, '2024-11-27 21:45:56', 1, '2025-03-05 16:59:01', null);
