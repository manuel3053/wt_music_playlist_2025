import { App } from "./app"
import { Component } from "./component"
import { Playlist } from "./playlist"

export class HomePage implements Component {
  private context: App

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
    `
  }

  get template(): string {
    return `
    <div class="home">
      <div class="playlist-list">
          <form class="form-playlist-list">
              <div class="playlist-list-body">
                  <div>Track title</div>
                  <div>Track album</div>
              </div>
              <div class="playlist-list-body">
                  <a class="link-button"></a>
                  <span></span>
              </div>
          </form>
      </div>
      <div class="load-track">
          <form method="post" enctype="multipart/form-data" class="form">
              <label class="form-field-name">Title</label>
              <input class="form-field" type="text" th:field="*{title}" placeholder="Title" required>
              <label class="form-field-name">File path</label>
              <input class="form-field-file" name="audio" type="file" accept="audio/*" th:field="*{file}" placeholder="File Path" required>
              <label class="form-field-name">Image path</label>
              <input class="form-field-file" name="image" type="file" accept="image/*" th:field="*{image}" placeholder="Image Path" required>
              <label class="form-field-name">Author</label>
              <input class="form-field" type="text" th:field="*{author}" placeholder="Author" required>
              <label class="form-field-name">Album title</label>
              <input class="form-field" type="text" th:field="*{albumTitle}" placeholder="Album Title" required>
              <label class="form-field-name">Album publication year</label>
              <select class="form-field" th:field="*{albumPublicationYear}">
                  <option></option>
              </select>
              <label class="form-field-name">Genre</label>
              <select class="form-field" th:field="*{genre}">
                  <option></option>
              </select>
              <input class="form-button" type="submit"/>
          </form>
      </div>
      <div>
          <form method="post" class="form">
              <label class="form-field-name">Title</label>
              <input class="form-field" type="text" th:field="*{title}" placeholder="Title" required>
              <div class="form-tracks-list">
                  <div class="form-tracks-list-body">
                      <div></div>
                      <div>Track title</div>
                      <div>Track album</div>
                  </div>
                  <div class="form-tracks-list-body">
                          <input type="checkbox">
                  </div>
              </div>
              <input class="form-button" type="submit"/>
          </form>
      </div>
    </div>
      `
  }

  buildEventListeners(): void {
    document.getElementById("go-to-playlist")?.addEventListener(
      "click",
      () => { this.context.currentPage = new Playlist(this.context) }
    )
  }

}
