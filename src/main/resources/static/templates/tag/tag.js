document.addEventListener("DOMContentLoaded", function(){
    var totalPage = 0;
    var currentPage = 0;
    var sortType = "POST_COUNT";    
    var searchKey = "";
    function showTag(){
        const tmpData = {
            page: currentPage,
            sortType: sortType,
            searchKey: searchKey
        }
        fetch(
        "/api/tag/all?" + new URLSearchParams(tmpData), {
            method:"GET",
            headers: {
                "Content-Type": "application/json"
            },
            'credentials': 'same-origin'
        }).then(
            response =>  {
                return response.json().then((result) => {
                    if(result.status == "SUCCESS"){
                        document.querySelector(".quantity").textContent = "(" + result.data.totalItems + ")";
                        appendResult(result.data);  
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
                            showTag();
                        }
                        pageable.clickNext =  () => {
                            currentPage++;
                            showTag();
                        }
                        page.appendChild(pageable);                  
                    }
                })
            },
            error => {
                console.log(error);
            }
        )
    }

    function appendResult(ques){
        var content = document.querySelector("#content-container");
        var tmp = document.getElementById("content");
        if(tmp){
            content.removeChild(tmp);
        }
        tmp = document.createElement("div");
        tmp.id = "content";
        tmp.className = "grid-layout";

        const dataResult = Array.from(ques.tags);
        if(dataResult){
            dataResult.forEach((item) => {
                var q = document.createElement("custom-tag");           
                q.tag = item;
                tmp.appendChild(q);
            })
        }
        content.insertBefore(tmp, document.querySelector(".pageable"));
    }

    showTag();

    var btnGroup = document.querySelector(".btn-group");
    btnGroup = Array.from(btnGroup.childNodes);
    if(btnGroup){
        btnGroup.forEach((item, index) => {
            item.addEventListener("click", () => {
                btnGroup.forEach((i) => {
                    i.style = "";
                })
                item.style = "background: #d6d9dc;";
                console.log(index);
                switch(index){
                    case 1: {
                        currentPage = 0;
                        sortType = "POST_COUNT"; 
                        showTag();
                        break;
                    }
                    case 3: {
                        currentPage = 0;
                        sortType = "TAG_NAME";
                        showTag();
                        break;
                    }
                    case 5: {
                        currentPage = 0;
                        sortType = "LATEST_CREATED";
                        showTag();
                        break;
                    }
                }
            })
        })
    }

    var search = document.getElementById("search");
    search.addEventListener("input", () => {
        searchKey = search.value;
        currentPage = 0;
        showTag();
    })
})