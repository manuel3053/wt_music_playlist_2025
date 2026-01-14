import { AuthRepository } from "./api.service.js";
import { SubscribePage } from "./subscribe.js";
class Login {
    constructor() {
        this._authRepository = new AuthRepository();
        const form = document.getElementById("form");
        form.addEventListener('submit', (event) => {
            event.preventDefault();
            const formData = new FormData(form);
            form.reset();
            this._authRepository.login(formData)
                .then(() => window.location.href = "app.html")
                .catch(() => alert("Login fallito"));
        });
        const subscribe = document.getElementById("subscribe");
        subscribe.addEventListener("click", () => {
            const page = new SubscribePage();
            document.body.innerHTML = page.template;
            page.build();
        });
    }
}
document.addEventListener('DOMContentLoaded', () => new Login());
