create table sys_user_post
(
    user_id     bigint   not null comment '用户id',
    post_id     bigint   not null comment '岗位id',
    create_time datetime null comment '绑定时间',
    create_id   bigint   null comment '绑定人id',
    primary key (user_id, post_id)
)
    comment '系统用户岗位关联表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_user_post (user_id, post_id, create_time, create_id) VALUES (1, 1, '2024-06-02 16:57:25', 1);
INSERT INTO shanzhu_restarts.sys_user_post (user_id, post_id, create_time, create_id) VALUES (1, 3, '2024-06-02 16:57:25', 1);
