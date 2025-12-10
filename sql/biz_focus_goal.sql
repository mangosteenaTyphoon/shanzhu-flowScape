create table biz_focus_goal
(
    id                          bigint auto_increment comment '主键ID'
        primary key,
    user_id                     bigint                               not null comment '用户ID',
    title                       varchar(255)                         not null comment '目标标题',
    description                 text                                 null comment '目标描述',
    category_id                 bigint                               null comment '分类ID (逻辑关联，非外键约束)',
    start_date                  date                                 not null comment '开始日期',
    end_date                    date                                 not null comment '结束日期',
    status                      char(20)   default 'active'          not null comment '目标状态 (draft: 草稿, active: 进行中, completed: 已完成, archived: 已归档)',
    final_progress              int        default 0                 not null comment '最终进度百分比 (0-100)',
    completion_status           char(30)                             null comment '完成状态 (early: 提前完成, on_time: 按时完成, delayed_completed: 延期完成, delayed_incomplete: 延期未完成)',
    has_delayed_tasks           tinyint(1) default 0                 not null comment '是否存在延期任务',
    expected_duration_sec       int                                  null comment '预期持续时间（秒）',
    actual_duration_sec         int                                  null comment '实际持续时间（秒）',
    overdue_completion_time_sec int                                  null comment '逾期完成时间（秒）',
    created_at                  datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at                  datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '专注目标表';

create index idx_completion_status
    on biz_focus_goal (completion_status)
    comment '完成状态索引';

create index idx_user_end_date
    on biz_focus_goal (user_id, end_date)
    comment '用户ID和结束日期联合索引';

create index idx_user_status
    on biz_focus_goal (user_id, status)
    comment '用户ID和状态联合索引';

