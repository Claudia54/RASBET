package RASBET.RASBET;

import RASBET.RASBET_DATA.UserDAO;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFacade {
    private UserDAO udao; //functions access database related to Users

    Map<String, String> tokens = new HashMap<>();
    public static String admin = "admin";

    public static String user = "user";
    public static String specialist = "specialist";

    public UserFacade(){
        this.udao = new UserDAO();
    }

    /**
     * Function that checks if a string email as a valid format.
     * It returns true, if the format is valid
     * @param email String
     * @return boolean
     */
    public boolean check_format_email(String email){
        String regex =  "^(?=.*[@])" + //should have at least one @
                "(?=.*[.])" + //Should have at least one .
                "(?=\\S+$).{8,100}$"; // Can't have whitespaces
        //Size between [8,20]
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        boolean matchFound = matcher.find();
        if(matchFound)
            return true;
        else
            return false;
    }

    /**
     * Function that checks if the password is valid
     * Has at least one number, upper case, lower case, one special char and no white spaces
     * It returns true, if pwd has a valid format
     * @param pwd String
     * @return boolean
     */
    public boolean check_format_password(String pwd){
        String regex = "^(?=.*[0-9])" + //should have at least one number
                "(?=.*[a-z])" + //Should have at least one lower case
                "(?=.*[A-Z])" + //Should have at least one upper case
                "(?=.*[@#$%^&-+=()])" + //Should have at least one special character
                "(?=\\S+$).{8,45}$";    // Can't have whitespaces
        //Size between [8,20]
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pwd);
        boolean matchFound = matcher.find();
        if(matchFound)
            return true;
        else
            return false;
    }

    /**
     * Function that checks if the nif is composed by 9 ints.
     * Returns true if it is valid.
     * @param nif String
     * @return boolean
     */
    public boolean check_format_nif(String nif){
        String regex = "^[0-9]+$.{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nif);
        boolean matchFound = matcher.find();
        if(matchFound)
            return true;
        else
            return false;
    }

    /**
     * Function that checks if the cc is composed by 8 ints.
     * Returns true if it is valid.
     * @param cc String
     * @return boolean
     */
    public boolean check_format_cc(String cc){
        String regex = "^[0-9]+$.{8}$";
        //Size between [8,20]
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cc);
        boolean matchFound = matcher.find();
        if(matchFound)
            return true;
        else
            return false;
    }

    /**
     * Function that checks if the phonenumber is composed by 9 ints.
     * Returns true if it is valid.
     * @param phonenumber String
     * @return boolean
     */
    public boolean check_format_phone_number(String phonenumber){
        String regex = "^[0-9]+$.{9}$";
        //Size between [8,20]
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phonenumber);
        boolean matchFound = matcher.find();
        if(matchFound)
            return true;
        else
            return false;
    }

    /**
     * Function that checks if the user is older than 18
     * Returns true if it is older.
     * @param birth String
     * @return boolean
     */
    public boolean check_older_than_18(String birth){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthday = LocalDate.parse(birth,formatter);
        if(Period.between(birthday, LocalDate.now()).getYears() < 18){
            return false;
        }
        else return true;
    }

    /**
     * Function that checks if the email already exists in the db
     * Returns true if it already exists
     * @param email String
     * @return boolean
     */
    public boolean check_existence_email(String email){
        if (this.udao.verifiesExistence(email))
            return true;
        return false;
    }

    /**
     * Function that checks if the nif already exists in the db
     * Returns true if it already exists
     * @param nif String
     * @return boolean
     */
    public boolean check_existence_nif(String nif){
        if (this.udao.verifiesExistenceNIF(nif))
            return true;
        return false;
    }

    /**
     * Function that checks if the cc already exists in the db
     * Returns true if it already exists
     * @param cc String
     * @return boolean
     */
    public boolean check_existence_cc(String cc){
        if (this.udao.verifiesExistenceCC(cc))
            return true;
        return false;
    }

    /**
     * Function that verifies if a given UserCode "code" exists in the database.
     * If it exists, it is return true;
     * @param code String
     * @return boolean
     */
    public boolean existsUserCode(String code){
        return this.udao.existsUserCode(code);
    }

    /**
     * Function that generates a random string of length n
     * @param n int
     * @return String
     */
    public String generateCodeUser(int n){
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Function that allows to create an user with the data given and an empty wallet.
     * Exceptions are thrown when: the email doesn't follow a valid format, the email already exists, the password is not valid,
     * the nif already exists or doesn't follow a valid format, the cc already exists or doesn't follow a valid format, and the user is
     * not older than 18.
     * @param email String
     * @param name String
     * @param pwd String
     * @param address String
     * @param phone_number String
     * @param nif String
     * @param cc String
     * @param birth String
     * @param role String
     * @return boolean
     * @throws Exception
     */
    public boolean createUser(String email, String name, String pwd, String address, String phone_number, String nif, String cc, String birth, String role) throws Exception{
        if (!check_format_email(email) || check_existence_email(email))  // Method that verifies that the given email exist:
            throw new ExceptionEmail("The email \""+email+"\" already exists, or is not in a valid format.");

        if (!check_format_password(pwd)) //REGEX                         //Check if password has certain characteristics:
            throw new ExceptionPassword();

        if(!check_format_nif(nif) || check_existence_nif(nif))           //Verifies if the nif already exists in the BD
            throw new ExceptionNIF(nif);

        if(!check_format_cc(cc) || check_existence_cc(cc))               //Verifies if the cc exists in the BD
            throw new ExceptionCC(cc);

        if(!check_older_than_18(birth)){                                 //Verifies that the user is older than 18:
            throw new ExceptionAge();
        }
        DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(birth,formatterDB);
        String birthdayDB = birthday.toString();

        int n = 10;
        String code = generateCodeUser(n);
        while (existsUserCode(code)) generateCodeUser(n);

        this.udao.addUser(email, name, pwd, address, phone_number, nif, cc, birthdayDB, role, code);
        return true;
    }

    /**
     * Function that allows to create an staff (admin or specialist) with the data given
     * Exceptions are thrown when: the email doesn't follow a valid format, the email already exists, the password is not valid,
     * the nif already exists or doesn't follow a valid format, the cc already exists or doesn't follow a valid format, and the user is
     * not older than 18.
     * @param email String
     * @param name String
     * @param pwd String
     * @param address String
     * @param phone_number String
     * @param nif String
     * @param cc String
     * @param birth String
     * @param role String
     * @return boolean
     * @throws Exception
     */
    public boolean createStaff(String email, String name, String pwd, String address, String phone_number, String nif, String cc, String birth, String role) throws Exception{
        if (!check_format_email(email) || check_existence_email(email))   // Method that verifies that the given email exist:
            throw new ExceptionEmail("The email \""+email+"\" already exists, or is not in a valid format.");

        if (!check_format_password(pwd)) //REGEX                          //Check if password has certain characteristics:
            throw new ExceptionPassword();

        if(!check_format_nif(nif) || check_existence_nif(nif))            //Verifies if the nif already exists in the BD
            throw new ExceptionNIF(nif);

        if(!check_format_cc(cc) || check_existence_cc(cc))                //Verifies if the cc exists in the BD
            throw new ExceptionCC(cc);

        if(!check_older_than_18(birth)){                                  //Verifies that the user is older than 18:
            throw new ExceptionAge();
        }
        DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(birth,formatterDB);
        String birthdayDB = birthday.toString();
        this.udao.addStaff(email, name, pwd, address, phone_number, nif, cc, birthdayDB, role);
        return true;
    }

    /**
     * Function that allows to update the bonus balance of an user with email.
     * Throws an exception if the user is not found
     * @param email String
     * @param additional_bonus float
     */
    public void updateBonusBalance(String email, float additional_bonus) throws Exception {
        if(!udao.verifiesExistence(email)){
            throw new Exception("User not found");
        }
        float bonusBalance = udao.getBonusBalanceWallet(email);
        udao.updateBonusBalanceWallet(email, additional_bonus + bonusBalance);
    }

    /**
     * Function that can be used to login a user.
     * It returns true if the login is successful, and false if it is not.
     * @param email String
     * @param pwd String
     * @return boolean
     */
    public boolean login(String email, String pwd){
        return udao.login(email, pwd);
    }

    /**
     * Function that allows to consult the name of an user.
     * An exception is thrown if the user is not found.
     * @param email String
     * @return String
     * @throws Exception
     */
    public String getName(String email) throws Exception{
        String res = this.udao.getName(email);
        if(res.equals("")){
            throw new ExceptionEmail("This user doesn't exist.");
        }
        else return res;
    }

    /**
     * Function that allows to consult the role of an user.
     * If the user is not found, and exception is thrown.
     * @param email String
     * @return String
     * @throws Exception
     */
    public String getRole(String email) throws Exception{
        String res = this.udao.getRole(email);
        if(res.equals("")){
            throw new ExceptionEmail("User not found.");
        }
        else return res;
    }

    /**
     * Function that allows a user to change their name. An exception is thrown if the user is not found
     * @param email String
     * @param newName String
     * @throws Exception
     */
    public void changeName(String email, String newName) throws Exception{
        if(!this.udao.changeName(email, newName))
            throw new Exception("User not found");
    }

    /**
     * Function that allows an user to change their password.
     * If the password is not valid, or the user is not found, an exception is thrown
     * @param email String
     * @param newPwd String
     * @throws Exception
     */
    public void changePwd(String email, String newPwd) throws Exception{
        if(!check_format_password(newPwd)) {
            throw new ExceptionPassword();
        }
        else{
            if(!this.udao.changePwd(email, newPwd)){
                throw new ExceptionEmail("User not found.");
            }
        }
    }

    /**
     * Function that allows an user to change their email.
     * If the email already exists, the format of the email is not valid or the user doesn't exist is thrown an exception
     * @param email String
     * @param newEmail String
     * @throws Exception
     */
    public void changeEmail(String email, String newEmail) throws Exception{
        if(!check_format_email(newEmail) || check_existence_email(email)){
            throw new ExceptionEmail("The email \""+email+"\" already exists, or is not in a valid format.");
        }
        int id = this.udao.getIdUser(email);
        if(id == -1){
            throw new ExceptionEmail("User can't be found");
        }
        this.udao.changeEmail(id, newEmail);
    }



    public String generateToken(int n){
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public String getTokenIdentification(String email) throws Exception{
        if(!this.udao.verifiesExistence(email))
            throw new Exception("User not found.");

        if(this.tokens.get(email)!= null) {
            System.out.println("A token was send to verify your identity");
            return generateToken(10);
        }
        else return "";
    }


    /**
     * Function that allows an user to change their address. It returns Exception if user is not found
     * @param email String
     * @param newAddress String
     * @throws Exception
     */
    public void changeAddress(String email, String newAddress, String token) throws Exception{
        if(!this.udao.verifiesExistence(email))
            throw new Exception("User not found.");

        if(!this.tokens.get(email).equals(token))
            throw new Exception("Token invalid.");


        if(!this.udao.changeAddress(email,newAddress))
            throw new ExceptionEmail("User not found.");
        else
            this.tokens.remove(email);
    }

    /**
     * Function that allows an user to change the phone number.
     * It throws an Exception if the phone number doesn't have a valid format. Or if the user is not found.
     * @param email String
     * @param newPhoneNumber String
     * @throws Exception
     */
    public void changePhoneNumber(String email, String newPhoneNumber, String token) throws Exception{
        if(!check_format_phone_number(newPhoneNumber))
            throw new Exception("Phone number invalid");


        if(!this.tokens.get(email).equals(token))
            throw new Exception("Token invalid.");


        if(!this.udao.changePhoneNumber(email,newPhoneNumber))
            throw new Exception("User not found");
        else
            this.tokens.remove(email);
    }

    /**
     * Function that allows an user to add money to its wallet
     * It throws exceptions if the value is negative or null, or if the user doesn't exit
     * @param email String
     * @param value float
     * @throws Exception
     */
    public void deposit_money(String email, float value) throws Exception{
        if(value <= 0){
            throw new ExceptionNegativeValue();
        }
        if(!udao.verifiesExistence(email)){
            throw new Exception("User not found");
        }

        float currentValue = udao.getBalanceWallet(email);
        currentValue += value;
        udao.updateBalanceWallet(email, currentValue);

        this.addTransaction(email,Wallet.deposit, value);
    }

    /**
     * Function that removes the value of a wallet of the user with email.
     * It throws exception if value is negative, email doesn't exist, or the user doesn't have enough money in it's account.
     * @param email String
     * @param value float
     * @throws Exception
     */
    public void withdraw_money(String email, float value) throws Exception{
        if(value <= 0)                                              // The value withdraw should be positive
            throw new ExceptionNegativeValue();

        if(!this.udao.verifiesExistence(email))                     // User should exist in the DB
            throw new ExceptionEmail("User not found");

        float currentValue = udao.getBalanceWallet(email);
        if(value > currentValue)                                    // Value wished to withdraw should be superior to current balance
            throw new ExceptionNotEnoughMoney();

        currentValue -= value;
        udao.updateBalanceWallet(email, currentValue);


        this.addTransaction(email,Wallet.withdraw, value);
    }

    /**
     * Function that gets a list with all the transactions maid by a certain user.
     * It return an empty list if the user is not found or if the list of transactions is empty.
     * @param email String
     * @return List<String>
     */
    public List<String> getTransactions(String email){
        int idUser = this.udao.getIdUser(email);
        if(idUser == -1){               //User doesn't exist
            return new ArrayList<>();
        }
        List<Transaction> allTransactions = udao.getTransactions(idUser);
        List<String> lista = new ArrayList<>();
        for(Transaction t: allTransactions){
            lista.add(t.toString());
        }
        return lista;
    }


    /*
     * ----------------------------------------------------------------------------------------------------------------------
     */
    public String getWallet(String email) throws Exception{
        List<String> wallet = this.udao.consultWallet(email);
        if(wallet.isEmpty())
            throw new Exception("Wallet not found");

        StringBuilder sb = new StringBuilder("********************************* Wallet *********************************\n");
        sb.append("**************************************************************************\n");
        sb.append(" Balance       | "+ wallet.get(1) +" " + wallet.get(2)+"\n");
        sb.append("**************************************************************************\n");
        sb.append(" Bonus Balance | "+ wallet.get(3) +" " + wallet.get(2)+"\n");
        sb.append("**************************************************************************\n");
        return sb.toString();
    }

    public int getIdUser(String email){
        return this.udao.getIdUser(email);
    }


    public float getBalance(String email){
        return this.udao.getBalanceWallet(email);
    }

    public float getBonusBalance(String email){
        return this.udao.getBonusBalanceWallet(email);
    }

    public boolean hasEnoughMoney(String email, float valueToPay) throws Exception{
        if(valueToPay <= 0)
            throw new Exception("Value to pay should be positive");

        if(!this.udao.verifiesExistence(email))
            throw new Exception("User not found");

        float currentBalance = this.udao.getBalanceWallet(email) + this.udao.getBonusBalanceWallet(email);
        if(currentBalance >= valueToPay){
            return true;
        }
        else{
            return false;
        }
    }


    public void reduceMoneyPayBet(String email, float valueToPay) throws Exception{
        if(valueToPay <= 0)                                          //Valor a pagar deve ser positivo
            throw new Exception("Value to pay should be positive");

        if(!this.udao.verifiesExistence(email))                      //User deve existir
            throw new Exception("User not found");

        float bonusBalance = this.udao.getBonusBalanceWallet(email);
        if(bonusBalance > 0){                                       //Se utilizador tiver saldo bonus, deve descontar daqui primeiro
            if(bonusBalance >= valueToPay){                         //Bonus chega para pagar a bet
                bonusBalance -= valueToPay;                         //reduz saldo bonus e atualiza bd
                this.udao.updateBonusBalanceWallet(email, bonusBalance);
            }
            else{
                valueToPay -= bonusBalance;                         //Bonus não chega para pagar a bet; reduz o valor que tem para pagar
                bonusBalance = 0;
                this.udao.updateBonusBalanceWallet(email, bonusBalance);    //Coloca o saldo bonus a 0 e atualiza bd

                float currentBalance = this.udao.getBalanceWallet(email);   //Vai buscar o resto do saldo
                currentBalance -= valueToPay;                               //Reduz saldo e atualiza bd
                this.udao.updateBalanceWallet(email, currentBalance);
            }
        }
    }

    public void addTransaction(String email, String type, float amount){
        int idUser = this.udao.getIdUser(email);
        this.udao.addTransaction(idUser, type, amount);
    }


}
