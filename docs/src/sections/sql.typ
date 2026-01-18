#import "../lib.typ" : *

= SQL database schema<sql-database-schema>

== Overview

The project requirements slightly change from `pure_html` and `js`, where the latter requires the playlist to support custom ordering the tracks -- this is achieved by adding an optional position column in `playlist_tracks` and a custom order flag in `playlist`

In both scenarios, the schema is the same.

== The tables

```sql
CREATE TABLE user
(
    id       integer AUTO_INCREMENT,
    username VARCHAR(64) not null,
    password VARCHAR(64) not null,
    name     VARCHAR(64) not null,
    surname  VARCHAR(64) not null,

    PRIMARY KEY (id),
    UNIQUE KEY `username` (`username`),
);
```
it is quite straightforward and standard. Apart from the `id` attribute, which is the primary key, the only other attribute that has a unique constraint is `username`.

```sql
CREATE TABLE track
(
    id          integer      AUTO_INCREMENT,
    loader_id   integer      not null ,
    file_path   VARCHAR(512) not null,
    image_path  VARCHAR(512) not null,
    title       VARCHAR(64)  not null,
    author      VARCHAR(64)  not null,
    album_title VARCHAR(64)  not null,
    album_publication_year integer not null,
    genre       VARCHAR(64)  not null,

    PRIMARY KEY (id),
    UNIQUE (loader_id, title, author, 
    album_title, album_publication_year),
    FOREIGN KEY (loader_id) 
    REFERENCES user (id) 
    ON UPDATE CASCADE ON DELETE CASCADE
);
```

The unique constraint on `loader_id, title, author, album_title, album_publication_year` is to make sure that the user doesn't load duplicates (there are almost all the attributes inside it to address the unlikely situation where an almost identical track is loaded).

The `loader_id` foreign key references the `id` of the user and if it is removed, his tracks are also removed.

```sql
CREATE TABLE playlist
(
    id            integer AUTO_INCREMENT,
    title         VARCHAR(64) not null,
    creation_date DATETIME not null DEFAULT NOW(),
    author_id     integer     not null,
    custom_order  BOOL        not null,

    PRIMARY KEY (id),
    UNIQUE (title, author_id),
    FOREIGN KEY (author_id) 
    REFERENCES user (id) 
    ON UPDATE CASCADE ON DELETE CASCADE
);
```

The `creatione_date` attribute defaults to today's date; and there is also the unique constraint on `title, author_id` because a playlist is bound to a single user (who can't have duplicate playlists) via the foreign key.


```sql
CREATE TABLE playlist_tracks
(
    id          integer AUTO_INCREMENT,
    playlist_id integer not null,
    track_id    integer not null,
    position    integer,

    PRIMARY KEY (id),
    FOREIGN KEY (playlist_id) 
    REFERENCES playlist (id) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (track_id) 
    REFERENCES track (id) 
    ON UPDATE CASCADE ON DELETE CASCADE
);
```<playlist-tracks-code>

This table represents the "Contains" relation in the ER diagram (@er-diagram). If a track or a playlist is removed their relationship is also removed.

