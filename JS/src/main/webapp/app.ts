import { Component } from "./component"
import { HomePage } from "./homepage"
import "./static/style.css"

export class App {

  constructor() {
    this.currentPage = new HomePage(this)

    // const style: HTMLElement = document.getElementById("app-style")!
    // style.innerHTML = this.css
  }

  public set currentPage(page: Component) {
    const app: HTMLElement = document.getElementById("app")!
    app.innerHTML = page.template
    const style: HTMLElement = document.getElementById("current-page-style")!
    style.innerHTML = page.css
    page.buildEventListeners()
  }

}

document.addEventListener('DOMContentLoaded', () => new App())
