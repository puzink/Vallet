--liquibase formatted sql

--changeset clerk:1
create table wallet (
    amount bigint not null check (amount >= 0),
    uuid uuid not null,
    primary key (uuid)
);

--rollback drop table wallet;