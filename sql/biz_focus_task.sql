create table biz_focus_task
(
    id                    bigint auto_increment comment '主键ID'
        primary key,
    user_id               bigint                             not null comment '用户ID',
    goal_id               bigint                             not null comment '所属目标ID',
    title                 varchar(255)                       not null comment '任务标题',
    plan_start_date       date                               not null comment '计划开始日期',
    plan_end_date         date                               not null comment '计划结束日期',
    actual_start_date     date                               null comment '实际开始日期',
    actual_end_date       date                               null comment '实际结束日期',
    progress_rate         int      default 0                 not null comment '进度百分比 (0-100)',
    weight                int      default 100               not null comment '权重 (0-100)',
    status                char(20) default 'todo'            not null comment '任务状态 (todo: 待办, in_progress: 进行中, done: 已完成, cancelled: 已取消)',
    priority              char(10) default 'medium'          null comment '优先级 (high: 高, medium: 中, low: 低)',
    expected_duration_sec int                                null comment '预期持续时间（秒）',
    actual_consumed_sec   int                                null comment '实际消耗时间（秒）',
    created_at            datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at            datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '任务表';

create index idx_goal_id
    on biz_focus_task (goal_id)
    comment '目标ID索引';

create index idx_status
    on biz_focus_task (status)
    comment '任务状态索引';

create index idx_user_plan_date
    on biz_focus_task (user_id, plan_start_date, plan_end_date)
    comment '用户ID和计划日期范围联合索引';

