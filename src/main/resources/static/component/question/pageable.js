class Pageable extends HTMLElement{
    connectedCallback(){
        var curr = Number(this.getAttribute("currentPage"));
        var totalPage = this.getAttribute("totalPage");

        const div = document.createElement("div");
        div.className = "item";
        div.style = "float: left; margin-left: 10px";

        const back = document.createElement("span");
        back.className = "numItem";
        back.textContent = "prev";
        if(curr == 1){
            back.style = "color: #eff0f1; cursor: default";
        }        

        const next = document.createElement("span");
        next.className = "numItem";
        next.textContent = "next";
        if(curr >= totalPage){
            next.style = "color: #eff0f1; cursor: default";
        }

        const num = document.createElement("span");
        num.className = "numItem";
        num.textContent = this.getAttribute("currentPage");

        div.appendChild(back);
        div.appendChild(num);
        div.appendChild(next);   
        back.addEventListener("click", () => {
            if(curr > 1){                
                curr -= 1;
                num.textContent = curr;
                if(curr == 1){
                    back.style = "color: #eff0f1; cursor: default";
                }
                if(curr < totalPage){
                    next.style = undefined;
                }
                this.clickBack();
            } 
        });

        next.addEventListener("click", () => {
            if(curr < totalPage){
                curr += 1;
                num.textContent = curr;
                if(curr == totalPage){
                    next.style = "color: #eff0f1; cursor: default";
                }
                if(curr > 1){
                    back.style = undefined;
                }
                this.clickNext();
            }            
        })
        const style = document.createElement("link");
        style.rel = "stylesheet";
        style.href = this.getAttribute("url") + "question.css";

        const shadowRoot = this.attachShadow({mode:"closed"});
        shadowRoot.appendChild(div);
        shadowRoot.appendChild(style);        
    }
}

customElements.define('custom-pageable', Pageable);