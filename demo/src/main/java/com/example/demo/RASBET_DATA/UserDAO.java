package com.example.demo.RASBET_DATA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.RASBET_LN.User.Transaction;
import com.example.demo.RASBET_LN.User.Wallet;

public class UserDAO extends DAO {

    public UserDAO() {
        super();
    }

    /**
     * Function that verifies if a user with the given email exists in the database
     * Returns true if the email exists
     * @param email String
     * @return boolean
     */
    public boolean verifiesExistence(String email) {
        super.startConnStmt();
        String sql = "select * from User where email = '" + email + "';";
        ResultSet result = super.querySQL(sql);
        boolean b = resultSetIsNotEmpty(result); //returns true if not empty
        super.closeConn();
        return b;
    }

    /**
     * Verifies if a certain nif already exists, as the nif must be unique.
     * Return true if the nif exists.
     * @param nif String
     * @return exists boolean
     */
    public boolean verifiesExistenceNIF(String nif) {
        startConnStmt();

        String sql = "select * from User where nif = '" + nif + "';";
        ResultSet result = super.querySQL(sql);

        boolean b = resultSetIsNotEmpty(result);
        super.closeConn();
        return b;
    }

    /**
     * Function that verifies if a certain cc already exists, as the cc must be unique
     * Returns true if the cc exists
     * @param cc String
     * @return boolean
     */
    public boolean verifiesExistenceCC(String cc) {
        startConnStmt();

        String sql = "select * from User where cc = '" + cc + "';";
        ResultSet result = super.querySQL(sql);

        boolean b = resultSetIsNotEmpty(result);
        super.closeConn();
        return b;
    }

    /**
     * Function that adds a new user to the database. It is assumed that the email, cc, nif, password and age were already checked
     * @param email        String
     * @param name         String
     * @param pwd          String
     * @param address      String
     * @param phone_number phone_number
     * @param nif          String
     * @param cc           String
     * @param birth        String
     * @param role         String
     * @param code         String
     */
    public void addUser(String email, String name, String pwd, String address, String phone_number, String nif,
                        String cc, String birth, String role, String code) {

        super.startConnStmt();

        // Gets the role of the User
        String getIntRole = "select idRole from Role where rolename = '" + role + "';";
        ResultSet intRoleRes = super.querySQL(getIntRole);
        int intRole = resultSetGetFirstInt(intRoleRes, 0);

        // New User is added to the database
        String createUser = "insert into User (email, name, pwd, address, phone_number, nif, cc, birth, role, codeUser) values('" + email + "','" + name + "','" + pwd + "','" + address + "','"
                + phone_number + "','" + nif + "','" + cc + "','" + birth + "'," + String.valueOf(intRole) + ", '" + code + "');";
        executeSQL(createUser); // atenção pode dar null = não funcionar

        // Get the id of the user that was just created
        String getIdUser = "select id from User where email = '" + email + "';";
        ResultSet intId = querySQL(getIdUser);
        int Id = resultSetGetFirstInt(intId, -1);

        // Creates an empty wallet for an User
        String createWallet = "insert into Wallet VALUES (" + Id + ",'" + email + "',0,'EUR',0)";
        executeSQL(createWallet);

        super.closeConn();
    }

    /**
     * Function that adds a new staff member(Specialisr or Admin) to the database. It is assumed that the email, cc, nif, password and age were already checked
     * @param email String
     * @param name String
     * @param pwd String
     * @param address String
     * @param phone_number String
     * @param nif String
     * @param cc String
     * @param birth String
     * @param role String
     */
    public void addStaff(String email, String name, String pwd, String address, String phone_number, String nif,
                         String cc, String birth, String role) {

        super.startConnStmt();

        // Get the id of the role of the user that is going to be created
        String getIntRole = "select idRole from Role where rolename = '" + role + "';";
        ResultSet intRoleRes = querySQL(getIntRole);
        int intRole = resultSetGetFirstInt(intRoleRes, 0);

        //Adds an User to the db
        String createUser = "insert into User (email, name, pwd, address, phone_number, nif, cc, birth, role) values('" + email + "','" + name + "','" + pwd + "','" + address + "','"
                + phone_number + "','" + nif + "','" + cc + "','" + birth + "'," + String.valueOf(intRole) + ");";
        executeSQL(createUser); // atenção pode dar null = não funcionar

        super.closeConn();
    }

    /**
     * Function that determines if the credentials inputted are valid ie exist together in the db. Returns true if it exists
     * @param email String
     * @param pwd   String
     * @return boolean (true if it exists)
     */
    public boolean login(String email, String pwd) {
        super.startConnStmt();
        String sql = "select * from User where email = '" + email + "' and pwd = '" + pwd + "';";
        ResultSet result = querySQL(sql);
        boolean b = resultSetIsNotEmpty(result);
        super.closeConn();
        return b;
    }

    /**
     * Function that returns the name of an user with the given Email. If it doesn't exist, it returns "".
     * @param email String
     * @return String
     */
    public String getName(String email) {
        super.startConnStmt();
        String sql = "select name from User where email = '" + email + "';";
        ResultSet result = querySQL(sql);
        String name = resultSetGetFirstString(result);
        super.closeConn();
        return name;
    }

    /**
     * Function that change the name of an user with a given email. It returns true if it was successful
     * @param email String
     * @param name String
     * @return boolean
     */
    public boolean changeName(String email, String name) {
        super.startConnStmt();
        String sql = "update User set name = '" + name + "' where email = '" + email + "';";
        boolean success = executeSQL(sql);
        super.closeConn();
        return success;
    }

    /**
     * Function that gets the role of a given email. It returns "" if the email is not in the db
     * @param email String
     * @return String
     */
    public String getRole(String email) {
        super.startConnStmt();
        String sql = "select r.rolename from Role r inner join User u on u.role = r.idRole where u.email = '" + email
                + "';";
        ResultSet result = querySQL(sql);
        String role = resultSetGetFirstString(result);
        super.closeConn();
        return role;
    }

    /**
     * Function that gets the id of the user with the email given. It returns -1 if the user can't be found
     * @param email String
     * @return int
     */
    public int getIdUser(String email){
        super.startConnStmt();
        String getIdUser = "select id from User where email = '"+email+"';";
        ResultSet intId = querySQL(getIdUser);
        int id = resultSetGetFirstInt(intId, -1); //Default é user
        super.closeConn();
        return id;
    }

    /**
     * Function that changes the password of an user given an email. It returns true if it was successful
     * @param email String
     * @param pwd String
     * @return boolean
     */
    public boolean changePwd(String email, String pwd) {
        super.startConnStmt();
        String sql = "update User set pwd = '" + pwd + "' where email = '" + email + "';";
        boolean success = executeSQL(sql);
        super.closeConn();
        return success;
    }

    /**
     * Function that changes the email of an user given its id.
     * @param id int
     * @param newEmail String
     * @return boolean
     */
    public boolean changeEmail(int id, String newEmail){
        super.startConnStmt();

        //Change email of the Bet of the user id
        String changeBet = "update Bet set email = '"+newEmail+"' where idUser = "+id+";";
        boolean s1 = executeSQL(changeBet);
        //Change email of the Wallet of the user id
        String changeWallet = "update Wallet set email = '"+newEmail+"' where idUser = "+id+";";
        boolean s2 = executeSQL(changeWallet);
        //Change the email of the user id
        String changeUser = "update User set email = '"+newEmail+"' where id = "+id+";";
        boolean s3 =executeSQL(changeUser);

        super.closeConn();
        return s1&s2&s3;
    }

    /**
     * Function that changes the address of the user with the email given
     * If returns true, if it is successful
     * @param email String
     * @param newAddress String
     * @return boolean
     */
    public boolean changeAddress(String email, String newAddress){
        super.startConnStmt();
        String sql = "update User set address = '" + newAddress + "' where email = '" + email + "';";
        boolean success = executeSQL(sql);
        super.closeConn();
        return success;
    }

    /**
     * Function that changes the phone number of an user with a given email
     * It returns true if it is successful
     * @param email String
     * @param newPhoneNumber String
     * @return boolean
     */
    public boolean changePhoneNumber(String email, String newPhoneNumber){
        super.startConnStmt();
        String sql = "update User set phone_number = '" + newPhoneNumber + "' where email = '" + email + "';";
        boolean success = executeSQL(sql);
        super.closeConn();
        return success;
    }

    /**
     * Function that verifies if an user code exists in the database. Returns true if it exists.
     * @param code String
     * @return boolean
     */
    public boolean existsUserCode(String code){
        super.startConnStmt();
        String sql = "select * from User where codeUser = '"+code+"';";
        ResultSet result = querySQL(sql);
        boolean b = resultSetIsNotEmpty(result);
        super.closeConn();
        return b;
    }

    /**
     * Function that gets the usercode of the user with the email given. It returns "" if the usercode is not found
     * @param email String
     * @return String
     */
    public String getUserCode(String email){
        super.startConnStmt();
        String sql = "select codeUser from User where email = '"+email+"';";
        ResultSet result = querySQL(sql);
        String s = resultSetGetFirstString(result);
        super.closeConn();
        return s;
    }

    /*
     * -----------------------------------------------------------
     *                      Wallet Functions:
     * -----------------------------------------------------------
     */

    /**
     * Function that gets the information of the wallet of the user with the given email
     * @param email String
     * @return Wallet
     */
    public Wallet consultWallet(String email) {
        super.startConnStmt();
        Wallet res = null;
        String sql = "select * from Wallet where email = '" + email + "';";
        ResultSet result = querySQL(sql);
        try{
            if(result.next()) {
                res = new Wallet();
                res.setBalance(result.getFloat(3));
                res.setCurrency(result.getString(4));
                res.setBonusBalance(result.getFloat(5));
            }
        } catch (SQLException s){
            res = null;
            System.out.println("Error consulting the ResultSet.");
        }
        super.closeConn();
        return res;
    }

    /**
     * The function updateBalanceWallet changes the balance of the wallet of the user email
     * @param email String
     * @param money float
     */
    public void updateBalanceWallet(String email, float money) {
        super.startConnStmt();
        String changeBalance = "update Wallet set balance = " + money + " where email = '" + email + "';";
        executeSQL(changeBalance);
        super.closeConn();
    }

    /**
     * Function that gets the balance of the wallet of the user with the given email.
     * It returns 0 if the wallet is not found.
     * @param email String
     * @return float
     */
    public float getBalanceWallet(String email){
        super.startConnStmt();
        String getBalance = "select balance from Wallet where email = '" + email + "';";
        ResultSet result = querySQL(getBalance);
        float currentBalance = super.resultSetGetFirstFloat(result, 0);
        super.closeConn();
        return currentBalance;
    }

    /**
     * The function updateBonusBalanceWallet changes the bonus balance of the wallet of the user email
     * @param email String
     * @param money float
     */
    public void updateBonusBalanceWallet(String email, float money) {
        super.startConnStmt();
        String changeBalance = "update Wallet set bonusBalance = " + money + " where email = '" + email + "';";
        executeSQL(changeBalance);
        super.closeConn();
    }

    /**
     * Function that gets the bonus balance in the wallet of the user with the email given.
     * It returns 0 if the wallet is not found.
     * @param email String
     * @return float
     */
    public float getBonusBalanceWallet(String email){
        super.startConnStmt();
        String getBalance = "select bonusBalance from Wallet where email = '" + email + "';";
        ResultSet result = querySQL(getBalance);
        float currentBalance = super.resultSetGetFirstFloat(result, 0);
        super.closeConn();
        return currentBalance;
    }

    /**
     * Function that adds to the DB the transaction of a certain type and a certain amount maid by the user idWallet.
     * In the db the idWallet is equal to the idUser.
     * @param idWallet int
     * @param type String
     * @param amount float
     */
    public void addTransaction(int idWallet, String type, float amount){
        super.startConnStmt();
        String sql = "insert into Transaction (idWallet, type, amount) values ("+idWallet+", '"+type+"', "+amount+");";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that gets all the transactions made by the user idWallet. In the db, the idWallet is equal to the userId.
     * It the user can't be found, the output will be a empty arraylist.
     * @param idWallet int
     * @return List<Transaction>
     */
    public List<Transaction> getTransactions(int idWallet){
        super.startConnStmt();
        List<Transaction> arr = new ArrayList<>();
        String sql = "select * from Transaction where idWallet = "+idWallet+";";
        ResultSet resultSet = querySQL(sql);

        try {
            while (resultSet.next()) {
                Transaction t = new Transaction(resultSet.getString(3), resultSet.getFloat(4));
                arr.add(t);
            }
        }catch (Exception e){
            System.out.println("Impossible to access the resultSet.");
        }
        super.closeConn();
        return arr;
    }

    /*------------------------------------------------*/

    /**
     * Function that returns the value of the coin in relation with EUR
     * @param coin String
     * @return float
     */
    public float getValueCoin(String coin){
        super.startConnStmt();
        String sql = "select valEuro from Currency where currencyName = '"+coin+"';";
        ResultSet res = super.querySQL(sql);
        float val = super.resultSetGetFirstFloat(res, 1);
        super.closeConn();
        return val;
    }

    public String getCoin(String email){
        super.startConnStmt();
        String sql = "select currency from Wallet where email = '"+email+"';";
        ResultSet res = super.querySQL(sql);
        String coin = super.resultSetGetFirstString(res);
        super.closeConn();
        return coin;
    }

    public void setCoin(String email, String coin){
        super.startConnStmt();
        String sql = "update Wallet set currency = '"+coin+"' where email = '"+email+"'";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that returns the list with the names of the coins available
     * @return List<String>
     */
    public List<String> getCoinsAvailable(){
        super.startConnStmt();
        String sql = "select currencyName from Currency;";
        ResultSet res = super.querySQL(sql);

        List<String> output = new ArrayList<>();
        try{
            while (res.next()){
                output.add(res.getString(1));
            }
        }catch (Exception e){}
        super.closeConn();
        return output;
    }

    public String getOwnerCode(String code){
        String res = "";
        super.startConnStmt();
        String sql = "select email from User where codeUser = '"+code+"';";
        ResultSet r = querySQL(sql);
        res = super.resultSetGetFirstString(r);
        super.closeConn();
        return res;
    }

    /**
     * Function that adds the token generated so that an user can then edit some user info that asks for it
     * @param email String
     * @param token String
     */
    public void addToken(String email, String token){
        super.startConnStmt();
        String sql = "update User set token = '"+token+"' where email = '"+email+"';";
        executeSQL(sql);
        super.closeConn();
    }
    /**
     * Function that removes the token that is used by the user with the given email when he wants to edit some profile info
     * @param email String
     */
    public void removeToken(String email){
        super.startConnStmt();
        String sql = "update User set token = null where email = '"+email+"';";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that verifies that the token provided by the user with a given email is the same as the one registered in the system.
     * If the function returns true, the token provided is the same as the one registered in the system.
     * @param email String
     * @param token String
     * @return boolean
     */
    public boolean verifyToken(String email, String token){
        super.startConnStmt();
        String sql = "select token from User where email = '"+email+"';";
        ResultSet res = super.querySQL(sql);
        boolean b = false;
        try{
            String tok = super.resultSetGetFirstString(res);
            if (tok.equals(token)) b = true;
        }catch (Exception e){}
        super.closeConn();
        return b;
    }

    /**
     * Function that verifies the existence of the token needed to edit some user info.
     * Retruns true if there is no token registered for the give email yet
     * @param email String
     * @return boolean
     */
    public boolean verifyTokenExistence(String email){
        super.startConnStmt();
        String sql = "select token from User where email = '"+email+"';";
        ResultSet res = super.querySQL(sql);
        boolean b = super.resultSetIsNotEmpty(res);
        super.closeConn();
        return b;

    }


    public List<String> get_followed_games(int idUser){
        super.startConnStmt();
        String sql = "select idGame from Game_Followers where idUser = "+idUser+";";
        ResultSet res = querySQL(sql);

        List<String> ret = new ArrayList<>();
        try{
            while(res.next()){
                ret.add(res.getString(1));
            }
        }
        catch (Exception e){}
        super.closeConn();
        return ret;
    }
}