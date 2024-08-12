package com.example.demo.RASBET_LN;

import java.util.List;
import java.util.Map;

public interface IRASBET_Facade {

    /*------System------*/
    List<Map<String,String>> getPromotions();
    void activatePromoBonus();
    void activatePromoMultiplier();
    void deactivatePromoBonus();
    void deactivatePromoMultiplier();
    List<String> generateNotification(String email);


    /*------User------*/

    /**
     * Function used to log in a user.
     * It checks if the user with email exists, and if its password is pwd.
     * Returns true if the data given is correct.
     * @param email String
     * @param pwd String
     * @return boolean
     */
    boolean login(String email, String pwd);

    /**
     * Function that creates the new User with the data given and an empty wallet.
     * It verifies if the email, CC and NIF already exists, and returns an exception if so.
     * It makes sure the user is older than 18, it returns an exception if this is not true.
     * A user-code is also generated, and if a valid friendCode is used, a bonus is added.
     * It returns false if is not successful
     * @param email String
     * @param name String
     * @param pwd String
     * @param address String
     * @param phone_number String
     * @param nif String
     * @param cc String
     * @param birth String
     * @param friendCode String
     * @return boolean
     * @throws Exception
     */
    boolean registerUser(String email, String name, String pwd, String address, String phone_number, String nif,
                                String cc, String birth, String friendCode) throws Exception;

    /**
     * Function that allows to create an Admin with the given data. No wallet is created.
     * It makes sure the email, cc and nif doesn't exist yet, and the age given is older than 18; otherwise, Exceptions are thrown.
     * It returns true if the user was correctly created
     * @param email String
     * @param name String
     * @param pwd String
     * @param address String
     * @param phone_number String
     * @param nif String
     * @param cc String
     * @param birth String
     * @return boolean
     * @throws Exception
     */
    boolean registerAdmin(String email, String name, String pwd, String address, String phone_number, String nif,
                                 String cc, String birth) throws Exception;

    /**
     * Function that allows to register a specialist with the given data. No wallet is created.
     * It makes sure the email, cc and nif doesn't exist yet, and the age given is older than 18; otherwise, Exceptions are thrown.
     * It returns true if the user was correctly created
     * @param email String
     * @param name String
     * @param pwd String
     * @param address String
     * @param phone_number String
     * @param nif String
     * @param cc String
     * @param birth String
     * @return boolean
     * @throws Exception
     */
    boolean registerSpecialist(String email, String name, String pwd, String address, String phone_number, String nif,
                                      String cc, String birth) throws Exception;

    /**
     * Function that gets the role of the User
     * It the user doesn't exist, it returns an Exception
     * @param email String
     * @throws Exception
     * @return String
     */
    String getRole(String email) throws Exception;

    /**
     * Function that gets the name of the User
     * It the user doesn't exist, it throws an exception
     * @param email String
     * @throws Exception
     * @return String
     */
    String getName(String email) throws Exception;

    /**
     * Function that allows a certain user with email to change their name to newName
     * It throws an Exception if it is not possible to find an user with the given email
     * @param email String
     * @param newName String
     * @throws Exception
     * @return boolean
     */
    boolean changeName(String email, String newName) throws Exception;

    /**
     * Function that allows a user with email to change their password to newPwd
     * If the password is not valid, or email not found it returns exception
     * @param email String
     * @param newPwd String
     * @throws Exception
     * @return boolean
     */
    boolean changePwd(String email, String newPwd) throws Exception;

    /**
     * Function that allows a certain user to change their email to newEmail
     * If the new email already exists, or if the user can't be found, it throws an exception
     * @param email String
     * @param newEmail String
     * @throws Exception
     */
    boolean changeEmail(String email, String newEmail) throws Exception;

    /**
     * Function that creates and sends a token of identification to a certain user.
     * This function should be called before the update address and the update phone number
     * @param email String
     * @return String
     * @throws Exception
     */
    String getTokenIdentification(String email) throws Exception;

    /**
     * Function that removes the token that is used by the user with the given email when he wants to edit some profile info
     * @param email String
     */
    void removeToken(String email);

    /**
     * Function that allows a certain user to change their address. To be allowed to perform this action, the user may use a generated token.
     * @param email String
     * @param newAddress String
     * @param token String
     * @throws Exception
     * @return boolean
     */
    boolean changeAddress(String email, String newAddress, String token) throws Exception;

    /**
     * Function that allows a user to change their phoneNumber
     * It throws an exception if the user is not found, or if the phone number doesn't have a valid format
     * @param email String
     * @param newPhoneNumber String
     * @param token String
     * @throws Exception
     */
    boolean changePhoneNumber(String email, String newPhoneNumber, String token) throws Exception;

    /**
     * Function that gets the balance of the wallet of the user with the given email.
     * It returns 0 if the wallet is not found.
     * @param email String
     * @return float
     */
    float getBalance(String email);

    /**
     * Function that allows a user to see their wallet. It includes the balance and bonus balance.
     * This function returns Exception if the wallet of this user can't be found.
     * @param email String
     * @return String
     * @throws Exception
     */
    Map<String,String> consult_Wallet(String email) throws Exception;

    /**
     * Function that allows a user with email to add money to their account.
     * This function should only be called after the transfer method has been approved.
     * It throws an exception if the user is not found, or if the value is negative or null
     * @param email String
     * @param value float
     * @throws Exception
     */
    void deposit_money(String email, float value) throws Exception;

    /**
     * Function that allows a user to remove money from their account
     * After this method, a transfer could be made.
     * If the user doesn't exist, value is negative or the user doesn't have enough money on the account,
     * an exception is thrown.
     * @param email String
     * @param value float
     * @throws Exception
     */
    void withdraw_money(String email, float value) throws Exception;

    /**
     * Function that allows a user to see their transactions
     * It returns an empty list if the user doesn't exist or if the user didn't make any transaction yet
     * @param email String
     * @return List<Map<String,String>>
     */
    List<Map<String,String>> getTransactions(String email);


    /**
     * Function that changes the coin and consequently both the balance and bonus balance of the user with the given email
     * Also, given an email and the value of the new coin, this function converts the value of the possible earnings and of the odds according to the value of the new coin
     * @param email String
     * @param newCoin String
     */
    void changeCoin(String email, String newCoin);

    /**
     * Function that gets the usercode of the user with the given email. It returns "" if the usercode is not found
     * @param email String
     * @return String
     */
    String getFriendBonus(String email) throws Exception;



    /*------Sports------*/

    /**
     * Function that gets the Games of a certain Sport.
     * If there are no Games for a certain Sport, the list returned will be empty
     * Each List<Map<String,String>> contains the data of a Game.
     * There should exist a Map that as the general info of a Game, and another
     * with the info of the participants.
     * The Map with general info, is the first map, and has entries for idGame,
     * idSport, date, outcome, X. The next Map has the odd for each participant.
     * @return A list of games unstructured, can be build into GameDTO
     * @param nameSport String
     * @return List<List<Map<String,String>>>
     */
    List<List<Map<String,String>>> get_Games_of_Sport(String nameSport);

    /**
     * Function that allows to get all the open games that exist in the system
     * If there is no open game the list will be empty.
     * Each List<Map<String,String>> contains the data of a Game.
     * There should exist a Map that as the general info of a Game, and another
     * with the info of the participants.
     * The Map with general info, is the first map, and has entries for idGame,
     * idSport, date, outcome, X. The next Map has the odd for each participant.
     * @return A list of games unstructured, can be build into GameDTO
     */
    List<List<Map<String,String>>> get_All_Games();

    /**
     * Function that allows to change an odd of a certain participant in a certain game
     * If the game or the participant is not found, an exception is thrown
     * If the game is closed, an exception is thrown
     * If the odd is not positive, an exception is thrown
     * @param idGame String
     * @param participant int
     * @param newOdd float
     * @param specialist boolean
     * @throws Exception
     */
    void changeOdd(String idGame, int participant, float newOdd, boolean specialist) throws Exception;

    /**
     * Function that allows to change the odd of the draw of a game
     * It returns an exception if the game is not found, the game doesn't accept draws, the newOdd is not valid
     * If the game is closed, an exception is thrown
     * @param idGame String
     * @param newOdd float
     * @param specialist boolean
     * @throws Exception
     */
    void changeOddDrawGame(String idGame, float newOdd, boolean specialist) throws Exception;

    /**
     * Function that allows an admin to close a Game.
     * It throws an exception if the game doesn't exist or is closed.
     * Close is a final state of a game, it can't be changed after, therefore, the result should exist and be valid.
     * @param idGame String
     * @throws Exception
     */
    void closeGame(String idGame) throws Exception;

    /**
     * Function that allows to open a Game
     * It throws an exception if the game doesn't exist, if the game is closed, or if it is already open.
     * If a game is closed, it can't be open again
     * @param idGame String
     * @throws Exception
     */
    void openGame(String idGame) throws Exception;

    /**
     * Function that allows to suspend a Game
     * It throws an exception if the game doesn't exist, the game is closed, or the game is already suspended.
     * @param idGame String
     * @throws Exception
     */
    void suspendGame(String idGame) throws Exception;

    /**
     * Function that allows to add a result to a Game.
     * This result can or not be the final result.
     * It returns an exception if
     * @param idGame String
     * @param result String
     * @return boolean
     * @throws Exception
     */
    boolean addResult(String idGame, String result) throws Exception;

    /**
     * Function that allows to end the game given a certain result
     * It returns an exception if
     * @param idGame String
     * @param result String
     * @throws Exception
     */
    void endGame(String idGame, String result) throws Exception;

    // ----------
    boolean startFollowingGame(String email, String game);

    boolean stopFollowingGame(String email, String idGame);

    List<String> get_followed_games(String email);

    /*------Bet------*/

    /**
     * Function that allows to consult the current Bet not set.
     *
     * @param email String
     * @return List<List<String>>
     */
    List<List<String>> showBet(String email);

    /**
     * Function that allows to consult all the bets previously made.
     * @param email String
     * @return List<List<List<String>>>
     */
    List<List<List<String>>> consult_Bets_History(String email);

    /**
     * Function that allows to add a guess to a bet.
     * It throws an exception if ...
     * @param email String
     * @param idGame String
     * @param guess String
     * @throws Exception
     */
    void add_to_bet(String email, String idGame, String guess) throws Exception;

    /**
     * Function that allows to add the money betted and pay the bet with money from Wallet.
     * It throwns an exception if ...
     * @param email
     * @param money
     * @throws Exception
     */
    void setBetWallet(String email, float money) throws Exception;

    /**
     * Function that allows to pay a bet without using the money from the wallet
     * @param email String
     * @param money float
     * @throws Exception
     */
    void setBetPayMethod(String email, float money) throws Exception;

    /**
     * Function that allows to remove a guess from a Bet
     * @param email String
     * @param idGame String
     */
    void removeFromBet(String email, String idGame);
}
