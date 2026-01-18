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

Space on a page is not infinite and Springboot adds some intermediate passages, so, to keep things readable, these assumptions are applied to the following sequence diagrams.

== Client

#grid(
  columns: (auto, 1fr),
  align: horizon + left,
  [Represented by:],
  client
)

== DispatcherServlet

#grid(
  columns: (auto, 1fr),
  align: horizon + left,
  [Represented by:],
  servlet
)

This servlet is used by Springboot to route incoming requests to the correct servlets created by the mappings in Springboot controllers.

In the case of a traditional MVC application (like the HTML version of this project), this servlet is also used to manage the rendering of a page: in this case DispatcherServlet has to pass a ModelAndView object to a render function.

In order to build this ModelAndView object, a Model object and a View object are needed; it's possible to create manually a ModelAndView object but in this project a different solution is adopted:
+ Each mapping (associated for convention to a showPage() method) that has to load the page, obtains the Model from the framework (by using dependency injection)
+ The Model is then filled with data that needs to be used by the view (in this case a thymeleaf template)
+ The mapping returns a string with the name of a template (return "home" $=>$ loads home.html); this string is read by DispatcherServlet to create the view

At the end of the process DispatcherServlet can build the ModelAndView object and render it.

== Redirects

After some operations (like adding a new track), the user wants to see changes: the mappings that are associated to those operations follow the same flow, like in the following example:
+ the user requests the home page with a GET call to /home
+ In the home page the user loads a new track with a POST call to /add_track
+ /add_track saves the track
+ /add_track redirects the user to /home
+ the home page is loaded again, this time with also the new track

== Exceptions

All exceptions are managed, but since it wasn't a requirement of the project to manage them precisely (and because of time constraints), they are captured by simple catch statements that catch RuntimeExceptions

== UserId

UserId appears a lot of times in the following diagrams: each time it is seen, the following snippet of code is executed:

```java
public static int getUserId() {
    return ((UserWithId) 
    SecurityContextHolder
    .getContext()
    .getAuthentication()
    .getPrincipal())
    .getId();
}
```

When a user is authenticated, Springboot stores his information in a SecurityContextHolder, and in this code we simply extract his id (see @login-sequence-html for more).

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
    Once the server is up and running, the Client requests the login page. Then, thymeleaf processes the request and returns the correct view its data. Afterwards, the User inserts their credentials.

    Those values are passed to DispatcherServlet which sends the message to a POST /login mapping offered by Springboot: here the message is analyzed by different filters (almost all of them are used internally by Springboot), till it reaches the AuthorizationFilter.

    The AuthorizationFilter calls internally the UserDetailsService Bean which is implemented by the UserService class: this class the loadUserByUsername method, which queries the DB to receive a user associated to that username (that's why the UserDAO is involved).

    After this operation, the AuthorizationFilter filter compares the data provided by the client and by UserDetailsService and if they are the same, the user is redirected to the home page. Otherwise the user is redirected to the login page.
  ],
  label_: "login-sequence-html",
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

    The call is redirected to SubscribeController by DispatcherServlet: the controller tries to save the user by calling userDAO.save() and if any kind of RuntimeException exception occurs (so SQLExceptions are also considered), the user is redirected to the subscribe page. Otherwise the user is redirected to the login page.

    In this situation an SQLException might also occur because in the DB there could be already a user with the same username.
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
    When the user requests the home page, the HomeController obtains the user id and with it, it requests: all playlists owned by the user, sorted as pointed in the submission, and all tracks owned by the user, sorted as pointed in the submission.
    The results of these requests are added inside the thymeleaf Model. 

    If a RuntimeException occurs the user is redirected to the login page.

    Inside the Model are also added the trackForm and Genre.values() to manage the upload of a track and playlistForm to manage the upload of a playlist.
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
    When the user requests the playlist page, PlaylistController obtains the user id and with it, it requests: the first group of tracks in the playlist; all tracks not in the playlist; all tracks in the playlist.
    The results of these requests are added inside the thymeleaf Model (for the last one only the size is considered). 

    If a RuntimeException occurs or the playlist is empty, the user is redirected to the login page.

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
    When the user requests the track page, the TrackController obtains the user id and it requests the track that the user wants to display.
    The result is added to the model to display the data about the track.

    If a RuntimeException occurs or the track is null, the user is redirected to the home page.
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
    When the user loads a track, the request is handled by a method in HomeController where the mime type of the loaded files is checked and if they are right, the track is also added to the database.

    If a RuntimeException occurs or if everything went fine, the user is redirected to the home page anyway.
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
    When the user loads a playlist, the request is handled by a method in HomeController which uses trackDAO and playlistTracksDAO to store the data from playlistForm.

    If a RuntimeException occurs or if everything went fine, the user is redirected to the home page.
  ],
)

#seq_diagram(
  [Add track to playlist sequence diagram],
  diagram({

    sequence(
      type: "POST playlistForm",
      mapping: [playlist\ /add_track_to_playlist],
      method: "addTrackToPlaylist (playlistForm)",
      view: "playlist/playlist_id/0",
      controller: "PlaylistController",
      daos: ("trackDAO", "playlistTracksDAO",),
      extra: (
        _alt(
          "try",
          {
            _alt(
              "",
              {
                _seq("C", "F", comment: [getAllByUserIdSorted(\ userId\ )])
                _seq("F", "C", comment: [tracks])
                _seq("C", "G", comment: [saveAll(\ playlistForm.toPlaylistTracks()\ )])
              },
              "tracks not owned by the user",
              {
                _seq("C", "B", comment: ["redirect:/home"])
              }
            )
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
    When the user loads a playlist, the request is handled by a method in PlaylistController which uses playlistTracksDAO to store the new tracks added in the playlist.

    If the submitted tracks are not owned by the user, the request is rejected.

    If a RuntimeException occurs or if everything went fine, the user is redirected to the home page.
  ],
)

#seq_diagram(
  [Get file sequence diagram],
  diagram({
    actors(controller: "FileController", daos: ("Storage",), full: false)
    action(
      type: "GET", 
      mapping: "file/{user_id}/{playlist_name}/{cover_name}", 
      action: [serveSafeFile(\ userId, \ playlistName, \ coverName\ )]
    )
    _seq("F", "C", comment: [new UrlResource(realFile.toUri())])
    _alt(
      "File.exists()",
      {
        _seq("C", "A", comment: [return ResponseEntity.ok() \ .contentType(MediaType.APPLICATION_OCTET_STREAM) \ .body(file)])
      },
      "file not owned by user",
      {
        _seq("C", "A", comment: [return ResponseEntity\ .status(HttpStatus.FORBIDDEN)\ .build();])
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
    When the user wants to request a file he calls this method which handles the request and if every check is passed, the file is returned.
  ],
)

= Sequence diagrams RIA

== Disclaimer

Without the integration of thymeleaf with Springboot, changes occurred in the project structure. For example the login is more manually handled (see).

== SecurityContextHolder

#grid(
  columns: (auto, 1fr),
  align: horizon + left,
  [Represented by:],
  guard
)

As the name suggests, this class contains the security context, which means it contains data about the currently authenticated user. And if SecurityContextHolder holder is empty the user is considered not authenticated.

Note that this is the same class used to retrieve the userId inside the controllers.

== Basic mappings

The first 6 mappings are very basic and they all act in the same way:
+ the client creates a GET request
+ the request arrives to the correct mapping
+ the mapping obtains the requested data from a specific DAO
+ the mapping returns the obtained data to the client

For this reason there won't be any comments under them.

#pagebreak()

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
  add_comment: false,
  next_page: false,
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
  add_comment: false,
  next_page: false,
  comment_next_page_: false,
)

#pagebreak()

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
  add_comment: false,
  next_page: false,
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
  add_comment: false,
  next_page: false,
  comment_next_page_: false,
)

#pagebreak()

#seq_diagram(
  [GetTrackById sequence diagram],
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
  add_comment: false,
  next_page: false,
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
  add_comment: false,
  next_page: false,
  comment_next_page_: false,
)

#pagebreak()

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
  }),
  comment: [
    Like in the html version the user sends a post request to the /login mapping: this time its implementation is handled more manually.

    First, a token is generated from the username and password received from the request.

    The token is then verified by SecurityContextHolder which calls loadUserByUsername() implemented by the same UserDetailsService from the HTML version.

    SecurityContextHolder returns an Authentication object holding the results of the validation of the credentials.

    The next chain of calls is basically boilerplate to finally store a valid context containing the authentication, inside SecurityContextRepository. This way the authentication is effectively stored.
  ],
  label_: "login-sequence-ria",
  comment_next_page_: true,
)


#seq_diagram(
  [Logout sequence diagram],
  diagram({
    actors(full: false)

    _seq("A", "B", comment: [POST\ /logout])

  }),
  comment: [
    In order to perform a logout, Springboot requires the client to call the POST /logout mapping which internally invalidates session and all the other data associated to the user (of course the data inside the DB are preserved).
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
    The client request is redirected to AuthController by DispatcherServlet: the controller tries to save the user by calling userDAO.save() and if any kind of RuntimeException exception occurs (so SQLExceptions are also considered), a response is sent back with a 200 status. Otherwise the response is sent back with a 500 status.
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [AddPlaylist sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("playlistDAO", "playlistTracksDAO", "trackDAO"),
      full: false
    )

    _seq("A", "B", comment: [POST \ /add_playlist])
    _seq("B", "C", comment: [addPlaylist(title, selectedTracks)])
    _alt(
      "try",
      {
        _seq("C", "H", comment: [getAllByUserIdSorted(userId)])
        _seq("H", "C", comment: [userTracks])
        _alt(
          "",
          {
            _seq("C", "F", comment: [save(playlist)])
            _seq("C", "G", comment: [saveAll(playlistTracks)])
          },
          "tracks not owned by the user",
          {
            _seq("C", "A", comment: [403])
          },
        )
      },
      "catch RuntimeException",
      {
        _seq("C", "A", comment: [500])
      }
    )

  }),
  comment: [
    When the user loads a playlist, the request is handled by a method in PlaylistController which uses trackDAO, playlistDAO and playlistTracksDAO to store the data received.

    It also checks if the user owns the ids of the tracks contained in selectedTracks. If at least one of them is not owned by the user, the request is rejected.

    If a RuntimeException occurs or if everything went fine, the user is redirected to the home page.
  ],
  comment_next_page_: false,
)

#seq_diagram(
  [AddTracksPlaylist sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("playlistTracksDAO", "trackDAO", "playlistDAO"),
      full: false
    )

    _seq("A", "B", comment: [POST \ /add_tracks_to_playlist])
    _seq("B", "C", comment: [addTracksToPlaylist(\ playlistId, selectedTracks)])
    _alt(
      "",
      {
        _seq("C", "H", comment: [findByAuthorIdAndId(userId, playlistId)])
        _seq("H", "C", comment: [playlist])
        _alt(
          "",
          {
            _seq("C", "G", comment: [getAllByUserIdSorted(userId)])
            _seq("G", "C", comment: [tracks])
            _alt(
              "playlist with custom order",
              {
                _seq("C", "G", comment: [getAllInPlaylist(userId, playlistId)])
                _seq("G", "C", comment: [tracks.size() - 1])
                _note("over", pos: ("C", "G",), [See note A in description])
              },
              "playlist with normal order",
              {
                _note("over", pos: ("C", "G",), [See note B in description])
              },
            )
            _alt(
              "try",
              {
                _seq("C", "F", comment: [saveAll(\ playlistTracks)])
              },
              "catch RuntimeException",
              {
                _seq("C", "A", comment: [500])
              },
            )
          },
          "selectedTracks are not owned by the user",
          {
            _seq("C", "A", comment: [403])
          },
        )
      },
      "playlist doesn't exists",
      {
        _seq("C", "A", comment: [404])
      },
    )

  }),
  comment: [
    When the user adds tracks to a playlist, the request is handled by a method in PlaylistController which uses trackDAO, playlistDAO and playlistTracksDAO to store the data received.

    It checks if the user owns the ids of the tracks contained in selectedTracks and if he owns the playlist associated to playlistId. If at least one of them is not owned by the user, the request is rejected.

    If the playlist has a custom order, then all tracks are inserted at the end of the playlist. Otherwise they are simply saved by setting their position to 0, since when a playlist is not custom ordered, that parameter is useless.

    *Note A*: by knowing the size of a playlist is possible to create new playlistTracks by assigning to them the right position.

    *Note B*: the list of new playlistTracks is created by assigning a position of 0, since in this situation the position is not relevant.
  ],
  comment_next_page_: true,
)

#seq_diagram(
  [SetCustomOrder sequence diagram],
  diagram({

    actors(
      controller: "PlaylistController", 
      daos: ("trackDAO", "playlistDAO", "playlistTracksDAO",),
      full: false
    )

    _seq("A", "B", comment: [POST \ /set_custom_order])
    _seq("B", "C", comment: [setCustomOrder(\ playlistId, tracks\ )])
    _alt(
      "",
      {
        _seq("C", "G", comment: [findByAuthorIdAndId(userId, playlistId)])
        _seq("G", "C", comment: [playlist])
        _alt(
          "",
          {
            _seq("C", "F", comment: [getAllByUserIdSorted(\ userId\ )])
            _seq("F", "C", comment: [userTracks])
            _alt(
              "try",
              {
                _seq("C", "G", comment: [setCustomOrder(playlistId)])
                _loop(
                  "tracks.size()",
                  {
                    _seq("C", "H", comment: [updatePosition(i, tracks.get(i), playlistId)])
                  }
                )
              },
              "RuntimeException",
              {
                _seq("C", "A", comment: [500])
              },
            )
          },
          "tracks not owned by the user",
          {
            _seq("C", "A", comment: [403])
          },
        )
      },
      "playlist is not owned by the user",
      {
        _seq("C", "A", comment: [404])
      },
    )

  }),
  comment: [
    When the user reorders a playlist, the request is handled by a method in PlaylistController which uses trackDAO and playlistDAO to store the data received.

    It checks if the user owns the ids of the tracks contained in selectedTracks and if he owns the playlist associated to playlistId. If at least one of them is not owned by the user, the request is rejected.

    If everything went fine, the playlist is flagged with "custom_order" and the tracks receives.
  ],
  comment_next_page_: true,
)

#seq_diagram(
  [AddTrack sequence diagram],
  diagram({

    actors(
      controller: "TrackController", 
      daos: ("trackDAO", "Storage"),
      full: false
    )

    _seq("A", "B", comment: [POST \ /add_track])
    _seq("B", "C", comment: [addTrack()])
        _alt(
          "try",
          {
            _seq("C", "G", comment: [Files.copy(audio)])
            _seq("C", "G", comment: [Files.copy(cover)])
            _seq("C", "F", comment: [save(trackForm.toTrack())])
          },
          "catch Exception",
          {
            _seq("C", "A", comment: [500])
          },
        )

  }),
  comment: [
    When the user loads a track, the request is handled by a method in TrackController where the mime type of the loaded files is checked and if they are right, the track is also added to the database.

    If a RuntimeException occurs the server throws an error to the client.
  ],
  comment_next_page_: false,
)
