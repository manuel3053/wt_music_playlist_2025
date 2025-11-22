import { ApiRepository, PlaylistRepository, UserRepository } from "./api.service"
import { User } from "./model/user"
import "./static/style.css"

class Login {
  private _userRepository: UserRepository = new UserRepository()
  private _playlistRepository: PlaylistRepository = new PlaylistRepository()

  constructor() {
    document.getElementById("test-button")?.addEventListener(
      "click",
      () => {
        let user: User = new User()
        user.name = "nuovo"
        user.surname = "nuovo"
        user.username = "nuovo"
        user.password = "nuovo"
        this._playlistRepository.getPlaylistSizeById(6).then(size => console.log(size))
        this._userRepository.login("s", "s").then(user => {
          document.getElementById("test-button")!.innerText = user.username
        })
      }
    )
  }

}

document.addEventListener('DOMContentLoaded', () => new Login())
