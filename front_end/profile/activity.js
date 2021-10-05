document.addEventListener("DOMContentLoaded", function(){
    var json = {
        title: "Spring boot",
        content: "lam the nao de tao project spring boot tren intellij",
        owner: {username: "lananh", id:"1"},
        createdDate: "2021-04-19 15:29:27",
        upvoteCount: 15,
        comments: [],
        tags: ["springboot", "java"],
        parent: "1",
        solve: false
    };
    json = JSON.stringify(json);

    var content = document.querySelector(".activity-content");
    const container = document.querySelector(".container");
    const nav = document.querySelector(".navigation");
    const list = Array.from(nav.childNodes);
    list.forEach((item) => {
        if(item.classList && item.classList.contains("link")){
            item.addEventListener("click", () => {
                list.forEach((i) => {
                    if(i.classList && i.classList.contains("active1")){
                        i.classList.remove("active1");
                    }
                })
                item.classList.add("active1");
                if(item.id === "questions"){
                    getAllQuestion();
                } else if(item.id === "answers"){
                    showAnswer();
                } else if(item.id === "tags"){
                    showTag();
                } else if(item.id === "bookmarks"){
                    showBookmark();
                }
            })
        }        
    })    

    var question = [];
    
    getAllQuestion();

    function getAllQuestion(){
        $.ajax({
            type : "GET",
            url :  "http://localhost:8081/api/post/get",
            dataType : 'json', 
            contentType:'application/json',
    
            success: function(result){
                question = result.data.posts;
                showQuestion();
            },
            error : function(e) {
                console.log("Something went wrong: ", e);
            }
        });     
    }

    function showQuestion() {
        container.removeChild(content);
        content = document.createElement("div");
        content.className = "activity-content";

        const grid = document.createElement("div");
        grid.className = "grid-container2";
        grid.style = "margin-top: 10px;";

        const item = document.createElement("div");
        item.className = "grid-item";

        const quantity = document.createElement("div");
        quantity.className = "quantity";
        quantity.textContent = question.length + " questions";

        item.appendChild(quantity);
        grid.appendChild(item);

        const item1 = document.createElement("div");
        item1.className = "grid-item";
        
        const btnGroup = document.createElement("div");
        btnGroup.className = "btn-group";
        btnGroup.style = "float: right;"

        const btn1 = document.createElement("button");
        btn1.className = "active";       
        btn1.textContent = "Newest";

        const btn2 = document.createElement("button");
        btn2.textContent = "Upvote";        

        const btn3 = document.createElement("button");
        btn3.textContent = "Comment";      

        btnGroup.appendChild(btn1);
        btnGroup.appendChild(btn2);
        btnGroup.appendChild(btn3);

        item1.appendChild(btnGroup);

        grid.appendChild(item1);

        content.appendChild(grid);
        // append result
        if(question){
            question.forEach((item) => {
                var q = document.createElement("custom-question");
                q.setAttribute("url", "../component/question/");            
                q.setAttribute("question", JSON.stringify(item));
                q.setAttribute("currUser", "1");
                content.appendChild(q);
            })
        }       

        container.appendChild(content);

        var group = Array.from(btnGroup.childNodes);
        group.forEach((btn) => {
            btn.addEventListener("click", () => {
                group.forEach((i) => {
                    if(i.classList && i.classList.contains("active"))
                        i.classList.remove("active");
                })
                btn.classList.add("active");
                sortQuestion();
            })
        })

        function sortQuestion(sortBy){
            //call API to get data sorted
        }
    }


    function showAnswer() {
        container.removeChild(content);
        content = document.createElement("div");
        content.className = "activity-content";

        const grid = document.createElement("div");
        grid.className = "grid-container2";
        grid.style = "margin-top: 10px;";

        const item = document.createElement("div");
        item.className = "grid-item";

        const quantity = document.createElement("div");
        quantity.className = "quantity";
        quantity.textContent = "input";

        item.appendChild(quantity);
        grid.appendChild(item);

        const item1 = document.createElement("div");
        item1.className = "grid-item";
        
        const btnGroup = document.createElement("div");
        btnGroup.className = "btn-group";
        btnGroup.style = "float: right;"

        const btn1 = document.createElement("button");
        btn1.className = "active";       
        btn1.textContent = "Newest";

        const btn2 = document.createElement("button");
        btn2.textContent = "Upvote";            

        btnGroup.appendChild(btn1);
        btnGroup.appendChild(btn2);

        item1.appendChild(btnGroup);

        grid.appendChild(item1);

        content.appendChild(grid);
        // append result
        var q = document.createElement("custom-question");
        q.setAttribute("url", "../component/question/");            
        q.setAttribute("question", json);
        q.setAttribute("currUser", "1");
        content.appendChild(q);

        var q1 = document.createElement("custom-question");
        q1.setAttribute("url", "../component/question/");            
        q1.setAttribute("question", json);
        q1.setAttribute("currUser", "1");
        content.appendChild(q1);

        container.appendChild(content);

        var group = Array.from(btnGroup.childNodes);
        group.forEach((btn) => {
            btn.addEventListener("click", () => {
                group.forEach((i) => {
                    if(i.classList && i.classList.contains("active"))
                        i.classList.remove("active");
                })
                btn.classList.add("active");
                sortQuestion();
            })
        })

        function sortQuestion(sortBy){
            //call API to get data sorted then recall showAnswer
        }
    }

    function showTag() {
        container.removeChild(content);
        content = document.createElement("div");
        content.className = "activity-content";

        const tag = document.createElement("a");
        tag.className = "tag";
        tag.hef = "link to tag";
        tag.textContent = "spring boot";

        content.appendChild(tag);
        container.appendChild(content);
    }

    function showBookmark(){
        //call api get all book mark
        showQuestion();
    }
}, false);