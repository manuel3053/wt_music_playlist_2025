export class Header {
    constructor(context) {
        this.context = context;
    }
    get css() {
        return "";
    }
    get template() {
        return `
    <button id="prev_page" class="link-button">Prev<button/>
    `;
    }
    build() {
        const back = document.getElementById("prev_page");
        back.addEventListener("click", this.context.pop.bind(this.context));
    }
}
