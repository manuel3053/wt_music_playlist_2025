#import "../lib.typ": *

#show: thymeleaf_trick.with()

#let actors(
  view: "",
  controller: "",
  daos: (),
  full: true,
) = {
  _par("A", display-name: "", shape: "custom", custom-image: client)
  _par("B", display-name: "", shape: "custom", custom-image: servlet)
  if controller.len() != 0 {
    _par("C", display-name: controller)
  }
  if full {
    _par("D", display-name: "Model")
    _par("E", display-name: "", shape: "custom", custom-image: thymeleaf)
  }
  daos.enumerate(start: 6).map(e => _par(numbering("A", e.at(0)), display-name: e.at(1))).join()
}

#let action(
  type: "",
  mapping: "",
  action: "",
) = {
  _seq("A", "B", comment: [#type\ /#mapping])
  _seq("B", "C", comment: action)
}

#let sequence(
  type: "GET",
  mapping: "",
  method: "",
  view: "",
  controller: "",
  daos: (),
  extra: (),
) = {
    actors(view: view, controller: controller, daos: daos, full: mapping == "")

    if mapping == "" {
      action(type: type, mapping: view, action: "showPage (model)")
    } else {
      action(type: type, mapping: mapping, action: method)
    }
    extra.join()
    if mapping == "" {
      _seq("C", "B", enable-dst: true, comment: ["#view"])
      _seq("B", "E", disable-src: true, enable-dst: true, comment: "render (mv, req, res)")
      _seq("E", "A", disable-src: true, comment: [#view#str(".html")])
    } else {
      _seq("C", "B", comment: [":redirect/#view"])
    }
  }

#let addAttribute(
  body
) = {
    _seq("C", "D", comment: [addAttribute (#body)])
}

= Sequence diagrams HTML

== Disclaimer
- scrivi cosa è DispatcherServlet e associalo all'icona
- associa client ad icona
- spiega che tutti i redirect seguono lo stesso sequence di quando si richiede una pagina normalmente e che questo è stato omesso per semplicità
- Aggiungi che Springboot modifica molto i sequence diagram rispetto a delle basilari servlet
- Mostra le configurazioni di springboot con il codice e spiega perché servono in questa parte
- Dai una spiegazione unica sul flusso di presentazione di una pagina, così non devi farlo ogni volta
- Aggiungi che le eccezioni sono gestite ma non in modo user-friendly, dato che non era richiesto di gestire finemente le eccezioni ma solo di gestirle; inoltre il tempo a disposizione era poco
- Aggiungi che ogni volta che compare userId vuol dire che è stato chiamato UserDetailsExtractor che recupera l'id dell'utente dal SecurityContext

#pagebreak()

#seq_diagram(
  "Login sequence diagram",
  diagram({

    sequence(
      view: "login", 
      controller: "LoginController", 
      daos: ("UserDAO",)
    )
    // _par("F", display-name: "", shape: "custom", custom-image: springboot)

    _seq("A", "B", comment: [POST\ /login])
    _seq("B", "F", comment: [loadUserByUsername(username)])
    _seq("F", "B", comment: [Compare data with credentials from post])
    _alt(
      "succesful comparison",
      {
        _seq("C", "B", comment: [":redirect/home"])
      },
      "Failed comparison",
      {
        _seq("C", "B", comment: [":redirect/login"])
      },
    )

  }),
  comment: [
    Once the server is up and running, the Client requests the Login page. Then, thymeleaf processes the request and returns the correct context to index the correct locale. Afterwards, the User inserts their credentials.

    Those values are passed to the DispatcherServlet which sends the message to a POST /login mapping offered by Springboot: here the message is analyzed by different filters (almost all of them are used internally by Springboot), till it reaches the AuthorizationFilter.

    The AuthorizationFilter calls internally the UserDetailsService Bean wich is implemented by the UserService class which, inside loadUserByUsername, queries the DB to receive a user associated to that username (that's why the UserDAO is called).

    After this operation, the AuthorizationFilter filter compares the data provided by the client and by the UserDetailsService and if they are the same, the user is redirected to the home page. Otherwise the user is redirected to the login page.
  ],
  label_: "login-sequence",
  comment_next_page_: false,
)


#seq_diagram(
  [Logout sequence diagram],
  diagram({
    actors(full: false)

    _seq("A", "B", comment: [POST\ /logout])
    _seq("B", "B", comment: [":redirect/logout"])

  }),
  comment: [
    In order to perform a logout, Springboot requires the client to call the POST /logout mapping which internally invalidates session and all the other data associated to the user (of course the data inside the DB are preserved), and then the user is redirected to the login page.
  ],
  label_: "logout-sequence",
  comment_next_page_: false,
)

#seq_diagram(
  [Subscribe sequence diagram],
  diagram({

    sequence(
      view: "subscribe", 
      controller: "SubscribeController", 
      daos: ("userDAO",),
      extra: (addAttribute("userForm"),)
    )

    _seq("A", "B", comment: [POST userForm\ /subscribe/submit])
    _seq("B", "C", comment: [subscribe (userForm)])
    // aggiungi i due percorsi
    _alt(
      "try",
      {
        _seq("C", "F", comment: [save (userForm.toUser())])
        _seq("C", "B", comment: [":redirect/login"])
      },
      "catch RuntimeException",
      {
        _seq("C", "B", comment: [":redirect/subscribe"])
      },
    )

  }),
  comment: [
    After requesting the page with GET /subscribe, the user can fill and submit the provided form to subscribe to the site.

    The call is redirected to the correct controller (SubscribeController) by the DispatcherServlet: the controller tries to save the user by calling userDAO.save() and if any kind of RuntimeException exceptio occurs (so SQLExceptions are also considered), the user is redirected to the subscribe page. Otherwise the user is redirected to the login page.

    In this situation an SQLExceptions exception might occour also because in the DB there is already a user with the same username.
  ],
  label_: "subscribe-sequence",
  comment_next_page_: false,
)

#seq_diagram(
  [Show home page sequence diagram],
  diagram({

    sequence(
      view: "home", 
      controller: "HomeController", 
      daos: ("playlistDAO", "trackDAO"), 
      extra: (
        _alt(
          "try",
          {
            _seq("C", "F", comment: [findByAuthorIdOrderByCreationDateAsc (userId)])
            _seq("F", "C", comment: [playlists])
            _seq("C", "G", comment: [getAllByUserIdSorted (userId)])
            _seq("G", "C", comment: [tracks])
          },
          "catch RuntimeException",
          {
            _seq("C", "B", comment: [":redirect/login"])
          },
        ),
        addAttribute("userId"),
        addAttribute("tracks"),
        addAttribute("playlists"),
        addAttribute("trackForm"),
        addAttribute("playlistForm"),
        addAttribute("Genres.values()"),
      )
    )


  }),
  comment_next_page_: false,
  comment: [
    When the user requests the home page, the HomeController obtains the user id and requests: all playlists owned by the user, sorted as pointed in the submission; all tracks owned by the user, sorted as pointed in the submission
    The results of these requests are added inside the thymeleaf Model. 

    If a RuntimeException occours the user is redirected to the login page.

    Inside the Model are also added the trackForm and Genre.values() to manage the upload of a track and playlistForm to manage the upload of a playlist
  ],
  label_: "homepage-sequence",
)

#seq_diagram(
  [Show playlist page sequence diagram],
  diagram({

    sequence(
      view: "playlist", 
      controller: "PlaylistController", 
      daos: ("trackDAO", "playlistTracksDAO"), 
      extra: (
        _alt(
          "try",
          {
            _seq("C", "F", comment: [getPlaylistTracksGroup (playlistId, offset $dot$ 5, userId)])
            // _seq("C", "F", comment: [getPlaylistTracksGroup (playlistId, offset $dot$ 5, userId)])
            _seq("F", "C", comment: [tracks])
            _seq("C", "F", comment: [getAllNotInPlaylist (userId, playlistId)])
            _seq("F", "C", comment: [tracksNotInPlaylist])
            addAttribute("tracksNotInPlaylist")
            _seq("C", "G", comment: [getAllByPlaylistId (playlistId, userId)])
            _seq("G", "C", comment: [playlistTracks])
            addAttribute("playlistTracksSize")
          },
          "tracks.isEmpty()",
          {
            _seq("C", "B", comment: [":redirect/home"])
          },
          "catch RuntimeException",
          {
            _seq("C", "B", comment: [":redirect/home"])
          },
        ),
        addAttribute("tracks"),
        addAttribute("userId"),
        addAttribute("playlistId"),
        addAttribute("offset"),
        addAttribute("playlistForm"),
      )
    )

  }),
  comment_next_page_: true,
  comment: [
    When the user requests the playlist page, the PlaylistController obtains the user id and requests: the first group of tracks in the playlist; all tracks not in the playlist; all the tracks in the playlist
    The results of these requests are added inside the thymeleaf Model (for the last one only the size is considered). 

    If a RuntimeException occours or the playlist is empty, the user is redirected to the login page.

    Inside the Model is also added playlistForm to manage the insertion of more tracks in the playlist.
  ],
)

#seq_diagram(
  [Show track page sequence diagram],
  diagram({
    sequence(
      view: "track", 
      controller: "TrackController", 
      daos: ("trackDAO",), 
      extra: (
        _alt(
          "try",
          {
            _seq("C", "F", comment: [findTrackByIdAndLoaderId (trackId, userId)])
            _seq("F", "C", comment: [track])
            addAttribute("track")
          },
          "tracks == null",
          {
            _seq("C", "B", comment: [":redirect/home"])
          },
          "catch RuntimeException",
          {
            _seq("C", "B", comment: [":redirect/home"])
          },
        ),
      )
    )

  }),
  comment_next_page_: false,
  comment: [
    When the user requests the track page, the TrackController obtains the user id and requests the track that the user wants to display.
    The result is added to the model to display the data about the track.

    If a RuntimeException occours or the track is null, the user is redirected to the home page.
  ],
)

#seq_diagram(
  [Add track sequence diagram],
  diagram({

    sequence(
      type: "POST trackForm",
      mapping: "home/add_track",
      method: "addTrack (trackForm)",
      view: "home",
      controller: "HomeController",
      daos: ("trackDAO", "Storage"),
      extra: (
        _alt(
          "try",
          {
            _seq("C", "G", comment: [Files.copy(audio)])
            _seq("C", "G", comment: [Files.copy(cover)])
            _seq("C", "F", comment: [save(trackForm.toTrack())])
          },
          "catch Exception",
          {
            _seq("C", "B", comment: [":redirect/home"])
          },
        ),
      )
    )

  }),
  comment_next_page_: false,
  comment: [
  ],
)

#seq_diagram(
  [Add playlist sequence diagram],
  diagram({

    sequence(
      type: "POST playlistForm",
      mapping: "home/add_playlist",
      method: "addPlaylist (playlistForm)",
      view: "home",
      controller: "HomeController",
      daos: ("trackDAO", "playlistTrackDAO",),
      extra: (
        _alt(
          "try",
          {
            _seq("C", "F", comment: [save(playlistForm.toPlaylist())])
            _seq("C", "G", comment: [saveAll(playlistForm.toPlaylistTracks())])
          },
          "catch Exception",
          {
            _seq("C", "B", comment: [":redirect/home"])
          },
        ),
      )
    )

  }),
  comment_next_page_: false,
  comment: [
  ],
)

#seq_diagram(
  [Add track to playlist sequence diagram],
  diagram({

    sequence(
      type: "POST playlistForm",
      mapping: "playlist/add_track_to_playlist",
      method: "addTrackToPlaylist (playlistForm)",
      view: "playlist/playlist_id/0",
      controller: "PlaylistController",
      daos: ("playlistTracksDAO",),
      extra: (
        _alt(
          "try",
          {
            _seq("C", "F", comment: [saveAll(\ playlistForm.toPlaylistTracks()\ )])
          },
          "catch Exception",
          {
            _seq("C", "B", comment: ["redirect:/home"])
          },
        ),
      )
    )

  }),
  comment_next_page_: false,
  comment: [
  ],
)

#seq_diagram(
  [Get file sequence diagram],
  diagram({
    actors(controller: "FileController", daos: ("Storage",), full: false)
    action(
      type: "GET", 
      mapping: "file/user_id/playlist_name/cover_name", 
      action: [serveSafeFile(\ userId, \ playlistName, \ coverName\ )]
    )
    _seq("F", "C", comment: [new UrlResource(realFile.toUri())])
    _alt(
      "File.exists()",
      {
        _seq("C", "A", comment: [return ResponseEntity.ok() \ .contentType(MediaType.APPLICATION_OCTET_STREAM) \ .body(file)])
      },
      "!File.exists()",
      {
        _seq("C", "A", comment: [return ResponseEntity.\ notFound() \.build()])
      },
      "catch IOException",
      {
        _seq("C", "A", comment: [return ResponseEntity\ .status( HttpStatus.INTERNAL_SERVER_ERROR)\ .build()])
      },
    )

  }),
  comment_next_page_: false,
  comment: [
  ],
)

= Sequence diagrams RIA

#seq_diagram(
  "Login sequence diagram",
  diagram({

    actors(
      view: "login",
      controller: "LoginController", 
      daos: ("UserDAO",),
      full: false,
    )

    _par("G", display-name: "", shape: "custom", custom-image: guard)
    // _par("H", display-name: "SecurityContextHolder")
    // _par("H", display-name: "SecurityContextRepository")
    // _par("H", display-name: [A])
    // _par("F", display-name: "", shape: "custom", custom-image: springboot)

    _seq("A", "B", comment: [POST\ /login])
    _seq("B", "C", comment: [login(...)])
    _seq("C", "C", comment: [token = \ UsernamePassword\\ \ AuthenticationToken \ .unauthenticated(...)])
    _seq("C", "G", comment: [authenticate(token)])
    _seq("G", "F", comment: [loadUserByUsername(...)])
    _seq("F", "G", comment: [user])
    _seq("G", "G", comment: [Compare user with \ credentials from post])
    _seq("G", "C", comment: [authentication])
    _seq("C", "C", comment: [context = \ SecurityContextHolder\ .createEmptyContext()])
    _seq("C", "C", comment: [context\ .setAuthentication(context)])
    _seq("C", "C", comment: [SecurityContextHolder\ .setContext(context)])
    _seq("C", "C", comment: [SecurityContextRepository\ .saveContext(context, res req)])
    _alt(
      "succesful comparison",
      {
        _seq("C", "B", comment: [":redirect/home"])
      },
      "Failed comparison",
      {
        _seq("C", "B", comment: [":redirect/login"])
      },
    )

  }),
  comment: [
  ],
  label_: "login-sequence",
  comment_next_page_: false,
)


#seq_diagram(
  [Logout sequence diagram],
  diagram({
    actors(full: false)

    _seq("A", "B", comment: [POST\ /logout])
    _seq("B", "B", comment: [":redirect/logout"])

  }),
  comment: [
    In order to perform a logout, Springboot requires the client to call the POST /logout mapping which internally invalidates session and all the other data associated to the user (of course the data inside the DB are preserved), and then the user is redirected to the login page.
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [Subscribe sequence diagram],
  diagram({

    actors(
      view: "subscribe", 
      controller: "AuthController", 
      daos: ("userDAO",),
      full: false
    )

    _seq("A", "B", comment: [POST \ /subscribe/submit])
    _seq("B", "C", comment: [subscribe (username, password, name, password)])
    // aggiungi i due percorsi
    _alt(
      "try",
      {
        _seq("C", "F", comment: [save (...)])
        _seq("C", "A", comment: [status 200])
      },
      "catch RuntimeException",
      {
        _seq("C", "A", comment: [status 500])
      },
    )

  }),
  comment: [
    After requesting the page with GET /subscribe, the user can fill and submit the provided form to subscribe to the site.

    The call is redirected to the correct controller (SubscribeController) by the DispatcherServlet: the controller tries to save the user by calling userDAO.save() and if any kind of RuntimeException exceptio occurs (so SQLExceptions are also considered), the user is redirected to the subscribe page. Otherwise the user is redirected to the login page.

    In this situation an SQLExceptions exception might occour also because in the DB there is already a user with the same username.
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [GetPlaylist sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("playlistDAO",),
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_playlists])
    _seq("B", "C", comment: [getPlaylists()])
    _seq("C", "F", comment: [findByAuthorIdOrderByCreationDateAsc(userId)])
    _seq("F", "C", comment: [playlists])
    _seq("C", "A", comment: [playlists])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [GetPlaylistSizeByid sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("playlistTracksDAO",),
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_playlist_size_by_id/{id}])
    _seq("B", "C", comment: [getPlaylistSizeByid(id)])
    _seq("C", "F", comment: [getAllByPlaylistId(id, userId)])
    _seq("F", "C", comment: [playlists])
    _seq("C", "A", comment: [playlists.size()])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [AddPlaylist sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("trackDAO", "playlistDAO", "playlistTracksDAO",),
      full: false
    )

    _seq("A", "B", comment: [POST \ /add_playlist])
    _seq("B", "C", comment: [addPlaylist(title, selectedTracks)])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [AddTracksPlaylist sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("trackDAO", "playlistDAO", "playlistTracksDAO",),
      full: false
    )

    _seq("A", "B", comment: [POST \ /add_tracks_to_playlist])
    _seq("B", "C", comment: [addTracksToPlaylist(playlistId, selectedTracks)])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [SetCustomOrder sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("trackDAO", "playlistDAO",),
      full: false
    )

    _seq("A", "B", comment: [POST \ /set_custom_order])
    _seq("B", "C", comment: [addTracksToPlaylist(playlistId, selectedTracks)])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [getTrackById sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      daos: ("trackDAO",),
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_track_by_id/{id}])
    _seq("B", "C", comment: [getTrackById(id)])
    _seq("C", "F", comment: [findTrackByIdAndLoaderId(id, userId)])
    _seq("F", "C", comment: [track])
    _seq("C", "A", comment: [track])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [GetTrackById sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      daos: ("trackDAO",),
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_tracks])
    _seq("B", "C", comment: [getTracks()])
    _seq("C", "F", comment: [getAllByUserIdSorted(userId)])
    _seq("F", "C", comment: [tracks])
    _seq("C", "A", comment: [tracks])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [GetTracks sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      daos: ("trackDAO",),
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_tracks])
    _seq("B", "C", comment: [getTracks()])
    _seq("C", "F", comment: [getAllByUserIdSorted(userId)])
    _seq("F", "C", comment: [tracks])
    _seq("C", "A", comment: [tracks])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [GetAllNotInPlaylist sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      daos: ("trackDAO",),
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_all_not_in_playlist/{id}])
    _seq("B", "C", comment: [getTracks(id)])
    _seq("C", "F", comment: [getAllNotInPlaylist(userId, id)])
    _seq("F", "C", comment: [tracks])
    _seq("C", "A", comment: [tracks])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [GetAllNotInPlaylist sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      daos: ("trackDAO",),
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_all_in_playlist/{id}])
    _seq("B", "C", comment: [getAllInPlaylist(id)])
    _seq("C", "F", comment: [getAllNotInPlaylist(userId, id)])
    _seq("F", "C", comment: [tracks])
    _seq("C", "A", comment: [tracks])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [GetGenres sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      full: false
    )

    _seq("A", "B", comment: [GET \ /get_genres])
    _seq("B", "C", comment: [getGenres()])
    _seq("C", "A", comment: [genres])

  }),
  comment: [
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [AddTrack sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      full: false
    )

    _seq("A", "B", comment: [POST \ /add_track])
    _seq("B", "C", comment: [addTrack()])

  }),
  comment: [
  ],
  comment_next_page_: false,
)
