package com.example.demo.RASBET_LN.Sport;

import com.example.demo.RASBET_DATA.SportDAO;
import com.example.demo.RASBET_LN.Email;
import com.example.demo.RASBET_LN.ReadJSON;
import com.mysql.cj.exceptions.StreamingNotifiable;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SportFacade {
    private SportDAO sportdao; //functions access database related to Sport

    public SportFacade(){
        this.sportdao = new SportDAO();
    }

    /**
     * Function that allows to get the id from a sport.
     * If the sport doesn't exist, -1 is returned.
     * @param nameSport String
     * @return int
     */
    public int getSportId(String nameSport){
        return this.sportdao.getSportId(nameSport);
    }

    /**
     * Function that gets all the open games of a sport.
     * It returns an empty array if the sportname doesn't exist.
     * It returns an empty array if the sport has no games
     * @param sportname String
     * @return List<Game>
     */
    public List<List<Map<String,String>>> get_games_sport(String sportname) {
        int sportId = getSportId(sportname);                     //Get the id of the sport
        if(sportId == -1){                                       //if the sport doesn't exist
            System.out.println("Sport "+sportname+" doesn't exist.");
            return new ArrayList<>();
        }

        List<List<Map<String,String>>> ret = new ArrayList<>();
        List<Game> listgames = this.sportdao.getGamesOfSport(sportId);  //Gets all the open games of a sport
        for (Game g : listgames){                                       //If there is no games the list will be empty
            System.out.println(g.toString());
            ret.add(g.returnMap());
        }
        return ret;
    }

    /**
     * Function that gets the name of all the sports.
     * It returns an empty list if no sports are in the database.
     * @return List<String>
     */
    public List<String> getNameSports() {
        List<String> gameNames = this.sportdao.getNameOfAllSports();
        return gameNames;
    }

    /**
     * Function that gets all the open games.
     * It returns an empty array if there aren't any open games
     * @return List<List<Map<String,String>>>
     */
    public List<List<Map<String,String>>> get_all_games() {
        List<String> l = getNameSports();           // Get the name of all sports

        List<List<Map<String,String>>> ret =
                new ArrayList<>();

        List<List<Map<String,String>>> aux;
        for (String sport: l){
            aux = get_games_sport(sport);    //Gets all the games of a certain sport
            ret.addAll(aux);
        }
        return ret;
    }

    /**
     * Function that verifies if a game exists in the database.
     * It returns true if the game exists, otherwise returns false.
     * @param idGame String
     * @return boolean
     */
    public boolean existsGame(String idGame){
        return this.sportdao.existsGame(idGame);
    }

    /**
     * Function that allows to change the odd of winning of a participant.
     * This function can be used to change the odd of a participant, or the odd of draw.
     * The param specialist indicates if the odd was changed by a specialist or by the API.
     * If the game/participant doesn't exist, the new odd is lower than 1 or the game is not open, it returns an exception.
     * If an odd is changed, all the followers of the Game will be notified
     * @param idGame String
     * @param participant int
     * @param newOdd float
     * @param specialist boolean
     * @throws Exception
     */
    public void changeOdd(String idGame, String participant, float newOdd, boolean specialist) throws Exception {
        if(!existsGame(idGame)){ // If the game doesn't exist,
            throw new Exception("Game \""+idGame+"\" doesn't exist.");
        }
        if(!participant.equals(Game.drawOutcome())){
            if(!this.sportdao.existsParticipantInGame(idGame, participant) ){
                throw new Exception("Participant idx: "+participant+" doesn't exist");
            }
        }
        if(!this.sportdao.getStateGame(idGame).equals(Game.open())){  // If the game is not open
            throw new Exception("Game \""+idGame+"\" is not open.");
        }
        if(newOdd < 1){  // If the odd is not superior to 1
            throw new Exception("Odd should be bigger or equal to 1.");
        }
        this.sportdao.changeStateGame(idGame, Game.suspended());
        this.sportdao.changeOdd(idGame, participant, newOdd, specialist);
        this.sportdao.changeStateGame(idGame, Game.open());

        // -------------
        notifyFollowers(idGame);
    }

    /**
     * Function that changes the odd of a certain result of game idGame.
     * If the game doesn't exist, the game is not open, or odd is not positive or
     * the sport doesn't allow draws, exceptions are thrown.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddDrawGame(String idGame, float newOdd, boolean specialist) throws Exception {
        /*if(!this.sportdao.existsGame(idGame)){
            throw new Exception("Game \""+idGame+"\" doesn't exist.");
        }
        int sport = this.sportdao.getSportGame(idGame);
        if(!this.sportdao.getSportDraw(sport)){
            throw new Exception("\""+idGame+"\" doesn't have a draw state.");
        }
        if(!this.sportdao.getStateGame(idGame).equals(Game.open())){
            throw new Exception("Game \""+idGame+"\" is not open.");
        }
        if(newOdd <= 0){
            throw new Exception("Odd should be positive.");
        }
        this.sportdao.changeStateGame(idGame, Game.suspended());
        this.sportdao.changeOdd(idGame, Game.drawOutcome(), newOdd, specialist);
        this.sportdao.changeStateGame(idGame, Game.open());
        // -------------
        notifyFollowers(idGame);*/
        int sport = this.sportdao.getSportGame(idGame);
        if(sport == -1){
            throw new Exception("Game \""+idGame+"\" doesn't exist.");
        }
        if(!this.sportdao.getSportDraw(sport)){
            throw new Exception("\""+idGame+"\" doesn't have a draw state.");
        }
        changeOdd(idGame, Game.drawOutcome(), newOdd, specialist);
    }

    /**
     * Function used to check if a game exists and if the game is not closed
     * @param idGame String
     * @return boolean
     * @throws Exception
     */
    public boolean check_state_closed(String idGame) throws Exception{
        if(!existsGame(idGame))
            throw new Exception("Game \""+idGame+"\" not found.");
        else if(this.sportdao.getStateGame(idGame).equals(Game.closed()))
            throw new ExceptionClosed();
        return true;
    }

    /**
     * Function that allows to check if the guess is Draw and if the Draw is a valid outcome for a certain sport.
     * It returns true if the guess is not draw, or if it is the game accepts draw
     * It throws an exception if the game is not found.
     * @param idGame String
     * @param guess String
     * @return boolean
     * @throws Exception
     */
    public boolean check_guess_draw(String idGame, String guess) throws Exception{
        if(!this.sportdao.existsGame(idGame))                           // Checks if the game exists
            throw new Exception("Game \""+idGame+"\" doesn't exist.");

        boolean ret;
        int sport = this.sportdao.getSportGame(idGame);                 //Gets the sport of this game
        if(!guess.equals(Game.drawOutcome()))                           //Checks if the outcome guessed is draw, otherwise returns true
            ret = true;
        else if(this.sportdao.getSportDraw(sport))                      //If the game can have draws returns true
            ret = true;
        else ret = false;                                               //Otherwise returns false
        return ret;
    }

    /**
     * This function changes the state of a game to close.
     * If the game is closed, it doesn't exist or it doesn't have an outcome yet, an exception is sent.
     * If the game doesn't allow final result to be draw, and the current outcome is draw, an exception is thrown
     * @param idGame String
     * @throws Exception
     */
    public void closeGame(String idGame) throws Exception{
        boolean ret = check_state_closed(idGame);
        if(this.sportdao.getOutcomeGame(idGame).equals("null"))
            throw new Exception("Game should have an outcome before be closed");
        if(!check_guess_draw(idGame, this.sportdao.getOutcomeGame(idGame))){
            throw new Exception("Current result is draw and this sport doesn't allow draws");
        }
        this.sportdao.changeStateGame(idGame, Game.closed());
    }

    /**
     * Function that changes the state of a game to open.
     * If the function is closed, already open or the game is not found.
     * @param idGame String
     * @throws Exception
     */
    public void openGame(String idGame) throws Exception{
        boolean ret = check_state_closed(idGame);
        if(this.sportdao.getStateGame(idGame).equals(Game.open()))
            throw new Exception("Game is already open");
        else this.sportdao.changeStateGame(idGame, Game.open());
    }

    /**
     * Function that suspends a game.
     * If the game is closed, already suspended, or doesn't exist, an exception is thrown.
     * @param idGame String
     * @throws Exception
     */
    public void suspendGame(String idGame) throws Exception{
        boolean ret = check_state_closed(idGame);
        if(this.sportdao.getStateGame(idGame).equals(Game.suspended()))
            throw new Exception("Game is already open");
        else this.sportdao.changeStateGame(idGame, Game.suspended());
    }

    /**
     * Function that checks if a certain game is open.
     * It returns true if the game is open.
     * It returns an exception if the game doesn't exist, or the game is not open.
     * @param idGame String
     * @return boolean
     * @throws Exception
     */
    public boolean checkGameOpen(String idGame) throws Exception{
        if(!existsGame(idGame))
            throw new Exception("Game \""+idGame+"\" doesn't exist.");
        if(!this.sportdao.getStateGame(idGame).equals(Game.open()))
            throw new Exception("Game \""+idGame+"\" isn't open.");
        return true;
    }

    /**
     * Function that allows the admin to add the result of the game idGame
     * This function can allow the result draw for sports without draws as it is an intermediate result
     * It throws an exception if the game is not open or if the game is not found
     * It throws an exception if the result passed is not valid
     * @param idGame String
     * @param result String
     * @return boolean
     * @throws Exception
     */
    public boolean addResultGame(String idGame, String result) throws Exception{
        String res = Game.parseResult2WithDraw(result); //Está incompleto => motogp
        System.out.println(res);
        if(res.equals("")){
            throw new Exception(" Result "+result+" not valid.");
        }

        if(!res.equals("X")){
            try {
                Integer.parseInt(res);
            }
            catch (NumberFormatException nfe){
                res = Integer.toString(this.sportdao.getIdxParticipant(res));
                if(res.equals("-1")){
                    throw new Exception("Participant not found");
                }
            }
        }

        try {
            checkGameOpen(idGame); // Checks if the game is open
        }
        catch (Exception e) {
            throw e;
        }

        this.sportdao.changeOutcomeGame(idGame, res); //Changes state of game
        // -------------
        notifyFollowers(idGame);

        return true;
    }

    /**
     * Function that allows to end a game, by adding the final result and closing the game
     * @param idGame String
     * @param result String
     * @return boolean
     * @throws Exception
     */
    public boolean endGame(String idGame, String result) throws Exception{
        String res = Game.parseResult2WithDraw(result);
        if (res.equals("")) {
            throw new Exception(" Result "+result+" not valid.");
        }

        check_state_closed(idGame); //Checks if a game exists and it is not closed

        if(result.equals(Game.drawOutcome())){     //Se res for empate e desporto não aceitar empate
            int sport = this.sportdao.getSportGame(idGame);
            if(!this.sportdao.getSportDraw(sport)){
                throw new Exception("Result is draw and this sport doesn't allow draws");
            }
        }

        sportdao.changeOutcomeGame(idGame, res);

        // -------------
        notifyFollowers(idGame);

        closeGame(idGame);

        return true;
    }

    /**
     * Function that allows to get the name of a certain participant based on the outcome.
     * Outcome equals the index of the participant or X (for Draw)
     * It throws an exception if game not found or outcome not valid
     * @param idGame String
     * @param outcome String
     * @return
     */
    public String getNameParticipant(String idGame, String outcome) throws Exception{

        if(!existsGame(idGame)){
            throw new Exception("Game \""+idGame+"\" not found.");
        }

        int sports = this.sportdao.getSportGame(idGame);
        if(outcome.equals(Game.drawOutcome())){
            return "X";
        }
        else if ((Integer.parseInt(outcome)) <= this.sportdao.getSportNParticipants(sports)){
            String part = this.sportdao.getParticipantName(idGame, Integer.parseInt(outcome));
            return part;
        }
        else // outcome > NPart of sport || not (Draw)
            throw new Exception("Outcome is not valid.");
    }

    /**
     * Function that allows to get the name of a certain Game
     * The name of the game is defined by the participants it has
     * If the game is not found, an "" is returned.
     * @param idGame String
     * @return String
     */
    public String getGameName(String idGame){
        String res = "";
        if(existsGame(idGame)){
            List <String> parts = this.sportdao.getParticipants(idGame);
            int nElem = parts.size(), i = 0;
            for (String p: parts){
                res += p;
                if(i < nElem - 1){
                    res+=" x ";
                }
                i++;
            }
        }
        return res;
    }

    /**
     * Function that allows to check if all the games in a bet are active.
     * It returns true if so, or false if not.
     * @param idBet int
     * @return boolean
     */
    public boolean checkAllGamesActive(int idBet) {
        return this.sportdao.checkAllGamesActive(idBet); //verifica se todos os jogos estão ativos
    }


    /*-------------------------------------------------------------*/

    /**
     * Function that allows to check if the outcome, when it is a number of a participant, is valid
     * Before this function is called, it should be checked that the game exist
     * @param outcome String
     * @param idGame String
     * @return boolean
     */
    public boolean valid_outcome_participant(String idGame, String outcome){
        int sports = this.sportdao.getSportGame(idGame);
        if((Integer.parseInt(outcome)) <= this.sportdao.getSportNParticipants(sports)){
            return true;
        }
        else return false;
    }

    /**
     * Function that gets the outcome odd given an idGame and an outcome if the outcome is valid.
     * Returns -1 if the outcome is not valid
     * @param idGame String
     * @param outcome String
     * @return float
     */
    public float getOddOutcome(String idGame, String outcome){
        if(outcome.equals(Game.drawOutcome()) || valid_outcome_participant(idGame,outcome))
            return this.sportdao.getOddGame(idGame, outcome);
        else return -1;
    }

    /**
     * Function that checks if the game identified by idGame is closed
     * @param idGame String
     * @return boolean
     */
    public boolean checkGameClosed(String idGame){
        if(this.sportdao.getStateGame(idGame).equals(Game.closed()))
            return true;
        else return false;
    }

    public void updateOutcomeGame(String idGame, Game g){
        if(!this.sportdao.getStateGame(idGame).equals(Game.closed())){
            this.sportdao.changeOutcomeGame(idGame, g.getOutcome());

            // -------------
            notifyFollowers(idGame);
        }
    }

    public String getOutcomeGame(String game){
        return this.sportdao.getOutcomeGame(game);
    }


    public void addGame(Game g){
        this.sportdao.addGame(g);
    }

    public void updateOddsGame(String idGame, Game newData, boolean specialist){
        int id = this.sportdao.getSportGame(idGame);
        int n_part = this.sportdao.getSportNParticipants(id);

        float X = getOddOutcome(idGame, Game.drawOutcome());
        try{
            if (newData.getX() != X && !checkOddBySpec(idGame, Game.drawOutcome())) {
                changeOddDrawGame(idGame, newData.getX(), specialist);
            }
        }
        catch (Exception e){}

        try {
            for (int i = 0; i < n_part; i++) {
                float odd = getOddOutcome(idGame, Integer.toString(i + 1));
                if (newData.retOddIdx(i) != odd && !checkOddBySpec(idGame, Integer.toString(i + 1))) {
                    changeOdd(idGame, Integer.toString(i + 1), newData.retOddIdx(i), specialist);
                }
            }
        }
        catch (Exception e){}

    }

    /**
     * Funciton that checks if a given
     * @param idGame
     * @param result
     * @return
     */
    public boolean checkOddBySpec(String idGame, String result){
        return this.sportdao.checkOddResultGame(idGame, result);
    }

    /**
     * Function that loads data of the football games of the api. It returns a list with all the games that ended
     * @return List<String>
     */
    public List<String> loadGamesFootball(){
        List<String> gamesEnded = new ArrayList<>();

        int idSport = getSportId("Football");
        ReadJSON novo = new ReadJSON();
        novo.getRequest();
        List<Game> games = novo.readFile(idSport);
        for (Game g: games) {
            String idGame = g.getIdGame();
            if (existsGame(idGame)) {                      //The state of the game is not the same as the one in the database
                if(g.isClosed()){                                           //Se já tiver terminado
                    if(!checkGameClosed(idGame)) {         //Mas na base de dados não...
                        try {
                            endGame(idGame, g.getOutcome());
                        }catch (Exception e){}
                        gamesEnded.add(idGame);
                    }
                }
                else{                                                       //Se ainda não tiver terminado,
                    if(!checkGameClosed(idGame)) {
                        updateOddsGame(idGame,g, false);
                        updateOutcomeGame(idGame, g);
                    }
                }
            }
            else {
                addGame(g);
            }
        }
        return gamesEnded;
    }

    public void newGame(String nameSport, String date, List<String> part){
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        ZonedDateTime dateGameZ = ZonedDateTime.parse(date);
        LocalDateTime dateGame = dateGameZ.toLocalDateTime();

        Game g = null;

        int idSport = getSportId(nameSport);
        if(idSport!=-1) {
            g = new Game("-1", idSport, Game.open(), "null", dateGame);
            if(this.sportdao.getSportDraw(idSport)){
                g.addDraw(0);
            }
            for(String p: part){
                g.addParticipant(p, 0);
            }
        }
        if(g!=null)
            this.sportdao.addGame(g);
    }



    //--------------------------------------------------------------------------------------------------

    /**
     * Function used so a user identified by idUser can start following a game identified by idGame
     * @param idUser String
     * @param idGame String
     * @return boolean
     */
    public boolean startFollowingGame(int idUser, String game){
        boolean b = this.sportdao.startFollowingGame(idUser,game);
        return b;
    }

    /**
     * Function used so a user identified by idUser can stop following a game identified by idGame
     * @param idUser String
     * @param idGame String
     * @return boolean
     */
    public boolean stopFollowing(int idUser, String idGame){
        boolean b = this.sportdao.stopFollowingGame(idUser, idGame);
        return b;
    }

    /**
     * Function that sends an email to all the users following a certain game identified by idGame to notify them something has changed
     * @param idGame String
     */
    public void notifyFollowers(String idGame){
        List<String> emails = this.sportdao.getEmailFollowers(idGame);
        String gameName = getGameName(idGame);
        for (String s: emails){
            Email.generateEmailFollowers(s,gameName);
        }
    }
}
