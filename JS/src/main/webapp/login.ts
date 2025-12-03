import { AuthRepository } from "./api.service.js"

class Login {
  private _authRepository: AuthRepository = new AuthRepository()

  constructor() {

    const form = document.getElementById("form") as HTMLFormElement

    form.addEventListener('submit', (event) => {
      event.preventDefault()
      const formData = new FormData(form)
      form.reset()
      this._authRepository.login(formData)
        .then(() => window.location.href = "app.html")
        .catch(() => alert("Login fallito"))
    });

  }

}

document.addEventListener('DOMContentLoaded', () => new Login())
