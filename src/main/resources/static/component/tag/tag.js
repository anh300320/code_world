class Tag extends HTMLElement{
    connectedCallback(){
        const tag = this.tag;
        var name = tag.tag.replaceAll('+', "%2B");
        const grid = document.createElement("div");
        grid.className = "grid-tag";

        const container = document.createElement("div");
        container.className = "grid-container";

        const tags = document.createElement("div");
        tags.className = "tags";

        const link = document.createElement("a");
        link.href = "/question?tag=" + name;
        link.textContent = tag.tag;

        tags.appendChild(link);
        container.appendChild(tags);
        grid.appendChild(container);

        const gridTime = document.createElement("div");
        gridTime.className = "grid-time";

        const post = document.createElement("div");
        post.innerHTML = "<span>" + tag.numPost + "</span> questions";

        const today = document.createElement("div");
        today.style = "text-align: right;";
        today.innerHTML = "<span>" + tag.postToday + "</span> asked today";
        
        gridTime.appendChild(post);
        gridTime.appendChild(today);
        grid.appendChild(gridTime);

        const lastModified = document.createElement("div");
        lastModified.className = "grid-time";
        lastModified.style ="grid-template-columns: none;";
        lastModified.textContent = tag.recentPost;

        grid.appendChild(lastModified);

        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = "../../templates/tag/tag.css";

        const shadowRoot = this.attachShadow({mode: "closed"});
        shadowRoot.appendChild(style);
        shadowRoot.appendChild(grid);

    }
}
customElements.define("custom-tag", Tag);