#import "../lib.typ": *

= Codebase overview

== Components

/ Introduction : Both versions of the project present a lot of similarities but fot the TS version have been reorganized and some features were added

#show: table-styles.with(header-height: 1)

== Backend

FAI IN MODO CHE LA TABELLA SIA POSIZIONATA PER IL BACKEND

#figure(
  placement: top,
  scope: "parent",
  table(
    stroke: 1pt,
    columns: (auto, 1fr, 1fr),
    align: left,
    table.header(
      [],
      [HTML],
      [TS],
    ),
    [*Entity*],
    table.cell(colspan: 2)[
      - Playlist
      - PlaylistTracks
      - Track
      - User
    ],

    [*DAO*], 
    table.cell(colspan: 2)[
      #grid(
        align: left,
        columns: (1fr, auto),
        [
        - PlaylistDAO
          - findByAuthorIdOrderByCreationDateAsc
          - save
          - findByAuthorIdAndId #ts()
          - setCustomOrder #ts()
        ], 
        [
        - PlaylistTracksDAO
          - save
          - getAllByPlaylistId
        ],
        [
        - TrackDAO
          - save
          - findTrackByIdAndLoaderId
          - getAllByUserIdSorted
          - getPlaylistTracksGroup
          - getAllNotInPlaylist
          - getAllInPlaylist #ts()
          - getAllInPlaylistCustom #ts()
          - updatePosition #ts()
        ],
        [
        - UserDAO
          - save
          - findByUsername
        ],
      )
    ],

    [*Controller*],
    [
      - FileController
        - serveSafeFile 
      - HomeController
        - showPage 
        - addTrack 
        - addPlaylist 
      - LoginController
        - showPage 
      - PlaylistConstroller
        - showPage 
        - addTrackToPlaylist 
      - SubscribeController
        - showPage 
        - subscribe 
      - TrackController
        - showPage 
    ],
    [
      - FileController
        - serveSafeFile 
      - AuthController
        - login
        - subscribe
      - PlaylistConstroller
        - getPlaylists
        - getPlaylistSizeByid
        - addPlaylist
        - addTracksToPlaylist
        - setCustomOrder
      - TrackConstroller
        - getTrackById
        - getTracks
        - getAllNotInPlaylist
        - getAllInPlaylist
        - getGenres
        - addTrack
    ],

    [*Springboot \ Config*],
    [
    - UserService
      - loadUserByUsername
    - UserWithId
    - SecurityConfig
      - securityFilterChain 
    ],
    [
    - SecurityConfig
      - securityFilterChain 
      - securityContextRepository
      - authenticationManager
    ],
  ),
  caption: [Components comparison],
)

== Frontend

parla delle viste html e delle viste per typescript

== RIA subproject

#show: table-styles.with()

#figure(
  placement: top,
  scope: "parent",
  table(
    columns: 4,
    align: horizon,
    table.header(
      table.cell(colspan: 2)[Client side],
      table.cell(colspan: 2)[Server side],
      [Event],
      [Action],
      [Event],
      [Action],
    ),

    [Index $=>$ Login form $=>$ Submit], [Data validation], [POST (`username`, `password`)], [Credentials check],
    [HomeView $=>$ Load], [Loads all User playlists], [GET (`user playlists`)], [Queries user playlists],
    [HomeView $=>$ Click on a playlist],
    [Loads all tracks associated to that Playlist],
    [GET (`playlistId`)],
    [Queries the tracks associated to the given playlistId],

    [HomeView $=>$ Click on reorder button],
    [Load a modal to custom order the track in the Playlist],
    [GET (`playlistId`)],
    [Queries the tracks associated to the given playlistId],

    [Reorder modal $=>$ Save order button],
    [Saves the custom order to the database],
    [POST (`trackIds, playlistId`)],
    [Updates the `playlist_tracks` table with the new custom order],

    [Create playlist modal $=>$ Create playlist button],
    [Loads the modal to create a new playlist; returns the newly created playlist if successful],
    [POST (`playlistTitle`, `selectedTracks`)],
    [Inserts the new Playlist in the `playlist` table],

    [Upload track modal $=>$ Upload track button],
    [Loads the modal to upload a new track; returns the newly uploaded track if successful],
    [POST (`title`, `artist`, `year`, `album`, `genre`, `image`, `musicTrack`)],
    [Inserts the new Track in the `tracks` table],

    [Sidebar $=>$ Playlist button],
    [Views the last selected Playlist, if one had been selected],
    [GET (`last selected Playlist`)],
    [Queries the tracks associated to the given playlistId],

    [Sidebar $=>$ Track button],
    [Views the last selected Track, if one had been selected],
    [GET (`last selected Track`)],
    [Queries the data associated with the given trackId],

    [Sidebar $=>$ HomePage], [Returns to the HomeView], [GET (user playlists)], [Queries user playlists],
    [Logout], [Invalidates the current User session], [GET], [Session invalidation],
  ),
  caption: [Events & Actions.],
)

#figure(
  placement: top,
  scope: "parent",
  table(
    columns: 4,
    align: horizon,
    table.header(
      table.cell(colspan: 2)[Client side],
      table.cell(colspan: 2)[Server side],
      [Event],
      [Controller],
      [Event],
      [Controller],
    ),

    [Index $=>$ Login form $=>$ Submit], [`makeCall()` function], [POST (`username`, `password`)], [Login (servlet)],
    [HomeView $=>$ Load],
    [`HomeView.show()` (its invocation is done by the `MainLoader` class)],
    [GET],
    [Homepage (servlet)],

    [HomeView $=>$ Click on a playlist],
    `loadPlaylist()`,
    [GET (`playlistId`)],
    [Playlist (servlet)],

    [HomeView $=>$ Click on reorder button],
    `loadReorderModal()`,
    [GET (`playlistId`)],
    [Playlist (servlet)],

    [Reorder modal $=>$ Save order button],
    `saveOrder()`,
    [POST (`trackIds`, `playlistId`)],
    [TrackReorder (servlet)],

    [Create playlist modal $=>$ Create playlist button],
    `makeCall()`,
    [POST (`playlistTitle`, `selectedTracks`)],
    [CreateNewPlaylist (servlet)],

    [Upload track modal $=>$ Upload track button],
    `makeCall()`,
    [POST (`title`, `artist`, `year`, `album`, `genre`, `image`, `musicTrack`)],
    [UploadTrack (servlet)],

    [Sidebar $=>$ Playlist button],
    `playlistView.show()`,
    [GET (`last selected Playlist`)],
    [Playlist (servlet)],

    [Sidebar $=>$ Track button],
    `trackView.show()`,
    [GET (`last selected Track`)],
    [Track (servlet)],

    [Sidebar $=>$ HomePage],
    `homeView.show()`,
    [GET (`user playlists`)], [Homepage (servlet)],
    [Logout], [`makeCall()` function], [GET], [Logout (servlet)],
  ),
  caption: [Events & Controllers (or event handlers).],
)
