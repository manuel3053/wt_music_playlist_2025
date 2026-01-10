#import "../lib.typ":*

#set align(horizon)

= Abstract

/ Overview: This project hosts the source code -- which can be found #link("https://github.com/manuel3053/wt_music_playlist_2025")[on Github] -- for a web server that handles a playlist management system. A user is able to register, login and then upload tracks. The tracks are strictly associated to one user, similar to how a cloud service works. The user will be able to create playlists -- sourcing from their tracks -- and listen to them.

It should be noted there are two subprojects: a (pure) *HTML version*, which is structured as a series of separate webpages; and a *RIA version* (#ts())#footnote[For historic reasons, in the project is is referred as just `js`.], which is structured as a multi-page webapp. The functionalities are quite the same. For more information about the requirements for each version see @project-breakdown.

/ Tools: To create the project, the following technologies have been used: 
- #text(fill: rgb("#5283A2"), weight: "bold", "Java"), for the backend server with servlets leveraging the #text(fill: rgb("#ae8e26"), weight: "bold", "Springboot framework")
- #text(fill: rgb("#dcca3f"), weight: "bold")[Typescript] for for the RIA one 
- #text(fill: rgb("#005F0F"), weight: "bold", "Thymeleaf"), a template engine, for the HTML version
- #text(fill: rgb("#192C5F"), weight: "bold")[MariaDB] instead of MySQL, since it's an open source fork of MySQL

= Credits

ringrazia vittorio

// #align(
//   center,
//   link("http://localhost:8080/pure_html_war_exploded", "http://localhost:8080/[version]_war_exploded") + [: `[version]` is either `pure_html` or `js`],
// )

// The credentials are stored in plain text in the database (see @register-sequence), while the tracks and images are stored in `target/webapp` (see @uploadtrack-sequence).

// The repository is bundled with some mock data, which can be found at the root of the project in the `mockdata` folder. They are copyright free songs @ncs because we didn't want to get sued #emoji.face.cover.
