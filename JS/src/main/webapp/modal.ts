import { PlaylistRepository, TrackRepository } from "./api.service.js"
import { App } from "./app.js"
import { Component } from "./component.js"


export class Modal implements Component {
  private playlistId: number
  private _trackRepository = new TrackRepository()
  private _playlistRepository = new PlaylistRepository()
  private start: Element | undefined
  private end: Element | undefined
  private _orderChanged: boolean = false
  private _sortedTracksIds: string[] = []

  constructor(context: App, playlistId: number) {
    this.playlistId = playlistId
  }

  get css(): string {
    return `
    `
  }

  get template(): string {
    return `
    <div id="modal" class="modal">
    </div>
    `
  }

  save(): void {
    if (this._orderChanged) {
      const formdData = new FormData()
      formdData.append("playlist_id", this.playlistId.toString())
      formdData.append("tracks", JSON.stringify(this._sortedTracksIds))
      this._playlistRepository.setCustomOrder(formdData)
    }
  }

  build(): void {
    this._trackRepository.getAllTracksInPlaylist(this.playlistId).then(tracks => {
      const modal = document.getElementById("modal")!
      modal.innerHTML = ""
      tracks.forEach(t => {
        const title: HTMLDivElement = document.createElement("div")
        title.textContent = t.title
        title.className = "draggable"
        title.draggable = true
        title.setAttribute("id", t.id.toString())
        title.addEventListener("dragstart", this.dragStart.bind(this))
        title.addEventListener("dragover", this.dragOver.bind(this))
        title.addEventListener("drop", this.drop.bind(this))
        modal.append(title)
        this._sortedTracksIds.push(t.id.toString())
      })
    })
  }

  private dragStart(e: DragEvent): void {
    this.start = (e.target as HTMLElement).closest(".draggable")!
  }

  private dragOver(e: DragEvent): void {
    e.preventDefault()
  }

  private drop(e: DragEvent): void {
    this.end = (e.target as HTMLElement).closest(".draggable")!
    const modal = document.getElementById("modal")!
    const list: Element[] = Array.from(modal.querySelectorAll(".draggable"))
    const endPosition: number = list.indexOf(this.end)
    const startPosition: number = list.indexOf(this.start!)
    if (startPosition < endPosition) {
      this.start!.parentElement!.insertBefore(this.start!, list[endPosition + 1])
    }
    else {
      this.start!.parentElement!.insertBefore(this.start!, list[endPosition])
    }
    const tmp = this._sortedTracksIds[startPosition]
    this._sortedTracksIds[startPosition] = this.end!.getAttribute("id")!
    this._sortedTracksIds[endPosition] = tmp
    this._orderChanged = true
  }

}
