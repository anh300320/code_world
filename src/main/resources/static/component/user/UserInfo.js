class UserInfo extends HTMLElement{
    connectedCallback(){
        const linkToUserDetailPage = "/user/";
        const user = this.user;
        const root = document.createElement("div");
        root.className = "grid-tag";

        const info = document.createElement("div");
        info.className = "user-info";

        const avatar = document.createElement("div");
        avatar.className = "avatar";

        const link = document.createElement("a");
        link.href = linkToUserDetailPage + user.id;

        link.innerHTML = '<div class="image"><img src="../../templates/user/user-01.png" alt="" width="32px"></div>';
        avatar.appendChild(link);
        info.appendChild(avatar);

        const detail = document.createElement("div");
        detail.className = "user-details";

        const link1 = document.createElement("a");
        link1.href = linkToUserDetailPage + user.id;
        link1.textContent = user.username;
        detail.appendChild(link1);
        
        const span = document.createElement("span");
        span.className = "user-location";
        span.textContent = user.createdDate;
        detail.appendChild(span);

        info.appendChild(detail);
        root.appendChild(info);

        const repu = document.createElement("div");
        repu.className = "reputation";
        repu.innerHTML = '<div class="reputation-score">Reputation '+ user.reputation +'</div>' +
                        '<div class="reputation-score">Questions '+ user.questionCount +'</div>';

        root.appendChild(repu);

        

        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = "../../templates/user/user.css";

        const shadowRoot = this.attachShadow({mode: "closed"});
        shadowRoot.appendChild(style);
        shadowRoot.appendChild(root);
    }
}

customElements.define("custom-user", UserInfo);