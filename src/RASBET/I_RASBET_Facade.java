package RASBET.RASBET;

import java.util.List;

public interface I_RASBET_Facade {

    boolean registerUser(String email, String name, String pwd, String address, String phone_number, String nif,
                                String cc, String birth, String friendCode) throws Exception;

    boolean registerAdmin(String email, String name, String pwd, String address, String phone_number, String nif,
                                 String cc, String birth) throws Exception;

    boolean registerSpecialist(String email, String name, String pwd, String address, String phone_number, String nif,
                                      String cc, String birth) throws Exception;

    boolean login(String email, String pwd);

    String getName(String email) throws Exception;

    String getRole(String email) throws Exception;

    void changeName(String email, String newName) throws Exception;

    void changePwd(String email, String newPwd) throws Exception;

    void changeEmail(String email, String newEmail) throws Exception;

    String getTokenIdentification(String email) throws Exception;

    void changeAddress(String email, String newAddress, String token) throws Exception;

    void changePhoneNumber(String email, String newPhoneNumber, String token) throws Exception;

    public String consult_Wallet(String email) throws Exception;

    void deposit_money(String email, float value) throws Exception;

    void withdraw_money(String email, float value) throws Exception;

    List<String> getTransactions(String email);

    List<String> get_Games_of_Sport(String nameSport);

    void changeOddHomeTeamGame(String idGame, float newOdd) throws Exception;

    void changeOddAwayTeamGame(String idGame, float newOdd) throws Exception;

    void changeOddDrawGame(String idGame, float newOdd) throws Exception;

    void closeGame(String idGame) throws Exception;

    void openGame(String idGame) throws Exception;

    void suspendGame(String idGame) throws Exception;

    List<Bet> consult_Bets_History(String email);

    void add_to_bet(String email, String idGame, String guess) throws Exception;





    void setBet(String email, float money) throws Exception;

    boolean addResult(String idGame, String result);

    void endGame(String idGame, String result) throws Exception;

    void removeBetsNotSet(String email);

    String showBet(String email);

    List<String> getPromotions();

    void activatePromoBonus();

    void activatePromoMultiplier();

}
