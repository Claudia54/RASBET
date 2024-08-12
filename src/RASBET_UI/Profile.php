
   <html>
    <title>
    </title>
    <body>
        <?php 
         echo "Edit";
        include '../RASBET/RASBET_Facade.php';
        $model = new RASBET_Facade();
        session_start(); //começa a sessão
        if($_SESSION['user']){

        }else {
            header("location: index.php");
        }
        $user = $_SESSION['user'];
        ?>
        <body>
            <?php
            $name = $_POST['name'];
                if(!empty($def['name'])){
                    $name = $_GET['name'];
                    $_SESSION['name'] = $name;
                    $model->changeName($name);
                }
            $email = $_POST['email'];
                if(!empty($def['email'])){
                    $email = $_GET['email'];
                    $_SESSION['email'] = $email;
                    $email->changeEmail();
            }
            $password = $_POST['password'];
                if(!empty($def['password'])){
                    $password = $_GET['password'];
                    $_SESSION['password'] = $password;
                    $model->changePwd();
                }
            
            $numero = $_POST['phone number'];
                if(!empty($def['phone number'])){
                    $phonenumber = $_GET['phone number'];
                    $_SESSION['phone number'] = $phonenumber;
                    $model->changePhoneNumber();

                }
            $adress = $_POST['Adress'];
            
?>
            
           <!-- 
            //$number="DELETE INTO " parte q apaga da base de dados o antigo email e poe mo novo
            
       /* echo "Edit";
        include '../RASBET/RASBET_Facade.php';
        $model = new 
        // utilzador escreve o seu email atual e a sua pp atual -> assumi isto provavelmente vai ter q ser alterado
        if (isset($_POST['New Email']){
            $val = $model->changeName();
        
            $email = $_POST['email'];

        if (isset($_POST['New Name']){

            <form method="post" action="{next_page}">
   <input type="hidden" name="cat" value="{category}" />
   <input type="hidden" name="var1" value="val1" />
   <input type="hidden" name="var2" value="val2" />
   <input type="submit" value="Button_TO_Next_Page" />
</form>
https://stackoverflow.com/questions/12546915/getting-data-from-previous-page
         


    

        
        

