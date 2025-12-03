import { AuthRepository } from "./api.service.js";
export class Header {
    constructor(context) {
        this._authRepository = new AuthRepository();
        this.context = context;
    }
    get css() {
        return `
    .header {
      display: flex;
    }
    `;
    }
    get template() {
        return `
    <div id="prev_page" class="link-button">Prev</div>
    <div id="logout" class="link-button"/>Logout</div>
    `;
    }
    build() {
        const back = document.getElementById("prev_page");
        back.addEventListener("click", this.context.pop.bind(this.context));
        const logout = document.getElementById("logout");
        logout.addEventListener("click", () => {
            this._authRepository.logout()
                .then(() => window.location.href = "index.html")
                .catch(console.log);
        });
    }
}
