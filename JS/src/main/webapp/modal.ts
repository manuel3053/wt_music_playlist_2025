import { PlaylistRepository, TrackRepository } from "./api.service.js"
import { App } from "./app.js"
import { Component } from "./component.js"


export class Modal implements Component {
  private context: App
  private playlistId: number
  private _trackRepository = new TrackRepository()
  private startElement: HTMLDivElement | undefined;

  constructor(context: App, playlistId: number) {
    this.context = context
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
  }

  build(): void {
    this._trackRepository.getAllTracksInPlaylist(this.playlistId).then(tracks => {
      const modal = document.getElementById("modal")!
      modal.innerHTML = ""
      console.log(tracks);

      tracks.forEach(t => {
        const title: HTMLDivElement = document.createElement("div")
        title.textContent = t.title
        title.draggable = true
        title.addEventListener("dragstart", this.dragStart);
        title.addEventListener("dragover", this.dragOver);
        title.addEventListener("dragleave", this.dragLeave);
        title.addEventListener("drop", this.drop);
        modal.append(title)
      })
    })
  }

  dragStart(event: Event) {
    let list_item: HTMLDivElement = event.target as HTMLDivElement;
    list_item.style.cursor = "pointer"; // or convert to a proper class
    this.startElement = list_item;
  }

  dragOver(event: Event) {
    event.preventDefault()
    let list_item: HTMLDivElement = event.target as HTMLDivElement;
    list_item.style.cursor = "grab";
  }

  dragLeave(event: Event) {
    let list_item: HTMLDivElement = event.target as HTMLDivElement;
    list_item.style.cursor = "pointer";
  }

  drop(event: Event) {
    let finalDest: HTMLDivElement = event.target as HTMLDivElement;

    let completeList: HTMLDivElement = finalDest.closest("div")!;
    let songsArray: HTMLDivElement[] = Array.from(completeList.querySelectorAll("div"));

    let indexDest: number = songsArray.indexOf(finalDest);

    if (songsArray.indexOf(this.startElement!) < indexDest) {
      this.startElement!.parentElement!.insertBefore(
        this.startElement!,
        songsArray[indexDest + 1],
      );
    } else {
      this.startElement!.parentElement!.insertBefore(
        this.startElement!,
        songsArray[indexDest],
      );
    }
  }

}
