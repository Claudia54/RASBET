package RASBET.RASBET;

import javax.sound.midi.SysexMessage;
import java.util.List;
import java.util.Scanner;

class Interpretador {
    public I_RASBET_Facade model;
    private Scanner scan;

    public Interpretador(I_RASBET_Facade model) {
        this.model = model;
        this.scan = new Scanner(System.in);
    }

    // Função que permite fazer o login e devolve o email se for bem sucedido:
    public String login() {
        System.out.print("EMAIL >");
        String email = scan.nextLine();
        System.out.print("PASSWORD >");
        String pwd = scan.nextLine();
        boolean success = this.model.login(email, pwd);
        if (success) {
            return email;
        } else {
            System.out.println("Email or Password not valid. User cannot be authenticated.");
            return "";
        }
    }

    // Função que permite registar um novo user e devolve o email do novo utilizador
    // se for bem sucessido:
    public String registar() {
        System.out.print("EMAIL >");
        String email = scan.nextLine();
        System.out.print("NAME >");
        String name = scan.nextLine();
        System.out.print("PASSWORD >");
        String pwd = scan.nextLine();
        System.out.print("ADDRESS >");
        String address = scan.nextLine();
        System.out.print("PHONE NUMBER (9 chars)>");
        String phone_number = scan.nextLine();
        System.out.print("NIF (9 chars)>");
        String nif = scan.nextLine();
        System.out.print("CC (8 chars)>");
        String cc = scan.nextLine();
        System.out.print("BIRTHDAY (dd/mm/yyyy)>");
        String birth = scan.nextLine();
        System.out.print("Friend code(if you don't have one please press Enter)>");
        String friendCode = scan.nextLine();
        boolean success = false;
        try {
            success = this.model.registerUser(email, name, pwd, address, phone_number, nif, cc, birth, friendCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (success)
            return email;
        else
            return "";
    }

    /*
     * ----------------- Functionalities User---------------------
     */
    // Acabar de implementar outras funcionalidades
    public void editProfile(String email) {
        int op = 0;
        System.out.println(
                "Diferentes parametros alteráveis vão ser apresentados.\nSe não pretender alterar algum dos campos, deixe o valor em branco.");

        System.out.print("NAME >");
        String newName = this.scan.nextLine();
        if (!newName.equals("")) {
            try {
                this.model.changeName(email, newName);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        System.out.print("EMAIL >");
        String newEmail = this.scan.nextLine();
        if(!newEmail.equals("")){
            try{
                this.model.changeEmail(email, newEmail);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        System.out.print("PASSWORD >");
        String newPwd = this.scan.nextLine();
        if (!newPwd.equals("")) {
            try {
                this.model.changePwd(email, newPwd);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.print("ADDRESS >");
        String newAddress = this.scan.nextLine();
        if (!newAddress.equals("")) {
            try {
                String t = this.model.getTokenIdentification(email);
                System.out.println("Your identification token is: "+t);
                System.out.print("Introduce authentication token >");
                String token = this.scan.nextLine();
                this.model.changeAddress(email, newAddress, token);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        System.out.print("PHONE NUMBER (9CHARS)>");
        String newTelemovel = this.scan.nextLine();
        if (!newTelemovel.equals("")) {
            try {
                String t = this.model.getTokenIdentification(email);
                System.out.println("Your identification token is: "+t);
                System.out.print("Introduce authentication token >");
                String token = this.scan.nextLine();
                this.model.changePhoneNumber(email, newTelemovel, token);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }



    public void listgames(String email) {
        int op = 0;
        do {
            System.out.println(
                    " \n\n > 1: Football \n > 2: Basket\n > 3: Bet\n > 4: Pay Bet \n > 5: Show Bet \n > 6: Exit");
            op = this.scan.nextInt();
            this.scan.nextLine();

            switch (op) {
                case 1:

                    List<String> gamesFootball = this.model.get_Games_of_Sport("Football");
                    System.out.println(
                            "*******************************************************************************************************************************************");
                    System.out.println(
                            "**             Id_Game            | Id_Sport |     State     |    Home    |  V1  |  X  |  V2  |    Away    |     outcome    |    Date    **");
                    System.out.println(
                            "**********************************|**********|***************|************|******|*****|******|************|*******************************");
                    System.out.println(
                            "**********************************|**********|***************|************|******|*****|******|************|*******************************");

                    for (String s : gamesFootball) {
                        System.out.println(s);
                        System.out.println(
                                "*******************************************************************************************************************************************");
                    }
                    break;
                case 2:
                    List<String> gamesBasket = this.model.get_Games_of_Sport("Basket");
                    System.out.println(
                            "*******************************************************************************************************************************************");
                    System.out.println(
                            "**             Id_Game            | Id_Sport |     State     |    Home    |  V1  |  V2  |    Away    |     outcome    |    Date    **");
                    System.out.println(
                            "**********************************|**********|***************|************|******|******|************|*******************************");
                    System.out.println(
                            "**********************************|**********|***************|************|******|******|************|*******************************");
                    for (String s : gamesBasket) {
                        System.out.println(s);
                    }
                    break;
                case 3:
                    System.out.print("ID GAME >");
                    String idGame = this.scan.nextLine();
                    System.out.print("GUESS (V1/X/V2) >");
                    String guess = this.scan.nextLine();
                    try {
                        this.model.add_to_bet(email, idGame, guess);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Money > ");
                    String aux = this.scan.nextLine();
                    float money = Float.valueOf(aux);
                    try {
                        this.model.setBet(email, money);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println(this.model.showBet(email));
                    break;
                default:
                    // verifica se nenhuma bet ficou com not set
                    break;
            }
        } while (op != 6);
    }

    public void WalletMenuDeposit(String email) {
        int op = 0;

        boolean success = false;
        do {
            System.out.print("\n*** Deposit Money ***\nVALUE >");
            String aux = this.scan.nextLine();
            float value = Float.valueOf(aux);

            System.out.print("Choose Method of Tranfer:\n> 1: Transfer\n> 2: MBWay\n> 3: Credit Card (Visa)\n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();

            switch (op) {
                case 1: // Transfer
                    System.out.println("TRANSFER:\n     Reference: notreal.");
                    break;
                case 2: // MBWay
                    System.out.print("MBWay:\n    Phone Number >");
                    String phone_number = this.scan.nextLine();
                    break;
                case 3: // Credit Card
                    System.out.print("CREDIT CARD:\n    Credit Card Number >");
                    String credit_card = this.scan.nextLine();
                    break;
                default:
                    break;
            }
            try {
                this.model.deposit_money(email, value);
                success = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Deposit not completed.");
            }
        } while (!success);
    }

    public void WalletMenuWithdrawal(String email) {
        int op = 0;
        boolean success = false;
        do {
            System.out.print("\n*** Withdraw Money ***\nVALUE >");
            String aux = this.scan.nextLine();
            float value = Float.valueOf(aux);

            System.out.print("Choose Method of Tranfer:\n> 1: Transfer\n> 2: MBWay\n> 3: Credit Card (Visa)\n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();

            switch (op) {
                case 1: // Transfer
                    System.out.println("TRANSFER:\n     Reference: notreal.");
                    break;
                case 2: // MBWay
                    System.out.print("MBWay:\n    Phone Number >");
                    String phone_number = this.scan.nextLine();
                    break;
                case 3: // Credit Card
                    System.out.print("CREDIT CARD:\n    Credit Card Number >");
                    String credit_card = this.scan.nextLine();
                    break;
                default:
                    break;
            }
            try{
                this.model.withdraw_money(email, value);
                success = true;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Withdraw not possible.");
                success = true;
            }
        } while (!success);

    }

    public void WalletMenu(String email) {
        int op = 0;
        do {
            String wallet = "";
            try {
                wallet = this.model.consult_Wallet(email);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            String optionsWallet = "> 1: Deposit Money.\n> 2: Withdraw Money.\n> 3: See all transactions.\n> 4: Exit";
            System.out.println(wallet + optionsWallet);
            op = this.scan.nextInt();
            this.scan.nextLine();
            switch (op) {
                case 1:
                    WalletMenuDeposit(email);
                    break;
                case 2:
                    WalletMenuWithdrawal(email);
                    break;
                case 3:
                    System.out.println("+----------+---------+");
                    System.out.println("|   type   |  value  |");
                    System.out.println("+----------+---------+");
                    List<String> allTransactions = this.model.getTransactions(email);
                    for(String s: allTransactions){
                        System.out.println(s);
                    }
                    break;
                case 4:
                    break;
            }
        } while (op != 4);

    }

    public void HomepageUser(String email) {
        int op = 0;
        String name = "";
        try {
            name = this.model.getName(email);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        String aux = "***************************";
        for (int i = 0; i <= name.length(); i++) {
            aux += "*";
        }
        do {
            System.out.print(aux + "\n********* Welcome " + name
                    + " *********\n> 1: Edit Profile\n> 2: Wallet \n> 3: Bets history\n> 4: Games \n> 5: Exit\n");

            op = this.scan.nextInt();
            this.scan.nextLine();

            switch (op) {
                /*case 1:
                    this.model.deleteAccount(email);
                    break;*/
                case 1:
                    editProfile(email);
                    break;
                case 2:
                    WalletMenu(email);
                    break;
                case 3:
                    List<Bet> lista = this.model.consult_Bets_History(email);
                    for (Bet b : lista) {
                        System.out.println(b.toString());
                    }
                    break;
                case 4:
                    listgames(email);
                    break;
                default:
                    break;
            }
        } while (op != 5 && op != 1);
        this.model.removeBetsNotSet(email);
    }

    /*
     * ----------------- Functionalities Admin---------------------
     */
    // Function that can be used by the admin to register new staff
    public void registerStaff() {
        System.out.print("ROlE(1-ADMIN | 2-SPECIALIST) >");
        int role = scan.nextInt();
        scan.nextLine();
        System.out.print("EMAIL >");
        String email = scan.nextLine();
        System.out.print("NAME >");
        String name = scan.nextLine();
        System.out.print("PASSWORD >");
        String pwd = scan.nextLine();
        System.out.print("ADDRESS >");
        String address = scan.nextLine();
        System.out.print("PHONE NUMBER (9 chars)>");
        String phone_number = scan.nextLine();
        System.out.print("NIF (9 chars)>");
        String nif = scan.nextLine();
        System.out.print("CC (8 chars)>");
        String cc = scan.nextLine();
        System.out.print("BIRTHDAY (dd/mm/yyyy)>");
        String birth = scan.nextLine();
        boolean success = false;
        try {
            if (role == 1)
                success = this.model.registerAdmin(email, name, pwd, address, phone_number, nif, cc, birth);
            else
                success = this.model.registerSpecialist(email, name, pwd, address, phone_number, nif, cc, birth);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (success) {
            System.out.println("New member registed.");
        } else {
            System.out.println("New member not added.");
        }
    }

    public void changeStateGameMenuAux(String email) {
        int op = 0;
        String idGame = "";

        do {
            System.out.print("> 1: Open game.\n> 2: Close game.\n> 3: Suspend game.\n> 4: Exit\n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();
            if (op == 1 || op == 2 || op == 3) {
                System.out.print("ID GAME >");
                idGame = this.scan.nextLine();
            }
            switch (op) {
                case 1:
                    try {
                        this.model.openGame(idGame);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        this.model.closeGame(idGame);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        this.model.suspendGame(idGame);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        } while (op != 4);

    }

    public void changeStateGameMenu(String email) {
        int op = 0;

        do {
            System.out.print(
                    "> 1: Change State of a Football Game. \n> 2: Change State of a Basketball Game. \n> 3: Exit.\n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();
            switch (op) {
                case 1:
                    List<String> gamesFootball = this.model.get_Games_of_Sport("Football");
                    for (String s : gamesFootball) {
                        System.out.println(s);
                    }
                    changeStateGameMenuAux(email);
                    break;
                case 2:
                    List<String> gamesBasket = this.model.get_Games_of_Sport("Basket");
                    for (String s : gamesBasket) {
                        System.out.println(s);
                    }
                    changeStateGameMenuAux(email);
                    break;
                default:
                    break;
            }
        } while (op != 3);

    }

    public void HomepageAdmin(String email) {
        int op = 0;
        String name = "";
        try {
            name = this.model.getName(email);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        String aux = "***************************";
        for (int i = 0; i <= name.length(); i++) {
            aux += "*";
        }

        do {
            System.out.print(aux+"\n********* Welcome "+name+" *********\n>0: End Game (beta). \n> 1: Register New Staff Member. \n> 2: Change State of a Game. \n> 3: Add/Change Game result. \n> 4: Activate/Deactivate Promotions. \n> 6: Exit.\n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();
            switch (op) {

                case 0:
                    System.out.print("ID GAME >");
                    String idGame = this.scan.nextLine();
                    System.out.print("RESULT (axb) >");
                    String result = this.scan.nextLine();
                    try {
                        this.model.endGame(idGame, result);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 1:
                    registerStaff();
                    break;
                case 2:
                    changeStateGameMenu(email);
                    break;
                case 3:
                    boolean exit = false;
                    do {
                         System.out.println(
                                    "\n > 1: Football \n > 2: Basket\n > 3: Exit\n");
                            op = this.scan.nextInt();
                            this.scan.nextLine();

                            switch (op) {
                                case 1:
                                    List<String> gamesFootball = this.model.get_Games_of_Sport("Football");
                                    System.out.println(
                                            "******************************************************************************************************************************");
                                    System.out.println(
                                            "**             Id_Game            | Id_Sport |     State     |    Home    |  V1  |  X  |  V2  |    Away    |     outcome    **");
                                    System.out.println(
                                            "**********************************|**********|***************|************|******|*****|******|************|******************");
                                    System.out.println(
                                            "**********************************|**********|***************|************|******|*****|******|************|******************");

                                    for (String s : gamesFootball) {
                                        System.out.println(s);
                                        System.out.println(
                                                "******************************************************************************************************************************");
                                    }
                                    break;
                                case 2:
                                    List<String> gamesBasket = this.model.get_Games_of_Sport("Basket");
                                    System.out.println(
                                            "******************************************************************************************************************************");
                                    System.out.println(
                                            "**             Id_Game            | Id_Sport |     State     |    Home    |  V1  |  X  |  V2  |    Away    |     outcome    **");
                                    System.out.println(
                                            "**********************************|**********|***************|************|******|*****|******|************|******************");
                                    System.out.println(
                                            "**********************************|**********|***************|************|******|*****|******|************|******************");
                                    for (String s : gamesBasket) {
                                        System.out.println(s);
                                    }
                                    break;
                                case 3:
                                    exit = true;
                                    break;
                            }
                            if(!exit) {
                                System.out.println("Insert game id to add/change result");
                                String id = this.scan.nextLine();
                                System.out.println("Insert game result(ex: 3x1)");
                                String res = this.scan.nextLine();
                                boolean b = this.model.addResult(id, res);
                                if (b) System.out.println("Game result added/changed with SUCCESS!");
                                else
                                    System.out.println("Game result NOT added/changed! Game DOES NOT exist or result inserted not in the expected syntax");
                            }
                    }while (op!=3);
                    break;
                case 4:
                    System.out.println("Promotions available:");
                    List<String> promotions = this.model.getPromotions();
                    for(int i = 0; i < promotions.size(); i++){
                        System.out.println("> "+(i+1)+": "+promotions.get(i));
                    }
                    System.out.println("> 3: Exit.");
                    System.out.print("Choose a promotion to activate.\n**>");
                    int promo = this.scan.nextInt();
                    this.scan.nextLine();
                    switch (promo){
                        case 1:
                            System.out.println("Change bonus to active");
                            this.model.activatePromoBonus();
                            break;
                        case 2:
                            System.out.println("Change multiplier to active");
                            this.model.activatePromoMultiplier();
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }
        } while (op != 5);
    }

    /*
     * ----------------- Functionalities Specialist---------------------
     */

    public void ChangeOddMenuWithDraw(String email){

        int op = 0; String idGame=""; float newOdd=-1;
        do {
            System.out.print("\n> 1: Change Odd Home Team.\n> 2: Change Odd Draw.\n> 3: Change Odd Away Team.\n> 4: Exit.\n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();

            if(op != 4) {
                System.out.print("ID GAME > ");
                idGame = this.scan.nextLine();
                System.out.print("NEW ODD > ");
                String aux = this.scan.nextLine();
                newOdd = Float.valueOf(aux);
            }

            switch (op){
                case 1:
                    try {
                        this.model.changeOddHomeTeamGame(idGame, newOdd);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        this.model.changeOddDrawGame(idGame, newOdd);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        this.model.changeOddAwayTeamGame(idGame, newOdd);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }while(op != 4);

    }

    public void ChangeOddMenuWithoutDraw(String email){
        int op = 0; String idGame=""; float newOdd=-1;
        do {
            System.out.print("\n> 1: Change Odd Home Team.\n> 2: Change Odd Away Team.\n> 3: Exit.\n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();

            if(op != 3) {
                System.out.print("ID GAME > ");
                idGame = this.scan.nextLine();
                System.out.print("NEW ODD > ");
                String aux = this.scan.nextLine();
                newOdd = Float.valueOf(aux);
            }

            switch (op){
                case 1:
                    try {
                        this.model.changeOddHomeTeamGame(idGame, newOdd);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        this.model.changeOddAwayTeamGame(idGame, newOdd);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }while(op != 3);

    }

    public void HomepageSpecialist(String email) {
        int op = 0;
        String name = "";
        try {
            name = this.model.getName(email);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        String aux = "***************************";
        for (int i = 0; i <= name.length(); i++) {
            aux += "*";
        }
        do {
            System.out.print(aux + "\n********* Welcome " + name + " *********\n> 1: Football. \n> 2: Basket. \n> 3: Exit. \n**>");
            op = this.scan.nextInt();
            this.scan.nextLine();
            switch (op) {
                case 1:
                    List<String> gamesFootball = this.model.get_Games_of_Sport("Football");
                    for (String s : gamesFootball) {
                        System.out.println(s);
                    }
                    ChangeOddMenuWithDraw(email);
                    break;
                case 2:
                    List<String> gamesBasket = this.model.get_Games_of_Sport("Basket");
                    for (String s : gamesBasket) {
                        System.out.println(s);
                    }
                    ChangeOddMenuWithoutDraw(email);
                    break;
                default:
                    break;
            }
        } while (op != 3);
    }

    /*
     * ----------------- Main ---------------------
     */
    public void run() {
        String email = "";
        while (email.equals("")) {
            System.out
                    .print("***************************\n********* Welcome ********* \n> 1: LOGIN\n> 2: REGISTAR\n**>");
            int op = this.scan.nextInt();
            this.scan.nextLine();
            email = "";
            switch (op) {
                case 1:
                    email = login();
                    break;
                case 2:
                    email = registar();
                    break;
                default:
                    email = ".";
                    break;
            }
        }
        String role = "";
        try {
            role = this.model.getRole(email);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if (role.equals("specialist"))
            HomepageSpecialist(email);
        else if (role.equals("admin"))
            HomepageAdmin(email);
        else
            HomepageUser(email);
    }
}

class Main {
    public static void main(String[] args) {
        I_RASBET_Facade model = new RASBET_Facade();
        Interpretador UI = new Interpretador(model);
        UI.run();
    }
}