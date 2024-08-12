function redirectSports(email, sportname) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/sports");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: email,SportName:sportname}));


    xmlhttp.onreadystatechange = function(){
        document.open();
        document.write(xmlhttp.response);
        document.close();
    };
}

function redirectReceipt(email,idGame,guess){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/bet");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: email,idGame:idGame,guess:guess}));

    setTimeout(() => {
        redirectSports(email, 'AllSports')
    }, 100);
}

function payWithWallet(email){
    let money = document.getElementById('montante').value;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/bet/payWallet");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email : email, money : money}));
    console.log(JSON.stringify({email : email, money : money}));

    setTimeout(() => {
        redirectSports(email, 'AllSports')
    }, 100);
}

function payTransference(email, montante){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/bet/payTransference");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email : email, money : montante}));
    console.log(JSON.stringify({email : email, money : montante}));
}

function removeFromBet(email, id){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/bet/remove");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email : email, idGame : id}));
    console.log(JSON.stringify({email : email, idGame : id}));

    setTimeout(() => {
        redirectSports(email, 'AllSports')
    }, 100);
}

function followGame(email,id){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/sports/follow");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email : email, idGame : id}));

    setTimeout(() => {
        redirectSports(email, 'AllSports')
    }, 100);
}

function unfollowGame(email,id){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/sports/unfollow");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email : email, idGame : id}));

    setTimeout(() => {
        redirectSports(email, 'AllSports')
    }, 100);
}
