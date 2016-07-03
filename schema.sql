CREATE TABLE images
(
    id INTEGER PRIMARY KEY NOT NULL,
    user_id INTEGER NOT NULL,
    file_path VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    name VARCHAR NOT NULL,
    coordinates POINT,
    taken_at TIMESTAMP,
    width INTEGER NOT NULL,
    height INTEGER NOT NULL
);

CREATE TABLE images_tags
(
    id INTEGER PRIMARY KEY NOT NULL,
    image_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL
);

CREATE UNIQUE INDEX images_tags_image_id_tag_id_uindex ON images_tags (image_id, tag_id);

CREATE TABLE tags
(
    id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR NOT NULL
);

CREATE UNIQUE INDEX tags_name_uindex ON tags (name);

CREATE TABLE users
(
    id INTEGER PRIMARY KEY NOT NULL,
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    access_token VARCHAR,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    name VARCHAR NOT NULL
);

CREATE UNIQUE INDEX users_email_uindex ON users (email);
