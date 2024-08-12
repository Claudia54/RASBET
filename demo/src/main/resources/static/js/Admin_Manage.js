function MyFunction(x) {
    x.addEventListener("click", function () {
            let role = document.getElementById('role').value;
            let nome = document.getElementById('nome').value;
            let email = document.getElementById('email').value;
            let password = document.getElementById('pp').value;
            let date =  document.getElementById('date').value;
            let morada = document.getElementById('morada').value;
            let telem = document.getElementById('telem').value;
            let cc = document.getElementById('cc').value;
            let nif = document.getElementById('nif').value;
            authenticate(role, nome, email, password, date, morada, telem, cc, nif);
        }
    );
}
function authenticate(role1, nome1, email1, password1, date1, morada1, telem1, cc1, nif1, codigo1){
    let xmlhttp = new XMLHttpRequest();
    if(role1 == "Administrador") {
        xmlhttp.open("POST", "/register/home/admin");
    }
    else{
        xmlhttp.open("POST", "/register/home/specialist");
    }
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({id:null, email: email1, name: nome1, pwd: password1, address: morada1, phone_number: telem1, nif: nif1, cc: cc1, birth: date1, role: 0, codeUser: codigo1}));

    xmlhttp.onreadystatechange = function(){
            alert(xmlhttp.response);
        };
}