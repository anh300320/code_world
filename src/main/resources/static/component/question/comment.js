class Comment extends HTMLElement{   

    connectedCallback(){
        var comment = this.comment;
        const root = document.createElement("div");
        root.className = "grid-question";

        const src = this.getAttribute("url") + "up.png";
        const srcVote = this.getAttribute("url") + "up-active.png";

        const left = document.createElement("div");
        left.className = "left";
        left.style = "padding-right: 16px;";

        const voteUp = document.createElement("div");
        voteUp.className = "voteUp";        

        const up = document.createElement("img");
        up.width = 37;
        if(comment.currentUserVote == 1){
            up.src = srcVote;
        } else {
            up.src = src;
        }
        
        voteUp.appendChild(up);

        const vote = document.createElement("div");
        vote.className = "vote";
        vote.style = "font-size: 20px;";
        vote.textContent = comment.upvoteCount;

        const voteDown = document.createElement("div");
        voteDown.className = "voteUp";
        
        const down = document.createElement("img");
        down.width = 37;
        if(comment.currentUserVote == -1){
            down.src = srcVote;
        } else {
            down.src = src;
        }
        down.style = "transform:rotate(180deg);";
        voteDown.appendChild(down);

        voteDown.addEventListener("click", () => {
            callVoteAPI(comment.id, -1);
        })

        voteUp.addEventListener("click", () => {
            callVoteAPI(comment.id, 1);
        })

        const solve = document.createElement("div");
        solve.className = "solve";

        const check = document.createElement("img");
        check.width = 37;
        if(this.getAttribute("solution") == comment.id){
            check.src = this.getAttribute("url") + "check.png";            
        } else {
            check.src = this.getAttribute("url") + "uncheck.png";
        }
        if(this.onClickSolve){
            solve.addEventListener("click", () => {
                this.onClickSolve();
            })
        }
        
        
        solve.appendChild(check);
        left.appendChild(voteUp);
        left.appendChild(vote);
        left.appendChild(voteDown);
        left.appendChild(solve);

        root.appendChild(left);

        const right = document.createElement("div");
        right.className = "right";

        const content = document.createElement("div");
        content.className = "question-content";
        content.textContent = comment.content;
        if(this.onClickDetele){
            const divBtn = document.createElement("div");
            divBtn.style = "height:20px";
            const deleteBtn = document.createElement("button");
            deleteBtn.className = "delete";
            deleteBtn.textContent = "Delete";
            deleteBtn.onclick = () => {
                this.onClickDetele();
            }
            divBtn.appendChild(deleteBtn);
            right.appendChild(divBtn);
        }

        right.appendChild(content);
        

        if(comment.code != null && comment.code != ""){
            const code = document.createElement("div");
            code.className = "code";

            const codeContent = document.createElement("code");
            codeContent.textContent = comment.code;
            code.appendChild(codeContent);

            right.appendChild(code);
        }       
        const user = document.createElement("div");
        user.className = "user";
        user.style = "padding: 5px 6px 7px 7px; background-color: #e1ecf4; margin-top: 10px;";

        const time = document.createElement("div");
        time.className = "time";
        time.innerHTML = "answered <span>" + new Date(comment.createdDate).toLocaleDateString("en-GB") + "</span>";

        const askedBy = document.createElement("div");
        askedBy.className = "askedBy";

        const link1 = document.createElement("a");
        link1.href = "/user/" + comment.owner.id;
        link1.innerHTML = '<div class="image"><img src="' + this.getAttribute("url") + 'user-01.png" width="32px"></div>';

        const link2 = document.createElement("a");
        link2.href = "/user/" + comment.owner.id;

        const name = document.createElement("div");
        name.className = "name";
        name.textContent = comment.owner.username;

        link2.appendChild(name);
        askedBy.appendChild(link1);
        askedBy.appendChild(link2);

        user.appendChild(time);
        user.appendChild(askedBy);
        right.appendChild(user);
        root.appendChild(right);

        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = this.getAttribute("url") + "question.css";

        const shadowRoot = this.attachShadow({mode: "closed"});
        shadowRoot.appendChild(style);
        shadowRoot.appendChild(root);

        function callVoteAPI(id, v){
            var tmpData = {
                itemId: id,
                vote: v
            }
            fetch(
                "/api/post/auth/upvote", {
                method:"POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(tmpData)
            }).then(
                response =>  {
                    return response.json().then((data) => {
                        if(data.status == "SUCCESS"){
                            window.location.reload();
                        }
                    })
                },
                error => {
                    console.log(error);
                }
            ).catch(
                response => {
                    console.log(response);
                    var r = window.confirm("Login required to vote, Login now?");
                    if (r === true) {
                        window.location.href = "/login";
                    } else {
                        
                    }
                }
            )
        }
    }    
}
customElements.define("custom-comment", Comment);