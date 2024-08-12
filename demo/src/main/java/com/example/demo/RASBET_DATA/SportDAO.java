package com.example.demo.RASBET_DATA;

import com.example.demo.RASBET_LN.Sport.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SportDAO extends DAO{

    public SportDAO(){
        super();
    }

    /**
     * Function that receives the name of a sport and returns its id in the db.
     * If the sport doesn't exist, -1 will be returned.
     * @param sport String
     * @return int: Sport Id
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
     * Function that receives the id of the sport, and return its type.
     * The type can be collective, individual, and "" if the sport is not found.
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
     * Function that given the id of a sport checks if it is a sport that can have a draw.
     * If it can have draw as result, it returns true. Else returns false.
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
     * Function that returns the number of participants that are usually presented in an event of the sport id.
     * It returns 0 if the sport id is not found.
     * @param id int
     * @return int
     */
    public int getSportNParticipants(int id){
        super.startConnStmt();
        String sql = "select n_participant from Sport where idSport = '" + id + "';";
        ResultSet result = querySQL(sql);
        int nPart = super.resultSetGetFirstInt(result, 0);
        super.closeConn();
        return nPart;
    }

    /**
     * Function that returns the name of all the sports that exist in the database.
     * If it doesn't exist any sport, it will be return an empty list.
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
     * Function that adds a new Sport into the database.
     * @param sport String
     * @param type String
     * @param draw int
     * @param n_part int
     */
    public void addSport(String sport, String type, int draw, int n_part){
        super.startConnStmt();
        String sql = "insert into Sport (sport, type, draw, n_participant) values ('"+sport+"','"+type+"',"+draw+","+n_part+");";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that allows to get all the games of a certain sport.
     * It returns an empty list if no games are found.
     * @param idSport int
     * @return List<Game>
     */
    public List<Game> getGamesOfSport(int idSport){
        super.startConnStmt();

        String npart = "select n_participant from Sport where idSport = "+idSport+";"; // Discovers the number of participants of a certain sport
        ResultSet resNPart = querySQL(npart);
        int np = super.resultSetGetFirstInt(resNPart, 0);

        String sql = "select * from Game where id_sport = " + idSport + " and (state = \"active\" or state = \"suspended\");";
        ResultSet result = querySQL(sql);       // Gets all the games that are open or suspended

        List<Game> l = new ArrayList<>();       // Adds all the games of a certain sport (idGame, id_sport, state, outcome, date)
        try {
            while (result.next()){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateGame = LocalDateTime.parse(result.getString(5),formatter);

                Game g = new Game(result.getString(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        dateGame);
                g.addDraw(result.getFloat(6));    //If it is null, it is turn to 0

                l.add(g);
            }
        }catch (SQLException exc){
            System.out.println(exc.getMessage());
        }

        // No games, no participants "collected"
        for (Game g: l){                        //For all the games selected, it determines their participants
            for (int i = 0; i < np; i++){       //It gets the number of participants expected for a certain sport
                String part = "select * from Participant where idGame = '"+ g.getIdGame() +"' and idx = "+(i+1)+";";
                ResultSet resPart = querySQL(part);
                try{
                    if(resPart.next()) {        //Name of the participant, and odd of "it"
                        g.addParticipant(resPart.getString(3), resPart.getFloat(4));
                    }
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
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
     * Function that verifies if a certain Game idGame exists.
     * It returns true, if the game exists, otherwise it is return false.
     * @param idGame String
     * @return boolean
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
     * Function that returns true if the idx participant exists
     * @param idGame String
     * @param participant int
     * @return
     */
    public boolean existsParticipantInGame(String idGame, String participant){
        super.startConnStmt();
        String sql = "select * from Participant where idGame = '"+idGame+"' and idx = "+participant+";";
        ResultSet query_res = querySQL(sql);
        boolean ret = super.resultSetIsNotEmpty(query_res);

        super.closeConn();
        return ret;
    }





    /**
     * Function that adds an object game to the database.
     * @param g Game
     */
    public void addGame(Game g){
        super.startConnStmt();
        String date = Game.parseDate(g.getDate());
        String addGame = "insert into Game VALUES ('" + g.getIdGame() + "', " + g.getIdSport() + ", '" + g.getState() + "', '" + g.getOutcome() + "','"+date+ "',"+ g.getX()+", "+0+")";
        executeSQL(addGame);

        int i = 0;
        for(Map.Entry<String, Float> m: g.getParticipantsOdds().entrySet()){
            String sql = "insert into Participant (idGame, name, odd, idx) values ('"+g.getIdGame()+"', '"+m.getKey()+"',"+m.getValue()+","+(i+1)+");";
            executeSQL(sql);
            i++;
        }
        super.closeConn();
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
     * Function that updates the result of a game id.
     * The parameter result should be: X, V{1..n_part}.
     * @param idGame String
     * @param result String
     */
    public void changeOutcomeGame(String idGame, String result){
        super.startConnStmt();
        String sql = "update Game set outcome = '"+result+"' where idGame = '"+idGame+"';";
        executeSQL(sql);
        super.closeConn();
    }

    /**
     * Function that gets the outcome X,V{1..n_part} of a certain game idGame.
     * If the game doesn't exist the output will be "".
     * If the outcome is not set, its value will be null.
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
     * Function that allows to get the id of the sport which a game belongs.
     * If the idGame doesn't exist, it returns -1.
     * @param idGame String
     * @return int
     */
    public int getSportGame(String idGame){
        super.startConnStmt();
        String sql = "select id_sport from Game where idGame = '"+idGame+"';";
        ResultSet res = super.querySQL(sql);
        int sport = super.resultSetGetFirstInt(res, -1);
        super.closeConn();
        return sport;
    }

    /*---------------------------------------------------------------------*/


    public void changeOdd(String idGame, String participant, float newOdd, boolean specialist){
        super.startConnStmt();
        String sql;
        if(participant.equals("X")){
            sql = "update Game set "+participant+" = "+newOdd+" where idGame = '"+idGame+"';";
            executeSQL(sql);
            if (specialist){
                sql = "update Game set spec_X = 1 where idGame = '"+idGame+"';";
                executeSQL(sql);
            }
        }
        else{
            sql = "update Participant set odd = "+newOdd+" where idGame = '"+idGame+"' and idx = "+participant+";";
            executeSQL(sql);
            if (specialist){
                sql = "update Participant set spec_odd = 1 where idGame = '"+idGame+"' and idx = "+participant+";";
                executeSQL(sql);
            }
        }
        super.closeConn();
    }

    /**
     * Attention result!
     * Function that gets the odd of a certain result of the game idGame. String result should be checked before. Possible values are V1,X,V2. it returns the odd (float).
     * If the game doesn't exist, the odd returned will be 0.
     * @param idGame String
     * @param result String
     * @return float
     */
    public float getOddGame(String idGame, String result){
        super.startConnStmt();
        float odd = 0;
        if(result.equals("X")) {
            String sql = "select " + result + " from Game where idGame = '" + idGame + "';";
            ResultSet res = querySQL(sql);
            odd = super.resultSetGetFirstFloat(res, 0);
        }
        else{
            String sql = "select odd from Participant  where idGame = '"+idGame+"' and idx = "+result+";";
            ResultSet res = querySQL(sql);
            odd = super.resultSetGetFirstFloat(res, 0);
        }
        super.closeConn();
        return odd;
    }

    /**
     * Function that checks if given an outcome(result) had already been changed by a specialist
     * @param idGame String
     * @param result String
     * @return boolean
     */
    public boolean checkOddResultGame(String idGame, String result){
        super.startConnStmt();
        String sql;
        boolean b = false;
        if(result.equals("X")){
            sql = "select spec_X from Game where idGame = '"+idGame+"';";
            ResultSet res = querySQL(sql);
            int check = super.resultSetGetFirstInt(res, 0);
            b = (check == 1) ? (b = true) : (b = false);
        }
        else{
            sql = "select spec_odd from Participant where idGame = '"+idGame+"' and idx = "+result+";";
            ResultSet res = querySQL(sql);
            int check = super.resultSetGetFirstInt(res, 0);
            b = (check == 1) ? (b = true) : (b = false);
        }
        super.closeConn();
        return b;
    }


    public String getParticipantName(String idGame, int idx){
        super.startConnStmt();
        String sql = "select name from Participant where idGame = '"+idGame+"' and idx = "+idx+";";
        ResultSet res = super.querySQL(sql);
        String name = resultSetGetFirstString(res);
        super.closeConn();
        return name;
    }

    public List<String> getParticipants(String idGame){
        super.startConnStmt();
        String sql = "select name from Participant where idGame = '"+idGame+"';";
        ResultSet res = querySQL(sql);
        List<String> names = new ArrayList<>();
        try {
            while (res.next()) {
                names.add(res.getString(1));
            }
        }
        catch (Exception e){System.out.println(e.getMessage());}
        return names;
    }

    public int getIdxParticipant(String part){
        super.startConnStmt();
        String sql = "select idx from Participant where name = '"+part+"';";
        ResultSet res = querySQL(sql);
        int idx = super.resultSetGetFirstInt(res, -1);
        super.closeConn();
        return idx;
    }


    // -------------------------------------------

    // Começar a seguir
    // Deve haver uma verificação se o idUser e o idGame existe
    public boolean startFollowingGame(int idUser, String idGame){
        super.startConnStmt();
        String sql = "insert into Game_Followers values ('"+idGame+"',"+idUser+");";
        boolean b = super.executeSQL(sql);
        super.closeConn();
        return b;
    }

    // Deixar de seguir
    public boolean stopFollowingGame(int idUser, String idGame){
        super.startConnStmt();
        String sql = "delete from Game_Followers where idGame = '"+idGame+"' and idUser = "+idUser+";";
        boolean b = super.executeSQL(sql);
        super.closeConn();
        return b;
    }

    // Listar todos os seguidores
    public List<String> getEmailFollowers(String idGame){
        super.startConnStmt();
        String sql = "select User.email from User inner join Game_Followers on User.id = Game_Followers.idUser where Game_Followers.idGame = '"+idGame+"';";
        ResultSet result = super.querySQL(sql);

        List<String> emails = new ArrayList<>();
        try{
            while (result.next()){
                String email = result.getString(1);
                emails.add(email);
            }
        }catch (Exception e){
            System.out.println("There are no followers for this game or game_followers not found.");
        }
        return emails;
    }

}