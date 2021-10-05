const API_URL = "http://localhost:8080/";

class Nav extends HTMLElement{
    constructor(){
        super(); 
        /*img icon*/
        const searchIcon = document.createElement("img");
        searchIcon.src = this.getAttribute("url") +"icon.png";
        searchIcon.width = 16;
        searchIcon.style = "float: left; margin-top:5px";
        /*css file*/
        const linkCss = document.createElement('link');
        linkCss.setAttribute('rel', 'stylesheet');
        linkCss.setAttribute('href', this.getAttribute("url") + "nav.css");

        const fontAwesome = document.createElement("script");
        fontAwesome.href = "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/js/all.min.js";
        fontAwesome.crossOrigin = "anonymous";

        const topnav = document.createElement('div');
        topnav.className = "topnav";        

        const nav = document.createElement('div');
        nav.className = 'nav';

        const gridLeft = document.createElement('div');
        gridLeft.className = "grid left";

        const homeLink = document.createElement('a');
        homeLink.className = "logo";
        homeLink.href = API_URL;
        homeLink.textContent = "CODEWORLD";

        const linkGroup = document.createElement('div');
        linkGroup.className = "grid";

        const a1 = document.createElement('a');
        a1.href = API_URL + "question";
        a1.textContent = "Questions";

        const a2 = document.createElement('a');
        a2.href = API_URL + "tags";
        a2.textContent = "Tags";

        const a3 = document.createElement('a');
        a3.href = API_URL + "users";
        a3.textContent = "Users";

        linkGroup.appendChild(a1);
        linkGroup.appendChild(a2);
        linkGroup.appendChild(a3);

        gridLeft.appendChild(homeLink);
        gridLeft.appendChild(linkGroup);

        nav.appendChild(gridLeft);

        const gridRight = document.createElement('div');
        gridRight.className = "grid right";

        const form = document.createElement("form");
        form.action = API_URL + "/search";

        const search = document.createElement("div");
        search.className = "search";

        const icon = document.createElement("i");
        icon.className = "fas fa-search";
        icon.style = "float: left; padding-top: 5px;";

        const input = document.createElement("input");
        input.type = "text";
        input.placeholder = "Search..";

        search.appendChild(searchIcon);
        search.appendChild(input);

        const input1 = document.createElement("input");
        input1.type = "submit";
        input1.value = "Search";

        form.appendChild(search);
        form.appendChild(input1);

        gridRight.appendChild(form);

        console.log(this.checkLogin());

        if(!this.checkLogin()){
            const action = document.createElement("div");
            action.className = "action grid";
            
            const login = document.createElement("a");
            login.className = "login";
            login.href = API_URL + "login";
            login.textContent = "Login";

            const signUp = document.createElement("a");
            signUp.className = "signup";
            signUp.href = API_URL + "signUp";
            signUp.textContent = "Sign Up";

            action.appendChild(login);
            action.appendChild(signUp);

            gridRight.appendChild(action);
        } else {
            const dropdown = document.createElement("div");
            dropdown.className = "dropdown";

            const button = document.createElement("div");
            button.className = "dropbtn";
            button.onclick = (e) => {
                this.dropContent.classList.toggle("active");
                e.stopPropagation();
            }

            const user = document.createElement("img");
            user.height = 30;
            user.style = "padding-top: 3px;";
            user.src = this.getAttribute("url") + "user.png";

            button.appendChild(user);

            this.dropContent = document.createElement("div");
            this.dropContent.className = "dropdown-content";

            const l1 = document.createElement('a');
            l1.href = API_URL + "user";
            l1.textContent = this.username;

            const l2 = document.createElement('a');
            l2.href = API_URL + "";
            l2.textContent = "Account";

            const l3 = document.createElement('a');
            l3.href = API_URL + "";
            l3.textContent = "Log out";

            this.dropContent.appendChild(l1);
            this.dropContent.appendChild(l2);
            this.dropContent.appendChild(l3);

            dropdown.appendChild(button);
            dropdown.appendChild(this.dropContent);
            gridRight.appendChild(dropdown);
        }
        nav.appendChild(gridRight);
        topnav.appendChild(nav);        

        const shadowRoot = this.attachShadow({mode: 'closed'});
        shadowRoot.appendChild(linkCss);
        shadowRoot.appendChild(fontAwesome);
        shadowRoot.appendChild(topnav);
    }

    //check login or not?
    checkLogin(){
        console.log("hoi cha");
        return false;
    }

    closeDropContent(){
        if(this.dropContent && this.dropContent.classList.contains("active")){
            this.dropContent.classList.remove("active");
        }
    }
}
customElements.define('custom-nav', Nav);

document.addEventListener("DOMContentLoaded", function(){
    window.addEventListener('click', function(){
        document.getElementsByTagName("custom-nav")[0].closeDropContent();
    }, false);
}, false);