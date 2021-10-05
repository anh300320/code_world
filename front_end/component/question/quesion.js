class Question extends HTMLElement{
    // need attribute "question" is a string json
    // attribute "url" is a url to quesion folder
    // attribute "currUser" is id of user who being login
    connectedCallback(){
        const question = JSON.parse(this.getAttribute("question"));
        const userLink = ""; //api link to userDetail page
        const tagLink = ""; //api link to questions of a tag page
        const questionLink = ""; //api link to question detail page
        const parentLink = ""; //api link to parent if comment
        const gridQuestion = document.createElement("div");
        gridQuestion.className = "grid-question";

        const left = document.createElement('div');
        left.className = "grid-item";
        left.style = "text-align: center; display: grid; grid-template-rows: 50% 50%;"

        const vote = document.createElement("div");
        vote.className = "vote text-left";

        const num = document.createElement("span");
        num.textContent = question.upvoteCount;

        const div = document.createElement('div');
        div.textContent = "Votes";

        vote.appendChild(num);
        vote.appendChild(div);
        left.appendChild(vote);

        const anwser = document.createElement("div");
        anwser.className = "answer text-left";

        if(!question.parent){
            const num1 = document.createElement("span");
            num1.textContent = question.comments.length;
            const div1 = document.createElement('div');
            div1.textContent = "Answers";
            anwser.appendChild(num1);
            anwser.appendChild(div1);
        } else if(question.solve){
            const check = document.createElement("img");
            check.src = this.getAttribute("url") + "check.png";
            check.width = 20;
            anwser.appendChild(check);
        } else {
            const check = document.createElement("img");
            check.src = this.getAttribute("url") + "uncheck.png";
            check.width = 20;
            anwser.appendChild(check);
        }

        
        left.appendChild(anwser);
        gridQuestion.appendChild(left);

        const right = document.createElement("div");
        right.className = "grid-item";

        const title = document.createElement('div');
        title.className = "title";        
        if(question.title){
            title.style = "margin-bottom: 5px";
            const link = document.createElement("a");
            link.href = questionLink;
            link.textContent = question.title;
            title.appendChild(link);
        }        

        //if current user same with owner show delete button
        if(this.getAttribute("currUser") == question.owner.id){
            const deleteBtn = document.createElement("button");
            deleteBtn.className = "delete";
            deleteBtn.textContent = "Delete";
            deleteBtn.onclick = () => {
                //function onClick delete button
                console.log("da click");
                window.location.reload();
            }
            title.appendChild(deleteBtn);
        }       

        const content = document.createElement("div");
        content.className = "content";
        content.textContent = question.content;
        right.appendChild(title);
        right.appendChild(content);

        const bottom = document.createElement("div");
        bottom.className = "bottom";

        const tags = document.createElement("div");
        tags.className = "tags";

        if(question.tags){
            question.tags.map((item) => {
                var tag = document.createElement("a");
                tag.href = tagLink + item;
                tag.textContent = item;
                tags.appendChild(tag);
            })
        }

        bottom.appendChild(tags);

        const user = document.createElement("div");
        user.className = "user";

        const time = document.createElement("div");
        time.className = "time";
        time.innerHTML = "asked <span>" + new Date(question.createdDate).toLocaleDateString("en-GB") + "</span>";
        user.appendChild(time);
        const askedBy = document.createElement("div");
        askedBy.className = "askedBy";
        const link1 = document.createElement("a");
        link1.href = userLink;
        const image = document.createElement("div");
        image.className = "image";
        const img = document.createElement("img");
        img.width = 32;
        img.src = this.getAttribute("url") + "user-01.png";
        image.appendChild(img);
        link1.appendChild(image);

        const link2 = document.createElement("div");
        link2.href = userLink;

        const name = document.createElement("div");
        name.className = "name";
        if(question.owner){
            name.textContent = question.owner.username;
        }        
        link2.appendChild(name);
        askedBy.appendChild(link1);
        askedBy.appendChild(link2);
        user.appendChild(askedBy);
        bottom.appendChild(user);
        right.appendChild(bottom);

        gridQuestion.appendChild(right);

        if(question.parent){
            const tmp = document.createElement("div");
            const view = document.createElement("a");
            view.textContent = "View post >>";
            view.href = parentLink;
            view.className = "text-left";
            tmp.appendChild(view);
            left.appendChild(tmp);
        }

        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = this.getAttribute("url") + "question.css";

        const shadowRoot = this.attachShadow({mode: "closed"});
        shadowRoot.appendChild(style);
        shadowRoot.appendChild(gridQuestion);
    }
}
customElements.define('custom-question', Question);