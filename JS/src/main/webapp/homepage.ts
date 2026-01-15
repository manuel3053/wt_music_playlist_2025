import { PlaylistRepository, TrackRepository } from "./api.service.js"
import { App } from "./app.js"
import { Component } from "./component.js"
import { Modal } from "./modal.js"
import { PlaylistPage } from "./playlistpage.js"

export class HomePage implements Component {
  private context: App
  private _trackRepository = new TrackRepository()
  private _playlistRepository = new PlaylistRepository()
  private _modal: Modal | undefined

  constructor(context: App) {
    this.context = context
  }

  get css(): string {
    return `
      .home {
          display: grid;
          grid-template-columns: 1fr 1fr;
          grid-template-rows: 2fr 1fr;
          height: 100vh;
          width: 100vw;
          margin: 0;
          padding: 0;
      }

      .playlist-list {
          grid-row: span 2;
      }

      .load-track, .load-playlist, .playlist-list {
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
      }

      .modal-area {
        position: absolute;
        visibility: hidden;
        top: 30vh;
        left: 40vw;
        min-width: 30vw;
        min-height: 10vh;
        background-color: black;
        border: 2px solid yellow;
        border-radius: var(--border-radius);
      }
    `
  }

  get template(): string {
    return `
    <div class="modal-area" id="modal-area"></div>
    <div class="home">
      <div class="playlist-list">
          <div "class="form-playlist-list">
              <div class="playlist-list-body">
                  <div>Sort</div>
                  <div>Track title</div>
                  <div>Track album</div>
              </div>
              <div class="playlist-list-body" id="playlist-list-body"></div>
          </div>
      </div>
      <div class="load-track">
          <form id="load-track" method="post" class="form">
              <label class="form-field-name">Title</label>
              <input class="form-field" name="title" type="text" placeholder="Title" required>
              <label class="form-field-name">File path</label>
              <input class="form-field-file" name="file" type="file" accept="audio/*" placeholder="File Path" required>
              <label class="form-field-name">Image path</label>
              <input class="form-field-file" name="image" type="file" accept="image/*" placeholder="Image Path" required>
              <label class="form-field-name">Author</label>
              <input class="form-field" name="author" type="text" placeholder="Author" required>
              <label class="form-field-name">Album title</label>
              <input class="form-field" name="album_title" type="text" placeholder="Album Title" required>
              <label class="form-field-name">Album publication year</label>
              <select id="years" class="form-field" name="album_publication_year"></select>
              <label class="form-field-name">Genre</label>
              <select id="genres" class="form-field" name="genre"></select>
              <input class="form-button" type="submit"/>
          </form>
      </div>
      <div>
          <form id="load-playlist" method="post" class="form">
              <label class="form-field-name">Title</label>
              <input class="form-field" name="title" type="text" placeholder="Title" required>
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
    </div>
      `
  }

  private getTracks(): void {
    this._trackRepository.getTracks().then(tracks => {
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
    }).catch(App.goToLogin)
  }

  private getPlaylists(): void {
    this._playlistRepository.getPlaylists().then(playlists => {
      const list = document.getElementById("playlist-list-body")!
      list.innerHTML = ""
      playlists.forEach(p => {
        const sort: HTMLButtonElement = document.createElement("button")
        sort.className = "link-button"
        sort.textContent = "Sort"
        sort.addEventListener(
          "click",
          () => {
            const modalArea = document.getElementById("modal-area")
            if (this._modal === undefined) {
              this._modal = new Modal(this.context, p.id)
              modalArea!.innerHTML = this._modal.template
              this._modal.build()
              sort.textContent = "Close"
              modalArea!.style.visibility = "visible"
            } else {
              modalArea!.innerHTML = ""
              this._modal.save()
              this._modal = undefined
              sort.textContent = "Sort"
              modalArea!.style.visibility = "hidden"
            }

          }
        )
        list.append(sort)
        const title: HTMLButtonElement = document.createElement("button")
        title.className = "link-button"
        title.textContent = p.title.toString()
        title.addEventListener(
          "click",
          () => this.context.currentPage = new PlaylistPage(this.context, p.id)
        )
        list.append(title)
        const date: HTMLSpanElement = document.createElement("span")
        date.innerText = p.creationDate.split("T")[0]
        list.append(date)
      })
    })
    // .catch(App.goToLogin)
  }

  build(): void {

    this.getTracks()
    this.getPlaylists()

    this._trackRepository.getGenres().then(genres => {
      const list = document.getElementById("genres")
      genres.forEach(g => {
        const option: HTMLOptionElement = document.createElement("option")
        option.value = g
        option.text = g
        list?.append(option)
      })
    })
    // .catch(App.goToLogin)

    const list = document.getElementById("years")
    for (let index = 1900; index < new Date().getFullYear(); index++) {
      const option: HTMLOptionElement = document.createElement("option")
      option.value = index.toString()
      option.text = index.toString()
      list?.append(option)
    }

    const trackForm = document.getElementById("load-track") as HTMLFormElement
    trackForm.addEventListener('submit', (event) => {
      event.preventDefault()
      if (!trackForm.checkValidity()) {
        alert("The form is not compiled correctly")
        return;
      }
      const formData = new FormData(trackForm)
      trackForm.reset()
      this._trackRepository.createTrack(formData)
        .then(this.getTracks.bind(this))
        .catch(console.log)
    });

    const playlistForm = document.getElementById("load-playlist") as HTMLFormElement
    playlistForm.addEventListener('submit', (event) => {
      event.preventDefault()
      if (!playlistForm.checkValidity()) {
        alert("The form is not compiled correctly")
        return;
      }
      const formData = new FormData(playlistForm)
      playlistForm.reset()
      this._playlistRepository.createPlaylist(formData)
        .then(this.getPlaylists.bind(this))
        .catch(() => alert("Failed to load playlist"))
    });

  }

}
