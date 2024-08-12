
function MyFunction(x) {
    x.addEventListener("click", function () {
            let email = document.getElementById('email');
            let password = document.getElementById('pp');
            authenticate(email, password);
        }
    );
}
function authenticate(userName,password){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({name: userName, password: password}));
}

