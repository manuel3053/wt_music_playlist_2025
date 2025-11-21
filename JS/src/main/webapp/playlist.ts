import { App } from "./app"
import { Component } from "./component"
// import "./static/playlist.css"

export class Playlist implements Component {
  private context: App

  constructor(context: App) {
    this.context = context
  }

  get css(): string {
    return ""
  }

  get template(): string {
    return `
    <h1>Playlist</h1>
    <p>Welcome to the playlist page!</p>
    <button id="go-to-playlist">bottone</button>
    `
  }

  buildEventListeners(): void {
  }

}
