create table profiles
(
    id          bigint  primary key ,
    bio         varchar(255) null,
    phone_number varchar(255)         null,
    date_of_birth varchar(255) null,
    loyalty_points int default 0,
    constraint profiles_users_id_fk
        foreign key (id) references users (id)
);


