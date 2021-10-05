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

    function postAPI(url, data){
        return fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            'credentials': 'same-origin',
            body: JSON.stringify(data)            
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

    var res = getAPI("/api/auth/info");
    if(res){
        res.then((data) => {
            if(data.status == "SUCCESS"){
                showDetail(data.data);
            } else {
                showDetail(false);
            }
            
        })
    } else {
        showDetail(false);
    }

    function showDetail(u){
        var id = window.location.href;
        const index = id.lastIndexOf("/");
        id = id.substr(index + 1);
        var voteUp = document.querySelector(".voteUp");
        var voteDown = document.querySelector(".voteDown");   
        var tmpDetail = getAPI("/api/post/" + id);
        tmpDetail.then((data) => {
            if(data){
                if(data.status == "SUCCESS"){
                    showData(data.data);
                } else {
                    document.getElementsByClassName("container")[0].innerHTML = "Câu hỏi không tồn tại";
                }
            } else {
                console.log("error");
            }            
        })

        function callVoteAPI(id, vote){
            var tmpData = {
                itemId: id,
                vote: vote
            }
            var result = postAPI("/api/post/auth/upvote", tmpData);
            result.then((data) => {
                if(data && data.status == "SUCCESS"){
                    var v = document.querySelector(".vote");
                    v.textContent = Number(v.textContent) + data.data.vote;
                } else {
                    var r = window.confirm("Login required to vote, Login now?");
                    if (r === true) {
                        window.location.replace("/login");
                    } else {
                    }
                }
            })                    
        }

        function showData(data){        

            const mainPost = data.mainPost;
            document.getElementById("title").textContent = mainPost.title;
            document.getElementById("createdDate").textContent = new Date(mainPost.createdDate).toLocaleDateString("en-GB");
            document.getElementsByClassName("vote")[0].textContent = mainPost.upvoteCount;
            
            document.getElementsByClassName("question-content")[0].textContent = mainPost.content;
            var code = document.getElementsByClassName("code")[0];
            if(mainPost.code != null && mainPost.code != ""){
                const c = document.createElement("code");
                c.textContent = mainPost.code;
                code.appendChild(c);
            } else {
                document.querySelector(".right").removeChild(code);
            }

            switch(mainPost.currentUserVote){
                case 1:
                    voteUp.classList.add("activeBookmark");
                    break;
                case -1:
                    voteDown.classList.add("activeBookmark");
                    break;
            }

            if(mainPost.tags){
                mainPost.tags.forEach(element => {
                    var t = document.querySelector(".tags");
                    const a = document.createElement('a');
                    a.href = "/question?tag=" + element.replaceAll("+", "%2B");
                    a.textContent = element;
                    t.appendChild(a);
                });
            }
            const user = u?u.id:-1;

            if(user == mainPost.owner.id){
                const contain = document.createElement("div");
                contain.style = "height: 20px";
                const edit = document.createElement("button");
                edit.textContent = "Edit";
                // Get the modal
                var modal = document.createElement("div");
                modal.className = "modal";
                modal.id = "myModal";

                var modalContent = document.createElement("div");
                modalContent.className = "modal-content";               

                var span = document.createElement("span");
                span.className = "close";
                span.innerHTML = "&times;";
                // When the user clicks on <span> (x), close the modal
                span.onclick = function() {
                    modal.style.display = "none";
                }

                const editForm = document.createElement("edit-form");
                editForm.question = mainPost;
                editForm.setAttribute("url", "../../component/question/");
                modalContent.appendChild(span);
                modalContent.appendChild(editForm);
                
                modal.appendChild(modalContent);

                // When the user clicks anywhere outside of the modal, close it
                window.onclick = function(event) {
                    if (event.target == modal) {
                        modal.style.display = "none";
                    }
                }
                edit.onclick = () => {
                    //edit
                    modal.style.display = "block";
                }
                edit.style = "float: right";
                contain.appendChild(edit);
                const m = document.querySelector(".main");
                m.insertBefore(contain, m.firstChild);
                document.querySelector(".container").appendChild(modal);
            }

            function showComment(comment, solution){
                var list = document.querySelector(".list-answer");
                const tmp = Array.from(list.childNodes);
                tmp.forEach((item) => {
                    list.removeChild(item);
                })

                function handleDelete(id, index){
                    list.removeChild(document.getElementById(id));
                    const idx = mainPost.comments.lastIndexOf(comment[index]);
                    mainPost.comments.splice(idx, 1);
                    var num = document.querySelector("#numAnswers");
                    num.textContent = Number(num.textContent) - 1;
                }
                
                comment.forEach((item, index) => {
                    const c = document.createElement("custom-comment");
                    c.setAttribute("id", String(item.id))
                    c.setAttribute("url", "../../component/question/");
                    c.comment = item;
                    if(solution)
                        c.setAttribute("solution", solution);
                    if(item.owner.id == user){
                        c.onClickDetele = () => {
                            var tmpDelete = getAPI("/api/post/auth/delete/" + item.id);                
                            tmpDelete.then((data) => {
                                if(data && data.status == "SUCCESS"){
                                    handleDelete(item.id, index);
                                }
                            })
                        }
                    }
                    if(mainPost.owner.id == user){
                        c.onClickSolve = () => {
                            var tmpSolve = getAPI("/api/post/auth/solve/" + item.id);
                            tmpSolve.then((data) => {
                                if(data && data.status == "SUCCESS"){
                                    window.location.reload();
                                }
                            })
                        }
                    }
                    list.appendChild(c);
                })
            }

            var askedBy = document.querySelector(".askedBy");
            askedBy = Array.from(askedBy.childNodes);
            askedBy.forEach((item) => {
                item.href = "/user/" + mainPost.owner.id;
            })

            document.getElementById("numAnswers").textContent = mainPost.comments.length;

            document.querySelector(".name").textContent = mainPost.owner.username;        

            const data1 = [].concat(mainPost.comments).sort((a, b) => {
                if (new Date(a.createdDate).getTime() > new Date(b.createdDate).getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            showComment(data1, mainPost.solution?mainPost.solution.id:undefined);

            var oldest = document.getElementById("oldest");
            var vote = document.getElementById("votes");
            oldest.addEventListener("click", () => {
                if(vote.classList.contains("active")){
                    vote.classList.remove("active");
                    oldest.classList.add("active");
                    
                    const myData = [].concat(mainPost.comments).sort((a, b) => {
                        if (new Date(a.createdDate).getTime() > new Date(b.createdDate).getTime()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    });
                    showComment(myData, mainPost.solution?mainPost.solution.id:undefined);
                }
            })

            vote.addEventListener("click", () => {
                if(oldest.classList.contains("active")){
                    oldest.classList.remove("active");
                    vote.classList.add("active");
                    
                    const myData = [].concat(mainPost.comments).sort((a, b) => {
                        if (a.upvoteCount < b.upvoteCount) {
                            return 1;
                        } else {
                            return -1;
                        }
                    });
                    showComment(myData, mainPost.solution?mainPost.solution.id:undefined);
                }
            })

            var postRelate = document.querySelector(".related");
            const related = data.relatedPosts;
            related.forEach((item) => {
                const itemRelated = document.createElement("div");
                itemRelated.className = "itemRelated";
                const tmpDiv = document.createElement("div");
                tmpDiv.style = "overflow: initial";

                const num = document.createElement("div");
                num.className = "number";
                num.textContent = item.upvoteCount;

                tmpDiv.appendChild(num);

                const a = document.createElement("a");
                a.href = "/question/" + item.id;
                a.className = "title";
                a.textContent = item.title;

                itemRelated.appendChild(tmpDiv);
                itemRelated.appendChild(a);
                postRelate.appendChild(itemRelated);
            })

            var hot = document.querySelector("#hotQuestion");
            var tmpHot = getAPI("/api/post/hot");
            tmpHot.then((res) => {
                if(res && res.status == "SUCCESS"){
                    var hotQues = res.data;
                    hotQues.forEach((item) => {
                        const itemRelated = document.createElement("div");
                        itemRelated.className = "itemRelated";
                        const tmpDiv = document.createElement("div");
                        tmpDiv.style = "overflow: initial";

                        const num = document.createElement("div");
                        num.className = "number";
                        num.textContent = item.upvoteCount;

                        tmpDiv.appendChild(num);

                        const a = document.createElement("a");
                        a.href = "/question/" + item.id;
                        a.className = "title";
                        a.textContent = item.title;

                        itemRelated.appendChild(tmpDiv);
                        itemRelated.appendChild(a);
                        hot.appendChild(itemRelated);
                    })
                }
            })

            var content = document.getElementById("content");
            var code = document.getElementById("code");
            var form = document.getElementById("form-answer");

        
            voteUp.addEventListener("click", () => {  
                callVoteAPI(mainPost.id ,1);
                voteUp.classList.toggle("activeBookmark");
                if(voteDown.classList.contains("activeBookmark")){
                    voteDown.classList.remove("activeBookmark");
                }                     
            })
            
            voteDown.addEventListener("click", () => {
                callVoteAPI(mainPost.id, -1);
                voteDown.classList.toggle("activeBookmark");
                if(voteUp.classList.contains("activeBookmark")){
                    voteUp.classList.remove("activeBookmark");
                }                
            })
            
            form.onsubmit = (e) => {
                var tmpData = {
                    content: content.value,
                    code: code.value,
                    parent: {
                        id: mainPost.id
                    }
                }
                e.preventDefault();

                if(content.value == ""){
                    window.alert("Your answered must have some text!");
                } else {
                    var tempComment = postAPI("/api/post/auth/create", tmpData);
                    tempComment.then((data) => {
                        if(data && data.status == "SUCCESS"){
                            const c1 = document.createElement("custom-comment");
                            c1.setAttribute("url", "../../component/question/");
                            c1.comment = data.data;
                            c1.setAttribute("id", String(data.data.id));
                            if(data.data.owner.id == user){
                                c1.onClickDetele = () => {
                                    console.log("co vao day khong?");
                                    var tmpDeleteCmt = getAPI("/api/post/auth/delete/" + data.data.id);
                                    if(tmpDeleteCmt){
                                        tmpDeleteCmt.then((res) => {
                                            if(res.status == "SUCCESS"){
                                                document.querySelector(".list-answer").removeChild(document.getElementById(data.data.id));
                                                var num = document.querySelector("#numAnswers");
                                                num.textContent = Number(num.textContent) - 1;
                                                var idx = mainPost.comments.lastIndexOf(data.data);
                                                console.log(idx);
                                                console.log(data.data);
                                                mainPost.comments.splice(idx, 1);
                                                console.log(mainPost.comments);
                                            }
                                        })
                                    }
                                }
                            }
                            if(mainPost.owner.id == user){
                                c1.onClickSolve = () => {
                                    var tmpSolve = getAPI("/api/post/solve/" + item.id);
                                    tmpSolve.then((data) => {
                                        if(data && data.status == "SUCCESS"){
                                            window.location.reload();
                                        }
                                    })
                                }
                            }
                            document.querySelector(".list-answer").appendChild(c1);
                            content.value = "";
                            code.value = "";
                            document.querySelector("#numAnswers").textContent = mainPost.comments.length + 1;
                            mainPost.comments.push(data.data);
                        } else {
                            var r = window.confirm("Login required to comment, Login now?");
                            if (r === true) {
                                window.location.replace("/login");
                            } else {
                                content.value = "";
                                code.value = "";
                            }
                        }
                    })                  
                }           
            }

            //bookmark
            var bookmark = document.getElementById("bookmark");
            if(data.bookmark){
                bookmark.classList.add("activeBookmark");
            }
            bookmark.addEventListener("click", () => {
                var tmpBookmark = getAPI("/api/post/auth/bookmark/" + id);
                tmpBookmark.then((res) => {
                    if(res.status == "ACCESS_DENIED"){
                        var r = window.confirm("Login required to bookmark question, Login now?");
                        if (r === true) {
                            window.location.replace("/login");
                        } else {
                        }
                    } else if(res.status == "SUCCESS"){
                        bookmark.classList.toggle("activeBookmark");
                    }
                })
            })
        }        
    } 
    
    
}, false);