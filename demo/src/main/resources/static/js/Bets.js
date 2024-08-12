function redirectBets(email) {
    const xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/betHistory");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(email);


    xmlhttp.onreadystatechange = function(){
        console.log(xmlhttp.response);
        document.open();
        document.write(xmlhttp.response);
        document.close();
    };

}