import { AuthRepository } from "./api.service.js";
import { HomePage } from "./homepage.js";
export class App {
    constructor() {
        this._authCaller = new AuthRepository();
        this.currentPage = new HomePage(this);
    }
    set currentPage(page) {
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
