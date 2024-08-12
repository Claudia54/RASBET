package RASBET.RASBET;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {
    final public String ACT = "active";
    final public String SUS = "suspended";
    final public String CLO = "closed";
    final private String OUT_V1 = "V1";
    final private String OUT_X = "X";
    final private String OUT_V2 = "V2";

    private String idGame;
    private int id_Sport;
    private String state;
    private String home;
    private float V1;
    private float X;
    private float V2;
    private String away;
    private String outcome;

    //In the BD must be = 'YYYY-MM-DD hh:mm:ss
    private LocalDateTime date;

    public Game(){

    }

    public Game(String idGame, int id_Sport, String home, float V1, float X, float V2, String away, LocalDateTime date){
        this.idGame = idGame;
        this.id_Sport = id_Sport;
        this.state = this.ACT;
        this.home = home;
        this.V1 = V1;
        this.X = X;
        this.V2 = V2;
        this.away = away;
        this.outcome = null;
        this.date = date;
    }

    public Game(String idGame, int id_Sport, String state, String home, float V1, float X, float V2, String away, String outcome, LocalDateTime date){
        this.idGame = idGame;
        this.id_Sport = id_Sport;
        this.state = state;
        this.home = home;
        this.V1 = V1;
        this.X = X;
        this.V2 = V2;
        this.away = away;
        this.outcome = outcome;
        this.date = date;
    }

    public String getIdGame(){
        return this.idGame;
    }

    public int getId_Sport(){
        return this.id_Sport;
    }

    public String getState(){
        return this.state;
    }

    public String getHome(){
        return this.home;
    }

    public float getV1(){
        return this.V1;
    }

    public float getX(){
        return this.X;
    }

    public float getV2(){
        return this.V2;
    }

    public String getAway(){
        return this.away;
    }

    public String getOutcome(){
        return this.outcome;
    }

    public LocalDateTime getDate(){return this.date;}

    public static String closed(){
        Game g = new Game();
        String s = g.CLO;
        g = null;
        return s;
    }

    public static String open(){
        Game g = new Game();
        String s = g.ACT;
        g = null;
        return s;
    }

    public static String suspended(){
        Game g = new Game();
        String s = g.SUS;
        g = null;
        return s;
    }

    public static String homeOutcome(){
        Game g = new Game();
        String s = g.OUT_V1;
        g = null;
        return s;
    }

    public static String drawOutcome(){
        Game g = new Game();
        String s = g.OUT_X;
        g = null;
        return s;
    }

    public static String awayOutcome(){
        Game g = new Game();
        String s = g.OUT_V2;
        g = null;
        return s;
    }

    public String toString(){
        String date = Game.parseDate(this.getDate());
        return this.getIdGame() + " | " +this.getId_Sport() +" | " + this.getState()+" | "+this.getHome()+" | "+this.getV1()+" | "+this.getX()+" | "+this.getV2()+" | "+this.getAway()+" | "+this.getOutcome()+" | "+date;

    }

    public static String parseResult(String result){
        //Parse results
        String[] tokens = result.split("x");

        int home = Integer.parseInt(tokens[0]);
        int away = Integer.parseInt(tokens[1]);

        String outcome = "";

        //Change outcome in the database:
        if(home == away){
            //Draw
            outcome = Game.drawOutcome();
        }
        else if (home > away){
            //V1
            outcome = Game.homeOutcome();
        }
        else if(home < away){
            //V2
            outcome = Game.awayOutcome();
        }
        return outcome;
    }

    public static String parseDate(LocalDateTime date){
        DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateDB = formatterDB.format(date);
        return dateDB;
    }
}
