create table biz_focus_tag_rel
(
    entity_id   bigint                not null comment '实体ID (关联到 goal 或 task 的 ID)',
    tag_id      bigint                not null comment '标签ID (关联到 tag 表的 ID)',
    entity_type enum ('goal', 'task') not null comment '实体类型 (goal: 目标, task: 任务)',
    primary key (entity_id, tag_id, entity_type) comment '复合主键：实体ID + 标签ID + 实体类型'
)
    comment '标签与目标/任务的通用关联表';

create index idx_tag_entity_type
    on biz_focus_tag_rel (tag_id, entity_type)
    comment '标签ID和实体类型联合索引，便于按标签查找不同类型实体';

