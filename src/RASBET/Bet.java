package RASBET.RASBET;

import java.util.List;

public class Bet {
    private int idBet;
    private float money;
    private float possible_earnings;
    private String email;
    private String state;

    final private String NOT_SET = "NOT_SET";
    final private String SET = "SET";
    final private String CONCLUDED = "CONCLUDED";


    private static int checked = 1;
    private static int not_checked = 0;



    //Constructor used for data that is loaded from the database:
    public Bet(int idBet, float money, float possible_earnings, String email, String state){
        this.idBet = idBet;
        this.money = money;
        this.possible_earnings = possible_earnings;
        this.email = email;
        this.state= state;
    }
    public Bet(){

    }


    public void addGame(int id_game){

    }

    public String toString(){
        return this.idBet+"/"+this.money+"/"+this.possible_earnings+"/"+this.email+"/"+this.state+".\n";
    }

    public static String noset(){
        Bet b = new Bet();
        String val =  b.NOT_SET;
        b = null;
        return val;
    }

    public static String set(){
        Bet b = new Bet();
        String val =  b.SET;
        b = null;
        return val;
    }

    public static String concluded(){
        Bet b = new Bet();
        String val =  b.CONCLUDED;
        b = null;
        return val;
    }

    public static int getChecked(){
        return checked;
    }
    public static int getNot_checked(){
        return not_checked;
    }
}
