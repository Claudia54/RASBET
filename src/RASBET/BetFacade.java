package RASBET.RASBET;

import RASBET.RASBET_DATA.BetDAO;

import java.util.List;

public class BetFacade {
    private BetDAO betdao; //functions access database related to Bets

    public BetFacade(){
        this.betdao = new BetDAO();
    }


    /**
     * Function that get all the bets done by a User.
     * It returns an empty list if the user is not found or if there are no bets
     * @param email String
     * @return List<Bet>
     */
    public List<Bet> consult_bets(String email) {
        List<Bet> bets = this.betdao.consult_bets(email);
        return bets;
    }





    public void bet(String email, int idUser, float odd, String guess, String idGame) throws Exception{
        if(!betdao.exists_bet_not_set(email)){
            betdao.init_bet(idUser, email);                             //Criar uma bet se não existir nenhuma não concluida
        }

        int idBet = betdao.get_id_not_set(email);
        if(!this.betdao.checks_game_not_in_bet(idBet, idGame)){         //Se um jogo já existir na bet dá erro
            throw new Exception("Game \""+idGame+"\" already on this bet.");
        }

        this.betdao.init_game_on_bet(email, idGame, guess);             //Adiciona novo game_on_bet
        float poss_earnings = betdao.get_possible_earnings(idBet);
        poss_earnings *= odd;
        betdao.update_possible_earnings(idBet, poss_earnings);          //Updates possible earnings of the bet
    }



    public int idBetNotSet(String email){
        int idBet = this.betdao.get_id_not_set(email);
        return idBet;
    }


    public void setBet(int idBet, float money) {
        //pagar bet e atualizar possible earnings
        this.betdao.update_money(idBet, money);
        float poss_earnings = betdao.get_possible_earnings(idBet);
        poss_earnings *= money;
        this.betdao.update_possible_earnings(idBet, poss_earnings);
        //mudar estado bet
        this.betdao.update_state(idBet, Bet.set());
    }


    public void removeBetNotSet(String email){
        if (this.betdao.exists_bet_not_set(email)) {
            int idBet = this.betdao.get_id_not_set(email);

            //Remover all game_on_bet of this bet:
            this.betdao.remove_game_on_bet(idBet);

            //Remove bet:
            this.betdao.remove_bet(idBet, email);
        }
    }

    public String showBet(String email){
        int idBetNotSet = this.betdao.get_id_not_set(email);
        return this.betdao.show_bet(idBetNotSet);
    }
}
