create table sys_dept
(
    id           bigint       not null comment ' 主键'
        primary key,
    parent_id    bigint       null comment '父级id',
    name         varchar(60)  null comment '名称',
    code         varchar(100) null comment '编码',
    sort         int          null comment '排序',
    status       char         null comment '状态',
    manager      varchar(30)  null comment '负责人',
    phone_number varchar(20)  null comment '联系电话',
    email        varchar(60)  null comment '邮箱',
    fax          varchar(60)  null comment '传真',
    del_flag     char         null comment '逻辑删除标志',
    create_id    bigint       null comment '创建人id',
    create_time  datetime     null comment '创建时间',
    update_id    bigint       null comment '最近一次更新人id',
    update_time  datetime     null comment '最近一次更新时间',
    remark       varchar(500) null comment '备注'
)
    comment '系统单位/岗位表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_dept (id, parent_id, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1810226204790657025, 0, '狸花猫科技', 'lihua', 1, '0', '', null, null, null, '0', 1, '2024-07-08 16:15:34', 1, '2024-10-04 17:02:16', null);
INSERT INTO shanzhu_restarts.sys_dept (id, parent_id, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842128409600917505, 1810226204790657025, '猫粮生产中心', 'lihua_food', 1, '0', '小黑子', null, null, null, '0', 1, '2024-10-04 17:03:33', 1, '2024-10-04 17:05:47', null);
INSERT INTO shanzhu_restarts.sys_dept (id, parent_id, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842128590274756609, 1810226204790657025, '冻干生产中心', 'lihua_freeze_dried', 2, '0', '小黑子', null, null, null, '0', 1, '2024-10-04 17:04:16', 1, '2024-10-04 17:05:55', null);
INSERT INTO shanzhu_restarts.sys_dept (id, parent_id, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842128734768529409, 1810226204790657025, '逗猫棒生产中心', 'lihua_teaser', 3, '0', '小喵子', null, null, null, '0', 1, '2024-10-04 17:04:50', 1, '2024-10-04 17:06:01', null);
INSERT INTO shanzhu_restarts.sys_dept (id, parent_id, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842128938393600002, 1810226204790657025, '猫窝生产中心', 'lihua_cattery', 4, '0', '小喵子', null, null, null, '0', 1, '2024-10-04 17:05:39', 1, '2024-10-04 17:06:10', null);
