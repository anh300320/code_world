class AskForm extends HTMLElement{
    connectedCallback(){
        const question = this.question;
        const container = document.createElement("div");

        const title = document.createElement("div");
        title.className = "pageTitle";
        title.style = "margin-bottom: 10px;";
        title.textContent = "Edit your question";

        const form = document.createElement("form");

        const label1 = document.createElement("label");
        label1.htmlFor = "title";
        label1.textContent = "*Title";

        const input1 = document.createElement("input");
        input1.className = "input";
        input1.type = "text";
        input1.name = "title";
        input1.id = "title";
        input1.placeholder = "e.g. Is there an R function for finding the index of an element in a vector?";
        input1.value = question.title;

        const label2 = document.createElement("label");
        label2.htmlFor = "content";
        label2.textContent = "Content";

        const input2 = document.createElement("textarea");
        input2.className = "input";
        input2.name = "content";
        input2.id = "content";
        input2.rows = 4;
        input2.placeholder = "Your question place here";
        input2.value = question.content;

        const label3 = document.createElement("label");
        label3.htmlFor = "code";
        label3.textContent = "Your code";

        const input3 = document.createElement("textarea");
        input3.className = "input";
        input3.name = "code";
        input3.rows = 4;
        input3.id = "code";
        input3.placeholder = "input your code";
        input3.value = question.code;

        const label4 = document.createElement("label");
        label4.htmlFor = "tags";
        label4.textContent = "Tags";

        const input4 = document.createElement("input");
        input4.className = "input";
        input4.type = "text";
        input4.name = "tags";
        input4.id = "tags";
        input4.placeholder = "Add tags to describe your question separated by commas";
        input4.value = question.tags.toString();

        const submit = document.createElement("input");
        submit.type = "submit";
        submit.className = "button";
        submit.value = "Save change";
        submit.style = "display: block; float: none; margin-top: 15px";

        form.appendChild(label1);
        form.appendChild(input1);
        form.appendChild(label2);
        form.appendChild(input2);
        form.appendChild(label3);
        form.appendChild(input3);
        form.appendChild(label4);
        form.appendChild(input4);
        form.appendChild(submit);

        form.onsubmit = (e) => {
            e.preventDefault();
            if(input1.value == null || input1.value == "" || input2.value == null || input2.value == ""){
                window.alert("Your question must have title and content!");
            } else {
                var tmp = Array.from(input4.value.split(","));
                var tmp1 = [];
                if(tmp){
                    tmp.forEach((item) => {
                        tmp1.push(item.trim());
                    })
                }
                const tmpData = {
                    id: question.id,
                    title: input1.value,
                    content: input2.value,
                    tags: tmp1,
                    code: input3.value
                }

                //api save change post
                // $.ajax({
                //     type: "POST",
                //     url: "/api/post/update",
                //     data: JSON.stringify(tmpData),
                //     success: function(result){
                //         if(result.status == "SUCCESS"){
                //             window.alert("Saved change!");
                //             window.location.replace("/question/" + question.id);
                //         }
                //     },
                //     dataType: 'json',
                //     contentType:'application/json',
                //     error : function(e) {
                //         var r = window.confirm("Login required to comment, Login now?");
                //         if (r === true) {
                //             window.location.replace("/login");
                //         } else {
                            
                //         }
                //     }
                // });

                fetch(
                    "/api/post/auth/update", {
                    method:"POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(tmpData),
                    'credentials': 'same-origin'
                }).then(
                    response =>  {
                        return response.json().then((data) => {
                            if(data.status == "SUCCESS"){
                                window.alert("Saved change!");
                                window.location.replace("/question/" + question.id);
                            }
                        })
                    },
                    error => {
                        console.log("e: " + error);
                    }
                ).catch(
                    response => {
                        console.log("r: " + response);
                        var r = window.confirm("Login required to comment, Login now?");
                        if (r === true) {
                            window.location.replace("/login");
                        } else {
                            
                        }
                    }
                )
            }
        }

        container.appendChild(title);
        container.appendChild(form);

        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = this.getAttribute("url") + "question.css";

        const shadowRoot = this.attachShadow({mode: "closed"});
        shadowRoot.appendChild(style);
        shadowRoot.appendChild(container);
    }
}
customElements.define("edit-form", AskForm);