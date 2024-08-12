package com.example.demo.RASBET_LN;

import com.example.demo.RASBET_DATA.SystemDAO;
import com.example.demo.RASBET_LN.Bet.BetFacade;
import com.example.demo.RASBET_LN.Sport.SportFacade;
import com.example.demo.RASBET_LN.User.UserFacade;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
/**
 * Class RASBET_Facade
 */
public class RASBET_Facade implements IRASBET_Facade {
    private UserFacade userfacade;
    private SportFacade sportfacade;
    private BetFacade betfacade;
    private SystemDAO systemdao; //functions access database related to system vars and promotions
    private Timer timer;

    private Map<String, Promotion> promotions;
    private String friendBonus = "friendBonus";
    private String bonus = "bonus";
    private String multiplier = "multi";

    /**
     * Constructor Rasbet_facade
     */
    public RASBET_Facade() {
        this.userfacade = new UserFacade();
        this.sportfacade = new SportFacade();
        this.betfacade = new BetFacade();
        this.systemdao = new SystemDAO();
        this.timer = new Timer(true);
        this.timer.scheduleAtFixedRate(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Request API.");
                        loadGamesFootball();
                    }
                },
                0,
                100*1000
        );
        this.promotions = new HashMap<>();
        for (Promotion p:  this.systemdao.getPromotions()){
            this.promotions.put(p.getType(), p);
        }
    }


    /*----------------------------------------------
                  System Function
    ----------------------------------------------*/

    /**
     * Function that checks if the promotion of a type is active
     * It returns true if so.
     * @param type String
     * @return boolean
     */
    public boolean check_promotion_active(String type){
        if(this.promotions.get(type).getState()){
            return true;
        }
        else return false;
    }

    /**
     * Function that allows the system to identify the amount of a certain promotion.
     * @param type String
     * @return float
     */
    public float get_amount_bonus(String type){
        return this.promotions.get(type).getAmount();
    }


    /**
     * Function that gets a list with all the promotions in the program
     * @return List<String>
     */
    public List<Map<String,String>> getPromotions(){
        List<Map<String,String>> allPromotions = new ArrayList<>();
        for(Promotion promo : this.promotions.values()){
            allPromotions.add(promo.returnMap());
        }
        return allPromotions;
    }

    /**
     * Function that changes the state of the promotion bonus to active.
     */
    public void activatePromoBonus(){
        this.promotions.get(bonus).setState(true);
        this.systemdao.changeStatePromo(bonus, 1);
    }

    /**
     * Function that changes the state of the promotion multiplier to active.
     */
    public void activatePromoMultiplier(){
        this.promotions.get(multiplier).setState(true);
        this.systemdao.changeStatePromo(multiplier, 1);
    }

    /**
     * Allows to change the state of the promotion bonus to deactivated
     */
    public void deactivatePromoBonus(){
        this.promotions.get(bonus).setState(false);
        this.systemdao.changeStatePromo(bonus, 0);
    }

    /**
     * Allows to change the state of the promotion multiplier to deactivated
     */
    public void deactivatePromoMultiplier(){
        this.promotions.get(multiplier).setState(false);
        this.systemdao.changeStatePromo(multiplier, 0);
    }

    /**
     * Function that generates all the notification that a recently logged-in user should receive.
     * This function should only be called after login.
     * It returns a list with the notifications, and if there are none, it should return an empty list
     * @param email String
     * @return List<String>
     */
    public List<String> generateNotification(String email){
        List<String> notification = new ArrayList<>();

        if(check_promotion_active(multiplier)){
            float minVal = this.promotions.get(multiplier).getMinVal();
            float value = this.promotions.get(multiplier).getAmount();
            String not1 = "De momento, todas as odds acima de "+minVal+" estão a ser multiplicadas por "+value+". Aproveita! ;)";//"Currently all odds above "+minVal+" are multiplied by "+value+". Enjoy! :)";
            notification.add(not1);
        }
        if(check_promotion_active(bonus)){
            float value = this.promotions.get(bonus).getAmount();
            String not2 = "PROMOÇÃO: Qualquer novo utilizador que se registar receberá um bónus de "+value+".\n Partilha o teu código com os teus amigos e saem todos a ganhar! :D";
            notification.add(not2);
        }
        String not3 = this.systemdao.checkNotification(email);
        if(!not3.equals("")){
            notification.add(not3);
        }
        return notification;
    }

    public void addNotificationBet(String email){
        Email.generateEmailBetWon(email);
    }


    /*----------------------------------------------
                       User Facade
     ----------------------------------------------*/

    /**
     * Function used to log in a user.
     * It checks if the user with email exists, and if its password is pwd.
     * Returns true if the data given is correct.
     * @param email String
     * @param pwd String
     * @return boolean
     */
    public boolean login(String email, String pwd) {
        boolean b = this.userfacade.login(email, pwd);
        return b;
    }

    /**
     * Function that checks if the Promotion bonus and friendBonus exist, and if they are active.
     * If so, an extra bonus balance is given to the user that just register.
     * This function should be called after register, as this promotion adds a bonus balance after signin.
     * @param email String
     * @param friendCode String
     */
    public void rewardpromotions(String email, String friendCode){
        if(this.promotions.get(friendBonus)!= null) {
            if(check_promotion_active(friendBonus)) {
                float bonus = get_amount_bonus(friendBonus);
                this.userfacade.giveFriendCodeBonus(email, friendCode, bonus);
            }
        }
        if(this.promotions.get(bonus)!= null){
            if(check_promotion_active(bonus)){
                float bonusAmount = get_amount_bonus(bonus);
                this.userfacade.giveBonus(email, bonusAmount);
            }
        }
    }

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
    public boolean registerUser(String email, String name, String pwd, String address, String phone_number, String nif,
                        String cc, String birth, String friendCode) throws Exception{
        boolean success;
        try {
            success = this.userfacade.createUser(email, name, pwd, address, phone_number, nif, cc, birth, UserFacade.user);
        }
        catch (Exception e){
            throw e;
        }
        if(success) {
            rewardpromotions(email, friendCode);
        }
        return success;
    }

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
    public boolean registerAdmin(String email, String name, String pwd, String address, String phone_number, String nif,
                                String cc, String birth) throws Exception{
        boolean success;
        try {
            success = this.userfacade.createStaff(email, name, pwd, address, phone_number, nif, cc, birth, UserFacade.admin);
        }
        catch (Exception e){
            throw e;
        }
        return success;
    }

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
    public boolean registerSpecialist(String email, String name, String pwd, String address, String phone_number, String nif,
                                String cc, String birth) throws Exception{
        boolean success;
        try {
            success = this.userfacade.createStaff(email, name, pwd, address, phone_number, nif, cc, birth, UserFacade.specialist);
        }
        catch (Exception e){
            throw e;
        }
        return success;
    }

    /**
     * Function that gets the role of the User
     * If the user doesn't exist, it returns an Exception
     * @param email String
     * @throws Exception
     * @return String
     */
    public String getRole(String email) throws Exception{
        String res;
        try {
            res = this.userfacade.getRole(email);
        }catch (Exception e){
            throw e;
        }
        return res;
    }

    /**
     * Function that gets the name of the User
     * It the user doesn't exist, it throws an exception
     * @param email String
     * @throws Exception
     * @return String
     */
    public String getName(String email) throws Exception{
        String res;
        try {
            res = this.userfacade.getName(email);
        }catch (Exception e){
            throw e;
        }
        return res;
    }

    /**
     * Function that gets the usercode of the user with the given email. It returns "" if the usercode is not found
     * @param email String
     * @return String
     */
    public String getFriendBonus(String email) throws Exception{
        String res = "";
        try{
            res = this.userfacade.getCodAmigo(email);
        }
        catch (Exception e){
            throw e;
        }
        return res;
    }

    /**
     * Function that allows a certain user with email to change their name to newName
     * It throws an Exception if it is not possible to find an user with the given email
     * @param email String
     * @param newName String
     * @throws Exception
     * @return boolean
     */
    public boolean changeName(String email, String newName) throws Exception{
        boolean s;
        try{
            s = this.userfacade.changeName(email,newName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return s;
    }

    /**
     * Function that allows a user with email to change their password to newPwd
     * If the password is not valid, or email not found it returns exception
     * @param email String
     * @param newPwd String
     * @throws Exception
     * @return boolean
     */
    public boolean changePwd(String email, String newPwd) throws Exception {
        boolean s;
        try{
            s = this.userfacade.changePwd(email, newPwd);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return s;
    }

    /**
     * Function that allows a certain user to change their email to newEmail
     * If the new email already exists, or if the user can't be found, it throws an exception
     * @param email String
     * @param newEmail String
     * @throws Exception
     * @return boolean
     */
    public boolean changeEmail(String email, String newEmail) throws Exception {
        boolean s;
        try{
            s = this.userfacade.changeEmail(email, newEmail);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return s;
    }

    /**
     * Function that creates and sends a token of identification to a certain user.
     * This function should be called before the update address and the update phone number
     * @param email String
     * @return String
     * @throws Exception
     */
    public String getTokenIdentification(String email) throws Exception{
        String res;
        try{
            res = this.userfacade.getTokenIdentification(email);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return res;
    }


    /**
     * Function that allows a certain user to change their address. To be allowed to perform this action, the user may use a generated token.
     * @param email String
     * @param newAddress String
     * @param token String
     * @throws Exception
     * @return boolean
     */
    public boolean changeAddress(String email, String newAddress, String token) throws Exception {
        boolean b;
        try {
            b = this.userfacade.changeAddress(email, newAddress, token);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return b;
    }

    /**
     * Function that allows a user to change their phoneNumber. To be allowed to perform this action, the user may use a generated token.
     * It throws an exception if the user is not found, or if the phone number doesn't have a valid format
     * @param email String
     * @param newPhoneNumber String
     * @throws Exception
     */
    public boolean changePhoneNumber(String email, String newPhoneNumber, String token) throws Exception{
        boolean b;
        try{
            b = this.userfacade.changePhoneNumber(email, newPhoneNumber, token);
        }
        catch (Exception e){
            throw e;
        }
        return b;
    }

    /**
     * Function that allows a user to see their wallet. It includes the balance and bonus balance.
     * This function returns Exception if the wallet of this user can't be found.
     * @param email String
     * @return String
     * @throws Exception
     */
    public Map<String,String> consult_Wallet(String email) throws Exception {
        Map<String,String> w;
        try {
            w = this.userfacade.getWallet(email);
        }
        catch (Exception e){
            throw e;
        }
        return w;
    }

    /**
     * Function that allows a user with email to add money to their account.
     * This function should only be called after the transfer method has been approved.
     * It throws an exception if the user is not found, or if the value is negative or null
     * @param email String
     * @param value float
     * @throws Exception
     */
    public void deposit_money(String email, float value) throws Exception{
        try {
            this.userfacade.deposit_money(email, value);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * Function that allows a user to remove money from their account
     * After this method, a transfer could be made.
     * If the user doesn't exist, value is negative or the user doesn't have enough money on the account,
     * an exception is thrown.
     * @param email String
     * @param value float
     * @throws Exception
     */
    public void withdraw_money(String email, float value) throws Exception{
        try{
            this.userfacade.withdraw_money(email, value);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * Function that allows a user to see their transactions
     * It returns an empty list if the user doesn't exist or if the array of transactions is empty
     * @param email String
     * @return List<Map<String,String>>
     */
    public List<Map<String,String>> getTransactions(String email){
        return this.userfacade.getTransactions(email);
    }

    /**
     * Function that allows to determine if the user has more money in the wallet than the amount they have to pay.
     * It throws an exception if value is negative, or user not found.
     * It returns true if it has enough money, false if value is not enough
     * @param email String
     * @param valueToPay float
     * @return boolean
     * @throws Exception
     */
    public boolean hasEnoughMoney(String email, float valueToPay) throws Exception{
        boolean success = this.userfacade.hasEnoughMoney(email, valueToPay);
        return success;
    }

    /**
     * Function that removes the token that is used by the user with the given email when he wants to edit some profile info
     * @param email String
     */
    public void removeToken(String email){
        this.userfacade.removeToken(email);
    }

    /**
     * Function that changes the coin and consequently both the balance and bonus balance of the user with the given email
     * Also, given an email and the value of the new coin, this function converts the value of the possible earnings and of the odds according to the value of the new coin
     * @param email String
     * @param newCoin String
     */
    public void changeCoin(String email, String newCoin){
        try {
            float val = this.userfacade.changeCoin(email, newCoin);   //Change coin in the Wallet
            this.betfacade.changeCoinBetsNotConcluded(email, val);    //Mudar possible earnings bets
        }catch (Exception e){}
    }

    /**
     * Function that gets the balance of the wallet of the user with the given email.
     * It returns 0 if the wallet is not found.
     * @param email String
     * @return float
     */
    public float getBalance(String email){
        return this.userfacade.getBalance(email);
    }


    /*
     * -----------------------------------------
     *          Game Functionalities:
     * -----------------------------------------
     */

    /**
     * Function that gets all the open games of a certain sport (nameSport)
     * If the nameSport doesn't exist or there are no open games, the list returned will be empty
     * @param nameSport String
     * @return  List<List<Map<String,String>>>
     */
    public List<List<Map<String,String>>> get_Games_of_Sport(String nameSport){
        return this.sportfacade.get_games_sport(nameSport);
    }

    /**
     * Function that gets all the open games
     * If there aren't any open games, the list returned will be empty
     * Each List<Map<String,String>> contains the data of a Game.
     * There should exist a Map that as the general info of a Game, and another
     * with the info of the participants.
     * The Map with general info, is the first map, and has entries for idGame,
     * idSport, date, outcome, X. The next Map has the odd for each participant.
     * @return List<List<Map<String,String>>>
     */
    public List<List<Map<String,String>>> get_All_Games(){
        return this.sportfacade.get_all_games();
    }

    /**
     * Function that allows to change the odd of a home team.
     * The param specialist indicates if the odd was change by a specialist or by the API.
     * If the game/participant doesn't exist, the odd is not superior to 1 or the game is not open, it returns an exception.
     * @param idGame String
     * @param newOdd float
     * @param specialist boolean
     * @throws Exception
     */
    public void changeOdd(String idGame,int participant, float newOdd, boolean specialist) throws Exception{
        try {
            this.sportfacade.changeOdd(idGame, Integer.toString(participant), newOdd, specialist);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Function that allows to change the odd of the draw.
     * The param specialist indicates if the odd was change by a specialist or by the API.
     * If the game doesn't exist, the odd is not superior to 1, the game is not open or the game doesn't allow draw, it returns an exception.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddDrawGame(String idGame, float newOdd, boolean specialist) throws Exception{
        try {
            this.sportfacade.changeOddDrawGame(idGame, newOdd, specialist);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }


    /**
     * Function that allows an admin to close a game.
     * It sends an exception if the game doesn't exist or is closed.
     * An exception is also thrown if the game doesn't have a result yet, it is not possible to close the game
     * @param idGame String
     * @throws Exception
     */
    public void closeGame(String idGame) throws Exception{
        try{
            this.sportfacade.closeGame(idGame);
            resolve_bets(idGame);
        }
        catch (Exception e) {
            throw e;
        }
    }

    /**
     * Function that allows an admin to open a game.
     * It throws an exception if the game is closed, already open or the game is not found.
     * @param idGame String
     * @throws Exception
     */
    public void openGame(String idGame) throws Exception{
        try{
            this.sportfacade.openGame(idGame);
        }
        catch (Exception e) {
            throw e;
        }
    }

    /**
     * Function that allows an admin to suspend a game.
     * It returns an exception if the game doesn't exist, it is closed or suspended.
     * @param idGame String
     * @throws Exception
     */
    public void suspendGame(String idGame) throws Exception{
        try{
            this.sportfacade.suspendGame(idGame);
        }
        catch (Exception e) {
            throw e;
        }
    }

    /**
     * Function that allows to add an intermediate result to a game.
     * It is not necessary to check if it allows draw
     * It returns an exception if the game doesn't exist
     * @param idGame String
     * @param result String
     * @return boolean
     */
    public boolean addResult(String idGame, String result) throws Exception{
        boolean b = false;
        try{
            b = this.sportfacade.addResultGame(idGame, result);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
        return b;
    }

    /**
     * Function that allows to add a game by adding a result.
     * It should check if the result is valid
     * @param idGame String
     * @param result String
     * @throws Exception
     */
    public void endGame(String idGame, String result) throws Exception{
        try{
            this.sportfacade.endGame(idGame, result);
            resolve_bets(idGame);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void newGame(String nameSport, String date, List<String> part){
        this.sportfacade.newGame(nameSport, date, part);
    }

    /*
     * -----------------------------------------
     *          Bet Functionalities:
     * -----------------------------------------
     */

    /**
     * Function that allows to add to a Bet the name of the participant guessed
     * and the name of the game
     * @param a
     * @throws Exception
     */
    public void addNamePartNameGame(List<String> a) throws Exception{
        String namePart = this.sportfacade.getNameParticipant(a.get(0),a.get(1));
        a.add(namePart);
        System.out.println(namePart);
        String nameGame = this.sportfacade.getGameName(a.get(0));
        a.add(nameGame);
        System.out.println(nameGame);
    }

    /**
     * Function that allows to see the current bet
     * @param email String
     * @return List<List<String>>
     */
    public List<List<String>> showBet(String email){
        List<List<String>> res = new LinkedList<>();
        try{
            int idUser = this.userfacade.getIdUser(email);
            res = this.betfacade.showBet(email, idUser);
            int i = 0;
            for(List<String> a : res){
                if (i != 0){
                    addNamePartNameGame(a);
                }
                i++;
            }
        }catch (Exception e){System.out.println(e.getMessage());}
        return res;
    }


    /**
     * Function that allows to consult all the Bets previously made.
     * If there are no bets, or the user is not found, an empty list is returned
     * @param email String
     * @return List<List<List<String>>>
     */
    public List<List<List<String>>> consult_Bets_History(String email) {
        List<List<List<String>>> allBets = this.betfacade.consult_bets(email);
        for(List<List<String >> res: allBets) {
            int i = 0;
            for (List<String> a : res) {
                if (i != 0) {
                    try {
                        addNamePartNameGame(a);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                i++;
            }
        }
        return allBets;
    }

    /**
     * Function that allows to add a guess to a bet not set.
     * It creates a new bet, or adds a guess to a old one.
     * An exception is thrown if the user is not found, the bet is already at max capacity,
     * the guessed outcome is invalid,
     * @param email String
     * @param idGame String
     * @param guess String
     * @throws Exception
     */
    public void add_to_bet(String email, String idGame, String guess) throws Exception {
        boolean state = this.sportfacade.checkGameOpen(idGame);            //Checks if a game exists and is open

        boolean draw = this.sportfacade.check_guess_draw(idGame, guess);  //If the outcome is draw, checks if the game can have the result draw

        if (state && draw) {
            int idUser = this.userfacade.getIdUser(email);                //Checks if the user exists
            if (idUser == -1) throw new Exception("User not found.");

            if(this.betfacade.bet_max_capacity(email))
                throw new Exception("Bet already at max capacity (max. 20 guesses).");

            float odd = this.sportfacade.getOddOutcome(idGame, guess);   //Checks if the outcome is valid
            if (odd == -1) throw new Exception("Guessed Outcome not valid.");

            if(check_promotion_active(multiplier)){                     // If a promotion is active...
                float minVal = this.promotions.get(multiplier).getMinVal();
                float amount = this.promotions.get(multiplier).getAmount();
                if(odd > minVal){
                    odd *= amount;
                }
            }
            odd*=this.userfacade.oddMultiplierCoin(email);              // Se a moeda for euro o multiplier vai ser 1

            this.betfacade.bet(email, idUser, odd, guess, idGame);
        }
    }

    /**
     * Function that allows to change state of a bet to set.
     * The money is remove from the wallet.
     * It returns an exception if ...
     * @param email String
     * @param money float
     * @throws Exception
     */
    public void setBetWallet(String email, float money) throws Exception{
        int idBet = this.betfacade.idBetNotSet(email);                        // descobrir a bet not set
        if(idBet == -1) throw new Exception("There is not bet to be pay.");

        boolean allGamesActive = this.sportfacade.checkAllGamesActive(idBet); // verifica se todos os jogos estão ativos
        if(!allGamesActive) {
            this.betfacade.update_poss_earning_and_remove_gamesNotActive(idBet);
            throw new Exception("Not all games are currectly open.");
        }

        boolean m = hasEnoughMoney(email, money);
        if(!m)
            throw new Exception("Not enough money in the wallet.");

        this.betfacade.setBet(idBet, money);                                 // Update money betted and state of bet
        this.userfacade.removeMoneyBalanceAddTransaction(email, money);
                    //Add the transaction to the user account

        for (String idGame: this.betfacade.getGamesOfBet(idBet)) {
            startFollowingGame(email, idGame);
        }
    }

    /**
     * Function that allows to set the money betted, and this money is not removed from Wallet
     * @param email String
     * @param money float
     * @throws Exception
     */
    public void setBetPayMethod(String email, float money) throws Exception{
        int idBet = this.betfacade.idBetNotSet(email);                         //descobrir a bet not set
        if(idBet == -1)
            throw new Exception("There is not bet to be set.");

        boolean allGamesActive = this.sportfacade.checkAllGamesActive(idBet); //verifica se todos os jogos estão ativos
        if(!allGamesActive)
            throw new Exception("Not all games are open.");

        this.betfacade.setBet(idBet, money);
        this.userfacade.addTransaction(email, this.userfacade.getTransactionBet(), money);
    }

    /**
     * Function that allows to remove a guess from the bet no set
     * @param email String
     * @param idGame String
     */
    public void removeFromBet(String email, String idGame){
        this.betfacade.removeFromBet(email, idGame);
    }

    /*--------------------------------------------------------------*/




    public void resolve_bets(String idGame){
        if(!this.sportfacade.checkGameClosed(idGame)){                  //Se o jogo ainda não tiver terminado, não faz nada
            return;
        }
        String outcome = this.sportfacade.getOutcomeGame(idGame);          //Get result Game:

        Map<String, Float> correctGuesses = this.betfacade.finishbet(idGame,outcome);
        for(Map.Entry<String, Float> g : correctGuesses.entrySet()){ //Email, earnings => only of the bets correctly guessed
            addNotificationBet(g.getKey());
            System.out.println(g.getKey());
            this.userfacade.updateBalanceWallet(g.getKey(), g.getValue());
            try {
                this.userfacade.addTransaction(g.getKey(), this.userfacade.getTransactionWon(), g.getValue());
            }
            catch (Exception e){}
        }
    }


    /**
     * Function that loads data of the football games of the api. It returns a list with all the games that ended
     */
    public void loadGamesFootball(){
        List<String> idGamesEnded = this.sportfacade.loadGamesFootball();   //Returns a list with all the games that ended
        for(String idG: idGamesEnded){
            resolve_bets(idG);              //resolve bets with all the games ended
        }
    }


    // -----------------------------------------------------------------------------------------------

    /**
     * Function that adds the user as follower of game
     * @param email String
     * @param game String
     * @return boolean
     */
    public boolean startFollowingGame(String email, String game){
        int idUser = this.userfacade.getIdUser(email);
        System.out.println(email);
        boolean b = this.sportfacade.startFollowingGame(idUser,game);
        return b;
    }

    /**
     * Function that allows a user to stop following game
     * @param email String
     * @param idGame String
     * @return boolean
     */
    public boolean stopFollowingGame(String email, String idGame){
        int idUser = this.userfacade.getIdUser(email);
        boolean b = this.sportfacade.stopFollowing(idUser, idGame);
        return b;
    }

    /**
     *  Function that allows to get all the games that a certain user follows
     * @param email String
     * @return List<String>
     */
    public List<String> get_followed_games(String email){
        List<String> ret = this.userfacade.get_followed_games(email);
        return ret;
    }

}