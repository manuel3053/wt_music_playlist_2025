import { AuthRepository } from "./api.service.js";
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
    }
}
document.addEventListener('DOMContentLoaded', () => new Login());
