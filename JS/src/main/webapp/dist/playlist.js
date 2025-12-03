// import "./static/playlist.css"
export class PlaylistPage {
    constructor(context, playlistId) {
        this.context = context;
        this.playlistId = playlistId;
    }
    get css() {
        return "";
    }
    get template() {
        return `
    <h1>Playlist</h1>
    <p>Welcome to the playlist page!</p>
    <button id="go-to-playlist">bottone</button>
    `;
    }
    build() {
    }
}
