import { PlaylistRepository, TrackRepository } from "./api.service.js";
export class Modal {
    constructor(context, playlistId) {
        this._trackRepository = new TrackRepository();
        this._playlistRepository = new PlaylistRepository();
        this._orderChanged = false;
        this._sortedTracksIds = [];
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
        if (this._orderChanged) {
            const formdData = new FormData();
            formdData.append("playlist_id", this.playlistId.toString());
            console.log(this._sortedTracksIds);
            formdData.append("tracks", JSON.stringify(this._sortedTracksIds.map(Number)));
            this._playlistRepository.setCustomOrder(formdData).catch(console.log);
        }
    }
    build() {
        this._trackRepository.getAllTracksInPlaylist(this.playlistId).then(tracks => {
            const modal = document.getElementById("modal");
            modal.innerHTML = "";
            tracks.forEach(t => {
                const title = document.createElement("div");
                title.textContent = t.title;
                title.className = "draggable";
                title.draggable = true;
                title.setAttribute("id", t.id.toString());
                title.addEventListener("dragstart", this.dragStart.bind(this));
                title.addEventListener("dragover", this.dragOver.bind(this));
                title.addEventListener("drop", this.drop.bind(this));
                modal.append(title);
                this._sortedTracksIds.push(t.id.toString());
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
        this.end = e.target.closest(".draggable");
        const modal = document.getElementById("modal");
        const list = Array.from(modal.querySelectorAll(".draggable"));
        const endPosition = list.indexOf(this.end);
        const startPosition = list.indexOf(this.start);
        if (startPosition < endPosition) {
            this.start.parentElement.insertBefore(this.start, list[endPosition + 1]);
        }
        else {
            this.start.parentElement.insertBefore(this.start, list[endPosition]);
        }
        const tmp = this._sortedTracksIds[startPosition];
        this._sortedTracksIds[startPosition] = this.end.getAttribute("id");
        this._sortedTracksIds[endPosition] = tmp;
        this._orderChanged = true;
    }
}
