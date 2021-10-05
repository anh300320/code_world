document.addEventListener("DOMContentLoaded", function(){
    function getAPI(url){
        return fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            'credentials': 'same-origin'            
        }).then(
            response => {
                return response.json().then(
                    (data) => {
                        return data;
                    }
                )
            }, 
            error => {
                return false;
            }
        ).catch(
            response => {
                return false;
            }
        )
    }
    var totalPage = 0;
    var currentPage = 0;

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
                    currentPage = 0;
                    showQuestion();
                } else if(item.id === "answers"){
                    currentPage = 0;
                    showAnswer();
                } else if(item.id === "tags"){
                    currentPage = 0;
                    showTag();
                } else if(item.id === "bookmarks"){
                    currentPage = 0;
                    showBookmark();
                }
            })
        }        
    })        
    showQuestion();

    function showQuestion() {
        var tmpQuestions = getAPI("/api/user/auth/questions?" + new URLSearchParams({page: currentPage}));
        tmpQuestions.then((result) => {
            if(result){
                if(result.status == "SUCCESS"){
                    console.log(result);
                    document.querySelector(".quantity").textContent = result.data.totalItems + " Questions";
                    appendResult(result.data, 3);  
                    currentPage = result.data.currentPage;
                    totalPage = result.data.totalPages;
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
                        showQuestion();
                    }
                    pageable.clickNext =  () => {
                        currentPage++;
                        showQuestion();
                    }
                    page.appendChild(pageable);
                }
            } else {
                console.log("error");
            }            
        })
    }


    function showAnswer() {
        var tmpComments = getAPI("/api/user/auth/comments?" + new URLSearchParams({page: currentPage}));
        tmpComments.then((result) => {
            if(result){
                if(result.status == "SUCCESS"){
                    console.log(result);
                    document.querySelector(".quantity").textContent = result.data.totalItems + " Answers";
                    appendResult(result.data, 2);  
                    currentPage = result.data.currentPage;
                    totalPage = result.data.totalPages;
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
                        showAnswer();
                    }
                    pageable.clickNext =  () => {
                        currentPage++;
                        showAnswer();
                    }
                    page.appendChild(pageable);
                }
            } else {
                console.log("error");
            }            
        })
    }

    function showTag() {
        var tmptags = getAPI("/api/user/auth/tags");
        tmptags.then((result) => {
            if(result.status == "SUCCESS"){
                document.querySelector(".quantity").textContent = result.data.length + " tags";
                var tmp = document.getElementById("content");
                if(tmp){
                    content.removeChild(tmp);
                }
                tmp = document.createElement("div");
                tmp.id = "content";
                tmp.style = "display: flex;flex-wrap: wrap;"
                result.data.forEach((item) => {
                    const tagcontainer = document.createElement("div");
                    tagcontainer.style = "width:150px; margin-top:15px";
                    const tag = document.createElement("a");
                    tag.className = "tag";
                    tag.href = "/question?tag=" + item.tag.replaceAll("+", "%2B");
                    tag.textContent = item.tag + "(" + item.postCount + ")";
                    tagcontainer.appendChild(tag);
                    tmp.appendChild(tagcontainer);
                })
                var page = document.getElementsByClassName("pageable")[0];
                content.insertBefore(tmp, page);                
                page.removeChild(page.firstChild);
            }
        })
    }

    function showBookmark(){
        //call api get all book mark
        var tmpBookmarks = getAPI("/api/user/auth/bookmarks?" + new URLSearchParams({page: currentPage}));
        tmpBookmarks.then((result) => {
            if(result){
                if(result.status == "SUCCESS"){
                    document.querySelector(".quantity").textContent = result.data.totalItems + " bookmarks";
                    appendResult(result.data, 1);  
                    currentPage = result.data.currentPage;
                    totalPage = result.data.totalPages;
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
                        showBookmark();
                    }
                    pageable.clickNext =  () => {
                        currentPage++;
                        showBookmark();
                    }
                    page.appendChild(pageable);
                }
            } else {
                console.log("error");
            }            
        })
    }

    function appendResult(ques, index){
        var tmp = document.getElementById("content");
        if(tmp){
            content.removeChild(tmp);
        }
        tmp = document.createElement("div");
        tmp.id = "content";

        const dataResult = Array.from(ques.posts);
        if(dataResult){
            dataResult.forEach((item) => {
                var q = document.createElement("custom-question");
                q.setAttribute("url", "../../component/question/");            
                q.question = item;
                if(index == 1){
                    q.deleteBookmark = () => {
                        var tmpDeleteBookmarks = getAPI("/api/post/auth/bookmark/" + item.id);
                        tmpDeleteBookmarks.then((result) => {
                            if(result){
                                if(result.status == "SUCCESS"){
                                    var idx = dataResult.lastIndexOf(item);
                                    dataResult.splice(idx, 1);
                                    document.querySelector(".quantity").textContent = dataResult.length + " bookmarks";
                                    tmp.removeChild(q);
                                }
                            } else {
                                console.log("error");
                            }
                        })
                    }
                } else if(index == 3 || index == 2){
                    q.setAttribute("delete", 1);
                }
                tmp.appendChild(q);
            })
        }

        content.insertBefore(tmp, document.querySelector(".pageable"));
    }
}, false);