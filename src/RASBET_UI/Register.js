function MyFunction(x) {
    x.addEventListener("click", function () {
            let email = document.getElementById('email');
            let password = document.getElementById('pp');
            let nif =  document.getElementById('nif');
            let date = document.getElementById('date');
            authenticate(email, password, nif, date);
        }
    );
}
function authenticate(userName,password,nif,date){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/Register");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({name: userName, password: password,nif: nif, date: date}));
}