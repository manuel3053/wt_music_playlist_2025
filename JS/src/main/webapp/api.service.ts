import { User } from "./model/user"

export class ApiService {

  createRequest(address: string, body?: {}): Request {
    const headers: Headers = new Headers()
    headers.set('Content-Type', 'application/json')

    return body === undefined
      ? new Request(address, { headers: headers, method: 'GET' })
      : new Request(address, { headers: headers, method: 'POST', body: JSON.stringify(body) })
  }

  async get<T extends object>(className: new () => T, address: string, body?: {}): Promise<T> {
    return fetch(this.createRequest(address, body))
      .then(async response => {
        return response.json().then(json => Object.assign(new className(), json))
      })
  }

  async getPrimitive<T extends string | number | boolean>(address: string, body?: {}): Promise<T> {
    return fetch(this.createRequest(address, body))
      .then(async response => {
        console.log(response);

        return response.json().then(json => {
          const value: T = json;
          return value
        })
      })
  }

  async getList<T extends object>(className: new () => T, address: string, body?: {}): Promise<T[]> {
    return fetch(this.createRequest(address, body))
      .then(async response => {
        return response.json().then(json => {
          return (json as any[]).map(j => Object.assign(new className(), j))
        })
      })
  }

  async call(address: string, body?: {}): Promise<void> {
    return fetch(this.createRequest(address, body)).then()
  }

}

export class Repository {

  public _apiService: ApiService = new ApiService()
  public _url: string = "http://localhost:8080/"
  public _restController: string

  constructor(restController: string) {
    this._restController = restController
  }

  public toFullPath(endpoint: string) {
    return `${this._url}${this._restController}/${endpoint}`
  }

}

export class ApiRepository extends Repository {

  getUser(): Promise<User> {
    return this._apiService.get<User>(User, "http://localhost:8080/login/test")
  }

}

export class PlaylistRepository extends Repository {

  constructor() {
    super("playlist")
  }

  getPlaylistSizeById(id: number): Promise<number> {
    return this._apiService.getPrimitive<number>(this.toFullPath(`get_playlist_size_by_id?id=${id}`))
  }

}
export class UserRepository extends Repository {

  constructor() {
    super("user")
  }

  login(username: string, password: string): Promise<User> {
    return this._apiService.get<User>(User, this.toFullPath("login"), { username, password })
  }

  subscribe(user: User): Promise<void> {
    return this._apiService.call(this.toFullPath("subscribe"), user)
  }

}
