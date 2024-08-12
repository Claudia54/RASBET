package com.example.demo.RASBET_LN.Bet;

import com.example.demo.RASBET_DATA.BetDAO;

import java.util.*;

public class BetFacade {
    private BetDAO betdao; //functions access database related to Bets

    public BetFacade(){
        this.betdao = new BetDAO();
    }

    /**
     * Function that allows to get the id of a bet not set.
     * It returns the id of the bet or -1 if there is no bet not set
     * @param email String
     * @return int
     */
    public int idBetNotSet(String email){
        int idBet = this.betdao.get_id_not_set(email);
        return idBet;
    }

    /**
     * Function that allows to get the current bet not set
     * @param email String
     * @return List<List<String>>
     * @throws Exception
     */
    public List<List<String>> showBet(String email, int idUser) throws Exception{
        List<List<String>> res = new ArrayList<>();
        int idBetNotSet = idBetNotSet(email);
        if(idBetNotSet == -1) {
            betdao.init_bet(idUser, email);
            idBetNotSet = idBetNotSet(email);
            // throw new Exception("Bet not set of user not found");
        }

        try {
            Bet notSet = this.betdao.get_bet(idBetNotSet, email);
            res = notSet.notStructured();
        }
        catch (Exception e){
            throw new Exception("Bet not found");
        }
        return res;
    }

    /**
     * Function that get all the bets done by a user.
     * It returns an empty list if the user is not found or if there are no bets
     * @param email String
     * @return List<Bet>
     */
    public List<List<List<String>>> consult_bets(String email) {
        List<List<List<String>>> allBets = new LinkedList<List<List<String>>>();
        List<Bet> bets = this.betdao.consult_bets(email);
        for(Bet b: bets){
            allBets.add(b.notStructured());
        }
        return allBets;
    }

    /**
     * Function that determines if a bet is already at max capacity (max. 20 guesses)
     * It returns true if the number of guesses is 20
     * Exception is thrown if a bet not set is not found.
     * @param email String
     * @return boolean
     * @throws Exception
     */
    public boolean bet_max_capacity(String email) throws Exception{
        int idBetNotSet = idBetNotSet(email);
        if (idBetNotSet == -1)
            throw new Exception("Bet not set of user not found");
        Bet notSet = this.betdao.get_bet(idBetNotSet, email);

        if(notSet.getGuesses().size() == 20) return true;
        else return false;
    }

    /**
     * Function that allows to add a guess to a bet not set, or create a new bet not set
     * It throws an exception if the game is already on this bet.
     * @param email String
     * @param idUser int
     * @param odd float
     * @param guess String
     * @param idGame String
     * @throws Exception
     */
    public void bet(String email, int idUser, float odd, String guess, String idGame) throws Exception{
        if(!betdao.exists_bet_not_set(email)){                          //Criar uma bet se não existir nenhuma não concluida
            betdao.init_bet(idUser, email);
        }

        int idBet = idBetNotSet(email);
        if(!this.betdao.checks_game_not_in_bet(idBet, idGame)){         //Se um jogo já existir na bet dá erro
            throw new Exception("Game \""+idGame+"\" already on this bet.");
        }

        this.betdao.init_game_on_bet(email, idGame, guess, odd);             //Adiciona novo game_on_bet
        float poss_earnings = betdao.get_possible_earnings(idBet);
        poss_earnings *= odd;
        betdao.update_possible_earnings(idBet, poss_earnings);          //Updates possible earnings of the bet
    }

    public void setBet(int idBet, float money) {
        this.betdao.update_money(idBet, money);                         //update the money from a bet
        float poss_earnings = betdao.get_possible_earnings(idBet);      //gets current possible earnings
        poss_earnings *= money;
        this.betdao.update_possible_earnings(idBet, poss_earnings);     //update possible earnings
        this.betdao.update_state(idBet, Bet.set());                     //mudar estado bet para Set
    }


    /**
     * Function that allows to remove a game that is currently not open, and reduces the odd of guess of
     * that game from possible earnings.
     * @param idBet int
     */
    public void update_poss_earning_and_remove_gamesNotActive(int idBet){
        this.betdao.update_poss_earning_and_remove_gamesNotActive(idBet);
    }

    public Map<String, Float> finishbet(String idGame, String outcome){
        Map<String, Float> betscorrect = new HashMap<>();
        Map<Integer, String> allGameonBets = this.betdao.get_all_game_on_bet_not_checked(idGame, Bet.getNot_checked()); //Get all bets with this game that are not checked: (idBet, guess)
        for(Map.Entry<Integer, String> bet: allGameonBets.entrySet()) {
            System.out.println(bet.getKey() + "_" + bet.getValue());

            if (this.betdao.get_state(bet.getKey()).equals(Bet.set())) {                                                //Se a bet em questão ainda não estiver concluded (e estiver paga)
                this.betdao.check_game_on_bet(bet.getKey(), idGame, Bet.getChecked());                                  //update game on bet . checked

                if (bet.getValue().equals(outcome)) {                                                                   //if the user correctly guessed the result

                    List<Integer> allGames_ofBet = betdao.get_game_on_bet_of_bet(bet.getKey());                         //gets all the game_on_bets, dessa bet
                    if (allGames_ofBet.stream().noneMatch(n -> n != 1)) {                                               //como acertou, estão todas vistas, e a bet não estava concluded:
                        float amount = betdao.get_possible_earnings(bet.getKey());
                        String email = betdao.getEmailOfBet(bet.getKey());

                        betscorrect.put(email, amount);

                        //concluded
                        betdao.update_state(bet.getKey(), Bet.concluded());
                    }
                }
                else {                                                                                                  //if the user didn't correctly guessed the result
                    betdao.update_state(bet.getKey(), Bet.concluded());                                                 //Mudar o estado da Bet para concluded
                    this.betdao.check_all_game_on_bet(bet.getKey(), Bet.getChecked());                                  //atualizar o estado de todas as game on bets para checked:
                }
            }
        }
        return betscorrect;
    }


    /**
     * Function that, given an email and the value of the new coin, converts the value of the possible earnings and of the odds according to the value of the new coin
     * @param email String
     * @param valCoin float
     */
    public void changeCoinBetsNotConcluded(String email, float valCoin){
        List<Bet> list = this.betdao.get_all_bets_not_concluded(email, Bet.set(), Bet.noset());
        for(Bet b : list){
            float updated_possible_earnings = 1;
            for(List<String> s: b.getGuesses()){
                float f = Float.valueOf(s.get(2));
                f*= valCoin;
                s.set(2,Float.toString(f));
                updated_possible_earnings *= f;
            }

            float pe = updated_possible_earnings*b.getMoney();
            b.setPossible_earnings(pe);

        }
        this.betdao.update_bets_not_concluded(list);
    }



    public void removeFromBet(String email, String idGame){
        int idBet = idBetNotSet(email);
        updatePossibleEarnings(idBet, idGame);
        this.betdao.remove_certain_game_on_bet(idBet, idGame);
    }

    public void updatePossibleEarnings(int idBet, String idGame){
        float possible_earnings = this.betdao.get_possible_earnings(idBet);
        float oddGuess = this.betdao.getOddGuessGame(idBet, idGame);

        if(this.betdao.get_game_on_bet_of_bet(idBet).size()==1){
            possible_earnings=1;
        }
        else {
            possible_earnings /= oddGuess;
        }

        this.betdao.update_possible_earnings(idBet, possible_earnings);
    }


    public List<String> getGamesOfBet(int idBet){
        List<String> l = new ArrayList<>();
        l = this.betdao.getGamesOfBet(idBet);
        return l;
    }

}
