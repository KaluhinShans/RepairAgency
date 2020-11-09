CREATE TABLE usr
(
    id              integer                NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    activation_code character varying(255),
    email           character varying(255) NOT NULL,
    password        character varying(255) NOT NULL,
    name            character varying(255) NOT NULL,
    last_name       character varying(255),
    balance         integer                NOT NULL,
    photo           character varying(255),
    locale          character varying(255),
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE orders
(
    id          INT NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_id     INT NOT NULL,
    master_id   INT,
    price       INT,
    name        character varying(255),
    description character varying(255),
    location    character varying(255),
    status      character varying(255),
    date        DATE,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES usr (id)
);

CREATE TABLE users_roles
(
    usr_id   integer,
    usr_role character varying(255),

    FOREIGN KEY (usr_id) REFERENCES usr (id)
);

CREATE TABLE billing
(
    id       INT NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_id  INT NOT NULL,
    order_id INT,
    amount   INT,
    reminder INT,
    card     character varying(255),
    date     DATE,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES usr (id)
);

CREATE TABLE comments
(
    id          INT NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_id     INT NOT NULL,
    master_id   INT,
    rate        INT,
    description character varying(255) COLLATE pg_catalog."default",
    date        date,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES usr (id),
    FOREIGN KEY (master_id) REFERENCES usr (id)
)
