function redirectSpecSports(email, sportname) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/specialist/Sports");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: email,SportName:sportname}));


    xmlhttp.onreadystatechange = function(){
        document.open();
        document.write(xmlhttp.response);
        document.close();
    };
}


function MyFunction(elem, email){
    let part = elem.parentNode.id;
    let idGame2 = elem.parentNode.parentNode.parentNode.id;
    var parent = elem.parentNode.getElementsByTagName("input")[0];
    let newOdd2 = parent.value;
    if(newOdd2 !== "") {
        let xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", "/home/specialist/manageOdds/changeOdd");
        xmlhttp.setRequestHeader("Accept", "application/json");
        xmlhttp.setRequestHeader("Content-Type", "application/json");
        console.log(JSON.stringify({participant:part, idGame: idGame2 , newOdd: newOdd2 }));

        if(part === "X"){
            xmlhttp.send(JSON.stringify({participant:"Draw", idGame: idGame2 , newOdd: newOdd2 }));
        } else {
            xmlhttp.send(JSON.stringify({participant:part, idGame: idGame2 , newOdd: newOdd2 }));
        }

        redirectSpecSports(email, 'AllSports')

    }
}


