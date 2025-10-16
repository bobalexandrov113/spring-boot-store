create table categories
(
    id tinyint primary key auto_increment,
    name varchar(255)
   );

create table products
(
    id bigint auto_increment primary key ,
    name varchar(255) not null,
    price decimal(10,2) not null ,
    category_id tinyint,
    constraint fk_category
        foreign key (category_id) references categories (id) on delete restrict
   );
