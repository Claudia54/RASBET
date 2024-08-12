package com.example.demo.RASBET_LN.User;

import com.example.demo.RASBET_DATA.UserDAO;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFacade {
    private UserDAO udao; //functions access database related to Users

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
        String regex = "(?=.*[0-9])" + //should have at least one number
                "(?=.*[a-z])" + //Should have at least one lower case
                "(?=.*[A-Z])" + //Should have at least one upper case
                "(?=.*[@#$%^&-+=()*_])" + //Should have at least one special character
                "(?=\\S+$).{8,45}";    // Can't have whitespaces
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
     * **>2
     * EMAIL >mariana@email.ot
     * NAME >Mariana
     * PASSWORD >Mfilipa_785
     * ADDRESS >rua de braga
     * PHONE NUMBER (9 chars)>123456342
     * NIF (9 chars)>678678678ng
     * @return boolean
     */
    public boolean check_format_nif(String nif){
        String regex = "^[0-9]{9}$";
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
        String regex = "^[0-9]{8}$";
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
        String regex = "^[0-9]{9}$";
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
    public String generateRandomToken(int n)
    {
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
            throw new Exception("The email \""+email+"\" already exists, or is not in a valid format.");
;
        if (!check_format_password(pwd)) //REGEX                         //Check if password has certain characteristics:
            throw new Exception();

        if(!check_format_nif(nif) || check_existence_nif(nif))           //Verifies if the nif already exists in the BD
            throw new Exception(nif);

        if(!check_format_cc(cc) || check_existence_cc(cc))               //Verifies if the cc exists in the BD
            throw new Exception(cc);

        if(!check_older_than_18(birth)){                                 //Verifies that the user is older than 18:
            throw new Exception();
        }
        DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(birth,formatterDB);
        String birthdayDB = birthday.toString();

        int n = 10;
        String code = generateRandomToken(n);
        while (existsUserCode(code)) generateRandomToken(n);

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
            throw new Exception("The email \""+email+"\" already exists, or is not in a valid format.");

        if (!check_format_password(pwd)) //REGEX                          //Check if password has certain characteristics:
            throw new Exception();

        if(!check_format_nif(nif) || check_existence_nif(nif))            //Verifies if the nif already exists in the BD
            throw new Exception(nif);


        if(!check_format_cc(cc) || check_existence_cc(cc))                //Verifies if the cc exists in the BD
            throw new Exception(cc);


        if(!check_older_than_18(birth)){                                  //Verifies that the user is older than 18:
            throw new Exception();
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
        String currency = this.udao.getCoin(email);
        if(!currency.equals(Transaction.euro)){
            float valCoin = this.udao.getValueCoin(currency);
            additional_bonus *= valCoin;
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
            throw new Exception("This user doesn't exist.");
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
            throw new Exception("User not found.");
        }
        else return res;
    }

    /**
     * Function that gets the usercode of the user with the given email. It returns "" if the usercode is not found
     * @param email String
     * @return String
     */
    public String getCodAmigo(String email) throws Exception{
        String res = this.udao.getUserCode(email);
        if(res.equals("")){
            throw new Exception("User not found.");
        }
        else return res;
    }

    /**
     * Function that allows a user to change their name. An exception is thrown if the user is not found
     * @param email String
     * @param newName String
     * @throws Exception
     * @return boolean
     */
    public boolean changeName(String email, String newName) throws Exception{
        boolean s = this.udao.changeName(email, newName);
        if(!s)
            throw new Exception("User not found");
        return s;
    }

    /**
     * Function that allows an user to change their password.
     * If the password is not valid, or the user is not found, an exception is thrown
     * @param email String
     * @param newPwd String
     * @throws Exception
     * @return boolean
     */
    public boolean changePwd(String email, String newPwd) throws Exception{
        boolean s = false;
        if(!check_format_password(newPwd)) {
            throw new Exception();
        }
        else{
            s = this.udao.changePwd(email, newPwd);
            if(!s){
                throw new Exception("User not found.");
            }
        }
        return s;
    }

    /**
     * Function that allows an user to change their email.
     * If the email already exists, the format of the email is not valid or the user doesn't exist is thrown an exception
     * @param email String
     * @param newEmail String
     * @throws Exception
     * @return boolean
     */
    public boolean changeEmail(String email, String newEmail) throws Exception{
        if(!check_format_email(newEmail) || check_existence_email(newEmail)){
            throw new Exception("The email \""+newEmail+"\" already exists, or is not in a valid format.");
        }
        int id = this.udao.getIdUser(email);
        if(id == -1){
            throw new Exception("User can't be found");
        }
        return this.udao.changeEmail(id, newEmail);
    }

    /*
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
    */

    /**
     * Function that allows the system to generate and store a new token to verify the identity of a user.
     * This token is going to be used by a user to verify their identity and edit their profile.
     * It throws an exception if the user is not found.
     * @param email String
     * @return String
     * @throws Exception
     */
    public String getTokenIdentification(String email) throws Exception{
        if(!this.udao.verifiesExistence(email))
            throw new Exception("User not found.");

        if(this.udao.verifyTokenExistence(email)) { //Se vier a verdade é porque está vazio
            System.out.println("A token was send to verify your identity");
            String token = generateRandomToken(10);
            this.udao.addToken(email,token);
            return token;
        }
        else return "";
    }


    /**
     * Function that allows an user to change their address. It returns Exception if user is not found
     * @param email String
     * @param newAddress String
     * @param token String
     * @throws Exception
     * @return boolean
     */
    public boolean changeAddress(String email, String newAddress, String token) throws Exception{
        boolean b = false;
        if(!this.udao.verifiesExistence(email))
            throw new Exception("User not found.");

        if(!this.udao.verifyToken(email,token))
            throw new Exception("Token invalid.");


        if(!this.udao.changeAddress(email,newAddress))
            throw new Exception("User not found.");

        return true;
    }

    /**
     * Function that allows an user to change the phone number.
     * It throws an Exception if the phone number doesn't have a valid format. Or if the user is not found.
     * @param email String
     * @param newPhoneNumber String
     * @throws Exception
     */
    public boolean changePhoneNumber(String email, String newPhoneNumber, String token) throws Exception{
        if(!check_format_phone_number(newPhoneNumber))
            throw new Exception("Phone number invalid");


        if(!this.udao.verifyToken(email,token))
            throw new Exception("Token invalid.");


        if(!this.udao.changePhoneNumber(email,newPhoneNumber))
            throw new Exception("User not found");


        return true;
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
        addTransaction(email,Transaction.deposit, value);
    }

    /**
     * Function that removes money from the wallet of the user with the given email email.
     * It throws exception if value is negative, email doesn't exist, or the user doesn't have enough money in it's account.
     * @param email String
     * @param value float
     * @throws Exception
     */
    public void withdraw_money(String email, float value) throws Exception{
        if(value <= 0)                                              // The value withdraw should be positive
            throw new ExceptionNegativeValue();

        if(!this.udao.verifiesExistence(email))                     // User should exist in the DB
            throw new Exception("User not found");

        float currentValue = udao.getBalanceWallet(email);
        if(value > currentValue)                                    // Value wished to withdraw should be superior to current balance
            throw new ExceptionNotEnoughMoney();

        currentValue -= value;
        udao.updateBalanceWallet(email, currentValue);

        addTransaction(email,Transaction.withdraw, value);
    }

    /**
     * Function that gets a list with all the transactions maid by a certain user.
     * It return an empty list if the user is not found or if the list of transactions is empty.
     * @param email String
     * @return List<Map<String,String>>
     */
    public List<Map<String,String>> getTransactions(String email){
        int idUser = this.udao.getIdUser(email);
        if(idUser == -1){               //User doesn't exist
            return new ArrayList<>();
        }
        List<Transaction> allTransactions = udao.getTransactions(idUser);

        List<Map<String , String>> l = new ArrayList<>();
        for (Transaction t : allTransactions){
            l.add(t.returnMap());
        }
        return l;
    }

    /*
     * ----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Function that allows to get the Wallet of a certain user with email.
     * This function returns the content of the Wallet not structure.
     * The keys in this Map are : "balance", "bonusBalance" and "currency".
     * The values are the values of each field of the Wallet
     * @param email String
     * @return Map<String, String>
     * @throws Exception
     */
    public Map<String,String> getWallet(String email) throws Exception{
        Wallet w = this.udao.consultWallet(email);
        if(w == null){
            throw new Exception("Wallet not found");
        }
        return w.returnMap();
    }

    public int getIdUser(String email){
        return this.udao.getIdUser(email);
    }


    /**
     * Function that gets the balance of the wallet of the user with the given email.
     * It returns 0 if the wallet is not found.
     * @param email String
     * @return float
     */
    public float getBalance(String email){
        return this.udao.getBalanceWallet(email);
    }

    public float getBonusBalance(String email){
        return this.udao.getBonusBalanceWallet(email);
    }


    /**
     * Function that allows to check if user has enough money in the wallet.
     * It throws an exception if value is negative, or user not found.
     * It returns true if it has enough money, false if value is not enough
     * @param email String
     * @param valueToPay float
     * @return boolean
     * @throws Exception
     */
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
        else{
            float currentBalance = this.udao.getBalanceWallet(email);   //Vai buscar o resto do saldo
            currentBalance -= valueToPay;                               //Reduz saldo e atualiza bd
            this.udao.updateBalanceWallet(email, currentBalance);
        }
    }

    /**
     * Function that allows to add a transaction to the database.
     * If the user doesn't exist the transaction is not added.
     * @param email String
     * @param type String
     * @param amount float
     */
    public void addTransaction(String email, String type, float amount) throws Exception{
        int idUser = this.udao.getIdUser(email);
        if(idUser == -1)
            throw new Exception("User not found.");

        this.udao.addTransaction(idUser, type, amount);
    }

    public void updateBalanceWallet(String email, float earnings){
        float currentValue = this.udao.getBalanceWallet(email);
        currentValue += earnings;
        this.udao.updateBalanceWallet(email, currentValue);
    }

    /**
     * Function that returns the value of the coin in relation with EUR
     * @param coin String
     * @return float
     */
    public float getCoinVal(String coin){
        return this.udao.getValueCoin(coin);
    }

    /**
     * Function checks if the coin of the user is not euro
     */
    public boolean coinNotEuro(String email){
        String coin = this.udao.getCoin(email);
        if(coin.equals(Transaction.euro)){
            return false;
        }
        else return true;
    }

    public float oddMultiplierCoin(String email){
        if(coinNotEuro(email)){
            String coin = this.udao.getCoin(email);
            float valEuro = this.udao.getValueCoin(coin);
            return valEuro;
        }
        else return 1;
    }

    /**
     * Function that converts the balance of the user with the given email according to the new coin value
     * @param email String
     * @param valCoin float
     */
    public void changeBalanceCoin(String email, float valCoin){
        float balanceCoin = this.udao.getBalanceWallet(email);
        float currentVal = balanceCoin*valCoin;
        this.udao.updateBalanceWallet(email, currentVal);
    }

    /**
     * Function that converts the bonus balance of the user with the given email according to the new coin value
     * @param email String
     * @param valCoin float
     */
    public void changeBalanceBonusCoin(String email, float valCoin) {
        float balanceCoin = this.udao.getBonusBalanceWallet(email);
        float currentVal =balanceCoin*valCoin;
        this.udao.updateBonusBalanceWallet(email, currentVal);
    }

    /**
     * Functions that returns the current coin of the wallet of the user with the given email
     * @param email String
     * @return String
     */
    public String getCurrentCoin(String email){
        String coin = this.udao.getCoin(email);
        return coin;
    }

    /**
     * Function that changes the coin and consequently both the balance and bonus balance of the user with the given email
     * @param email String
     * @param newCoin String
     * @return float
     * @throws Exception
     */
    public float changeCoin(String email, String newCoin) throws Exception{
        String currentCoin = getCurrentCoin(email);
        float coinVal;

        List<String> coinsAvailable = this.udao.getCoinsAvailable();
        if(coinsAvailable.contains(newCoin)){
            this.udao.setCoin(email, newCoin);
        }
        else throw new Exception("Coin "+newCoin+" doesn't exist in the system.");

        float newCoinVal = getCoinVal(newCoin);
        if(currentCoin.equals(Transaction.euro)){
            coinVal = newCoinVal;
            changeBalanceCoin(email, newCoinVal);
            changeBalanceBonusCoin(email, newCoinVal);
        }
        else{
            float currentCoinVal = getCoinVal(currentCoin);
            coinVal = newCoinVal/currentCoinVal;
            changeBalanceCoin(email, coinVal);
            changeBalanceBonusCoin(email, coinVal);
        }
        addTransaction(email,Transaction.coin,0); //Regista nas transações a mudança de moeda.

        return coinVal;
    }

    public void giveFriendCodeBonus(String email, String code, float bonus){
        try {
            if (existsUserCode(code)) {
                updateBonusBalance(email, bonus);
                addTransaction(email, Transaction.bonus, bonus);

                String emailF = this.udao.getOwnerCode(code);
                updateBonusBalance(emailF, bonus);
                addTransaction(emailF, Transaction.bonus, bonus);
            }
        }
        catch (Exception e){}
    }

    public void giveBonus(String email, float bonus) {
        try {
            updateBonusBalance(email, bonus);
            addTransaction(email, Transaction.bonus, bonus);
        }catch (Exception e){}
    }

    /**
     * Function that removes the token that is used by the user with the given email when he wants to edit some profile info
     * @param email String
     */
    public void removeToken(String email){
        this.udao.removeToken(email);
    }

    /**
     * Function that allows to get the transaction type "won bet"
     * @return
     */
    public String getTransactionWon(){
        return Transaction.won;
    }

    /**
     * Function that allows to get the type for transaction "pay bet"
     * @return
     */
    public String getTransactionBet(){
        return Transaction.bet;
    }

    public List<String> get_followed_games(String email){
        List<String> ret = new ArrayList<>();
        int idUser = getIdUser(email);
        if(idUser == -1){
            return ret;
        }
        else ret = this.udao.get_followed_games(idUser);
        return ret;
    }

    public void removeMoneyBalanceAddTransaction(String email, float money) throws Exception{
        reduceMoneyPayBet(email, money);
        addTransaction(email, getTransactionBet(), money);
    }
}
