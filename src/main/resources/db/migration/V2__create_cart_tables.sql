create table carts
(
    id           UUID default gen_random_uuid()
        primary key,
    date_created date       default CURRENT_DATE
);
create table cart_items
(
    id         int           PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    cart_id    UUID    not null,
    product_id bigint        not null,
    quantity   int default 1 not null,
    constraint cart_items_carts_id_fk
        foreign key (cart_id) references carts (id)
            on delete cascade,
    constraint cart_items_products_id_fk
        foreign key (product_id) references products (id)
            on delete cascade,
     unique(product_id,cart_id)
);
