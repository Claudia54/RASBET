package RASBET.RASBET_DATA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import RASBET.RASBET.Transaction;

public class UserDAO extends DAO {

    public UserDAO() {
        super();
    }

    /**
     * Function that verifies if a given user exists in the database
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
     * Function that adds a new user to the database. It is assumed that the email, cc, nif, password and age was already checked
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
     * Function that determines if the credential inputted are valid ie exist together in the db. Returns true if it exists
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
     * Function that change the name of an user with a given email. It returns true if it was sucessful
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
     * Function that changes the email of an user giving its id.
     * @param id int
     * @param newEmail String
     * @return boolean
     */
    public boolean changeEmail(int id, String newEmail){
        super.startConnStmt();

        //Change email of the Bet of the user id
        String changeBet = "update Bet set email = '"+newEmail+"' where idUser = "+id+";";
        executeSQL(changeBet);
        //Change email of the Wallet of the user id
        String changeWallet = "update Wallet set email = '"+newEmail+"' where idUser = "+id+";";
        executeSQL(changeWallet);
        //Change the email of the user id
        String changeUser = "update User set email = '"+newEmail+"' where id = "+id+";";
        executeSQL(changeUser);

        super.closeConn();
        return true;
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
     * Function that gets the usercode of the own user with the email given. It returns "" if the usercode is not found
     * @param email
     * @return
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
     * @return List<String>
     */
    public List<String> consultWallet(String email) {
        super.startConnStmt();
        List<String> res = new ArrayList<>();
        String sql = "select * from Wallet where email = '" + email + "';";
        ResultSet result = querySQL(sql);
        try{
            result.next();
            res.add(result.getString(2));
            res.add(String.valueOf(result.getFloat(3)));
            res.add(result.getString(4));
            res.add(result.getString(5));

        } catch (SQLException s){
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
     * Function that gets the balance in the wallet of the user with the email given.
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
}