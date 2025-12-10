create table sys_attachment
(
    id               bigint        not null comment '主键'
        primary key,
    storage_name     varchar(100)  null comment '文件数据库名',
    original_name    varchar(100)  null comment '文件原名称',
    extension_name   varchar(50)   null comment '文件扩展名',
    path             varchar(500)  null comment '文件保存路径',
    upload_id        varchar(500)  null comment '分片上传id',
    business_code    varchar(100)  null comment '业务编码',
    business_name    varchar(100)  null comment '业务名称',
    size             varchar(100)  null comment '文件大小',
    type             varchar(150)  null comment '文件类型',
    upload_mode      char          null comment '上传方式（一般上传、分片上传、文件秒传）',
    status           char          null comment '上传状态（成功、失败、分片上传中、业务删除）',
    storage_location varchar(50)   null comment '文件存储位置',
    md5              varchar(50)   null comment '文件md5值',
    create_id        bigint        null comment '上传人id',
    create_time      datetime      null comment '上传时间',
    del_flag         char          null comment '删除标识',
    error_msg        varchar(1000) null comment '上传失败原因',
    url              varchar(500)  null comment '原url（通过url上传有该字段）'
)
    comment '系统附件表' row_format = DYNAMIC;

