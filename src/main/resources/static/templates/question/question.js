document.addEventListener("DOMContentLoaded", function(){
    var url = new URL(window.location.href);
    var searchKey = url.searchParams.get("title");
    var question = [];
    var totalPage = 0;
    var currentPage = 0;
    var itemPerPage = 15;
    var sortType = "NEWEST";
    var numQuestion = document.getElementById('numQuestion');

    var tag = url.searchParams.get("tag");

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
    if(res){
        res.then((data) => {
            if(data.status == "SUCCESS"){
                showResult(data.data);
            } else {
                showResult(false);
            }
            
        })
    } else {
        showResult(false);
    }

    
    function showResult(user){   
        getResult();
        function getResult(){
            tmpquestion = getQuestions();
            if(tmpquestion){
                tmpquestion.then((data) => {
                    if(data.status == "SUCCESS"){
                        showQuestion(data.data);
                    }
                })
            }
        }
    
        function showQuestion(ques){
            question = ques.questions || ques.posts;
            totalPage = ques.totalPages;
            currentPage = ques.currentPage;
            document.getElementById("numQuestion").textContent = ques.totalItems + " questions";
            
            //console.log(question);
            
            var questions = document.getElementById("questions");
            var content = document.getElementById("content");
            var pageable = document.getElementById("pageable");
            var page = document.getElementsByClassName("pageable")[0];
            if(pageable){
                page.removeChild(pageable);
            }
            pageable = document.createElement("custom-pageable");
            pageable.id = "pageable";
            pageable.setAttribute("totalPage", totalPage);
            pageable.setAttribute("currentPage", currentPage + 1);
            pageable.setAttribute("url", "../../component/question/");
            pageable.clickBack = () => {
                currentPage--;
                getResult();
            }
            pageable.clickNext =  () => {
                currentPage++;
                getResult();
            }
            page.appendChild(pageable);

            if(content){
                questions.removeChild(content);
            }
            content = document.createElement("div");
            content.id = "content";
            if(question){
                //console.log(question);
                question.forEach((item) => {
                    var q = document.createElement("custom-question");
                    q.setAttribute("url", "../../component/question/");            
                    q.question = item;
                    q.setAttribute("currUser", user?user.id:undefined);
                    content.appendChild(q);
                })
            }     
            questions.appendChild(content);
            window.scrollTo(0, 0);

        }

        var pageSize = document.querySelector("#pageSize");
        pageSize = Array.from(pageSize.getElementsByClassName("pageItem"));
        pageSize.forEach((item) => {
            item.addEventListener("click", () => {
                pageSize.forEach((i) => {
                    if(i.classList && i.classList.contains("activeItem")){
                        i.classList.remove("activeItem");
                        i.classList.add("numItem");
                    }
                })
                item.classList.remove("numItem");
                item.classList.add("activeItem");
                itemPerPage = item.textContent;
                currentPage = 0;
                getResult();
            })
        })

        var btnGroup = document.querySelector(".btn-group");
        btnGroup = Array.from(btnGroup.childNodes);
        btnGroup.forEach((item, index) => {
            item.addEventListener("click", () => {
                btnGroup.forEach((i) => {
                    if(i.classList && i.classList.contains("active")){
                        i.classList.remove("active");
                    }
                })
                item.classList.add("active");
                if(index == 1){
                    sortType = "NEWEST";
                    getResult();
                } else if(index == 3){
                    sortType = "UPVOTE";
                    getResult();
                } else if(index == 5){
                    sortType = "COMMENT";
                    getResult();
                }
            })
        })

        function getQuestions(){
            var url = "/api/post?";
            var tmpdata = {
                page: currentPage,
                size: itemPerPage,
                sortTypeStr: sortType
            }
            if(searchKey){
                tmpdata["searchKey"] = searchKey;
            }
            if(tag){
                url = "/api/tag/get";
                tmpdata = {
                    tag: tag,
                    page: currentPage,
                    size: itemPerPage,
                    sortTypeStr: sortType                    
                }
                return fetch(
                    url, {
                    method:"POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    'credentials': 'same-origin',
                    body: JSON.stringify(tmpdata)
                }).then(
                    response =>  {
                        return response.json().then((data) => {
                            return data;
                        })
                    },
                    error => {
                        console.log(error);
                        return false;
                    }
                )
            }
            
            return fetch(
            url + new URLSearchParams(tmpdata), {
                method:"GET",
                headers: {
                    "Content-Type": "application/json"
                },
                'credentials': 'same-origin'
            }).then(
                response =>  {
                    return response.json().then((data) => {
                        return data;
                    })
                },
                error => {
                    console.log(error);
                    return false;
                }
            )
        }        
    }
}, false);