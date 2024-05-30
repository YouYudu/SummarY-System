# create database SummarY character set utf8;
use SummarY;

create table admin
(
    id                  bigint auto_increment comment '主键'
        primary key,
    username        varchar(32)                 not null comment '用户名',
    password        varchar(64)                 not null comment '密码',
    picture         varchar(150)                null comment '头像地址',
    create_time     datetime                    null comment '创建时间',
    update_time     datetime                    null comment '修改时间',
    constraint idx_username
        unique (username)
)
    comment '管理员信息' collate = utf8_bin;


create table activation
(
    id                  bigint auto_increment comment '主键'
        primary key,
    activation_code     varchar(36)                 not null comment '激活码',
    state               bigint                      not null comment '状态',
    effective_time      bigint                      not null comment '有效期',
    activate_time       datetime                    null comment '激活时间',
    deactivate_time     datetime                    null comment '失效时间',
    create_time         datetime                    null comment '创建时间',
    update_time         datetime                    null comment '修改时间',
    constraint idx_activation_code
        unique (activation_code)
)
    comment '激活信息' collate = utf8_bin;


create table operation
(
    id                  bigint auto_increment comment '主键'
        primary key,
    activation_code     varchar(36)                 not null comment '被操作的激活码',
    admin_id            bigint                      not null comment '管理员id',
    opcode              bigint                      not null comment '操作码',
    create_time         datetime                    null comment '创建时间'
)
    comment '管理员对激活信息的操作' collate = utf8_bin;


create table comment_task
(
    id                  bigint auto_increment comment '主键'
        primary key,
    task_uuid           varchar(36)                 not null comment '摘要生成任务的uuid',
    activation_code     varchar(36)                      not null comment '发起该任务所使用的激活码',
    code                longtext                    not null comment '代码片段',
    comment             longtext                    not null comment '生成的代码摘要',
    create_time         datetime                    null comment '创建时间',
    constraint idx_task_uuid
        unique (task_uuid)
)
    comment '摘要生成的任务信息' collate = utf8_bin;


create table comment_feedback
(
    id                  bigint auto_increment comment '主键'
        primary key,
    task_uuid           varchar(36)               not null comment '被评价的任务的uuid',
    suggestion          longtext                    not null comment '建议',
    accuracy            bigint                      not null comment '准确性',
    fluency             bigint                      not null comment '流畅性',
    informativeness     bigint                      not null comment '信息量',
    create_time         datetime                    null comment '创建时间'
)
    comment '摘要评价' collate = utf8_bin;


create table plugin_feedback
(
    id                  bigint auto_increment comment '主键'
        primary key,
    suggestion          longtext                    not null comment '建议',
    functionality       bigint                      not null comment '功能性',
    beauty              bigint                      not null comment '美观性',
    convenience         bigint                      not null comment '便捷性',
    create_time         datetime                    null comment '创建时间'
)
    comment '插件评价' collate = utf8_bin;