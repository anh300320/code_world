class GroupLink extends HTMLElement{
    
    connectedCallback(){
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
        
        const div = document.createElement("div");
        div.className = "grid";

        const link1 = document.createElement("a");
        link1.className = "link";
        
        link1.textContent = "Profile";
        var res = getData();
        res.then((data) => {
            if(data){
                if(data.status == "SUCCESS"){
                    link1.href = "/user/" + data.data.id;
                } else {
                    console.log("error!");
                }
            } else {
                console.log("error!");
            }          
        })

        const link2 = document.createElement("a");
        link2.className = "link";
        link2.href = "/activity";
        link2.textContent = "Activity";

        const link3 = document.createElement("a");
        link3.className = "link";
        link3.href = "/user/edit";
        link3.textContent = "Edit Profile";

        const a = this.getAttribute("active")

        switch(a) {
            case "1":
                link1.classList.add("active");
                break;
            case "2":
                link2.classList.add("active");
                break;
            case "3":
                link3.classList.add("active");
                break;
        }

        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = this.getAttribute("url") + "profile.css";

        div.appendChild(link1);
        div.appendChild(link2);
        div.appendChild(link3);

        const shadowRoot = this.attachShadow({mode: "closed"});
        shadowRoot.appendChild(style);
        shadowRoot.appendChild(div);
    }
}
customElements.define("group-link", GroupLink);