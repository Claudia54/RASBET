function MyFunction(x) {
    x.addEventListener("click", function () {
            let email = document.getElementById('email').value;
            let password = document.getElementById('pp').value;
            authenticate(email, password);
        }
    );

}
function authenticate(userName,password){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: userName, password: password}));

        xmlhttp.onreadystatechange = function(){
            document.open();
            document.write(xmlhttp.response);
            document.close();
        };
}