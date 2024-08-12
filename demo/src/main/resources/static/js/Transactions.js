function redirectTransactions(email) {
    const xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/wallet/transactions");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: email}));


    xmlhttp.onreadystatechange = function(){

        document.open();
        document.write(xmlhttp.response);
        document.close();
    };

}
