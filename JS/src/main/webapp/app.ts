import { Component } from "./component.js"
import { Header } from "./header.js";
import { HomePage } from "./homepage.js"

export class App {
  private _history: Component[] = [];

  constructor() {
    this.currentPage = new HomePage(this)
    const headerComponent = new Header(this)
    const header = document.getElementById("header")
    header!.innerHTML = headerComponent.template
    headerComponent.build()
  }

  public set currentPage(page: Component) {
    this._history.push(page)
    this.buildPage(page)
  }

  public pop() {
    if (this._history.length != 0) {
      this._history.pop()!
      this.buildPage(this._history[this._history.length - 1])
    }
  }

  private buildPage(page: Component) {
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
