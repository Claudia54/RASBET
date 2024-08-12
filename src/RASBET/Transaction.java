package RASBET.RASBET;

class Wallet {

    //There are different types of transitions each user can made.
    //The types are:

    //Money that was deposit in the wallet
    public static String deposit = "deposit";

    //Money that was withdraw by the user
    public static String withdraw = "withdraw";

    //Money that was earn from a bet
    public static String won = "won";

    //Money that was betted
    public static String bet = "bet";


    //String builder para wallet
}

public class Transaction{
    private String type;
    private float amount;

    public Transaction(String type, float amount){
        this.type = type;
        this.amount = amount;
    }

    public String toString(){
        String output = "";
        if(this.type.equals(Wallet.deposit)||this.type.equals(Wallet.won)){
            output+= "| "+this.type+" | +"+this.amount+" |";
        }
        else{
            output+= "| "+this.type+" | -"+this.amount+" |";
        }
        return output;
    }
}
