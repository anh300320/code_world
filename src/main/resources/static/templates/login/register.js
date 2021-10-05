document.addEventListener("DOMContentLoaded", function(){
    const username = document.getElementById("username");
    const firstname = document.getElementById("firstname");
    const lastname = document.getElementById("lastname");
    const password = document.getElementById("password");
    const repassword = document.getElementById("re-password");

    function validatePassword(){
        var myInput = document.getElementById("password");
        var letter = document.getElementById("letter");
        var number = document.getElementById("number");
        var length = document.getElementById("length");

        // When the user clicks on the password field, show the message box
        myInput.onfocus = function() {
            document.getElementById("message").style.display = "block";
        }

        // When the user clicks outside of the password field, hide the message box
        myInput.onblur = function() {
            document.getElementById("message").style.display = "none";
        }

        // When the user starts to type something inside the password field
        myInput.onkeyup = function() {
        // Validate lowercase letters
            var lowerCaseLetters = /[A-Za-z]/g;
            if(myInput.value.match(lowerCaseLetters)) {
                letter.classList.remove("invalid");
                letter.classList.add("valid");
            } else {
                letter.classList.remove("valid");
                letter.classList.add("invalid");
            }

            // Validate numbers
            var numbers = /[0-9]/g;
            if(myInput.value.match(numbers)) {
                number.classList.remove("invalid");
                number.classList.add("valid");
            } else {
                number.classList.remove("valid");
                number.classList.add("invalid");
            }

            // Validate length
            if(myInput.value.length >= 8) {
                length.classList.remove("invalid");
                length.classList.add("valid");
            } else {
                length.classList.remove("valid");
                length.classList.add("invalid");
            }
        }
    }

    validatePassword();

    document.getElementById("formSignup").onsubmit =  (e) => {
        e.preventDefault();
        if(username.value == "" || firstname.value == "" || lastname.value == "" || password.value == "" || repassword.value == ""){
            window.alert("Please fill out the information to register!");
        } else {
            if(password.value == repassword.value){
                    var tmpData = {
                        username: username.value,
                        plainTextPassword: password.value,
                        firstname: firstname.value,
                        lastname: lastname.value
                    }
                    
                    fetch(
                        "/api/register", {
                        method:"POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(tmpData)
                    }).then(
                        response =>  {
                            return response.json().then((data) => {
                                if(data.status == "USER_EXISTED"){
                                    window.alert("Username existed!");
                                    username.focus();
                                } else if(data.status == "SUCCESS"){
                                    window.alert("Register success! Login now!");
                                    window.location.replace("/login");
                                } else if(data.status == "INVALID_PASSWORD"){
                                    window.alert("Password must contain at least one number and one letter, and at least 8 or more characters");
                                    password.focus();
                                }
                            })
                        },
                        error => {
                            console.log(error);
                        }
                    ).catch(
                        response => {
                            console.log(response);
                        }
                    )
                              
            } else {
                window.alert("Passwords do not match!");
            }
        }
    }

}, false);