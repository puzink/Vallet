--liquibase formatted sql

--changeset clerk:1
create table wallet (
    amount bigint,
    uuid uuid not null,
    primary key (uuid)
);

alter table wallet add constraint wallet_amount_check check ( amount >= 0 );

--rollback drop table wallet;