class Nav extends HTMLElement{
    dropContent = null;

    showUser(u){
        if(u){
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
            l1.href = "/user/" + u.id;
            l1.textContent = u.username;

            const l2 = document.createElement('a');
            l2.href = "/user/" + u.id;
            l2.textContent = "Account";

            const l3 = document.createElement('a');
            l3.href = "/logout";
            l3.textContent = "Log out";

            this.dropContent.appendChild(l1);
            this.dropContent.appendChild(l2);
            this.dropContent.appendChild(l3);

            dropdown.appendChild(button);
            dropdown.appendChild(this.dropContent);
            this.gridRight.appendChild(dropdown);
        } else {
            const action = document.createElement("div");
            action.className = "action grid";
            
            const login = document.createElement("a");
            login.className = "login";
            login.href ="/login";
            login.textContent = "Login";

            const signUp = document.createElement("a");
            signUp.className = "signup";
            signUp.href = "/register";
            signUp.textContent = "Sign Up";

            action.appendChild(login);
            action.appendChild(signUp);

            this.gridRight.appendChild(action);
        }
    }


    connectedCallback(){                
        /*img icon*/
        const searchIcon = document.createElement("img");
        searchIcon.src = this.getAttribute("url") +"icon.png";
        searchIcon.width = 16;
        searchIcon.style = "float: left; margin-top:5px";
        /*css file*/
        const linkCss = document.createElement('link');
        linkCss.setAttribute('rel', 'stylesheet');
        linkCss.setAttribute('href', this.getAttribute("url") + "nav.css");

        const topnav = document.createElement('div');
        topnav.className = "topnav";        

        const nav = document.createElement('div');
        nav.className = 'nav';

        const gridLeft = document.createElement('div');
        gridLeft.className = "grid left";

        const homeLink = document.createElement('a');
        homeLink.className = "logo";
        homeLink.href = "/";
        homeLink.textContent = "CODEWORLD";

        const linkGroup = document.createElement('div');
        linkGroup.className = "grid";

        const a1 = document.createElement('a');
        a1.href = "/question";
        a1.textContent = "Questions";

        const a2 = document.createElement('a');
        a2.href = "/tags";
        a2.textContent = "Tags";

        const a3 = document.createElement('a');
        a3.href = "/users";
        a3.textContent = "Users";

        linkGroup.appendChild(a1);
        linkGroup.appendChild(a2);
        linkGroup.appendChild(a3);

        gridLeft.appendChild(homeLink);
        gridLeft.appendChild(linkGroup);

        nav.appendChild(gridLeft);

        this.gridRight = document.createElement('div');
        this.gridRight.className = "grid right";

        const form = document.createElement("form");
        form.onsubmit = (e) => {
            e.preventDefault();
            window.location.replace("/question?title=" + input.value);
        }
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

        this.gridRight.appendChild(form);   

        nav.appendChild(this.gridRight);
        topnav.appendChild(nav);        

        const shadowRoot = this.attachShadow({mode: 'closed'});
        shadowRoot.appendChild(linkCss);
        shadowRoot.appendChild(topnav);
        
        var thisClass = this;

        //check login or not?
        // $.ajax({
        //     type : "GET",
        //     url :  "/api/auth/info",
        //     dataType : 'json', 
        //     contentType:'application/json',        
    
        //     success: function(result){
        //         if(result.status == "SUCCESS"){
        //             thisClass.showUser(result.data);
        //         } else {
        //             thisClass.showUser(false);
        //         }
        //     },
        //     error : function(e) {
        //         console.log("Something went wrong: ", e);
        //         thisClass.showUser(false);
        //     }
        // });    

        function getData(){
            return fetch(
                "/api/auth/info", {
                    method:"GET",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    'credentials': 'same-origin'
                }).then(
                    response =>  {
                        return response.json().then((data) => {
                            //console.log(data);
                            return data;
                        })
                    },
                    error => {
                        console.log(error);
                        return false;
                    }
                )
        }; 
        var res = getData();
        res.then((data) => {
            if(data){
                if(data.status == "SUCCESS"){
                    this.showUser(data.data);
                } else {
                    this.showUser(false);
                }
            } else {

            }          
        })
        // getData().then((data) => {
        //     console.log(data);
        // });     
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