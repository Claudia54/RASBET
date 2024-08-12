<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RASBET-Sports</title>
</head>

<body>
    <h1 id="Site">RASBET</h1>

    <?php
    include '../RASBET/RASBET_Facade.php';
    $model = new RASBET_Facade();
    $list_sports = $model->getNameSports();

    $reps = count($list_sports);
    for ($x = 0; $x < $reps; $x++) {
        echo '
            <a href="#' . $list_sports[$x] . '">' . $list_sports[$x] . '</a>
        ';
    }


    $allGames = $model->getAllGames();
    print_r($allGames);

    for ($x = 0; $x < $reps; $x++) {
        echo '
            <h2 id = ' . $list_sports[$x] . '>' . $list_sports[$x] . '</h2>
            <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Debitis voluptatum quia a esse, similique commodi alias soluta corporis officia at eos placeat officiis optio doloribus, aperiam nesciunt ducimus delectus illum unde neque id praesentium reprehenderit fugiat laudantium. Fugiat debitis unde animi ex a, tempora iste perspiciatis nam tempore officiis quod, voluptates minima velit repudiandae obcaecati! Sequi totam saepe minus quae maiores exercitationem aliquid, praesentium repellat quia suscipit numquam molestiae alias voluptates rem voluptatibus laborum. Ut, libero, facere dolorum debitis suscipit nam, quis error facilis repudiandae omnis asperiores sequi. Explicabo cupiditate iste repudiandae! Aut facere, ad culpa omnis, consequatur quam autem quisquam illo adipisci vel ab eius quia cupiditate quas maiores illum natus porro fugit assumenda in velit magnam enim quidem quis. Cupiditate, modi quasi. Nemo corporis nulla dolores eum excepturi quod dolore, rem veritatis labore accusantium esse quaerat maxime, libero unde recusandae. Impedit culpa repellendus sint eaque error mollitia minima esse. Inventore velit, tempore reprehenderit possimus doloribus quo facere, rem itaque odio id, mollitia corrupti. Libero eveniet quos molestiae fuga voluptas voluptate quia voluptates necessitatibus expedita ipsam quod, laborum sunt soluta suscipit, ex dignissimos quibusdam nam quis ratione vero provident accusantium. Modi dolorem doloremque ea ullam optio veritatis voluptatibus magnam.</p>
        ';
    }
    ?>

    <h2 id=teste> TESTE</h2>
    <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Debitis voluptatum quia a esse, similique commodi alias soluta corporis officia at eos placeat officiis optio doloribus, aperiam nesciunt ducimus delectus illum unde neque id praesentium reprehenderit fugiat laudantium. Fugiat debitis unde animi ex a, tempora iste perspiciatis nam tempore officiis quod, voluptates minima velit repudiandae obcaecati! Sequi totam saepe minus quae maiores exercitationem aliquid, praesentium repellat quia suscipit numquam molestiae alias voluptates rem voluptatibus laborum. Ut, libero, facere dolorum debitis suscipit nam, quis error facilis repudiandae omnis asperiores sequi. Explicabo cupiditate iste repudiandae! Aut facere, ad culpa omnis, consequatur quam autem quisquam illo adipisci vel ab eius quia cupiditate quas maiores illum natus porro fugit assumenda in velit magnam enim quidem quis. Cupiditate, modi quasi. Nemo corporis nulla dolores eum excepturi quod dolore, rem veritatis labore accusantium esse quaerat maxime, libero unde recusandae. Impedit culpa repellendus sint eaque error mollitia minima esse. Inventore velit, tempore reprehenderit possimus doloribus quo facere, rem itaque odio id, mollitia corrupti. Libero eveniet quos molestiae fuga voluptas voluptate quia voluptates necessitatibus expedita ipsam quod, laborum sunt soluta suscipit, ex dignissimos quibusdam nam quis ratione vero provident accusantium. Modi dolorem doloremque ea ullam optio veritatis voluptatibus magnam.</p>


</body>


</html>