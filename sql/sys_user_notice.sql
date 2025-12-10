create table sys_user_notice
(
    user_id   bigint   not null comment '用户id',
    notice_id bigint   not null comment '公告id',
    star_flag char     null comment 'star标记',
    read_flag char     null comment '已读标记',
    read_time datetime null comment '已读时间',
    primary key (user_id, notice_id)
)
    comment '用户通知关联表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_user_notice (user_id, notice_id, star_flag, read_flag, read_time) VALUES (1, 1, '1', '1', '2024-12-19 22:01:03');
