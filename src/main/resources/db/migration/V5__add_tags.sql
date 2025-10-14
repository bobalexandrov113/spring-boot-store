create table tags
(
    id bigint primary key auto_increment,
    name varchar(255),
    constraint user_tags_tags_id_fk
        foreign key (id) references users (id)
);

create table user_tags
(
    user_id bigint not null ,
    tag_id bigint  not null,
    primary key (user_id,tag_id),
    constraint user_tags_users_id_fk
        foreign key (user_id) references users (id) on delete cascade ,
    constraint tags_users_id_fk
        foreign key (tag_id) references tags (id) on delete cascade
);
