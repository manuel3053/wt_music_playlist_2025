#import "@preview/cetz:0.4.2"
#import "../lib.typ": *
#import "../dati-basati/src/lib.typ" as db

= Project submission breakdown<project-breakdown>

== Database logic

#let db_legend = (
  entity("Entity"),
  attr("Attribute"),
  attr_spec([Attribute \ specification]),
  rel("Relationship"),
)
#legend(db_legend)

Each #entity[user] has a #attr[username], #attr[password], #attr[name] and #attr[surname]. Each musical #entity[track] is stored in the database via #attr[title], #attr[image], #attr[album title], #attr[album artist name] (single or group), #attr[album release year], #attr[musical genre] and #attr[file]. Furthermore:

- Suppose the #attr_spec[genres are predetermined]\ #comment("the user cannot create new genres")
- It is not requested to store the track order within albums
- Suppose #attr_spec[each track can belong to a unique album] (no compilations)

// #colbreak()

After the login, the user is able to #rel[create tracks] by loading their data and then group them in playlists. A #entity[playlist] #rel[is a set of chosen tracks] from the uploaded ones of the user. A playlist has a #attr[title], a #attr[creation date] and is #rel[associated to its creator].

#let entities = (
  "user": (
    coordinates: (0, 0),
    attributes: (
      "west": ("id", "nickname", "password",),
      "north": ("name", "surname"),
    ),
    primary-key: ("id",),
    weak-primary-key: none,
    label: "user",
    name: "user",
  ),
  "playlist": (
    coordinates: (5, -5),
    attributes: (
      "north": ("title", "id", "author_id", "creation_date", "custom_order",).rev(),
    ),
    primary-key: ("id",),
    weak-primary-key: ("title", "west"),
    label: "playlist",
    name: "playlist",
  ),
  "track": (
    coordinates: (10, 0),
    attributes: (
      "east": ("id", "title", "file_path", "image_path",).rev(),
      "north": ("loader_id", "author", "genre", "position", "album_publication_year", "album_title"),
    ),
    primary-key: ("id",),
    weak-primary-key: none,
    label: "track",
    name: "track",
  ),
)

#let relations = (
  "user-playlist": (
    coordinates: (0, -5),
    entities: ("user", "playlist"),
    label: "creates",
    name: "user-playlist",
    cardinality: ("(0,n)", "(1,1)"),
    attributes: none,
  ),
  "user-track": (
    coordinates: (5, 0),
    entities: ("user", "track"),
    label: "uploads",
    name: "user-track",
    cardinality: ("(0,n)", "(1,1)"),
    attributes: none,
  ),
  "playlist-track": (
    coordinates: (10, -5),
    entities: ("playlist", "track"),
    label: "contains",
    name: "playlist-track",
    cardinality: ("(0,n)", "(0,n)"),
    attributes: none,
  ),
)



#figure(
  placement: bottom,
  scope: "parent",
  cetz.canvas({
    import cetz.draw: *
    register-mark("filled-circle", style => {
      circle((0, 0), radius: 0.2em, fill: black)
      anchor("tip", (0.2em, 0))
      anchor("base", (-0.2em, 0))
    })
    line(
      (3.6, -4), (2.6, -4), (2.6, -5.3),
      length: 4,
      mark: (end: "filled-circle"),
      stroke: 1pt + black
    )
    for entity in entities.values() {
      db.entity(
        entity.coordinates,
        label: entity.label,
        name: entity.name,
        attributes: entity.attributes,
        primary-key: entity.primary-key,
        weak-primary-key: entity.weak-primary-key,
      )
    }

    for relation in relations.values() {
      db.relation(
        coordinates: relation.coordinates,
        entities: relation.entities,
        label: relation.label,
        name: relation.name,
        cardinality: relation.cardinality,
        attributes: relation.attributes,
      )
    }
  }),
  caption: [ER diagram (HTML and RIA).],
)<er-diagram>

// #colbreak()

== Behaviour

#let bhr_legend = (
  user_action("User action"),
  server_action("Server action"),
  page("HTML page"),
  element("Page element"),
)
#legend(bhr_legend)

After the login, the user #user_action[accesses] the #page[HOME PAGE] which #server_action[displays] the #element[list of their playlists], ordered by descending creation date; a #element[form to load a track with relative data] and a #element[form to create a new playlist]. The playlist form:

- #server_action[Shows] the #element[list of user tracks] ordered by artist name in ascending alphabetic order and by ascending album release date
- The form allows to #user_action[select] one or more tracks

When a user #user_action[clicks] on a playlist in the #page[HOME PAGE], the application #server_action[loads] the #page[PLAYLIST PAGE]\; initially, it contains a #element[table with a row and five columns]:

- Every cell contains the track's title and album cover
- The tracks are ordered from left to right by artist name in ascending alphabetic order and by ascending album release date
- If a playlist contains more than 5 tracks, there are available commands to see the others (in blocks of five)

// #colbreak()

/ Playlist tracks navigation: If the #page[PLAYLIST PAGE]:

+ Shows the first group and there are subsequent ones, a #element[NEXT button] appears on the right side of the row
+ Shows the last group and there are precedent ones, a #element[PREVIOUS button] appears on the left side of the row that allows to see the five preceding tracks
+ Shows a block of tracks and there are both subsequent and preceding ones, then on left and the right side appear both previous and next buttons

/ Track creation: The #page[PLAYLIST PAGE] includes a #element[form that allows to add one or more tracks to the current playlist, if not already present]. This form acts in the same way as the playlist creation form.

After adding a new track to the current playlist, the application #server_action[refreshes the page] to display the first block of the playlist (the first 5 tracks). Once a user #user_action[selects the title of a track], the #page[PLAYER PAGE] #server_action[shows] all of the #element[track data] and the #element[audio player].

// #colbreak()

== RIA version

Create a client-server web application that modifies the previous specification as follows:

- After the login, the entire application is built as a single webapp

- Every user interaction is managed without completely refreshing the page, but instead it asynchronously invokes the server and the content displayed is potentially updated

- The visualization event of the previous/next blocks is managed client-side without making a request to the server

/ Track reordering : The application must allow the user to reorder the tracks in a playlist with a personalized order. From the #page[HOME PAGE] with an associated link to each playlist, the user #user_action[access] a modal window #element[REORDER] which shows the list of tracks ordered with the current criteria (custom or default).

The user can #user_action[drag] the title of a track and #user_action[drop] it in a different position to achieve the desired order, without invoking the server. Once finished, the user can click on a #element[button to save the order] and #server_action[store] the sequence on the server. In subsequent accesses, the personalized track order is #server_action[loaded] instead of the default one. A newly added track in a custom-ordered playlist is #server_action[inserted #underline(stroke: yellow.darken(20%))[always] at the end].

== Added features

- Logout function:
  - HTML: @logout-sequence-html
  - RIA: @logout-sequence-ria
- Subscribe function:
  - HTML: @subscribe-sequence-html  
  - RIA: @subscribe-sequence-ria

// #colbreak()

#figure(
  placement: top,
  scope: "parent",
  image(
    width: 115%,
    fit: "contain",
    "../img/ifml/ifml_html.png",
  ),
  caption: "IFML diagram (HTML).",
)<html-ifml-diagram>



#figure(
  placement: bottom,
  scope: "parent",
  image(
    width: 115%,
    fit: "contain",
    "../img/ifml/ifml_ria.png",
  ),
  caption: "IFML diagram (RIA).",
)<ria-ifml-diagram>
