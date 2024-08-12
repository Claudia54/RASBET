<html>
    <title>


    </title>
    <body>
        <?php
            echo "Hello Novato";

            
            if (isset($_POST['email']) and isset($_POST['pwd']) and isset($_POST['birth']) and isset($_POST['nif'])) {
                $email = $_POST['email'];
                $pwd = $_POST['pwd'];
                $birth = $_POST['birth'];
                $nif = $_POST['nif'];
            
                include '../RASBET/RASBET_Facade.php';
                $model = new RASBET_Facade();
                $val = $model->register($email, $pwd, $birth, $nif);

                if(!$val){
                    echo 'user not registered';
                }
                else{
                   
                    header("Location: http://localhost:8080/RASBET_UI/Homepage.php");
                    
                }
            }


        ?>

        <form action="" method="post">
            <label>Email:</label> <input type="text" name="email" value='<?php echo $email; ?>'/><br><br>
            <label>Pwd:</label> <input type="text" name="pwd" value='<?php echo $pwd; ?>'/><br><br>
            <label>Nif:</label> <input type="date" name="birth" value='<?php echo $birth; ?>'/><br><br>
            <label>Nif:</label> <input type="text" name="nif" value='<?php echo $nif; ?>'/><br><br>


            <!--Não devia ser visivel-->
            <input name="form" type="submit" value="Submit"/><br><br>

        </form>

    </body>


</html>