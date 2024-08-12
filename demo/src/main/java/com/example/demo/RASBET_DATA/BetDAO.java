package com.example.demo.RASBET_DATA;

import com.example.demo.RASBET_LN.Bet.Bet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetDAO extends DAO{

    public BetDAO(){
        super();
    }

    /*
     * ---------------------------------------------------------------------------------------------------------
     * GAME_ON_BET FUNCTIONS
     * ---------------------------------------------------------------------------------------------------------
     */

    /**
     * Given the email of an user, it is added to a bet that is not set, the guess of a certain user.
     * The bet not set should already exist.
     * @param email String
     * @param idGame String
     * @param guess String
     */
    public void init_game_on_bet(String email, String idGame, String guess, float odd){
        super.startConnStmt();
        String sql = "SELECT idBet FROM Bet where email = '"+email+"' and state = '"+Bet.noset()+"';";
        ResultSet result = querySQL(sql);
        int id = super.resultSetGetFirstInt(result, -1);

        //Adiciona novo Game_on_Bet, que ainda não foi "checked":
        sql = "insert into Game_on_Bet values ("+id+",'"+idGame+"','"+guess+"', "+odd+" ,0);";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that gets all the games on bet with the game idGame that haven't been checked yet.
     * It returns a map, where the key is the idBet, and the value is the guess for the game idGame
     * @param idGame String
     * @param notchecked int
     * @return Map<Integer,String>
     */
    public Map<Integer,String> get_all_game_on_bet_not_checked(String idGame, int notchecked){
        super.startConnStmt();
        String sql = "select idBet, guess from Game_on_Bet where idGame = '"+idGame+"' and checked = "+notchecked+";";
        ResultSet result = querySQL(sql);

        Map<Integer,String> output = new HashMap<>();
        try{
            while(result.next()){
                output.put(result.getInt(1), result.getString(2));
            }
        }catch (SQLException s){
            System.out.println("Error: impossible to read the ResultSet.");
        }
        super.closeConn();
        return output;
    }

    /**
     * Function that gets all the game_on_bet in a certain bet idBet.
     * @param idBet int
     * @return List<Integer>
     */
    //(checked)
    public List<Integer> get_game_on_bet_of_bet(int idBet){
        super.startConnStmt();
        String sql = "select checked from Game_on_Bet where idBet = "+idBet+";";
        ResultSet result = querySQL(sql);
        List<Integer> values = new ArrayList<>();
        try{
            while(result.next()){
                values.add(result.getInt(1));
            }
        }catch (SQLException s){
            System.out.println("Error: impossible to read the ResultSet.");
        }
        super.closeConn();
        return values;
    }

    /**
     * Function that updates the checked value of one game_on_bet of a certain bet idBet, related to a certain game idGame
     * @param idBet int
     * @param idGame String
     * @param val int
     */
    public void check_game_on_bet(int idBet, String idGame, int val){
        super.startConnStmt();
        String sql = "update Game_on_Bet set checked = "+val+" where idBet = "+idBet+" and idGame = '"+idGame+"';";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Removes all game_on_bet of the bet idBet
     * @param idBet int
     */
    public void remove_game_on_bet(int idBet){
        super.startConnStmt();
        String sql = "delete from Game_on_Bet where idBet = "+idBet+";";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that removes a certain game_on_bet of a bet idBet, where the game is idGame
     * @param idBet int
     * @param idGame String
     */
    public void remove_certain_game_on_bet(int idBet, String idGame){
        super.startConnStmt();
        String sql = "delete from Game_on_Bet where idBet = "+idBet+" and idGame = '"+idGame+"';";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that changes the value of checked of all the game_on_bet of a certain idBet
     * @param idBet int
     * @param state int
     */
    public void check_all_game_on_bet(int idBet, int state){
        super.startConnStmt();
        String sql = "update Game_on_Bet set checked = "+state+" where idBet="+idBet+";";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that gets the email associated to a bet idBet
     * @param idBet int
     * @return String
     */
    public String getEmailOfBet(int idBet){
        super.startConnStmt();

        String sql = "SELECT email FROM Bet where idBet = '"+idBet+"';";
        ResultSet result = querySQL(sql);
        String email = super.resultSetGetFirstString(result);
        super.closeConn();
        return email;
    }

    /**
     * ---------------------------------------------------------------------------------------------------------
     * FUNCTIONS OF BET
     * ---------------------------------------------------------------------------------------------------------
     */

    /**
     * Function that allows to get all the bets and all the guesses of each bet
     * It returns a list of Bet, or if there are no bets or user not found, it returns an empty list
     * @param email String
     * @return List<Bet>
     */
    public List<Bet> consult_bets(String email) {
        List<Bet> lista = new ArrayList<>();

        // Gets all the Bet
        super.startConnStmt();
        String sql = "select * from Bet where email = '" + email + "';";
        ResultSet result = querySQL(sql);
        try {
            while (result.next()) {
                Bet b = new Bet(result.getInt(1), result.getFloat(2), result.getFloat(3), result.getString(4), result.getString(6));
                lista.add(b);
            }
        }
        catch (SQLException s){
            System.out.println("Error getting content bet.");
        }

        // Gets the guesses of the Bet
        for(Bet b: lista) {
            String sqlPart = "select idGame, guess, odd_guess from Game_on_Bet where idBet = "+b.getIdBet()+";";
            result = querySQL(sqlPart);
            try {
                while (result.next()) {
                    b.addGuesses(result.getString(1), result.getString(2), result.getString(3));
                }
            } catch (SQLException s) {
                System.out.println("Error consulting results.");
            }
        }
        super.closeConn();
        return lista;
    }


    /**
     * Function that creates a new bet, without any guesses and not set.
     * To keep the correctness of the program it is expected that no other bet not set exists
     * @param id int
     * @param email String
     */
    public void init_bet(int id, String email) {
        super.startConnStmt();
        String sql = "insert into Bet (money, possible_earnings, email, idUser, state) values ( 1, 1,'"+email+"', "+id+", '"+Bet.noset()+"');";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that removes a bet with the idBet
     * @param idBet int
     * @param email String
     */
    public void remove_bet(int idBet, String email){
        super.startConnStmt();
        String sql = "delete from Bet where idBet = "+idBet+" and email = '"+email+"';";
        executeSQL(sql);
        super.closeConn();

    }

    /**
     * Function that verifies if for a certain user with the email has a certain bet not set
     * @param email String
     * @return boolean
     */
    public boolean exists_bet_not_set(String email){
        super.startConnStmt();
        String sql = "SELECT * FROM Bet where email = '"+email+"' and state = '"+Bet.noset()+"';";
        ResultSet result = querySQL(sql);
        boolean output = super.resultSetIsNotEmpty(result);
        super.closeConn();
        return output;
    }

    /**
     * Function that gets the id of the bet not set of the user.
     * If the user doesn't exist or no bet is not_set, the function returns -1.
     * @param email String
     * @return int
     */
    public int get_id_not_set(String email){
        super.startConnStmt();
        String sql = "SELECT * FROM Bet where email = '"+email+"' and state = '"+Bet.noset()+"';";
        ResultSet result = querySQL(sql);
        int id = super.resultSetGetFirstInt(result, -1);
        super.closeConn();
        return id;
    }

    /**
     * Function that sets the possible_earnings of the bet idBet
     * @param idBet int
     * @param possible_earnings float
     */
    public void update_possible_earnings(int idBet, float possible_earnings){
        super.startConnStmt();
        String sql = "update Bet set possible_earnings = "+possible_earnings+" where idBet = "+idBet+";";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that returns the possible earnings of the bet idBet
     * @param idBet int
     * @return float
     */
    public float get_possible_earnings(int idBet){
        super.startConnStmt();
        String sql = "SELECT possible_earnings FROM Bet where idBet = "+idBet+";";
        ResultSet result = querySQL(sql);
        float possibleEarnings = super.resultSetGetFirstFloat(result,0);
        super.closeConn();
        return possibleEarnings;
    }

    /**
     * Function that changes the money betted by the user on the bet idBet
     * @param idBet int
     * @param money float
     */
    public void update_money(int idBet, float money){
        super.startConnStmt();
        String sql = "update Bet set money = "+money+" where idBet = "+idBet+";";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Funtion that returns the money betted by the user in the bet idBet
     * @param idBet int
     * @return float
     */
    public float get_money(int idBet){
        super.startConnStmt();

        String sql = "SELECT money FROM Bet where idBet = "+idBet+";";
        ResultSet result = querySQL(sql);
        float money = super.resultSetGetFirstFloat(result, -1);
        super.closeConn();
        return money;
    }

    /**
     * Function that changes the state of a given bet idBet. The state should be verified before hand.
     * @param idBet int
     * @param state String
     */
    public void update_state(int idBet, String state){
        super.startConnStmt();
        String sql = "update Bet set state = '"+state+"' where idBet = "+idBet+";";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that returns the state of the bet idBet.
     * If the bet doesn't exist, the function returns "".
     * @param idBet int
     * @return String
     */
    public String get_state(int idBet){
        super.startConnStmt();
        String sql = "SELECT state FROM Bet where idBet = '"+idBet+"';";
        ResultSet result = querySQL(sql);
        String state = super.resultSetGetFirstString(result);
        super.closeConn();
        return state;
    }





    //Se não existir, isto é se estiver vazio, devolve true
    public boolean checks_game_not_in_bet(int idBet, String game){
        super.startConnStmt();
        String sql = "select * from Game_on_Bet where idBet = "+idBet+" and idGame = '"+game+"';";
        ResultSet res = querySQL(sql);
        boolean empty = super.resultSetIsNotEmpty(res);
        super.closeConn();
        return !empty;
    }


    /**
     * Function that returns the list of bets that are not concluded
     * @param email String
     * @param stateSet String
     * @param stateNotSet String
     * @return List<Bet>
     */
    public List<Bet> get_all_bets_not_concluded(String email, String stateSet, String stateNotSet){
        super.startConnStmt();
        String sql = "select * from Bet where email='"+email+"' and (state = '"+stateSet+"' or state = '"+stateNotSet+"');";
        ResultSet res = super.querySQL(sql);
        List<Bet> output = new ArrayList<>();
        try{
            while(res.next()){
                Bet b = new Bet(res.getInt(1), res.getFloat(2), res.getFloat(3), res.getString(4), res.getString(6));
                output.add(b);
            }
        }
        catch (Exception e){}
        for(Bet b: output) {
            String sqlPart = "select idGame, guess, odd_guess from Game_on_Bet where idBet = "+b.getIdBet()+";";
            res = querySQL(sqlPart);

            try {
                while (res.next()) {
                    b.addGuesses(res.getString(1), res.getString(2), res.getString(3));
                }
            } catch (SQLException s) {
                System.out.println("Error consulting results.");
            }
        }
        super.closeConn();
        return output;
    }

    /**
     * Function that updates all the bets that are not concluded yet.
     * @param bets List<Bet>
     */
    public void update_bets_not_concluded(List<Bet>bets){
        super.startConnStmt();
        for(Bet b: bets){
            String sql = "update Bet set possible_earnings = "+b.getPossible_earnings()+" where idBet = "+b.getIdBet()+";";
            executeSQL(sql);

            for(List<String> odd : b.getGuesses()){
                sql = "update Game_on_Bet set odd_guess = "+odd.get(2)+" where (idBet = "+b.getIdBet()+" and idGame = '"+odd.get(0)+"');";
                executeSQL(sql);
            }
        }
        super.closeConn();
    }

    public Bet get_bet(int idBet, String email) throws Exception{
        super.startConnStmt();
        String sql = "select * from Bet where email = '" +email+ "' and idBet = " +idBet+ ";";
        ResultSet result = querySQL(sql);

        Bet b = null;
        try {
            if (result.next()) {
                b = new Bet(result.getInt(1), result.getFloat(2), result.getFloat(3), result.getString(4), result.getString(6));
            }
        }
        catch (SQLException s){
            System.out.println("Error consulting results.");
            throw new Exception("Bet not found");
        }

        String sqlPart = "select idGame, guess, odd_guess from Game_on_Bet where idBet = "+idBet+";";
        result = querySQL(sqlPart);

        try {
            while (result.next()) {
                b.addGuesses(result.getString(1), result.getString(2), result.getString(3));
            }
        } catch (SQLException s) {System.out.println("Error consulting results.");}

        super.closeConn();
        return b;
    }


    
    public void remove_gamesNotActive(int idBet){
        super.startConnStmt();
        String sql = "delete from Game_on_Bet where idBet = "+idBet+";";
        executeSQL(sql);
        super.closeConn();
    }

    public void update_poss_earning_and_remove_gamesNotActive(int idBet){
        super.startConnStmt();
        String sql = "select Game_on_Bet.odd_guess from Game inner join Game_on_Bet on Game_on_Bet.idGame = Game.idGame where Game_on_Bet.idBet = "+idBet+" and Game.state != 'active';";
        ResultSet res = querySQL(sql);
        List<Float> l = new ArrayList<>();
        try{
            while(res.next()){
                float f = res.getFloat(1);
                float possible_earns = get_possible_earnings(idBet);
                possible_earns /= f;
                update_possible_earnings(idBet, possible_earns);
            }
        }
        catch (Exception e){}
        super.closeConn();
        remove_gamesNotActive(idBet);
    }

    public float getOddGuessGame(int idBet, String idGame){
        super.startConnStmt();
        String sql = "select odd_guess from Game_on_Bet where idBet = "+idBet+" and idGame = '"+idGame+"';";
        ResultSet res = super.querySQL(sql);
        float odd = super.resultSetGetFirstFloat(res, -1);
        super.closeConn();
        return odd;
    }


    public List<String> getGamesOfBet(int idBet){
        List<String> games = new ArrayList<>();
        super.startConnStmt();
        String sql = "select idGame from Game_on_Bet where idBet = "+idBet+";";
        ResultSet res = super.querySQL(sql);

        try{
            while (res.next()){
                String game = res.getString(1);
                games.add(game);
            }
        }catch (Exception e){
            System.out.println("There are no games registered in this bet");
        }
        super.closeConn();
        return games;
    }



}
