package RASBET.RASBET;

import RASBET.RASBET_DATA.*;

import java.util.*;

/**
 * Class RASBET_Facade
 */
public class RASBET_Facade implements I_RASBET_Facade{
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
                        loadJSONFootball();
                    }
                },
                0,
                10*1000
        );
        this.promotions = new HashMap<>();
        Promotion friend = new Promotion(this.friendBonus, 10, 0) ;
        friend.setState(true);
        this.promotions.put(friend.getType(), friend);
        Promotion bonus = new Promotion(this.bonus, 5, 0);
        this.promotions.put(bonus.getType(), bonus);
        Promotion multiplier = new Promotion(this.multiplier, (float) 1.05,2);
        this.promotions.put(multiplier.getType(), multiplier);
    }


    /**
     * Function that checks if the promotion of type exists.
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
     * Function that gets a list with all the promotions in the program
     * @return List<String>
     */
    public List<String> getPromotions(){
        List<String> allPromotions = new ArrayList<String>();
        for(Promotion promo : this.promotions.values()){
            allPromotions.add(promo.toString());
        }
        return allPromotions;
    }

    //Notify all users

    /**
     * Function that changes the state of the promotion bonus to active.
     */
    public void activatePromoBonus(){
        this.promotions.get(bonus).setState(true);
    }

    //Notify all users

    /**
     * Function that changes the state of the promotion multiplier to active.
     */
    public void activatePromoMultiplier(){
        this.promotions.get(multiplier).setState(true);
    }

    /**
     * Allows to change the state of the promotion bonus to deactivated
     */
    public void deactivatePromoBonus(){
        this.promotions.get(bonus).setState(false);
    }

    /**
     * Allows to change the state of the promotion multiplier to deactivated
     */
    public void deactivatePromoMultiplier(){
        this.promotions.get(multiplier).setState(false);
    }

    /**
     * Function that allows the system to identify the amount of a certain promotion.
     * It throws an exception if the promotion type is not found.
     * @param type String
     * @return float
     */
    public float get_amount_bonus(String type) throws Exception{
        if(this.promotions.get(type)!= null){
            return this.promotions.get(type).getAmount();
        }
        else throw new Exception("Promotion not found.");
    }

    /**
     * Function that checks if the Promotion bonus exists, and if it is active. If so, an extra bonus balance is given to the user that just register
     * This function should be called after register, as this promotion adds a bonus balance after signin
     * @param email String
     */
    public void checkandrewardpromotionbonus(String email){
        if(this.promotions.get(bonus)!= null){
            if(check_promotion_active(bonus)){
                try {
                    float bonusamount = get_amount_bonus(bonus);
                    this.userfacade.updateBonusBalance(email, bonusamount);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
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
            if (this.userfacade.existsUserCode(friendCode)) {
                float bonus = get_amount_bonus(friendBonus);
                this.userfacade.updateBonusBalance(email, bonus);
            }
            checkandrewardpromotionbonus(email);
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
            success = this.userfacade.createStaff(email, name, pwd, address, phone_number, nif, cc, birth, this.userfacade.admin);
        }
        catch (Exception e){
            throw e;
        }
        return success;
    }

    /**
     * Function that allows to register a specialist with the given data. No wallet is created.
     *      * It makes sure the email, cc and nif doesn't exist yet, and the age given is older than 18; otherwise, Exceptions are thrown.
     *      * It returns true if the user was correctly created
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
            success = this.userfacade.createStaff(email, name, pwd, address, phone_number, nif, cc, birth, this.userfacade.specialist);
        }
        catch (Exception e){
            throw e;
        }
        return success;
    }

    /**
     * Function used to log in a user.
     * It checks if the user with email exists, and if its password is pwd.
     * Returns true if the data given is correct.
     * @param email String
     * @param pwd String
     * @return boolean
     */
    public boolean login(String email, String pwd) {
        return this.userfacade.login(email, pwd);
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
            throw new Exception(e.getMessage());
        }
        return res;
    }

    /**
     * Function that gets the role of the User
     * It the user doesn't exist, it returns an Exception
     * @param email String
     * @throws Exception
     * @return String
     */
    public String getRole(String email) throws Exception{
        String res;
        try {
            res = this.userfacade.getRole(email);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return res;
    }

    /**
     * Function that allows a certain user with email to change their name to newName
     * It throws an Exception if it is not possible to find an user with email
     * @param email String
     * @param newName String
     * @throws Exception
     */
    public void changeName(String email, String newName) throws Exception{
        try{
            this.userfacade.changeName(email,newName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Function that allows a user with email to change their password to newPwd
     * If the password is not valid, or email not found it returns exception
     * @param email String
     * @param newPwd String
     * @throws Exception
     */
    public void changePwd(String email, String newPwd) throws Exception {
        try{
            this.userfacade.changePwd(email, newPwd);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Function that allows a certain user to change their email to newEmail
     * If the new email already exists, or if the user can't be found, it throws an exception
     * @param email String
     * @param newEmail String
     * @throws Exception
     */
    public void changeEmail(String email, String newEmail) throws Exception {
        try{
            this.userfacade.changeEmail(email, newEmail);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
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
     * Function that allows a certain user to change their address
     * @param email String
     * @param newAddress String
     * @throws Exception
     */
    public void changeAddress(String email, String newAddress, String token) throws Exception {
        try {
            this.userfacade.changeAddress(email, newAddress, token);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Function that allows a user to change their phoneNumber
     * It throws an exception if the user is not found, or if the phone number doesn't have a valid format
     * @param email String
     * @param newPhoneNumber String
     * @throws Exception
     */
    public void changePhoneNumber(String email, String newPhoneNumber, String token) throws Exception{
        try{
            this.userfacade.changePhoneNumber(email, newPhoneNumber, token);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * Function that allows a user to see their wallet. It includes the balance and bonus balance.
     * This function returns Exception if the wallet of this user can't be found
     * @param email String
     * @return String
     * @throws Exception
     */
    public String consult_Wallet(String email) throws Exception {
        String res;
        try {
            res = this.userfacade.getWallet(email);
        }
        catch (Exception e){
            throw e;
        }
        return res;
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
     * Function that allows a user to remove money to their account
     * After this method, a transfer should be made.
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
     * Function that allows a user to see their transations
     * It returns an empty list if the user doesn't exist or if the array of transactions is empty
     * @param email String
     * @return List<String>
     */
    public List<String> getTransactions(String email){
        return this.userfacade.getTransactions(email);
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
     * @return List<String>
     */
    public List<String> get_Games_of_Sport(String nameSport){
        return this.sportfacade.get_games_sport(nameSport);
    }

    /**
     * Function that gets the names of all the sports in the system.
     * If there are no sports in the database, an empty array is returned.
     * @return List<String>
     */
    public List<String> getNameSports() {
        return this.sportfacade.getNameSports();
    }

    /**
     * Function that allows to change the odd of a home team.
     * If the game doesn't exist, the odd is not positive or the game is not open, it returns an exception.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddHomeTeamGame(String idGame, float newOdd) throws Exception{
        try {
            this.sportfacade.changeOddHomeTeamGame(idGame, newOdd);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * Function that allows to change the odd of an away team.
     * If the game doesn't exist, the odd is not positive or the game is not open, it returns an exception.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddAwayTeamGame(String idGame, float newOdd) throws Exception{
        try {
            this.sportfacade.changeOddAwayTeamGame(idGame, newOdd);
        }
        catch (Exception e){
            throw e;
        }

    }

    /**
     * Function that allows to change the odd of the draw.
     * If the game doesn't exist, the odd is not positive, the game is not open or the game doesn't allow draw, it returns an exception.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddDrawGame(String idGame, float newOdd) throws Exception{
        try {
            this.sportfacade.changeOddDrawGame(idGame, newOdd);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * Function that allows an admin to close a game.
     * It sends an exception if the game doesn't exist or is closed.
     * @param idGame String
     * @throws Exception
     */
    public void closeGame(String idGame) throws Exception{
        try{
            this.sportfacade.closeGame(idGame);
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


/*--------------------------------------------------------------*/


    public void changeCurrency(String email, String currency) throws Exception{

    }








    //User pode cancelar bet:


    public List<Bet> consult_Bets_History(String email) {
        List<Bet> bets = this.betfacade.consult_bets(email);
        return bets;
    }

    public void add_to_bet(String email, String idGame, String guess) throws Exception {
        boolean state;
        try {
            state = this.sportfacade.checkGameOpen(idGame);                         //Checks if a game is open
        } catch (Exception e) {
            throw e;
        }
        boolean draw;
        try {
            draw = this.sportfacade.checkDraw(idGame, guess);                       //If the outcome is draw, checks if the game can have the result draw
        } catch (Exception e) {
            throw e;
        }
        if (state && draw) {
            int idUser = this.userfacade.getIdUser(email);
            if (idUser == -1)                                                        //Checks if the user exists
                throw new Exception("User not found.");
            float odd = this.sportfacade.getOddOutcome(idGame, guess);
            if (odd == -1)                                                           //Checks if the outcome is valid
                throw new Exception("Guessed Outcome not valid.");

            if(check_promotion_active(multiplier)){
                float minVal = this.promotions.get(multiplier).getMinVal();
                float amount = this.promotions.get(multiplier).getAmount();

                if(odd > minVal){
                    odd *= amount;
                }
            }

            try {
                this.betfacade.bet(email, idUser, odd, guess, idGame);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public boolean hasMoneyWallet(String email, float valueToPay) throws Exception{
        boolean success;
        try {
            success = this.userfacade.hasEnoughMoney(email, valueToPay);
        }
        catch (Exception e){
            throw e;
        }
        return success;
    }

    //Desconta saldo:
    public void reduceBalance(String email, float valueToPay) throws Exception{
        try{
            this.userfacade.reduceMoneyPayBet(email, valueToPay);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void setBet(String email, float money) throws Exception{
        int idBet = this.betfacade.idBetNotSet(email);                  //descobrir a bet not set
        if(idBet == -1){
            throw new Exception("There is not bet to be set.");
        }
        boolean allGamesActive = this.sportfacade.checkAllGamesActive(idBet); //verifica se todos os jogos estão ativos

        if(hasMoneyWallet(email, money) && allGamesActive) {
            this.betfacade.setBet(idBet, money);
            reduceBalance(email, money);
            this.userfacade.addTransaction(email, Wallet.bet, money);
        }
        else{
            System.out.println("Not enough money on the wallet or one of the games selected is suspended or already finished");
        }
    }



    public boolean addResult(String idGame, String result){
        return this.sportfacade.addResultGame(idGame, result);
    }

    public boolean addFinalResult(String idGame, String result) {
        boolean success;
        String res = Game.parseResult(result);
        if (res.equals("")) {
            success = false;
        } else{
            success = this.sportfacade.addFinalResultGame(idGame, res);
            if(!success)
                System.out.println("Result not added.");
        }
        return success;
    }

    //Ends game, and result should be something as (1x2)
    public void endGame(String idGame, String result) throws Exception{
        try{
            this.sportfacade.closeGame(idGame);
        }
        catch (Exception e){
            throw e;
        }
        boolean success = addFinalResult(idGame, result);
        resolve_bets(idGame);
    }

    //Pre: Jogo tem que vir já fechado e com o resultado:
    public void resolve_bets(String idGame){

        if(!this.sportdao.getStateGame(idGame).equals(Game.closed())){
            return;
        }
        //Get result Game:
        String outcome = this.sportdao.getOutcomeGame(idGame);

        //Get all bets with this game that are not checked: (idBet, guess)
        Map<Integer, String> allGameonBets = this.betdao.get_all_game_on_bet_not_checked(idGame, Bet.getNot_checked());
        for(Map.Entry<Integer, String> bet: allGameonBets.entrySet()) {
            System.out.println(bet.getKey() + "_" + bet.getValue());

            //Se a bet em questão ainda não estiver concluded:
            if (this.betdao.get_state(bet.getKey()).equals(Bet.set())) {
                //update game on bet . checked
                this.betdao.check_game_on_bet(bet.getKey(), idGame, Bet.getChecked());

                //if the user correctly guessed the result
                if (bet.getValue().equals(outcome)) {
                    //(checked)
                    List<Integer> allGames_ofBet = betdao.get_game_on_bet_of_bet(bet.getKey());
                    //como acertou, estão todas vistas, e a bet não estava concluded:
                    if (allGames_ofBet.stream().noneMatch(n -> n != 1)) {
                        float amount = betdao.get_possible_earnings(bet.getKey());

                        String email = betdao.getEmailOfBet(bet.getKey());

                        float currentValue = udao.getBalanceWallet(email);
                        currentValue += amount;
                        udao.updateBalanceWallet(email, currentValue);

                        //The value idWallet in the DB is equal to the idUser of the Wallet
                        //The idUser in the wallet is the same as the id in User:
                        int idUser = udao.getIdUser(email);
                        udao.addTransaction(idUser, Wallet.won, amount);

                        //concluded
                        betdao.update_state(bet.getKey(), Bet.concluded());
                    }
                }
                //if the user didn't correctly guessed the result
                else {
                    //Mudar o estado da Bet para concluded
                    betdao.update_state(bet.getKey(), Bet.concluded());

                    //atualizar o estado de todas as game on bets para checked:
                    this.betdao.check_all_game_on_bet(bet.getKey(), Bet.getChecked());
                }
            }
        }
    }


    //Function that at logout removes the bets not set of a certain user:
    public void removeBetsNotSet(String email) {
        //if exists any bet that is not set
        this.betfacade.removeBetNotSet(email);
    }

    public String showBet(String email){
        return this.betfacade.showBet(email);
    }





    public void loadJSONFootball(){
        int idSport = sportdao.getSportId("Football");
        ReadJSON novo = new ReadJSON();
        novo.getRequest();

        List<Game> games = novo.readFile(idSport);
        for (Game g: games) {
            String idGame = g.getIdGame();
            //If a game exists:
            if (sportdao.existsGame(idGame)) {
                //The state of the game is not the same as the one in the database
                String val = Game.closed();
                if(g.getState().equals(val) && !sportdao.getStateGame(idGame).equals(val)){
                    //Fecha o jogo:
                    sportdao.changeStateGame(idGame, val);

                    //Atualiza o resultado:
                    sportdao.changeOutcomeGame(idGame, g.getOutcome());

                    //resolve bets:
                    resolve_bets(idGame);
                }
                else{
                    float V1 = sportdao.getOddGame(idGame, Game.homeOutcome());
                    float X = sportdao.getOddGame(idGame, Game.drawOutcome());
                    float V2 = sportdao.getOddGame(idGame, Game.awayOutcome());
                    if(g.getV1() != V1) {
                        sportdao.changeStateGame(idGame, Game.suspended());
                        sportdao.changeOddGame(idGame, g.getV1(), Game.homeOutcome());
                        sportdao.changeStateGame(idGame, Game.open());
                    }
                    if( g.getX() != X){
                        sportdao.changeStateGame(idGame, Game.suspended());
                        sportdao.changeOddGame(idGame, g.getX(), Game.drawOutcome());
                        sportdao.changeStateGame(idGame, Game.open());
                    }
                    if(g.getV2() != V2){
                        sportdao.changeStateGame(idGame, Game.suspended());
                        sportdao.changeOddGame(idGame, g.getV2(), Game.awayOutcome());
                        sportdao.changeStateGame(idGame, Game.open());
                    }
                }
            }
            //If a game doesn't exist is added
            else {
                sportdao.addGame(g);
            }
        }
    }

    public static void main(String[] args){
        RASBET_Facade facade = new RASBET_Facade();
        facade.loadJSONFootball();
    }

}