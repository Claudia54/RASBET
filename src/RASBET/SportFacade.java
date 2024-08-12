package RASBET.RASBET;

import RASBET.RASBET_DATA.SportDAO;

import java.util.ArrayList;
import java.util.List;

public class SportFacade {
    private SportDAO sportdao; //functions access database related to Sport

    public SportFacade(){
        this.sportdao = new SportDAO();
    }

    /**
     * Function that gets all the open games of a sport.
     * It returns an empty array if the sportname doesn't exist.
     * @param sportname String
     * @return List<String>
     */
    public List<String> get_games_sport(String sportname) {
        List<String> gamesStrings = new ArrayList<>();

        int sportId = this.sportdao.getSportId(sportname);                   //Get the id of the sport
        if(sportId == -1){                                              //if the sport doesn't exist
            System.out.println("Sport "+sportname+" doesn't exist.");
            return gamesStrings;
        }

        List<Game> listgames = this.sportdao.getGamesOfSport(sportId);       //Gets all the open games of a sport
        for(Game g: listgames){
            gamesStrings.add(g.toString());
        }
        return gamesStrings;
    }

    /**
     * Function that gets the name of all the sports.
     * It returns an empty list if no sports are in the database
     * @return List<String>
     */
    public List<String> getNameSports() {
        List<String> gameNames = this.sportdao.getNameOfAllSports();
        return gameNames;
    }

    /**
     * Function that changes the odd of a certain result of game idGame.
     * If the game doesn't exist, the game is not open, or odd is not positive,
     * exceptions are thrown.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddHomeTeamGame(String idGame, float newOdd) throws Exception {
        if(!this.sportdao.existsGame(idGame)){
            throw new Exception("Game \""+idGame+"\" doesn't exist.");
        }
        if(!this.sportdao.getStateGame(idGame).equals(Game.open())){
            throw new Exception("Game \""+idGame+"\" is not open.");
        }
        if(newOdd <= 0){
            throw new Exception("Odd should be positive.");
        }
        this.sportdao.changeStateGame(idGame, Game.suspended());
        this.sportdao.changeOddGame(idGame, newOdd, Game.homeOutcome());
        this.sportdao.changeStateGame(idGame, Game.open());
    }

    /**
     * Function that changes the odd of a certain result of game idGame.
     * If the game doesn't exist, the game is not open, or odd is not positive,
     * exceptions are thrown.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddAwayTeamGame(String idGame, float newOdd) throws Exception {
        if(!this.sportdao.existsGame(idGame)){
            throw new Exception("Game \""+idGame+"\" doesn't exist.");
        }
        if(!this.sportdao.getStateGame(idGame).equals(Game.open())){
            throw new Exception("Game \""+idGame+"\" is not open.");
        }
        if(newOdd <= 0){
            throw new Exception("Odd should be positive.");
        }
        this.sportdao.changeStateGame(idGame, Game.suspended());
        this.sportdao.changeOddGame(idGame, newOdd, Game.awayOutcome());
        this.sportdao.changeStateGame(idGame, Game.open());
    }

    /**
     * Function that changes the odd of a certain result of game idGame.
     * If the game doesn't exist, the game is not open, or odd is not positive or
     * the sport doesn't allow draws, exceptions are thrown.
     * @param idGame String
     * @param newOdd float
     * @throws Exception
     */
    public void changeOddDrawGame(String idGame, float newOdd) throws Exception {
        if(!this.sportdao.existsGame(idGame)){
            throw new Exception("Game \""+idGame+"\" doesn't exist.");
        }
        String sport = this.sportdao.getSportGame(idGame);
        int id = this.sportdao.getSportId(sport);
        if(!this.sportdao.getSportDraw(id)){
            throw new Exception("\""+idGame+"\" doesn't have a draw state.");
        }
        if(!this.sportdao.getStateGame(idGame).equals(Game.open())){
            throw new Exception("Game \""+idGame+"\" is not open.");
        }
        if(newOdd <= 0){
            throw new Exception("Odd should be positive.");
        }
        this.sportdao.changeStateGame(idGame, Game.suspended());
        this.sportdao.changeOddGame(idGame, newOdd, Game.drawOutcome());
        this.sportdao.changeStateGame(idGame, Game.open());
    }

    /**
     * This function changes the state of a game to close.
     * If the game is closed, or it doesn't exist, an exception is sent.
     * @param idGame String
     * @throws Exception
     */
    public void closeGame(String idGame) throws Exception{
        if(!this.sportdao.existsGame(idGame))
            throw new Exception("Game \""+idGame+"\" not found.");
        if(this.sportdao.getStateGame(idGame).equals(Game.closed()))
            throw new ExceptionClosed();
        else this.sportdao.changeStateGame(idGame, Game.closed());
    }

    /**
     * Function that changes the state of a game to open.
     * If the function is closed, already open or the game is not found.
     * @param idGame String
     * @throws Exception
     */
    public void openGame(String idGame) throws Exception{
        if(!this.sportdao.existsGame(idGame))
            throw new Exception("Game \""+idGame+"\" not found.");
        if(this.sportdao.getStateGame(idGame).equals(Game.closed()))
            throw new ExceptionClosed();
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
        if(!this.sportdao.existsGame(idGame))
            throw new Exception("Game \""+idGame+"\" not found.");
        if(this.sportdao.getStateGame(idGame).equals(Game.closed()))
            throw new ExceptionClosed();
        if(this.sportdao.getStateGame(idGame).equals(Game.suspended()))
            throw new Exception("Game is already open");
        else this.sportdao.changeStateGame(idGame, Game.suspended());
    }







    public boolean checkGameOpen(String idGame) throws Exception{
        if(!this.sportdao.existsGame(idGame))
            throw new Exception("Game \""+idGame+"\" doesn't exist.");

        if(this.sportdao.getStateGame(idGame).equals(Game.open()))
            return true;
        else return false;
    }

    public boolean checkDraw(String idGame, String guess) throws Exception{
        if(!this.sportdao.existsGame(idGame))                           // Checks if the game exists
            throw new Exception("Game \""+idGame+"\" doesn't exist.");

        if(!guess.equals(Game.drawOutcome()))                           //Checks if the outcome guessed is draw, otherwise returns true
            return true;
        String sport = this.sportdao.getSportGame(idGame);              //Gets the sport of this game
        int id = this.sportdao.getSportId(sport);
        if(this.sportdao.getSportDraw(id))                              //If the game can have draws returns true
            return true;
        else return false;                                              //Otherwise returns false
    }

    public float getOddOutcome(String idGame, String outcome){
        if(outcome.equals(Game.drawOutcome()) || outcome.equals(Game.drawOutcome()) || outcome.equals(Game.drawOutcome()))
            return this.sportdao.getOddGame(idGame, outcome);
        else return -1;
    }


    public boolean addFinalResultGame(String idGame, String result){
        if(!this.sportdao.existsGame(idGame))                               //Se jogo não existir
            return false;

        if(result.equals(Game.drawOutcome())){                              //Se res for empate e desporto não aceitar empate
            String sport = this.sportdao.getSportGame(idGame);
            int id = this.sportdao.getSportId(sport);
            if(!this.sportdao.getSportDraw(id)){
                return false;
            }
        }
        return sportdao.changeOutcomeGame(idGame, result);
    }

    public boolean addResultGame(String idGame, String result){
        String res = Game.parseResult(result);
        if(!this.sportdao.existsGame(idGame) || res.equals(""))               //Se jogo não existir
            return false;

        /*if(result.equals(Game.drawOutcome())){                              //Se res for empate e desporto não aceitar empate
            String sport = this.sportdao.getSportGame(idGame);
            int id = this.sportdao.getSportId(sport);
            if(!this.sportdao.getSportDraw(id)){
                return false;
            }
        }*/
        return sportdao.changeOutcomeGame(idGame, result);
    }

    public boolean checkAllGamesActive(int idBet) {
        return this.sportdao.checkAllGamesActive(idBet); //verifica se todos os jogos estão ativos
    }


}
