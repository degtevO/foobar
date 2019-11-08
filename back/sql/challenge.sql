create table challenge (
    id bigserial primary key,
    type_id int,
    creator bigint,
    assigned bigint,
    status varchar(16)
);

create index challenge_creator_idx on challenge(creator);

create index challenge_assigned_idx on challenge(assigned);