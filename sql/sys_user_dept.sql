create table sys_user_dept
(
    user_id      bigint   not null comment '用户id',
    dept_id      bigint   not null comment '部门id',
    create_time  datetime null comment '绑定时间',
    create_id    bigint   null comment '绑定人id',
    default_dept char     null comment '默认单位',
    primary key (user_id, dept_id)
)
    comment '系统用户部门关联表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_user_dept (user_id, dept_id, create_time, create_id, default_dept) VALUES (1, 1842128590274756609, '2024-12-01 13:36:09', 1, '0');
