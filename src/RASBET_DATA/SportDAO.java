package RASBET.RASBET_DATA;

import RASBET.RASBET.Game;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SportDAO extends DAO{

    public SportDAO(){
        super();
    }

    /**
     * Function that receives the name of a sport and returns its id in the db. If the sport doesn't exist, -1 will be returned.
     * @param sport String
     * @return int
     */
    public int getSportId(String sport){
        super.startConnStmt();
        String sql = "select idSport from Sport where sport = '" + sport + "';";
        ResultSet result = querySQL(sql);
        int s = super.resultSetGetFirstInt(result, -1);
        super.closeConn();
        return s;
    }

    /**
     * Function that receives the id of a game, and returns its name. It the sport doesn't exist, it will be sent ""
     * @param id int
     * @return String
     */
    public String getSportName(int id){
        super.startConnStmt();
        String sql = "select sport from Sport where idSport = '" + id + "';";
        ResultSet result = querySQL(sql);
        String s = super.resultSetGetFirstString(result);
        super.closeConn();
        return s;
    }

    /**
     * Function that receives the id of the sport, and return its type. The type can be collective, individual, and "" if the sport is not found.
     * @param id int
     * @return String
     */
    public String getSportType(int id){
        super.startConnStmt();
        String sql = "select type from Sport where idSport = '" + id + "';";
        ResultSet result = querySQL(sql);
        String s = super.resultSetGetFirstString(result);
        super.closeConn();
        return s;
    }


    /**
     * Function that given the id of a sport checks if it is a sport that can have a draw. If it can have draw as result, it returns true. Else returns false.
     * @param id int
     * @return boolean
     */
    public boolean getSportDraw(int id){
        super.startConnStmt();
        String sql = "select draw from Sport where idSport = '" + id + "';";
        ResultSet result = querySQL(sql);

        boolean b = false;
        try {
            result.next();
            if (result.getInt(1) == 1) b = true;
        }catch (SQLException exc){
            System.out.println(exc.getMessage());
        }
        super.closeConn();
        return b;
    }

    /**
     * Function that returns the name of all the sports that exist in the database. If it doesn't existe any sport, it will be return an empty list.
     * @return List<String>
     */
    public List<String> getNameOfAllSports(){
        super.startConnStmt();
        String sql = "select sport from Sport;";
        ResultSet result = querySQL(sql);

        List<String> nameSports = new ArrayList<>();
        try{
            while(result.next()){
                nameSports.add(result.getString(1));
            }
        }catch (SQLException s){
            System.out.println("Error: impossible to analise the ResultSet");
        }
        super.closeConn();
        return nameSports;
    }

    /**
     * Function that gets the list of all the games of a given sport that are open or suspended.
     * If no game is open or suspended, it will return the empty list.
     * @param idSport int
     * @return List<Game>
     */
    public List<Game> getGamesOfSport(int idSport){
        super.startConnStmt();
        String sql = "select * from Game where id_sport = '" + idSport + "' and state = \"active\" or state = \"suspended\";";
        ResultSet result = querySQL(sql);

        List<Game> l = new ArrayList<>();
        try {
            while (result.next()){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //YYYY-MM-DD hh:mm:ss = result.getString(10)
                LocalDateTime dateGame = LocalDateTime.parse(result.getString(10),formatter);

                Game g = new Game(result.getString(1), result.getInt(2), result.getString(3), result.getString(4), result.getFloat(5), result.getFloat(6), result.getFloat(7), result.getString(8), result.getString(9), dateGame);
                l.add(g);
            }
        }catch (SQLException exc){
            System.out.println(exc.getMessage());
        }

        super.closeConn();
        return l;
    }

    /*
     * -----------------------------------------------------------
     *                      Game Functions:
     * -----------------------------------------------------------
     */

    /**
     * Function that adds an object game to the database.
     * @param g Game
     */
    public void addGame(Game g){
        super.startConnStmt();
        String date = Game.parseDate(g.getDate());
        String addGame = "insert into Game VALUES ('" + g.getIdGame() + "', " + g.getId_Sport() + ", '" + g.getState() + "', '" + g.getHome() + "', '" + g.getV1() + "', '" + g.getX() + "', '" + g.getV2() + "', '" + g.getAway() + "', '" + g.getOutcome() + "','"+date+"')";
        executeSQL(addGame);
        super.closeConn();
    }

    /**
     * Function that verifies if a certain Game idGame exists.
     * It returns true, if the game exists, otherwise it is return false.
     * @param idGame
     * @return
     */
    public boolean existsGame(String idGame){
        super.startConnStmt();
        String sql = "select * from Game where idGame = '"+idGame+"';";
        ResultSet res = querySQL(sql);
        boolean b = super.resultSetIsNotEmpty(res);
        super.closeConn();
        return b;
    }

    /**
     * Function that makes sure that the games currently in a bet (that as not been finished) are all active and the user can bet on those games.
     * It returns false, if any of those games is not active. Otherwise, it returns true.
     * @param idBet int
     * @return boolean
     */
    public boolean checkAllGamesActive(int idBet){
        super.startConnStmt();
        String sql = "select Game.state from Game inner join Game_on_Bet on Game_on_Bet.idGame = Game.idGame where Game_on_Bet.idBet = "+idBet+";";
        ResultSet res = querySQL(sql);
        try {
            while (res.next()){
                if(!res.getString(1).equals("active")) return false;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.closeConn();
        return true;
    }

    /**
     * Function that can change the odd of a certain result of a given game. The result values should be checked.
     * @param idGame String
     * @param newOdd float
     * @param result String
     */
    public void changeOddGame(String idGame, float newOdd, String result){
        super.startConnStmt();
        String sql = "update Game set "+result+" = "+newOdd+" where idGame = '"+idGame+"';";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that gets the odd of a certain result of the game idGame. String result should be checked before. Possible values are V1,X,V2. it returns the odd (float).
     * If the game doesn't exist, the odd returned will be 0.
     * @param idGame String
     * @param result String
     * @return float
     */
    public float getOddGame(String idGame, String result){
        super.startConnStmt();
        String sql = "select "+result+" from Game where idGame = '"+idGame+"';";
        ResultSet res = querySQL(sql);
        float odd = super.resultSetGetFirstFloat(res, 0);
        super.closeConn();
        return odd;
    }

    /**
     * Function that allows to update the state of a game id. The state passed should be checked.
     * @param id String
     * @param state String
     */
    public void changeStateGame(String id, String state){
        super.startConnStmt();
        String sql = "update Game set state = '"+state+"' where idGame = '"+id+"';";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that gets the current state of a game id. It returns "" if the game is not found.
     * @param id String
     * @return String
     */
    public String getStateGame(String id){
        startConnStmt();
        String sql = "select state from Game where idGame = '"+id+"';";
        ResultSet r = querySQL(sql);
        String output = super.resultSetGetFirstString(r);
        super.closeConn();
        return output;
    }

    /**
     * Function that updates the result of a game id. The parameter result should be one of three: V1, X, V2.
     * @param idGame String
     * @param result String
     */
    public boolean changeOutcomeGame(String idGame, String result){
        super.startConnStmt();
        String sql = "update Game set outcome = '"+result+"' where idGame = '"+idGame+"';";
        executeSQL(sql);
        super.closeConn();
        return true;
    }

    /**
     * Function that gets the outcome (V1,X,V2) of a certain game idGame. If the game doesn't exist the output will be "". If the outcome is not set, its value will be null.
     * @param idGame String
     * @return String
     */
    public String getOutcomeGame(String idGame){
        super.startConnStmt();
        String sql = "select outcome from Game where idGame = '"+idGame+"';";
        ResultSet res = querySQL(sql);
        String outcome = super.resultSetGetFirstString(res);
        super.closeConn();
        return outcome;
    }

    /**
     * Function that allows to get the sport which a game belongs
     * @param idGame String
     * @return String
     */
    public String getSportGame(String idGame){
        super.startConnStmt();
        String sql = "select id_sport from Game where idGame = '"+idGame+"';";
        ResultSet res = super.querySQL(sql);
        String sport = super.resultSetGetFirstString(res);
        super.closeConn();
        return sport;
    }
}