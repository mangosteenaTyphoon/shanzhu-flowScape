create table sys_user
(
    id                   bigint        not null comment '主键'
        primary key,
    username             varchar(30)   not null comment '用户名',
    password             varchar(100)  not null comment '密码',
    nickname             varchar(20)   null comment '昵称',
    avatar               varchar(500)  null comment '头像',
    gender               char          null comment '性别',
    status               char          null comment '用户状态',
    theme                varchar(4000) null comment '用户系统主题json',
    del_flag             char          null comment '逻辑删除标志',
    create_id            bigint        null comment '创建人id',
    create_time          datetime      null comment '创建时间',
    update_id            bigint        null comment '更新人id',
    update_time          datetime      null comment '更新时间',
    password_update_time datetime      null comment '密码更新时间',
    email                varchar(100)  null comment '邮箱',
    phone_number         varchar(20)   null comment '手机号码',
    remark               varchar(500)  null comment '备注',
    register_type        char          null comment '注册类型 0 管理员新增，1 用户自助注册'
)
    comment '系统用户表' row_format = DYNAMIC;

create index idx_sys_user_email
    on sys_user (email);

create index idx_sys_user_phone_number
    on sys_user (phone_number);

create index idx_sys_user_username
    on sys_user (username);

INSERT INTO shanzhu_restarts.sys_user (id, username, password, nickname, avatar, gender, status, theme, del_flag, create_id, create_time, update_id, update_time, password_update_time, email, phone_number, remark, register_type) VALUES (1, 'admin', '$2a$10$7uob8wEDL7VvXmC9wC4MZ.7Ut3qJOaiFVErYgpsbhAWxSA8jZWL9u', 'admin', '{"value":"XiaoMiaoCool","type":"icon","backgroundColor":"conic-gradient(from 45deg, rgb(22, 119, 255),rgb(245, 34, 45),rgb(250, 84, 28),rgb(250, 173, 20),rgb(19, 194, 194),rgb(82, 196, 26),rgb(47, 84, 235),rgb(114, 46, 209))"}', '1', '0', '{"layoutType":"sider-header","componentSize":"default","showViewTabs":true,"isDarkTheme":false,"followSystemTheme":true,"colorPrimary":"rgb(22, 119, 255)","antColorPrimary":"#1677ff","siderTheme":"light","groundGlass":true,"affixHead":true,"layoutBackgroundColor":"rgba(255,255,255,0.6)","siderBackgroundColor":"rgba(255,255,255,1)","siderMode":"inline","siderGroup":false,"siderWith":220,"originSiderWith":220,"routeTransition":"fade","grayModel":"none","themeConfig":{"token":{"colorPrimary":"rgb(22, 119, 255)"}},"isServerLoad":true}', '0', null, '2024-06-02 16:57:25', 1, '2025-08-20 13:45:28', '2024-10-04 20:59:29', '2651518588@qq.com', '15510916240', null, '0');
INSERT INTO shanzhu_restarts.sys_user (id, username, password, nickname, avatar, gender, status, theme, del_flag, create_id, create_time, update_id, update_time, password_update_time, email, phone_number, remark, register_type) VALUES (1958725250623516673, '123', '$2a$10$gQsLhjqDxMOv3ByzaBHpqOyQrrUKFIm4BOYWoQK3fftF7GCYE/qWq', '山竹', null, '1', '0', null, '0', 1, '2025-08-22 10:57:46', null, null, '2025-08-22 10:57:46', null, null, null, '0');
