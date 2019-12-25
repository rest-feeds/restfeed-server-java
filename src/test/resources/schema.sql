drop table feed if exists;

create table feed
(
    position identity primary key,
    feed     varchar(1024) not null,
    id       varchar(1024) not null,
    type     varchar(1024),
    resource varchar(1024),
    method   varchar(1024),
    timestamp timestamp,
    data      clob
);

create index feed_position ON feed(feed, position);
