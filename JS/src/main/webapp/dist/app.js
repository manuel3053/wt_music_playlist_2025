import { AuthRepository } from "./api.service.js";
import { Header } from "./header.js";
import { HomePage } from "./homepage.js";
export class App {
    constructor() {
        this._authCaller = new AuthRepository();
        this._history = [];
        this.currentPage = new HomePage(this);
        const headerComponent = new Header(this);
        const header = document.getElementById("header");
        header.innerHTML = headerComponent.template;
        headerComponent.build();
    }
    set currentPage(page) {
        if (this._currentPage != undefined) {
            this._history.push(this._currentPage);
        }
        this._currentPage = page;
        this.buildPage(page);
    }
    pop() {
        if (this._history.length != 0) {
            const page = this._history.pop();
            this.buildPage(page);
        }
    }
    buildPage(page) {
        const app = document.getElementById("app");
        app.innerHTML = page.template;
        const style = document.getElementById("current-page-style");
        style.innerHTML = page.css;
        page.build();
    }
    static goToLogin() {
        window.location.href = "index.html";
    }
}
document.addEventListener('DOMContentLoaded', () => new App());
