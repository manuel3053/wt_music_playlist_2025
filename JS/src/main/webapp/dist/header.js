export class Header {
    constructor(context) {
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
    }
}
