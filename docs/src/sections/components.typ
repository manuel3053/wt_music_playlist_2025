#import "../lib.typ": *

= Codebase overview

#show: table-styles.with(header-height: 1)

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

== RIA subproject

The frontend was built by using the oop capabilities of Typescript.

== Views

The views are represented by the following classes:
- LoginPage: the user can login in the app or go to the subscribe page
- SubscribePage: the user can subscribe
- HomePage: the user can load a track, create a playlist, open a playlist or open the modal to sort the playlist
- PlaylistPage: the user can add tracks to the playlist or open a track
- TrackPage: the user can see data about the track and listen to it
- Header: contains the logout and go to previous page buttons
- Modal: shows the tracks in the playlist and the user can sort them

The views are backed by two html files:
- index.html: is used by LoginPage and SubscribePage
- app.html: is used by all the others views

All the views, apart from LoginPage, implement a Component interface with the following methods:
- css: contains the styling specific to that page
- template: contains the static part of a page, in order to ease the creation of a page, instead of using only Typescript
- build: contains the code that builds the dynamic part of the page

This way the code is more organized.

== Repositories

A very basic implementation of the repository pattern is implemented in the client, just to centralize the management of the api calls.

The repositories are:
- AuthRepository
- TrackRepository
- PlaylistRepository

As you can see they are a direct transposition of the controllers from the backend.

When a view has to make a call to the server, it instantiate the repository and uses its implementation, instead of writing each time the same code.

== Actions

The possible actions are described in the following tables.

#pagebreak()

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

    [Index $=>$ Login form $=>$ Submit], 
    [Data validation], 
    [POST (`username`, `password`)], 
    [Credentials check],

    [HomePage $=>$ Load], 
    [Loads all User playlists and tracks], 
    [GET], 
    [Queries user playlists],

    [HomePage $=>$ Click on a playlist],
    [Loads PlaylistPage for that Playlist],
    [GET], 
    [Queries the tracks associated to the given playlist],

    [HomePage $=>$ Click on sort button],
    [Load a modal to custom order the tracks in the Playlist],
    [GET], 
    [Queries the tracks associated to the given playlist],

    [HomePage $=>$ Close button],
    [Saves the custom order to the database],
    [POST (`trackIds, playlistId`)],
    [Updates the `playlist_tracks` table with the new custom order],

    [HomePage $=>$ add playlist form $=>$ Submit],
    [Data validation],
    [POST (`playlistTitle`, `selectedTracks`)],
    [Inserts the new Playlist in the `playlist` table],

    [HomePage $=>$ add track form $=>$ Submit],
    [Data validation],
    [POST (`title`, `artist`, `year`, `album`, `genre`, `cover`, `track`)],
    [Inserts the new Track in the `tracks` table],

    [PlaylistPage $=>$ add tracks to playlist form $=>$ Submit],
    [Data validation],
    [POST (`playlistId`, `selectedTracks`)],
    [Inserts the selected tracks in the given playlist],

    [PlaylistPage $=>$ Click on a track],
    [Loads TrackPage for that Track],
    [GET],
    [Queries the requested track],

    [Logout], 
    [Invalidates the current User session], 
    [GET], 
    [Session invalidation],

    [Prev], 
    [Loads previous page], 
    [-], 
    [-],
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

    [Index $=>$ Login form $=>$ Submit], 
    [AuthRepository\ .login()], 
    [POST (`username`, `password`)], 
    [AuthController],

    [HomePage $=>$ Load], 
    [PlaylistRepository\ .getPlaylists()\ TrackController\ .getTracks()], 
    [GET], 
    [PlaylistController and TrackController],

    [HomePage $=>$ Click on a playlist],
    [TrackRepository\ .getAllTracksInPlaylist()], 
    [GET], 
    [TrackController],

    [HomePage $=>$ Click on sort button],
    [TrackRepository\ .getAllTracksInPlaylist()], 
    [GET], 
    [TrackController],

    [HomePage $=>$ Close button],
    [PlaylistRepository\ .setCustomOrder()], 
    [POST (`trackIds, playlistId`)],
    [PlaylistController],

    [HomePage $=>$ add playlist form $=>$ Submit],
    [PlaylistRepository\ .createPlaylist()], 
    [POST (`playlistTitle`, `selectedTracks`)],
    [PlaylistController],

    [HomePage $=>$ add track form $=>$ Submit],
    [TrackRepository\ .createTrack()], 
    [POST (`title`, `artist`, `year`, `album`, `genre`, `cover`, `track`)],
    [TrackController],

    [PlaylistPage $=>$ add tracks to playlist form $=>$ Submit],
    [PlaylistRepository\ .addTracksToPlaylist()], 
    [POST (`playlistId`, `selectedTracks`)],
    [PlaylistController],

    [PlaylistPage $=>$ Click on a track],
    [TrackRepository\ .getTrackById()], 
    [GET],
    [TrackController],

    [Logout], 
    [AuthRepository\ .logout()], 
    [GET], 
    [Springboot internal LogoutController],

    [Prev], 
    [App.pop()], 
    [-], 
    [-],
  ),
  caption: [Events & Controllers (or event handlers).],
)

= SecurityConfig

Instead of manually creating filters to block mappings to an anauthenticated user, it's possible to use the system of filters provided by Springboot.

The version for the *HTML* subproject is the simplest:
- the requests that can always be accessed are /login and /subscribe (also the stylesheets are always available)
- all the other requests requires the user to be authenticated
- the login can be performed by making a POST request to /login and if successful, the user is redirected to /home
- the logout can be performed by making a POST request to /logout

The version for the *RIA* subproject is more complex because the login process is handled differently (because there isn't thymeleaf to help):
- the requests that can always be accessed are /login and /subscribe
- also the resources folder containing the scripts and the html files using those scripts, are always available
- all the other requests requires the user to be authenticated
- the logout can be performed by making a POST request to /logout and in case of success the user is redirect to /index.html

Since the login process is more complex, it also requires to use an AuthenticationManager and a SecurityContextRepository: by declaring them here, later they can be injected in the login function (see @login-sequence-ria).

#figure(
  placement: bottom,
  scope: "parent",
  [
    ```java
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {


        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
                    .authorizeHttpRequests(registry -> registry
                            .dispatcherTypeMatchers(
                              DispatcherType.FORWARD, 
                              DispatcherType.ERROR
                            ).permitAll()
                            .requestMatchers("/login", "/subscribe", "/css/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(httpForm -> httpForm
                            .loginPage("/login")
                            .defaultSuccessUrl("/home")
                            .permitAll()
                    )
                    .logout(logout -> logout.permitAll());

            return http.build();
        }

    }
    ```
  ],
  caption: "Springboot security configuration HTML",
)<security-html>

#figure(
  placement: top,
  scope: "parent",
  [
    ```java
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeHttpRequests(registry -> registry
                            .dispatcherTypeMatchers(
                              DispatcherType.FORWARD, 
                              DispatcherType.ERROR
                            ).permitAll()
                            .requestMatchers("/login", "/subscribe", "/static/**", "/favicon.ico", "/dist/**",
                                    "/index.html", "/app.html")
                            .permitAll()
                            .anyRequest().authenticated())
                    .csrf(csrf -> csrf.disable())
                    .logout(l -> l
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/index.html")
                            .invalidateHttpSession(true)
                            .permitAll())
                    .build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        public SecurityContextRepository securityContextRepository() {
            return new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(),
                    new HttpSessionSecurityContextRepository());
        }

    }
    ```
  ],
  caption: "Springboot security configuration RIA",
)<security-ria>

