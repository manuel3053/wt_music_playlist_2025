import { TrackRepository } from "./api.service.js";
export class Modal {
    constructor(context, playlistId) {
        this._trackRepository = new TrackRepository();
        this.context = context;
        this.playlistId = playlistId;
    }
    get css() {
        return `
    `;
    }
    get template() {
        return `
    <div id="modal" class="modal">
    </div>
    `;
    }
    save() {
    }
    build() {
        this._trackRepository.getAllTracksInPlaylist(this.playlistId).then(tracks => {
            const modal = document.getElementById("modal");
            modal.innerHTML = "";
            console.log(tracks);
            tracks.forEach(t => {
                const title = document.createElement("div");
                title.textContent = t.title;
                title.draggable = true;
                title.addEventListener("dragstart", this.dragStart);
                title.addEventListener("dragover", this.dragOver);
                title.addEventListener("dragleave", this.dragLeave);
                title.addEventListener("drop", this.drop);
                modal.append(title);
            });
        });
    }
    dragStart(event) {
        let list_item = event.target;
        list_item.style.cursor = "pointer"; // or convert to a proper class
        this.startElement = list_item;
    }
    dragOver(event) {
        event.preventDefault();
        let list_item = event.target;
        list_item.style.cursor = "grab";
    }
    dragLeave(event) {
        let list_item = event.target;
        list_item.style.cursor = "pointer";
    }
    drop(event) {
        let finalDest = event.target;
        let completeList = finalDest.closest("div");
        let songsArray = Array.from(completeList.querySelectorAll("div"));
        let indexDest = songsArray.indexOf(finalDest);
        if (songsArray.indexOf(this.startElement) < indexDest) {
            this.startElement.parentElement.insertBefore(this.startElement, songsArray[indexDest + 1]);
        }
        else {
            this.startElement.parentElement.insertBefore(this.startElement, songsArray[indexDest]);
        }
    }
}
