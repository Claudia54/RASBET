function redirectProfile(x){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/wallet");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: x}));

    xmlhttp.onreadystatechange = function(){
        document.open();
        document.write(xmlhttp.response);
        document.close();
    };
}

function changeCurrency(email) {
    let coin = document.getElementById('currency').value;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/profile/currency");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email:email, new_coin: coin}));
    redirectProfile(email);
}

function changeInfo(email){
    let new_name = document.getElementById('newName').value;
    let new_email = document.getElementById('newEmail').value;
    let password = document.getElementById('newPp').value;
    let address = document.getElementById('newAddress').value;
    let phone = document.getElementById('newPhone').value;
    let token = document.getElementById('token').value;
    sendProfile(email,new_name,new_email,password,address,phone,token);

}
function sendProfile(email,new_name,new_email,password,address,phone,token){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/profile/edit");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email:email, new_name: new_name, new_email: new_email, new_password: password, new_address: address, new_phone:phone, token: token}));

    if(new_email !== "") redirectProfile(new_email);
    else redirectProfile(email);


}

function requestToken(email){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/profile/token");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email:email}));

    xmlhttp.onload = function(){
        alert(xmlhttp.response);

    };
}

function makeDeposit(x, montante){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/wallet/deposit");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: x, value: montante}));
}

function makeWithdraw(x, montante){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/home/wallet/withdraw");
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({email: x, value: montante}));
}

