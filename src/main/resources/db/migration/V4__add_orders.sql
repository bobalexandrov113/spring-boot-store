create table orders
(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id bigint not null,
    status varchar(20) not null,
    created_at timestamp default current_timestamp,
    total_price decimal(10,2),
    constraint orders_users_id_fk foreign key (customer_id) references users (id)
);
create table order_items
(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    order_id bigint not null,
    product_id bigint not null,
    unit_price decimal(10,2) not null,
    quantity int not null,
    total_price decimal(10,2) not null,
    constraint orders_items_id_fk foreign key (order_id) references orders(id),
    constraint product_items_fk foreign key(product_id) references products(id)
);