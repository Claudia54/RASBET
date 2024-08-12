function Ativar(elem){

    let xmlhttp = new XMLHttpRequest();
    let var1 = "/home/admin/promos/activatePromo";
    var path = var1.concat(elem.parentNode.id);
    xmlhttp.open("POST", path);
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send();

    xmlhttp.onreadystatechange = function(){
         document.open();
         document.write(xmlhttp.response);
         document.close();
    };

}

function Desativar(elem){

    let xmlhttp = new XMLHttpRequest();
    let var1 = "/home/admin/promos/deactivatePromo";
    var path = var1.concat(elem.parentNode.id);
    xmlhttp.open("POST", path);
    xmlhttp.setRequestHeader("Accept", "application/json");
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send();

    xmlhttp.onreadystatechange = function(){
         document.open();
         document.write(xmlhttp.response);
         document.close();
    };


}