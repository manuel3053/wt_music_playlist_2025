import { PlaylistRepository, TrackRepository } from "./api.service.js"
import { App } from "./app.js"
import { Component } from "./component.js"
import { Track } from "./model/track.js"
import { TrackPage } from "./trackpage.js"

export class PlaylistPage implements Component {
  private context: App
  private playlistId: number
  private _trackRepository = new TrackRepository()
  private _playlistRepository = new PlaylistRepository()
  private _tracks: Track[] = [];

  constructor(context: App, playlistId: number) {
    this.context = context
    this.playlistId = playlistId
  }

  get css(): string {
    return `
    .playlist {
        display: grid;
        height: 100vh;
        width: 100vw;
        grid-template-columns: 1fr;
        grid-template-rows: 2fr 1fr;
        justify-content: center;
        justify-items: center;
        align-items: center;
    }

    .carousel {
        display: grid;
        grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr;
        grid-template-rows: 1fr;
        width: 100%;
        text-align: center;
        align-items: center;
    }

    .link-button {
        grid-column: span 1;
    }

    .link-button:hover {
        grid-column: span 1;
    }
    `
  }

  get template(): string {
    return `
    <div class = "playlist">
        <div id="carousel" class="carousel">
        </div>
        <form id="load-tracks" method="post" class="form">
            <div class="form-tracks-list">
                <div class="form-tracks-list-body">
                    <div></div>
                    <div>Track title</div>
                    <div>Track album</div>
                </div>
                <div class="form-tracks-list-body" id="form-tracks-list-body"></div>
            </div>
            <input class="form-button" type="submit"/>
        </form>
    </div>
    `
  }

  private getTracksNotInPlaylist(): void {
    this._trackRepository.getAllTracksNotInPlaylist(this.playlistId).then(tracks => {
      const list = document.getElementById("form-tracks-list-body")!
      list.innerHTML = ""
      tracks.forEach(t => {
        const input: HTMLInputElement = document.createElement("input")
        input.type = "checkbox"
        input.name = "selected_tracks"
        input.value = t.id.toString()
        list.append(input)
        const title: HTMLDivElement = document.createElement("div")
        title.innerText = t.title
        list.append(title)
        const album: HTMLDivElement = document.createElement("div")
        album.innerText = t.albumTitle
        list.append(album)
      })
    })
  }

  private scrollCarousel(index: number): void {
    const carousel = document.getElementById("carousel")!
    carousel.innerHTML = ""
    const placeholder: HTMLDivElement = document.createElement("div")

    if (index > 0) {
      const prev: HTMLButtonElement = document.createElement("button")
      prev.className = "link-button"
      prev.textContent = "Prev"
      prev.addEventListener(
        "click",
        () => this.scrollCarousel.bind(this)(index - 1)
      )
      carousel.append(prev)
    } else {
      carousel.append(placeholder)
    }

    this._tracks.slice(index * 5, index * 5 + 5).forEach(t => {
      const cell: HTMLDivElement = document.createElement("div")
      cell.className = "carousel-cell"
      const cover: HTMLImageElement = document.createElement("img")
      cover.className = "track-cover"
      cover.src = `/file/${t.imagePath}`
      const title: HTMLButtonElement = document.createElement("button")
      title.className = "link-button"
      title.textContent = t.title.toString()
      title.addEventListener(
        "click",
        () => this.context.currentPage = new TrackPage(this.context, t.id)
      )
      cell.append(cover)
      cell.append(title)
      carousel.append(cell)
    })

    if (this._tracks.length > 5 && (index + 1) * 5 < this._tracks.length) {
      const next: HTMLButtonElement = document.createElement("button")
      next.className = "link-button"
      next.textContent = "Next"
      next.addEventListener(
        "click",
        () => this.scrollCarousel.bind(this)(index + 1)
      )
      carousel.append(next)
    } else {
      carousel.append(placeholder)
    }

  }

  build(): void {
    this._trackRepository.getAllTracksInPlaylist(this.playlistId).then(tracks => {
      this._tracks = tracks
      this.scrollCarousel(0)
    })
    this.getTracksNotInPlaylist()

    const playlistForm = document.getElementById("load-tracks") as HTMLFormElement
    playlistForm.addEventListener('submit', (event) => {
      event.preventDefault()
      if (!playlistForm.checkValidity()) {
        alert("The form is not compiled correctly")
        return;
      }
      const formData = new FormData(playlistForm)
      formData.append("playlist_id", this.playlistId.toString())
      playlistForm.reset()
      this._playlistRepository.addTracksToPlaylist(formData)
        .then(() => {
          this.getTracksNotInPlaylist.bind(this)()
          this._trackRepository.getAllTracksInPlaylist(this.playlistId).then(tracks => {
            this._tracks = tracks
            this.scrollCarousel(0)
          })
        })
        .catch(() => alert("Failed to load playlist"))
    });

  }

}
