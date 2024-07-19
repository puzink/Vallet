--liquibase formatted sql

--changeset test:2
insert into wallet(uuid, amount)
values ('4906eed1-3ee9-47bb-b10f-596e2d071ea7', 1000),
       ('877d8cf7-15c2-466b-b081-0b7d8664ae74', 2000);

--rollback delete from wallet;