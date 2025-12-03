import { AuthRepository } from "./api.service.js"
import { App } from "./app.js"
import { Component } from "./component.js"

export class Header implements Component {
  private context: App
  private _authRepository: AuthRepository = new AuthRepository()

  constructor(context: App) {
    this.context = context
  }

  get css(): string {
    return `
    .header {
      display: flex;
    }
    `
  }

  get template(): string {
    return `
    <div id="prev_page" class="link-button">Prev</div>
    <div id="logout" class="link-button"/>Logout</div>
    `
  }

  build(): void {
    const back: HTMLButtonElement = document.getElementById("prev_page") as HTMLButtonElement
    back.addEventListener("click", this.context.pop.bind(this.context))
    const logout: HTMLButtonElement = document.getElementById("logout") as HTMLButtonElement
    logout.addEventListener("click", () => {
      this._authRepository.logout()
        .then(() => window.location.href = "index.html")
        .catch(console.log)
    })
  }

}
