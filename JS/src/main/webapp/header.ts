import { App } from "./app.js"
import { Component } from "./component.js"

export class Header implements Component {
  private context: App

  constructor(context: App) {
    this.context = context
  }

  get css(): string {
    return ""
  }

  get template(): string {
    return `
    <button id="prev_page" class="link-button">Prev<button/>
    `
  }

  build(): void {
    const back: HTMLButtonElement = document.getElementById("prev_page") as HTMLButtonElement
    back.addEventListener("click", this.context.pop.bind(this.context))
  }

}
