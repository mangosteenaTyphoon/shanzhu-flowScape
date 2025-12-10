create table sys_post
(
    id           bigint       not null comment ' 主键'
        primary key,
    dept_id      varchar(60)  null comment '部门主键',
    dept_code    varchar(60)  null comment '部门编码',
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

INSERT INTO shanzhu_restarts.sys_post (id, dept_id, dept_code, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842129333329264642, '1810226204790657025', 'lihua', '研发岗', 'lihua_dev', 1, '0', '', null, null, null, '0', 1, '2024-10-04 17:07:13', 1, '2024-10-04 17:07:46', null);
INSERT INTO shanzhu_restarts.sys_post (id, dept_id, dept_code, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842129449343713282, '1810226204790657025', 'lihua', '销售岗', 'lihua_sell', 2, '0', null, null, null, null, '0', 1, '2024-10-04 17:07:40', null, null, null);
INSERT INTO shanzhu_restarts.sys_post (id, dept_id, dept_code, name, code, sort, status, manager, phone_number, email, fax, del_flag, create_id, create_time, update_id, update_time, remark) VALUES (1842188089392041986, '1810226204790657025', 'lihua', '测试岗', 'lihua_test', 3, '0', null, null, null, null, '0', 1, '2024-10-04 21:00:41', null, null, null);
