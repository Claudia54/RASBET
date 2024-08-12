function redirectAdminSports(email, sportname) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/admin/Sports");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: email,SportName:sportname}));


    xmlhttp.onreadystatechange = function(){
        document.open();
        document.write(xmlhttp.response);
        document.close();
    };
}

function redirectAdminPromo(email) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/admin/promos");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(email);

    xmlhttp.onreadystatechange = function(){
        document.open();
        document.write(xmlhttp.response);
        document.close();
    };
}

function redirectAdminManage(email) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/admin_manage");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(email);

    xmlhttp.onreadystatechange = function(){
        document.open();
        document.write(xmlhttp.response);
        document.close();
    };
}
function Open(elem) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/admin/manageGames/openGame");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");

    let idGame2 = elem.parentNode.parentNode.id;
    xmlhttp.send(JSON.stringify({ idGame: idGame2 }));

    xmlhttp.onreadystatechange = function(){
            alert("Jogo Aberto.");
        };
}

function Suspend(elem) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/admin/manageGames/suspendGame");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");

    let idGame2 = elem.parentNode.parentNode.id;
    xmlhttp.send(JSON.stringify({ idGame: idGame2 }));

    xmlhttp.onreadystatechange = function(){
            alert("Jogo Suspendido.");
        };
}

function Close(elem) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/admin/manageGames/closeGame");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");

    let idGame2 = elem.parentNode.parentNode.id;
    xmlhttp.send(JSON.stringify({ idGame: idGame2 }));

    xmlhttp.onreadystatechange = function(){
            alert("Jogo Encerrado.");
        };
}

function UpdateResult(elem) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/admin/manageGames/changeResult");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");

    let idGame2 = elem.parentNode.parentNode.id;
    let results = elem.parentNode.getElementsByTagName("input");
    let result1 = (results[0].value).concat("x").concat(results[1].value);
    xmlhttp.send(JSON.stringify({ idGame: idGame2 , result: result1}));

    xmlhttp.onreadystatechange = function(){
            alert("Resultado atualizado.");
        };
}

function UpdateResultMoto(elem) {
    let name = document.getElementById('moto').value;
    let idGame = elem.parentNode.parentNode.id;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/admin/manageGames/changeResult");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({ idGame: idGame , result: name}));

    xmlhttp.onreadystatechange = function(){
        alert("Resultado atualizado.");
    };
}