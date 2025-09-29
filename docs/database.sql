CREATE TABLE track
(
    id                     integer,
    file_path              VARCHAR(512) not null,
    image_path             VARCHAR(512) not null,
    title                  VARCHAR(64)  not null,
    author                 VARCHAR(64)  not null,
    album_title            VARCHAR(64)  not null,
    album_publication_year integer      not null,
    genre                  VARCHAR(64)  not null,
    position               integer
);

-- valutare per la data in che formato salvarla
CREATE TABLE playlist
(
    id            integer,
    title         VARCHAR(64) not null,
    creation_date VARCHAR(64) not null,
    author        VARCHAR(64) not null,
    custom_order  BOOL        not null,

    PRIMARY KEY (id)
);

CREATE TABLE user
(
    username VARCHAR(64) not null,
    password VARCHAR(64) not null,
    name     VARCHAR(64) not null,
    surname  VARCHAR(64) not null,

    PRIMARY KEY (username)
);

CREATE TABLE playlist_tracks
(
    playlist_id integer,
    track_id    integer
);
