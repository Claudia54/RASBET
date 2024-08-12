<html>
<title>


</title>

<body>
    <?php

    echo "Login:";

    if (isset($_POST['email']) and isset($_POST['pwd'])) {
        $email = $_POST['email'];
        $pwd = $_POST['pwd'];

        include '../RASBET/RASBET_Facade.php';
        $model = new RASBET_Facade();

        $val = $model->login($email, $pwd);

        $role = $model->getRole($email);

        if ($val) {
            if ($role == "user") {
                header("Location: http://localhost:8080/RASBET_UI/Homepage.php");
            } else if ($role == "specialist") {
                header("Location: http://localhost:8080/RASBET_UI/HomepageSpecialist.php");
            } else if ($role == "admin") {
                header("Location: http://localhost:8080/RASBET_UI/HomepageAdmin.php");
            }
        }
    }
    echo "Error";
    ?>

    <form action="" method="post">
        <label>Email:</label> <input type="text" name="email" value='<?php echo $email; ?>' /><br><br>
        <label>Pwd:</label> <input type="text" name="pwd" value='<?php echo $pwd; ?>' /><br><br>
        <!--Não devia ser visivel-->
        <input name="form" type="submit" value="Submit" /><br><br>

    </form>

</body>


</html>


<!--<form onsubmit="console.log('You clicked submit.'); return false">
  <button type="submit">Submit</button>
</form>-->