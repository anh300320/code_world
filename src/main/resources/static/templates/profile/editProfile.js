document.addEventListener("DOMContentLoaded", function(){
    fetch(
    "/api/auth/info", {
        method:"GET",
        headers: {
            "Content-Type": "application/json"
        },
        'credentials': 'same-origin'
    }).then(
        response =>  {
            return response.json().then((data) => {
                if(data.status == "SUCCESS"){
                    showData(data.data);
                } else {
                    window.location.replace("/login");
                }
            })
        },
        error => {
            console.log(error);
            window.location.replace("/login");
        }
    )    

    function showData(user){
        var firsname = document.getElementById("firstname");
        var lastname = document.getElementById("lastname");
        var role = document.getElementById("role");
        var about = document.getElementById("about");
        var username = document.getElementById("username");
        var form = document.querySelector("#formEdit");

        firsname.value = user.firstName;
        lastname.value = user.lastName;
        role.value = user.role;
        about.value = user.about;
        username.value = user.username;

        

        form.addEventListener("submit", (e) => {
            e.preventDefault();
            console.log("submit");
            var tmpData = {
                id: user.id,
                firstname: firsname.value,
                lastname: lastname.value,
                about: about.value
            }

            if(firsname.value == "" || lastname.value == ""){
                window.alert("Firstname and Lastname is required!");
            } else {
                fetch("/api/user/auth/edit", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    'credentials': 'same-origin',
                    body: JSON.stringify(tmpData)
                }).then(
                    response =>  {
                        return response.json().then((data) => {
                            if(data.status == "SUCCESS"){
                                window.alert("Your change saved!");
                                window.location.replace("/user/" + user.id);
                            } else {
                                window.alert("Something wrong!");
                            }
                        })
                    },
                    error => {
                        console.log(error);
                        window.alert("Something wrong!");
                    }
                )    
            }
            
        })
        
        var changePassword = document.getElementById("changePassword");
        changePassword.addEventListener("click", (e) => {
            e.preventDefault();
            console.log("click change password");
            document.getElementById("formChange").classList.toggle("activeForm");
        })

        document.getElementById("formChange").addEventListener("submit", (e) => {
            e.preventDefault();
            var oldPass = document.getElementById("oldPass");
            var newPass = document.getElementById("newPass");
            var reNew = document.getElementById("reNew");
            if(oldPass.value == "" || newPass.value == "" || reNew.value == ""){
                window.alert("You must fill out all field to change password!");
            } else if(newPass.value != reNew.value){
                window.alert("Passwords do not match!");
            } else {
                var tmpData = {
                    currentPassword: oldPass.value,
                    newPassword: newPass.value
                }
                fetch("/api/user/auth/change-password", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(tmpData)
                }).then(
                    response => {
                        return response.json().then((data) => {
                            if(data.status == "PASSWORD_NOT_MATCH"){
                                window.alert("Your current password does not match with the password you provided. Please try again!");
                            } else if(data.status == "USER_NOT_FOUND"){
                                console.log("user not found!");
                            } else if(data.status == "SUCCESS"){
                                window.alert("Password saved change!");
                                window.location.replace("/user/" + user.id);
                            } else {
                                console.log("unknown error");
                            }
                        })
                    },
                    error => {
                        console.log(error);
                    }
                )
            }
        })
    }
}, false);