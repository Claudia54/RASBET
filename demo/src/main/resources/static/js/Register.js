function MyFunction(x) {
    x.addEventListener("click", function () {
            let nome = document.getElementById('nome').value;
            let email = document.getElementById('email').value;
            let password = document.getElementById('pp').value;
            let date =  document.getElementById('date').value;
            let morada = document.getElementById('morada').value;
            let telem = document.getElementById('telem').value;
            let cc = document.getElementById('cc').value;
            let nif = document.getElementById('nif').value;
            let codigo = document.getElementById('codigo').value;
            //console.log(email);
            authenticate(nome, email, password, date, morada, telem, cc, nif, codigo);
        }
    );
}
function authenticate(nome1, email1, password1, date1, morada1, telem1, cc1, nif1, codigo1){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/register/home");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    console.log(JSON.stringify({id:null, email: email1, name: nome1, pwd: password1, address: morada1, phone_number: telem1, nif: nif1, cc: cc1, birth: date1, role: null, codeUser: codigo1}));
    xmlhttp.send(JSON.stringify({id:null, email: email1, name: nome1, pwd: password1, address: morada1, phone_number: telem1, nif: nif1, cc: cc1, birth: date1, role: null, codeUser: codigo1}));

    xmlhttp.onreadystatechange = function(){
            document.open();
            document.write(xmlhttp.response);
            document.close();
        };
}