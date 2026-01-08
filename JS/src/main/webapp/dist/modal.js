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
        // se delle tracce sono state spostate:
        // - segna la playlist con custom_order
        // - segna nelle tracce la posizione
    }
    build() {
        this._trackRepository.getAllTracksInPlaylist(this.playlistId).then(tracks => {
            const modal = document.getElementById("modal");
            modal.innerHTML = "";
            tracks.forEach((t, index) => {
                const title = document.createElement("div");
                title.textContent = t.title;
                title.className = "draggable";
                title.setAttribute('position', index.toString());
                title.draggable = true;
                title.addEventListener("dragstart", this.dragStart.bind(this));
                title.addEventListener("dragover", this.dragOver.bind(this));
                title.addEventListener("drop", this.drop.bind(this));
                modal.append(title);
            });
        });
    }
    dragStart(e) {
        this.start = e.target.closest(".draggable");
    }
    dragOver(e) {
        e.preventDefault();
    }
    drop(e) {
        this.dest = e.target.closest(".draggable");
        const modal = document.getElementById("modal");
        const list = Array.from(modal.querySelectorAll(".draggable"));
        const position = list.indexOf(this.dest);
        if (list.indexOf(this.start) < position) {
            this.start.parentElement.insertBefore(this.start, list[position + 1]);
        }
        else {
            this.start.parentElement.insertBefore(this.start, list[position]);
        }
    }
}
