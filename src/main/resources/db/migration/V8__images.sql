create table images
(
    id  integer primary key ,
    filename varchar(56) not null,
    alt varchar(60),
    width     int,
    height int
);

create table products_images
(
    product_id integer not null,
    image_id   integer,
    constraint products_images_pk
        primary key (product_id, image_id),
    constraint products_images_images_id_id_fk
        foreign key (image_id) references images (id),
    constraint products_images_product_id_id_fk
            foreign key (image_id) references products(id)
);

insert into images(id,filename,alt,width,height) values (1,'SelectJDK.png','a kettle',300,200);
insert into images(id,filename,alt,width,height) values (2,'runTest.png','a toothbrush',300,200);

insert into products_images values(6,1);
insert into products_images values (10,2);