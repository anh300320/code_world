document.addEventListener("DOMContentLoaded", function(){
    var id = window.location.href;
    const index = id.lastIndexOf("/");
    id = id.substr(index + 1);
    var container = document.querySelector(".container");
    
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
        if(data && data.status == "SUCCESS" && data.data.id == id){
            const link = document.createElement("group-link");
            link.setAttribute("curr", data.data.id);
            link.setAttribute("url", "../../component/profile/");
            link.setAttribute("active", "1");
            container.appendChild(link);
            getDetail();
        } else {
            getDetail();
        }
        
    })
    function getDetail(){
        fetch(
        "/api/user/" + id, {
            method:"GET",
            headers: {
                "Content-Type": "application/json"
            },
            'credentials': 'same-origin'
        }).then(
            response =>  {
                return response.json().then((data) => {
                    if(data.status == "SUCCESS"){
                        const detail = document.createElement("custom-profile");
                        detail.setAttribute("id", "profile");
                        detail.setAttribute("url", "../../component/profile/");
                        detail.user = data.data;
                        container.appendChild(detail);
                    } else {
                        container.innerHTML = "USER NOT FOUND!";
                    }
                })
            },
            error => {
                console.log(error);
            }
        )
    }
}, false)