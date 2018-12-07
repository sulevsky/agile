create table log_entry
(
    id               bigint primary key auto_increment,
    date_time        timestamp(3),
    ip_address       varchar(15),
    http_head        varchar(1024),
    http_status_code int,
    user_agent       varchar(1024)
);

create table blocked_ip
(
    id         bigint primary key auto_increment,
    ip_address varchar(15),
    comment    varchar(1024)
);