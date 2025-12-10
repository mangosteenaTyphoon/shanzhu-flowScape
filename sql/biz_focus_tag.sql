create table biz_focus_tag
(
    id      bigint auto_increment
        primary key,
    name    varchar(50)                  not null,
    color   varchar(7) default '#999999' null,
    user_id bigint                       not null
);

create index idx_user_id
    on biz_focus_tag (user_id);

