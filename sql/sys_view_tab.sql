create table sys_view_tab
(
    user_id bigint not null comment '用户id',
    menu_id bigint not null comment '菜单id',
    affix   char   null comment '是否固定（1固定，0不固定）',
    star    char   null comment '是否收藏（1收藏，0不收藏）',
    primary key (user_id, menu_id)
)
    comment '用户菜单收藏管理表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_view_tab (user_id, menu_id, affix, star) VALUES (1, 1777536311941824513, '0', '1');
INSERT INTO shanzhu_restarts.sys_view_tab (user_id, menu_id, affix, star) VALUES (1, 1897209379781541890, '1', '1');
