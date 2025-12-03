import { Playlist } from "./model/playlist.js"
import { Track } from "./model/track.js"
import { User } from "./model/user.js"

export class ErrorResponse {
  private _response: Response

  public constructor(response: Response) {
    this._response = response
  }

  get status(): number {
    return this._response.status
  }

  get statusText(): string {
    return this._response.statusText
  }

}

export class ApiService {

  private buildHeaders(): Headers {
    const headers: Headers = new Headers()
    return headers
  }

  private createRequest(address: string, body?: {}): Request {
    const headers: Headers = this.buildHeaders()
    headers.set('Content-Type', 'application/json')

    return body === undefined
      ? new Request(address, { headers: headers, method: 'GET' })
      : new Request(address, { headers: headers, method: 'POST', body: JSON.stringify(body) })
  }

  private checkError(response: Response): void {
    if (!response.ok) {
      throw new ErrorResponse(response)
    }
  }

  async getPrimitive<T extends string | number | boolean>(address: string, body?: {}): Promise<T> {
    return fetch(this.createRequest(address, body))
      .then(async response => {
        this.checkError(response)
        return response.json().then(json => {
          const value: T = json;
          return value
        })
      })
  }

  async getPrimitivesList<T extends string | number | boolean>(address: string, body?: {}): Promise<T[]> {
    return fetch(this.createRequest(address, body))
      .then(async response => {
        this.checkError(response)
        return response.json().then(json => {
          return (json as any[]).map(j => {
            const value: T = j;
            return value
          })
        })
      })
  }

  async get<T extends object>(className: new () => T, address: string, body?: {}): Promise<T> {
    return fetch(this.createRequest(address, body))
      .then(async response => {
        this.checkError(response)
        return response.json().then(json => {
          return Object.assign(new className(), json)
        })
      })
  }

  async getList<T extends object>(className: new () => T, address: string, body?: {}): Promise<T[]> {
    return fetch(this.createRequest(address, body))
      .then(async response => {
        this.checkError(response)
        return response.json().then(json => {
          return (json as any[]).map(j => Object.assign(new className(), j))
        })
      })
  }

  async call(address: string, body?: {}): Promise<void> {
    return fetch(this.createRequest(address, body)).then(async response => {
      this.checkError(response)
    })
  }

  async submit(address: string, form: FormData): Promise<void> {
    const headers: Headers = new Headers()
    const response = await fetch(new Request(address, { headers: headers, method: 'POST', body: form }))
    this.checkError(response)
  }

}

export class ApiRepository {

  public _apiService: ApiService = new ApiService()
  public _url: string = "http://localhost:8080/"

  constructor() {
  }

  public toFullPath(endpoint: string) {
    return `${this._url}${endpoint}`
  }

}

export class PlaylistRepository extends ApiRepository {

  getPlaylistSizeById(id: number): Promise<number> {
    return this._apiService.getPrimitive<number>(this.toFullPath(`get_playlist_size_by_id?id=${id}`))
  }

  addTracksToPlaylist(form: FormData): Promise<void> {
    return this._apiService.submit(this.toFullPath(`add_tracks_to_playlist`), form)
  }

  createPlaylist(form: FormData): Promise<void> {
    return this._apiService.submit(this.toFullPath(`add_playlist`), form)
  }

  getPlaylists(): Promise<Playlist[]> {
    return this._apiService.getList<Playlist>(Playlist, this.toFullPath("get_playlists"))
  }


}

export class TrackRepository extends ApiRepository {

  createTrack(form: FormData): Promise<void> {
    return this._apiService.submit(this.toFullPath(`add_track`), form)
  }

  getAllTracksSorted(): Promise<Track[]> {
    return this._apiService.getList<Track>(Track, this.toFullPath(`get_all_tracks_sorted`))
  }

  getTrackById(trackId: number): Promise<Track> {
    return this._apiService.get<Track>(Track, this.toFullPath(`get_track_by_id/${trackId}`)
    )
  }

  getAllTracksNotInPlaylist(playlistId: number): Promise<Track[]> {
    return this._apiService.getList<Track>(
      Track,
      this.toFullPath(`get_all_not_in_playlist/${playlistId}`)
    )
  }

  getAllTracksInPlaylist(playlistId: number): Promise<Track[]> {
    return this._apiService.getList<Track>(
      Track,
      this.toFullPath(`get_all_in_playlist/${playlistId}`)
    )
  }

  getTracks(): Promise<Track[]> {
    return this._apiService.getList<Track>(Track, this.toFullPath("get_tracks"))
  }

  getGenres(): Promise<string[]> {
    return this._apiService.getPrimitivesList<string>(this.toFullPath("get_genres"))
  }


}

export class AuthRepository extends ApiRepository {

  login(form: FormData): Promise<void> {
    return this._apiService.submit(this.toFullPath("login"), form)
  }

  logout(): Promise<void> {
    return this._apiService.call(this.toFullPath("logout"), {})
  }

  test(): Promise<User> {
    return this._apiService.get<User>(User, this.toFullPath("test"))
  }

  testone(): Promise<User> {
    return this._apiService.get<User>(User, this.toFullPath("testone"))
  }

  subscribe(form: FormData): Promise<void> {
    return this._apiService.submit(this.toFullPath("subscribe"), form)
  }

}
