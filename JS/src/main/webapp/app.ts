import { AuthRepository, ErrorResponse } from "./api.service.js"
import { Component } from "./component.js"
import { HomePage } from "./homepage.js"

export class App {
  private _authCaller: AuthRepository = new AuthRepository()
  private _currentPage: Component | undefined;

  constructor() {
    this.currentPage = new HomePage(this)
  }

  public set currentPage(page: Component) {
    const app: HTMLElement = document.getElementById("app")!
    app.innerHTML = page.template
    const style: HTMLElement = document.getElementById("current-page-style")!
    style.innerHTML = page.css
    page.build()
  }

  public static goToLogin() {
    window.location.href = "index.html"
  }

}

document.addEventListener('DOMContentLoaded', () => new App())
