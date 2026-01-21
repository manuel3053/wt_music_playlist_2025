import { TrackRepository } from "./api.service.js"
import { App } from "./app.js"
import { Component } from "./component.js"

export class TrackPage implements Component {
  private context: App
  private trackId: number
  private _trackRepository = new TrackRepository()

  constructor(context: App, trackId: number) {
    this.context = context
    this.trackId = trackId
  }

  get css(): string {
    return `
      .track {
          display: grid;
          height: 100vh;
          width: 100vw;
          justify-content: center;
          align-items: center;
      }

      .track-content {
          display: grid;
          grid-template-columns: 1fr 1fr 1fr;
          grid-template-rows: repeat(6, 1fr);
          column-gap: 20px;
          height: 50vh;
          width: 90vw;
          margin: 0;
          padding: 0;
      }

      .album-cover {
          display: flex;
          grid-row: span 5;
      }

      .attribute {
          display: flex;
      }

      .attribute-name {
          display: flex;
          font-weight: bold;
      }

      .link-button {
          grid-column: span 3;
      }
    `
  }

  get template(): string {
    return `
    <div class = "track">
        <div class="track-content">
            <div class="album-cover">
                <img id="track-cover" class="track-cover"/>
            </div>
            <div class="attribute-name">Title:</div>
            <div id="title" class="attribute"></div>
            <div class="attribute-name">Author:</div>
            <div id="author" class="attribute"></div>
            <div class="attribute-name">Album title:</div>
            <div id="album_title" class="attribute"></div>
            <div class="attribute-name">Publication year:</div>
            <div id="publication_year" class="attribute"></div>
            <div class="attribute-name">Genre: </div>
            <div id="genre" class="attribute"></div>
        </div>
    </div>
    `
  }

  build(): void {
    this._trackRepository.getTrackById(this.trackId).then((track) => {
      // const source: HTMLSourceElement = document.getElementById("s")! as HTMLSourceElement
      // source.src = `/file/${track.filePath}`
      const audio: HTMLAudioElement = document.createElement("audio")
      audio.controls = true
      const source: HTMLSourceElement = document.createElement("source")
      source.src = `/file/${track.filePath}`

      audio.appendChild(source)
      document.getElementsByClassName("track-content")[0].appendChild(audio)

      const cover: HTMLImageElement = document.getElementById("track-cover")! as HTMLImageElement
      cover.src = `/file/${track.imagePath}`
      document.getElementById("title")!.textContent = track.title
      document.getElementById("author")!.textContent = track.author
      document.getElementById("album_title")!.textContent = track.albumTitle
      document.getElementById("publication_year")!.textContent = track.albumPublicationYear.toString()
      document.getElementById("genre")!.textContent = track.genre
    })
  }

}
