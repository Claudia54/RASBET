let nome = document.getElementById('nome');
let apelido = document.getElementById('apelido');
let email = document.getElementById('email');
let password = document.getElementById('pp');
let morada= document.getElementById('morada');
let nif =  document.getElementById('nif');
let cc= document.getElementById('cc');
let date = document.getElementById('date');
let telemovel = document.getElementById('telemovel');

function MyFunctionAdmin(x){
    x.addEventListener("click", function () {
        let btn = document.getElementById("btn");
        btn.addEventListener("click", function () {

                authenticateadmin(nome,apelido,email, password,morada,nif,cc, date,telemovel);
            }
        );
    });

}


function authenticateadmin(nome,apelido,email, password,morada,nif,cc, date,telemovel){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/Register");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({name: nome, apelido: apelido,email:email,
        password: password,morada:morada,nif: nif,cc:cc, date: date, telemovel:telemovel}));
}


function MyFunctionEsp(x) {
    x.addEventListener("click", function () {
        let btn = document.getElementById("btn");
        btn.addEventListener("click", function () {
                authenticateesp(nome,apelido,email, password,morada,nif,cc, date,telemovel);
            }
        );
    });
}

function authenticateesp(nome,apelido,email, password,morada,nif,cc, date,telemovel){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/Register");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({name: nome, apelido: apelido,email:email,
        password: password,morada:morada,nif: nif,cc:cc, date: date, telemovel:telemovel}));
}
