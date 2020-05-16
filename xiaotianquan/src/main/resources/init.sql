drop table if exists file_meta;

create table if not exists file_meta(
    name varchar(50) not null,--文件名称
    path varchar(1000) not null,-- 文件路径
    size bigint not null,-- 文件大小
    Last_modified timestamp not null,-- 最近一次修改时间
    pinyin varchar(50),
    pinyin_fist varchar(50),
    is_directory boolean not null
);