class Profile extends HTMLElement{
    // need attribute "user" is a string json
    // attribute "url" is a url to profile folder
    connectedCallback(){
        const user = JSON.parse(this.getAttribute("user"));
        console.log(this.getAttribute("user"));
        const userProfile = document.createElement("div");
        userProfile.className="user-profile";

        const top = document.createElement("div");
        top.className = "grid block";
        const avatar = document.createElement("div");
        avatar.className = "avatar";
        const img = document.createElement("img");
        img.src = this.getAttribute("url") + "user-01.png";
        img.width = 164;
        img.height = 164;
        avatar.appendChild(img);
        const repu = document.createElement("div");
        repu.className = "reputation";
        repu.innerHTML = '<span id="reputation">' + user.reputation + '</span> Reputation';
        avatar.appendChild(repu);
        top.appendChild(avatar);

        const profile = document.createElement("div");
        profile.className = "profile";
        const name = document.createElement("div");
        name.className = "name";
        name.textContent = user.fullname;
        const about = document.createElement("div");
        about.className = "about";
        about.textContent = user.about;
        profile.appendChild(name);
        profile.appendChild(about);
        top.appendChild(profile);

        userProfile.appendChild(top);

        const createdDate = document.createElement("div");
        createdDate.className = "createdDate";
        createdDate.innerHTML = '<img src="' + this.getAttribute("url") + 'history.png" width=15px>' + 'Member from <span id="createdDate">' + new Date(user.createdDate).toLocaleDateString("en-GB") + '</span>';
        userProfile.appendChild(createdDate);

        const div = document.createElement("div");
        div.innerHTML = '<div style="font-size: 25px; color: #535a60; margin-left:5px">Activity Summary</div>';
       

        const summary = document.createElement("div");
        summary.className = "grid-col";
        summary.appendChild(div);
        const question = document.createElement("div");
        question.className = "card";
        const title = document.createElement("div");
        title.className = "title";
        title.textContent = "Questions";
        question.appendChild(title);
        const num = document.createElement("div");
        num.className = "num";
        num.textContent = user.numQuestions + " questions";
        question.appendChild(num);
        summary.appendChild(question);

        const answer = document.createElement("div");
        answer.className = "card";
        const title1 = document.createElement("div");
        title1.className = "title";
        title1.textContent = "Answers";
        answer.appendChild(title1);
        const num1 = document.createElement("div");
        num1.className = "num";
        num1.textContent = user.numAnswers + " answers";
        answer.appendChild(num1);
        summary.appendChild(answer);

        const tag = document.createElement("div");
        tag.className = "card";
        const title2 = document.createElement("div");
        title2.className = "title";
        title2.textContent = "Tags";
        tag.appendChild(title2);
        const num2 = document.createElement("div");
        num2.className = "num";
        num2.textContent = user.numTags + " questions";
        tag.appendChild(num2);
        summary.appendChild(tag);

        top.appendChild(summary);

        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = this.getAttribute("url") + "profile.css";

        const shadowRoot = this.attachShadow({mode: "closed"});
        shadowRoot.appendChild(style);
        shadowRoot.appendChild(userProfile);
    }
}

customElements.define("custom-profile", Profile);