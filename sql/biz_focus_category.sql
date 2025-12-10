create table biz_focus_category
(
    id      bigint auto_increment
        primary key,
    name    varchar(50)                                     not null,
    color   varchar(7)                    default '#3366ff' null,
    type    enum ('goal', 'task', 'both') default 'both'    not null,
    user_id bigint                                          not null
);

create index idx_user_id
    on biz_focus_category (user_id);

