function buttonHandler() {

    // First create an XMLHttprequest object
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "/home/wallet/transactions", true); // ver qual é o json c as transações todas
    xhr.getResponseHeader("Content-type", "application/json");

    xhr.onload = function () {
        //let myjson = [{type: "John", amount: 30}, {type: "Ja", amount: 30}]
        const myjson= JSON.parse(this.responseText);
        if(this.readyState ===4 && this.status===200){
            for (let i in myjson) {
                let target = document.getElementById("type")
                target.innerHTML += "<span class='transactions-text31'>" + myjson[i].type + "</span>"
            }
            for (let i in myjson) {
                let target = document.getElementById("amount")
                target.innerHTML += "<span class='transactions-text31'>" + myjson[i].amount + "</span>"
            }

            /* Array.from(amount).forEach((amount, index) => {
                 amount.innerText = obj.array[index].amount;
             });
         }

         xhr.send();*/
        }
    }
}

setTimeout(() => {
    buttonHandler();
}, 100)