CREATE TABLE user
(
    id       integer AUTO_INCREMENT,
    username VARCHAR(64) not null,
    password VARCHAR(64) not null,
    name     VARCHAR(64) not null,
    surname  VARCHAR(64) not null,

    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE track
(
    id                     integer AUTO_INCREMENT,
    loader_id                     integer not null ,
    file_path              VARCHAR(512) not null,
    image_path             VARCHAR(512) not null,
    title                  VARCHAR(64)  not null,
    author                 VARCHAR(64)  not null,
    album_title            VARCHAR(64)  not null,
    album_publication_year integer      not null,
    genre                  VARCHAR(64)  not null,
    position               integer,

    PRIMARY KEY (id),
    UNIQUE (loader_id, title, author, album_title, album_publication_year),
    FOREIGN KEY (loader_id) REFERENCES user (id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- valutare per la data in che formato salvarla
CREATE TABLE playlist
(
    id            integer AUTO_INCREMENT,
    title         VARCHAR(64) not null,
    creation_date DATETIME not null DEFAULT NOW(),
    author_id     integer not null,
    custom_order  BOOL        not null,

    PRIMARY KEY (id),
    UNIQUE (title, author_id),
    FOREIGN KEY (author_id) REFERENCES user (id) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE playlist_tracks
(
    id integer AUTO_INCREMENT,
    playlist_id integer not null,
    track_id    integer not null,

    PRIMARY KEY (id),
    FOREIGN KEY (playlist_id) REFERENCES playlist (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (track_id) REFERENCES track (id) ON UPDATE CASCADE ON DELETE CASCADE
);