document.addEventListener("DOMContentLoaded", function(){
    var dropdown = document.querySelector(".dropbtn");
    var toggleMenu = document.querySelector(".dropdown-content");
    dropdown.addEventListener('click', function(ev){
        toggleMenu.classList.toggle('active');
        ev.stopPropagation();
    }, false)
    window.addEventListener('click', function(){
        if(toggleMenu.classList.contains('active'))
            toggleMenu.classList.remove('active');
    }, false);
}, false);