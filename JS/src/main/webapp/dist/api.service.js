var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { Playlist } from "./model/playlist.js";
import { Track } from "./model/track.js";
export class ErrorResponse {
    constructor(response) {
        this._response = response;
    }
    get status() {
        return this._response.status;
    }
    get statusText() {
        return this._response.statusText;
    }
}
export class ApiService {
    createRequest(address, body) {
        const headers = new Headers();
        headers.set('Content-Type', 'application/json');
        return body === undefined
            ? new Request(address, { headers: headers, method: 'GET' })
            : new Request(address, { headers: headers, method: 'POST', body: JSON.stringify(body) });
    }
    checkError(response) {
        if (!response.ok) {
            throw new ErrorResponse(response);
        }
    }
    getPrimitive(address, body) {
        return __awaiter(this, void 0, void 0, function* () {
            return fetch(this.createRequest(address, body))
                .then((response) => __awaiter(this, void 0, void 0, function* () {
                this.checkError(response);
                return response.json().then(json => {
                    const value = json;
                    return value;
                });
            }));
        });
    }
    getPrimitivesList(address, body) {
        return __awaiter(this, void 0, void 0, function* () {
            return fetch(this.createRequest(address, body))
                .then((response) => __awaiter(this, void 0, void 0, function* () {
                this.checkError(response);
                return response.json().then(json => {
                    return json.map(j => {
                        const value = j;
                        return value;
                    });
                });
            }));
        });
    }
    get(className, address, body) {
        return __awaiter(this, void 0, void 0, function* () {
            return fetch(this.createRequest(address, body))
                .then((response) => __awaiter(this, void 0, void 0, function* () {
                this.checkError(response);
                return response.json().then(json => {
                    return Object.assign(new className(), json);
                });
            }));
        });
    }
    getList(className, address, body) {
        return __awaiter(this, void 0, void 0, function* () {
            return fetch(this.createRequest(address, body))
                .then((response) => __awaiter(this, void 0, void 0, function* () {
                this.checkError(response);
                return response.json().then(json => {
                    return json.map(j => Object.assign(new className(), j));
                });
            }));
        });
    }
    call(address, body) {
        return __awaiter(this, void 0, void 0, function* () {
            return fetch(this.createRequest(address, body)).then((response) => __awaiter(this, void 0, void 0, function* () {
                this.checkError(response);
            }));
        });
    }
    submit(address, form) {
        return __awaiter(this, void 0, void 0, function* () {
            const headers = new Headers();
            const response = yield fetch(new Request(address, { headers: headers, method: 'POST', body: form }));
            this.checkError(response);
        });
    }
}
export class ApiRepository {
    constructor() {
        this._apiService = new ApiService();
        this._url = "http://localhost:8080/";
    }
    toFullPath(endpoint) {
        return `${this._url}${endpoint}`;
    }
}
export class PlaylistRepository extends ApiRepository {
    addTracksToPlaylist(form) {
        return this._apiService.submit(this.toFullPath(`add_tracks_to_playlist`), form);
    }
    createPlaylist(form) {
        return this._apiService.submit(this.toFullPath(`add_playlist`), form);
    }
    getPlaylists() {
        return this._apiService.getList(Playlist, this.toFullPath("get_playlists"));
    }
    setCustomOrder(form) {
        return this._apiService.submit(this.toFullPath("set_custom_order"), form);
    }
}
export class TrackRepository extends ApiRepository {
    createTrack(form) {
        return this._apiService.submit(this.toFullPath(`add_track`), form);
    }
    getTrackById(trackId) {
        return this._apiService.get(Track, this.toFullPath(`get_track_by_id/${trackId}`));
    }
    getAllTracksNotInPlaylist(playlistId) {
        return this._apiService.getList(Track, this.toFullPath(`get_all_not_in_playlist/${playlistId}`));
    }
    getAllTracksInPlaylist(playlistId) {
        return this._apiService.getList(Track, this.toFullPath(`get_all_in_playlist/${playlistId}`));
    }
    getTracks() {
        return this._apiService.getList(Track, this.toFullPath("get_tracks"));
    }
    getGenres() {
        return this._apiService.getPrimitivesList(this.toFullPath("get_genres"));
    }
}
export class AuthRepository extends ApiRepository {
    login(form) {
        return this._apiService.submit(this.toFullPath("login"), form);
    }
    logout() {
        return this._apiService.call(this.toFullPath("logout"), {});
    }
    subscribe(form) {
        return this._apiService.submit(this.toFullPath("subscribe"), form);
    }
}
