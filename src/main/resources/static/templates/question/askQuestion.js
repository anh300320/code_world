document.addEventListener("DOMContentLoaded", function (){
    const title = document.getElementById("title");
    const content = document.getElementById("content");
    const code = document.getElementById("code");
    const tags = document.getElementById("tags");
    
    const form = document.getElementById("inputForm");
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        if(title.value == null || title.value == "" || content.value == null || content.value == ""){
            window.alert("Your question must have title and content!");
        } else {
            var tmp = Array.from(tags.value.split(","));
            var tmp1 = [];
            if(tmp){
                tmp.forEach((item) => {
                    tmp1.push(item.trim());
                })
            }
            console.log(tmp1);
            const tmpData = {
                title: title.value,
                content: content.value,
                tags: tmp1,
                code: code.value
            }

            fetch(
                "/api/post/auth/create", {
                method:"POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(tmpData)
            }).then(
                response =>  {
                    return response.json().then((data) => {
                        if(data.status == "SUCCESS"){
                            window.alert("Your question was posted!");
                            window.location.replace("/question/" + data.data.id);
                        }
                    })
                },
                error => {
                    console.log(error);
                }
            )
        }
    })
}, false);