import "./static/style.css"

class Login {
  constructor() {
    // const style: HTMLElement = document.getElementById("login-style")!
    // style.innerHTML = `@import url('https://fonts.googleapis.com/css2?family=Overpass:ital,wght@0,100..900;1,100..900&family=Space+Mono:ital,wght@0,400;0,700;1,400;1,700&display=swap');`
    // window.location.href = "app.html"
  }

  get css(): string {
    throw new Error("Method not implemented.");
  }

  buildEventListeners(): void {
    throw new Error("Method not implemented.");
  }

}

document.addEventListener('DOMContentLoaded', () => new Login())
