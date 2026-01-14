import { AuthRepository } from "./api.service.js";
export class SubscribePage {
    constructor() {
        this._authRepository = new AuthRepository();
    }
    get css() {
        return "";
    }
    get template() {
        return `
        <div class = "login">
            <form method="post" class="form" id="form">
                <label class="form-field-name">Name</label>
                <input class="form-field" name="name" type="text" placeholder="Name" required>
                <label class="form-field-name">Surname</label>
                <input class="form-field" name="surname" type="text" placeholder="Surname" required>
                <label class="form-field-name">Username</label>
                <input class="form-field" name="username" type="text" placeholder="Username" required>
                <label class="form-field-name">Password</label>
                <input class="form-field" name="password" type="text" placeholder="Password" required>
                <input class="form-button" type="submit"/>
            </form>
        </div>
      `;
    }
    build() {
        const form = document.getElementById("form");
        form.addEventListener('submit', (event) => {
            event.preventDefault();
            const formData = new FormData(form);
            form.reset();
            this._authRepository.subscribe(formData)
                .then(() => window.location.href = "index.html")
                .catch(() => alert("Failed subscription"));
        });
    }
}
