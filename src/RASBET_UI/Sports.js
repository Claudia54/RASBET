
function AddJson(x) {
    x.addEventListener("click", function () {
        registerBet(email,id_game,guess)
        }
    );
}

function registerBet(email,id_game,guess){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email:email, idGame:id_game, guess:guess}));
}

//json : id_game ,  id participantestodos  , id_aposta, cota
//email, idgame , guess